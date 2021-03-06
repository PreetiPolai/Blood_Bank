package com.android.BloodBank.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.BloodBank.Model.Chat;
import com.android.BloodBank.Model.UserProfileData;
import com.android.BloodBank.R;
import com.android.BloodBank.UI.Messaging.MessagingActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<UserProfileData> users;
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String LastMessage;
    private int count;

    public ChatAdapter(List<UserProfileData> users, Context context) {
        this.users = users;
        this.context = context;
    }




    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.name.setText(users.get(position).getUserName());

        Glide.with(context)
                .asBitmap()
                .load(users.get(position).getUserImage())
                .into(holder.imageView);

        lastMessage(users.get(position).getUserId(),holder.lastMsg);
        UnseenCount(users.get(position).getUserId(),holder.UnseenCount);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MessagingActivity.class);
                intent.putExtra("clickedUserName",users.get(position).getUserName());
                intent.putExtra("clickedUserProfileImage",users.get(position).getUserImage());
                intent.putExtra("clickedUserID",users.get(position).getUserId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView name, lastMsg, UnseenCount;


        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.profileImage);
            name = itemView.findViewById(R.id.itemUserName);
            lastMsg = itemView.findViewById(R.id.last_msg);
            UnseenCount = itemView.findViewById(R.id.UnseenCount);
        }
    }


    //check for the last message
    private void lastMessage(String UserId, TextView last_msg){

        LastMessage = "default";
        db.collection("Chats").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Messaging Users", document.getId() + " => " + document.getData().get(Chat.class));
                        HashMap hashMap = (HashMap) document.getData();
                        Log.d("Values from cloud", hashMap.toString());
                        if (hashMap.get("sender").equals(UserId) && hashMap.get("receiver").equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) ||
                                hashMap.get("sender").equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && hashMap.get("receiver").equals(UserId)) {
                            LastMessage = (String) hashMap.get("message");
                            break;
                        }
                    }

                }

                switch (LastMessage){
                    case "default":
                        last_msg.setText("No messages");

                    default:
                        last_msg.setText(LastMessage);
                }
            }
        });
    }

    //check the unseen messages count
    private void UnseenCount(String UserId, TextView UnseenCount){
        db.collection("Chats").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    count = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Messaging Users", document.getId() + " => " + document.getData().get(Chat.class));
                        HashMap hashMap = (HashMap) document.getData();
                        Log.d("Values from cloud", hashMap.toString());
                        if (hashMap.get("sender").equals(UserId) && hashMap.get("receiver").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            if(hashMap.get("messageSeenStatus").equals("unseen"))
                            count++;
                        }
                    }

                }

                switch (count){
                    case 0:
                        UnseenCount.setVisibility(View.GONE);
                        break;

                    default:
                        UnseenCount.setVisibility(View.VISIBLE);
                        UnseenCount.setText(count +"");
                        break;
                }
            }
        });
    }

}
