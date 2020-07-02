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


public class NotificationFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_notification,container,false);

        mRecyclerView = v.findViewById(R.id.recyclerViewSubscribeArtist);

        ArrayList<BlockedUser_Model> subscribeToArtistArrayList = new ArrayList<>();

        subscribeToArtistArrayList.add(new BlockedUser_Model(R.drawable.woman,"Usama","cold play Ghost Stiries"));
        subscribeToArtistArrayList.add(new BlockedUser_Model(R.drawable.ic_doctor,"Hamza","Adhitia Sofyan Adeialide sky"));
        subscribeToArtistArrayList.add(new BlockedUser_Model(R.drawable.woman,"Usama","CEO Web Febricant DJ"));
        subscribeToArtistArrayList.add(new BlockedUser_Model(R.drawable.ic_doctor,"Ahmad","Farig Awara Wella wakeel"));
        subscribeToArtistArrayList.add(new BlockedUser_Model(R.drawable.woman,"Usama","cold play Ghost Stiries"));
        subscribeToArtistArrayList.add(new BlockedUser_Model(R.drawable.ic_doctor,"Hamza","Adhitia Sofyan Adeialide sky"));
        subscribeToArtistArrayList.add(new BlockedUser_Model(R.drawable.woman,"Usama","CEO Web Febricant DJ"));
        subscribeToArtistArrayList.add(new BlockedUser_Model(R.drawable.ic_doctor,"Ahmad","Farig Awara Wella wakeel"));

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new RecyclerBlockedUser(subscribeToArtistArrayList,"Notification");

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

       return v;
    }
}
