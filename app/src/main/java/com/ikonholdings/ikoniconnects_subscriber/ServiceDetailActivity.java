package com.ikonholdings.ikoniconnects_subscriber;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.NetworkChangeReceiver;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerServiceGallery;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.RecyclerServiceReviews;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SingleServiceModel;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SingleServiceReviews;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SliderModel;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ServiceDetailActivity extends AppCompatActivity {

    //Service Recycler View
    private RecyclerView mGalleryRecyclerView;
    private RecyclerView.Adapter mGalleryAdapter;
    private RecyclerView.LayoutManager mGalleryLayoutManager;

    //Reviews Recycler View
    private RecyclerView mReviewsRecyclerView;
    private RecyclerView.Adapter mReviewsAdapter;
    private RecyclerView.LayoutManager mReviewsLayoutManager;
    
    private Button btn_edit,btn_delete;
    private Switch swt_ServiceStatus;

    private TextView txt_Service_Name,
            txt_Subscriber_Name,
            txt_Price,
            txt_Price_Type,
            txt_Description,
    txt_ReviewHeading;

    private RatingBar ratingBar;

    private ImageView Featured_img;

    private ProgressBar progressBarProfile;

    private ConstraintLayout Parent;

    private String
            serviceImage,
            serviceName,
            subscriber_Name,
            price,
            price_type,
            description,
            Gallery;
    private int serviceId;
    private float totalRating;

    private List<SliderModel> singleServiceModleArrayList;
    private List<SingleServiceModel> singleServiceModels;
    private List<SingleServiceReviews> reviewsModels;

    private NetworkChangeReceiver mNetworkChangeReceiver;
    private AlertDialog loadingDialog;

    private Retrofit retrofit;
    private JSONApiHolder jsonApiHolder;

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
        setContentView(R.layout.activity_service_detail);
        getSupportActionBar().setTitle("Service Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createReferences();
        retrofit = ApiClient.retrofit( this);
        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        loadingDialog = DialogsUtils.showLoadingDialogue(this);
        Parent.setVisibility(View.GONE);

        Intent intent = getIntent();
        serviceId = intent.getIntExtra("serviceId", 0);

        new GetServiceDataAndReviews().execute(String.valueOf(serviceId));

        singleServiceModleArrayList = new ArrayList<>();

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteAlertDialog();
            }
        });
    }

    private void showDeleteAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirmation");
        builder.setMessage("Are you Sure You Want to delete this Service");
        builder.setCancelable(true);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                new DeleteService(serviceId).execute();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.setIcon(R.drawable.ic_alert);
        builder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setDataInToView() {
        ratingBar.setRating(totalRating);
        txt_Service_Name.setText(serviceName);
        txt_Subscriber_Name.setText(subscriber_Name);
        txt_Price.setText(" $" + price + " ");
        txt_Price_Type.setText(price_type);
        txt_Description.setText(description);

        if(serviceImage != null){
            Picasso.get().load(ApiClient.Base_Url + serviceImage)
                    .fit()
                    .centerCrop()
                    .into(Featured_img, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressBarProfile.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            progressBarProfile.setVisibility(View.GONE);
                            Toast.makeText(ServiceDetailActivity.this, "Something Happend Wrong feed image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }//if service is not equal to null
    }

    private void buildServiceGalleryRecycler(String[] gallery) {

        for (int i = 0; i <= gallery.length - 1; i++) {
            singleServiceModleArrayList.add(new SliderModel(ApiClient.Base_Url+"post_images/" + gallery[i]));
        }

        mGalleryRecyclerView.setNestedScrollingEnabled(false);
        mGalleryRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mGalleryLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mGalleryAdapter = new RecyclerServiceGallery(singleServiceModleArrayList);

        mGalleryRecyclerView.setLayoutManager(mGalleryLayoutManager);
        mGalleryRecyclerView.setAdapter(mGalleryAdapter);
    }

    private void buildReviewsRecyclerView(List<SingleServiceReviews> reviewsModels) {
        mReviewsRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mReviewsLayoutManager = new LinearLayoutManager(this);
        mReviewsAdapter = new RecyclerServiceReviews(reviewsModels);

        mReviewsRecyclerView.setLayoutManager(mReviewsLayoutManager);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);
    }

    private void createReferences() {
        Parent = findViewById(R.id.parent);

        Featured_img = findViewById(R.id.img_seervice_image);
        txt_Service_Name = findViewById(R.id.txt_Servic_Name);
        txt_Subscriber_Name = findViewById(R.id.txt_subscriber_name);
        txt_Price = findViewById(R.id.txt_service_charges);
        txt_Price_Type = findViewById(R.id.txt_price_type);
        txt_Description = findViewById(R.id.txt_service_discription);
        txt_ReviewHeading = findViewById(R.id.reviewheading);
        ratingBar = findViewById(R.id.ratingBar);
        progressBarProfile = findViewById(R.id.progressBarProfile);

        mGalleryRecyclerView = findViewById(R.id.recyclerview_service_gallery);
        mReviewsRecyclerView = findViewById(R.id.reviews_recycler);
        btn_delete = findViewById(R.id.btn_delete);
        btn_edit = findViewById(R.id.btn_edit);
        swt_ServiceStatus = findViewById(R.id.sercvice_status);

    }

    private class GetServiceDataAndReviews extends AsyncTask<String,Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            singleServiceModels = new ArrayList<>();
            reviewsModels = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(String... strings) {

            jsonApiHolder = retrofit.create(JSONApiHolder.class);
            Call<SingleServiceModel> call = jsonApiHolder.getSingleServiceData("products/"+strings[0]);

            call.enqueue(new Callback<SingleServiceModel>() {
                @Override
                public void onResponse(Call<SingleServiceModel> call, Response<SingleServiceModel> response) {

                    if (response.isSuccessful()) {

                        serviceId = response.body().getId();
                        serviceImage = response.body().getFeature_image();
                        serviceName = response.body().getName();
                        subscriber_Name = response.body().getArtist_name();
                        price = String.valueOf(response.body().getPrice());
                        price_type = response.body().getPrice_type();
                        totalRating = response.body().getRating();
                        description = response.body().getDescription();

                        Gallery = response.body().getGallery();

                        reviewsModels = response.body().getSingleServiceReviews();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                setDataInToView();

                                if (!Gallery.equals("no") || Gallery.isEmpty()) {

                                    mGalleryRecyclerView.setVisibility(View.VISIBLE);
                                    Gallery = Gallery.replaceAll("\\[", "").replaceAll("\\]", "").replace("\"", "");
                                    String[] GalleryArray = Gallery.split(",");
                                    buildServiceGalleryRecycler(GalleryArray);

                                } else {
                                    mGalleryRecyclerView.setVisibility(View.GONE);
                                }

                                //Reviews RecyclerView
                                if (!reviewsModels.isEmpty()) {
                                    buildReviewsRecyclerView(reviewsModels);
                                }else {
                                    txt_ReviewHeading.setText("No Review Found");
                                    mReviewsRecyclerView.setVisibility(View.GONE);
                                }
                                Parent.setVisibility(View.VISIBLE);
                                loadingDialog.dismiss();
                            }
                        });

                    }
                }

                @Override
                public void onFailure(Call<SingleServiceModel> call, Throwable t) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismiss();
                            DialogsUtils.showResponseMsg(ServiceDetailActivity.this,true);
                        }
                    });
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class DeleteService extends AsyncTask<Void,Void,Void> {
        ProgressDialog progressDialog;
        int ServiceId;

        public DeleteService( int ServiceId) {
            this.ServiceId = ServiceId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = DialogsUtils.showProgressDialog(ServiceDetailActivity.this,
                    "Working...",
                    "Please wait. While connecting with the server.");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = ApiClient.retrofit(ServiceDetailActivity.this);
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            Call<SuccessErrorModel> call = jsonApiHolder.deleteService(
                    ServiceId
            );
            call.enqueue(new retrofit2.Callback<SuccessErrorModel>() {
                @Override
                public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                    if(response.isSuccessful()){
                        progressDialog.dismiss();
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                DialogsUtils.showAlertDialog(ServiceDetailActivity.this,
                                        false,
                                        "Error",
                                        "Please try again and check your internet connection");
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            DialogsUtils.showAlertDialog(ServiceDetailActivity.this,
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

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkChangeReceiver);
    }

}