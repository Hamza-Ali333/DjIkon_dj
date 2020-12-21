package com.Ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Ikonholdings.ikoniconnects_subscriber.Activities.AddServiceActivity;
import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.Ikonholdings.ikoniconnects_subscriber.R;
import com.Ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerServices;
import com.Ikonholdings.ikoniconnects_subscriber.ServicesModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    private TextView txt_Msg;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v =  inflater.inflate(R.layout.fragment_services,container,false);
       mRecyclerView = v.findViewById(R.id.services_recycler);
       txt_Msg = v.findViewById(R.id.msg);

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
                      txt_Msg.setVisibility(View.GONE);
                      buildRecyclerView(services);
                  }else {
                      txt_Msg.setVisibility(View.VISIBLE);
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
