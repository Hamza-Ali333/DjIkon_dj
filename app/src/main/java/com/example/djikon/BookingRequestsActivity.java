package com.example.djikon;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookingRequestsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_requests);
        getSupportActionBar().setTitle("All Booking Requests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRecyclerView = findViewById(R.id.recyclerView_booking_request);

        ArrayList<BookingRequest_Model> bookingRequest_modelArrayList = new ArrayList<>();

        bookingRequest_modelArrayList.add(new BookingRequest_Model(R.drawable.photo2, "$25.00","Hamza Ali",
                "Night Dj Service", "Discount",
                "09-07-2020","10-7-2020","105 William St, Chicago, Us"));


        bookingRequest_modelArrayList.add(new BookingRequest_Model(R.drawable.ic_doctor, "$20.00","Usama Ali",
                "Night Dj Service", "Discount",
                "09-07-2020","10-7-2020","105 William St, Chicago, Us"));



        bookingRequest_modelArrayList.add(new BookingRequest_Model(R.drawable.woman, "$15.00","Bilawal",
                "Night Dj Service", "Discount",
                "09-07-2020","10-7-2020","105 William St, Chicago, Us"));



        bookingRequest_modelArrayList.add(new BookingRequest_Model(R.drawable.photo2, "$25.00","Admad",
                "Night Dj Service", "Discount",
                "09-07-2020","10-7-2020","105 William St, Chicago, Us"));


        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerBookingRequests(bookingRequest_modelArrayList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}