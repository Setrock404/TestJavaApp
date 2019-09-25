package com.example.testjavaapp.adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testjavaapp.R;


public class ImageRecyclerAdaptor extends RecyclerView.Adapter<ImageRecyclerAdaptor.ImageViewHolder> {

    public static final String TAG = "RecyclerView";

    private int lastPosition = -1;

    private ArrayList<Bitmap> bitmaps;

    private Context context;

    public ImageRecyclerAdaptor(Context context) {
        this.context = context;
        bitmaps = new ArrayList<>();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_list_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.imageView.setImageBitmap(bitmaps.get(position));
        setAnimation(holder.imageView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fall_down_anim);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

//    public void updateRange(int numOfImagesAdded) {
//
////        for(DownloadImageAsync2 a: asyncQueue)
////        {
////            Log.d(TAG, "execute task...");
////            //a.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
////            a.execute();
////        }
//
////        DownloadImageAsync2 downloadImageAsync2 = new DownloadImageAsync2(barrier,semaphore, countDownLatch,images.get(0),bitmaps);
////        downloadImageAsync2.execute();
////        DownloadImageAsync2 downloadImageAsync3 = new DownloadImageAsync2(barrier,semaphore, countDownLatch,images.get(1),bitmaps);
////        downloadImageAsync3.execute();
////        DownloadImageAsync2 downloadImageAsync4 = new DownloadImageAsync2(barrier,semaphore, countDownLatch,images.get(2),bitmaps);
////        downloadImageAsync4.execute();
//
////        Thread thread1 = new Thread(new DownloadImageThread(barrier,semaphore, countDownLatch,images.get(0),bitmaps));
////        thread1.start();
////        Thread thread2 = new Thread(new DownloadImageThread(barrier,semaphore, countDownLatch,images.get(1),bitmaps));
////        thread2.start();
////        Thread thread3 = new Thread(new DownloadImageThread(barrier,semaphore, countDownLatch,images.get(2),bitmaps));
////        thread3.start();
//        this.numOfImagesAdded = numOfImagesAdded;
//
//        for (Thread t : threadQueue)
//            t.start();
//    }

//    public void setThreadConfigurations(int threads, int semaphoreNum) {
//        barrier = new CyclicBarrier(threads);
//        semaphore = new Semaphore(semaphoreNum, true);
//        countDownLatch = new CountDownLatch(threads);
//        addCompleteTask();
//    }
//
//    private void addCompleteTask() {
//        threadQueue.add(new Thread(new DownloadImageThreadComplete(this, countDownLatch)));
//    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateImages(ArrayList<Bitmap> newBitmaps, int numOfImagesAdded) {
        bitmaps.addAll(newBitmaps);
        notifyItemRangeInserted(bitmaps.size() - numOfImagesAdded, bitmaps.size());
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.list_imageView);
        }

    }
}

