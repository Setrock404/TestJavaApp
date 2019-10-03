package com.example.testjavaapp.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

public class DownloadCompleteRunnable implements Runnable {

    private final CountDownLatch countDownLatch;
    private DownloadCallback callback;

    public DownloadCompleteRunnable(DownloadCallback callback, CountDownLatch countDownLatch) {
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
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(callback::onImagesDownloaded);
    }
}

