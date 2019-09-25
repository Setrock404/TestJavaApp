package com.example.testjavaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testjavaapp.adaptor.ImageRecyclerAdaptor;
import com.example.testjavaapp.data.DataHolder;
import com.example.testjavaapp.service.BroadcastService;

import java.util.ArrayList;

public class ServiceActivity extends AppCompatActivity {

    public static final String mBroadcastBitmapAction = "com.truiton.broadcast.string";
    private static final String TAG = "RecyclerView";
    private RecyclerView recyclerView;
    private ImageRecyclerAdaptor adapter;
    private Button btn_load;
    private EditText et_barrierNum;
    private EditText et_semaphoreNum;

    private int numOfPictures;
    private int semaphoreNum;
    private int currentNumOfClicks;

    private ArrayList<String> urls = new ArrayList<>();
    private ArrayList<String> urlsToLoad = new ArrayList<>();
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();

    private IntentFilter mIntentFilter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_images);

        recyclerView = findViewById(R.id.recycler_view_images);
        btn_load = findViewById(R.id.button);
        et_barrierNum = findViewById(R.id.et_threadNumber);
        et_semaphoreNum = findViewById(R.id.et_semaphoreNum);

        numOfPictures = 0;
        semaphoreNum = 0;
        currentNumOfClicks = 0;

        adapter = new ImageRecyclerAdaptor(this);

        addItems();

        btn_load.setOnClickListener(view1 -> click());
        initRecyclerView();

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastBitmapAction);

    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void click(){

        //IF FIRST CLICK CHECK INPUT AND PROCEED
        if (numOfPictures == 0) {
            if (inputIsWrong(et_barrierNum.getText().toString()) || inputIsWrong(et_semaphoreNum.getText().toString())) {
                Toast.makeText(this, "Wrong input", Toast.LENGTH_SHORT).show();
                return;
            } else {
                numOfPictures = Integer.parseInt(et_barrierNum.getText().toString());
                semaphoreNum = Integer.parseInt(et_semaphoreNum.getText().toString());
                setUiEnabled(false);
            }
        }
        //ADD IMAGE TO DOWNLOAD QUEUE
        //currentNumOfClicks % urls.size() USED TO DOWNLOAD IMAGES IN CYCLE IN CASE MAX CLICKS EXCEEDS URLS SIZE
        if (currentNumOfClicks < numOfPictures) {
            addImageToDownload(urls.get(currentNumOfClicks % urls.size()));
            currentNumOfClicks++;
        }
        //ONCE CLICK COUNTER REACHED MAX, START DOWNLOADING AND DISABLE UI
        if (currentNumOfClicks != 0 && currentNumOfClicks >= numOfPictures) {
            loadWithService();
            setButtonEnabled(false);
        }
    }

    private void loadWithService() {
        Intent serviceIntent = new Intent(this, BroadcastService.class);
        serviceIntent.putExtra("barrier_num", numOfPictures);
        serviceIntent.putExtra("semaphore_num",semaphoreNum);
        serviceIntent.putStringArrayListExtra("url_list",urlsToLoad);
        startService(serviceIntent);
    }

    private boolean inputIsWrong(String text) {
        return (text.trim().isEmpty() && Integer.parseInt(text) <= 0);
    }

    private void addImageToDownload(String imageUrl) {
        urlsToLoad.add(imageUrl);
    }

    private void setUiEnabled(boolean b) {
        et_barrierNum.setEnabled(b);
        et_semaphoreNum.setEnabled(b);
    }

    private void setButtonEnabled(boolean b) {
        btn_load.setEnabled(b);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter(mBroadcastBitmapAction));

    }

    private void addItems() {
        if (!urls.isEmpty())
            return;

        urls.add("https://s3.amazonaws.com/cdn-origin-etr.akc.org/wp-content/uploads/2017/11/12193133/German-Shepherd-Puppy-Fetch.jpg");
        urls.add("https://i.ytimg.com/vi/mRf3-JkwqfU/hqdefault.jpg");
        urls.add("https://thumbor.forbes.com/thumbor/960x0/https%3A%2F%2Fblogs-images.forbes.com%2Fscottmendelson%2Ffiles%2F2017%2F05%2Fpirates_of_the_caribbean_dead_men_tell_no_tales_by_mintmovi3-db23j4w.jpg");
        urls.add("https://i.ytimg.com/vi/DItov3CYM_w/hqdefault.jpg");
        urls.add("https://cnet3.cbsistatic.com/img/rdEm2TvKo_XDj2mRwdgHa5Iolbo=/1092x0/2019/08/26/56497cd9-7fe4-438c-8b11-5240c29a317e/ebff9hkw4aegbfj.jpg");
        urls.add("https://cdn.vox-cdn.com/thumbor/pAOIY3gvUyx3j97J7gnQnGTWVoc=/0x0:1920x1080/1200x800/filters:focal(725x485:1031x791)/cdn.vox-cdn.com/uploads/chorus_image/image/53153749/legobatmancover.0.jpg");

    }

    private void resetData(){
        currentNumOfClicks = 0;
        numOfPictures = 0;
        bitmaps.clear();
        urlsToLoad.clear();
        setUiEnabled(true);
        setButtonEnabled(true);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "onReceive: Message received");
            bitmaps = DataHolder.getInstance().getBitmaps();
            Toast.makeText(ServiceActivity.this, "Downloading complete", Toast.LENGTH_SHORT).show();
            adapter.updateImages(bitmaps, numOfPictures);

            resetData();

            Intent stopIntent = new Intent(ServiceActivity.this,
                    BroadcastService.class);
            stopService(stopIntent);


        }
    };

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onPause();
    }

}
