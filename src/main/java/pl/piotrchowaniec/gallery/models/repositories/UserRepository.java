package pl.piotrchowaniec.gallery.models.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.piotrchowaniec.gallery.models.entities.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    @Query(value = "SELECT * FROM `users` WHERE `username` = ?1", nativeQuery = true)
    Optional<UserEntity> getUserEntityByUsername(String username);
}
