package com.ikonholdings.ikoniconnects_subscriber.GlobelClasses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DeleteEnteryOnServer extends AsyncTask<Void,Void,Void> {

        ProgressDialog progressDialog;
        int position;
        int itemId;
        Context context;


        public DeleteEnteryOnServer(String Url,int position,int itemId,Context context) {
            this.position = position;
            this.itemId = itemId;
            this.context = context;
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
            Call<SuccessErrorModel> call = jsonApiHolder.deleteBlog(
                    itemId
            );
            call.enqueue(new retrofit2.Callback<SuccessErrorModel>() {
                @Override
                public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                    if(response.isSuccessful()){
                        progressDialog.dismiss();
//                        mBlogs.remove(position);
//                        notifyItemRemoved(position);
//                        notifyItemRangeChanged(position, mBlogs.size());
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
