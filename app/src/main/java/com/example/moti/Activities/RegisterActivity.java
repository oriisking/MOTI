package com.example.moti.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.app.ProgressDialog;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;


import com.example.moti.Activities.Models.User;
import com.example.moti.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegisterActivity extends AppCompatActivity {

    Intent homeIntent;
    Intent loginIntent;
    AutoCompleteTextView registerEmail;
    AutoCompleteTextView registerName;
    AutoCompleteTextView registerPassword;
    AutoCompleteTextView registerConfirmPassword;
    CheckBox registerCB;
    ImageButton registerBtn;
    private ProgressDialog pDialog;
    DatabaseReference table_user;
    public ProgressDialog mDialog;
    public String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        homeIntent = new Intent(this, HomeActivity.class);
        registerEmail = (AutoCompleteTextView) findViewById(R.id.registerEmailField);
        registerPassword = (AutoCompleteTextView) findViewById(R.id.registerPasswordField);
        registerConfirmPassword = (AutoCompleteTextView) findViewById(R.id.registerConfirmPasswordField);
        registerName = (AutoCompleteTextView)findViewById(R.id.registerNameField);


        loginIntent = new Intent(this, LoginActivity.class);
        registerBtn = (ImageButton)findViewById(R.id.signUpButton);
        registerCB = (CheckBox)findViewById(R.id.registerCB);

        registerBtn.setClickable(false);
        registerCB.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view)
            {
                if (checkCompability())
                {
                    registerBtn.setBackgroundResource(R.drawable.signup_button);
                    registerBtn.setClickable(true);
                }
                else {
                    registerBtn.setBackgroundResource(R.drawable.signup_gray_button);
                    registerBtn.setClickable(false);
                }
            }

        });

        //Init FirebaseDatabase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        table_user  = database.getReference("User");



    }
    //Sign up button, implement here register method
    public void buttonRegister(View view) {

        mDialog = new ProgressDialog((RegisterActivity.this));
        mDialog.setMessage("Please wait...");
        mDialog.show();

        final String email = registerEmail.getText().toString().trim();
        final String password = registerPassword.getText().toString().trim();
        final String confirmPassword = registerConfirmPassword.getText().toString().trim();
        name = registerName.getText().toString().trim();

        if (!isEmpty(email)
                && !isEmpty(password)
                && !isEmpty(confirmPassword)) {
            if (isValidEmail(email)) {
                if (doStringsMatch(password, confirmPassword)) {
                    //Enter register code here
                    registerNewEmail(email, password);
                } else {
                    mDialog.dismiss();
                    Toast.makeText(this, "Passwords does not match", Toast.LENGTH_SHORT).show();
                }
            } else {
                mDialog.dismiss();

                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            }

        } else {
            mDialog.dismiss();

            Toast.makeText(this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
        }


    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Sent verification email", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Coul'dnt send verification email", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


    private void registerNewEmail(String email, String password) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Sending email verification
                            sendVerificationEmail();

                            //Updating user's display name
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                            FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Completed display name change", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "Failed to update profile display name", Toast.LENGTH_SHORT).show();
                                }
                            });

                            //Sign out to confirm mail
                            FirebaseAuth.getInstance().signOut();
                            mDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Registered Succesfully, Please Verify Your Email!", Toast.LENGTH_SHORT).show();
                            startActivity(loginIntent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Unable to register", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }
                    }
                }
        );
    }


    private boolean doStringsMatch(String s1, String s2) {
        return s1.equals(s2);
    }

    private boolean isValidEmail(String email) {
        return email.contains("@");
    }

    private boolean isEmpty(String s) {
        return s.equals("");
    }

    public boolean checkIfACTVIsEmpty(AutoCompleteTextView actv) {
        boolean flag = false;
        if (actv.getText().toString().equals(""))
            flag = true;
        return flag;
    }

    public boolean checkCompability() {
        if (!checkIfACTVIsEmpty(registerConfirmPassword)
                && !checkIfACTVIsEmpty(registerEmail)
                && !checkIfACTVIsEmpty(registerPassword) && registerCB.isChecked()) {
            //Toast.makeText(this, "Compatible", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            //Toast.makeText(this, "Not Compatible", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
