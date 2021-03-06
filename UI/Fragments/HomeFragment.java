package com.android.BloodBank.UI.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.BloodBank.Adapter.HomePostAdapter;
import com.android.BloodBank.Adapter.SearchItemAdapter;
import com.android.BloodBank.CreatePost;
import com.android.BloodBank.Model.Post;
import com.android.BloodBank.Model.UserProfileData;
import com.android.BloodBank.R;
import com.android.BloodBank.UI.Messaging.ChatActivity;
import com.android.BloodBank.UI.Messaging.MessagingActivity;
import com.android.BloodBank.databinding.FragmentHomeBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.VERTICAL;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser;
    String ID, UserName, UserImage;

    List<Post> posts;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //set up toolbar
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Blood Bank");

        //set the listener on the floating button
        CheckUser();

        //set recycler view
        setRecyclerView();

    }

    private void CheckUser(){

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            ID = firebaseUser.getUid();

        //push the post to the fireStore
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreatePost.class));
            }
        });


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Logout:
                startActivity(new Intent(getContext(), ChatActivity.class));
                return true;
        }


        return super.onOptionsItemSelected(item);
    }


    private void setRecyclerView(){
        posts = new ArrayList<>();
       db.collection("Posts").orderBy("time", Query.Direction.DESCENDING)
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                posts.clear();
                posts = value.toObjects(Post.class);

                //attach adapter
                attachAdapter();


            }
        });
    }

    private void attachAdapter(){

        //attach recycler for the other items that are being sold by the vendor

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        HomePostAdapter adapter1 = new HomePostAdapter(posts,getContext());
        binding.homeRecyclerView.setLayoutManager(manager);

        binding.homeRecyclerView.setAdapter(adapter1);

    }
}