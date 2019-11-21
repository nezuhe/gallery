package pl.piotrchowaniec.gallery.models.daos;

import lombok.Data;
import pl.piotrchowaniec.gallery.models.Status;

import java.time.LocalDateTime;

@Data
public class UploadDao {

    int uploadId;
    private String name;
    private String surname;
    private String email;
    private LocalDateTime uploadDate;
    private Status status;
    private int photosQuantity;
}
