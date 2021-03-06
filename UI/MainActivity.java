package com.android.BloodBank.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.android.BloodBank.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //run splash screen
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                FirebaseUser currentUser = mAuth.getCurrentUser();


                Intent myIntent;
                if ( currentUser != null) {
                    //check if there are users already logged in both in google and firebase
                    myIntent = new Intent(MainActivity.this, HomePageActivity.class);
                }

                else
                {
                    myIntent = new Intent(MainActivity.this, LoginVsSignUp.class);
                }
                startActivity(myIntent);
                finish();
            }
        },2000);


    }
}