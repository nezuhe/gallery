package pl.piotrchowaniec.gallery.models.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.piotrchowaniec.gallery.models.entities.UploadedFileEntity;

import java.util.List;
import java.util.Set;

@Repository
public interface UploadedFilesRepository extends CrudRepository<UploadedFileEntity, Integer> {

    @Query(value = "SELECT * FROM `uploaded_files` WHERE `file_id` = ?1", nativeQuery = true)
    UploadedFileEntity getFileEntityById(int fileId);

    @Query(value = "SELECT * FROM `uploaded_files` WHERE `upload_id` = ?1", nativeQuery = true)
    List<UploadedFileEntity> getFilesListByUploadId(int uploadId);

    @Query(value = "SELECT * FROM `uploaded_files` WHERE `upload_id` = ?1", nativeQuery = true)
    Set<UploadedFileEntity> getFilesSetByUploadId(int uploadId);

    @Modifying
    @Query(value = "DELETE FROM `uploaded_files` WHERE `upload_id` = ?1", nativeQuery = true)
    void deleteUploadedFileEntitiesByUploadId(int uploadId);
}
