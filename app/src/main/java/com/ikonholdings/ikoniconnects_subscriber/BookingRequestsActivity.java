package com.ikonholdings.ikoniconnects_subscriber;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerBookingRequests;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.MyBookingRequests;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookingRequestsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog progressDialog;

    private TextView txt_Total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_requests);
        getSupportActionBar().setTitle("All Booking Requests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txt_Total = findViewById(R.id.txt_total);

        mRecyclerView = findViewById(R.id.recyclerView_booking_request);
        progressDialog = DialogsUtils.showProgressDialog(this,
                "Loading...",
                "Please Wait. While getting data from server.");
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
            Call<List<MyBookingRequests>> call = jsonApiHolder.getBookings();

            call.enqueue(new Callback<List<MyBookingRequests>>() {
                @Override
                public void onResponse(Call<List<MyBookingRequests>> call, Response<List<MyBookingRequests>> response) {
                    if(response.isSuccessful()){
                        List<MyBookingRequests> bookingsList = (List<MyBookingRequests>) response.body();

                        if(bookingsList.isEmpty()) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                      DialogsUtils.showAlertDialog(BookingRequestsActivity.this,
                                            false,
                                            "No Booking",
                                            "it's seems like you did't get any request yet!");
                                }
                            });
                        }else{

                            buildRecyclerView(bookingsList);
                        }
                        progressDialog.dismiss();

                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                 DialogsUtils.showAlertDialog(BookingRequestsActivity.this,
                                        false,
                                        "Error",
                                        "Please try again and check your internet connection");
                                 progressDialog.dismiss();
                            }
                        });
                    }

                }
                @Override
                public void onFailure(Call<List<MyBookingRequests>> call, Throwable t) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                             DialogsUtils.showAlertDialog(BookingRequestsActivity.this,
                                    false,
                                    "No Server Connection",
                                    t.getMessage());
                             progressDialog.dismiss();
                        }
                    });
                }
            });
            return null;
        }
    }

}