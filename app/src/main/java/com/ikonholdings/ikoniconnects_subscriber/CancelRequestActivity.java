package com.ikonholdings.ikoniconnects_subscriber;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.BookingRequestFetcher;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerCancelAndAcceptRequests;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.MyBookingRequests;

import java.util.ArrayList;
import java.util.List;

public class CancelRequestActivity extends AppCompatActivity implements BookingRequestFetcher.onRequestProcessComplete {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    TextView txt_total_canceled_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_requests);
        getSupportActionBar().setTitle("Canceled Requests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txt_total_canceled_request = findViewById(R.id.txt_total);
        mRecyclerView = findViewById(R.id.recyclerView_booking_request);

        new BookingRequestFetcher(txt_total_canceled_request,"rejectBookings");
    }

    private void buildRecyclerView(List<MyBookingRequests> list){
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerCancelAndAcceptRequests(list,true);
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

}