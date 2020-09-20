package com.ikonholdings.ikoniconnects_subscriber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.NetworkChangeReceiver;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.PreferenceData;
import com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment.BookingsFragment;
import com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment.LiveFeedToggleFragment;
import com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment.NotificationFragment;
import com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment.ServicesFragment;
import com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment.SocialMediaFrameFragment;
import com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment.UserAdminFragment;
import com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment.ViewSongRequestFragment;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.LoginRegistrationModel;
import com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment.AddBlogFragment;
import com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment.ChatListFragment;
import com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment.MyFeedFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View navHeaderView;

    private Toolbar toolbar;

    private ProgressDialog progressDialog;

    private NetworkChangeReceiver mNetworkChangeReceiver;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference myRef;

    private CircularImageView currentUserProfile;
    private ImageView UserProfileHeader;
    private TextView UserName;

    private String currentUserEmail;
    private String currentUserPassword;
    private Boolean isComeFromRegistrationActivity;

    private Retrofit retrofit;
    private JSONApiHolder jsonApiHolder;

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkChangeReceiver, filter);
        mFirebaseAuth = FirebaseAuth.getInstance();

        PreferenceData.registerPref(this,this);

        //if User in not Register on FireBase then Register him
        try {
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseUser = mFirebaseAuth.getCurrentUser();

            if (mFirebaseUser == null) {
                Intent i = getIntent();
                isComeFromRegistrationActivity = i.getBooleanExtra("come_from_registration", false);
                currentUserEmail = PreferenceData.getUserEmail(this);
                currentUserPassword = PreferenceData.getUserPassword(this);
                if (currentUserEmail != null && currentUserEmail != null)
                    new RegisteringUserAlsoOnFirebase().execute(isComeFromRegistrationActivity);
            }
        } catch (Exception e) {
            Log.i("TAG", "onStart: "+e.getMessage());
        }

        retrofit = ApiClient.retrofit(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createReferences();
        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        UserName.setText(PreferenceData.getUserName(MainActivity.this));

        getCurrentUserImage();

        setSupportActionBar(toolbar);
        //tool bar image
        currentUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, UserProfileActivity.class));
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

    private void getCurrentUserImage() {
        String imageName = PreferenceData.getUserImage(this);

        if (!imageName.equals("No Image") && !imageName.equals("no")){
            Picasso.get().load(ApiClient.Base_Url + imageName)
                    .placeholder(R.drawable.ic_avatar)
                    .fit()
                    .centerCrop()
                    .into(currentUserProfile, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            //Navigation DrawerPhoto of User
                            UserProfileHeader.setImageDrawable(currentUserProfile.getDrawable());
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        }

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void createReferences(){

        toolbar = findViewById(R.id.toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_View);
        navHeaderView =  navigationView.getHeaderView(0);

        currentUserProfile = findViewById(R.id.currentUserProfile);
        UserProfileHeader = navHeaderView.findViewById(R.id.img_userProfile);
        UserName = navHeaderView.findViewById(R.id.txt_userName);
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
                        new AddBlogFragment()).commit();
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

                progressDialog = DialogsUtils.showProgressDialog(this, "LogingOut", "Please wait...");
                userLogOut();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void userLogOut() {
        jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<LoginRegistrationModel> call = jsonApiHolder.logout();

        call.enqueue(new Callback<LoginRegistrationModel>() {
            @Override
            public void onResponse(Call<LoginRegistrationModel> call, Response<LoginRegistrationModel> response) {
                if (response.isSuccessful()) {
                    PreferenceData.clearLoginState(MainActivity.this);
                    mFirebaseAuth.getInstance().signOut();
                    progressDialog.dismiss();
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                    finish();

                } else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<LoginRegistrationModel> call, Throwable t) {

                 DialogsUtils.showAlertDialog(MainActivity.this, false, "No Internet", "Please Check Your Internet Connection");

            }
        });
    }

    private void creatingUserOnFirebase(String Email, String Password) {
        //Creating Subscriber also on Firebase for Chat System
        mFirebaseAuth.createUserWithEmailAndPassword
                (Email, Password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserIDAndUIDOnFirebase();
                            sendFCMToken();
                        }
                    }
                });
    }

    private void signInUserOnFirebase(String Email, String Password) {
        //SignIn Subscriber Also on FireBase For ChatSystem
        mFirebaseAuth.signInWithEmailAndPassword(Email,
                Password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    //if user is not exit in data base but successfully Sign in ON server then should create also on firebase
                    creatingUserOnFirebase(Email, Password);
                    Log.i("TAG", "onComplete: SignIn Done");
                }else {
                    saveUserIDAndUIDOnFirebase();
                    sendFCMToken();
                }
            }
        });
    }

    private void saveUserIDAndUIDOnFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("All_Users");

        if (mFirebaseUser != null) {
            String userUId = mFirebaseUser.getUid();
            Map<String, String> userData = new HashMap<>();
            userData.put("uid", userUId);
            userData.put("server_id", PreferenceData.getUserId(this));

            myRef.child("Subscribers").child(PreferenceData.getUserId(this)).setValue(userData);
        } else {
            Log.i("TAG", "saveUserIDAndUIDonFirebase: no user found");
        }
    }

    //send fcm Token to the Server for sending notification form server to application
    private void sendFCMToken() {
        //Get Firebase FCM token
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                jsonApiHolder = retrofit.create(JSONApiHolder.class);
                Call<Void> call = jsonApiHolder.postFCMTokenForWeb(instanceIdResult.getToken());

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(!response.isSuccessful()){
                            //if failed to send token on server then run Again
                            sendFCMToken();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        //if failed to send token on server then run Again
                        sendFCMToken();
                    }
                });

            }
        });
    }

    //when name change will automatically
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        UserName.setText(PreferenceData.getUserName(this));
    }

    private class RegisteringUserAlsoOnFirebase extends AsyncTask<Boolean, Void, Void> {
        @Override
        protected Void doInBackground(Boolean... booleans) {
            if (booleans[0]) {
                //isUserComeFromRegistrationActivity
                creatingUserOnFirebase(currentUserEmail, currentUserPassword);
            } else {
                //isUserComeFromSignIn
                signInUserOnFirebase(currentUserEmail, currentUserPassword);
            }

            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentUserImage();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkChangeReceiver);
        PreferenceData.unRegisterPref(this, this);
    }

}