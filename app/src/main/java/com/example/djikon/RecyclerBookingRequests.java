package com.example.djikon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.GlobelClasses.PreferenceData;
import com.example.djikon.ResponseModels.MyBookingRequests;
import com.example.djikon.ResponseModels.SuccessErrorModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecyclerBookingRequests extends RecyclerView.Adapter<RecyclerBookingRequests.ViewHolder>{

    private List<MyBookingRequests> mBookingRequesterArrayList;
    private Context context;
    private static final String ImageUrl = "http://ec2-52-91-44-156.compute-1.amazonaws.com/";


    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_Requester_Profile, img_Accept,img_Cancel;
        public TextView txt_Requester_Name, txt_Service_Name, txt_Discount, txt_Service_Charges,
                txt_Start_Date, txt_End_Date, txt_Address;
        public ProgressBar progressBar;

        public TextView txt_Accept, txt_Message, txt_Cancel;

        public ViewHolder(View itemView){
            super(itemView);
            img_Requester_Profile = itemView.findViewById(R.id.img_requester);
            img_Accept = itemView.findViewById(R.id.accept_check);
            img_Cancel = itemView.findViewById(R.id.img_cancle);

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
            Picasso.get().load((ImageUrl+currentItem.getUser_profile_image()))
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

       holder.txt_Service_Charges.setText(currentItem.getPrice());
       holder.txt_Start_Date.setText(currentItem.getStart_date());
       holder.txt_End_Date.setText(currentItem.getEnd_date());
       holder.txt_Address.setText(currentItem.getAddress());

//        if (st_Activity_Name.equals("Song")) {
//            holder.txt_Accept.setVisibility(View.GONE);
//            holder.txt_Service_Charges.setVisibility(View.GONE);
//            holder.img_accept.setVisibility(View.GONE);
//        } else {
//            holder.txt_Accept.setVisibility(View.VISIBLE);
//            holder.txt_Service_Charges.setVisibility(View.VISIBLE);
//            holder.img_accept.setVisibility(View.VISIBLE);
//        }
       
       holder.txt_Accept.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               new GetAllBookingFromServer(1).execute();
               Toast.makeText(context, "get", Toast.LENGTH_SHORT).show();
           }
       });

        holder.img_Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetAllBookingFromServer(1).execute();
            }
        });

        holder.txt_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetAllBookingFromServer(2).execute();
            }
        });

        holder.img_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetAllBookingFromServer(2).execute();
            }
        });
}

    @Override
    public int getItemCount() {
        return mBookingRequesterArrayList.size();
    }


    private class GetAllBookingFromServer extends AsyncTask<Void,Void,Void> {
        AlertDialog alertDialog;
        int status;

        public GetAllBookingFromServer(int status) {
            this.status = status;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = ApiClient.retrofit(context);
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            String relativeUrl = "bookingStatus/"+ PreferenceData.getUserId(context);
            Call<SuccessErrorModel> call = jsonApiHolder.acceptOrRejectRequest(
                    relativeUrl,
                    status
            );
            call.enqueue(new Callback<SuccessErrorModel>() {
                @Override
                public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                    if(response.isSuccessful()){
                        Log.i("TAG", "onResponse: Done");
                    }else {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog = DialogsUtils.showAlertDialog(context,
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
                            alertDialog = DialogsUtils.showAlertDialog(context,
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
