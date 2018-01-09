package com.rescuex_za.rescuex;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Asus on 12/31/2017.
 */

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.MyAlertViewHolder>{


    private ArrayList<Alerts > arrayListMessage= new ArrayList<>();
    private Context mcontext;
    FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String from_user;
    private String emerg_message;
    private String emerg_location;
    private String message_type;


    public AlertAdapter(Context context, ArrayList<Alerts> arrayListMessages){
        this.arrayListMessage = arrayListMessages;
        mcontext = context;

    }
    @Override
    public AlertAdapter.MyAlertViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout, parent,false);

        return new AlertAdapter.MyAlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AlertAdapter.MyAlertViewHolder holder, int position) {

        mAuth = FirebaseAuth.getInstance();
        final String mCurrentUser = mAuth.getCurrentUser().getUid();

        Alerts messages = arrayListMessage.get(position);
        String message_type = messages.getType();
        String from_user = messages.getFrom();
        emerg_location = messages.getOpen_location();


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

        if(mCurrentUser.equals(messages.getFrom())){
            holder.textViewMessage.setBackgroundResource(R.drawable.custom_message_bg_primary);
            holder.textViewMessage.setTextColor(Color.WHITE);
        }else{
            holder.textViewMessage.setBackgroundResource(R.drawable.custom_message_bd_white);
            holder.textViewMessage.setTextColor(Color.BLACK);
        }
        holder.textViewMessage.setText(messages.getUserName()+"\n"+"\n"+emerg_location);

        if(message_type.equals("text")) {

            holder.textViewMessage.setText(messages.getUserName()+"\n"+"\n"+emerg_location);
            holder.messageImage.setVisibility(View.INVISIBLE);


        }



    }

    private void openLocation() {

    }

    @Override
    public int getItemCount() {
        return arrayListMessage.size();
    }

    class MyAlertViewHolder extends RecyclerView.ViewHolder{

        TextView textViewMessage;
        CircleImageView UserProfile;
        ImageView messageImage;

        public MyAlertViewHolder(View itemView) {
            super(itemView);

            textViewMessage = itemView.findViewById(R.id.messageTextView);
            UserProfile = itemView.findViewById(R.id.message_profile_layout);
            messageImage = (ImageView) itemView.findViewById(R.id.message_image_layout);
        }


    }
}

