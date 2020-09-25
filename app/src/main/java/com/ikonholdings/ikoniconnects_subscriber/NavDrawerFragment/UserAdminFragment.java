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

import com.ikonholdings.ikoniconnects_subscriber.FollowersActivity;
import com.ikonholdings.ikoniconnects_subscriber.R;


public class UserAdminFragment extends Fragment {

    private RelativeLayout rlt_Followers,rlt_Referral_User;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_user_admin,container,false);
       createReferences(v);
        rlt_Followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lunchFollowerActivity(false);
            }
        });

        rlt_Referral_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lunchFollowerActivity(true);
            }
        });

       return v;
    }

    private void lunchFollowerActivity(Boolean runningForReferral){
        Intent i = new Intent(getContext(),FollowersActivity.class);
        i.putExtra("referralUser",runningForReferral);
        startActivity(i);
    }
    private void createReferences (View v) {
        rlt_Followers =v.findViewById(R.id.rlt_followers_user);
        rlt_Referral_User =v.findViewById(R.id.rlt_referraluser);
    }
}
