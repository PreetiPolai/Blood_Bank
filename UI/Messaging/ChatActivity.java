package com.android.BloodBank.UI.Messaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.BloodBank.Adapter.ChatAdapter;
import com.android.BloodBank.Adapter.MessageAdapter;
import com.android.BloodBank.Adapter.SearchItemAdapter;
import com.android.BloodBank.Model.Chat;
import com.android.BloodBank.Model.UserProfileData;
import com.android.BloodBank.Notifications.Token;
import com.android.BloodBank.databinding.ActivityChatBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser;


    private ChatAdapter adapter;
    private List<UserProfileData> users;
    private List<UserProfileData> usersListDemo;
    private List<String> exchangedMessages;
    private List<Chat> chats;



    //element to be removed from the array
    UserProfileData a = new UserProfileData();


    @Override
    protected void onStart() {
        super.onStart();

        //set up toolbar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        exchangedMessages = new ArrayList<>();

        check();
        Toast.makeText(getApplicationContext(), "Messages", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setAdapter();



    }

    private void setAdapter(){
        binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }


//    private void checkMessages(){
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//               exchangedMessages.clear();
//
//               for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                   Chat chat = dataSnapshot.getValue(Chat.class);
//
//                   if(chat.getSender().equals(firebaseUser.getUid())){
//                       exchangedMessages.add(chat.getReceiver());
//                   }
//
//                   if(chat.getReceiver().equals(firebaseUser.getUid())){
//                       exchangedMessages.add(chat.getSender());
//                   }
//               }
//
//               readMessages();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

    private void readMessages(List<String> exchangedMessages) {

        users = new ArrayList<>();
        usersListDemo =  new ArrayList<>();
        CollectionReference docRef = db.collection("Users");
        docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                usersListDemo.clear();
                users.clear();
                usersListDemo = value.toObjects(UserProfileData.class);

                //                //remove current user from list
                for(UserProfileData i : usersListDemo)
                    if (i.getUserId().equals(FirebaseAuth.getInstance().getUid()))
                        a = i;

                usersListDemo.remove(a);

                //check for all senders and receivers in the earlier array
                for(String a : exchangedMessages)
                {
                    for (UserProfileData i : usersListDemo)
                            if (i.getUserId().equals(a)) {
                                users.add(i);
                            }

                    Set<UserProfileData> set = new HashSet<>(users);
                    users.clear();
                    users.addAll(set);

                }

                adapter = new ChatAdapter(users,getApplicationContext());

                DividerItemDecoration itemDecor = new DividerItemDecoration(getApplicationContext(), VERTICAL);
                binding.chatRecyclerView.addItemDecoration(itemDecor);
                binding.chatRecyclerView.setAdapter(adapter);

            }
        });

    }




    private void check() {

        chats =  new ArrayList<>();
        db.collection("Chats").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                chats.clear();
                exchangedMessages.clear();
                chats = value.toObjects(Chat.class);


                for (Chat chat : chats) {
                    if(chat.getSender().equals(firebaseUser.getUid())){
                        exchangedMessages.add(chat.getReceiver());
                    }

                    if(chat.getReceiver().equals(firebaseUser.getUid())){
                        exchangedMessages.add(chat.getSender());
                    }
                }

                readMessages(exchangedMessages);

            }
        });


        //for notification
        updateToken(FirebaseInstanceId.getInstance().getToken());

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



    //notification
    private void updateToken(String token){
        DatabaseReference reference  =  FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }
}
