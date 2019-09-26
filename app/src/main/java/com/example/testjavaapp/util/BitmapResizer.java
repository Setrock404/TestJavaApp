package com.example.testjavaapp.util;

public class BitmapResizer {

    //SIMPLE FUNCTION THAT RETURNS MIN OUT OF TWO VALUES
    public static int resizeBitmap(int viewSize, int bitmapSize) {
        return Math.min(viewSize, bitmapSize);
    }
}
