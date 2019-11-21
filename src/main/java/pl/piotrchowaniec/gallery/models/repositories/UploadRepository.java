package pl.piotrchowaniec.gallery.models.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.piotrchowaniec.gallery.models.entities.UploadEntity;

import java.util.List;

@Repository
public interface UploadRepository extends CrudRepository<UploadEntity, Integer> {

    @Query(value = "SELECT * FROM `uploads` ORDER BY `upload_id` DESC", nativeQuery = true)
    List<UploadEntity> getUploadsList();

    @Query(value = "SELECT * FROM `uploads` WHERE `upload_id` = ?1", nativeQuery = true)
    UploadEntity getUploadEntityById(int id);

    @Modifying
    @Query(value = "DELETE FROM `uploads` WHERE `upload_id` = ?1", nativeQuery = true)
    void deleteUploadEntityByUploadId(int uploadId);

    @Modifying
    @Query(value= "UPDATE `uploads` SET `status` = ?2 WHERE `upload_id` = ?1", nativeQuery = true)
    void changeUploadStatus(int uploadId, String status);
}
