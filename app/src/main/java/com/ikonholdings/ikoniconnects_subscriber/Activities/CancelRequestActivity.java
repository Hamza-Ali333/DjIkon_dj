package com.ikonholdings.ikoniconnects_subscriber.Activities;

import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.BookingRequestFetcher;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.NetworkChangeReceiver;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerCancelAndAcceptRequests;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.MyBookingRequests;

import java.util.List;

public class CancelRequestActivity extends AppCompatActivity implements BookingRequestFetcher.onRequestProcessComplete {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView txt_total_canceled_request;

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
        setContentView(R.layout.activity_booking_requests);
        getSupportActionBar().setTitle("Canceled Requests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        txt_total_canceled_request = findViewById(R.id.txt_total);
        mRecyclerView = findViewById(R.id.recyclerView_booking_request);

        new BookingRequestFetcher(txt_total_canceled_request,"rejectBookings").execute();
    }

    private void buildRecyclerView(List<MyBookingRequests> list){
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerCancelAndAcceptRequests(list,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onComplete(List<MyBookingRequests> requestList) {
        buildRecyclerView(requestList);
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