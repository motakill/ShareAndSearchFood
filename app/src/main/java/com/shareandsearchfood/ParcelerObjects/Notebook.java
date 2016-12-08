package com.shareandsearchfood.ParcelerObjects;

import org.parceler.Parcel;

/**
 * Created by david_000 on 07/12/2016.
 */
@Parcel
public class Notebook {
    private String note;
    private String date;

    public Notebook(String note,
                    String date) {
        this.note = note;
        this.date = date;
    }

    public Notebook() {
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
