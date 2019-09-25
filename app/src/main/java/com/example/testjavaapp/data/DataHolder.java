package com.example.testjavaapp.data;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class DataHolder {

    private ArrayList<Bitmap> data;

    public ArrayList<Bitmap> getBitmaps() {return data;}

    public void setBitmaps(ArrayList<Bitmap> data) {this.data = data;}

    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}
}