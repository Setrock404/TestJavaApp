package com.example.testjavaapp.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.IBinder;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.testjavaapp.ServiceActivity;
import com.example.testjavaapp.data.BitmapHolder;
import com.example.testjavaapp.util.DownloadImageRunnable;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class BroadcastService extends Service {
    private ArrayList<String> urls;
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();

    private CyclicBarrier barrier;
    private Semaphore semaphore;
    private CountDownLatch countDownLatch;

    private int threadNum;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        setDownloadData(intent);

        return START_REDELIVER_INTENT;
    }

    private Runnable downloadBitmaps = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < threadNum; i++) {
                new Thread(new DownloadImageRunnable(barrier, semaphore, countDownLatch, urls.get(i), bitmaps)).start();
            }
            try {
                countDownLatch.await();
                onImagesDownloaded();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private void setDownloadData(Intent intent) {

        threadNum = intent.getIntExtra("barrier_num", 0);
        urls = intent.getStringArrayListExtra("url_list");
        int semaphoreNum = intent.getIntExtra("semaphore_num", 0);

        setThreadConfiguration(semaphoreNum);

        new Thread(downloadBitmaps).start();
    }

    private void setThreadConfiguration(int semaphoreNum) {
        barrier = new CyclicBarrier(threadNum);
        semaphore = new Semaphore(semaphoreNum, true);
        countDownLatch = new CountDownLatch(threadNum);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void onImagesDownloaded() {

        BitmapHolder.getInstance().setBitmaps(bitmaps);

        Intent broadcastIntent = new Intent();
        broadcastIntent
                .setAction(ServiceActivity.mBroadcastBitmapAction);

        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
}