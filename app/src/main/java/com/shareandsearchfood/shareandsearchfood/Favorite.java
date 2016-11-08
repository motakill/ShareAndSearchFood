package com.shareandsearchfood.shareandsearchfood;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by david_000 on 07/11/2016.
 */
@Entity
public class Favorite {
    @Id(autoincrement = true)
    private Long id;
    private long userId;
    @NotNull
    private long receiptId;


    @Generated(hash = 1418969085)
    public Favorite(Long id, long userId, long receiptId) {
        this.id = id;
        this.userId = userId;
        this.receiptId = receiptId;
    }

    @Generated(hash = 459811785)
    public Favorite() {
    }


    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getReceiptId() {
        return this.receiptId;
    }

    public void setReceiptId(long receiptId) {
        this.receiptId = receiptId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
