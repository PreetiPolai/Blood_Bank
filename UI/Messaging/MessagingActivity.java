package com.android.BloodBank.UI.Messaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.BloodBank.APIService;
import com.android.BloodBank.Adapter.MessageAdapter;
import com.android.BloodBank.Model.Chat;
import com.android.BloodBank.Notifications.Client;
import com.android.BloodBank.Notifications.Data;
import com.android.BloodBank.Notifications.MyResponse;
import com.android.BloodBank.Notifications.Sender;
import com.android.BloodBank.Notifications.Token;
import com.android.BloodBank.R;
import com.android.BloodBank.databinding.ActivityMessagingBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagingActivity extends AppCompatActivity {

    private ActivityMessagingBinding binding;
    private String ImageUrl;
    private  String Name;
    private String selectedUserId;
    private String timestamp;

    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private MessageAdapter adapter;

    private List<Chat> nChat;
    private List<Chat> chatsDemo = new ArrayList<>();

    private APIService apiService;

    private boolean notify = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityMessagingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //{notification} Api Service
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        //get current firebase user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //setAdapter
        setAdapter();

        //set up toolbar
        setUpToolBar();



        //        //for notification
          updateToken(FirebaseInstanceId.getInstance().getToken());


        //sending the message;
        binding.sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String mssg = binding.writeMessage.getText().toString();
                if(mssg.length() != 0){

                    sendMessage(firebaseUser.getUid(),selectedUserId,mssg);

                }

                else
                    Toast.makeText(getApplicationContext(),"You can't send empty message", Toast.LENGTH_SHORT).show();

                binding.writeMessage.setText("");
            }

        });



    }

    private void setUpToolBar(){
        //set up toolbar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");

        //set the toolbar content
        Intent intent = getIntent();
        Name = intent.getStringExtra("clickedUserName");
        ImageUrl = intent.getStringExtra("clickedUserProfileImage");
        selectedUserId = intent.getStringExtra("clickedUserID");
        //set the dp

        Glide.with(getApplicationContext())
                .asBitmap()
                .load(ImageUrl)
                .into(binding.ProfileImage);
        binding.userName.setText(Name);
        
        //check if the user is online or not
        checkStatus(selectedUserId);
        
        //firestore
        //check();

        //show messages in the display
        check(firebaseUser.getUid(),selectedUserId,ImageUrl);
    }

    private void checkStatus(String selectedUserId) {

       reference = FirebaseDatabase.getInstance().getReference("Users").child(selectedUserId).child("status");
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               String S = (String) snapshot.getValue();
               if(S.equals("online")){
                   binding.status.setVisibility(View.VISIBLE);
                   binding.status.setText(S);
               }
               else
                   binding.status.setVisibility(View.GONE);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }


    private void sendMessage(String sender, String receiver, String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);


        //get Current time
        long currentTime = Calendar.getInstance().getTimeInMillis();
        timestamp = currentTime + "";

//        //sending the value to chats too realtime firebase
//        reference.child("Chats").push().setValue(hashMap);


        Chat chat = new Chat(sender,receiver,message,timestamp,"unseen");

        //Adding the user phone no and type to the fireStore database
        Task<DocumentReference> d = db.collection("Chats").add(chat);

        //notification
        final String msg = message;


        DocumentReference docRef = db.collection("Users").document(sender);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

               if(notify) {
                   sendNotification(receiver, value.getString("userName"), msg);
               }
               notify = false;
            }
        });


    }


    //notification
    private void sendNotification(String receiver, String userName, String msg) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        com.google.firebase.database.Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){

                    Token token = snapshot1.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(),R.drawable.ic_notification,userName+ ": " + msg,"New Message",selectedUserId);
                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        if(response.body().success != 1){
                                            Toast.makeText(getApplicationContext(),"Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }







    private void setAdapter(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        binding.messagesRecycler.setLayoutManager(layoutManager);
    }

//    private void readMessages(String myId, String selectedUserId, String imageUrl){
//        mChat = new ArrayList<>();
//
//        reference = FirebaseDatabase.getInstance().getReference("Chats");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                 mChat.clear();
//                 for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//                     Chat chat = dataSnapshot.getValue(Chat.class);
//                     if (chat.getReceiver().equals(myId) && chat.getSender().equals(selectedUserId) ||
//                             chat.getReceiver().equals(selectedUserId) && chat.getSender().equals(myId)) {
//                         mChat.add(chat);
//                     }
//                 }
//
//                     adapter = new MessageAdapter(mChat,getApplicationContext(), imageUrl);
//                     binding.messagesRecycler.setAdapter(adapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }



    //get the values from the fireStore
    private void check(String myId, String selectedUserId, String imageUrl) {

        nChat =  new ArrayList<>();
        db.collection("Chats").orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                chatsDemo.clear();
                chatsDemo = value.toObjects(Chat.class);
                nChat.clear();

                for (Chat chat : chatsDemo) {
                    if (chat.getReceiver().equals(myId) && chat.getSender().equals(selectedUserId) ||
                            chat.getReceiver().equals(selectedUserId) && chat.getSender().equals(myId)) {
                        nChat.add(chat);
                    }
                }

                adapter = new MessageAdapter(nChat, getApplicationContext(), imageUrl);
                binding.messagesRecycler.setAdapter(adapter);

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
      seenMessage(selectedUserId);
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    private void seenMessage(String userId){
        db.collection("Chats").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Messaging Users", document.getId() + " => " + document.getData().get(Chat.class));
                        HashMap hashMap = (HashMap) document.getData();
                        Log.d("Values from cloud",hashMap.toString());
                        if(hashMap.get("sender").equals(userId) && hashMap.get("receiver").equals(firebaseUser.getUid())){
                            Log.d("MESSAGE STATUS ", "SEEN");
                            db.collection("Chats").document(document.getId()).update("messageSeenStatus","seen");
                        }
                    }
                } else {
                    Log.d("Users", "Error getting documents: ", task.getException());
                }
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