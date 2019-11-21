package pl.piotrchowaniec.gallery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.piotrchowaniec.gallery.models.services.UserService;

public abstract class BaseController {

    final int MAIN_GALLERY_ID = 2;
    final int GUESTS_GALLERY_ID = 3;

    @Autowired
    private UserService userService;

    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        return userService.isAdminLogged();
    }
}
