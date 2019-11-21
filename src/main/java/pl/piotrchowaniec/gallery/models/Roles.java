package pl.piotrchowaniec.gallery.models;

public enum Roles {
    USER(0), ADMIN(1);

    private int statusValue;
    private Roles (int statusValue) {
        this.statusValue = statusValue;
    }
}
