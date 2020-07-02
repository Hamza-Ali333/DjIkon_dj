package com.example.djikon;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class BookingsFragment extends Fragment {


    RelativeLayout rlt_View_All_Request, rlt_Approved_Request, rlt_Cancle_Request;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_bookings,container,false);
       createRefrences(v);


       rlt_View_All_Request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), BookingRequestsActivity.class);
                startActivity(i);
            }
        });

       rlt_Cancle_Request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), CancelRequestActivity.class);
                startActivity(i);
            }
        });

        rlt_Approved_Request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), AcceptRequestActivity.class);
                startActivity(i);
            }
        });
       return v;
    }

    private void createRefrences (View v) {
        rlt_View_All_Request = v.findViewById(R.id.rlt_view_all_request);
        rlt_Approved_Request = v.findViewById(R.id.rlt_approved_request);
        rlt_Cancle_Request = v.findViewById(R.id.rlt_cancle_request);
    }



}
