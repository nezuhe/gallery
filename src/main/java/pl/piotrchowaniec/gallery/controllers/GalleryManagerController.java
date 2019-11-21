package pl.piotrchowaniec.gallery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.piotrchowaniec.gallery.models.forms.AddPhotosToGalleryForm;
import pl.piotrchowaniec.gallery.models.services.GalleryManagerService;
import pl.piotrchowaniec.gallery.models.utils.AttributeNames;
import pl.piotrchowaniec.gallery.models.utils.Mappings;
import pl.piotrchowaniec.gallery.models.utils.Messages;
import pl.piotrchowaniec.gallery.models.utils.ViewNames;

import java.util.List;
import java.util.Map;

@Controller
public class GalleryManagerController extends BaseController {

    private final GalleryManagerService galleryManagerService;
    private final Messages messages;

    @Autowired
    public GalleryManagerController(GalleryManagerService galleryManagerService, Messages messages) {
        this.galleryManagerService = galleryManagerService;
        this.messages = messages;
    }

    @GetMapping(Mappings.MANAGER_ADD_PHOTOS)
    public String galleryManagement(Model model) {
        model.addAttribute(AttributeNames.TITLE, messages.getManagerAddPhotosTitle());
        model.addAttribute("managerClass", "active");
        model.addAttribute(AttributeNames.ADD_PHOTOS_TO_GALLERY_FORM, new AddPhotosToGalleryForm());
        return ViewNames.MANAGER_ADD_PHOTOS;
    }

    @PostMapping(Mappings.MANAGER_ADD_PHOTOS)
    public String addPhotosToGallery(@ModelAttribute AddPhotosToGalleryForm addPhotosToGalleryForm,
                                     RedirectAttributes redirectAttributes) {
        if (galleryManagerService.isMultipartFilesArrayEmpty(addPhotosToGalleryForm)) {
            redirectAttributes.addFlashAttribute(AttributeNames.ERROR_UPLOAD_MESSAGE,
                    messages.getMessageUploadEmptyError());
        } else if (galleryManagerService.saveUpload(addPhotosToGalleryForm)) {
            redirectAttributes.addFlashAttribute(AttributeNames.SUCCESS_UPLOAD_MESSAGE,
                    messages.getMessageUploadSuccess());
        } else {
            redirectAttributes.addFlashAttribute(AttributeNames.ERROR_UPLOAD_MESSAGE,
                    messages.getMessageUploadFileSizeError());
        }
        return "redirect:/" + Mappings.MANAGER_ADD_PHOTOS;
    }

    @GetMapping(Mappings.MANAGER_MAIN_GALLERY)
    public String manageMainGallery(Model model) {
        model.addAttribute(AttributeNames.TITLE, messages.getManagerMainGalleryTitle());
        model.addAttribute("managerClass", "active");
        model.addAttribute(AttributeNames.GALLERY, galleryManagerService.getPhotosListToManager(MAIN_GALLERY_ID));
        return ViewNames.MANAGER_MAIN_GALLERY;
    }

    @GetMapping(Mappings.MANAGER_GUESTS_GALLERY)
    public String manageGuestsGallery(Model model) {
        model.addAttribute(AttributeNames.TITLE, messages.getManagerGuestsGalleryTitle());
        model.addAttribute("managerClass", "active");
        model.addAttribute(AttributeNames.GALLERY, galleryManagerService.getPhotosListToManager(GUESTS_GALLERY_ID));
        return ViewNames.MANAGER_MAIN_GALLERY;
    }

    @PostMapping(Mappings.MANAGER_SAVE_CHANGES)
    @ResponseBody
    public void saveManagerChanges(@RequestBody Map<String, List<Integer>> imagesLists) {
        galleryManagerService.removePhotosFromGallery(imagesLists.get(AttributeNames.PHOTOS_TO_REMOVE_LIST));
        galleryManagerService.setNewSequences(imagesLists.get(AttributeNames.PHOTOS_NEW_SEQUENCE_LIST));
    }

    @PostMapping(Mappings.MANAGER_ADD_PHOTOS_TEST)
    public String addImagesToGalleryTest(@ModelAttribute AddPhotosToGalleryForm addPhotosToGalleryForm) {
        galleryManagerService.saveUpload(addPhotosToGalleryForm);
        return "redirect:/" + Mappings.MANAGER_ADD_PHOTOS;
    }
}
