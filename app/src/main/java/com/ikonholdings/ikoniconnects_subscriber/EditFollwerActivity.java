package com.ikonholdings.ikoniconnects_subscriber;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EditFollwerActivity extends AppCompatActivity {

    Button btn_delete_User, btn_Block_User, btn_Block_User_IP, btn_Block_User_Access;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_follwer);
        getSupportActionBar().setTitle("Follower Actions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefrence();


        btn_Block_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),BlockedUserActivity.class));
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
}