package com.example.moti.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.app.ProgressDialog;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.moti.Activities.Models.User;
import com.example.moti.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.logging.LogRecord;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "";
    private Button btnLogin;
    private Button btnLinkToRegister;
    private Intent homeIntent;
    private Intent registerIntent;
    private CheckBox loginCB;
    private AutoCompleteTextView loginEmail;
    private AutoCompleteTextView loginPassword;
    private ProgressDialog mDialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setBackgroundDrawableResource(R.drawable.login_bg);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Inits
        homeIntent = new Intent(this, HomeActivity.class);
        registerIntent = new Intent(this, RegisterActivity.class);
        loginEmail = (AutoCompleteTextView) findViewById((R.id.userLoginEmail));
        loginPassword = (AutoCompleteTextView) findViewById((R.id.userLoginPassword));
        loginCB = (CheckBox) findViewById(R.id.loginCheckBox);


        //Setting up the firebase authentication
        setupFirebaseAuth();


    }


    //Sign in button click, implement the login method here
    public void buttonSignIn(View view) {

        //Sign in credentials
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();


        //Dialog to show the user
        mDialog = new ProgressDialog((LoginActivity.this));
        mDialog.setMessage("Please wait...");
        mDialog.show();


        //Check if the user filled the credentials
        if (!isEmpty(email) && !isEmpty(password)) {
            //Try to login with the credentials the user inputted
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //The user is now logged in into the firebase authentication using his credentials.
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    //Let the user know his credentials was wrong =
                    mDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


    private boolean isEmpty(String s) {
        return s.equals("");
    }

    //Sign up text click
    public void buttonRegister(View view) {
        startActivity(registerIntent);
    }

/*
-----------------------------------------Firebase Setup------------------------------
 */

    private void setupFirebaseAuth() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //Check if a user is signed in
                if (user != null) {

                    //In case he is and he verified his Email
                    if (user.isEmailVerified()) {
                        //Let the user know he is now signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in: " + user.getUid());
                        Toast.makeText(LoginActivity.this, "Authenticated with: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        try {
                            mDialog.dismiss();
                        } catch (NullPointerException e) {
                            Log.e("Error", e.getMessage());
                        }
                        startActivity(homeIntent);
                        finish();
                    } else {

                        //Tell the user to verify his email
                        try {
                            mDialog.dismiss();
                        } catch (NullPointerException e) {
                            Log.e("Error", e.getMessage());
                        }
                        Toast.makeText(LoginActivity.this, "Please check your inbox for verification email", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }

                } else {
                    //User Signed out
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener((mAuthListener));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }
}
