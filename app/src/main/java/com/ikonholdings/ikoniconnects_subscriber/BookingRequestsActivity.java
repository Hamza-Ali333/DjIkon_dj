package com.ikonholdings.ikoniconnects_subscriber;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.BookingRequestFetcher;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerBookingRequests;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.MyBookingRequests;

import java.util.List;

public class BookingRequestsActivity extends AppCompatActivity implements BookingRequestFetcher.onRequestProcessComplete {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AlertDialog loadingDialog;

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
        new BookingRequestFetcher(txt_Total,"bookings").execute();
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

   /* private class GetAllbookingFromServer extends AsyncTask<Void,Void,Void> {

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
                                    txt_Total.setText("You have total "+String.valueOf(bookingsList.size()+" booking requests."));
                                      DialogsUtils.showAlertDialog(BookingRequestsActivity.this,
                                            false,
                                            "No Booking",
                                            "it's seems like you did't get any request yet!");
                                }
                            });
                        }else{

                            buildRecyclerView(bookingsList);
                        }
                        loadingDialog.dismiss();

                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                 DialogsUtils.showAlertDialog(BookingRequestsActivity.this,
                                        false,
                                        "Error",
                                        "Please try again and check your internet connection");
                                 loadingDialog.dismiss();
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
                             loadingDialog.dismiss();
                        }
                    });
                }
            });
            return null;
        }
    }*/

}