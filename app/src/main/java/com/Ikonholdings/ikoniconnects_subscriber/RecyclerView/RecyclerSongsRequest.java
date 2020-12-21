package com.Ikonholdings.ikoniconnects_subscriber.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.Ikonholdings.ikoniconnects_subscriber.R;
import com.Ikonholdings.ikoniconnects_subscriber.ResponseModels.RequestedSongsModel;
import com.Ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecyclerSongsRequest extends RecyclerView.Adapter<RecyclerSongsRequest.ViewHolder>{

    private List<RequestedSongsModel> requestedSongsModels;
    private Context context;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public CircularImageView img_msg_Subscriber_Profile;
        public TextView txt_Requester_Name;
        public TextView  txt_SongName;
        public TextView  txt_Date;
        public ProgressBar progressBar;
        public Button btn_Remove;

        public ViewHolder(View itemView){
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBarProfile);
            img_msg_Subscriber_Profile = itemView.findViewById(R.id.img_profile);

            txt_Requester_Name = itemView.findViewById(R.id.txt_user_name);
            txt_SongName = itemView.findViewById(R.id.txt_song);
            txt_Date = itemView.findViewById(R.id.txt_request_date);
            btn_Remove = itemView.findViewById(R.id.remove);
        }

    }

//constructor
    public RecyclerSongsRequest(List<RequestedSongsModel> requestedSongsModels,Context context) {
        this.requestedSongsModels = requestedSongsModels;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_requests,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       final RequestedSongsModel currentItem = requestedSongsModels.get(position);

       holder.txt_Requester_Name.setText(currentItem.getFirstname()+" "+currentItem.getLastname());
       holder.txt_SongName.setText(currentItem.getSong_name());
       holder.txt_Date.setText(currentItem.getRequest_date());

        if (!currentItem.getProfile_image().equals("no")) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Picasso.get().load(ApiClient.Base_Url+currentItem.getProfile_image())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_avatar)
                    .into(holder.img_msg_Subscriber_Profile, new Callback() {
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

        holder.btn_Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteRequest(position,currentItem.getId()).execute();
            }
        });

}

    @Override
    public int getItemCount() {
        return requestedSongsModels.size();
    }


    private class DeleteRequest extends AsyncTask<Void,Void,Void> {
        ProgressDialog progressDialog;
        int position;
        int requestId;


        public DeleteRequest(int position, int requestId) {
            this.position = position;
            this.requestId = requestId;
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
            Call<SuccessErrorModel> call = jsonApiHolder.deleteSongRequest(
                    requestId
            );
            call.enqueue(new retrofit2.Callback<SuccessErrorModel>() {
                @Override
                public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                    if(response.isSuccessful()){
                        progressDialog.dismiss();
                        requestedSongsModels.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, requestedSongsModels.size());
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
