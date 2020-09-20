package com.ikonholdings.ikoniconnects_subscriber;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookingHistoryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        getSupportActionBar().setTitle("Booking History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRecyclerView = findViewById(R.id.recyclerViewBookingHistory);

        ArrayList<BookingHistory> bookingHistoryArrayList = new ArrayList<>();

        bookingHistoryArrayList.add(new BookingHistory(R.drawable.ic_avatar,
                "Fransic Briggs",
                "30-09-2020","Take A Romantic Break In A Boutique Hotel","You love having a second home but the mortgage putting a crater in your wallet",
                "900$"));


        bookingHistoryArrayList.add(new BookingHistory(R.drawable.ic_avatar,
                "Billawal",
                "01-08-2020","Take A Romantic Break In A Boutique Hotel","You love having a second home but the mortgage putting a crater in your wallet",
                "500$"));

        bookingHistoryArrayList.add(new BookingHistory(R.drawable.ic_avatar,
                "Usama Ali",
                "35-07-2020","Take A Romantic Break In A Boutique Hotel","You love having a second home but the mortgage putting a crater in your wallet",
                "800$"));


        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerBookingHistory(bookingHistoryArrayList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}