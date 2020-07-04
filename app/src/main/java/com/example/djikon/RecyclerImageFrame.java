package com.example.djikon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerImageFrame extends RecyclerView.Adapter<RecyclerImageFrame.ViewHolder>{

    private ArrayList<ImageFrame_Model> mImageFram_Model;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_Frame_image;
       public TextView txt_Title;

        public ViewHolder(View itemView){
            super(itemView);
            img_Frame_image = itemView.findViewById(R.id.frame_image);

            txt_Title = itemView.findViewById(R.id.txt_frame_title);


        }
    }

//constructor
    public RecyclerImageFrame(ArrayList<ImageFrame_Model> imageFrame_modelArrayList) {
        this.mImageFram_Model = imageFrame_modelArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fram_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ImageFrame_Model currentItem = mImageFram_Model.get(position);

       holder.img_Frame_image.setImageResource(currentItem.getImage());
       holder.txt_Title.setText(currentItem.getTitle());

}

    @Override
    public int getItemCount() {
        return mImageFram_Model.size();
    }
}
