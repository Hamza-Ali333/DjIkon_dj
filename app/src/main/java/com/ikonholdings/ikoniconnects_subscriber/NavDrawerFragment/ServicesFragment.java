package com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ikonholdings.ikoniconnects_subscriber.AddServiceActivity;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerServices;
import com.ikonholdings.ikoniconnects_subscriber.Services_Model;

import java.util.ArrayList;


public class ServicesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton btn_Add_New_Services;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_services,container,false);
        mRecyclerView = v.findViewById(R.id.services_recycler);

        ArrayList<Services_Model> services_modelArrayList = new ArrayList<>();

        services_modelArrayList.add(new Services_Model(R.drawable.photo2,"Night Subscriber","A product description is the marketing copy that explains what a product is and why it’s worth purchasing","$25"));
        services_modelArrayList.add(new Services_Model(R.drawable.photo3,"Night Subscriber","A product description is the marketing copy that explains what a product is and why it’s worth purchasing","$25"));
        services_modelArrayList.add(new Services_Model(R.drawable.rectangle,"Night Subscriber","A product description is the marketing copy that explains what a product is and why it’s worth purchasing","$25"));
        services_modelArrayList.add(new Services_Model(R.drawable.photo3,"Night Subscriber","A product description is the marketing copy that explains what a product is and why it’s worth purchasing","$25"));
        services_modelArrayList.add(new Services_Model(R.drawable.rectangle2,"Night Subscriber","A product description is the marketing copy that explains what a product is and why it’s worth purchasing","$25"));
        services_modelArrayList.add(new Services_Model(R.drawable.photo2,"Night Subscriber","A product description is the marketing copy that explains what a product is and why it’s worth purchasing","$25"));
        services_modelArrayList.add(new Services_Model(R.drawable.dj_event,"Night Subscriber","A product description is the marketing copy that explains what a product is and why it’s worth purchasing this is the first time that i m purchasign this form this app is it good or not","$25"));



        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mAdapter = new RecyclerServices(services_modelArrayList);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);


        btn_Add_New_Services = v.findViewById(R.id.fab);
        btn_Add_New_Services.setOnClickListener(new View.OnClickListener() {
            @java.lang.Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddServiceActivity.class));
            }
        });

       return v;
    }

}
