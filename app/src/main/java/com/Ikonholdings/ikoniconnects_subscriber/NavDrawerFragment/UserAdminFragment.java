package com.Ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.Ikonholdings.ikoniconnects_subscriber.Activities.ShowUserActivity;
import com.Ikonholdings.ikoniconnects_subscriber.R;


public class UserAdminFragment extends Fragment {

    private RelativeLayout rlt_Followers,rlt_Referral_User, rlt_BlockedUser, rlt_AccessBlockedUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_user_admin,container,false);
       createReferences(v);
                      //Note
        //TODO 1 for Followers User
        //TODO 2 for Referral User
        //TODO 3 AllBlocked User
        //TODO 4 blockedReferralFollowers User

        rlt_Followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lunchFollowerActivity(1);
            }
        });

        rlt_Referral_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lunchFollowerActivity(2);
            }
        });

        rlt_BlockedUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lunchFollowerActivity(3);
            }
        });

        rlt_AccessBlockedUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lunchFollowerActivity(4);
            }
        });

       return v;
    }

    private void lunchFollowerActivity(int runningForReferral){
        Intent i = new Intent(getContext(), ShowUserActivity.class);
        i.putExtra("user",runningForReferral);
        startActivity(i);
    }

    private void createReferences (View v) {
        rlt_Followers =v.findViewById(R.id.rlt_followers_user);
        rlt_Referral_User =v.findViewById(R.id.rlt_referraluser);
        rlt_BlockedUser =v.findViewById(R.id.rlt_blokedUser);
        rlt_AccessBlockedUser =v.findViewById(R.id.accessBlock);
    }
}
