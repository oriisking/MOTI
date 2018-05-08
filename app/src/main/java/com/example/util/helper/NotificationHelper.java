package com.example.util.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.TaskStackBuilder;

import com.example.moti.ui.nutrition.NutritionActivity;
import com.example.moti.ui.progress.ProgressActivity;
import com.example.moti.R;

public class NotificationHelper {

    public static NotificationManager mManager;


    @SuppressWarnings("static-access")
    public static void generateNotification(Context context,int notification_id) {

        //custom notification
        android.support.v7.app.NotificationCompat.Builder nb = new android.support.v7.app.NotificationCompat.Builder(context);
        if(notification_id == 11121)
        {
            nb.setSmallIcon(R.mipmap.ic_launcher);
            nb.setContentTitle("Have you captured your progress today?");
            nb.setContentText("Don't forget to upload today's progress, click now!");
            nb.setTicker("Take a look");
            nb.setAutoCancel(true);
        }
        //midnight notification
        else if(notification_id == 11222)
        {
            nb.setSmallIcon(R.mipmap.ic_launcher);
            nb.setContentTitle("Finished eating?");
            nb.setContentText("Don't forget to clean your log, click now!");
            nb.setTicker("Take a look");
            //Where do I tell the notification to redirect the user to another activity?
            nb.setAutoCancel(true);
        }



        //get the bitmap to show in notification bar
        Bitmap bitmap_image = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        android.support.v7.app.NotificationCompat.BigPictureStyle s = new android.support.v7.app.NotificationCompat.BigPictureStyle().bigPicture(bitmap_image);
        s.setSummaryText("Notification reminder");
        nb.setStyle(s);


        Intent resultIntent = new Intent(context, ProgressActivity.class);
        //Thanks, you really helped me <3
        //Would'nt let you work without some payment, you're dismissed now xD
        if(notification_id == 11222){
            resultIntent = new Intent(context,NutritionActivity.class);
        }
        TaskStackBuilder TSB = TaskStackBuilder.create(context);
        TSB.addParentStack(ProgressActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        TSB.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                TSB.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        nb.setContentIntent(resultPendingIntent);
        nb.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notification_id, nb.build());

    }
}
