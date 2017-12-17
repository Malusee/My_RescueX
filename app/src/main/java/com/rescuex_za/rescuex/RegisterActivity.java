package com.rescuex_za.rescuex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {
    private EditText mDisplayName;
    private EditText mEmail;
    private EditText mPassword;
    private Button regbtn;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;
    private ProgressDialog mRegProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegProgress= new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        mDisplayName=(EditText)findViewById(R.id.reg_name);
        mEmail=(EditText)findViewById(R.id.reg_email);
        mPassword=(EditText)findViewById(R.id.reg_password);
        regbtn=(Button)findViewById(R.id.regbtn);
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String display_name = mDisplayName.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(display_name)) {
                    mDisplayName.setError("Please choose your display name");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Please Enter your email Address");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Create your password");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password too short, enter minimum 6 characters!");
                    return;
                }
                mRegProgress.setTitle("Registering Account");
                mRegProgress.setMessage("Please wait while we Register your Account");
                mRegProgress.setCanceledOnTouchOutside(false);
                mRegProgress.show();
                register_user(display_name, email, password);

            }

        });
    }
    private void register_user(final String display_name, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful()){


                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    String device_token = FirebaseInstanceId.getInstance().getToken();

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", display_name);
                    userMap.put("status", "Hi there, I'm with the RescueX team.");
                    userMap.put("profile_picture", "default");
                    userMap.put("thumb_image", "default");
                    userMap.put("device_token", device_token);

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                mRegProgress.dismiss();
                                Toast.makeText(RegisterActivity.this, "Registration Successful.", Toast.LENGTH_LONG).show();

                                Intent mainIntent = new Intent(RegisterActivity.this, RegistrationConfirm.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();

                            }

                        }
                    });


                } else {

                    mRegProgress.hide();
                    Toast.makeText(RegisterActivity.this, "Registration failed, please check your internet connection and try again.", Toast.LENGTH_LONG).show();

                }

            }
        });

    }
}