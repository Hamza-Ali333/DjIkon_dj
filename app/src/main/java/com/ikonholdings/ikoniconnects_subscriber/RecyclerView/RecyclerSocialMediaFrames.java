package com.ikonholdings.ikoniconnects_subscriber.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;


import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.FramesModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerSocialMediaFrames extends RecyclerView.Adapter<RecyclerSocialMediaFrames.ViewHolder> {

    private List<FramesModel> frameImageList;



    //view holder class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_Frame;
        public ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            img_Frame = itemView.findViewById(R.id.frame_image);
            progressBar = itemView.findViewById(R.id.progressBarProfile);

        }
    }

    //constructor
    public RecyclerSocialMediaFrames(List<FramesModel> liveArtistModels) {
        this.frameImageList = liveArtistModels;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fram_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FramesModel currentItem = frameImageList.get(position);

        if (currentItem.getFrame() != null && !currentItem.getFrame().equals("no")) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Picasso.get().load(currentItem.getFrame())
                    .placeholder(R.drawable.ic_avatar)
                    .fit()
                    .centerCrop()
                    .into(holder.img_Frame, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                    });
        }//if

    }

    @Override
    public int getItemCount() {
        return frameImageList.size();
    }
}
