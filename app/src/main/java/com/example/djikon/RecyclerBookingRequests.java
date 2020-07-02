package com.example.djikon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerBookingRequests extends RecyclerView.Adapter<RecyclerBookingRequests.ViewHolder>{

    private ArrayList<BookingRequest_Model> mBookingRequesterArrayList;
    private String st_Activity_Name;


    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_Requester_Profile,img_accept;
        public TextView txt_Requester_Name, txt_Service_Name, txt_Discount, txt_Service_Charges,
                txt_Start_Date, txt_End_Date, txt_Address;

        public TextView txt_Accept, txt_Message, txt_Cancel;



        public ViewHolder(View itemView){
            super(itemView);
            img_Requester_Profile = itemView.findViewById(R.id.img_requester);
            img_accept = itemView.findViewById(R.id.accept_check);

            txt_Requester_Name = itemView.findViewById(R.id.txt_requester_name);
            txt_Service_Name = itemView.findViewById(R.id.txt_sirvice_name);
            txt_Discount = itemView.findViewById(R.id.txt_discount);
            txt_Service_Charges = itemView.findViewById(R.id.txt_sirvice_charges);
            txt_Start_Date = itemView.findViewById(R.id.txt_start_date);
            txt_End_Date = itemView.findViewById(R.id.txt_end_date);
            txt_Address = itemView.findViewById(R.id.txt_address);
            txt_Accept = itemView.findViewById(R.id.txt_accept);
            txt_Message = itemView.findViewById(R.id.txt_message);
            txt_Cancel = itemView.findViewById(R.id.txt_cancel);

        }
    }

//constructor
    public RecyclerBookingRequests(ArrayList<BookingRequest_Model> bookingRequest_modelArrayList,String ActivityName) {
        this.mBookingRequesterArrayList = bookingRequest_modelArrayList;
        this.st_Activity_Name = ActivityName;

    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_request,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BookingRequest_Model currentItem = mBookingRequesterArrayList.get(position);

       holder.img_Requester_Profile.setImageResource(currentItem.getRequester_image());

       holder.txt_Requester_Name.setText(currentItem.getRequester_name());
       holder.txt_Service_Name.setText(currentItem.getService_name());


       holder.txt_Service_Charges.setText(currentItem.getService_charges());
       holder.txt_Start_Date.setText(currentItem.getStart_date());
       holder.txt_End_Date.setText(currentItem.getEnd_date());
       holder.txt_Address.setText(currentItem.getAddress());

        if (st_Activity_Name.equals("Song")) {
            holder.txt_Accept.setVisibility(View.GONE);
            holder.txt_Service_Charges.setVisibility(View.GONE);
            holder.img_accept.setVisibility(View.GONE);
        } else {
            holder.txt_Accept.setVisibility(View.VISIBLE);
            holder.txt_Service_Charges.setVisibility(View.VISIBLE);
            holder.img_accept.setVisibility(View.VISIBLE);
        }


       
       holder.txt_Accept.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(view.getContext(), "This Feature Will Available Soon", Toast.LENGTH_SHORT).show();
           }
       });


}

    @Override
    public int getItemCount() {
        return mBookingRequesterArrayList.size();
    }
}
