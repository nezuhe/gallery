package pl.piotrchowaniec.gallery.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.piotrchowaniec.gallery.models.Roles;
import pl.piotrchowaniec.gallery.models.entities.UserEntity;
import pl.piotrchowaniec.gallery.models.forms.LoginForm;
import pl.piotrchowaniec.gallery.models.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserSession userSession;
    private final HashService hashService;

    @Autowired
    public UserService(UserRepository userRepository, UserSession userSession, HashService hashService) {
        this.userRepository = userRepository;
        this.userSession = userSession;
        this.hashService = hashService;
    }

    public boolean logIn(LoginForm loginForm) {
        Optional<UserEntity> userEntityFromLoginForm = userRepository.getUserEntityByUsername(loginForm.getUsername());
        if (userEntityFromLoginForm.isPresent()
                && hashService.isTypedStringCorrect(loginForm.getPassword(), userEntityFromLoginForm.get().getPassword())) {
            setUserEntityToSession(userEntityFromLoginForm.get());
            return true;
        }
        return false;
    }

    public void logOut() {
        eraseUserEntityFromSession();
    }

    private void setUserEntityToSession(UserEntity userEntity) {
        userSession.setLoggedIn(true);
        userSession.setUserEntity(userEntity);
    }

    private void eraseUserEntityFromSession() {
        userSession.setLoggedIn(false);
        userSession.setUserEntity(null);
    }

    public void setUserSessionId(String sessionId) {
        userSession.setSessionId(sessionId);
        userSession.setSessionIdGenerationTime(LocalDateTime.now());
    }

    public boolean isAdminLogged() {
        return userSession.getUserEntity().getRole().equals(Roles.ADMIN);
    }
}
