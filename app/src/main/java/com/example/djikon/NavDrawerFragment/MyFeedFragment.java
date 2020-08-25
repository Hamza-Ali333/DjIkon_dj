package com.example.djikon.NavDrawerFragment;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.Models.MyFeedBlogModel;
import com.example.djikon.R;
import com.example.djikon.RecyclerMyFeed;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;


public class MyFeedFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout pullToRefresh;
    private AlertDialog alertDialog;
    private RelativeLayout rlt_progressBar;

    private FloatingActionButton AddNewBlog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_myfeed,container,false);
        createReferences(v);
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(getContext());

        new DownloadThisArtistBlogs().execute();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rlt_progressBar.setVisibility(View.VISIBLE);
               new DownloadThisArtistBlogs().execute();
                pullToRefresh.setRefreshing(false);
            }
        });

        AddNewBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BlogFragment()).commit();
            }
        });

        return v;
    }

    private void createReferences(View v) {
        mRecyclerView = v.findViewById(R.id.recyclerViewLatestFeed);
        pullToRefresh =v.findViewById(R.id.pullToRefresh);
        rlt_progressBar = v.findViewById(R.id.progressbar);
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

                    if (!response.isSuccessful()) {
                        Log.i(TAG, "onResponse: "+response.code());
                        rlt_progressBar.setVisibility(View.GONE);
                        alertDialog = DialogsUtils.showAlertDialog(getContext(),
                                false,"Error","Something happened wrong\nplease try again!");
                        return;
                    }

                    List<MyFeedBlogModel> blogs = response.body();

                    mAdapter = new RecyclerMyFeed(blogs,getContext());
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setAdapter(mAdapter);
                    rlt_progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<List<MyFeedBlogModel>> call, Throwable t) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog = DialogsUtils.showAlertDialog(getContext(),false,"No Server Connection",t.getMessage());
                            rlt_progressBar.setVisibility(View.INVISIBLE);
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
