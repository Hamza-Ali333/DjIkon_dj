package com.Ikonholdings.ikoniconnects_subscriber.GlobelClasses;

import android.content.Context;
import android.os.AsyncTask;

import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects_subscriber.ResponseModels.MainActivityResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetSubscriberDrawerData extends AsyncTask<Boolean, Void, Void> {
   Context mContext;
    public GetSubscriberDrawerData(Context context) {
        this.mContext = context;
    }

    @Override
    protected Void doInBackground(Boolean... booleans) {
        Retrofit retrofit = ApiClient.retrofit(mContext);
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<MainActivityResponseModel> call = jsonApiHolder.getResponse();

        call.enqueue(new Callback<MainActivityResponseModel>() {
            @Override
            public void onResponse(Call<MainActivityResponseModel> call, Response<MainActivityResponseModel> response) {
                if(!response.isSuccessful()){
                    return;
                }
                MainActivityResponseModel data = response.body();
                PreferenceData.setUserName(mContext, data.getFirstname()+" "+ data.getLastname());
                if (data.getProfile_image() != null){
                    PreferenceData.setUserImage(mContext,data.getProfile_image());
                }
            }

            @Override
            public void onFailure(Call<MainActivityResponseModel> call, Throwable t) {

            }
        });
        return null;
    }

}
