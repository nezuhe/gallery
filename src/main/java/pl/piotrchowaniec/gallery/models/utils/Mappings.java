package pl.piotrchowaniec.gallery.models.utils;

public class Mappings {

    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String HOME = "home";
    public static final String MAIN_GALLERY = "gallery";
    public static final String GUESTS_GALLERY = "guests-gallery";
    public static final String UPLOAD = "upload";
    public static final String UPLOAD_SAVE = "upload-save";

    private static final String MANAGER_PREFIX = "manager";
    public static final String MANAGER_ADD_PHOTOS = MANAGER_PREFIX + "-add-photos";
    public static final String MANAGER_ADD_PHOTOS_TEST = MANAGER_PREFIX + "-add-photos-test";
    public static final String MANAGER_MAIN_GALLERY = MANAGER_PREFIX + "-main";
    public static final String MANAGER_GUESTS_GALLERY = MANAGER_PREFIX + "-guests";
    public static final String MANAGER_SAVE_CHANGES = MANAGER_PREFIX + "-save";
    public static final String MANAGER_UPLOADS = MANAGER_PREFIX + "-uploads";
    public static final String MANAGER_UPLOADS_DETAILS = MANAGER_UPLOADS + "-";
    public static final String MANAGER_DOWNLOAD = MANAGER_PREFIX + "-download-";
    public static final String MANAGER_DELETE_UPLOAD = MANAGER_PREFIX + "-delete-upload-";
    public static final String MANAGER_UPLOADS_CHANGE_STATUS = MANAGER_UPLOADS + "-change-";
}
