package com.example.moti.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MidNightReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
/*Intent service1 = new Intent(context, MyAlarmService.class);
        context.startService(service1);*/
        Log.i("App", "called receiver method");
        try{
            //
            Utils.generateNotification(context,11222);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
