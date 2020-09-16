package com.ikonholdings.ikoniconnects_subscriber;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ServiceDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    Button btn_Edit_Service, btn_Delete_Service, Pause_Service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        getSupportActionBar().setTitle("Service Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefrences();

        mRecyclerView = findViewById(R.id.recyclerview_service_gallery);

        ArrayList<ServiceImage_Model> serviceImage_modelArrayList = new ArrayList<>();

        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.photo2,"Night Event"));
        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.photo3,"Music Night In Us"));
        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.dj_event,"Catering Service In Event"));
        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.photo2,"Night Event"));
        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.photo3,"Music Night In Us"));

        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.woman,"Night Event"));
        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.photo3,"Music Night In Us"));
        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.dj_event,"Catering Service In Event"));
        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.photo2,"Night Event"));
        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.photo3,"Music Night In Us"));

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        mAdapter = new RecyclerServiceGallery(serviceImage_modelArrayList);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



        btn_Edit_Service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void createRefrences () {
        btn_Edit_Service = findViewById(R.id.btn_edit_service);
        btn_Edit_Service = findViewById(R.id.btn_delete_service);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}