package com.example.djikon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommnetActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mRecyclerView = findViewById(R.id.recycler_comment);


        ArrayList<Comments_Model> comments_model_array_list = new ArrayList<>();


        comments_model_array_list.add(new Comments_Model(R.drawable.woman,"Bilawal","24 spet 2019","This is the first time that i m commeting on something throug android"));
        comments_model_array_list.add(new Comments_Model(R.drawable.rectangle2,"Hamza","24 spet 2019","This is the first time that i m commeting on something throug android it's not very hard to do commetn like this if you like it then give a like heart like on youtube we give to the commenter's is it good or bad tell me replay me"));
        comments_model_array_list.add(new Comments_Model(R.drawable.ic_doctor,"Usama","24 spet 2019","This is the first time that i m commeting on something throug android"));
        comments_model_array_list.add(new Comments_Model(R.drawable.photo2,"Bilawal","24 spet 2019","This is the first time that i m commeting on something throug android"));


        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerCommnets(comments_model_array_list);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        

    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}