package com.shareandsearchfood.ParcelerObjects;

/**
 * Created by david_000 on 10/12/2016.
 */

public class Comments {
    private String coment;
    private String photo;
    private String userID;
    private String date;

    public Comments(String coment, String photo, String date, String userID) {
        this.coment = coment;
        this.photo = photo;
        this.userID = userID;
        this.date = date;
    }

    public Comments() {
    }

    public String getComent() {
        return coment;
    }

    public void setComent(String coment) {
        this.coment = coment;
    }
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
