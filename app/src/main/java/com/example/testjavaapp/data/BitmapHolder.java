package com.example.testjavaapp.data;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class BitmapHolder {

    private ArrayList<Bitmap> bitmaps;

    public ArrayList<Bitmap> getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(ArrayList<Bitmap> data) {
        this.bitmaps = data;
    }

    private static final BitmapHolder holder = new BitmapHolder();

    public static BitmapHolder getInstance() {
        return holder;
    }
}