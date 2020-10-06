package com.ikonholdings.ikoniconnects_subscriber;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.GetUsers;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.NetworkChangeReceiver;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerFollowerAndBlockedUser;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.FollowersModel;

import java.util.List;

public class ShowUserActivity extends AppCompatActivity implements GetUsers.onServerResponse {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView total;
    private Integer User;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent i = getIntent();
        User = i.getIntExtra("user",0);
        mRecyclerView = findViewById(R.id.recyclerViewFollwer);
        total = findViewById(R.id.total);
        String Url = null;
        String ToolBarTitle = null;

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        switch (User) {
            case 1:
                Url = "followers";
                total.setVisibility(View.VISIBLE);
                ToolBarTitle = "Followers";
                break;
            case 2:
                Url = "referralFollowers";
                ToolBarTitle = "Referral User";
                total.setVisibility(View.GONE);
                break;
            case 3:
                Url = "allBlockUsers";
                ToolBarTitle = "Blocked User";
                total.setVisibility(View.GONE);
                break;
            case 4:
                Url = "blockedReferralFollowers";
                ToolBarTitle = "Access Block";
                total.setVisibility(View.GONE);
                break;
        }
        getSupportActionBar().setTitle(ToolBarTitle);
        if(Url != null) new GetUsers(total,Url).execute();
    }

    private void buildRecyclerView(List<FollowersModel> followersList){
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerFollowerAndBlockedUser(followersList,User);

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
