package com.ikonholdings.ikoniconnects_subscriber.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.Models.GalleryImagesUri;
import com.ikonholdings.ikoniconnects_subscriber.R;

import java.util.List;

public class RecyclerShowGalleryImages extends RecyclerView.Adapter<RecyclerShowGalleryImages.ViewHolder>{

    private List<GalleryImagesUri> mImageUri;
    private onItemClickListner onItemClickListner;

    public interface onItemClickListner{
        void onClick(Integer position);//pass your object types.
    }

    //initailizing
    public void setOnItemClickListner(RecyclerShowGalleryImages.onItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView imageView;
        public ImageView imgClose;

        public ViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            imgClose = itemView.findViewById(R.id.delete);
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

       holder.imgClose.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mImageUri.remove(position);
               notifyDataSetChanged();
               onItemClickListner.onClick(position);
           }
       });

}

    @Override
    public int getItemCount() {
        return mImageUri.size();
    }

}
