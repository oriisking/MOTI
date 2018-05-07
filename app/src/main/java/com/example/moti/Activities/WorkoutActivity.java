package com.example.moti.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moti.Activities.Models.WorkoutItem;
import com.example.moti.Activities.Models.WorkoutItemAdapter;
import com.example.moti.R;
import com.example.moti.data.AppConst;
import com.example.moti.listener.OnWorkoutClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity implements View.OnClickListener, OnWorkoutClickListener{
    private ImageView btnAddWorkout;
    private RecyclerView recyclerView;

    Intent homeIntent;

    private List<WorkoutItem> workoutItems;
    private WorkoutItemAdapter mAdapter;

    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        homeIntent = new Intent(this, HomeActivity.class);

        initView();

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_workout:
                flag = AppConst.FLAG_SAVE;
                showCustomDialog(null);
                break;
        }
    }

    @Override
    public void onItemClick(View view, WorkoutItem workoutItem) {
        switch (view.getId()){
            case R.id.btn_edit:
                flag = AppConst.FLAG_EDIT;
                showCustomDialog(workoutItem);
                break;

            case R.id.btn_delete:
                handleDataDelete(workoutItem);
                break;
        }
    }

    private void initView() {
        btnAddWorkout = (ImageView) findViewById(R.id.btn_add_workout);
        btnAddWorkout.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        workoutItems = new ArrayList<>();
        for (int i = 1; i<=20; i++){
            String nm = "FBW";
            if(i>6) nm = "A ";
            if(i>10) nm = "B ";
            if(i>15) nm = "c ";
            workoutItems.add(new WorkoutItem(i, nm, "Exercise " + i, i, i));
        }

        mAdapter = new WorkoutItemAdapter (workoutItems, this);
        mAdapter.setListener(this);
        recyclerView.setAdapter(mAdapter);
    }



    private void loadData() {
        //load data from server here
        // add them to list
        //notify adapter
    }

    public void workoutBackButton(View view) {
        startActivity(homeIntent);
    }

    /*
    ---------------------------Security--------------------------------
    */

    private void checkAuthenticationState() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user==null)
        {
            Intent intent = new Intent(WorkoutActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void showCustomDialog(WorkoutItem item){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.prompt_workout, null);

        final TextView txtTitle = (TextView) view.findViewById(R.id.txt_title);
        final Spinner spDays = (Spinner) view.findViewById(R.id.sp_day);
        final EditText etExName = (EditText) view.findViewById(R.id.et_ex_name);
        final EditText etExWeight = (EditText) view.findViewById(R.id.et_ex_weight);
        final EditText etExRepeats = (EditText) view.findViewById(R.id.et_ex_repeats);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnSave = (Button) view.findViewById(R.id.btn_save);

        if(item != null){
            txtTitle.setText("Edit Exercise");
            spDays.setSelection(1);
            etExName.setText(item.getExerciseName());
            etExWeight.setText(String.valueOf(item.getExerciseWeight()));
            etExRepeats.setText(String.valueOf(item.getExerciseRepeats()));
            btnSave.setText("Update");
        }
        else {
            txtTitle.setText("Add Exercise");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spDays.getSelectedItemPosition() == 0)
                    Toast.makeText(WorkoutActivity.this, "Please select day", Toast.LENGTH_SHORT).show();
                else {
                    if(checkIfACTVIsEmpty(etExName))
                        Toast.makeText(WorkoutActivity.this,
                                "Please insert exercise name", Toast.LENGTH_SHORT).show();
                    else {
                        if(checkIfACTVIsEmpty(etExWeight))
                            Toast.makeText(WorkoutActivity.this,
                                    "Please insert weight", Toast.LENGTH_SHORT).show();
                        else {
                            if(checkIfACTVIsEmpty(etExRepeats))
                                Toast.makeText(WorkoutActivity.this,
                                        "Please insert number of repeats", Toast.LENGTH_SHORT).show();
                            else {
                                String selectedDay = spDays.getSelectedItem().toString();
                                String exName = etExName.getText().toString();
                                int exWeight = Integer.valueOf(etExWeight.getText().toString());
                                int exRepeats = Integer.valueOf(etExRepeats.getText().toString());

                                dialog.dismiss();
                                handleDataStore(new WorkoutItem(selectedDay, exName, exWeight, exRepeats));
                            }
                        }
                    }

                }
            }
        });

        dialog.show();
    }

    private boolean checkIfACTVIsEmpty(EditText editText) {
        if (editText == null || editText.getText().toString().equals(""))
            return true;

        return false;
    }

    private void handleDataStore(WorkoutItem workoutItem){
        if(flag == AppConst.FLAG_SAVE){
            //store new exercise here
            Toast.makeText(this, "Store: " + workoutItem.getExerciseName(), Toast.LENGTH_SHORT).show();
            mAdapter.addData(workoutItem);
        }
        else if(flag == AppConst.FLAG_EDIT){
            //update old exercise item here
            Toast.makeText(this, "Update: " + workoutItem.getExerciseName(), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleDataDelete(final WorkoutItem workoutItem){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + workoutItem.getExerciseName())
                .setMessage("Are you sure?")
                .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //delete exercise item here
                        Toast.makeText(WorkoutActivity.this, "Delete: " + workoutItem.getExerciseName(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}