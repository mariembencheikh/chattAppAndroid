package com.example.androidproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.Model.Chat;
import com.example.androidproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private final Context mContext;
    private final List<Chat> mChats;
    FirebaseUser fuser;
    String friendid;
    String friendname;

    public MessageAdapter(Context mContext, List<Chat> mChats) {
        this.mContext = mContext;
        this.mChats = mChats ;
    }
    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == MSG_TYPE_RIGHT){
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);

        }
       else{
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_left_item, parent, false);

        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = mChats.get(position);
        holder.show_message.setText(chat.getMessage());
        if(position == mChats.size()-1){
            if(chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            }
            else{
                holder.txt_seen.setText("Delivered");
            }
        }
        else{
            holder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

       public  TextView show_message;
       public ImageView profile_image;
       public TextView txt_seen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            show_message = itemView.findViewById(R.id.show_msg);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen=itemView.findViewById(R.id.txt_seen);
        }
    }
    public int getItemViewType(int position){
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(mChats.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }
}
