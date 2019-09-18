package com.example.gigspot.LoginRegistrationCode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.gigspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private Button resend_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        resend_btn = findViewById(R.id.resend_btn);
        mAuth = FirebaseAuth.getInstance();

        resend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(VerifyActivity.this, "An email has been sent.", Toast.LENGTH_SHORT).show();
                    user.sendEmailVerification();
                    finish();
                }
            }
        });
    }
}
