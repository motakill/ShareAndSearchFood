package com.shareandsearchfood.ParcelerObjects;

import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by david_000 on 07/12/2016.
 */
@Parcel
public class HowTo {

    private long userId;
    private String title;
    private String obs;
    private String photo;
    private String comments;
    private String videos;
    private java.util.Date date;

    public HowTo(long userId,String title,
                          String obs, String photo, String comments,
                          String videos, java.util.Date date) {

        this.userId = userId;
        this.title = title;
        this.obs = obs;
        this.photo = photo;
        this.comments = comments;
        this.videos = videos;
        this.date = date;
    }

    public HowTo() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

}
