package com.example.moti.Activities.Models;

/**
 * Created by User on 13/02/2018.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.example.moti.R;

import java.util.ArrayList;
import java.util.List;

public class ProgressItemAdapter extends RecyclerView.Adapter<ProgressItemViewHolder> {
    private static final String TAG = "ChatMessageAdapter";
    private final Activity activity;
    List<ProgressItem> progress = new ArrayList<>();

    public ProgressItemAdapter(Activity activity) {
        this.activity = activity;
    }

    public void addProgress(ProgressItem pi) {
        progress.add(pi);
        notifyItemInserted(progress.size());
    }


    @Override
    public ProgressItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProgressItemViewHolder(activity, activity.getLayoutInflater().inflate(R.layout.progress_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ProgressItemViewHolder holder, int position) {
        holder.bind(progress.get(position));
    }

    @Override
    public int getItemCount() {
        return progress.size();
    }
}