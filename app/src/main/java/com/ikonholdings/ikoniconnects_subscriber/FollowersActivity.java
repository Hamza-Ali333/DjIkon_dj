package com.ikonholdings.ikoniconnects_subscriber;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.GetUsers;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.NetworkChangeReceiver;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerFollowerAndBlockedUser;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.FollowersModel;

import java.util.List;

public class FollowersActivity extends AppCompatActivity implements GetUsers.onServerResponse {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView total;

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
        setContentView(R.layout.fragment_followers);
        getSupportActionBar().setTitle("Followers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent i = getIntent();
        Boolean referralUser = i.getBooleanExtra("referralUser",false);
        mRecyclerView = findViewById(R.id.recyclerViewFollwer);
        total = findViewById(R.id.total);
        String Url;

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

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

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(mNetworkChangeReceiver);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
