package com.example.djikon.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.djikon.Models.GalleryImagesUri;
import com.example.djikon.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class RecyclerShowGalleryImages extends RecyclerView.Adapter<RecyclerShowGalleryImages.ViewHolder>{

    private List<GalleryImagesUri> mImageUri;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView imageView;

        public ViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }

//constructor
    public RecyclerShowGalleryImages(List<GalleryImagesUri> chat_List_modelArrayList) {
        this.mImageUri = chat_List_modelArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_images,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       final GalleryImagesUri currentItem = mImageUri.get(position);

       holder.imageView.setImageURI(currentItem.getUri());










}

    @Override
    public int getItemCount() {
        return mImageUri.size();
    }

}
