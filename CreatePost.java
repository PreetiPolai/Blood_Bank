package com.android.BloodBank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.BloodBank.Model.GeoCodeLocation;
import com.android.BloodBank.Model.Post;
import com.android.BloodBank.Notifications.Client;
import com.android.BloodBank.Notifications.Data;
import com.android.BloodBank.Notifications.MyResponse;
import com.android.BloodBank.Notifications.Sender;
import com.android.BloodBank.Notifications.Token;
import com.android.BloodBank.UI.HomePageActivity;
import com.android.BloodBank.databinding.ActivityCreatePostBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePost extends AppCompatActivity {
    String date;
    String time;
    String Id;
    String postMessage;
    String Location;
    String BloodGroup;
    String Contact;

    private APIService apiService;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    ActivityCreatePostBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //{notification} Api Service
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        //        //for notification
        updateToken(FirebaseInstanceId.getInstance().getToken());


        clickListener();
    }

    private void gatherInformation(){
        Date currentTime = Calendar.getInstance().getTime();

        date = DateFormat.getDateTimeInstance().format(currentTime).toString();


        long mtime = Calendar.getInstance().getTimeInMillis();
        time = mtime + "";
        Id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        postMessage = binding.MessageBody.getText().toString();
        BloodGroup = binding.BloodGroup.getText().toString();
        Location = binding.Location.getText().toString();
        Contact = binding.ContactDetails.getText().toString();

        DocumentReference docRef = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                        String Name = (String) value.get("userName");
                        sendNotification(Name,postMessage);

            }
        });
    }


    private void clickListener(){
        binding.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatherInformation();
                if(binding.MessageBody.getText().toString().isEmpty() || binding.BloodGroup.getText().toString().isEmpty() || binding.Location.getText().toString().isEmpty() || binding.ContactDetails.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Please fill all the data", Toast.LENGTH_SHORT).show();
                else
                { //Adding the user phone no and type to the fireStore database

                    Post post = new Post(date,time,Id,postMessage,Location,BloodGroup,Contact);
                   db.collection("Posts").add(post);
                   finish();
                }
            }
        });
    }

    //notification
    private void sendNotification( String userName, String msg) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
       tokens.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){

                    Token token = snapshot1.getValue(Token.class);
                    String Key = snapshot1.getKey();
                    if(!Key.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        Data data = new Data(firebaseUser.getUid(), R.drawable.ic_notification, userName + ": " + msg, "New Post", Key);
                        Sender sender = new Sender(data, token.getToken());

                        apiService.sendNotification(sender)
                                .enqueue(new Callback<MyResponse>() {
                                    @Override
                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                        if (response.code() == 200) {
                                            if (response.body().success != 1) {
                                                Toast.makeText(getApplicationContext(), "TOKEN INVALID", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MyResponse> call, Throwable t) {

                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    //notification
    private void updateToken(String token){
        DatabaseReference reference  =  FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }

}