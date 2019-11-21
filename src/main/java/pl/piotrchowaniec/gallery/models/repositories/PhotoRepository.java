package pl.piotrchowaniec.gallery.models.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.piotrchowaniec.gallery.models.entities.PhotoEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends CrudRepository<PhotoEntity, Integer> {

    @Query(value = "SELECT * FROM `gallery` WHERE `gallery_id` = ?1 ORDER BY `sequence`", nativeQuery = true)
    List<PhotoEntity> getPhotoEntityListByGalleryId(int galleryId);

    @Query(value = "SELECT MAX(`sequence`) FROM `gallery` WHERE `gallery_id` = ?1", nativeQuery = true)
    Optional<Integer> getMaxSequenceByGalleryId(int galleryId);

    @Query(value = "SELECT * FROM `gallery` WHERE `id` = ?1", nativeQuery = true)
    PhotoEntity getPhotoEntityById(int photoId);

    @Modifying
    @Query(value = "DELETE FROM `gallery` WHERE `id` = ?1", nativeQuery = true)
    void deletePhotoEntityById(int photoId);
}
