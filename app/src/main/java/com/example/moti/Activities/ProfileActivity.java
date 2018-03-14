package com.example.moti.Activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.moti.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {

    Intent homeIntent;
    Intent loginIntent;
    SharedPreferences profileSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        homeIntent = new Intent(this, HomeActivity.class);
        loginIntent = new Intent(this, LoginActivity.class);







    }

    public void profileUpdateButton(View view)
    {



        //Need to change it to take the hour requested from the profile section.
        /*Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,13);
        calendar.set(Calendar.MINUTE,05);

        Intent notIntent = new Intent(getApplicationContext(),Notification_reciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,notIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        */
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeating_intent = new Intent(this,ProgressActivity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,100,repeating_intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle("Update Your Progress!")
                .setContentText("Don't forget to upload your daily progress pictures!")
                .setColor(16747520)
                .setAutoCancel(true);
        notificationManager.notify(100,builder.build());

       // profileSP = getSharedPreferences("Startup",MODE_PRIVATE);
        //profileSP.edit().putBoolean("ProfileInitial",false);
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
