package com.rescuex_za.rescuex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    private TextView emergency;
    private TextView about_dev;
    private TextView change_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        emergency = (TextView)findViewById(R.id.emergency_contacts);
        about_dev = (TextView)findViewById(R.id.developer_info);
        change_name = (TextView)findViewById(R.id.change_name);

        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emergencyIntent= new Intent(Settings.this, EmergencyList.class);
                startActivity(emergencyIntent);
            }
        });
        about_dev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent devIntent = new Intent(Settings.this, DeveloperInfo.class);
                startActivity(devIntent);
            }
        });
        change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nameIntent = new Intent(Settings.this, Notifications.class);
                startActivity(nameIntent);
            }
        });
    }
}
