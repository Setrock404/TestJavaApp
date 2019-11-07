package com.example.testjavaapp.adapter;

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


public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.ImageViewHolder> {

    private int lastPosition = -1;
    private ArrayList<Bitmap> bitmaps;

    private Context context;

    public ImageRecyclerAdapter(Context context) {
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

