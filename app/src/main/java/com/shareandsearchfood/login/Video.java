package com.shareandsearchfood.login;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by david_000 on 07/11/2016.
 */

@Entity
public class Video {

    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String video;
    @NotNull
    private String description;
    private long receiptId;

    @Generated(hash = 628445655)
    public Video(Long id, @NotNull String video, @NotNull String description,
            long receiptId) {
        this.id = id;
        this.video = video;
        this.description = description;
        this.receiptId = receiptId;
    }

    @Generated(hash = 237528154)
    public Video() {
    }

    public String getVideo(){
        return video;
    }

    public void setVideo(String video){
        this.video = video;
    }

    public String getDescription(){
        return description;
    }

    public void setDescriptiont(String description){
        this.description = description;
    }

    public long getReceiptId(){
        return receiptId;
    }

    public void setReceiptId(long receiptId){
        this.receiptId = receiptId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
