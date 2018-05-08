package com.example.moti.ui.nutrition;

/**
 * Created by User on 13/02/2018.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.moti.R;
import com.example.moti.data.local.nutrition.NutritionItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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

    public void resetNutrition()
    {
        nutrition.clear();
        notifyDataSetChanged();
    }


    @Override
    public NutritionItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NutritionItemViewHolder(activity, activity.getLayoutInflater().inflate(R.layout.nutrition_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final NutritionItemViewHolder holder, final int position) {
        holder.bind(nutrition.get(position));
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNutritionForADay();
            }
        });
        holder.calories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.btn.getVisibility() == View.VISIBLE)
                {
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

    public void deleteNutritionForADay()
    {
        Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nutrition").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.removeValue();
    }
    @Override
    public int getItemCount() {
        return nutrition.size();
    }


}