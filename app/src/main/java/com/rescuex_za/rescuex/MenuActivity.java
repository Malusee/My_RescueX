package com.rescuex_za.rescuex;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,ConnectionCallbacks, OnConnectionFailedListener  {

private static final  String TAG = "RescueX";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private DatabaseReference mLocationDatabase;
    
    ImageButton fakeCallBtn;
    Button mRescue;
    ImageButton notif;
    ImageButton flash;
    private  Double lati;
    private GoogleMap mMap;
    LocationManager locationManager;

    private DatabaseReference mRootRef;
    private String mCurrentUserId;
    private String userName;
    private DatabaseReference user_id;
    private String mChatUser;

    private String message;
    private String value_lat = null;
    private String value_long = null;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;
    LocationTrack locationTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
  
        FirebaseApp.initializeApp(this);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mLocationDatabase = mRootRef.child("EmergencyMessages");
        mAuth = FirebaseAuth.getInstance();


        gettingIntent();

        buildGoogleApiClient();


        mRescue = (Button)findViewById(R.id.rescue);
        mRescue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addEmergencyMessage();
                addEmergencyChat();
                Toast.makeText(getApplicationContext(), "Emergency Alert message was successfully sent",Toast.LENGTH_LONG).show();


            }
        });

        fakeCallBtn = (ImageButton) findViewById(R.id.fake_callbtn);
        fakeCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fakecallIntent = new Intent(MenuActivity.this, FakeCalling.class);
                startActivity(fakecallIntent);
            }
        });
        flash = (ImageButton) findViewById(R.id.flash);
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent flashIntent = new Intent(MenuActivity.this, FlashLight.class);
                startActivity(flashIntent);
            }
        });

        notif = (ImageButton) findViewById(R.id.notification_btn);
        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationIntent = new Intent(MenuActivity.this, Notifications.class);
                startActivity(notificationIntent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("RescueX ");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        if (mAuth.getCurrentUser() != null) {


            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
            mUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userName = dataSnapshot.child("name").getValue().toString();


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("fist","error");

            return ;

        }


        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    //get latitude
                    double latitude = location.getLatitude();
                    //get longitude
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String str = addressList.get(0).getCountryName() + ",";
                        str += addressList.get(0).getLocality();

                        mMap.addMarker(new MarkerOptions().position(latLng).title(str));

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }


                @Override
                public void onProviderDisabled(String provider) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
        }

    }

    private void checkUser() {

        if (mCurrentUserId == null) {
            sendToStart();
        }else{
            mUserRef.child("online").setValue("true");
        }
    }

    private void gettingIntent() {

        Intent intent =getIntent();
        mChatUser = intent.getStringExtra("user_id");
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                          .addConnectionCallbacks(this)
                          .addOnConnectionFailedListener(this)
                          .addApi(LocationServices.API)
                          .build();
    }

    private void addEmergencyChat() {


        value_lat = String.valueOf(mLastLocation.getLatitude());
        value_long = String.valueOf(mLastLocation.getLongitude());
                String current_user_ref="Emergency_Messages/"+mCurrentUserId+"/"+mChatUser;
                String chat_user_ref= "Emergency_Messages/"+mChatUser+"/"+mCurrentUserId;

                DatabaseReference chat_push_key = mRootRef.child("Emergency_Messages").child(mCurrentUserId).child(mChatUser).push();

                String push_key = chat_push_key.getKey();

                Map messageMap = new HashMap();
                messageMap.put("userName", userName + " is in trouble on this location:"+"\nlatitude ="+value_lat +"\nlongitude ="+ value_long);
                messageMap.put("open_location", "Tap Here to see "+userName+"'s location");
                messageMap.put("type","text");
                messageMap.put("latitude",value_lat);
                messageMap.put("longitude", value_long);
                messageMap.put("from",mCurrentUserId);
                messageMap.put("seen",false);
                messageMap.put("time", ServerValue.TIMESTAMP);

                Map messageUserMap = new HashMap();
                messageUserMap.put(current_user_ref+ "/"+push_key,messageMap);
                messageUserMap.put(chat_user_ref+ "/"+push_key,messageMap);

                mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        if(databaseError!=null){
                            Log.d("TAG",databaseError.getMessage().toString());
                        }
                    }
                });

        }



    private void addEmergencyMessage() {

        mRootRef.child("Emergency_Chat").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(mChatUser)){

                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen",false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Emergency_Chat/"+mCurrentUserId+"/"+mChatUser, chatAddMap);
                    chatUserMap.put("Emergency_Chat/"+mChatUser+"/"+mCurrentUserId, chatAddMap);

                    mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError!= null){
                                Toast.makeText(MenuActivity.this, "Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mGoogleApiClient.connect();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){

            sendToStart();

        } else{
            mCurrentUserId = mAuth.getCurrentUser().getUid();
            mUserRef.child("online").setValue("true");

        }

    }


    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {

            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

        }

    }

    private void sendToStart() {

        Intent startIntent = new Intent(MenuActivity.this, Home.class);
        startActivity(startIntent);
        finish();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


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
        super.onCreateOptionsMenu(menu);// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()== R.id.log_out){
            FirebaseAuth.getInstance().signOut();
            sendToStart();

        }



        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_settings) {
            Intent notifIntent= new Intent(MenuActivity.this, Settings.class);
            startActivity(notifIntent);
        }
        if(item.getItemId() == R.id.all_users){

            Intent usersIntent= new Intent(MenuActivity.this, UsersActivity.class);
            startActivity(usersIntent);

        }

        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile_layout) {
            Intent searchIntent = new Intent(MenuActivity.this, Profile.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_users_activity) {
            Intent searchIntent = new Intent(MenuActivity.this, UsersActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_history_layout) {
            Intent searchIntent = new Intent(MenuActivity.this, History.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_help_layout) {
            Intent searchIntent = new Intent(MenuActivity.this, Help.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_feedback_layout) {
            Intent searchIntent = new Intent(MenuActivity.this, Feedback.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_signout_layout) {
            Intent searchIntent = new Intent(MenuActivity.this, SignOut.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_friends_layout) {
            Intent searchIntent = new Intent(MenuActivity.this, FriendsActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_share) {
            Intent searchIntent = new Intent(MenuActivity.this, Share.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null){
            value_lat = String.valueOf(mLastLocation.getLatitude()).replace(".","d");
            value_long = String.valueOf(mLastLocation.getLongitude()).replace(".","d");

        }


    }

    @Override
    public void onConnectionSuspended(int cause) {
      Log.i(TAG,"Connection suspended");
      mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, " Connection Failed "+ connectionResult.getErrorMessage());

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
