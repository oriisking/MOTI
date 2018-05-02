package com.example.moti.Activities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moti.Activities.Models.NutritionItem;
import com.example.moti.Activities.Models.NutritionItemAdapter;
import com.example.moti.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NutritionActivity extends AppCompatActivity {

    Intent homeIntent;
    RecyclerView nutrition_recyclerview;
    NutritionItemAdapter adapter;
    List<NutritionItem> data;
    FirebaseDatabase database;
    DatabaseReference databaseRef;
    private String newName;
    private int newCalories = 0;
    private  AlertDialog mDialog;
    Calendar calendar;
    int currentHour,currentMinute;
    String currentFullHour;
    int totalCalories;
    TextView totalCalorieTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        homeIntent = new Intent(this, HomeActivity.class);
        totalCalorieTextView = (TextView)findViewById(R.id.nutritionCalorieSum);
        nutrition_recyclerview = (RecyclerView) findViewById(R.id.nutrition_recyclerview);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(getApplicationContext());
        nutrition_recyclerview.setLayoutManager(LM);
        nutrition_recyclerview.setHasFixedSize(false);
        adapter = new NutritionItemAdapter(this);
        nutrition_recyclerview.setAdapter(adapter);



        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("nutrition").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        calculateAndUpdateSum();
        databaseRef.addChildEventListener(new ChildEventListener() {

            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                NutritionItem ni = dataSnapshot.getValue(NutritionItem.class);
                adapter.addNutrition(ni);
                totalCalories = 0;
                calculateAndUpdateSum();
            }
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.resetNutrition();
                calculateAndUpdateSum();
            }
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            public void onCancelled(DatabaseError databaseError) {
                adapter.notifyDataSetChanged();

            }
        });



    }



    public void nutritionBackButton(View view) {
        startActivity(homeIntent);
    }

    public void nutritionAddButton(View view) {


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(NutritionActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.nutrition_add_dialog, null);
        final EditText name = (EditText) mView.findViewById(R.id.nutrition_dialog_name);
        final EditText calorie = (EditText) mView.findViewById(R.id.nutrition_dialog_calorie);
        Button button = (Button) mView.findViewById(R.id.nutrition_dialog_button);
        mDialog = null;
        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);
        currentFullHour = currentHour + ":" + currentMinute;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.getText().toString().isEmpty() && !calorie.getText().toString().isEmpty())
                {
                    newName = name.getText().toString();
                    newCalories = Integer.parseInt(calorie.getText().toString());
                    NutritionItem ni2 = new NutritionItem(currentFullHour,newName,newCalories);
                    addNewNutritionItem(ni2);
                    mDialog.dismiss();

                }
                else{
                    Toast.makeText(NutritionActivity.this, "Please fill the data.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mBuilder.setView(mView);
        mDialog = mBuilder.create();
        mDialog.show();

    }
    private void calculateAndUpdateSum()
    {
        totalCalories=0;
        DatabaseReference calcRef = FirebaseDatabase.getInstance().getReference("nutrition").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        calcRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() == 0) {
                    totalCalorieTextView.setText("Total Calories: " + totalCalories);
                }
                else
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        NutritionItem ni = ds.getValue(NutritionItem.class);
                        totalCalories += ni.getCalories();
                    }
                    totalCalorieTextView.setText("Total Calories: "+totalCalories);
                    totalCalories=0;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void addNewNutritionItem(final NutritionItem ni) {
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("nutrition").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() == 0)
                {
                    DatabaseReference dbRef = databaseRef.child("0");
                    dbRef.setValue(ni);
                }
                else
                {
                    int num = (int)dataSnapshot.getChildrenCount();
                    DatabaseReference dbRef = databaseRef.child(Integer.toString(num));
                    dbRef.setValue(ni);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
            Intent intent = new Intent(NutritionActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

}
