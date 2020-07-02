package com.example.djikon;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerCancelAndAcceptRequests extends RecyclerView.Adapter<RecyclerCancelAndAcceptRequests.ViewHolder>{

    private ArrayList<CacelAndAcceptRequest_Model> mCancleRequestArrayList;
    private String st_Activtiy;


    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_Requester_Profile, img_Accept_Or_Cancel_Sign;

        public TextView txt_Requester_Name, txt_Service_Name, txt_Discount, txt_Service_Charges,
               txt_Address;




        public ViewHolder(View itemView){
            super(itemView);
            img_Requester_Profile = itemView.findViewById(R.id.img_requester);
            img_Accept_Or_Cancel_Sign = itemView.findViewById(R.id.img_cancle_accept);

            txt_Requester_Name = itemView.findViewById(R.id.txt_requester_name);
            txt_Service_Name = itemView.findViewById(R.id.txt_sirvice_name);
            txt_Discount = itemView.findViewById(R.id.txt_discount);
            txt_Service_Charges = itemView.findViewById(R.id.txt_sirvice_charges);

            txt_Address = itemView.findViewById(R.id.txt_address);


        }
    }

//constructor
    public RecyclerCancelAndAcceptRequests(ArrayList<CacelAndAcceptRequest_Model> cacelAndAcceptRequest_modelArrayList, String ActivityName) {
        this.mCancleRequestArrayList = cacelAndAcceptRequest_modelArrayList;
        this.st_Activtiy = ActivityName;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cancel_accept_request,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CacelAndAcceptRequest_Model currentItem = mCancleRequestArrayList.get(position);

       holder.img_Requester_Profile.setImageResource(currentItem.getRequester_image());

       holder.txt_Requester_Name.setText(currentItem.getRequester_name());
       holder.txt_Service_Name.setText(currentItem.getService_name());

       holder.txt_Service_Charges.setText(currentItem.getService_charges());
       holder.txt_Address.setText(currentItem.getAddress());



       if (st_Activtiy.equals("Cancel")){
           holder.img_Accept_Or_Cancel_Sign.setImageResource(R.drawable.ic_cancel_red);
       }
       else {
           holder.img_Accept_Or_Cancel_Sign.setImageResource(R.drawable.ic_check_circle);
       }

}

    @Override
    public int getItemCount() {
        return mCancleRequestArrayList.size();
    }
}
