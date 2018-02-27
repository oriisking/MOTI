package com.example.moti.Activities.Models;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by User on 12/02/2018.
 */

public class ProgressItem{

    private String date;
    private String image;

    public ProgressItem() {

    }

    public ProgressItem(String date, String image) {

        this.date = date;
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
