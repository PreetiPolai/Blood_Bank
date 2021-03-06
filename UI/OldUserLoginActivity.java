package com.android.BloodBank.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.BloodBank.R;
import com.android.BloodBank.databinding.ActivityForgotPasswordBinding;
import com.android.BloodBank.databinding.ActivityOldUserLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OldUserLoginActivity extends AppCompatActivity {

    private ActivityOldUserLoginBinding binding;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOldUserLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //remove status and action bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //firebase
        mAuth = FirebaseAuth.getInstance();

        binding.logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.OldlogInPassword.getEditText().getText().toString().isEmpty() ||binding.OldlogInEmail.getEditText().getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Fill all details",
                            Toast.LENGTH_SHORT).show();
                else
                    signUpHere();
            }
        });

        binding.ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });


    }

    // setting authentication login
    private void signUpHere(){
        //On clicking the button set the visibility of the progress bar
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.logIn.setVisibility(View.INVISIBLE);

        String email = binding.OldlogInEmail.getEditText().getText().toString();
        String password = binding.OldlogInPassword.getEditText().getText().toString();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                       if(task.isSuccessful()) {
                           // Sign in success, update UI with the signed-in user's information
                           Log.d("UserProfileData Login", "createUserWithEmail:success");
                           FirebaseUser user = mAuth.getCurrentUser();
                           // Signed in successfully, show authenticated UI.
                           binding.progressBar.setVisibility(View.INVISIBLE);
                           binding.OldlogInEmail.getEditText().setText("");
                           binding.OldlogInPassword.getEditText().setText("");
                           binding.logIn.setVisibility(View.VISIBLE);

                           Toast.makeText(getApplicationContext(), "Login Success",
                                   Toast.LENGTH_SHORT).show();


                           Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                           startActivity(intent);
                           finish();
                       }
                       else {
                           // If sign in fails, display a message to the user.
                           binding.progressBar.setVisibility(View.INVISIBLE);
                           Log.w("TAG", "createUserWithEmail:failure", task.getException());
                           Toast.makeText(getApplicationContext(), "Authentication failed.",
                                   Toast.LENGTH_SHORT).show();
                       }
                    }
                });
    }
}