package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidproject.Adapter.MessageAdapter;
import com.example.androidproject.Model.Chat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.androidproject.Model.User;

import de.hdodenhof.circleimageview.CircleImageView;



public class MessageActivity extends AppCompatActivity {
    TextView friendname;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    CircleImageView profile_image;
    ImageButton btn_send;
    EditText text_send;
    MessageAdapter messageAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;
    User user;
    ValueEventListener seenListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ImageView leftIcon = findViewById(R.id.left_iconM);

        btn_send=findViewById(R.id.btn_send);
        text_send=findViewById(R.id.text_send);

        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this,AcceuilActivity.class));

            }
        });


        recyclerView =findViewById(R.id.recycler_view_message);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.getStackFromEnd();
        recyclerView.setLayoutManager(linearLayoutManager);
        friendname=findViewById(R.id.friend_name);
        profile_image=findViewById(R.id.profile_image);
        Intent intent = getIntent();
        String friendId=intent.getStringExtra("friendid");
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        btn_send.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(firebaseUser.getUid(),friendId,msg);
                }
                else{
                    Toast.makeText(MessageActivity.this,"You can't send empty message",Toast.LENGTH_LONG).show();
                }
                text_send.setText("");
            }
        });
        reference= FirebaseDatabase.getInstance().getReference("Users").child(friendId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                friendname.setText(user.getUsername());
                readMessage(firebaseUser.getUid(),user.getId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        seenMessage(friendId);

    }

private  void seenMessage(final String userid){
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Chat chat = snapshot1.getValue(Chat.class);
                    if(chat.getReciever().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("isseen",true);
                        snapshot1.getRef().updateChildren(hashMap);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

}
    private void sendMessage(String sender,String reciever,String message){
         reference = FirebaseDatabase.getInstance().getReference();
         HashMap<String,Object> hashMap = new HashMap<>();
         hashMap.put("sender",sender);
         hashMap.put("reciever",reciever);
         hashMap.put("message",message);
         hashMap.put("isseen",false);

         reference.child("Chats").push().setValue(hashMap);
        final DatabaseReference chatRef=FirebaseDatabase.getInstance().getReference("ChatList")
                 .child(firebaseUser.getUid())
                 .child(user.getId());

        chatRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if(!dataSnapshot.exists()){
                     chatRef.child("id").setValue(user.getId());
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
        });



    }






    private  void readMessage(final String recieverid,final String userid){
        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if(     chat.getReciever().equals(recieverid)&& chat.getSender().equals(userid)
                            ||
                            chat.getReciever().equals(userid) && chat.getSender().equals(recieverid)){
                        mChat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this,mChat);
                    recyclerView.setAdapter(messageAdapter);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }

    protected  void onResume() {

        super.onResume();
        status("online");
    }
    protected  void onPause() {

        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
    }





}