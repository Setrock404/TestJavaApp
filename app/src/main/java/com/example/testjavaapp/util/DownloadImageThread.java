package com.example.testjavaapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class DownloadImageThread implements Runnable {


    private static final String TAG = "RecyclerView";
    private final CyclicBarrier cyclicBarrier;
    private final Semaphore semaphore;
    private final CountDownLatch countDownLatch;

    private String url;
    private ArrayList<Bitmap> bitmaps;

    public DownloadImageThread(CyclicBarrier cyclicBarrier, Semaphore semaphore, CountDownLatch countDownLatch, String url, ArrayList<Bitmap> bitmaps) {
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
            String imageURL = url;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d(TAG, "Thread " + Thread.currentThread().getName() + " has finished its work.. waiting for others...");

            //IMITATE LONG-TERM OPERATION
            Thread.sleep(2000);
            semaphore.release();
            cyclicBarrier.await();

        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        bitmaps.add(bitmap);
        countDownLatch.countDown();

    }

}

