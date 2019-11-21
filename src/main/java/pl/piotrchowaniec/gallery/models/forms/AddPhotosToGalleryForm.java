package pl.piotrchowaniec.gallery.models.forms;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AddPhotosToGalleryForm {

    private MultipartFile[] multipartFiles;
    private int galleryId;
}
