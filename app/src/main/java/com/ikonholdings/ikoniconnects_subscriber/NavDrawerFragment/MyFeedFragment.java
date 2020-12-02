package com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ikonholdings.ikoniconnects_subscriber.Activities.PackageActivity;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerMyFeed;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.MyFeedBlogModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class
MyFeedFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout pullToRefresh;
    private AlertDialog loadingDialog;

    private FloatingActionButton AddNewBlog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        loadingDialog = DialogsUtils.showLoadingDialogue(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        new DownloadThisArtistBlogs().execute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_myfeed,container,false);
        createReferences(v);
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(getContext());

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               new DownloadThisArtistBlogs().execute();
                pullToRefresh.setRefreshing(false);
            }
        });

        AddNewBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddBlogFragment()).commit();
            }
        });

        return v;
    }

    private void createReferences(View v) {
        mRecyclerView = v.findViewById(R.id.recyclerViewLatestFeed);
        pullToRefresh =v.findViewById(R.id.pullToRefresh);
        AddNewBlog = v.findViewById(R.id.fab);
    }

    private class DownloadThisArtistBlogs extends AsyncTask<Void,Void,Void> {

        Call<List<MyFeedBlogModel>> call;
        @Override
        protected void onPreExecute() {
            Retrofit retrofit = ApiClient.retrofit(getContext());
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            call = jsonApiHolder.getBlogs();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            call.enqueue(new Callback<List<MyFeedBlogModel>>() {
                @Override
                public void onResponse(Call<List<MyFeedBlogModel>> call, Response<List<MyFeedBlogModel>> response) {

                    if (response.isSuccessful()) {
                        List<MyFeedBlogModel> blogs = response.body();

                        mAdapter = new RecyclerMyFeed(blogs,getContext());
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setAdapter(mAdapter);

                    }else if(response.code() == 405){
                        startActivity(new Intent(getContext(), PackageActivity.class));
                    }else{
                        DialogsUtils.showResponseMsg(getContext(),false);
                    }

                    loadingDialog.dismiss();

                }

                @Override
                public void onFailure(Call<List<MyFeedBlogModel>> call, Throwable t) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismiss();
                            DialogsUtils.showAlertDialog(getContext(), false, "Error", t.getMessage());
                            //DialogsUtils.showResponseMsg(getContext(),true);
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
