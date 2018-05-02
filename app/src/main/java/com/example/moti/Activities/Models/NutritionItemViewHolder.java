package com.example.moti.Activities.Models;

/**
 * Created by User on 12/02/2018.
 */

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moti.R;


public class NutritionItemViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "NutritionItemViewHolder";
    private final Activity activity;

     TextView hour;
     TextView name;
     TextView calories;
     ImageButton btn;


    public NutritionItemViewHolder(Activity activity, View itemView) {
        super(itemView);
        this.activity = activity;
        name = (TextView) itemView.findViewById(R.id.nutrition_item_name);
        hour = (TextView) itemView.findViewById(R.id.nutrition_item_hour);
        calories = (TextView) itemView.findViewById(R.id.nutrition_item_calories);
        btn = (ImageButton) itemView.findViewById(R.id.nutrition_item_delete_button);


    }

    public void bind(NutritionItem pi) {
        name.setText(pi.getName());
        hour.setText(pi.getHour());
        calories.setText(Integer.toString(pi.getCalories()));
    }
}