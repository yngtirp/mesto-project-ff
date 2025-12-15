package ru.mesto.server.model;

public class User {
    private String _id;
    private String name;
    private String about;
    private String avatar;
    private String authorization;

    public User() {
    }

    public User(String _id, String name, String about, String avatar, String authorization) {
        this._id = _id;
        this.name = name;
        this.about = about;
        this.avatar = avatar;
        this.authorization = authorization;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}

