package com.example.djikon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PackageActivity extends AppCompatActivity {

    Button btn_Monthly_Pakage, btn_Yearly_Package;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_package);
        getSupportActionBar().hide();

        btn_Monthly_Pakage = findViewById(R.id.btn_mothly_package);
        btn_Yearly_Package = findViewById(R.id.btn_year_package);


        btn_Monthly_Pakage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            lunchMainActivity();
            }
        });
        btn_Monthly_Pakage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               lunchMainActivity();
            }
        });


    }

    private void lunchMainActivity(){
                Intent i = new Intent(PackageActivity.this,
                        MainActivity.class);
                startActivity(i);
    }
}