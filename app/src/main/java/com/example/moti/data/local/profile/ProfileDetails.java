package com.example.moti.data.local.profile;

import java.util.Date;

/**
 * Created by User on 29/04/2018.
 */

public class ProfileDetails {

    private int startingWeight;
    private int goalWeight;
    private String reminderHour;

    public ProfileDetails()
    {

    }
    public ProfileDetails(int startingWeight, int goalWeight, String reminderHour) {

        this.startingWeight = startingWeight;
        this.goalWeight = goalWeight;
        this.reminderHour = reminderHour;
    }

    public int getStartingWeight() {
        return startingWeight;
    }

    public void setStartingWeight(int startingWeight) {
        this.startingWeight = startingWeight;
    }

    public int getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(int goalWeight) {
        this.goalWeight = goalWeight;
    }

    public String getReminderHour() {
        return reminderHour;
    }

    public void setReminderHour(String reminderHour) {
        this.reminderHour = reminderHour;
    }


}
