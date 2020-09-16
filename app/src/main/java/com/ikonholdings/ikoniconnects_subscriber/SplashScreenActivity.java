package com.ikonholdings.ikoniconnects_subscriber;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import com.google.firebase.auth.FirebaseAuth;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.PermissionHelper;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.PreferenceData;


public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //PreferenceData.clearLoginState(this);
        //mFirebaseAuth.getInstance().signOut();
        getSupportActionBar().hide();

        int i = PreferenceData.getBuildVersion(this);

        if(i != BuildConfig.VERSION_CODE) {
            PermissionHelper.managePermissions(this);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (PreferenceData.getUserLoggedInStatus(SplashScreenActivity.this)) {
                        startActivity(new Intent(SplashScreenActivity.this, PackageActivity.class));
                        finish();
                    }else {
                        lunchSignInActivity();
                    }
                }
            },2000);
        }
    }

    //handle Request for permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 200){
            lunchSignInActivity();
        }
    }
    private void lunchSignInActivity(){
        startActivity(new Intent(SplashScreenActivity.this, PackageActivity.class));
        finish();
    }
}