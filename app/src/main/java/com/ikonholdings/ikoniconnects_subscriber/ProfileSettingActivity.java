package com.ikonholdings.ikoniconnects_subscriber;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileSettingActivity extends AppCompatActivity {

    private Switch swt_Biometric_State, swt_AllowMessage, swt_AllowBookings, swt_AllowSongRequest;
    private TextView txt_LiveStreaming, txt_ChangePassword, txt_SocialMedia, txt_StreamNetwork;
    private ImageView img_changePassword, img_SocialMedia;
    private int allowBookings;
    private int allowMessage;
    private int allowSongRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
        getSupportActionBar().setTitle("Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createReferences();

        Intent i = getIntent();
        allowBookings = i.getIntExtra("allowBookings",0);
        allowMessage = i.getIntExtra("allowMessage",0);
        allowSongRequest = i.getIntExtra("allowSongRequest",0);
        manageActiveDeActive();


        txt_ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             openChangePasswordDialogue();
            }
        });

        txt_LiveStreaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLiveStreaming();
            }
        });

        txt_SocialMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMedia();
            }
        });

    }

    private void manageActiveDeActive(){
        if(allowBookings == 1)
            swt_AllowBookings.setChecked(true);
        else
            swt_AllowBookings.setChecked(false);

        if(allowMessage == 1)
            swt_AllowMessage.setChecked(true);
        else
            swt_AllowMessage.setChecked(false);

        if(allowSongRequest == 1)
            swt_AllowSongRequest.setChecked(true);
        else
            swt_AllowSongRequest.setChecked(false);
    }


    private void openChangePasswordDialogue() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dailoge_change_password, null);

        EditText edt_oldPassword, edt_newPassword, edt_confirmPassword;
        Button btnResetPassword;

        edt_oldPassword = view.findViewById(R.id.edt_old_password);
        edt_oldPassword = view.findViewById(R.id.edt_new_password);
        edt_oldPassword = view.findViewById(R.id.edt_ConfirmPassword);
        btnResetPassword = view.findViewById(R.id.btn_Reset_Password);

        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog alertDialog =  builder.show();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    private void openSocialMedia() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.connect_social_media, null);

       RelativeLayout rlt_Twitter, rlt_FaceBook, rlt_Instagram, rlt_Pinterst;

        rlt_Twitter = view.findViewById(R.id.connectWithTwitter);
        rlt_FaceBook = view.findViewById(R.id.connectWithFB);
        rlt_Instagram = view.findViewById(R.id.connectWithInstagram);
        rlt_Pinterst = view.findViewById(R.id.connectWithPinterst);

        builder.setView(view);
        builder.setCancelable(true);

        builder.show();
    }



    private void openLiveStreaming() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.live_streaming_option_dialogue, null);

        RelativeLayout rlt_wifiOnly, rlt_CellTower, rlt_Both;


        rlt_wifiOnly = view.findViewById(R.id.rlt_wifionly);
        rlt_CellTower = view.findViewById(R.id.rlt_CellTower);
        rlt_Both = view.findViewById(R.id.rlt_Both);


        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog alertDialog =  builder.show();

        rlt_wifiOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileSettingActivity.this, "i m clicked", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

    }




    private void createReferences(){

        txt_LiveStreaming = findViewById(R.id.txt_liveStreaming);
        txt_SocialMedia = findViewById(R.id.txt_social);
        txt_ChangePassword = findViewById(R.id.txt_change_password);
        txt_StreamNetwork = findViewById(R.id.txt_stream);

        img_changePassword = findViewById(R.id.img_change);
        img_SocialMedia = findViewById(R.id.img_social);



          swt_AllowBookings = findViewById(R.id.swt_allow_booking);
          swt_AllowMessage = findViewById(R.id.swt_allow_messaging);
          swt_AllowSongRequest = findViewById(R.id.swt_allow_song_request);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}