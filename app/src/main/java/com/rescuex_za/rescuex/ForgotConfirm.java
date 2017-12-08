package com.rescuex_za.rescuex;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ForgotConfirm extends AppCompatActivity {
    Button ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_confirm);
        ok=(Button)findViewById(R.id.got);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent okIntent= new Intent(ForgotConfirm.this, Home.class);
                startActivity(okIntent);
            }
        });
    }
}