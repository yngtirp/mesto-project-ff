package ru.mesto.server.model;

import java.util.ArrayList;
import java.util.List;

public class CardResponse {
    private String _id;
    private String name;
    private String link;
    private Owner owner;
    private List<Owner> likes;
    private String createdAt;

    public CardResponse() {
        this.likes = new ArrayList<>();
    }

    public static class Owner {
        private String _id;

        public Owner() {
        }

        public Owner(String _id) {
            this._id = _id;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }
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

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Owner> getLikes() {
        return likes;
    }

    public void setLikes(List<Owner> likes) {
        this.likes = likes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

