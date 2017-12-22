package com.rescuex_za.rescuex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

public class ChangePassword extends AppCompatActivity {

    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        mToolbar= (Toolbar) findViewById(R.id.user_Appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Change your password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
