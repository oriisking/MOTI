package com.example.moti.Activities.Models;

/**
 * Created by User on 13/02/2018.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.moti.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.example.moti.Activities.ProgressImage;


public class ProgressItemAdapter extends RecyclerView.Adapter<ProgressItemViewHolder> {
    public static final String IMAGE_PREF = "ImagePreference";
    private static final String TAG = "ChatMessageAdapter";
    private final Activity activity;
    private final int CLICK = 0;
    private final int LONG_CLICK = 1;


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
    public void onBindViewHolder(final ProgressItemViewHolder holder, final int position) {
        holder.bind(progress.get(position));

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ProgressImage.class);
                i.putExtra("IMAGE_URI", progress.get(position).getImage());
                i.putExtra("STATE", CLICK);
                activity.startActivity(i);
            }
        });
        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AnimationDrawable ad = new AnimationDrawable();
                Drawable[] dr = new Drawable[position];
                ProgressItem pi;
                for(int i = 0;i<=position;i++)
                {
                    pi=progress.get(i);

                    try{
                        URL url;
                        URI uri = URI.create(pi.getImage());
                        url = uri.toURL();
                        dr[i] = drawableFromUrl(url.toString());
                    }
                    catch(IOException ios)
                    {
                        Toast.makeText(activity, ios.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                Intent i = new Intent(activity,ProgressImage.class);

                i.putExtra("IMAGE_ANIMATION_ARRAY",dr);
                i.putExtra("STATE", LONG_CLICK);
                i.putExtra("LENGTH",position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return progress.size();
    }


    @NonNull
    public Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;



        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        try{
            connection.connect();
        }
        catch(Exception ex)
        {
            Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(x);
    }

}