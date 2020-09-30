package com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ikonholdings.ikoniconnects_subscriber.AddServiceActivity;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerServices;
import com.ikonholdings.ikoniconnects_subscriber.ServicesModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;


public class ServicesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton btn_Add_New_Services;

    private AlertDialog loadingDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v =  inflater.inflate(R.layout.fragment_services,container,false);
       mRecyclerView = v.findViewById(R.id.services_recycler);

       loadingDialog = DialogsUtils.showLoadingDialogue(getContext());
       new DownloadThisArtistServices().execute();

        btn_Add_New_Services = v.findViewById(R.id.fab);
        btn_Add_New_Services.setOnClickListener(new View.OnClickListener() {
            @java.lang.Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddServiceActivity.class));
            }
        });

       return v;
    }

    private void buildRecyclerView(List<ServicesModel> serviceList) {
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mAdapter = new RecyclerServices(serviceList);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);
    }


    private class DownloadThisArtistServices extends AsyncTask<Void,Void,Void> {

        Call<List<ServicesModel>> call;
        @Override
        protected void onPreExecute() {
            Retrofit retrofit = ApiClient.retrofit(getContext());
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            call = jsonApiHolder.getServices();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            call.enqueue(new Callback<List<ServicesModel>>() {
                @Override
                public void onResponse(Call<List<ServicesModel>> call, Response<List<ServicesModel>> response) {

                    if (!response.isSuccessful()) {
                        Log.i(TAG, "onResponse: "+response.code());
                        loadingDialog.dismiss();
                        DialogsUtils.showAlertDialog(getContext(),
                                false,"Error","Something happened wrong\nplease try again!");
                        return;
                    }

                    List<ServicesModel> services = response.body();

                  if(!services.isEmpty()){
                      buildRecyclerView(services);
                  }
                    loadingDialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<ServicesModel>> call, Throwable t) {
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
