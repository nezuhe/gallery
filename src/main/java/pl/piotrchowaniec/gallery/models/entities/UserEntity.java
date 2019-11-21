package pl.piotrchowaniec.gallery.models.entities;

import lombok.Data;
import pl.piotrchowaniec.gallery.models.Roles;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@Data
public class UserEntity {

    @Id
    @GeneratedValue
    private int id;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @Enumerated(EnumType.STRING)
    private Roles role;
}
