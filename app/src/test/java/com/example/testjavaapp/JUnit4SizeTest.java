package com.example.testjavaapp;

import com.example.testjavaapp.util.BitmapResizer;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class JUnit4SizeTest {

    @Test
    public void resize_isCorrect1() {
        int actual = BitmapResizer.resizeBitmap(150, 200);
        int expected = 150;
        assertEquals("Resize failed", expected, actual);
    }

    @Test
    public void resize_isCorrect2() {
        int actual = BitmapResizer.resizeBitmap(150, 100);
        int expected = 100;
        assertEquals("Resize failed", expected, actual);
    }

}

