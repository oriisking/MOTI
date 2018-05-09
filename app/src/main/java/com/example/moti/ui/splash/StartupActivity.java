package com.example.moti.ui.splash;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.moti.ui.home.HomeActivity;
import com.example.moti.ui.login.LoginActivity;
import com.example.util.receiver.MidNightReceiver;
import com.example.moti.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class StartupActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    ImageButton loginButton;
    Intent loginIntent;
    Intent homeIntent;
    SharedPreferences startupSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        //SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        setNotification();
        loginIntent = new Intent(this, LoginActivity.class);
        homeIntent = new Intent(this, HomeActivity.class);
        loginButton = (ImageButton) findViewById(R.id.startupBackGroundButton);
        Thread myThread = new Thread(){
            @Override
            public void run(){
                try{
                    sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });
                    sleep(2000);
                    startOneActivity();
                }
                catch(InterruptedException ex)
                {
                    ex.printStackTrace();
                }
            }
        };
        myThread.start();

        startupSP = getSharedPreferences("Startup",MODE_PRIVATE);
        startupSP.edit().putBoolean("ProfileInitial",true);



    }

    private void startOneActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        if(user==null)
        {
            startActivity(loginIntent);
            finish();
        }
        else{
            startActivity(homeIntent);
            finish();
        }

    }

    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }


    private void checkAuthenticationState() {

    }

    private void setNotification() {
        Calendar calendar = Calendar.getInstance();


        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 0);


        Intent myIntent = new Intent(StartupActivity.this, MidNightReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(StartupActivity.this, 0, myIntent,0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis()+1000, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                1000 * 60 * 60 * 24, pendingIntent);
    }




}
