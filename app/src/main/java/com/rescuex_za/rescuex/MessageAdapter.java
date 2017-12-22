package com.rescuex_za.rescuex;


import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Asus on 12/12/2017.
 */


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyMessageViewHolder>{


    private ArrayList<Messages> arrayListMessages= new ArrayList<>();
    private Context mcontext;
    FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    public MessageAdapter(Context context,ArrayList<Messages> arrayListMessages){
        this.arrayListMessages = arrayListMessages;
        mcontext = context;
    }
    @Override
    public MyMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout, parent,false);

        return new MyMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyMessageViewHolder holder, int position) {

        mAuth = FirebaseAuth.getInstance();
        String mCurrentUser = mAuth.getCurrentUser().getUid();

        Messages messages = arrayListMessages.get(position);
        String message_type = messages.getType();
        String from_user = messages.getFrom();


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

        if(mCurrentUser.equals(messages.getFrom())){
            holder.textViewMessage.setBackgroundResource(R.drawable.custom_message_bg_primary);
            holder.textViewMessage.setTextColor(Color.WHITE);
        }else{
            holder.textViewMessage.setBackgroundResource(R.drawable.custom_message_bd_white);
            holder.textViewMessage.setTextColor(Color.BLACK);
        }
        holder.textViewMessage.setText(messages.getMessage());

        if(message_type.equals("text")) {

            holder.textViewMessage.setText(messages.getMessage());
            holder.messageImage.setVisibility(View.INVISIBLE);


        } else {

            holder.textViewMessage.setVisibility(View.INVISIBLE);
            Picasso.with(holder.UserProfile.getContext()).load(messages.getMessage())
                    .placeholder(R.drawable.my_profile).into(holder.messageImage);

        }



    }

    @Override
    public int getItemCount() {
        return arrayListMessages.size();
    }

    class MyMessageViewHolder extends RecyclerView.ViewHolder{

        TextView textViewMessage;
        CircleImageView UserProfile;
        ImageView messageImage;

        public MyMessageViewHolder(View itemView) {
            super(itemView);

            textViewMessage = itemView.findViewById(R.id.messageTextView);
            UserProfile = itemView.findViewById(R.id.message_profile_layout);
            messageImage = (ImageView) itemView.findViewById(R.id.message_image_layout);
        }


    }
}
