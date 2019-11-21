package pl.piotrchowaniec.gallery.models.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Messages {

    private final MessageSource messageSource;

    private final String TITLE_HOME = "title.home";
    private final String TITLE_GALLERY = "title.mainGallery";
    private final String TITLE_GUESTS_GALLERY = "title.guestsGallery";
    private final String TITLE_UPLOAD = "title.upload";

    private final String TITLE_MANAGER_ADD_PHOTOS = "title.manager.addPhotos";
    private final String TITLE_MANAGER_MAIN_GALLERY = "title.manager.mainGallery";
    private final String TITLE_MANAGER_GUESTS_GALLERY = "title.manager.guestsGallery";
    private final String TITLE_MANAGER_UPLOADS = "title.manager.uploads";

    private final String MESSAGE_LOGIN_ERROR = "message.loginError";
    private final String MESSAGE_UPLOAD_FILE_SIZE_ERROR = "message.upload.fileSize.error";
    private final String MESSAGE_UPLOAD_EMPTY_ERROR = "message.upload.empty.error";
    private final String MESSAGE_UPLOAD_SUCCESS = "message.upload.success";
    private final String MESSAGE_UPLOAD_DELETE_SUCCESS = "message.upload.delete.success";

    @Autowired
    public Messages(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public String getHomeTitle() {return getMessage(TITLE_HOME);}
    public String getGalleryTitle() {return getMessage(TITLE_GALLERY);}
    public String getGuestsGalleryTitle() {return getMessage(TITLE_GUESTS_GALLERY);}
    public String getUploadTitle() {return getMessage(TITLE_UPLOAD);}

    public String getManagerAddPhotosTitle() {return getMessage(TITLE_MANAGER_ADD_PHOTOS); }
    public String getManagerMainGalleryTitle() {return getMessage(TITLE_MANAGER_MAIN_GALLERY);}
    public String getManagerGuestsGalleryTitle() {return getMessage(TITLE_MANAGER_GUESTS_GALLERY);}
    public String getManagerUploadsTitle() {return getMessage(TITLE_MANAGER_UPLOADS);}

    public String getMessageLoginError() {return getMessage(MESSAGE_LOGIN_ERROR);}
    public String getMessageUploadFileSizeError() {return getMessage(MESSAGE_UPLOAD_FILE_SIZE_ERROR);}
    public String getMessageUploadEmptyError() {return getMessage(MESSAGE_UPLOAD_EMPTY_ERROR);}
    public String getMessageUploadSuccess() {return getMessage(MESSAGE_UPLOAD_SUCCESS);}
    public String getMessageUploadDeleteSuccess() {return getMessage(MESSAGE_UPLOAD_DELETE_SUCCESS);}
}
