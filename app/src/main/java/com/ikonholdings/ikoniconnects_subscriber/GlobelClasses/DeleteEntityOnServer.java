package com.ikonholdings.ikoniconnects_subscriber.GlobelClasses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.MainActivity;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DeleteEntityOnServer extends AsyncTask<Void,Void,Void> {
        private ProgressDialog progressDialog;
        private Context context;
        private String Url;

        public DeleteEntityOnServer(String Url, Context context) {
            this.context = context;
            this.Url = Url;
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
            Call<SuccessErrorModel> call = jsonApiHolder.deleteEntry(Url);

            call.enqueue(new retrofit2.Callback<SuccessErrorModel>() {
                @Override
                public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                    if(response.isSuccessful()){
                        context.startActivity(new Intent(context, MainActivity.class));
                    }else {
                                DialogsUtils.showAlertDialog(context,
                                        false,
                                        "Error",
                                        "Please try again and check your internet connection");
                    }
                    progressDialog.dismiss();
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
