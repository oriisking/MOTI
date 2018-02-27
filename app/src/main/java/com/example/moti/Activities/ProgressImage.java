package com.example.moti.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.gifencoder.AnimatedGifEncoder;
import com.example.moti.Activities.Models.DataWraper;
import com.example.moti.R;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class ProgressImage extends AppCompatActivity {

    private ImageView progressImage;
    private TextView progressText;
    private final int CLICK = 0;
    private final int LONG_CLICK = 1;
    private GifImageView progressImageGif;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
        progressImageGif = (GifImageView) findViewById(R.id.progress_item_activity_image_gif) ;
        progressImage.setMinimumWidth(width);
        progressImage.setMinimumHeight(height);
        progressImageGif.setMinimumWidth(width);
        progressImageGif.setMinimumHeight(height);
        int state = i.getExtras().getInt("STATE");
        if(state == CLICK) {
            progressImage.setVisibility(View.VISIBLE);
            progressImageGif.setVisibility(View.INVISIBLE);
            Glide.with(this)
                    .load(uri)
                    .into(progressImage);
        }
        else
        {
            try {
                progressImage.setVisibility(View.INVISIBLE);
                progressImageGif.setVisibility(View.VISIBLE);
                //int length = i.getExtras().getInt("LENGTH");
                DataWraper dw = (DataWraper) getIntent().getSerializableExtra("IMAGE_ANIMATION_ARRAY");
                Bitmap[] ba;// = new Drawable[length];
                ba = dw.getItems();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                AnimatedGifEncoder encoder = new AnimatedGifEncoder();
                encoder.start(bos);
                encoder.addFrame(ba[0]);
                ba[0]=null;

                for (Bitmap dra : ba) {
                    if(ba != null)
                        encoder.addFrame(dra);
                }
                encoder.finish();
                GifDrawable gifFromBytes = null;
                byte[] rawGifBytes = bos.toByteArray();
                try {
                    gifFromBytes = new GifDrawable(rawGifBytes);
                } catch (IOException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }



                try {
                    //Toast.makeText(this, ba[1].getHeight() +"  "+ ba[1].getWidth(), Toast.LENGTH_SHORT).show();
                    gifFromBytes.setLoopCount(30);
                    gifFromBytes.setSpeed(0.1f);
                    progressImageGif.setBackground(gifFromBytes);
                    gifFromBytes.reset();
                    gifFromBytes.start();
                } catch (Exception ex) {
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
            catch(Exception e)
            {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }



    }

}
