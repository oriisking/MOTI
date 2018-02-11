package com.example.moti.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.moti.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    Intent homeIntent;
    Intent loginIntent;
    SharedPreferences loginSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        homeIntent = new Intent(this, HomeActivity.class);
        loginIntent = new Intent(this, LoginActivity.class);
    }

    public void profileBackButton(View view) {
        startActivity(homeIntent);
    }

    public void profileLogoutButton(View view) {

       /* loginSP =getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor editor = loginSP.edit();
        editor.remove("isLogged");
        editor.remove("PhoneNumber");
        editor.remove("Name");
        editor.putBoolean("isLogged",false);
        editor.apply();
        */
        FirebaseAuth.getInstance().signOut();
        startActivity(loginIntent);
        finish();
    }


    /*
    ---------------------------Security--------------------------------
    */

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }

    private void checkAuthenticationState() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user==null)
        {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }




}
