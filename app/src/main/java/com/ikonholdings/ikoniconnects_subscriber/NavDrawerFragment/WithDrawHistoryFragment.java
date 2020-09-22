package com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerWithDrawHistory;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.WithDrawModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class WithDrawHistoryFragment extends Fragment {

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
        new DownloadPaymentHistory().execute();

       return v;
    }

    private void buildRecyclerView(List<WithDrawModel> list) {
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerWithDrawHistory(list);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private class DownloadPaymentHistory extends AsyncTask<Void,Void,Void> {

        Call<List<WithDrawModel>> call;
        @Override
        protected void onPreExecute() {
            Retrofit retrofit = ApiClient.retrofit(getContext());
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            call = jsonApiHolder.getPaymentDetails();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            call.enqueue(new Callback<List<WithDrawModel>>() {
                @Override
                public void onResponse(Call<List<WithDrawModel>> call, Response<List<WithDrawModel>> response) {

                    if (!response.isSuccessful()) {
                        DialogsUtils.showAlertDialog(getContext(),
                                false,"Error","Something happened wrong\nplease try again!");
                        return;
                    }

                    List<WithDrawModel> requestedSongsModelList = response.body();

                    if(!requestedSongsModelList.isEmpty()){
                        buildRecyclerView(requestedSongsModelList);
                    }else {
                        DialogsUtils.showAlertDialog(getContext(),false,
                                "No History","it's seems like you don't have any payment history yet.");
                    }
                    loadingDialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<WithDrawModel>> call, Throwable t) {
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
