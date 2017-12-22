package com.rescuex_za.rescuex;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private RecyclerView mFriendsList;

    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;
    private String mCurrentUser;



    private View mMainView;
    private String name;
    private String thumb_image;
    private ImageButton add_friends;


    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_friends, container, false);

        // Inflate the layout for this fragment
        return mMainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFriendsList = mMainView.findViewById(R.id.friends_list);
        mFriendsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFriendsList.setHasFixedSize(true);

        mAuth= FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser().getUid();
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrentUser);
        mFriendsDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);


        add_friends = (ImageButton)mMainView.findViewById(R.id.add);
        add_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFriends = new Intent(getActivity(), UsersActivity.class);
                getActivity().startActivity(addFriends);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<Friends, MyViewHolder> adapter =new FirebaseRecyclerAdapter<Friends, MyViewHolder>(
                Friends.class,
                R.layout.users_single_layout,
                MyViewHolder.class,
                mFriendsDatabase
        ) {
            @Override
            protected void populateViewHolder(final MyViewHolder viewHolder, final Friends model, int position) {

                final String userId=getRef(position).getKey();

                viewHolder.setDate(model.getDate().toString());

                mUsersDatabase.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                         name =  dataSnapshot.child("name").getValue().toString();
                         thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                        if(dataSnapshot.hasChild("online")){
                            String online_status =dataSnapshot.child("online").getValue().toString();
                            viewHolder.setOnlineStatus(online_status);
                        }

                        viewHolder.setName(name);
                        viewHolder.setImage(thumb_image,getContext());

                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CharSequence options[] = new CharSequence[]{"Open Profile","Send Message"};

                                final AlertDialog.Builder builder =new AlertDialog.Builder(getContext());

                                builder.setTitle("Choose Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if(i==0){
                                            Intent intent =new Intent(getContext(), ProfileActivity.class);
                                            intent.putExtra("user_id",userId);
                                            startActivity(intent);
                                        }
                                        else if(i==1){

                                            Intent intent = new Intent(getContext(), ChatActivity.class);
                                            intent.putExtra("user_id",userId);
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

        mFriendsList.setAdapter(adapter);
    }



    static class MyViewHolder extends RecyclerView.ViewHolder{

        View view;
        public MyViewHolder(View itemView) {
            super(itemView);

            view= itemView;
        }

        public void setDate(String date){

            TextView chat_date= view.findViewById(R.id.user_single_status);
            chat_date.setText(date);
        }

        public void setName(String name){

            TextView disp_name= view.findViewById(R.id.user_single_name);
            disp_name.setText(name);
        }

        public void setImage(String image,Context context){

            CircleImageView imageView =view.findViewById(R.id.user_single_image);
            Picasso.with(context).load(image).placeholder(R.drawable.my_profile).into(imageView);
        }

        public void setOnlineStatus(String online_status) {

            ImageView onlineImage_Icon = view.findViewById(R.id.user_single_online_icon);

            if(online_status.equals("true")){
                onlineImage_Icon.setVisibility(View.VISIBLE);
            }
            else{
                onlineImage_Icon.setVisibility(View.INVISIBLE);
            }
        }
    }
}
