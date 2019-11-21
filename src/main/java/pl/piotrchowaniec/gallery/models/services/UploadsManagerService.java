package pl.piotrchowaniec.gallery.models.services;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.piotrchowaniec.gallery.config.ApplicationProperties;
import pl.piotrchowaniec.gallery.models.Status;
import pl.piotrchowaniec.gallery.models.daos.UploadDao;
import pl.piotrchowaniec.gallery.models.entities.UploadEntity;
import pl.piotrchowaniec.gallery.models.entities.UploadedFileEntity;
import pl.piotrchowaniec.gallery.models.mappers.UploadEntityToUploadDaoMapper;
import pl.piotrchowaniec.gallery.models.repositories.UploadRepository;
import pl.piotrchowaniec.gallery.models.repositories.UploadedFilesRepository;

import java.util.List;
import java.util.Set;

@Service
public class UploadsManagerService {

    private final UploadRepository uploadRepository;
    private final UploadedFilesRepository uploadedFilesRepository;
    private final AmazonS3 amazonS3;

    private final String bucketName;

    public UploadsManagerService(UploadRepository uploadRepository, UploadedFilesRepository uploadedFilesRepository,
                                 AmazonS3 amazonS3, ApplicationProperties applicationProperties) {
        this.uploadRepository = uploadRepository;
        this.uploadedFilesRepository = uploadedFilesRepository;
        this.amazonS3 = amazonS3;
        this.bucketName = applicationProperties.getAwsServices().getBucketName();
    }

    public List<UploadEntity> getUploadsList() {
        return uploadRepository.getUploadsList();
    }

    public UploadDao getUploadDao(int uploadId) {
        UploadDao uploadDao = new UploadEntityToUploadDaoMapper().map(uploadRepository.getUploadEntityById(uploadId));
        return uploadDao;
    }

    public Set<UploadedFileEntity> uploadedPhotos(int uploadId) {
        return uploadedFilesRepository.getFilesSetByUploadId(uploadId);
    }

    private String getFolder(int uploadId) {
        return uploadRepository.getUploadEntityById(uploadId).getFolder();
    }

    public String getFilename(int fileId) {
        return uploadedFilesRepository.getFileEntityById(fileId).getFilename();
    }

    private int getUploadId(int fileId) {
        return uploadedFilesRepository.getFileEntityById(fileId).getUploadEntity().getUploadId();
    }

    public String getFileKey(int fileId) {
        return getFolder(getUploadId(fileId)).concat("/").concat(getFilename(fileId));
    }

    @Transactional
    public void deleteUpload(int uploadId) {
        deleteFiles(uploadId);
        deleteUploadFromDB(uploadId);
    }

    private void deleteFiles(int uploadId) {
        Set<UploadedFileEntity> uploadedFilesSet = uploadedFilesRepository.getFilesSetByUploadId(uploadId);
        for (UploadedFileEntity file : uploadedFilesSet) {
            int fileId = file.getFileId();
            deleteUploadedFileFromS3Bucket(fileId);
        }
        deleteUploadedFilesFromDB(uploadId);
    }

    private void deleteUploadedFileFromS3Bucket(int fileId) {
        amazonS3.deleteObject(this.bucketName, getFileKey(fileId));
    }

    private void deleteUploadedFilesFromDB(int uploadId) {
        uploadedFilesRepository.deleteUploadedFileEntitiesByUploadId(uploadId);
    }

    private void deleteUploadFromDB(int uploadId) {
        uploadRepository.deleteUploadEntityByUploadId(uploadId);
    }

    public void changeUploadStatus(int uploadId, int statusId) {
        switch (statusId) {
            case 1:
                uploadRepository.changeUploadStatus(uploadId, Status.NEW.toString());
                break;
            case 2:
                uploadRepository.changeUploadStatus(uploadId, Status.READ.toString());
                break;
            case 3:
                uploadRepository.changeUploadStatus(uploadId, Status.DOWNLOADED.toString());
                break;
        }
    }
}
