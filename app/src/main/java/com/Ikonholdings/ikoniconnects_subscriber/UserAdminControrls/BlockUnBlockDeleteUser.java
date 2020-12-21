package com.Ikonholdings.ikoniconnects_subscriber.UserAdminControrls;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.Ikonholdings.ikoniconnects_subscriber.Activities.MainActivity;
import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BlockUnBlockDeleteUser extends AsyncTask<Void,Void, Void> {
    Retrofit  retrofit;
     Context context;
    String Url;
    Integer StatusCode;
    ProgressDialog progressDialog;

    public BlockUnBlockDeleteUser(String url, Integer statusCode, Context context) {
        this.context = context;
        Url = url;
        StatusCode = statusCode;
        this.retrofit = ApiClient.retrofit(context);
        progressDialog = DialogsUtils.showProgressDialog(context,
                "Working",
                "Please wait. While connecting with the server.");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        JSONApiHolder  jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<Void> uploadCall = jsonApiHolder.blockUnBlock(Url, StatusCode);
        uploadCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("fromAdminControl", true);
                    context.startActivity(intent);
                }else {
                    progressDialog.dismiss();
                    DialogsUtils.showResponseMsg(context,false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                DialogsUtils.showResponseMsg(context,true);
            }
        });
        return null;
    }
}
