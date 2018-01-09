package com.rescuex_za.rescuex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class statusActivity extends AppCompatActivity {


    private TextInputLayout status;
    private Button post_status;

    private DatabaseReference myStatusDatabase;
    private FirebaseUser currentUser;

    private ProgressDialog mProgress;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);


        mToolbar= (Toolbar) findViewById(R.id.user_Appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Update your status");
        mToolbar.setTitleTextColor(android.graphics.Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        String uid= currentUser.getUid();
        myStatusDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);


        String status_value=getIntent().getStringExtra("status_value");

        status=(TextInputLayout)findViewById(R.id.status_input);
        post_status=(Button)findViewById(R.id.status_btn);

        status.getEditText().setText(status_value);

        post_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress= new ProgressDialog(statusActivity.this);
                mProgress.setTitle("Saving status");
                mProgress.setMessage("Please wait while we save your status");
                mProgress.show();

                String my_status=status.getEditText().getText().toString();
                myStatusDatabase.child("status").setValue(my_status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Intent profIntent = new Intent(statusActivity.this, Profile.class);
                            profIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(profIntent);
                            finish();
                            mProgress.dismiss();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "There was a problem with saving your status, check your internet connection and try again",Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }
        });

    }
}