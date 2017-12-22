package com.rescuex_za.rescuex;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmergencyList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    private DatabaseReference UsersDatabase;

    private FirebaseAuth mAuth;
    private String mCurrentUser;
    private String name;
    private String thumb_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_list);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser =mAuth.getCurrentUser().getUid();
        myRef = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrentUser);
        myRef.keepSynced(true);
        UsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        UsersDatabase.keepSynced(true);

        recyclerView = (RecyclerView)findViewById(R.id.emergency_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerAdapter<Emergency, EmergencyViewHolder> emAdapter = new FirebaseRecyclerAdapter<Emergency, EmergencyViewHolder>(

                Emergency.class,
                R.layout.users_emergency_layout,
                EmergencyViewHolder.class,
                myRef
        ) {
            @Override
            protected void populateViewHolder(final EmergencyViewHolder viewHolder, final Emergency model, int position) {

               final String user_id = getRef(position).getKey();

                UsersDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        name =  dataSnapshot.child("name").getValue().toString();
                        thumb_image = dataSnapshot.child("thumb_image").getValue().toString();


                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CharSequence options[] = new CharSequence[]{"Open Profile","Send Message"};

                                final AlertDialog.Builder builder =new AlertDialog.Builder(EmergencyList.this);

                                builder.setTitle("Choose Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if(i==0){
                                            Intent intent =new Intent(EmergencyList.this, ProfileActivity.class);
                                            intent.putExtra("user_id",user_id);
                                            startActivity(intent);
                                        }
                                        else if(i==1){

                                            Intent intent = new Intent(EmergencyList.this, ChatActivity.class);
                                            intent.putExtra("user_id",user_id);
                                            intent.putExtra("Username",name);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    }

                                });
                                AlertDialog dialog =builder.create();
                                dialog.show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("TAG","Error: "+databaseError);
                    }
                });


            }
        };

        recyclerView.setAdapter(emAdapter);
    }



    static class EmergencyViewHolder extends RecyclerView.ViewHolder{

        View view;
        public EmergencyViewHolder(View itemView) {
            super(itemView);

            view= itemView;
        }


        public void setName(String name){

            TextView disp_name= view.findViewById(R.id.contacts_name);
            disp_name.setText(name);
        }


        public void setThumb_image(String thumb_image) {

            CircleImageView image =view.findViewById(R.id.image_display);
            Picasso.with(view.getContext()).load(thumb_image).placeholder(R.drawable.my_profile).into(image);;
        }


    }
}


