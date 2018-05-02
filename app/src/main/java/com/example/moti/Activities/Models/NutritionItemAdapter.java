package com.example.moti.Activities.Models;

/**
 * Created by User on 13/02/2018.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.moti.R;


import java.util.ArrayList;
import java.util.List;


public class NutritionItemAdapter extends RecyclerView.Adapter<NutritionItemViewHolder> {
    private final Activity activity;
    private View view;

    private List<NutritionItem> nutrition = new ArrayList<>();

    public NutritionItemAdapter(Activity activity) {
        this.activity = activity;


    }

    public void addNutrition(NutritionItem pi) {
        nutrition.add(pi);
        notifyItemInserted(nutrition.size());

    }


    @Override
    public NutritionItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NutritionItemViewHolder(activity, activity.getLayoutInflater().inflate(R.layout.nutrition_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final NutritionItemViewHolder holder, final int position) {
        holder.bind(nutrition.get(position));
        holder.calories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.btn.getVisibility() == View.VISIBLE)
                {
                    holder.btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Delete item here
                            Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show();

                        }
                    });
                    holder.btn.setVisibility(View.INVISIBLE);
                    holder.btn.setClickable(false);
                }
                else{
                    holder.btn.setVisibility(View.VISIBLE);
                    holder.btn.setClickable(true);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return nutrition.size();
    }


}