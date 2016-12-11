package com.shareandsearchfood.ParcelerObjects;

/**
 * Created by david_000 on 10/12/2016.
 */

public class Comments {
    private String comment;
    private String photo;
    private String userID;
    private String date;
    private String commentID;
    private String video;

    public Comments(String comment, String photo,String video,
                    String date, String userID, String commentID) {
        this.comment = comment;
        this.photo = photo;
        this.userID = userID;
        this.date = date;
        this.commentID = commentID;
        this.video = video;
    }

    public Comments() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
