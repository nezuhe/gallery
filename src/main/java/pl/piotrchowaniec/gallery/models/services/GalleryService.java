package pl.piotrchowaniec.gallery.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.piotrchowaniec.gallery.models.entities.PhotoEntity;
import pl.piotrchowaniec.gallery.models.repositories.PhotoRepository;

import java.util.List;

@Service
public class GalleryService {

    private final PhotoRepository photoRepository;

    @Autowired
    public GalleryService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public List<PhotoEntity> getPhotosToGallery(int id) {
        return photoRepository.getPhotoEntityListByGalleryId(id);
    }
}
