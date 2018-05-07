package com.example.moti.Activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.moti.Activities.Models.ProfileDetails;
import com.example.moti.Activities.Models.ProgressItem;
import com.example.moti.Activities.Models.ProgressItemAdapter;
import com.example.moti.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {

    Intent loginIntent;
    SharedPreferences profileSP;
    Intent homeIntent;
    EditText profileHourPicker;
    TimePickerDialog tpd;
    Calendar calendar;
    int currentHour;
    int currentMinute;

    AutoCompleteTextView profileName;
    AutoCompleteTextView profileStartingWeight;
    AutoCompleteTextView profileGoalWeight;
    CheckBox profileCB;
    private ProgressDialog mDialog;
    ImageButton profileBtn;
    private String formattedDate;
    String amPm;

    private FirebaseApp app;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private DatabaseReference databaseRef;
    private StorageReference storageRef;

    private ProfileDetails pd;
    public String selected_time="00:00";
    private PendingIntent pendingIntent;

    public static final String TAG = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        homeIntent = new Intent(this, HomeActivity.class);
        loginIntent = new Intent(this, LoginActivity.class);
        profileHourPicker = (EditText) findViewById(R.id.profileHourPickerField);
        profileName = (AutoCompleteTextView) findViewById(R.id.profileNameField);
        profileStartingWeight = (AutoCompleteTextView) findViewById(R.id.profileStartingWeightField);
        profileGoalWeight = (AutoCompleteTextView) findViewById(R.id.profileGoalWeightField);
        profileCB = (CheckBox) findViewById(R.id.profileCB);
        profileBtn = (ImageButton)findViewById(R.id.profileBtn);
        profileHourPicker.setClickable(false);
        pd = new ProfileDetails(1,1,"2");

        // Get the Firebase app and all primitives we'll use
        app = FirebaseApp.getInstance();
        database = FirebaseDatabase.getInstance(app);
        auth = FirebaseAuth.getInstance(app);
        user = auth.getCurrentUser();




        profileHourPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);
                tpd = new TimePickerDialog(ProfileActivity.this, R.style.TimePicker, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                        selected_time=hourOfDay+":"+minutes;
                        profileHourPicker.setText(hourOfDay+":"+minutes);
                    }
                },currentHour,currentMinute,true);

                tpd.show();
            }
        });





        homeIntent = new Intent(this, HomeActivity.class);
        mDialog = new ProgressDialog(this);


        databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbr = databaseRef.child("profile").child(auth.getCurrentUser().getUid().toString().trim());
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProfileDetails profileDetails = dataSnapshot.getValue(ProfileDetails.class);
                changeFieldsInit(profileDetails);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "NOPE", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void changeFieldsInit(ProfileDetails pd){
        try {
            if (pd.getGoalWeight() != 0) {
                profileGoalWeight.setText(Integer.toString(pd.getGoalWeight()));
            }

            if (pd.getStartingWeight() != 0) {
                profileStartingWeight.setText(Integer.toString(pd.getStartingWeight()));
            }
            if (pd.getReminderHour().trim() != "") {
                profileHourPicker.setText(pd.getReminderHour());
            }

            profileName.setText(user.getDisplayName());
        }
        catch(Exception ex)
        {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setNotification() {
        Calendar calendar = Calendar.getInstance();

        int hour= 00;
        if(selected_time.length()>0 && selected_time.contains(":")){ hour=Integer.parseInt(selected_time.split(":")[0]);}
        int minutes= 00;
        if(selected_time.length()>0 && selected_time.contains(":")){ minutes=Integer.parseInt(selected_time.split(":")[1]) + 1;}
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);


        Intent myIntent = new Intent(ProfileActivity.this, MyReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(ProfileActivity.this, 0, myIntent,0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis()+1000, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                1000 * 60 * 60 * 24, pendingIntent);
    }


    public void saveDataFromDetails(ProfileDetails profileDetails){
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("profile").child(auth.getCurrentUser().getUid().trim());
        ProfileDetails prof = new ProfileDetails();
        if(Integer.parseInt(profileGoalWeight.getText().toString().trim()) != 0) {
            prof.setGoalWeight(Integer.parseInt(profileGoalWeight.getText().toString().trim()));
        }

        if(Integer.parseInt(profileStartingWeight.getText().toString().trim()) != 0)
        {
            prof.setStartingWeight(Integer.parseInt(profileStartingWeight.getText().toString().trim()));
        }
        if(profileHourPicker.getText().toString() != "") {
            prof.setReminderHour(profileHourPicker.getText().toString());
        }
        dr.setValue(prof).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                setNotification();
                Toast.makeText(ProfileActivity.this, "Your details have been uploaded succesfully!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Failed uploading, please check your details.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void profileUpdateButton(View view){
        ProfileDetails prof = new ProfileDetails();
        DatabaseReference dbr = databaseRef.child("profile").child(auth.getCurrentUser().getUid().toString().trim());
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProfileDetails profileDetails = dataSnapshot.getValue(ProfileDetails.class);
                if(profileCB.isChecked())
                    saveDataFromDetails(profileDetails);
                else
                    Toast.makeText(ProfileActivity.this, "Please read the terms of use and agree with them.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "NOPE", Toast.LENGTH_SHORT).show();
            }
        });
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
