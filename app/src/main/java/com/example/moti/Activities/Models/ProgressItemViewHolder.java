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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moti.R;


public class ProgressItemViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "ProgressItemViewHolder";
    private final Activity activity;

    TextView date;
    ImageView image;

    public ProgressItemViewHolder(Activity activity, View itemView) {
        super(itemView);
        this.activity = activity;
        date = (TextView) itemView.findViewById(R.id.progress_item_text);
        image= (ImageView) itemView.findViewById(R.id.progress_item_image);

    }

    public void bind(ProgressItem pi) {
        date.setText(pi.getDate());
        image.setImageURI(Uri.parse(pi.getImage()));
        Glide.with(activity)
                .load(pi.getImage())
                .into(image);
    }
}