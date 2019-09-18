package com.example.gigspot.LoginRegistrationCode;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gigspot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_EmailPw_Activity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private Button reset; //reset button
    private EditText mail; //email textbox

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        mail = findViewById(R.id.mail);
        reset = findViewById(R.id.reset);
        mAuth = FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = mail.getText().toString();

                if(TextUtils.isEmpty(userEmail)){
                    mail.setError("Please enter a valid email");
                    mail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                    mail.setError("Please enter a valid email");
                    mail.requestFocus();
                    return;
                }
                else {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Forgot_EmailPw_Activity.this, "Please check your email account...", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Forgot_EmailPw_Activity.this, LoginActivity.class));
                            }
                            else{
                                String message = task.getException().getMessage();
                                Toast.makeText(Forgot_EmailPw_Activity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
