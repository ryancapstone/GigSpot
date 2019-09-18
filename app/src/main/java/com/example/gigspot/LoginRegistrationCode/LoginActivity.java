package com.example.gigspot.LoginRegistrationCode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gigspot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email_txt; //Email
    private EditText password_txt; //Password
    private TextView forgotpw_btn; //ForgotPassword Button
    private TextView login_btn; //Login Button
    private TextView do_not_have_account_btn; //No account Button
    private ProgressDialog progressDialog; //Progress Dialog
    private FirebaseAuth mAuth; //For login firebase
    private boolean emailverify; //For email verification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_txt = findViewById(R.id.email_txt);
        password_txt = findViewById(R.id.password_txt);
        forgotpw_btn = findViewById(R.id.forgotpw_btn);
        login_btn = findViewById(R.id.login_btn);
        do_not_have_account_btn = findViewById(R.id.do_not_have_account_btn);

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        login_btn.setOnClickListener(this);
        do_not_have_account_btn.setOnClickListener(this);
        forgotpw_btn.setOnClickListener(this);
    }

    //User login process
    private void userLogin(){
        String email = email_txt.getText().toString().trim();
        String password = password_txt.getText().toString().trim();

        if(TextUtils.isEmpty(email)){

            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_txt.setError("Please enter a valid email");
            email_txt.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(password)){

            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("You are being logged in");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            finish();
                            verifyemailadd();
                        }
                        else {
                            //No email or password is found or error
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    //Email verification process kung verified or hindi
    private void verifyemailadd() {
        FirebaseUser user = mAuth.getCurrentUser();
        emailverify = user.isEmailVerified();

        if (emailverify)
        {
            //goes to home page
        }
        else
        {
            startActivity(new Intent(LoginActivity.this, VerifyActivity .class));
        }
    }

    //Check if user is signed in and update UI accordingly.
    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null){
            finish();
            verifyemailadd();
            //if email is verified automatically goes to home page when user starts the application
        }
    }

    @Override
    public void onClick (View view) {
        if (view == login_btn){
            userLogin();
        }
        if (view == do_not_have_account_btn) {
            finish();
            //goes to registration page
            startActivity(new Intent(this, RegisterActivity.class));
        }
        if (view == forgotpw_btn) {
            finish();
            startActivity(new Intent(this, Forgot_EmailPw_Activity.class));
        }
    }
}
