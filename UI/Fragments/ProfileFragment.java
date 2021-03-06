package com.android.BloodBank.UI.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.BloodBank.Adapter.OtherProfilePostAdapter;
import com.android.BloodBank.Model.Post;
import com.android.BloodBank.Model.UserProfileData;
import com.android.BloodBank.R;
import com.android.BloodBank.UI.LoginVsSignUp;
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


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private HashMap<String,String> hashMap = new HashMap<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Post> Posts;
    private List<Post> tempPosts;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);


        //set up toolbar
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");

        //set the fields
        setUserInformation();

        //set RecyclerView
        setPosts(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //logout
        binding.Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginVsSignUp.class));
//                getActivity().finish();
            }
        });

        return binding.getRoot();

    }



    private void setUserInformation(){

        DocumentReference docRef = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                binding.userName.setText(value.getString("userName"));

                Glide.with(getActivity())
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


            }
        });

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
                            RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            OtherProfilePostAdapter adapter1 = new OtherProfilePostAdapter(Posts,getActivity());
                            binding.postsRecyclerView.setLayoutManager(manager);

                            binding.postsRecyclerView.setAdapter(adapter1);


                        }
                    }
                });
    }
}