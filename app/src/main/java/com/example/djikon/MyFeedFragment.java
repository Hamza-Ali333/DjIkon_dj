package com.example.djikon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MyFeedFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_myfeed,container,false);

         mRecyclerView = v.findViewById(R.id.recyclerViewLatestFeed);



//        final ArrayList<MyFeed_Model> latestFeedItemArrayList = new ArrayList<>();
//        latestFeedItemArrayList.add(new MyFeed_Model(R.drawable.rectangle2,R.drawable.rectangle,"Hamza","2m ago","you will enjoye the event","1","23"));
//        latestFeedItemArrayList.add(new MyFeed_Model(R.drawable.woman,R.drawable.rectangle2,"Ahmad","6m ago","you will enjoye the event","5","3"));
//        latestFeedItemArrayList.add(new MyFeed_Model(R.drawable.ic_doctor,R.drawable.rectangle,"Bilawal","6m ago","you will enjoye the event","8","23"));
//        latestFeedItemArrayList.add(new MyFeed_Model(R.drawable.woman,R.drawable.rectangle2,"Usama","7m ago","you will enjoye the event","90","34"));



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-54-161-107-128.compute-1.amazonaws.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FeedJsonApi feedJsonApi = retrofit.create(FeedJsonApi.class);


        Call<List<Blog>> call = feedJsonApi.getBlogs();

        call.enqueue(new Callback<List<Blog>>() {
            @Override
            public void onResponse(Call<List<Blog>> call, Response<List<Blog>> response) {
                if (!response.isSuccessful()) {
                   // text.setText("Code: " + response.code());
                    return;
                }
                List<Blog> blogs = response.body();
                mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
                mLayoutManager = new LinearLayoutManager(getContext());
                mAdapter = new RecyclerMyFeed(blogs,getContext());

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
//                for (Blog post : blogs) {
//                    String content = "";
//                    content += "ID: " + post.getTitle() + "\n";
//                    content += "User ID: " + post.getDescription() + "\n";
//                    content += "Title: " + post.getLikes() + "\n";
//                    content += "Text: " + post.getVideo() + "\n\n";
//                    content += "photo: " + post.getPhoto() + "\n\n";
//                   // text.append(content);
//                }
            }
            @Override
            public void onFailure(Call<List<Blog>> call, Throwable t) {
                //text.setText(t.getMessage());
            }
        });




        return v;
    }


}
