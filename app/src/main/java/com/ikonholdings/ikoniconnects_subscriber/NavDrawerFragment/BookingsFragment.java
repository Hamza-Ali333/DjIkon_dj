package com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ikonholdings.ikoniconnects_subscriber.AcceptRequestActivity;
import com.ikonholdings.ikoniconnects_subscriber.BookingRequestsActivity;
import com.ikonholdings.ikoniconnects_subscriber.CancelRequestActivity;
import com.ikonholdings.ikoniconnects_subscriber.R;


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
                startActivity(new Intent(view.getContext(), BookingRequestsActivity.class));
            }
        });

       rlt_Cancle_Request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), CancelRequestActivity.class));
            }
        });

        rlt_Approved_Request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AcceptRequestActivity.class));
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
