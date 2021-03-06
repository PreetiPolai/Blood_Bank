package com.android.BloodBank.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.BloodBank.Model.Chat;
import com.android.BloodBank.R;
import com.android.BloodBank.UI.Messaging.MessagingActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessagesViewHolder>{

    public static final int MSG_TYPE_LEFT =0;
    public static final int  MSG_TYPE_RIGHT = 1;
    private List<Chat> mchats = new ArrayList<>();
    private Context context;
    private String imageUrl;

    private FirebaseUser user;
    public MessageAdapter(List<Chat> mchats, Context context, String image) {
        this.mchats = mchats;
        this.context = context;
        this.imageUrl = image;
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new MessagesViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new MessagesViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {

        Chat chat = mchats.get(position);

        holder.show_message.setText(chat.getMessage());

        if(chat.getMessageSeenStatus().equals("seen")){
            holder.seenStatus.setVisibility(View.VISIBLE);
        }

//
//        Glide.with(context)
//                .asBitmap()
//                .load(users.get(position).getUserImage())
//                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return mchats.size();
    }

    class MessagesViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView show_message, seenStatus;

        public MessagesViewHolder(@NonNull View itemView)  {
            super(itemView);

            imageView = itemView.findViewById(R.id.profileImage);
            show_message = itemView.findViewById(R.id.message);
            seenStatus = itemView.findViewById(R.id.textSeen);



        }
    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (mchats.get(position).getSender().equals(user.getUid())){
            return MSG_TYPE_RIGHT;
        } else
            return MSG_TYPE_LEFT;

    }
}
