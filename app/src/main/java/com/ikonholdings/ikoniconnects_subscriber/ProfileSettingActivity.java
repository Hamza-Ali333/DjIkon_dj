package com.ikonholdings.ikoniconnects_subscriber;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileSettingActivity extends AppCompatActivity {

    RelativeLayout rlt_LiveStreaming, rlt_FaceId, rlt_Biometrics, rlt_ChangePassword, rlt_ChangePin, rlt_BookingHistory,
    rlt_ConnectSocial, rlt_ReferralCode;

    Switch swt_FaceId_State, swt_Biometric_State, swt_AllowMessage, swt_AllowBookings, swt_AllowSongRequest;
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


        rlt_ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             openChangePasswordDialogue();
            }
        });

        rlt_LiveStreaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLiveStreaming();
            }
        });

        rlt_BookingHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileSettingActivity.this,BookingHistoryActivity.class);
                startActivity(i);
            }
        });


        rlt_ChangePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangePinDialogue();
            }
        });

        rlt_ConnectSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMedia();
            }
        });

        rlt_ReferralCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReferralCode();
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

    private void openChangePinDialogue() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dailogue_change_pin, null);

        EditText edt_oldPin, edt_newPin, edt_pin;
        Button btnResetPin;

        edt_oldPin = view.findViewById(R.id.edt_old_pin);
        edt_oldPin = view.findViewById(R.id.edt_new_pin);
        edt_oldPin = view.findViewById(R.id.edt_ConfirmPin);
        btnResetPin = view.findViewById(R.id.btn_Reset_Pin);

        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog alertDialog =  builder.show();

        btnResetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    private void openReferralCode() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dailogue_referrel_code, null);

        TextView txt_Code;
        Button btn_Ok;

        txt_Code = view.findViewById(R.id.txt_code);
        btn_Ok = view.findViewById(R.id.btn_ok);

        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog alertDialog =  builder.show();

        btn_Ok.setOnClickListener(new View.OnClickListener() {
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
          rlt_ChangePassword = findViewById(R.id.rlt_ChangePassowrd);
          rlt_ChangePin = findViewById(R.id.rlt_ChangePin);
          rlt_Biometrics = findViewById(R.id.rlt_biometrics);
          rlt_BookingHistory = findViewById(R.id.rlt_bookingHistory);
          rlt_FaceId = findViewById(R.id.rlt_faceId);
          rlt_ConnectSocial = findViewById(R.id.rlt_socialmedia);
          rlt_LiveStreaming = findViewById(R.id.rlt_liveStreaming);
          rlt_ReferralCode = findViewById(R.id.rlt_view_refrell_code);

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