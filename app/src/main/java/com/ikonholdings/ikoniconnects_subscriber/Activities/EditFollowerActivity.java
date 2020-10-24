package com.ikonholdings.ikoniconnects_subscriber.Activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.NetworkChangeReceiver;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.UserAdminControrls.BlockUnBlockDeleteUser;

public class EditFollowerActivity extends AppCompatActivity {

    Button btn_delete_User, btn_Block_User, btn_Block_User_Access;
    String id;

    private NetworkChangeReceiver mNetworkChangeReceiver;

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkChangeReceiver, filter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_follwer);
        getSupportActionBar().setTitle("User Actions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createReference();

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        Intent i = getIntent();
        id = i.getStringExtra("id");

        btn_Block_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BlockUnBlockDeleteUser("blockUser/"+id,1,
                        EditFollowerActivity.this).execute();
            }
        });

        btn_delete_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BlockUnBlockDeleteUser("deleteReferralUser/"+id,0,
                        EditFollowerActivity.this).execute();
            }
        });

        btn_Block_User_Access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BlockUnBlockDeleteUser("blockReferralUserAccess/"+id,2,
                        EditFollowerActivity.this).execute();
            }
        });

    }

    private void createReference() {
        btn_delete_User = findViewById(R.id.delete_user);
        btn_Block_User = findViewById(R.id.block_user);
        btn_Block_User_Access = findViewById(R.id.block_user_access);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
         try {
            unregisterReceiver(mNetworkChangeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}