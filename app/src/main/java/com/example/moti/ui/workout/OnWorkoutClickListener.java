package com.example.moti.ui.workout;

import android.view.View;

import com.example.moti.data.local.workout.WorkoutItem;

public interface OnWorkoutClickListener {
    void onItemClick(View view, WorkoutItem workoutItem);
}
