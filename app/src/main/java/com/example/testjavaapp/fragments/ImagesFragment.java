package com.example.testjavaapp.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testjavaapp.R;
import com.example.testjavaapp.adapter.ImageRecyclerAdapter;
import com.example.testjavaapp.util.DownloadCallback;
import com.example.testjavaapp.util.DownloadImageRunnable;
import com.example.testjavaapp.util.DownloadCompleteRunnable;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;


public class ImagesFragment extends Fragment implements DownloadCallback {


    private RecyclerView recyclerView;
    private ImageRecyclerAdapter adapter;
    private Button loadBtn;
    private EditText et_PicturesNumber;
    private EditText et_semaphoreNumber;

    private CyclicBarrier barrier;
    private Semaphore semaphore;
    private CountDownLatch countDownLatch;

    private int numOfPictures;
    private int semaphoreNum;
    private int currentNumOfClicks;

    private ArrayList<String> recyclerImages = new ArrayList<>();
    private ArrayList<Thread> threadQueue = new ArrayList<>();
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_images, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler_view_images);
        adapter = new ImageRecyclerAdapter(getActivity());
        initUI(view);
        initData();
        addItems();
        initRecyclerView();
    }

    private void initUI(View view){
        et_PicturesNumber = view.findViewById(R.id.et_threadNumber);
        et_semaphoreNumber = view.findViewById(R.id.et_semaphoreNum);
        loadBtn = view.findViewById(R.id.button);
        loadBtn.setOnClickListener(view1 -> click());
    }

    private void initData() {
        numOfPictures = 0;
        semaphoreNum = 0;
        currentNumOfClicks = 0;
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void click() {
        //IF FIRST CLICK CHECK INPUT AND PROCEED
        if (numOfPictures == 0) {
            if (inputIsWrong(et_PicturesNumber.getText().toString()) || inputIsWrong(et_semaphoreNumber.getText().toString())) {
                Toast.makeText(getActivity(), "Wrong input", Toast.LENGTH_SHORT).show();
                return;
            } else {
                numOfPictures = Integer.parseInt(et_PicturesNumber.getText().toString());
                semaphoreNum = Integer.parseInt(et_semaphoreNumber.getText().toString());
                setThreadConfigurations(numOfPictures, semaphoreNum);
                setUiEnabled(false);
            }
        }
        //ADD IMAGE TO DOWNLOAD QUEUE
        //currentNumOfClicks % urls.size() USED TO DOWNLOAD IMAGES IN CYCLE IN CASE MAX CLICKS EXCEEDS URLS SIZE
        if (currentNumOfClicks < numOfPictures) {
            addImageToDownload(recyclerImages.get(currentNumOfClicks % recyclerImages.size()));
            currentNumOfClicks++;
        }
        //ONCE CLICK COUNTER REACHED MAX, START DOWNLOADING AND DISABLE UI
        if (currentNumOfClicks != 0 && currentNumOfClicks >= numOfPictures) {
            updateRange();
            setButtonEnabled(false);
        }
    }

    private void addImageToDownload(String imageUrl) {
        threadQueue.add(new Thread(new DownloadImageRunnable(barrier, semaphore, countDownLatch, imageUrl, bitmaps)));
    }

    private void setThreadConfigurations(int threads, int semaphoreNum) {
        barrier = new CyclicBarrier(threads);
        semaphore = new Semaphore(semaphoreNum, true);
        countDownLatch = new CountDownLatch(threads);
        addCompleteTask();
    }

    private void addCompleteTask() {
        threadQueue.add(new Thread(new DownloadCompleteRunnable(this, countDownLatch)));
    }

    private void updateRange() {
        for (Thread t : threadQueue)
            t.start();
    }

    private void setUiEnabled(boolean b) {
        et_PicturesNumber.setEnabled(b);
        et_semaphoreNumber.setEnabled(b);
    }

    private void setButtonEnabled(boolean b) {
        loadBtn.setEnabled(b);
    }

    private boolean inputIsWrong(String text) {
        return (text.trim().isEmpty() || Integer.parseInt(text) <= 0);
    }


    private void resetData() {
        currentNumOfClicks = 0;
        numOfPictures = 0;
        threadQueue.clear();
        bitmaps.clear();
        setUiEnabled(true);
        setButtonEnabled(true);
    }


    //FILL ARRAY WITH IMAGES URL ADDRESSES
    private void addItems() {
        if (!recyclerImages.isEmpty())
            return;

        recyclerImages.add("https://s3.amazonaws.com/cdn-origin-etr.akc.org/wp-content/uploads/2017/11/12193133/German-Shepherd-Puppy-Fetch.jpg");
        recyclerImages.add("https://i.ytimg.com/vi/mRf3-JkwqfU/hqdefault.jpg");
        recyclerImages.add("https://thumbor.forbes.com/thumbor/960x0/https%3A%2F%2Fblogs-images.forbes.com%2Fscottmendelson%2Ffiles%2F2017%2F05%2Fpirates_of_the_caribbean_dead_men_tell_no_tales_by_mintmovi3-db23j4w.jpg");
        recyclerImages.add("https://i.ytimg.com/vi/DItov3CYM_w/hqdefault.jpg");
        recyclerImages.add("https://cnet3.cbsistatic.com/img/rdEm2TvKo_XDj2mRwdgHa5Iolbo=/1092x0/2019/08/26/56497cd9-7fe4-438c-8b11-5240c29a317e/ebff9hkw4aegbfj.jpg");
        recyclerImages.add("https://cdn.vox-cdn.com/thumbor/pAOIY3gvUyx3j97J7gnQnGTWVoc=/0x0:1920x1080/1200x800/filters:focal(725x485:1031x791)/cdn.vox-cdn.com/uploads/chorus_image/image/53153749/legobatmancover.0.jpg");
    }

    @Override
    public void onImagesDownloaded() {
        Toast.makeText(getActivity(), "Downloading complete", Toast.LENGTH_SHORT).show();
        if (bitmaps.size() > 0)
            adapter.updateImages(bitmaps, bitmaps.size());
        resetData();
    }
}
