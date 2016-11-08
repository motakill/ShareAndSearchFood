package com.shareandsearchfood.shareandsearchfood;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

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

    @Generated(hash = 1014251840)
    public HowToDoItTable(Long id, long userId, @NotNull String title,
            @NotNull String obs, @NotNull String photo, String comments,
            String videos) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.obs = obs;
        this.photo = photo;
        this.comments = comments;
        this.videos = videos;
    }

    @Generated(hash = 1683627809)
    public HowToDoItTable() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserId() {
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
}
