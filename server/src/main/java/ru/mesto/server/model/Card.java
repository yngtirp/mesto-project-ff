package ru.mesto.server.model;

import java.util.ArrayList;
import java.util.List;

public class Card {
    private String _id;
    private String name;
    private String link;
    private String owner_id;
    private List<Like> likes;
    private String createdAt;

    public Card() {
        this.likes = new ArrayList<>();
    }

    public Card(String _id, String name, String link, String owner_id) {
        this._id = _id;
        this.name = name;
        this.link = link;
        this.owner_id = owner_id;
        this.likes = new ArrayList<>();
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

