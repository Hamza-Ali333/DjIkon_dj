package com.ikonholdings.ikoniconnects_subscriber;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class FollowersActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_followers);
        getSupportActionBar().setTitle("Followers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRecyclerView = findViewById(R.id.recyclerViewFollwer);

        ArrayList<FollowerAndBlocked_User_Model> follower_modelArrayList = new ArrayList<>();

        follower_modelArrayList.add(new FollowerAndBlocked_User_Model(R.drawable.ic_avatar,"Usama"));
        follower_modelArrayList.add(new FollowerAndBlocked_User_Model(R.drawable.ic_avatar,"Hamza"));
        follower_modelArrayList.add(new FollowerAndBlocked_User_Model(R.drawable.ic_avatar,"Usama"));
        follower_modelArrayList.add(new FollowerAndBlocked_User_Model(R.drawable.ic_avatar,"Ahmad"));

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerFollowerAndBlockedUser(follower_modelArrayList,"Followers");

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
