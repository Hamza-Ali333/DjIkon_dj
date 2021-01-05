package com.Ikonholdings.ikoniconnects_subscriber.GlobelClasses;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects_subscriber.ResponseModels.FollowersModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetUsers extends AsyncTask<Void, Void, Void> {
    Context context;
    TextView txt_total, txt_Msg;
    AlertDialog loadingDialog;
    List<FollowersModel> requestList;
    String Url;

    private onServerResponse onServerResponse;

    public interface onServerResponse {
        void onResponse(List<FollowersModel> requestList);
    }

    private void initializeGetBookingInterFace(onServerResponse onServerResponse){
        this.onServerResponse = onServerResponse;
    }

    public GetUsers(TextView total, TextView txt_Msg, String Url) {
        this.context = total.getContext();
        this.txt_total = total;
        this.txt_Msg = txt_Msg;
        this.Url = Url;
        loadingDialog = DialogsUtils.showLoadingDialogue(context);
        initializeGetBookingInterFace((onServerResponse) context);
        requestList = new ArrayList<>();
    }

    @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = ApiClient.retrofit(context);
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            Call<List<FollowersModel>> call = jsonApiHolder.getFollowers(Url);

            call.enqueue(new Callback<List<FollowersModel>>() {
                @Override
                public void onResponse(Call<List<FollowersModel>> call, Response<List<FollowersModel>> response) {
                    if(response.isSuccessful()){
                        requestList = (List<FollowersModel>) response.body();
                        if(requestList.isEmpty()) {
                            txt_Msg.setVisibility(View.VISIBLE);

                        }else{
                            txt_Msg.setVisibility(View.GONE);
                            onServerResponse.onResponse(requestList);
                        }
                        txt_total.setText("You have total " + String.valueOf(requestList.size() + " followers."));
                    }else {
                                DialogsUtils.showAlertDialog(context,
                                        false,
                                        "Error",
                                        "Please try again and check your internet connection");

                    }
                    loadingDialog.dismiss();
                }
                @Override
                public void onFailure(Call<List<FollowersModel>> call, Throwable t) {
                            DialogsUtils.showResponseMsg(context,true);
                            loadingDialog.dismiss();

                }
            });
            return null;
        }

}
