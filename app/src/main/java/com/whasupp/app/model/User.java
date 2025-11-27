package com.whasupp.app.model;

public class User {
    private String uid;
    private String name;
    private String email;
    private String photoUrl; // Nuevo campo para la foto

    public User() {}

    public User(String uid, String name, String email, String photoUrl) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public String getUid() { return uid; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhotoUrl() { return photoUrl; }
}