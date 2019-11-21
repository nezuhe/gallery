package pl.piotrchowaniec.gallery.models.services;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import pl.piotrchowaniec.gallery.models.entities.UserEntity;

import java.time.LocalDateTime;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class UserSession {

    private boolean isLoggedIn;
    private UserEntity userEntity;
    private String sessionId;
    private LocalDateTime sessionIdGenerationTime;
}
