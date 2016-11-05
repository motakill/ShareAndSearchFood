package com.shareandsearchfood.login;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;


@Entity
public class Notebook {

    @Id(autoincrement = true)
    private Long id;
    private long userId;
    @NotNull
    private String note;
    private java.util.Date date;

    @Generated(hash = 1218005558)
    public Notebook(Long id, long userId, @NotNull String note,
            java.util.Date date) {
        this.id = id;
        this.userId = userId;
        this.note = note;
        this.date = date;
    }

    @Generated(hash = 1348176405)
    public Notebook() {
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
