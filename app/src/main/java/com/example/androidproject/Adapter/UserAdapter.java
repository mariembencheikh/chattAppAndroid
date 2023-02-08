package com.example.androidproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.androidproject.MessageActivity;
import com.example.androidproject.Model.Chat;
import com.example.androidproject.Model.User;
import com.example.androidproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder > {

    private final Context mContext;
    private final List<User> mUsers;
    private boolean isChat;
    private String lastMsg;


    String friendid;
    String friendname;

    public UserAdapter(Context mContext, List<User> mUsers,boolean isChat) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isChat = isChat;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.username.setText(user.getUsername());

        if(isChat){
            lastMessage(user.getId(),holder.last_msg);
        }else{
            holder.last_msg.setVisibility(View.GONE);
        }

        if(isChat){
            if(user.getStatus().equals("online")){
                holder.img_status_on.setVisibility(View.VISIBLE);
                holder.img_status_off.setVisibility(View.GONE);
            }
            else{
                holder.img_status_on.setVisibility(View.GONE);
                holder.img_status_off.setVisibility(View.VISIBLE);
            }
        }
        else{
            holder.img_status_on.setVisibility(View.GONE);
            holder.img_status_off.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("friendid",user.getId());
                Toast.makeText(mContext,"name is " + user.getUsername(),Toast.LENGTH_LONG).show();

                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView username;
        ImageView profile_image;
        private ImageView img_status_on;
        private ImageView img_status_off;
        private TextView last_msg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            username = itemView.findViewById(R.id.item_username);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_status_on = itemView.findViewById(R.id.img_status_on);
            img_status_off = itemView.findViewById(R.id.img_status_off);
            last_msg = itemView.findViewById(R.id.last_msg);
        }
    }

    private  void  lastMessage(final String userid,TextView last_msg){
        lastMsg = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if(firebaseUser != null) {
                        assert chat != null;
                        if (chat.getReciever().equals(firebaseUser.getUid())
                                && chat.getSender().equals(userid)
                                ||chat.getReciever().equals(userid)
                                && chat.getSender().equals(firebaseUser.getUid())
                        ) {
                            if (firebaseUser.getUid().equals(chat.getSender())){
                                lastMsg = "You: " + chat.getMessage();
                            }else {
                                lastMsg = chat.getMessage();
                            }
                        }
                    }else {
                        Log.d(TAG, "Firebase User Is Null");
                    }

                }
                switch (lastMsg){
                    case "default":
                        last_msg.setText("No message");
                        break;
                    default:
                        last_msg.setText(lastMsg);
                }
                lastMsg = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}



