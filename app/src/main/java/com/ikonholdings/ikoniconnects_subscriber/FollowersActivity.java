package com.ikonholdings.ikoniconnects_subscriber;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.GetUsers;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerFollowerAndBlockedUser;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.FollowersModel;

import java.util.List;

public class FollowersActivity extends AppCompatActivity implements GetUsers.onServerResponse {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_followers);
        getSupportActionBar().setTitle("Followers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent i = getIntent();
        Boolean referralUser = i.getBooleanExtra("referralUser",false);
        mRecyclerView = findViewById(R.id.recyclerViewFollwer);
        total = findViewById(R.id.total);
        String Url;

        if(referralUser){
            Url = "referralFollowers";
            total.setVisibility(View.GONE);
        }
        else{
            Url = "followers";
            total.setVisibility(View.VISIBLE);
        }

        new GetUsers(total,Url).execute();
    }

    private void buildRecyclerView(List<FollowersModel> followersList){
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerFollowerAndBlockedUser(followersList,"Followers");

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onResponse(List<FollowersModel> followersList) {
        buildRecyclerView(followersList);
    }
}