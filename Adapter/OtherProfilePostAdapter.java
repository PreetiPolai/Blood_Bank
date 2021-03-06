package com.android.BloodBank.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.BloodBank.Model.Post;
import com.android.BloodBank.R;
import com.android.BloodBank.UI.Messaging.MessagingActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

public class OtherProfilePostAdapter extends RecyclerView.Adapter<OtherProfilePostAdapter.OtherProfilePostAdapterViewHolder> {
    List<Post> posts;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String Name,Image;

    public OtherProfilePostAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public OtherProfilePostAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post_item_home, parent, false);
        return new OtherProfilePostAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OtherProfilePostAdapterViewHolder holder, int position) {

        Post post = posts.get(position);
        holder.message.setVisibility(View.GONE);
        //setting name and image for the given ID
        getUserImageAndName(holder.Name,holder.imageView, post.getId());

        //setting the other details of post
        holder.BloodGroup.setText(post.getBloodGroup());
        holder.message.setVisibility(View.VISIBLE);
        holder.message.setText(post.getPostMessage());
        holder.location.setText("" +post.getLocation());
        holder.contact.setText("" +post.getContact());
        holder.date.setText(post.getDate());
        holder.frameLayout.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class OtherProfilePostAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView Name,BloodGroup,message,date ,time,location, contact;
        ImageView imageView;
        FrameLayout frameLayout;

        public OtherProfilePostAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.userName);
            BloodGroup = itemView.findViewById(R.id.bloodGroup);
            message = itemView.findViewById(R.id.messageBody);
            location = itemView.findViewById(R.id.userRequirementLocation);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            imageView = itemView.findViewById(R.id.userImage);
            contact = itemView.findViewById(R.id.ContactDetails);
            frameLayout = itemView.findViewById(R.id.messageicon);



        }
    }


    private void getUserImageAndName(TextView textView, ImageView imageView, String ID){

        DocumentReference docRef = db.collection("Users").document(ID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                Name = value.getString("userName");
                textView.setText(value.getString("userName"));

                Image = value.getString("userImage");


                Glide.with(context)
                        .asBitmap()
                        .load(Image)
                        .into(imageView);
            }
        });
    }

}

