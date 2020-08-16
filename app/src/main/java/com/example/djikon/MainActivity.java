package com.example.djikon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.GlobelClasses.NetworkChangeReceiver;
import com.example.djikon.GlobelClasses.PreferenceData;
import com.example.djikon.Models.LoginRegistrationModel;
import com.example.djikon.NavDrawerFragment.ChatListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private static final String BASEURL ="http://ec2-54-161-107-128.compute-1.amazonaws.com/api/";
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private Toolbar toolbar;

    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;

    private NetworkChangeReceiver mNetworkChangeReceiver;

    private FirebaseAuth mFirebaseAuth;
    private Boolean firebaseLoginStatus =false;

    String currentUserEmail;
    String currentUserPassword;
    Boolean isComeFromRegistrationActivity;


    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkChangeReceiver, filter);
        mFirebaseAuth = FirebaseAuth.getInstance();

        Intent i = getIntent();
        isComeFromRegistrationActivity = i.getBooleanExtra("come_from_registration",false);
        currentUserEmail = i.getStringExtra("email");
        currentUserPassword = i.getStringExtra("password");
        new RegisteringUserAlsoOnFirebase().execute(isComeFromRegistrationActivity);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createRefrencer();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CircularImageView img = findViewById(R.id.loginUser);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(i);
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // what do you want here

            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                  R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //if the activity luch for the first time and saveInstante null then it set the fragment
        if(savedInstanceState == null) {
            getSupportActionBar().setTitle(R.string.myFeed);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MyFeedFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_MyFeed);
        }

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else  {
            super.onBackPressed();
        }
    }

    private void createRefrencer(){
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_View);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.nav_MyFeed:

                getSupportActionBar().setTitle(R.string.myFeed);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyFeedFragment()).commit();
            break;

            case R.id.nav_BookingRequest:

                getSupportActionBar().setTitle(R.string.bookingRequest);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BookingsFragment()).commit();
                break;

            case R.id.nav_ChatRoom:

                getSupportActionBar().setTitle(R.string.ChatArea);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChatListFragment()).commit();
                break;


            case R.id.nav_Notifications:

                getSupportActionBar().setTitle(R.string.Notification);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NotificationFragment()).commit();

                break;

            case R.id.nav_SocialMediaFrame:

                getSupportActionBar().setTitle(R.string.SocialMediaFram);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SocialMediaFrameFragment()).commit();

                break;


            case R.id.nav_LiveFeedToggle:

                getSupportActionBar().setTitle(R.string.SocialMediaFram);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LiveFeedToggleFragment()).commit();

                break;

            case R.id.nav_ViewSongRequest:

                getSupportActionBar().setTitle(R.string.ViewSong);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ViewSongRequestFragment()).commit();

                break;


            case R.id.nav_Blog:

                getSupportActionBar().setTitle(R.string.Services);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BlogFragment()).commit();
                break;


            case R.id.nav_UserAdmin:

                getSupportActionBar().setTitle(R.string.UserAdmin);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UserAdminFragment()).commit();

                break;



            case R.id.nav_MyServices:

                getSupportActionBar().setTitle(R.string.Services);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ServicesFragment()).commit();

                break;


            case R.id.nav_Logout:

                progressDialog= DialogsUtils.showProgressDialog(this,"LogingOut","Please wait...");
                userLogOut();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private  void userLogOut () {

        Retrofit retrofit= ApiClient.retrofit(BASEURL,this);

        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);


        Call<LoginRegistrationModel> call = jsonApiHolder.logout();

        call.enqueue(new Callback<LoginRegistrationModel>() {
            @Override
            public void onResponse(Call<LoginRegistrationModel> call, Response<LoginRegistrationModel> response) {
                if(response.isSuccessful()){
                    PreferenceData.clearPrefrences(MainActivity.this);
                    mFirebaseAuth.getInstance().signOut();
                    progressDialog.dismiss();
                    startActivity(new Intent(MainActivity.this,SignInActivity.class));
                    finish();

                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "please check Your Network", Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "onResponse: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginRegistrationModel> call, Throwable t) {
                Log.i("TAG", "onFailure: "+t.getMessage());
                alertDialog = DialogsUtils.showAlertDialog(MainActivity.this,false,"No Internet","Please Check Your Internet Connection");

            }
        });

    }

    private void creatingUserOnFirebase(String Email, String Password) {
        //Creating DJ also on Firebase for Chat System
        mFirebaseAuth.createUserWithEmailAndPassword
                (Email,Password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               if (task.isSuccessful()) {
                                   //store the Email adrress for sending otp on it
                                   Toast.makeText(MainActivity.this, "User Successfully SignUp On Firebase", Toast.LENGTH_SHORT).show();
                               }
                               if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                   alertDialog = DialogsUtils.showAlertDialog(MainActivity.this,false,"Firebase","Email or Password in not correct");
                               } else {
                                   alertDialog = DialogsUtils.showAlertDialog(MainActivity.this,false,"Firebase Creating User","UnSuccessfull Registration");
                               }
                           }
                       });

                    }
                });
    }

    private void signInUserOnFirebase(String Email, String Password){
        //SignIn DJ Also on FireBase For ChatSystem
        mFirebaseAuth.signInWithEmailAndPassword(Email,
                Password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (task.isSuccessful()) {

                            Toast.makeText(MainActivity.this, "Yes Firebase Login", Toast.LENGTH_SHORT).show();

                        }  else {

                            alertDialog = DialogsUtils.showAlertDialog(MainActivity.this,false,"Firebase Sign In","Email or Password in not correct");
                        }
                    }
                });

            }
        });
    }


    private void isUserLoginedInFireBase(){
        try {
            mFirebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

            if (mFirebaseUser != null){
                firebaseLoginStatus = true;
                Toast.makeText(this, "User Login", Toast.LENGTH_SHORT).show();
            }else {
                firebaseLoginStatus = false;
                Toast.makeText(this, "Not Login", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private class RegisteringUserAlsoOnFirebase extends AsyncTask<Boolean,Void,Void>{

        @Override
        protected Void doInBackground(Boolean... booleans) {

            if(booleans[0]){
               //isUserComeFromRegistrationActivity
                creatingUserOnFirebase(currentUserEmail, currentUserPassword);
            }else {
                //isUserComeFromSignIn
                signInUserOnFirebase(currentUserEmail, currentUserPassword);
            }

            return null;
        }

    }


}