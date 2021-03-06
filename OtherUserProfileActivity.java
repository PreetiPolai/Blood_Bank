package com.android.BloodBank;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.BloodBank.Adapter.HomePostAdapter;
import com.android.BloodBank.Adapter.OtherProfilePostAdapter;
import com.android.BloodBank.Model.Post;
import com.android.BloodBank.Model.UserProfileData;
import com.android.BloodBank.UI.Messaging.MessagingActivity;
import com.android.BloodBank.databinding.ActivityMessagingBinding;
import com.android.BloodBank.databinding.ActivityOtherUserProfileBinding;
import com.android.BloodBank.databinding.FragmentProfileBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OtherUserProfileActivity extends AppCompatActivity {

    ActivityOtherUserProfileBinding binding;
    private HashMap<String,String> hashMap = new HashMap<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Post> Posts;
    private List<Post> tempPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtherUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        setUserInformation(intent.getStringExtra("selectedUserId"));
        setPosts(intent.getStringExtra("selectedUserId"));

    }

    private void setPosts(String selectedUserId) {

        tempPosts =  new ArrayList<>();
        Posts = new ArrayList<>();
            db.collection("Posts").orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    tempPosts.clear();
                    Posts.clear();

                    tempPosts = value.toObjects(Post.class);
                    for(Post post : tempPosts){
                        if(post.getId().equals(selectedUserId))
                            Posts.add(post);



                        //attach recycler for the other items that are being sold by the vendor

                        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        OtherProfilePostAdapter adapter1 = new OtherProfilePostAdapter(Posts,getApplicationContext());
                        binding.postsRecyclerView.setLayoutManager(manager);

                        binding.postsRecyclerView.setAdapter(adapter1);


                    }
                }
            });
    }




    private void setUserInformation(String ID){

        DocumentReference docRef = db.collection("Users").document(ID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                binding.userName.setText(value.getString("userName"));

                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(value.getString("userImage"))
                        .into(binding.Dp);

                binding.BloodGroup.setText(value.getString("userBloodGroup"));
                binding.userWeight.setText(value.getString("userWeight") + " Kg");
                binding.userLastBloodDonation.setText(value.getString("userLastBloodDonationDate"));
                binding.phoneNumber.setText( "+91 " + value.getString("userPhoneNo"));
                Log.d("address", String.valueOf(value.get("userAddress")));

                hashMap = (HashMap<String, String>) value.get("userAddress");

                binding.userAddress.setText(hashMap.get("Street") + ", " +hashMap.get("City") + ", " +hashMap.get("District") +", " +hashMap.get("State"));
                binding.userPincode.setText(hashMap.get("PinCode"));


                binding.message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getApplicationContext(), MessagingActivity.class);
                        intent.putExtra("clickedUserName",value.getString("userName"));
                        intent.putExtra("clickedUserProfileImage",value.getString("userImage"));
                        intent.putExtra("clickedUserID",value.getString("userId"));
                        startActivity(intent);
                    }
                });

            }
        });

    }
}