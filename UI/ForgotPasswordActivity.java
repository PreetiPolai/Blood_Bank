package com.android.BloodBank.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.BloodBank.R;
import com.android.BloodBank.databinding.ActivityForgotPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {


    ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.ForgetPasswordemail.getEditText().getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Please enter the email",Toast.LENGTH_SHORT).show();
                else
                    sendResetPassword();
            }
        });
    }



    private void sendResetPassword(){

        String email = binding.ForgetPasswordemail.getEditText().getText().toString();
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Reset password email has been sent",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }
}