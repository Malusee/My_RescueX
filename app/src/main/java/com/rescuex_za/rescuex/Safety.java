package com.rescuex_za.rescuex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class Safety extends AppCompatActivity {
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety);


        mToolbar= (Toolbar) findViewById(R.id.user_Appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Safety Tips");
        mToolbar.setTitleTextColor(android.graphics.Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
