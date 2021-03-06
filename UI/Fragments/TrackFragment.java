package com.android.BloodBank.UI.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.BloodBank.ApiBloodBank;
import com.android.BloodBank.Model.BloodBankPOJO;
import com.android.BloodBank.Model.LocationModel;
import com.android.BloodBank.Model.Record;
import com.android.BloodBank.R;
import com.android.BloodBank.databinding.FragmentTrackBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrackFragment extends Fragment {

    private DatabaseReference reference;
    FragmentTrackBinding binding;
    private List<LocationModel> UsersLocations;
    private List<LatLng> Locations;
    private LatLng CurrentUserLocation;
    SupportMapFragment mapFragment;
    private List<Record> BloodBankList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTrackBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //set up toolbar
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Track Donors");

        //set all the users
        getAllUsersRealTime();
         mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

         binding.searchLocation.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 makeApiCalls();
             }
         });

    }


        private class GeoCoderHandler extends Handler {
            @Override
            public void handleMessage(Message message) {
                String locationAddress;
                switch (message.what) {
                    case 1:
                        Bundle bundle = message.getData();
                        locationAddress = bundle.getString("address");
                        break;
                    default:
                        locationAddress = null;
                }

            }
        }


        private OnMapReadyCallback callback = new OnMapReadyCallback() {

            /**
             * Manipulates the map once available.
             * This callback is triggered when the map is ready to be used.
             * This is where we can add markers or lines, add listeners or move the camera.
             * In this case, we just add a marker near Sydney, Australia.
             * If Google Play services is not installed on the device, the user will be prompted to
             * install it inside the SupportMapFragment. This method will only be triggered once the
             * user has installed Google Play services and returned to the app.
             */
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng Maharashtra = new LatLng(20.23710174335751, 85.78337164879618);
                googleMap.addMarker(new MarkerOptions().position(Maharashtra).title("Maharashtra"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(Maharashtra));
            }
        };


        private OnMapReadyCallback allUsers = new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {


                googleMap.clear();
               for(int i = 0; i<Locations.size();i++){

                   googleMap.addMarker(new MarkerOptions().position(Locations.get(i)).title(UsersLocations.get(i).getName()));
                   googleMap.animateCamera(CameraUpdateFactory.zoomTo(500.0f));
                   googleMap.moveCamera(CameraUpdateFactory.newLatLng(Locations.get(i)));

               }

            }
        };



        //this has to be called by default to represent all the users
        private void getAllUsersRealTime() {


            UsersLocations = new ArrayList<>();
            Locations = new ArrayList<>();
            reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UsersLocations.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        UsersLocations.add(snapshot1.getValue(LocationModel.class));
                        LocationModel m = snapshot1.getValue(LocationModel.class);
                        double latitude = (double) m.getLocation().get("latitude");
                        double longitude = (double) m.getLocation().get("longitude");
                        Locations.add(new LatLng(latitude, longitude));
                    }

                    if (mapFragment != null) {
                        mapFragment.getMapAsync(allUsers);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


    private OnMapReadyCallback users10km = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {


            googleMap.clear();
            for(int i = 0; i<Locations.size();i++){


                if(distance(CurrentUserLocation.latitude,CurrentUserLocation.longitude,Locations.get(i).latitude,Locations.get(i).longitude) <= 10.00) {
                    googleMap.addMarker(new MarkerOptions().position(Locations.get(i)).title(UsersLocations.get(i).getName()));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(500.0f));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(Locations.get(i)));

                }

            }

        }
    };


    private OnMapReadyCallback user25km = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {

            googleMap.clear();
            for(int i = 0; i<Locations.size();i++){


                if(distance(CurrentUserLocation.latitude,CurrentUserLocation.longitude,Locations.get(i).latitude,Locations.get(i).longitude) <= 25.00) {
                    googleMap.addMarker(new MarkerOptions().position(Locations.get(i)).title(UsersLocations.get(i).getName()));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(500.0f));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(Locations.get(i)));

                }

            }

        }
    };


     private void findUsersIn5KiloMeters(){

         reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
         reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {

                 LocationModel currentUser = snapshot.getValue(LocationModel.class);
                 double latitude = (double) currentUser.getLocation().get("latitude");
                 double longitude = (double) currentUser.getLocation().get("longitude");
                 CurrentUserLocation = new LatLng(latitude,longitude);


                 if (mapFragment != null) {
                     mapFragment.getMapAsync(users10km);
                 }

             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });

        }



    private void findUsersIn25KiloMeters(){

        reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                LocationModel currentUser = snapshot.getValue(LocationModel.class);
                double latitude = (double) currentUser.getLocation().get("latitude");
                double longitude = (double) currentUser.getLocation().get("longitude");
                CurrentUserLocation = new LatLng(latitude,longitude);


                if (mapFragment != null) {
                    mapFragment.getMapAsync(user25km);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    private double distance(double lat1, double lng1, double lat2, double lng2){

            double lngDiff = lng1 - lng2;
             double distance = Math.sin(deg2rad(lat1))
                     *Math.sin(deg2rad(lat2))
                     +Math.cos(deg2rad(lat1))
                     *Math.cos(deg2rad(lat2))
                     *Math.cos(deg2rad(lngDiff));

             distance = Math.acos(distance);

             //convert radian to degree
                distance = rad2deg(distance);

             //distance in miles
                distance = distance * 60 *1.1515;

             //distance to kilometers
                distance = distance * 1.609344;

            return distance;
            }

    //radian to degree
    private double rad2deg(double distance) {
            return (distance * 180.0/Math.PI);
    }

    //degree to radian
    private double deg2rad(double lat1) {
            return (lat1*Math.PI/180.0);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.track_location, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.allUsers:
                Toast.makeText(getActivity(),"All Users Locations",Toast.LENGTH_SHORT).show();
                binding.editText.setVisibility(View.GONE);
                binding.searchLocation.setVisibility(View.GONE);
                getAllUsersRealTime();
                return true;

            case R.id.five_km_distance:
                Toast.makeText(getActivity(),"All Users in 10 kms",Toast.LENGTH_SHORT).show();
                binding.editText.setVisibility(View.GONE);
                binding.searchLocation.setVisibility(View.GONE);
                findUsersIn5KiloMeters();
                return true;


            case R.id.ten_km_distance:
                Toast.makeText(getActivity(),"All Users in 25 kms",Toast.LENGTH_SHORT).show();
                binding.editText.setVisibility(View.GONE);
                binding.searchLocation.setVisibility(View.GONE);
                findUsersIn25KiloMeters();
                return true;


            case  R.id.find_bloodBanks:
                Toast.makeText(getActivity(),"All Users in 25 kms",Toast.LENGTH_SHORT).show();
                binding.editText.setVisibility(View.VISIBLE);
                binding.searchLocation.setVisibility(View.VISIBLE);
        }


        return super.onOptionsItemSelected(item);
    }




    private void makeApiCalls(){

         String City = binding.editText.getText().toString();
        ApiBloodBank.getInstance().getApi().getBloodBanks("579b464db66ec23bdd000001cdd3946e44ce4aad7209ff7b23ac571b","json",0,10,City)
                .enqueue(new Callback<BloodBankPOJO>() {
                    @Override
                    public void onResponse(Call<BloodBankPOJO> call, Response<BloodBankPOJO> response) {
                       BloodBankList = response.body().getRecords();
                       for (Record i : BloodBankList)
                           Log.d("CITY HOSPITALS", i.getAddress());
                        if (mapFragment != null) {
                            mapFragment.getMapAsync(bloodBankLocations);
                        }

                    }

                    @Override
                    public void onFailure(Call<BloodBankPOJO> call, Throwable t) {

                    }
                });
    }

    private OnMapReadyCallback bloodBankLocations = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {

            googleMap.clear();
            for(Record i : BloodBankList){

                    double latitude = Double.parseDouble(i.getLatitude());
                    double longitude =  Double.parseDouble(i.getLongitude());
                    LatLng location = new LatLng(latitude,longitude);
                    googleMap.addMarker(new MarkerOptions().position(location).title(i.getBloodBankName()));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(500.0f));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));

            }

        }
    };
}