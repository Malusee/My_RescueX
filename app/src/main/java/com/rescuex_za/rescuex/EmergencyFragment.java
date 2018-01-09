package com.rescuex_za.rescuex;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;




public class EmergencyFragment extends Fragment {
    private RecyclerView mFriendsList;

    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;


    public EmergencyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_emergency, container, false);

        mFriendsList = (RecyclerView) mMainView.findViewById(R.id.emergency_contacts);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Emergency_Contact").child(mCurrent_user_id);
        mFriendsDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);


        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inflate the layout for this fragment
        return mMainView;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Emergency, EmergencyFragment.EmergencyViewHolder> emergencyRecyclerViewAdapter = new FirebaseRecyclerAdapter<Emergency, EmergencyFragment.EmergencyViewHolder>(

                Emergency.class,
                R.layout.users_emergency_layout,
                EmergencyFragment.EmergencyViewHolder.class,
                mFriendsDatabase


        ) {
            @Override
            protected void populateViewHolder(final EmergencyFragment.EmergencyViewHolder emergencyViewHolder, Emergency emergency, int i) {

                emergencyViewHolder.setDate(emergency.getDate());

                final String user_id = getRef(i).getKey();

                mUsersDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String Username = dataSnapshot.child("name").getValue().toString();
                        String userThumb = dataSnapshot.child("thumb_image").getValue().toString();

                        if(dataSnapshot.hasChild("online")) {

                            String userOnline = dataSnapshot.child("online").getValue().toString();
                            emergencyViewHolder.setUserOnline(userOnline);

                        }

                        emergencyViewHolder.setName(Username);
                        emergencyViewHolder.setUserImage(userThumb, getContext());

                        emergencyViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CharSequence options[] = new CharSequence[]{" Open Contact Details","Send Alert Message", " Open Alerts History"};

                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                builder.setTitle("Select Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        //Click Event for each item.
                                        if(i == 0){

                                            Intent emergencyIntent = new Intent(getContext(), EmergencyList.class);
                                            emergencyIntent.putExtra("user_id", user_id);
                                            startActivity(emergencyIntent);

                                        }
                                        if (i == 1){
                                            Intent emergencyMessage = new Intent(getContext(), MenuActivity.class);
                                            emergencyMessage.putExtra("user_id", user_id);
                                            emergencyMessage.putExtra("Username", Username);
                                            startActivity(emergencyMessage);
                                        }
                                        if( i == 2 ){
                                            Intent emergIntent = new Intent(getContext(), EmergencyAlertActivity.class);
                                            emergIntent.putExtra("user_id", user_id);
                                            emergIntent.putExtra("Username", Username);
                                            startActivity(emergIntent);

                                        }


                                    }
                                });

                                builder.show();

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        mFriendsList.setAdapter(emergencyRecyclerViewAdapter);


    }


    public static class EmergencyViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public EmergencyViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDate(String date){

            TextView userStatusView = (TextView) mView.findViewById(R.id.contacts_status);
            userStatusView.setText(date);

        }

        public void setName(String name){

            TextView userNameView = (TextView) mView.findViewById(R.id.contacts_name);
            userNameView.setText(name);

        }

        public void setUserImage(String thumb_image, Context ctx){

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.image_display);
            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.my_profile).into(userImageView);

        }

        public void setUserOnline(String online_status) {

            ImageView userOnlineView = (ImageView) mView.findViewById(R.id.user_single_online_icon);

            if(online_status.equals("true")){

                userOnlineView.setVisibility(View.VISIBLE);

            } else {

                userOnlineView.setVisibility(View.INVISIBLE);

            }

        }


    }


}