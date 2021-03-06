package com.android.BloodBank.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.BloodBank.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {


    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //remove status and action bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //firebase
        mAuth = FirebaseAuth.getInstance();



        //set on click listener for the firebase login
        binding.logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpHere();
            }
        });



    }



    @Override
    protected void onStart() {
        super.onStart();

        //firebase
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }




    // setting authentication
    private void signUpHere(){
        //On clicking the button set the visibility of the progress bar
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.logIn.setVisibility(View.INVISIBLE);

        String email = binding.logInEmail.getEditText().getText().toString();
        String password = binding.logInPassword.getEditText().getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("UserProfileData Login", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Signed in successfully, show authenticated UI.
                            binding.progressBar.setVisibility(View.INVISIBLE);
                            binding.logInEmail.getEditText().setText("");
                            binding.logInPassword.getEditText().setText("");
                            binding.logIn.setVisibility(View.VISIBLE);

                            Toast.makeText(LogInActivity.this, "Registration Success",
                                    Toast.LENGTH_SHORT).show();


                            Intent intent = new Intent(LogInActivity.this, CreateProfileActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            binding.progressBar.setVisibility(View.INVISIBLE);
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LogInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }


}