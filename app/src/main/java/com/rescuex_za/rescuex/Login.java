package com.rescuex_za.rescuex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class Login extends AppCompatActivity {
    private EditText etl_email, etl_pass;

    Button log;
    Button reg;
    TextView forgotpass;

    private ProgressDialog mLoginProgress;
    private FirebaseAuth mAuth;

    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mLoginProgress= new ProgressDialog(this);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");



        etl_email=(EditText)findViewById(R.id.login_email);
        etl_pass=(EditText)findViewById(R.id.login_password);
        log = (Button) findViewById(R.id.Login);
        reg = (Button) findViewById(R.id.lreg);
        forgotpass = (TextView) findViewById(R.id.forgot_pass);
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent passIntent= new Intent(Login.this, ForgotPassword.class);
                startActivity(passIntent);
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regInt=new Intent(Login.this, RegisterActivity.class);
                startActivity(regInt);
            }
        });

        log.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mlogEmail=etl_email.getText().toString().trim();
                String mlog_password=etl_pass.getText().toString().trim();

                if (TextUtils.isEmpty(mlogEmail)) {
                    etl_email.setError("Enter the Email you used on Registration");
                    return;
                }
                if (TextUtils.isEmpty(mlog_password)) {
                    etl_pass.setError("Enter your password");
                    return;
                }
                mLoginProgress.setTitle("Signing in...");
                mLoginProgress.setMessage("Please wait while we check your credentials.");
                mLoginProgress.setCanceledOnTouchOutside(false);
                mLoginProgress.show();
                loginUser(mlogEmail, mlog_password);
            }
        });
    }

    private void loginUser(String mlogEmail, String mlog_password) {
        mAuth.signInWithEmailAndPassword(mlogEmail, mlog_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mLoginProgress.dismiss();

                    String current_user_id = mAuth.getCurrentUser().getUid();
                    String DeviceToken = FirebaseInstanceId.getInstance().getToken();

                    mUserDatabase.child(current_user_id).child("device_token").setValue(DeviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Intent logIntent= new Intent(Login.this, LoginConfirm.class);
                            logIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(logIntent);
                            finish();

                        }
                    });




                } else{
                    mLoginProgress.hide();
                    Toast.makeText(Login.this, "Login Failed!... Please check your details and try again." + task.getException(),
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}