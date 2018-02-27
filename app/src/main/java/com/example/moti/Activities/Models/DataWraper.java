package com.example.moti.Activities.Models;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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