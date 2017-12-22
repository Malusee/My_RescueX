package com.rescuex_za.rescuex;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {


    private Toolbar mChatToolbar; //used
    private String mChatUser;  //used
    private String mthumb_image;
    private String userName;
    private String mCurrentUserId;
    private TextView mUserStatus;
    private EditText mChatMessageView;

    private CircleImageView mProfileImage;
    private FirebaseAuth mAuth;
    private ImageButton mChatAddBtn;

    private DatabaseReference mRootRef;
    // Storage Firebase
    private StorageReference mImageStorage;
    private static final int GALLERY_PICK = 1;

    private RecyclerView mMessagesList;
    private SwipeRefreshLayout mRefreshLayout;

    private ArrayList<Messages> arrayList_Messages = new ArrayList<>();

    private MessageAdapter mAdapter;

    private static final int TOTAL_ITEMS_TO_LOAD = 10;
    private int mCurrentPage = 1;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatMessageView = (EditText) findViewById(R.id.chat_message_view);
        mChatAddBtn = (ImageButton) findViewById(R.id.chat_add_btn);

        mChatToolbar = (Toolbar) findViewById(R.id.chat_app_bar);
        setSupportActionBar(mChatToolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        //for Custom Action bar
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View customBar = inflater.inflate(R.layout.chat_custom_bar,null);

        actionBar.setCustomView(customBar);

        //getting intent Data
        gettingIntentData();

        // initializing user view
        intCustomBarViewAndSetData();


        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId =  mAuth.getCurrentUser().getUid();

        //------- IMAGE STORAGE ---------
        mImageStorage = FirebaseStorage.getInstance().getReference();

        mRootRef.child("Chat").child(mCurrentUserId).child(mChatUser).child("seen").setValue(true);

        LoadMessages();

        //getting information about user online or offline and thumb image
        mRootRef.child("Users").child(mChatUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                Picasso.with(ChatActivity.this).load(thumb_image).placeholder(R.drawable.my_profile).into(mProfileImage);

                String lastSeen = dataSnapshot.child("online").getValue().toString();

                if(lastSeen.equals("true")){
                    mUserStatus.setText("Online");
                }
                else{

                    //converting string into long
                    Long lastTime = Long.parseLong(lastSeen);

                    // creating an instance of GetTimeAgo class
                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    String lastSeenTime = GetTimeAgo.getTimeAgo(lastTime,getApplicationContext());
                    mUserStatus.setText(lastSeenTime);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //for creating chat object
        mRootRef.child("Chat").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(mChatUser)){

                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen",false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chat/"+mCurrentUserId+"/"+mChatUser, chatAddMap);
                    chatUserMap.put("Chat/"+mChatUser+"/"+mCurrentUserId, chatAddMap);

                    mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError!= null){
                                Toast.makeText(ChatActivity.this, "Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Retrieving the chat messages into recyclerview
        LoadMessages();
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mCurrentPage++;

                //onRefresh remove the current messages from arraylist and load new messages
                arrayList_Messages.clear();

                // Load message
                LoadMessages();
            }
        });


    }
    // Load all messages from database into recyclerView
    private void LoadMessages() {

        DatabaseReference messageRef = mRootRef.child("messages").child(mCurrentUserId).child(mChatUser);

        //Query to load message per page i.e. 10
        /*
           per page load 10 message and onRefresh mCurrentpage is increment by 1
           page 1 => load 10 messages (mCurrentPage = 1 then 1*10 =10)
           page 2 => load 20 messages (mCurrentPage = 2 then 2*10 =20) and so on
         */

        Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Messages messages = dataSnapshot.getValue(Messages.class);
                arrayList_Messages.add(messages);
                mAdapter.notifyDataSetChanged();

                mMessagesList.scrollToPosition(arrayList_Messages.size()-1);

                //when data load completely set refreshing
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    // send button
    public void chatSendButton(View view){

        sendMessage();
    }

    //sending a message
    private void sendMessage() {

        String message = mChatMessageView.getText().toString().trim();

        if(!TextUtils.isEmpty(message)){

            mChatMessageView.setText("");
            String current_user_ref="messages/"+mCurrentUserId+"/"+mChatUser;
            String chat_user_ref= "messages/"+mChatUser+"/"+mCurrentUserId;

            DatabaseReference chat_push_key = mRootRef.child("messages").child(mCurrentUserId).
                    child(mChatUser).push();

            String push_key = chat_push_key.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message",message);
            messageMap.put("type","text");
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
    }

    //add button
    public void chatAddButton(View view){
        mChatAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            final String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
            final String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

            DatabaseReference user_message_push = mRootRef.child("messages")
                    .child(mCurrentUserId).child(mChatUser).push();

            final String push_id = user_message_push.getKey();


            StorageReference filepath = mImageStorage.child("message_images").child( push_id + ".jpg");

            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful()){

                        String download_url = task.getResult().getDownloadUrl().toString();


                        Map messageMap = new HashMap();
                        messageMap.put("message", download_url);
                        messageMap.put("seen", false);
                        messageMap.put("type", "image");
                        messageMap.put("time", ServerValue.TIMESTAMP);
                        messageMap.put("from", mCurrentUserId);

                        Map messageUserMap = new HashMap();
                        messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                        messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                        mChatMessageView.setText("");

                        mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                if(databaseError != null){

                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());

                                }

                            }
                        });


                    }

                }
            });

        }

    }


    private void intCustomBarViewAndSetData() {
        TextView mTitleView = (TextView) findViewById(R.id.custom_bar_title);
        mUserStatus = (TextView) findViewById(R.id.custom_bar_seen);
        mProfileImage = (CircleImageView) findViewById(R.id.custom_bar_image);
        mMessagesList = (RecyclerView) findViewById(R.id.messages_list);

        mMessagesList.setLayoutManager(new LinearLayoutManager(this));
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.message_swipe_layout);
        mMessagesList.setHasFixedSize(true);

        mAdapter = new MessageAdapter(this, arrayList_Messages);
        mMessagesList.setAdapter(mAdapter);

        //showing name on toolbar
        mTitleView.setText(userName);


    }

    private void gettingIntentData() {
        Intent intent =getIntent();
        userName = intent.getStringExtra("Username");
        mChatUser = intent.getStringExtra("user_id");
    }
}
