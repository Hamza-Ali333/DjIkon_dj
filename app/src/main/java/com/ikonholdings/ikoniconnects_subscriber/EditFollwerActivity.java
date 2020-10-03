package com.ikonholdings.ikoniconnects_subscriber;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DeleteEntityOnServer;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.NetworkChangeReceiver;

public class EditFollwerActivity extends AppCompatActivity {

    Button btn_delete_User, btn_Block_User, btn_Block_User_IP, btn_Block_User_Access;
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
        getSupportActionBar().setTitle("Follower Actions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefrence();

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        Intent i = getIntent();
        id = i.getStringExtra("id");


        btn_Block_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),BlockedUserActivity.class));
            }
        });

        btn_delete_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteEntityOnServer("deleteReferralUser/"+id,
                        EditFollwerActivity.this)
                        .execute();
            }
        });

        btn_Block_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2 for block User 1 for unblock user
                new DeleteEntityOnServer(
                        2,
                        "deleteReferralUser/"+id,
                        EditFollwerActivity.this)
                        .execute();
            }
        });

    }

    private void createRefrence() {
        btn_delete_User = findViewById(R.id.delete_user);
        btn_Block_User = findViewById(R.id.block_user);
        btn_Block_User_IP = findViewById(R.id.block_user_ip);
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