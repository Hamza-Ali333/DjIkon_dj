package com.ikonholdings.ikoniconnects_subscriber.GlobelClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Switch;

import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AllowAndDisableToggle extends AsyncTask<Void, Void, Void> {
    AlertDialog loadingDialog;
    String status;
    String title;
    String msg;
    int subscriberId;
    Context context;
    Switch clickedButton;

    private onStatusChange onStatusChange;

    public interface onStatusChange {
        void onComplete(Boolean status);
    }

    private void initializeStatusChangeInterFace(onStatusChange onStatusChange) {
        this.onStatusChange = onStatusChange;
    }

    public AllowAndDisableToggle(Context context, String status, Switch clickedButton) {
        this.status = status;
        this.clickedButton = clickedButton;
        this.context = context;

        loadingDialog = DialogsUtils.showProgressDialog(context, "Working...",
                "Waiting for server response.");
        initializeStatusChangeInterFace((AllowAndDisableToggle.onStatusChange) context);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Retrofit retrofit = ApiClient.retrofit(context);
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<SuccessErrorModel> call = jsonApiHolder.changeStatus(
                "settings/" + subscriberId,
                status);

        call.enqueue(new Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                if (response.isSuccessful()) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismiss();
                        }
                    });
                    onStatusChange.onComplete(true);
                } else {
                    ((Activity) context).runOnUiThread(new Runnable() {
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
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogsUtils.showAlertDialog(context,
                                false,
                                "No Server Connection",
                                t.getMessage());
                        loadingDialog.dismiss();
                    }
                });
            }
        });
        return null;
    }

}

