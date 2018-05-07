package com.example.moti.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.moti.Activities.Models.WorkoutItem;
import com.example.moti.Activities.Models.WorkoutItemAdapter;
import com.example.moti.R;
import com.example.moti.util.lib.stickyindex.FastScroller;
import com.example.moti.util.lib.stickyindex.StickyIndex;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    Intent homeIntent;

    private List<WorkoutItem> workoutItems;
    private WorkoutItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        homeIntent = new Intent(this, HomeActivity.class);

        initRecyclerView();
    }

    private void initRecyclerView() {
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
        recyclerView.setAdapter(mAdapter);
    }

    public void workoutBackButton(View view) {
        startActivity(homeIntent);
    }

    public void workoutAddButton(View view) {

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
            Intent intent = new Intent(WorkoutActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
