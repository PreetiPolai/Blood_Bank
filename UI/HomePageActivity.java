package com.android.BloodBank.UI;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.BloodBank.Model.GeoCodeLocation;
import com.android.BloodBank.Notifications.Token;
import com.android.BloodBank.R;
import com.android.BloodBank.UI.Fragments.HomeFragment;
import com.android.BloodBank.UI.Fragments.ProfileFragment;
import com.android.BloodBank.UI.Fragments.SearchFragment;
import com.android.BloodBank.UI.Fragments.TrackFragment;
import com.android.BloodBank.databinding.ActivityHomePageBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class HomePageActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ActivityHomePageBinding binding;
    HashMap<String, String> hashMap = new HashMap<>();
    String Address;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //location
        setUserInformation();

        //update token
        //        //for notification
        updateToken(FirebaseInstanceId.getInstance().getToken());

        //Load the start page with home fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_frame,new HomeFragment()).commit();

        //set the item click listener
        bottomNavigationView = binding.activityHomeBottomNavView;
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //temp fragment that will hold the desired item
                Fragment temp = null;

                switch (item.getItemId()){
                    case R.id.home : temp = new HomeFragment ();
                        break;
                    case R.id.search : temp = new SearchFragment();
                        break;
                    case R.id.track : temp = new TrackFragment();
                        break;

                    case R.id.profile : temp = new ProfileFragment();
                        break;

                }

                //swap the frame layout with the temp fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_frame,temp).commit();


                return true;
            }
        });


    }


    private void status(String status){

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        database.updateChildren(hashMap);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }


    private void setUserInformation(){

        DocumentReference docRef = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {



                hashMap = (HashMap<String, String>) value.get("userAddress");

                Address = hashMap.get("Street") + ", " +hashMap.get("City") + ", " +hashMap.get("District") +", " +hashMap.get("State");
                //Load Users location
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("Address", Address);
                database.updateChildren(hashMap);
                GeoCodeLocation locationAddress = new GeoCodeLocation();
                locationAddress.getAddressFromLocation(Address, getApplicationContext(), new
                        GeoCoderHandler());

            }
        });

    }



    private class GeoCoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            String LatLng;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    LatLng = bundle.getString("LatLng");

                    break;
                default:
                    locationAddress = null;
                    LatLng = null;

            }


            String[] LatitudeNLongitude = LatLng.split("\n");
            HashMap<String,Double> location = new HashMap<>();
            location.put("latitude", Double.parseDouble(LatitudeNLongitude[0]));
            location.put("longitude", Double.parseDouble(LatitudeNLongitude[1]));
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("Location", location);
            database.updateChildren(hashMap);

        }
    }

    //notification
    private void updateToken(String token){
        DatabaseReference reference  =  FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token1);
    }

}