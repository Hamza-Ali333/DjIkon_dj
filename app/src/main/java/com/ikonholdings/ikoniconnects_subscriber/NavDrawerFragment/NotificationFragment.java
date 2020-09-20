package com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.Notification_Model;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerNotification;

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

        ArrayList<Notification_Model> subscribeToArtistArrayList = new ArrayList<>();

        subscribeToArtistArrayList.add(new Notification_Model(R.drawable.ic_avatar,"Usama","cold play Ghost Stiries"));
        subscribeToArtistArrayList.add(new Notification_Model(R.drawable.ic_avatar,"Hamza","Adhitia Sofyan Adeialide sky"));
        subscribeToArtistArrayList.add(new Notification_Model(R.drawable.ic_avatar,"Usama","CEO Web Febricant Subscriber"));
        subscribeToArtistArrayList.add(new Notification_Model(R.drawable.ic_avatar,"Ahmad","Farig Awara Wella wakeel"));
        subscribeToArtistArrayList.add(new Notification_Model(R.drawable.ic_avatar,"Usama","cold play Ghost Stiries"));
        subscribeToArtistArrayList.add(new Notification_Model(R.drawable.ic_avatar,"Hamza","Adhitia Sofyan Adeialide sky"));
        subscribeToArtistArrayList.add(new Notification_Model(R.drawable.ic_avatar,"Usama","CEO Web Febricant Subscriber"));
        subscribeToArtistArrayList.add(new Notification_Model(R.drawable.ic_avatar,"Ahmad","Farig Awara Wella wakeel"));

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new RecyclerNotification(subscribeToArtistArrayList,"Notification");

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

       return v;
    }
}
