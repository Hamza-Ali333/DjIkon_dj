package com.example.djikon;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.ResponseModels.MyBookingRequests;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookingRequestsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_requests);
        getSupportActionBar().setTitle("All Booking Requests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRecyclerView = findViewById(R.id.recyclerView_booking_request);
        new GetAllbookingFromServer().execute();

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

    private class GetAllbookingFromServer extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = ApiClient.retrofit(BookingRequestsActivity.this);
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            Call<MyBookingRequests> call = jsonApiHolder.getBookings();
            call.enqueue(new Callback<MyBookingRequests>() {
                @Override
                public void onResponse(Call<MyBookingRequests> call, Response<MyBookingRequests> response) {
                    if(response.isSuccessful()){
                        List<MyBookingRequests> bookingsList = (List<MyBookingRequests>) response.body();

                        if(bookingsList.isEmpty()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    alertDialog = DialogsUtils.showAlertDialog(BookingRequestsActivity.this,
                                            false,
                                            "No Booking",
                                            "it's seems like you did't get any request yet!");
                                }
                            });
                        }else{
                            buildRecyclerView(bookingsList);
                        }

                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog = DialogsUtils.showAlertDialog(BookingRequestsActivity.this,
                                        false,
                                        "Error",
                                        "Please try again and check your internet connection");
                            }
                        });
                    }

                }

                @Override
                public void onFailure(Call<MyBookingRequests> call, Throwable t) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog = DialogsUtils.showAlertDialog(BookingRequestsActivity.this,
                                    false,
                                    "No Server Connection",
                                    t.getMessage());
                        }
                    });
                }
            });
            return null;
        }
    }

}