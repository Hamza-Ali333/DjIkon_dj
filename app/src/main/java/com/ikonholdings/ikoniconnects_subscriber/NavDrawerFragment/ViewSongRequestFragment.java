package com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

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
import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerSongsRequest;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.RequestedSongsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;


public class ViewSongRequestFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private AlertDialog loadingDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.activity_booking_requests,container,false);

        mRecyclerView = v.findViewById(R.id.recyclerView_booking_request);

        loadingDialog = DialogsUtils.showLoadingDialogue(getContext());
        new DownloadSongsRequests().execute();

       return v;
    }

    private void buildRecyclerView(List<RequestedSongsModel> list) {
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerSongsRequest(list,getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private class DownloadSongsRequests extends AsyncTask<Void,Void,Void> {

        Call<List<RequestedSongsModel>> call;
        @Override
        protected void onPreExecute() {
            Retrofit retrofit = ApiClient.retrofit(getContext());
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            call = jsonApiHolder.getRequestedSongs();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            call.enqueue(new Callback<List<RequestedSongsModel>>() {
                @Override
                public void onResponse(Call<List<RequestedSongsModel>> call, Response<List<RequestedSongsModel>> response) {

                    if (!response.isSuccessful()) {
                        Log.i(TAG, "onResponse: "+response.code());
                        loadingDialog.dismiss();
                        DialogsUtils.showAlertDialog(getContext(),
                                false,"Error","Something happened wrong\nplease try again!");
                        return;
                    }

                    List<RequestedSongsModel> requestedSongsModelList = response.body();

                    if(!requestedSongsModelList.isEmpty()){
                        buildRecyclerView(requestedSongsModelList);
                    }else {
                        DialogsUtils.showAlertDialog(getContext(),false,
                                "No Request","OOPS! you don't have any request yet.");
                    }
                    loadingDialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<RequestedSongsModel>> call, Throwable t) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismiss();
                            DialogsUtils.showResponseMsg(getContext(),true);
                        }
                    });
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
