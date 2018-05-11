package com.example.util.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.util.helper.NotificationHelper;
import com.example.util.helper.NotificationService1;

public class MidNightReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
/*Intent service1 = new Intent(context, MyAlarmService.class);
        context.startService(service1);*/
        Log.i("App", "called receiver method");
        try{
            //
            Intent service1 = new Intent(context, NotificationService1.class);
            service1.setData((Uri.parse("custom://"+System.currentTimeMillis())));
            context.startService(service1);

            //  NotificationHelper.generateNotification(context,11222);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
