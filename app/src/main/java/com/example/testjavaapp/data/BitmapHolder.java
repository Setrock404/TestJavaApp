package com.example.testjavaapp.data;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class BitmapHolder {

    private ArrayList<Bitmap> data;

    public ArrayList<Bitmap> getBitmaps() {
        return data;
    }

    public void setBitmaps(ArrayList<Bitmap> data) {
        this.data = data;
    }

    private static final BitmapHolder holder = new BitmapHolder();

    public static BitmapHolder getInstance() {
        return holder;
    }
}