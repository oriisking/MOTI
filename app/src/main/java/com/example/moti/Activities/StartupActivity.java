package com.example.moti.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.moti.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.PrivateKey;

public class StartupActivity extends AppCompatActivity {

    ImageButton loginButton;
    Intent loginIntent;
    Intent homeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        //SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);


        loginIntent = new Intent(this, LoginActivity.class);
        homeIntent = new Intent(this, HomeActivity.class);
        loginButton = (ImageButton) findViewById(R.id.startupBackGroundButton);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user==null)
        {
            loginButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    startActivity(loginIntent);
                    finish();
                }

            });

        }
        else{
            loginButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    startActivity(homeIntent);
                    finish();
                }

            });
        }



    }

    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }


    private void checkAuthenticationState() {

    }


}
