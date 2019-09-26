package com.example.testjavaapp.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

public class DownloadImageThreadComplete implements Runnable {


    private static final String TAG = "RecyclerView";
    private final CountDownLatch countDownLatch;
    private DownloadCallback callback;

    public DownloadImageThreadComplete(DownloadCallback callback, CountDownLatch countDownLatch) {
        this.callback = callback;
        this.countDownLatch = countDownLatch;

    }

    @Override
    public void run() {

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "run: task complete, calling handler");

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(callback::onImagesDownloaded);

    }

}

