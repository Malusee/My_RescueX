package com.rescuex_za.rescuex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Home extends AppCompatActivity {
    Button home_reg;
    Button home_login;
    TextView terms_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        home_reg=(Button)findViewById(R.id.home_regbtn);
        home_login=(Button)findViewById(R.id.home_loginbtn);
        terms_page=(TextView)findViewById(R.id.terms);
        terms_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tpIntent= new Intent(Home.this, Terms.class);
                startActivity(tpIntent);
            }
        });
        home_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regscreen= new Intent(Home.this,RegisterActivity.class);
                startActivity(regscreen);
            }
        });
        home_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logscreen=new Intent(Home.this,Login.class);
                startActivity(logscreen);
            }
        });
    }
}
