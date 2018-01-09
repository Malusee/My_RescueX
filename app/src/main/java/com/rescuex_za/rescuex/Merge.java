package com.rescuex_za.rescuex;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Merge extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mUsersList;
    private FirebaseRecyclerAdapter<Connection, Merge.ConnectionViewHolder> recyclerAdapter;
    private DatabaseReference mUsersDatabase;
    private FirebaseAuth mAuth;
    private CircleImageView mProfileImage;
    private TextView mUserStatus;
    private String mChatUser;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge);
        mToolbar= (Toolbar) findViewById(R.id.user_Appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Search Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mUsersList = (RecyclerView) findViewById(R.id.users_list);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));
        mUsersList.setHasFixedSize(true);

        //Firebase Database
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Emergency_Messages");
        mUsersDatabase .keepSynced(true);

        mAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        //for Custom Action bar
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View customBar = inflater.inflate(R.layout.chat_custom_bar,null);

        actionBar.setCustomView(customBar);

    }


    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()!= null){
            mUsersDatabase.child(mAuth.getCurrentUser().getUid()).child("online").setValue("true");
        }

        recyclerAdapter = new FirebaseRecyclerAdapter<Connection, ConnectionViewHolder>(
                Connection.class,
                R.layout.emergency_single_layout,
                ConnectionViewHolder.class,
                mUsersDatabase
        ) {


            @Override
            protected void populateViewHolder(ConnectionViewHolder viewHolder, Connection model, int position) {

                viewHolder.setDisplayName(model.getUserName());



                final String user_id=getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent profileIntent=new Intent(Merge.this,ProfileActivity.class);
                        profileIntent.putExtra("user_id",user_id);
                        startActivity(profileIntent);
                    }
                });
            }
        };

        mUsersList.setAdapter(recyclerAdapter);
    }



    @Override
    protected void onStop() {
        super.onStop();
        mUsersDatabase.child(mAuth.getCurrentUser().getUid()).child("online").setValue(ServerValue.TIMESTAMP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //cleaning up the adapter data
        recyclerAdapter.cleanup();
    }


    public static class ConnectionViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ConnectionViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDisplayName(String name) {

            TextView userNameView = (TextView) mView.findViewById(R.id.emergency_text);
            userNameView.setText(name);

        }


    }
    private void intCustomBarViewAndSetData() {
        TextView mTitleView = (TextView) findViewById(R.id.custom_bar_title);
        mUserStatus = (TextView) findViewById(R.id.custom_bar_seen);
        mProfileImage = (CircleImageView) findViewById(R.id.custom_bar_image);


        //showing name on toolbar
        mTitleView.setText(userName);
        mTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(Merge.this, ProfileActivity.class);
                profileIntent.putExtra("user_id", mChatUser);
                startActivity(profileIntent);
            }
        });
    }




    private void gettingIntentData() {
        Intent intent =getIntent();
        userName = intent.getStringExtra("Username");
        mChatUser = intent.getStringExtra("user_id");


    }
}
