package com.Ikonholdings.ikoniconnects_subscriber.GlobelClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects_subscriber.ResponseModels.MyBookingRequests;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookingRequestFetcher extends AsyncTask<Void, Void, Void> {
    Context context;
    TextView txt_Total, txt_Msg;
    AlertDialog loadingDialog;
    List<MyBookingRequests> requestList;
    String Url;

    private onRequestProcessComplete onRequestProcessComplete;

    public interface onRequestProcessComplete {
        void onComplete(List<MyBookingRequests> requestList);
    }

    private void initializeGetBookingInterFace(onRequestProcessComplete onRequestProcessComplete){
        this.onRequestProcessComplete = onRequestProcessComplete;
    }

    public BookingRequestFetcher(TextView total, TextView Msg, String Url) {
        this.context = total.getContext();
        this.txt_Total = total;
        this.txt_Msg = Msg;
        this.Url = Url;
        loadingDialog = DialogsUtils.showLoadingDialogue(context);
        initializeGetBookingInterFace((onRequestProcessComplete) context);
        requestList = new ArrayList<>();
    }

    @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = ApiClient.retrofit(context);
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            Call<List<MyBookingRequests>> call = jsonApiHolder.getBookings(Url);

            call.enqueue(new Callback<List<MyBookingRequests>>() {
                @Override
                public void onResponse(Call<List<MyBookingRequests>> call, Response<List<MyBookingRequests>> response) {
                    if(response.isSuccessful()){
                        requestList = (List<MyBookingRequests>) response.body();

                        if(requestList.isEmpty()) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txt_Msg.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            txt_Msg.setVisibility(View.GONE);
                            onRequestProcessComplete.onComplete(requestList);
                        }
                        txt_Total.setText("You have total " + String.valueOf(requestList.size() + " booking requests."));
                        txt_Total.setVisibility(View.VISIBLE);
                        loadingDialog.dismiss();
                    }else {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DialogsUtils.showAlertDialog(context,
                                        false,
                                        "Error",
                                        "Please try again and check your internet connection");
                                loadingDialog.dismiss();
                            }
                        });
                    }
                }
                @Override
                public void onFailure(Call<List<MyBookingRequests>> call, Throwable t) {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogsUtils.showResponseMsg(context,true);
                            loadingDialog.dismiss();
                        }
                    });
                }
            });
            return null;
        }

}