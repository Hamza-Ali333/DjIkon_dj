package com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerMyFeed;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerNotification;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.MyFeedBlogModel;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.NotificationModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;


public class NotificationFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private AlertDialog loadingDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_notification,container,false);
        mRecyclerView = v.findViewById(R.id.recyclerViewSubscribeArtist);

        loadingDialog = DialogsUtils.showLoadingDialogue(getContext());
        new DownloadThisArtistBlogs().execute();

       return v;
    }

    private class DownloadThisArtistBlogs extends AsyncTask<Void,Void,Void> {

        Call<List<NotificationModel>> call;
        @Override
        protected void onPreExecute() {
            Retrofit retrofit = ApiClient.retrofit(getContext());
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            call = jsonApiHolder.getNotification();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            call.enqueue(new Callback<List<NotificationModel>>() {
                @Override
                public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {

                    if (!response.isSuccessful()) {
                        loadingDialog.dismiss();
                        DialogsUtils.showAlertDialog(getContext(),
                                false,"Error","Something happened wrong\nplease try again!");
                        return;
                    }

                    List<NotificationModel> notificationList = response.body();
                    if(!notificationList.isEmpty()){
                        buildRecyclerView(notificationList);
                    }else {
                        DialogsUtils.showAlertDialog(getContext(),
                                false,"No Notification","You don't have any notification yet!");
                    }
                    loadingDialog.dismiss();


                }

                @Override
                public void onFailure(Call<List<NotificationModel>> call, Throwable t) {

                            loadingDialog.dismiss();
                            DialogsUtils.showResponseMsg(getContext(),true);
                        }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private void buildRecyclerView(List<NotificationModel> list){
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new RecyclerNotification(list,getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
