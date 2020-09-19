package com.ikonholdings.ikoniconnects_subscriber;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerCancelAndAcceptRequests;

import java.util.ArrayList;
import java.util.List;

public class AcceptRequestActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    TextView txt_total_canceled_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_requests);
        getSupportActionBar().setTitle("Approved Requests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txt_total_canceled_request = findViewById(R.id.txt_total);
        txt_total_canceled_request.setText("You Have 05 Approved Request");

        mRecyclerView = findViewById(R.id.recyclerView_booking_request);



    }

    private void buildRecyclerView(List<CacelAndAcceptRequest_Model> list){
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this);
       // mAdapter = new RecyclerCancelAndAcceptRequests(cacelAndAcceptRequest_modelArrayList,"Accept");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}