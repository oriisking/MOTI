package com.example.moti.ui.nutrition;

/**
 * Created by User on 12/02/2018.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moti.R;
import com.example.moti.data.local.nutrition.NutritionItem;


public class NutritionItemViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "NutritionItemViewHolder";
    private final Activity activity;

     TextView hour;
     TextView name;
     TextView calories;
     ImageView btn;


    public NutritionItemViewHolder(Activity activity, View itemView) {
        super(itemView);
        this.activity = activity;
        name = (TextView) itemView.findViewById(R.id.nutrition_item_name);
        hour = (TextView) itemView.findViewById(R.id.nutrition_item_hour);
        calories = (TextView) itemView.findViewById(R.id.nutrition_item_calories);
        btn = (ImageView) itemView.findViewById(R.id.nutrition_item_delete_button);


    }

    public void bind(NutritionItem pi) {
        name.setText(pi.getName());
        hour.setText(pi.getHour());
        calories.setText(Integer.toString(pi.getCalories()));
    }
}