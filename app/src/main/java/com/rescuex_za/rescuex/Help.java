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
import android.widget.TextView;

public class Help extends AppCompatActivity {


    TextView sets;
    TextView friendz;
    TextView termz;
    TextView Policies;
    TextView Tipz;
    TextView emergencyz;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


        mToolbar= (Toolbar) findViewById(R.id.user_Appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("RescueX Help");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sets = (TextView) findViewById(R.id.haccount);
        friendz = (TextView) findViewById(R.id.hfriends);
        termz = (TextView) findViewById(R.id.h_terms);
        Policies = (TextView) findViewById(R.id.hprivacy);
        Tipz = (TextView) findViewById(R.id.htips);
        emergencyz = (TextView) findViewById(R.id.halert);
        emergencyz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emergencyIntent = new Intent(Help.this, Safety.class);
                startActivity(emergencyIntent);
            }
        });
        Tipz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tipsIntent = new Intent(Help.this, Safety.class);
                startActivity(tipsIntent);
            }
        });
        Policies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent policyIntent = new Intent(Help.this, Privacy.class);
                startActivity(policyIntent);
            }
        });
        termz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent termzIntent = new Intent(Help.this, Terms.class);
                startActivity(termzIntent);
            }
        });
        friendz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent friengzIntent = new Intent(Help.this, About.class);
                startActivity(friengzIntent);
            }
        });
        sets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setsIntent = new Intent(Help.this, About.class);
                startActivity(setsIntent);
            }
        });



    }
}
