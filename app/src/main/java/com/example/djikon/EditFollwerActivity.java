package com.example.djikon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    }

    private void createRefrence() {
        btn_delete_User = findViewById(R.id.delete_user);
        btn_Block_User = findViewById(R.id.block_user);
        btn_Block_User_IP = findViewById(R.id.block_user_ip);
        btn_Block_User_Access = findViewById(R.id.block_user_access);
    }
}