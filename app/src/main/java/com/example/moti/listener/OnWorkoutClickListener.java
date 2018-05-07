package com.example.moti.listener;

import android.view.View;

import com.example.moti.Activities.Models.WorkoutItem;

public interface OnWorkoutClickListener {
    void onItemClick(View view, WorkoutItem workoutItem);
}
