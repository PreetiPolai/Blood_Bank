package com.android.BloodBank.UI.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.BloodBank.Adapter.SearchItemAdapter;
import com.android.BloodBank.Model.UserProfileData;
import com.android.BloodBank.databinding.FragmentSearchBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static android.widget.LinearLayout.VERTICAL;

public class SearchFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser;
    String ID;
    List<Map<String, Object>> Users = new ArrayList<>();
    List<UserProfileData> usersList = new ArrayList<>();

    SearchView searchView;
    SearchItemAdapter adapter1;

    //element to be removed from the array
    UserProfileData a = new UserProfileData();



    FragmentSearchBinding binding;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        //check Users
        check();

        // Log.d("USER DETAILS**********", Users.get(0).get("userName"));
        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }


    private void check(){
        CollectionReference docRef = db.collection("Users");
        docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                usersList = value.toObjects(UserProfileData.class);


//                //remove current user from list
                for(UserProfileData i : usersList)
                    if (i.getUserId().equals(FirebaseAuth.getInstance().getUid()))
                        a = i;

                usersList.remove(a);

                //attach adapter
                  attachAdapter();


                attachSearchView();


            }
        });
    }


    private void attachAdapter(){

        //attach recycler for the other items that are being sold by the vendor
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
         adapter1 = new SearchItemAdapter(usersList,getContext());
        binding.searchRecyclerView.setLayoutManager(manager);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), VERTICAL);
        binding.searchRecyclerView.addItemDecoration(itemDecor);
        binding.searchRecyclerView.setAdapter(adapter1);

    }

    private void attachSearchView(){


        searchView = binding.searchBar;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter1.getFilter().filter(newText);
                return false;
            }
        });

    }
}