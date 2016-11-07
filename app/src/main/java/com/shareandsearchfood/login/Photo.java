package com.shareandsearchfood.login;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by david_000 on 07/11/2016.
 */

@Entity
public class Photo {

    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String photo;
    @NotNull
    private String description;
    private long receiptId;

    @Generated(hash = 1056810425)
    public Photo(Long id, @NotNull String photo, @NotNull String description,
            long receiptId) {
        this.id = id;
        this.photo = photo;
        this.description = description;
        this.receiptId = receiptId;
    }

    @Generated(hash = 1043664727)
    public Photo() {
    }

    public String getPhoto(){
        return photo;
    }

    public void setPhoto(String photo){
        this.photo = photo;
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
