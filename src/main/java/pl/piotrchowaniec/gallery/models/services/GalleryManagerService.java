package pl.piotrchowaniec.gallery.models.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.piotrchowaniec.gallery.config.ApplicationProperties;
import pl.piotrchowaniec.gallery.models.entities.PhotoEntity;
import pl.piotrchowaniec.gallery.models.forms.AddPhotosToGalleryForm;
import pl.piotrchowaniec.gallery.models.repositories.PhotoRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GalleryManagerService {

    private final AmazonS3 amazonS3;
    private final PhotoRepository photoRepository;

    private List<File> filesToUpload;
    private int galleryId;
    private String bucketName;

    public GalleryManagerService(AmazonS3 amazonS3, ApplicationProperties applicationProperties, PhotoRepository photoRepository) {
        this.amazonS3 = amazonS3;
        this.photoRepository = photoRepository;
        this.bucketName = applicationProperties.getAwsServices().getBucketName();
    }

    public List<PhotoEntity> getPhotosListToManager(int galleryId) {
        return photoRepository.getPhotoEntityListByGalleryId(galleryId);
    }

    @Transactional
    public void removePhotosFromGallery(List<Integer> photosToRemoveList) {
        for (Integer photoId : photosToRemoveList) {
            removePhotoFromS3Bucket(photoId);
            photoRepository.deletePhotoEntityById(photoId);
        }
    }

    private void removePhotoFromS3Bucket(int photoId) {
        amazonS3.deleteObject(this.bucketName, getFileKey(photoId));
    }

    private String getFileKey(int photoId) {
        PhotoEntity photoEntity = photoRepository.getPhotoEntityById(photoId);
        return String.valueOf(photoEntity.getGalleryId()).concat("/").concat(photoEntity.getFilename());
    }

    public void setNewSequences(List<Integer> photosNewSequenceList) {
        for (int i = 0; i < photosNewSequenceList.size(); i++) {
            int photoId = photosNewSequenceList.get(i);
            if (photoRepository.findById(photoId).isPresent()) {
                photoRepository.findById(photoId).get().setSequence(i);
            }
        }
    }

    public boolean saveUpload(AddPhotosToGalleryForm addPhotosToGalleryForm) {
        getFilesListAndGalleryIdFromUploadForm(addPhotosToGalleryForm);
        if (isMultipartFilesSizeInLimit(addPhotosToGalleryForm)) {
            uploadFilesAndAddToDB();
            deleteTempFiles();
            return true;
        }
        return false;
    }

    private MultipartFile[] getMultipartFilesFromUploadForm(AddPhotosToGalleryForm addPhotosToGalleryForm) {
        return addPhotosToGalleryForm.getMultipartFiles();
    }

    private boolean isMultipartFilesSizeInLimit(AddPhotosToGalleryForm addPhotosToGalleryForm) {
        long sizeLimit = 10 * 1024 * 1024;
        long multipartFileSize = 0;
        for (MultipartFile multipartFile : getMultipartFilesFromUploadForm(addPhotosToGalleryForm)) {
            multipartFileSize += multipartFile.getSize();
        }
        return multipartFileSize <= sizeLimit;
    }

    private void deleteTempFiles() {
        for (File file : this.filesToUpload) {
            file.delete();
        }
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

    private void getFilesListAndGalleryIdFromUploadForm(AddPhotosToGalleryForm addPhotosToGalleryForm) {
        this.filesToUpload = new ArrayList<>();
        this.galleryId = addPhotosToGalleryForm.getGalleryId();
        MultipartFile[] multipartFiles = getMultipartFilesFromUploadForm(addPhotosToGalleryForm);
        for (MultipartFile multipartFile : multipartFiles) {
            this.filesToUpload.add(getFileFromMultipartFile(multipartFile));
        }
    }

    private void uploadFilesAndAddToDB() {
        List<String> keysInBucket = listKeysInBucket(this.bucketName);
        for (File file : this.filesToUpload) {
            String filename = file.getName();
            String fileKey = setFileKey(this.galleryId, filename);
            while (!isKeyAvailable(fileKey, keysInBucket)) {
                StringBuilder stringBuilder = new StringBuilder();
                filename = stringBuilder
                        .append(getFilenameWithoutExtension(filename))
                        .append('a')
                        .append(getFileExtension(filename))
                        .toString();
                fileKey = setFileKey(this.galleryId, filename);
            }
            uploadFileToS3Bucket(fileKey, file);
            addFileToDB(fileKey, file);
        }
    }

    private String setFileKey(int galleryId, String filename) {
        return String.valueOf(galleryId).concat("/").concat(filename);
    }

    private void uploadFileToS3Bucket(String fileKey, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(this.bucketName, fileKey, file);
        amazonS3.putObject(putObjectRequest);
        amazonS3.setObjectAcl(this.bucketName, fileKey, CannedAccessControlList.PublicRead);
    }

    private int getDotPositionInFilename(String filename) {
        return filename.lastIndexOf('.');
    }

    private String getFilenameWithoutExtension(String filename) {
        return filename.substring(0, getDotPositionInFilename(filename));
    }

    private String getFileExtension(String filename) {
        return filename.substring(getDotPositionInFilename(filename));
    }

    private List<String> listKeysInBucket(String bucketName) {
        List<S3ObjectSummary> objectsSummariesList = amazonS3.listObjects(bucketName).getObjectSummaries();
        List<String> keysInBucket = new ArrayList<>();
        for (S3ObjectSummary s3ObjectSummary : objectsSummariesList) {
            keysInBucket.add(s3ObjectSummary.getKey());
        }
        return keysInBucket;
    }

    private boolean isKeyAvailable(String filenameToCheck, List<String> keysInBucket) {
        for (String key : keysInBucket) {
            if (filenameToCheck.equals(key)) {
                return false;
            }
        }
        return true;
    }

    private void addFileToDB(String fileKey, File file) {
        photoRepository.save(getPhotoEntity(fileKey, file));
    }

    private PhotoEntity getPhotoEntity(String fileKey, File file) {
        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setFilename(file.getName());
        photoEntity.setGalleryId(this.galleryId);
        photoEntity.setPhotoUrl(getPhotoUrl(fileKey));
        photoEntity.setSequence(setPhotoSequence());
        return photoEntity;
    }

    private String getPhotoUrl(String fileKey) {
        return amazonS3.getUrl(this.bucketName, fileKey).toString();
    }

    private int setPhotoSequence() {
        int sequence;
        if (photoRepository.getMaxSequenceByGalleryId(this.galleryId).isPresent()) {
            sequence = photoRepository.getMaxSequenceByGalleryId(this.galleryId).get() + 1;
            return sequence;
        }
        return 0;
    }

    public boolean isMultipartFilesArrayEmpty(AddPhotosToGalleryForm addPhotosToGalleryForm) {
        return addPhotosToGalleryForm.getMultipartFiles()[0].isEmpty();
    }
}