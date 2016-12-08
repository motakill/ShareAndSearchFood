package com.shareandsearchfood.ParcelerObjects;

import org.parceler.Parcel;

/**
 * Created by david_000 on 07/12/2016.
 */
@Parcel
public class Favorite {
    private long userId;
    private long receiptId;

    public Favorite(long userId, long receiptId) {
        this.userId = userId;
        this.receiptId = receiptId;
    }

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

}

