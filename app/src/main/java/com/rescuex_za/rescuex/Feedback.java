package com.rescuex_za.rescuex;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Feedback extends AppCompatActivity {

    private Button email_us;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        mToolbar= (Toolbar) findViewById(R.id.user_Appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Give us Feedback");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email_us = (Button)findViewById(R.id.email);
        email_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailFeed = new Intent(Feedback.this, FeedbackMail.class);
                startActivity(emailFeed);
            }
        });


    }



}
