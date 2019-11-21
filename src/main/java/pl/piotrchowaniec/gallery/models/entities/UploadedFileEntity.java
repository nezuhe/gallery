package pl.piotrchowaniec.gallery.models.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "uploaded_files")
@Data
public class UploadedFileEntity {

    @Column(name = "file_id")
    @Id
    @GeneratedValue
    int fileId;
    @NotNull
    private String filename;
    @Column(name = "photo_url")
    @NotNull
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "upload_id")
    private UploadEntity uploadEntity;
}
