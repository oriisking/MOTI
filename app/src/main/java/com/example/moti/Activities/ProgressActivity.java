package com.example.moti.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.CameraProfile;
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
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.example.moti.Activities.Models.ProgressItem;
import com.example.moti.Activities.Models.ProgressItemAdapter;
import com.example.moti.Manifest;
import com.example.moti.R;
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

    Intent homeIntent;
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

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }

        homeIntent = new Intent(this, HomeActivity.class);
        progressList = (RecyclerView) findViewById(R.id.progress_recyclerview);
        mDialog = new ProgressDialog(this);

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

        databaseRef = database.getReference("progress").child(auth.getCurrentUser().getUid().toString());
        storageRef = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());


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

    public void progressBackButton(View view) {
        startActivity(homeIntent);
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
    private File createTemporaryFile(String part, String ext) throws Exception
    {
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        if(!tempDir.exists())
        {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            mDialog.setMessage("Uploading...");
            mDialog.show();

            //android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
            //Bitmap b = (Bitmap)data.getExtras().get("data");
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


            // Get a reference to the location where we'll store our photos
            storageRef = storage.getReference(auth.getCurrentUser().getUid().toString());
            // Get a reference to store file at chat_photos/<FILENAME>
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
