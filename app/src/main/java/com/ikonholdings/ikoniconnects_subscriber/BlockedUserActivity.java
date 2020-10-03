package com.ikonholdings.ikoniconnects_subscriber;

import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.NetworkChangeReceiver;

import java.util.ArrayList;

public class BlockedUserActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private NetworkChangeReceiver mNetworkChangeReceiver;
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkChangeReceiver, filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_users);
        getSupportActionBar().setTitle("Blocked User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mRecyclerView = findViewById(R.id.recyclerView_blocked_users);

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        ArrayList<FollowerAndBlocked_User_Model> follower_modelArrayList = new ArrayList<>();

        follower_modelArrayList.add(new FollowerAndBlocked_User_Model(R.drawable.ic_avatar,"Usama"));
        follower_modelArrayList.add(new FollowerAndBlocked_User_Model(R.drawable.ic_avatar,"Hamza"));
        follower_modelArrayList.add(new FollowerAndBlocked_User_Model(R.drawable.ic_avatar,"Usama"));
        follower_modelArrayList.add(new FollowerAndBlocked_User_Model(R.drawable.ic_avatar,"Ahmad"));

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this);
    //  mAdapter = new RecyclerFollowerAndBlockedUser(follower_modelArrayList,"BlockedUsers");

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
         try {
            unregisterReceiver(mNetworkChangeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}