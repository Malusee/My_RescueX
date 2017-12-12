package com.rescuex_za.rescuex;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;


public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CircleImageView mDisplay_image;
    private TextView mdisplay_name;
    private TextView mstatus;
    Button btn;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    Button change_status;
    Bitmap thumb_bitmap = null;


    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    private StorageReference mPictureStorage;

    private ProgressDialog mProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        mDisplay_image = (CircleImageView) findViewById(R.id.circleImageView);
        btn = (Button) findViewById(R.id.picture_btn);
        mdisplay_name = (TextView) findViewById(R.id.disp_name);
        mstatus = (TextView) findViewById(R.id.status_text);
        change_status = (Button) findViewById(R.id.status_btn);


        mPictureStorage = FirebaseStorage.getInstance().getReference();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.keepSynced(true);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                final String image = dataSnapshot.child("profile_picture").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mdisplay_name.setText(name);
                mstatus.setText(status);

                if(!image.equals("default")) {

                    Picasso.with(Profile.this).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.my_profile).into(mDisplay_image, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(Profile.this).load(image).placeholder(R.drawable.my_profile).into(mDisplay_image);

                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                status();
            }

        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile_layout) {
            Intent searchIntent = new Intent(Profile.this, Profile.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_users_activity) {
            Intent searchIntent = new Intent(Profile.this, UsersActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_history_layout) {
            Intent searchIntent = new Intent(Profile.this, History.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_help_layout) {
            Intent searchIntent = new Intent(Profile.this, Help.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_feedback_layout) {
            Intent searchIntent = new Intent(Profile.this, Feedback.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_signout_layout) {
            Intent searchIntent = new Intent(Profile.this, SignOut.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_friends_layout) {
            Intent searchIntent = new Intent(Profile.this, MyFriends.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_share) {
            Intent searchIntent = new Intent(Profile.this, Share.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void openGallery() {
        Intent image_pickerIntent = new Intent();
        image_pickerIntent.setType("image/*");
        image_pickerIntent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(image_pickerIntent, "SELECT IMAGE"), PICK_IMAGE);

    }

    private void status() {
        String status_value = mstatus.getText().toString();
        Intent statusIntent = new Intent(Profile.this, statusActivity.class);
        statusIntent.putExtra("status_value", status_value);
        startActivity(statusIntent);
    }


    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mProgressDialog = new ProgressDialog(Profile.this);
                mProgressDialog.setTitle("uploading picture...");
                mProgressDialog.setMessage("please wait");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                Uri resultUri = result.getUri();

                File thumb_filePath = new File(resultUri.getPath());
                String current_user_id = mCurrentUser.getUid();



                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }




                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();


                StorageReference filepath = mPictureStorage.child("profile_pictures").child(current_user_id + "jpg");
                final StorageReference thumb_file = mPictureStorage.child("profile_picture").child("thumb").child(current_user_id + ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            @SuppressWarnings("VisibleForTests") final String download_url = task.getResult().getDownloadUrl().toString();
                            final UploadTask uploadTask = thumb_file.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    @SuppressWarnings("VisibleForTests") String thumbdownUrl = thumb_task.getResult().getDownloadUrl().toString();

                                    if (thumb_task.isSuccessful()) {

                                        Map updateHashmap = new HashMap<>();
                                        updateHashmap.put("profile_picture", download_url);
                                        updateHashmap.put("thumb_image", thumbdownUrl);
                                        mUserDatabase.updateChildren(updateHashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    mProgressDialog.dismiss();
                                                    Toast.makeText(Profile.this, "Your picture was successfully updated", Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        });


                                    } else {
                                        Toast.makeText(Profile.this, "Failed to updated your picture", Toast.LENGTH_LONG).show();
                                        mProgressDialog.dismiss();

                                    }
                                }
                            });
                        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                            Exception error = result.getError();
                        }

                    }
                });
            }
        }
    }
}


