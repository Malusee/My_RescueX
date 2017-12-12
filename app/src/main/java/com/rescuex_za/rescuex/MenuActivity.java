package com.rescuex_za.rescuex;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ImageButton fakeCallBtn;
    Button rsc;
    ImageButton notif;
    ImageButton flash;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mAuth = FirebaseAuth.getInstance();

        fakeCallBtn = (ImageButton) findViewById(R.id.fake_callbtn);
        fakeCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fakecallIntent = new Intent(MenuActivity.this, FakeCalling.class);
                startActivity(fakecallIntent);
            }
        });
        flash = (ImageButton) findViewById(R.id.flash);
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent flashIntent = new Intent(MenuActivity.this, FlashLight.class);
                startActivity(flashIntent);
            }
        });

        notif = (ImageButton) findViewById(R.id.notification_btn);
        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationIntent = new Intent(MenuActivity.this, Notifications.class);
                startActivity(notificationIntent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("RescueX ");

        if (mAuth.getCurrentUser() != null) {


            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){

            sendToStart();

        } else {

            mUserRef.child("online").setValue("true");

        }

    }


    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {

            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

        }

    }

    private void sendToStart() {

        Intent startIntent = new Intent(MenuActivity.this, Home.class);
        startActivity(startIntent);
        finish();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()== R.id.log_out){
            FirebaseAuth.getInstance().signOut();
            sendToStart();

        }



        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_settings) {
            Intent notifIntent= new Intent(MenuActivity.this, Notifications.class);
            startActivity(notifIntent);
        }
        if(item.getItemId() == R.id.all_users){

            Intent usersIntent= new Intent(MenuActivity.this, UsersActivity.class);
            startActivity(usersIntent);

        }

        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile_layout) {
            Intent searchIntent = new Intent(MenuActivity.this, Profile.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_users_activity) {
            Intent searchIntent = new Intent(MenuActivity.this, UsersActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_history_layout) {
            Intent searchIntent = new Intent(MenuActivity.this, History.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_help_layout) {
            Intent searchIntent = new Intent(MenuActivity.this, Help.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_feedback_layout) {
            Intent searchIntent = new Intent(MenuActivity.this, Feedback.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_signout_layout) {
            Intent searchIntent = new Intent(MenuActivity.this, SignOut.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_friends_layout) {
            Intent searchIntent = new Intent(MenuActivity.this, MyFriends.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_share) {
            Intent searchIntent = new Intent(MenuActivity.this, Share.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
