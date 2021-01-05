package com.Ikonholdings.ikoniconnects_subscriber.Activities;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Ikonholdings.ikoniconnects_subscriber.GlobelClasses.BookingRequestFetcher;
import com.Ikonholdings.ikoniconnects_subscriber.GlobelClasses.NetworkChangeReceiver;
import com.Ikonholdings.ikoniconnects_subscriber.R;
import com.Ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerBookingRequests;
import com.Ikonholdings.ikoniconnects_subscriber.ResponseModels.MyBookingRequests;

import java.util.List;

public class BookingRequestsActivity extends AppCompatActivity implements BookingRequestFetcher.onRequestProcessComplete {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView txt_Total, txt_Msg;

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
        getSupportActionBar().setTitle("All Booking Requests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        txt_Total = findViewById(R.id.txt_total);
        txt_Msg = findViewById(R.id.msg);
        txt_Msg.setText("No Bookings Request.");
        txt_Msg.setVisibility(View.GONE);

        mRecyclerView = findViewById(R.id.recyclerView_booking_request);
        new BookingRequestFetcher(txt_Total, txt_Msg, "bookings").execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void buildRecyclerView(List<MyBookingRequests> myBookings){
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerBookingRequests(myBookings,this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
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