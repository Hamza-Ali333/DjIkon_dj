package com.Ikonholdings.ikoniconnects_subscriber.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects_subscriber.R;
import com.Ikonholdings.ikoniconnects_subscriber.ResponseModels.MyBookingRequests;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerCancelAndAcceptRequests extends RecyclerView.Adapter<RecyclerCancelAndAcceptRequests.ViewHolder> {

    private List<MyBookingRequests> mBookingRequesterArrayList;
    private Boolean runningForAccept;

    //view holder class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_Requester_Profile, img_CancelSign, img_AcceptSign;
        public TextView txt_Requester_Name, txt_Service_Name, txt_ServiceType, txt_Service_Charges,
                txt_Start_Date, txt_End_Date, txt_Address, txt_Email, txt_PhoneNo;
        public ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            img_Requester_Profile = itemView.findViewById(R.id.img_requester);
            img_CancelSign = itemView.findViewById(R.id.cancel_sign);
            img_AcceptSign = itemView.findViewById(R.id.img_accept);

            txt_Requester_Name = itemView.findViewById(R.id.txt_requester_name);
            txt_Service_Name = itemView.findViewById(R.id.serviceName);
            txt_ServiceType = itemView.findViewById(R.id.serviceType);
            txt_Service_Charges = itemView.findViewById(R.id.txt_sirvice_charges);
            txt_Start_Date = itemView.findViewById(R.id.txt_start_date);
            txt_End_Date = itemView.findViewById(R.id.txt_end_date);
            txt_Email = itemView.findViewById(R.id.email);
            txt_PhoneNo = itemView.findViewById(R.id.phoneNo);
            txt_Address = itemView.findViewById(R.id.txt_address);

            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    //constructor
    public RecyclerCancelAndAcceptRequests(List<MyBookingRequests> bookingRequest_modelArrayList,Boolean runningForAccepts) {
        this.mBookingRequesterArrayList = bookingRequest_modelArrayList;
        this.runningForAccept = runningForAccepts;
    }

    @Override
    public RecyclerCancelAndAcceptRequests.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cancel_accept_request, parent, false);
        RecyclerCancelAndAcceptRequests.ViewHolder viewHolder = new RecyclerCancelAndAcceptRequests.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerCancelAndAcceptRequests.ViewHolder holder, final int position) {
        final MyBookingRequests currentItem = mBookingRequesterArrayList.get(position);
        if (!currentItem.getUser_profile_image().equals("no") &&
                currentItem.getUser_profile_image() != null) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Picasso.get().load((ApiClient.Base_Url + currentItem.getUser_profile_image()))
                    .into(holder.img_Requester_Profile, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                    });
        }

        if(runningForAccept){
            holder.img_CancelSign.setVisibility(View.GONE);
            holder.img_AcceptSign.setVisibility(View.VISIBLE);
        }else {
            holder.img_CancelSign.setVisibility(View.VISIBLE);
            holder.img_AcceptSign.setVisibility(View.GONE);
        }

        holder.txt_Requester_Name.setText(currentItem.getName());
        holder.txt_Service_Name.setText(currentItem.getService_name());
        holder.txt_ServiceType.setText(currentItem.getService_price_type());

        holder.txt_Service_Charges.setText("$" + currentItem.getPrice());
        holder.txt_Start_Date.setText(currentItem.getStart_date());
        holder.txt_End_Date.setText(currentItem.getEnd_date());
        holder.txt_Email.setText(currentItem.getEmail());
        holder.txt_PhoneNo.setText(currentItem.getPhone());
        holder.txt_Address.setText(currentItem.getAddress());

    }

    @Override
    public int getItemCount() {
        return mBookingRequesterArrayList.size();
    }

}