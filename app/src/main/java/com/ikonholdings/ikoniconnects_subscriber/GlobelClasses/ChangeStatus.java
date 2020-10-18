package com.ikonholdings.ikoniconnects_subscriber.GlobelClasses;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.internal.$Gson$Preconditions;
import com.ikonholdings.ikoniconnects_subscriber.Activities.ProfileSettingActivity;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangeStatus extends AsyncTask<Void,Void,Void> {
    AlertDialog loadingDialog;
    String status;
    String title;
    String msg;
    Context context;

    public ChangeStatus(String status, String title, String msg, Context context) {
        this.status = status;
        this.title = title;
        this.msg = msg;
        this.context = context;
        loadingDialog = DialogsUtils.showProgressDialog(context,"Working...",
                "Waiting for server response.");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Retrofit retrofit = ApiClient.retrofit(context);
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<SuccessErrorModel> call = jsonApiHolder.changeStatus(
                "settings/"+PreferenceData.getUserId(context),
                status);

        call.enqueue(new Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                if(response.isSuccessful()){
                            DialogsUtils.showSuccessDialog(context,
                                    title,
                                    msg);
                }else {
                            DialogsUtils.showAlertDialog(context,
                                    false,
                                    "Error",
                                    "Please try again and check your internet connection");
                }
                loadingDialog.dismiss();
            }
            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {

                        DialogsUtils.showAlertDialog(context,
                                false,
                                "No Server Connection",
                                t.getMessage());
                        loadingDialog.dismiss();

            }
        });
        return null;
    }

}
