package com.rescuex_za.rescuex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegistrationConfirm extends AppCompatActivity {
    Button nxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_confirm);
        nxt=(Button)findViewById(R.id.regcon_next);
        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nxtclick= new Intent(RegistrationConfirm.this, MenuActivity.class);
                nxtclick.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(nxtclick);
                finish();
            }
        });
    }
}