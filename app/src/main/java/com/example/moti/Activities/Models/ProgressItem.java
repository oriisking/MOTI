package com.example.moti.Activities.Models;

import android.net.Uri;

/**
 * Created by User on 12/02/2018.
 */

public class ProgressItem {

    private String date;
    private Uri image;

    public ProgressItem() {

    }

    public ProgressItem(String date, Uri image) {

        this.date = date;
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }


}
