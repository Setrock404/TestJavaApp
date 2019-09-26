package com.example.testjavaapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class DownloadImageRunnable implements Runnable {

    private static final String TAG = DownloadImageRunnable.class.getSimpleName();
    private final CyclicBarrier cyclicBarrier;
    private final Semaphore semaphore;
    private final CountDownLatch countDownLatch;

    private final String url;
    private final ArrayList<Bitmap> bitmaps;

    public DownloadImageRunnable(CyclicBarrier cyclicBarrier, Semaphore semaphore, CountDownLatch countDownLatch, String url, ArrayList<Bitmap> bitmaps) {
        this.cyclicBarrier = cyclicBarrier;
        this.semaphore = semaphore;
        this.countDownLatch = countDownLatch;
        this.bitmaps = bitmaps;
        this.url = url;
    }

    @Override
    public void run() {

        Bitmap bitmap = null;
        try {
            semaphore.acquire();

            InputStream input = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(input);

            Log.d(TAG, "Thread " + Thread.currentThread().getName() + " has finished its work.. waiting for others...");

            //IMITATE LONG-TERM OPERATION
            Thread.sleep(2000);
            semaphore.release();
            cyclicBarrier.await();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (bitmap != null)
            bitmaps.add(bitmap);

        countDownLatch.countDown();

    }

}

