package com.example.moti.ui.progress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.moti.data.local.progress.ProgressItem;
import com.example.moti.R;
import com.example.moti.ui.home.HomeActivity;
import com.example.moti.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProgressActivity extends AppCompatActivity {

    static final int CAMERA_REQUEST_CODE = 1;

    private RecyclerView progressList;
    private Uri imageUri;
    private ProgressItem pi;
    private ProgressDialog mDialog;
    private String formattedDate;

    private FirebaseApp app;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseStorage storage;

    private DatabaseReference databaseRef;
    private StorageReference storageRef;

    private ProgressItemAdapter adapter;

    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("PROGRESS");
        myToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Permission to the camera and files
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, CAMERA_REQUEST_CODE);
        }

        //Variables initialize
        progressList = (RecyclerView) findViewById(R.id.progress_recyclerview);
        mDialog = new ProgressDialog(this);

        //The main RecyclerView initialize
        GridLayoutManager glm = new GridLayoutManager(this, 3);
        progressList.setHasFixedSize(false);
        progressList.setLayoutManager(glm);
        adapter = new ProgressItemAdapter(this);
        progressList.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                progressList.smoothScrollToPosition(adapter.getItemCount());
            }
        });

        // Get the Firebase app and all primitives we'll use
        app = FirebaseApp.getInstance();
        database = FirebaseDatabase.getInstance(app);
        auth = FirebaseAuth.getInstance(app);
        storage = FirebaseStorage.getInstance(app);

        //References to the location in the database and the storage
        databaseRef = database.getReference("progress").child(auth.getCurrentUser().getUid().toString());
        storageRef = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

        //Whenever a change is called into the database, add it to the RecycleView
        databaseRef.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot snapshot, String s) {
                // Get the Image from the snapshot and add it to the UI
                ProgressItem pi = snapshot.getValue(ProgressItem.class);
                adapter.addProgress(pi);
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
            public void onChildRemoved(DataSnapshot dataSnapshot) { }
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void progressAddButton(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Date c = (Date) Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        File photo = null;
        try
        {
            // place where to store camera taken picture
            photo = this.createTemporaryFile("picture", ".jpg");
            photo.delete();
        }
        catch(Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        mImageUri = Uri.fromFile(photo);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        try{
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
        catch(Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    private File createTemporaryFile(String part, String ext) throws Exception{
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");

        if(!tempDir.exists()) {
            try{

               tempDir.mkdirs();

            }
            catch(Exception ex)
            {
                Log.d("TAG",ex.toString());
                Toast.makeText(this, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }
        return File.createTempFile(part, ext, tempDir);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == CAMERA_REQUEST_CODE ) {
            mDialog.setMessage("Uploading...");
            mDialog.show();

            Bitmap b = null;
            try {
                b = MediaStore.Images.Media.getBitmap(this.getContentResolver(),mImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = (int)(dm.widthPixels * 0.8);
            int height = (int)(dm.heightPixels * 0.6);
            Bitmap newb = Bitmap.createScaledBitmap(b,width,height,true);
            Uri selectedImageUri = getImageUri(getApplicationContext(), newb);


            // Reference to the storage location
            storageRef = storage.getReference(auth.getCurrentUser().getUid().toString());
            // Get a reference to store file at progress
            final StorageReference photoRef = storageRef.child(mImageUri.getLastPathSegment());

            // Upload file to Firebase Storage

            photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // When the imag1e has successfully uploaded, we get its download URL
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            pi = new ProgressItem(formattedDate,downloadUrl.toString());
                            try {
                                databaseRef.push().setValue(pi).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ProgressActivity.this, "Failed to upload.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnSuccessListener(ProgressActivity.this, new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ProgressActivity.this, "Upload Completed.", Toast.LENGTH_SHORT).show();
                                        mDialog.dismiss();
                                    }
                                });
                            }
                            catch (Exception ex)
                            {
                                Toast.makeText(ProgressActivity.this, "Error: " + ex.toString(), Toast.LENGTH_SHORT).show();
                            }
                            // Set the download URL to the message box, so that the user can send it to the database

                        }
                    });

        }
        else if (resultCode == -1)
        {
            Toast.makeText(this, "Error reading the image.", Toast.LENGTH_SHORT).show();
        }
    }

/*
    ---------------------------Security--------------------------------
    */

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }

    private void checkAuthenticationState() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user==null)
        {
            Intent intent = new Intent(ProgressActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }



}