package com.example.moti.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.moti.ui.nutrition.NutritionActivity;
import com.example.moti.ui.profile.ProfileActivity;
import com.example.moti.ui.progress.ProgressActivity;
import com.example.moti.ui.workout.WorkoutActivity;
import com.example.moti.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    Intent workoutIntent;
    Intent progressIntent;
    Intent nutritionIntent;
    Intent profileIntent;
    SharedPreferences loginSP;
    TextView homeTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Inits
        workoutIntent = new Intent(this, WorkoutActivity.class);
        progressIntent = new Intent(this, ProgressActivity.class);
        nutritionIntent = new Intent(this, NutritionActivity.class);
        profileIntent = new Intent(this, ProfileActivity.class);
        loginSP = getSharedPreferences("Login", MODE_PRIVATE);
        homeTop = (TextView)findViewById(R.id.homeTopText);
        String userName = loginSP.getString("Name", "");
        homeTop.setText("WELCOME, " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }

    public void workoutButton(View view) {
        startActivity(workoutIntent);
    }

    public void progressButton(View view) {
        startActivity(progressIntent);
    }

    public void nutritionButton(View view) {
        startActivity(nutritionIntent);
    }

    public void profileButton(View view) {
        startActivity(profileIntent);
    }
}
