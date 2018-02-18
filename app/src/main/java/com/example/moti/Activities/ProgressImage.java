package com.example.moti.Activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moti.R;

public class ProgressImage extends AppCompatActivity {

    private ImageView progressImage;
    private TextView progressText;
    private final int CLICK = 0;
    private final int LONG_CLICK = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_image);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int)(dm.widthPixels * 0.8);
        int height = (int)(dm.heightPixels * 0.6);

        getWindow().setLayout(width,height);

        Intent i = this.getIntent();

        String uri = i.getExtras().getString("IMAGE_URI");
        progressImage = (ImageView) findViewById(R.id.progress_item_activity_image);
        progressImage.setMinimumWidth(width);
        progressImage.setMinimumHeight(height);
        int state = i.getExtras().getInt("STATE");
        if(state == CLICK) {
            progressImage.setImageURI(Uri.parse(uri));
            Glide.with(this)
                    .load(uri)
                    .into(progressImage);
        }
        else
        {
            //int length = i.getExtras().getInt("LENGTH");
            Drawable[] dr;// = new Drawable[length];
            dr = (Drawable[]) i.getExtras().get("IMAGE_ANIMATION_ARRAY");
            AnimationDrawable ad = new AnimationDrawable();
            for(Drawable dra : dr)
            {
                ad.addFrame(dra,5);
            }
            ad.setOneShot(false);

            Glide.with(this)
                    .load(ad)
                    .into(progressImage);
            ad.start();
        }



    }

}
