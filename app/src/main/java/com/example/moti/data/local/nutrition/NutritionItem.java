package com.example.moti.data.local.nutrition;

/**
 * Created by User on 02/05/2018.
 */

public class NutritionItem {
    private String hour;
    private String name;
    private int calories;

    public NutritionItem()
    {

    }

    public NutritionItem(String hour, String name, int calories) {
        this.hour = hour;
        this.name = name;
        this.calories = calories;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }





}
