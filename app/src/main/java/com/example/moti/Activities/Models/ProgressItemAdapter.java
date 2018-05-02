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

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.example.moti.Activities.ProgressImage;


public class ProgressItemAdapter extends RecyclerView.Adapter<ProgressItemViewHolder> {
    public static final String IMAGE_PREF = "ImagePreference";
    private static final String TAG = "ImageAdapter";
    private final Activity activity;
    private final int CLICK = 0;
    private final int LONG_CLICK = 1;
    private ProgressDialog pd;

    private List<ProgressItem> progress = new ArrayList<>();

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


                Bitmap[] ba = new Bitmap[position+1];
                ProgressItem pi;
                Uri uri;

                try {
                    for (int i = 0; i <= position; i++) {
                        pi = progress.get(i);
                        uri = Uri.parse(progress.get(i).getImage());
                        try {
                            ba[i] = bitmapFromUrl(pi.getImage());
                        } catch (IOException ios) {
                            Toast.makeText(activity, ios.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                }
                catch(Exception ex)
                {
                    Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

                Intent i = new Intent(activity,ProgressImage.class);
                DataWraper.setItems(ba);
                i.putExtra("STATE", LONG_CLICK);
                try {
                    //pd.dismiss();
                    activity.startActivity(i);
                }
                catch(Exception ex)
                {
                    Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return progress.size();
    }


    @NonNull
    public Bitmap bitmapFromUrl(String url) throws IOException {
        Bitmap x;
        MyDownloadTask mdt = new MyDownloadTask(activity);
        try{
           mdt.execute(url).get();
        }
        catch(Exception ex)
        {
            Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        x = mdt.getBitmap();
        return x;
    }

}
class MyDownloadTask extends AsyncTask<String,Void,Void>
{

    private InputStream input;
    private Bitmap x;
    private HttpURLConnection connection;
    private Activity act;
    private ProgressDialog pd;


    public MyDownloadTask(Activity act) {
        this.act = act;
        pd = new ProgressDialog(this.act);
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            this.connection = (HttpURLConnection) new URL(strings[0]).openConnection();
            connection.setDoInput(true);
            connection.connect();
            this.input = connection.getInputStream();
            this.x = BitmapFactory.decodeStream(this.input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Bitmap getBitmap()
    {
        return this.x;
    }
    protected void onPreExecute() {

        this.pd.setMessage("Pleas wait...");
        this.pd.show();

    }




    protected void onPostExecute(Void result) {
        this.pd.dismiss();
    }
}

