package pl.piotrchowaniec.gallery.models.entities;

import lombok.Data;
import pl.piotrchowaniec.gallery.models.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "uploads")
@Data
public class UploadEntity {

    @Id
    @GeneratedValue
    @Column(name = "upload_id")
    int uploadId;
    @NotNull
    private String name;
    private String surname;
    private String email;
    @NotNull
    private String folder;
    @Column(name = "upload_date")
    private LocalDateTime uploadDate;
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "uploadEntity", cascade = {}, fetch = FetchType.LAZY)
    private List<UploadedFileEntity> uploadedFilesList;
}
