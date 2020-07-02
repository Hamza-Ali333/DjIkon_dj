package com.example.djikon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ViewSongRequestFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.activity_booking_requests,container,false);


        mRecyclerView = v.findViewById(R.id.recyclerView_booking_request);

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
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerBookingRequests(bookingRequest_modelArrayList,"Song");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


       return v;
    }
}
