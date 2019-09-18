package com.example.gigspot.LoginRegistrationCode;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.gigspot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private EditText editTextFirstName; //first name
    private EditText editTextLastName; //last name
    private TextView textViewBirthday; //birthday
    private Button buttonDatePicker; //Date picker button
    private TextView textViewAge; // Age
    private EditText phone_text; //phone number
    private EditText Email_text; //email address
    private EditText pw_text; //Password
    private EditText editTextConfirmPassword; //confirm password
    private RadioGroup radio_group; //radio buttons for Talents and Talent Seekers
    private RadioButton radioButtonTorTS; //radio button to get
    private TextView create_btn; //Create an account button
    private TextView Have_an_account_btn; //Already have an account
    private ProgressDialog progressDialog; //pang loading effect
    private FirebaseAuth mAuth; //For registration
    private FirebaseFirestore firestoredb; // saving

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        textViewBirthday = findViewById(R.id.textViewBirthday);
        buttonDatePicker = findViewById(R.id.buttonDatePicker);
        textViewAge = findViewById(R.id.textViewAge);
        phone_text = findViewById(R.id.phone_text);
        Email_text = findViewById(R.id.Email_text);
        pw_text = findViewById(R.id.pw_text);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        radio_group = findViewById(R.id.radio_group);
        create_btn = findViewById(R.id.create_btn);
        Have_an_account_btn = findViewById(R.id.Have_an_account_btn);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        firestoredb = FirebaseFirestore.getInstance();

        create_btn.setOnClickListener(this);
        Have_an_account_btn.setOnClickListener(this);
        buttonDatePicker.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        if (view == create_btn){ //Goes to registration process
            registerUser();
        }
        if (view == buttonDatePicker){
            OpenDatePicker();
        }
        if (view == Have_an_account_btn){ //Goes to Login page 
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    //Registration process
    private void registerUser() {

        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String birthday = textViewBirthday.getText().toString().trim();
        String age = textViewAge.getText().toString().trim();
        String phone = phone_text.getText().toString().trim();
        String email = Email_text.getText().toString().trim();
        String password = pw_text.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (firstName.isEmpty()){
            editTextFirstName.setError("Please enter your first name");
            editTextFirstName.requestFocus();
            return;
        }
        if (lastName.isEmpty()){
            editTextLastName.setError("Please enter your last name");
            editTextLastName.requestFocus();
            return;
        }
        if (birthday.isEmpty()){
            textViewBirthday.setError("Please enter your birthday");
            textViewBirthday.requestFocus();
            return;
        }
        if (age.isEmpty()){
            textViewAge.setError("Please enter your age");
            textViewAge.requestFocus();
            return;
        }
        if(Integer.parseInt(age) < 18){
            Toast.makeText(this, "Only ages 18 and above are allowed.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.isEmpty()){
            phone_text.setError("Please enter your phone number");
            phone_text.requestFocus();
            return;
        }
        if (email.isEmpty()){
            Email_text.setError("Please enter your email address");
            Email_text.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){ //If valid email
            Email_text.setError("Please enter a valid email");
            Email_text.requestFocus();
            return;
        }
        if (password.isEmpty()){
            pw_text.setError("Please enter your password");
            pw_text.requestFocus();
            return;
        }
        if(password.length()<8) { //Length of password
            pw_text.setError("Minimum length of password should be 8");
            pw_text.requestFocus();
            return;
        }
        if (confirmPassword.isEmpty()){
            editTextConfirmPassword.setError("Please enter your password");
            editTextConfirmPassword.requestFocus();
            return;
        }
        if(confirmPassword.length()<8) { //Length of password
            editTextConfirmPassword.setError("Minimum length of password should be 8");
            editTextConfirmPassword.requestFocus();
            return;
        }
        if(!password.equals(confirmPassword)){
            editTextConfirmPassword.setError("Your password does not match");
            editTextConfirmPassword.requestFocus();
            return;
        }
        if(radio_group.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(this, "Please select an account type.", Toast.LENGTH_SHORT).show();
            return;
        }
       if(firstName.isEmpty() || lastName.isEmpty() || birthday.isEmpty() || age.isEmpty() || phone.isEmpty() ||
                age.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                radio_group.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(this, "Please verify all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        //Firebase Authentication for registering a user
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            addUser(); //initiates addUser class to save remaining details
                        }
                        else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "Email has been used already", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    //Process for sending email verification
    private void SendEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(RegisterActivity.this, "Account has been created successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, VerifyActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }


    // saving user's info to firestore
    private void addUser () {

        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String birthday = textViewBirthday.getText().toString().trim();
        String age = textViewAge.getText().toString().trim();
        String phone = phone_text.getText().toString().trim();
        String email = Email_text.getText().toString().trim();

        //getting radiobutton text
        int radioID = radio_group.getCheckedRadioButtonId();
        radioButtonTorTS = findViewById(radioID);
        String TorTS = radioButtonTorTS.getText().toString().trim();


        //getting signed in user UID in auth
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = authUser.getUid();

        DocumentReference dbUsers = firestoredb.collection("Users").document(userID);

        UsersGetSet users = new UsersGetSet(
                firstName,
                lastName,
                birthday,
                Integer.parseInt(age),
                phone,
                email,
                TorTS
        );

        dbUsers.set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                SendEmailVerification();//Goes to verification page

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_LONG).show();

            }
        });



    }

    //DatePicker
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.DEFAULT).format(c.getTime());

        textViewBirthday.setText(currentDateString);

        //Getting and calculating age
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - year;
        if (today.get(Calendar.DAY_OF_YEAR) < c.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        textViewAge.setText(ageInt.toString());


    }

    private void OpenDatePicker () {

        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");

    }
}
