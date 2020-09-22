package com.ikonholdings.ikoniconnects_subscriber.GlobelClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.AboutModel;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.DisclosureModel;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.MyBookingRequests;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetAppAboutAndDisclosure extends AsyncTask<Void, Void, Void> {

    Context context;
    AlertDialog progressDialog;
    private onGetAbout mOnGetAbout;

    public interface onGetAbout {
        void onGetAbout(String about);
    }

    private void initializeInterface(onGetAbout onGetAbout){
        this.mOnGetAbout = onGetAbout;
    }

    public GetAppAboutAndDisclosure(Context context) {
        this.context = context;
        initializeInterface((onGetAbout) context);
        progressDialog = DialogsUtils.showProgressDialog(context,
                "Getting...","Please Wait while fetching detail from server.");
    }

    @Override
    protected Void doInBackground(Void... voids) {

        Retrofit retrofit = ApiClient.retrofit(context);
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<AboutModel> callAbout = jsonApiHolder.getAbout();


        callAbout.enqueue(new Callback<AboutModel>() {
            @Override
            public void onResponse(Call<AboutModel> call, Response<AboutModel> response) {
                AboutModel data = response.body();
                if(response.isSuccessful()){
                    if (data.getAbout().isEmpty()) {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DialogsUtils.showAlertDialog(context,
                                        false,
                                        "No About",
                                        "it's seems like Admin is not Added about yet.");
                            }
                        });
                    }else {
                       mOnGetAbout.onGetAbout(data.getAbout());
                    }
                }
                 else {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogsUtils.showAlertDialog(context,
                                    false,
                                    "Error",
                                    "Please try again and check your internet connection");
                        }
                    });
                }

                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onFailure(Call<AboutModel> call, Throwable t) {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogsUtils.showResponseMsg(context,true);
                        progressDialog.dismiss();
                    }
                });
            }
        });
        return null;
    }
}
