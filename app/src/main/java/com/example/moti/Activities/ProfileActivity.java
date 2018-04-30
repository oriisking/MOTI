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

                        profileHourPicker.setText(hourOfDay+":"+minutes);
                    }
                },currentHour,currentMinute,true);

                tpd.show();
            }
        });


        profileBtn.setClickable(false);
        profileCB.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view)
            {
                if (profileCB.isChecked())
                {
                    profileBtn.setBackgroundResource(R.drawable.signup_button);
                    profileBtn.setClickable(true);
                }
                else {
                    profileBtn.setBackgroundResource(R.drawable.signup_gray_button);
                    profileBtn.setClickable(false);
                }
            }

        });



        homeIntent = new Intent(this, HomeActivity.class);
        mDialog = new ProgressDialog(this);


        databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbr = databaseRef.child("profile").child(auth.getCurrentUser().getUid().toString().trim());
        dbr.addValueEventListener(new ValueEventListener() {
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


    public void changeFieldsInit(ProfileDetails pd)
    {
        if(pd.getGoalWeight() != 0)
        {
            profileGoalWeight.setText(pd.getGoalWeight());
        }

        if(pd.getStartingWeight() != 0)
        {
            profileStartingWeight.setText(pd.getStartingWeight());
        }
        if(pd.getReminderHour() != "")
        {
            profileHourPicker.setText(pd.getReminderHour());
        }

        profileName.setText(user.getDisplayName());
    }
    public void profileUpdateButton(View view)
    {
        final ProfileDetails[] profileDetails = new ProfileDetails[1];
        ProfileDetails prof = new ProfileDetails();
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profileDetails[0] = dataSnapshot.getValue(ProfileDetails.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(profileDetails[0].getGoalWeight() != 0) {
            prof.setGoalWeight(Integer.parseInt(profileGoalWeight.getText().toString().trim()));
        }

        if(profileDetails[0].getStartingWeight() != 0)
        {
            prof.setStartingWeight(Integer.parseInt(profileStartingWeight.getText().toString().trim()));
        }
        if(profileDetails[0].getReminderHour() != "")
        {
            prof.setReminderHour(profileHourPicker.getText().toString());
        }
        databaseRef.setValue(prof).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

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
