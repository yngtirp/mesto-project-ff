package ru.mesto.server.model;

public class Like {
    private String _id;
    private String user_id;
    private String card_id;

    public Like() {
    }

    public Like(String _id, String user_id, String card_id) {
        this._id = _id;
        this.user_id = user_id;
        this.card_id = card_id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }
}

