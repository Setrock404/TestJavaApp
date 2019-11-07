package com.example.testjavaapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
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

    private final String imageUrl;
    private final ArrayList<Bitmap> bitmaps;

    public DownloadImageRunnable(CyclicBarrier cyclicBarrier, Semaphore semaphore, CountDownLatch countDownLatch, String imageUrl, ArrayList<Bitmap> bitmaps) {
        this.cyclicBarrier = cyclicBarrier;
        this.semaphore = semaphore;
        this.countDownLatch = countDownLatch;
        this.bitmaps = bitmaps;
        this.imageUrl = imageUrl;
    }

    @Override
    public void run() {
        Bitmap bitmap = null;
        try {
            semaphore.acquire();
            InputStream input = new java.net.URL(imageUrl).openStream();
            bitmap = BitmapFactory.decodeStream(input);
            //IMITATE LONG-TERM OPERATION
            Thread.sleep(2000);
            Log.d(TAG, "Thread " + Thread.currentThread().getName() + " has finished its work.. waiting for others...");
            semaphore.release();
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            Log.d(TAG, "Thread error");
            e.printStackTrace();
        } catch (MalformedURLException e){
            Log.d(TAG, "Wrong Url");
            e.printStackTrace();
        } catch (IOException e){
            Log.d(TAG, "Cant open input stream");
            e.printStackTrace();
        }
        if (bitmap != null)
            bitmaps.add(bitmap);
        countDownLatch.countDown();
    }
}

