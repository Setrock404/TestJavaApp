package com.example.testjavaapp.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.testjavaapp.ServiceActivity;
import com.example.testjavaapp.data.DataHolder;
import com.example.testjavaapp.util.DownloadCallback;
import com.example.testjavaapp.util.DownloadImageThread;
import com.example.testjavaapp.util.DownloadImageThreadComplete;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class BroadcastService extends Service implements DownloadCallback {
    private String LOG_TAG = "RecyclerView";
    private ArrayList<String> urls;
    private ArrayList<Bitmap> bitmaps;

    private CyclicBarrier barrier;
    private Semaphore semaphore;
    private CountDownLatch countDownLatch;

    @Override
    public void onCreate() {
        super.onCreate();
        LOG_TAG = "RecyclerView";
        Log.d(LOG_TAG, "In onCreate");
        bitmaps = new ArrayList<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "In onStartCommand");

        setDownloadData(intent);

        return START_REDELIVER_INTENT;
    }

    private void setDownloadData(Intent intent) {

        int barrierNum = intent.getIntExtra("barrier_num",0);
        int semaphoreNum = intent.getIntExtra("semaphore_num",0);
        urls = intent.getStringArrayListExtra("url_list");

        barrier = new CyclicBarrier(barrierNum);
        semaphore = new Semaphore(semaphoreNum,true);
        countDownLatch = new CountDownLatch(barrierNum);

        new Thread(new DownloadImageThreadComplete(this,countDownLatch)).start();
        for(int i=0; i<barrierNum;i++){
            new Thread(new DownloadImageThread(barrier, semaphore, countDownLatch, urls.get(i), bitmaps)).start();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // Wont be called as service is not bound
        Log.d(LOG_TAG, "In onBind");
        return null;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(LOG_TAG, "In onTaskRemoved");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "In onDestroy");
    }

    @Override
    public void onImagesDownloaded() {
        Log.d("RecyclerView", "onImagesDownloaded: service");
        DataHolder.getInstance().setBitmaps(bitmaps);

        Intent broadcastIntent = new Intent();
        broadcastIntent
                .setAction(ServiceActivity.mBroadcastBitmapAction);

//            sendBroadcast(broadcastIntent);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

    }
}