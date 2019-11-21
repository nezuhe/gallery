package pl.piotrchowaniec.gallery.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.piotrchowaniec.gallery.config.ApplicationProperties;
import pl.piotrchowaniec.gallery.models.services.UploadsManagerService;
import pl.piotrchowaniec.gallery.models.utils.AttributeNames;
import pl.piotrchowaniec.gallery.models.utils.Mappings;
import pl.piotrchowaniec.gallery.models.utils.Messages;
import pl.piotrchowaniec.gallery.models.utils.ViewNames;

@Controller
public class UploadsManagerController extends BaseController {

    private final UploadsManagerService uploadsManagerService;
    private final Messages messages;
    private final AmazonS3 amazonS3;

    private final String bucketName;

    public UploadsManagerController(UploadsManagerService uploadsManagerService, Messages messages, AmazonS3 amazonS3, ApplicationProperties applicationProperties) {
        this.uploadsManagerService = uploadsManagerService;
        this.messages = messages;
        this.amazonS3 = amazonS3;
        this.bucketName = applicationProperties.getAwsServices().getBucketName();
    }

    @GetMapping(Mappings.MANAGER_UPLOADS)
    public String uploadsManager(Model model) {
        model.addAttribute(AttributeNames.TITLE, messages.getManagerUploadsTitle());
        model.addAttribute("managerClass", "active");
        model.addAttribute(AttributeNames.UPLOADS, uploadsManagerService.getUploadsList());
        return ViewNames.MANAGER_UPLOADS;
    }

    @GetMapping(Mappings.MANAGER_UPLOADS_DETAILS + "{uploadId}")
    public String uploadDetails(@PathVariable("uploadId") int uploadId,
                                Model model) {
        model.addAttribute(AttributeNames.TITLE, messages.getManagerUploadsTitle());
        model.addAttribute("managerClass", "active");
        model.addAttribute(AttributeNames.UPLOAD_DETAILS, uploadsManagerService.getUploadDao(uploadId));
        model.addAttribute(AttributeNames.UPLOADED_FILES, uploadsManagerService.uploadedPhotos(uploadId));
        model.addAttribute(AttributeNames.UPLOAD_ID, uploadId);
        return ViewNames.MANAGER_UPLOAD_DETAILS;
    }

    @GetMapping(Mappings.MANAGER_DOWNLOAD + "{fileId}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") int fileId) {

        S3Object s3Object = amazonS3.getObject(bucketName, uploadsManagerService.getFileKey(fileId));
        InputStreamResource resource = new InputStreamResource(s3Object.getObjectContent());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition",
                String.format("attachment; filename=\"%s\"", uploadsManagerService.getFilename(fileId)));
        httpHeaders.add("Cache-Control", "no-cache, no-store, must-revalidate");
        httpHeaders.add("Pragma", "no-cache");
        httpHeaders.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentLength(s3Object.getObjectMetadata().getContentLength())
                .contentType(MediaType.parseMediaType(s3Object.getObjectMetadata().getContentType()))
                .body(resource);
    }

    @PostMapping(Mappings.MANAGER_DELETE_UPLOAD + "{uploadId}")
    public String deleteUpload(@PathVariable("uploadId") int uploadId,
                               RedirectAttributes redirectAttributes) {
        uploadsManagerService.deleteUpload(uploadId);
        redirectAttributes.addFlashAttribute(AttributeNames.SUCCESS_DELETE_UPLOAD_MESSAGE,
                messages.getMessageUploadDeleteSuccess());
        return "redirect:/" + Mappings.HOME;
    }

    @PostMapping(Mappings.MANAGER_UPLOADS_CHANGE_STATUS + "{uploadId}" + "-" + "{statusId}")
    public void changeUploadStatus(@PathVariable("uploadId") int uploadId,
                                   @PathVariable("statusId") int statusId) {
        uploadsManagerService.changeUploadStatus(uploadId, statusId);
    }
}
