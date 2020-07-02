package com.example.djikon;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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

        txt_total_canceled_request = findViewById(R.id.txt_new_request);
        txt_total_canceled_request.setText("You Have 05 Approved Request");

        mRecyclerView = findViewById(R.id.recyclerView_booking_request);

        ArrayList<CacelAndAcceptRequest_Model> cacelAndAcceptRequest_modelArrayList = new ArrayList<>();

        cacelAndAcceptRequest_modelArrayList.add(new CacelAndAcceptRequest_Model(R.drawable.photo2, "$25.00","Hamza Ali",
                "Night Dj Service", "Discount", "105 William St, Chicago, Us"));


        cacelAndAcceptRequest_modelArrayList.add(new CacelAndAcceptRequest_Model(R.drawable.ic_doctor, "$20.00","Usama Ali",
                "Night Dj Service", "Discount", "105 William St, Chicago, Us"));

        cacelAndAcceptRequest_modelArrayList.add(new CacelAndAcceptRequest_Model(R.drawable.woman, "$15.00","Bilawal",
                "Night Dj Service", "Discount", "105 William St, Chicago, Us"));



        cacelAndAcceptRequest_modelArrayList.add(new CacelAndAcceptRequest_Model(R.drawable.photo2, "$25.00","Admad",
                "Night Dj Service", "Discount", "William St, Chicago, Us"));


        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerCancelAndAcceptRequests(cacelAndAcceptRequest_modelArrayList,"Accept");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}