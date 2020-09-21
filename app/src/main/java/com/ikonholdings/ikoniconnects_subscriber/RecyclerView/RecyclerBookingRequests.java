package com.ikonholdings.ikoniconnects_subscriber.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.MyBookingRequests;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecyclerBookingRequests extends RecyclerView.Adapter<RecyclerBookingRequests.ViewHolder>{

    private List<MyBookingRequests> mBookingRequesterArrayList;
    private Context context;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_Requester_Profile;
        public Button btn_Accept, btn_Message, btn_Reject;
        public TextView txt_Requester_Name, txt_Service_Name, txt_ServiceType,txt_Service_Charges,
                txt_Start_Date, txt_End_Date, txt_Address, txt_Email,txt_PhoneNo;
        public ProgressBar progressBar;

        public ViewHolder(View itemView){
            super(itemView);
            img_Requester_Profile = itemView.findViewById(R.id.img_requester);
            btn_Accept = itemView.findViewById(R.id.accept);
            btn_Reject = itemView.findViewById(R.id.reject);
            btn_Message = itemView.findViewById(R.id.message);

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
    public RecyclerBookingRequests(List<MyBookingRequests> bookingRequest_modelArrayList, Context context) {
        this.mBookingRequesterArrayList = bookingRequest_modelArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_request,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MyBookingRequests currentItem = mBookingRequesterArrayList.get(position);
        if(!currentItem.getUser_profile_image().equals("no") &&
                currentItem.getUser_profile_image() != null) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Picasso.get().load((ApiClient.Base_Url+currentItem.getUser_profile_image()))
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

       holder.txt_Requester_Name.setText(currentItem.getName());
       holder.txt_Service_Name.setText(currentItem.getService_name());
       holder.txt_ServiceType.setText(currentItem.getService_price_type());

       holder.txt_Service_Charges.setText("$"+currentItem.getPrice());
       holder.txt_Start_Date.setText(currentItem.getStart_date());
       holder.txt_End_Date.setText(currentItem.getEnd_date());
       holder.txt_Email.setText(currentItem.getEmail());
       holder.txt_PhoneNo.setText(currentItem.getPhone());
       holder.txt_Address.setText(currentItem.getAddress());
       
       holder.btn_Accept.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               new AcceptRejectRequest(1,
                       position,
                       currentItem.getId()).execute();
           }
       });

        holder.btn_Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AcceptRejectRequest(2,
                        position,
                        currentItem.getId()).execute();
            }
        });
}

    @Override
    public int getItemCount() {
        return mBookingRequesterArrayList.size();
    }


    private class AcceptRejectRequest extends AsyncTask<Void,Void,Void> {
        ProgressDialog progressDialog;
        int status;
        int position;
        int bookingId;


        public AcceptRejectRequest(int status, int position, int bookingId) {
            this.status = status;
            this.position = position;
            this.bookingId = bookingId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = DialogsUtils.showProgressDialog(context,
                    "Working...",
                    "Please wait. While connecting with the server.");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = ApiClient.retrofit(context);
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            String relativeUrl = "bookingStatus/"+ bookingId;
            Call<SuccessErrorModel> call = jsonApiHolder.acceptOrRejectRequest(
                    relativeUrl,
                    status
            );
            call.enqueue(new Callback<SuccessErrorModel>() {
                @Override
                public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                    if(response.isSuccessful()){
                        progressDialog.dismiss();
                        switch (status){
                            case 1:
                                Toast.makeText(context, "Request Accepted" , Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(context, "Request Canceled" , Toast.LENGTH_SHORT).show();
                                break;
                        }
                        mBookingRequesterArrayList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mBookingRequesterArrayList.size());
                    }else {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                DialogsUtils.showAlertDialog(context,
                                        false,
                                        "Error",
                                        "Please try again and check your internet connection");
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            DialogsUtils.showAlertDialog(context,
                                    false,
                                    "No Server Connection",
                                    t.getMessage());
                        }
                    });
                }
            });
            return null;
        }
    }

}