package com.example.djikon;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BlockedUserActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_users);
        getSupportActionBar().setTitle("Blocked User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mRecyclerView = findViewById(R.id.recyclerView_blocked_users);

        ArrayList<FollowerAndBlocked_User_Model> follower_modelArrayList = new ArrayList<>();

        follower_modelArrayList.add(new FollowerAndBlocked_User_Model(R.drawable.woman,"Usama"));
        follower_modelArrayList.add(new FollowerAndBlocked_User_Model(R.drawable.ic_doctor,"Hamza"));
        follower_modelArrayList.add(new FollowerAndBlocked_User_Model(R.drawable.woman,"Usama"));
        follower_modelArrayList.add(new FollowerAndBlocked_User_Model(R.drawable.ic_doctor,"Ahmad"));

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerFollowerAndBlockedUser(follower_modelArrayList,"BlockedUsers");

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}