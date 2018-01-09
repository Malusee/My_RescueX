package com.rescuex_za.rescuex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FeedbackMail extends AppCompatActivity {

    private TextView rescuexEmail;
    private EditText emailSub;
    private EditText write;

    private Button sendE;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_mail);


        mToolbar= (Toolbar) findViewById(R.id.user_Appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Fill the fields below");
        mToolbar.setTitleTextColor(android.graphics.Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rescuexEmail = (TextView) findViewById(R.id.sendTo);
        emailSub = (EditText)findViewById(R.id.subject);
        write = (EditText)findViewById(R.id.emailText);

        sendE = (Button)findViewById(R.id.sendEmail);
        sendE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailTo = rescuexEmail.getText().toString();
                String email_subject = emailSub.getText().toString();
                String message = write.getText().toString();

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {emailTo});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, email_subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, message);

                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, " Choose App to send Email with"));



            }
        });


    }
}
