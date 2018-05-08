package com.example.moti.data.local.progress;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by User on 18/02/2018.
 */

public class DataWraper implements Serializable {

    private static Bitmap[] items;

    public DataWraper(Bitmap[] data) {
        this.items = data;
    }

    public static Bitmap[] getItems() {
        return items;
    }

    public static void setItems(Bitmap[] items) {
        DataWraper.items = items;
    }
}