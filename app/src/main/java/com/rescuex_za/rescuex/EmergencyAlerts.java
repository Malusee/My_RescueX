package com.rescuex_za.rescuex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class EmergencyAlerts extends AppCompatActivity implements OnMapReadyCallback {

    private Toolbar mToolbar;
    private Button mViewLoci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_alerts);

        mToolbar= (Toolbar) findViewById(R.id.user_Appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Emergency Alerts History");
        mToolbar.setTitleTextColor(android.graphics.Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewLoci = (Button)findViewById(R.id.viewLoci);
        mViewLoci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lociIntent = new Intent(EmergencyAlerts.this, ViewLocation.class);
                startActivity(lociIntent);
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
