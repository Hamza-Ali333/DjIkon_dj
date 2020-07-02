package com.example.djikon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    private Toolbar toolbar;

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
                        new ChatRoomFragment()).commit();
                break;


            case R.id.nav_Notifications:

                getSupportActionBar().setTitle(R.string.SubscribeArtist);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NotificationFragment()).commit();

                break;

            case R.id.nav_UserAdmin:


                break;


            case R.id.nav_SocialMediaFrame:

                getSupportActionBar().setTitle(R.string.SocialMediaFram);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SocialMediaFramFragment()).commit();

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

                getSupportActionBar().setTitle(R.string.CurrentLiveArtist);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BlogFragment()).commit();
                break;



        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}