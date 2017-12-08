package com.rescuex_za.rescuex;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginConfirm extends Login {
    Button logNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_confirm);
        logNext=(Button)findViewById(R.id.log_next);
        logNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logNextClick= new Intent(LoginConfirm.this, MenuActivity.class);
                logNextClick.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logNextClick);
                finish();
            }
        });
    }
}