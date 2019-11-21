package pl.piotrchowaniec.gallery.models.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "gallery")
@Data
public class PhotoEntity {

    @Id
    @GeneratedValue
    private int id;
    @NotNull
    private String filename;
    @Column(name = "photo_url")
    @NotNull
    private String photoUrl;
    @Column(name = "gallery_id")
    @NotNull
    private int galleryId;
    @Column(name = "sequence")
    @NotNull
    private int sequence;
}
