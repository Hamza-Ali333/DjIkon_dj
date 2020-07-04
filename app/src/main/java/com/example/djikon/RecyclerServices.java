package com.example.djikon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerServices extends RecyclerView.Adapter<RecyclerServices.ViewHolder>{

    private ArrayList<Services_Model> mServices;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_service_image;
        public TextView txt_Title;
        public TextView txt_Discription;
        public TextView txt_Charges;

        public ViewHolder(View itemView){
            super(itemView);
            img_service_image = itemView.findViewById(R.id.img_servic_image);

            txt_Title = itemView.findViewById(R.id.txt_servic_name);
            txt_Discription = itemView.findViewById(R.id.txt_servic_description);
            txt_Charges = itemView.findViewById(R.id.txt_service_prize);

        }
    }

//constructor
    public RecyclerServices(ArrayList<Services_Model> services_modelArrayList) {
        this.mServices = services_modelArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Services_Model currentItem = mServices.get(position);

       holder.img_service_image.setImageResource(currentItem.getService_image());
       holder.txt_Title.setText(currentItem.getService_Title());
       holder.txt_Discription.setText(currentItem.getService_Discription());
       holder.txt_Charges.setText(currentItem.getCharges());

}

    @Override
    public int getItemCount() {
        return mServices.size();
    }
}
