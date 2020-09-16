package com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.ImageFrame_Model;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerImageFrame;

import java.util.ArrayList;


public class SocialMediaFrameFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_social_media_fram,container,false);
       mRecyclerView = v.findViewById(R.id.recycler_frame);

        ArrayList<ImageFrame_Model> imageFrame_modelArrayList = new ArrayList<>();

        imageFrame_modelArrayList.add(new ImageFrame_Model(R.drawable.rectangle2,"Naughty Face"));
        imageFrame_modelArrayList.add(new ImageFrame_Model(R.drawable.rectangle,"Inside Darker"));
        imageFrame_modelArrayList.add(new ImageFrame_Model(R.drawable.photo2,"Make Up"));
        imageFrame_modelArrayList.add(new ImageFrame_Model(R.drawable.photo3,"Follower"));
        imageFrame_modelArrayList.add(new ImageFrame_Model(R.drawable.rectangle2,"Naughty Face"));
        imageFrame_modelArrayList.add(new ImageFrame_Model(R.drawable.rectangle,"Inside Darker"));
        imageFrame_modelArrayList.add(new ImageFrame_Model(R.drawable.photo2,"Make Up"));
        imageFrame_modelArrayList.add(new ImageFrame_Model(R.drawable.photo3,"Follower"));


        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new GridLayoutManager(v.getContext(), 2);
        mAdapter = new RecyclerImageFrame(imageFrame_modelArrayList);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);

       return v;
    }
}
