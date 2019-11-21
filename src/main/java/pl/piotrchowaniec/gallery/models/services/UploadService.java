package pl.piotrchowaniec.gallery.models.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.piotrchowaniec.gallery.config.ApplicationProperties;
import pl.piotrchowaniec.gallery.models.Status;
import pl.piotrchowaniec.gallery.models.entities.UploadEntity;
import pl.piotrchowaniec.gallery.models.entities.UploadedFileEntity;
import pl.piotrchowaniec.gallery.models.forms.UploaderForm;
import pl.piotrchowaniec.gallery.models.repositories.UploadRepository;
import pl.piotrchowaniec.gallery.models.repositories.UploadedFilesRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UploadService {

    private final AmazonS3 amazonS3;
    private final UserSession userSession;
    private final UploadRepository uploadRepository;
    private final UploadedFilesRepository uploadedFilesRepository;

    private String bucketName;
    private List<File> filesToUpload;

    @Autowired
    public UploadService(AmazonS3 amazonS3, UserSession userSession, UploadRepository uploadRepository, UploadedFilesRepository uploadedFilesRepository, ApplicationProperties applicationProperties) {
        this.amazonS3 = amazonS3;
        this.userSession = userSession;
        this.uploadRepository = uploadRepository;
        this.uploadedFilesRepository = uploadedFilesRepository;
        this.bucketName = applicationProperties.getAwsServices().getBucketName();
    }

    public void cleanUploadList() {
        this.filesToUpload = new ArrayList<>();
    }

    public void addFileToUploadList(MultipartFile multipartFile) {
        File file = getFileFromMultipartFile(multipartFile);
        this.filesToUpload.add(file);
    }

    public boolean saveUpload(UploaderForm uploaderForm) {
        if (uploaderForm.getName().isEmpty()) {
            return false;
        } else {
            UploadEntity uploadEntity = setUploadEntity(uploaderForm);
            saveUploadEntity(uploadEntity);
            uploadFiles(uploadEntity);
            deleteTempFiles();
            return true;
        }
    }

    private void uploadFiles(UploadEntity uploadEntity) {
        for (File file : this.filesToUpload) {
            uploadFileAndAddToDB(file, uploadEntity);
        }
    }

    private void uploadFileAndAddToDB(File file, UploadEntity uploadEntity) {
        String folder = setS3BucketFolderName();
        uploadFileToS3Bucket(file, folder);
        saveUploadedFileEntity(getUploadedFileEntity(file, uploadEntity));
    }

    private void deleteTempFiles() {
        for (File file : this.filesToUpload) {
            file.delete();
        }
    }

    private String setS3BucketFolderName() {
        return userSession.getSessionId();
    }

    private String setFileKey(String filename, String folder) {
        return String.valueOf(folder).concat("/").concat(filename);
    }

    private void uploadFileToS3Bucket(File file, String folder) {
        String fileKey = setFileKey(file.getName(), folder);
        PutObjectRequest putObjectRequest = new PutObjectRequest(this.bucketName, fileKey, file);
        amazonS3.putObject(putObjectRequest);
        amazonS3.setObjectAcl(this.bucketName, fileKey, CannedAccessControlList.PublicRead);
    }

    private File getFileFromMultipartFile(MultipartFile multipartFile) {
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private UploadEntity setUploadEntity(UploaderForm uploaderForm) {
        UploadEntity uploadEntity = new UploadEntity();
        uploadEntity.setName(uploaderForm.getName());
        uploadEntity.setSurname(uploadEntity.getSurname());
        uploadEntity.setEmail(uploaderForm.getEmail());
        uploadEntity.setFolder(userSession.getSessionId());
        uploadEntity.setStatus(Status.NEW);
        uploadEntity.setUploadDate(userSession.getSessionIdGenerationTime());
        return uploadEntity;
    }

    private void saveUploadEntity(UploadEntity uploadEntity) {
        uploadRepository.save(uploadEntity);
    }

    private UploadedFileEntity getUploadedFileEntity(File file, UploadEntity uploadEntity) {
        String filename = file.getName();
        String folder = uploadEntity.getFolder();

        UploadedFileEntity uploadedFileEntity = new UploadedFileEntity();
        uploadedFileEntity.setFilename(filename);
        uploadedFileEntity.setPhotoUrl(getPhotoUrl(filename, folder));
        uploadedFileEntity.setUploadEntity(uploadEntity);
        return uploadedFileEntity;
    }

    private void saveUploadedFileEntity(UploadedFileEntity uploadedFileEntity) {
        uploadedFilesRepository.save(uploadedFileEntity);
    }

    private String getPhotoUrl(String filename, String folder) {
        String fileKey = folder.concat("/").concat(filename);
        return amazonS3.getUrl(this.bucketName, fileKey).toString();
    }
}
