package com.example.util.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.util.helper.NotificationHelper;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
/*Intent service1 = new Intent(context, MyAlarmService.class);
        context.startService(service1);*/
        Log.i("App", "called receiver method");
        try{
            NotificationHelper.generateNotification(context,11121);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
