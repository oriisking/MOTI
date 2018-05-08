package com.example.moti.data.local.workout;


import android.os.Parcel;
import android.os.Parcelable;

public class WorkoutItem implements Parcelable{
    private int id;
    private String exerciseDay;
    private String exerciseName;
    private int exerciseWeight;
    private int exerciseRepeats;

    public WorkoutItem()
    {

    }
    public WorkoutItem(int id, String exerciseDay, String exerciseName, int exerciseWeight, int exerciseRepeats) {
        this.id = id;
        this.exerciseDay = exerciseDay;
        this.exerciseName = exerciseName;
        this.exerciseWeight = exerciseWeight;
        this.exerciseRepeats = exerciseRepeats;
    }

    public WorkoutItem(String exerciseDay, String exerciseName, int exerciseWeight, int exerciseRepeats) {
        this.exerciseDay = exerciseDay;
        this.exerciseName = exerciseName;
        this.exerciseWeight = exerciseWeight;
        this.exerciseRepeats = exerciseRepeats;
    }


    protected WorkoutItem(Parcel in) {
        id = in.readInt();
        exerciseDay = in.readString();
        exerciseName = in.readString();
        exerciseWeight = in.readInt();
        exerciseRepeats = in.readInt();
    }

    public static final Creator<WorkoutItem> CREATOR = new Creator<WorkoutItem>() {
        @Override
        public WorkoutItem createFromParcel(Parcel in) {
            return new WorkoutItem(in);
        }

        @Override
        public WorkoutItem[] newArray(int size) {
            return new WorkoutItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExerciseDay() {
        return exerciseDay;
    }

    public void setExerciseDay(String exerciseDay) {
        this.exerciseDay = exerciseDay;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getExerciseWeight() {
        return exerciseWeight;
    }

    public void setExerciseWeight(int exerciseWeight) {
        this.exerciseWeight = exerciseWeight;
    }

    public int getExerciseRepeats() {
        return exerciseRepeats;
    }

    public void setExerciseRepeats(int exerciseRepeats) {
        this.exerciseRepeats = exerciseRepeats;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(exerciseName);
        dest.writeInt(exerciseWeight);
        dest.writeInt(exerciseRepeats);
    }
}
