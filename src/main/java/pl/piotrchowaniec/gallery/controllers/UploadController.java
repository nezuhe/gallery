package pl.piotrchowaniec.gallery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.piotrchowaniec.gallery.models.forms.UploaderForm;
import pl.piotrchowaniec.gallery.models.services.UploadService;
import pl.piotrchowaniec.gallery.models.utils.AttributeNames;
import pl.piotrchowaniec.gallery.models.utils.Mappings;
import pl.piotrchowaniec.gallery.models.utils.Messages;
import pl.piotrchowaniec.gallery.models.utils.ViewNames;

@Controller
public class UploadController extends BaseController {

    private final UploadService uploadService;
    private final Messages messages;

    @Autowired
    public UploadController(UploadService uploadService, Messages messages) {
        this.uploadService = uploadService;
        this.messages = messages;
    }

    @GetMapping(Mappings.UPLOAD)
    public String uploadFiles(Model model) {
        model.addAttribute(AttributeNames.TITLE, messages.getUploadTitle());
        model.addAttribute("uploadFilesClass", "active");
        model.addAttribute(AttributeNames.UPLOADER_FORM, new UploaderForm());
        uploadService.cleanUploadList();
        return ViewNames.UPLOAD;
    }

    @PostMapping(Mappings.UPLOAD)
    @ResponseBody
    public void handleFileUpload(@RequestParam("file") MultipartFile multipartFile) {
        uploadService.addFileToUploadList(multipartFile);
    }

    @PostMapping(Mappings.UPLOAD_SAVE)
    public String sendUploaderData(@ModelAttribute UploaderForm uploaderForm,
                                 RedirectAttributes redirectAttributes) {
        if (uploadService.saveUpload(uploaderForm)) {
            redirectAttributes.addFlashAttribute(AttributeNames.SUCCESS_UPLOAD_MESSAGE, messages.getMessageUploadSuccess());
        } else {
            redirectAttributes.addFlashAttribute(AttributeNames.ERROR_UPLOAD_MESSAGE,messages.getMessageUploadFileSizeError());
        }
        return "redirect:/" + Mappings.UPLOAD;
    }
}
