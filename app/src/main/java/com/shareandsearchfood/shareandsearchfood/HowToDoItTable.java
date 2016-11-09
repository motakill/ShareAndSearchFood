package com.shareandsearchfood.shareandsearchfood;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

/**
 * Created by tiagomota on 08/11/16.
 */
@Entity
public class HowToDoItTable {

    @Id(autoincrement = true)
    private Long id;

    private long userId;

    @NotNull
    private String title;

    @NotNull
    private String obs;

    @NotNull
    private String photo;

    private String comments;

    private String videos;

    private java.util.Date date;

    @Generated(hash = 1700257450)
    public HowToDoItTable(Long id, long userId, @NotNull String title,
            @NotNull String obs, @NotNull String photo, String comments,
            String videos, java.util.Date date) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.obs = obs;
        this.photo = photo;
        this.comments = comments;
        this.videos = videos;
        this.date = date;
    }

    @Generated(hash = 1683627809)
    public HowToDoItTable() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserId_how_to_do_it() {
        return userId;
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

    public long getUserId() {
        return this.userId;
    }
}
