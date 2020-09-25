package com.ikonholdings.ikoniconnects_subscriber.GlobelClasses;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DeleteEntityOnServer extends AsyncTask<Void,Void,Void> {
        ProgressDialog progressDialog;

        Context context;
        String Url;
        Boolean BlockUser;
        int action;

        String Title;
        String Msg;

        public DeleteEntityOnServer(String Url, Context context) {
            this.context = context;
            this.Url = Url;
            Title = "User Deleted";
            Msg = "User Deleted Successfully.";
            BlockUser = false;
        }

    public DeleteEntityOnServer(int action ,String Url, Context context) {
        this.context = context;
        this.Url = Url;
        this.action = action;

        if(action==2){
            Title = "User Blocked";
            Msg = "User Blocked Successfully.";
        }else {
            Title = "User UnBlock";
            Msg = "User UnBlock Successfully.";
        }

        //2 for Block User 1 for Unblock User
        BlockUser = true;
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
            Call<SuccessErrorModel> call = null;
            if(BlockUser){
                call = jsonApiHolder.blockUser(
                        Url,
                        action
                );
            }else {
                call = jsonApiHolder.deleteEntry(
                        Url
                );

            }

            call.enqueue(new retrofit2.Callback<SuccessErrorModel>() {
                @Override
                public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                    if(response.isSuccessful()){
                        progressDialog.dismiss();

                        DialogsUtils.showSuccessDialog(context, Title,
                                Msg);
//                        mBlogs.remove(position);
//                        notifyItemRemoved(position);
//                        notifyItemRangeChanged(position, mBlogs.size());
                    }else {
                                progressDialog.dismiss();
                                DialogsUtils.showAlertDialog(context,
                                        false,
                                        "Error",
                                        "Please try again and check your internet connection");
                    }
                }

                @Override
                public void onFailure(Call<SuccessErrorModel> call, Throwable t) {

                            progressDialog.dismiss();
                            DialogsUtils.showAlertDialog(context,
                                    false,
                                    "No Server Connection",
                                    t.getMessage());

                }
            });
            return null;
        }
    }
