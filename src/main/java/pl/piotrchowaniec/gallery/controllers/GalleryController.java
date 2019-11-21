package pl.piotrchowaniec.gallery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.piotrchowaniec.gallery.models.services.GalleryService;
import pl.piotrchowaniec.gallery.models.utils.AttributeNames;
import pl.piotrchowaniec.gallery.models.utils.Mappings;
import pl.piotrchowaniec.gallery.models.utils.Messages;
import pl.piotrchowaniec.gallery.models.utils.ViewNames;

@Controller
public class GalleryController extends BaseController {

    private final GalleryService galleryService;
    private final Messages messages;

    @Autowired
    public GalleryController(GalleryService galleryService, Messages messages) {
        this.galleryService = galleryService;
        this.messages = messages;
    }

    @GetMapping(Mappings.MAIN_GALLERY)
    public String gallery(Model model) {
        model.addAttribute(AttributeNames.TITLE, messages.getGalleryTitle());
        model.addAttribute("mainGalleryClass", "active");
        model.addAttribute(AttributeNames.GALLERY, galleryService.getPhotosToGallery(MAIN_GALLERY_ID));
        return ViewNames.GALLERY;
    }

    @GetMapping(Mappings.GUESTS_GALLERY)
    public String guestsGallery(Model model) {
        model.addAttribute(AttributeNames.TITLE, messages.getGuestsGalleryTitle());
        model.addAttribute("guestsGalleryClass", "active");
        model.addAttribute(AttributeNames.GALLERY, galleryService.getPhotosToGallery(GUESTS_GALLERY_ID));
        return ViewNames.GALLERY;
    }
}
