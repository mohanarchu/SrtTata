package com.example.srttata.appointmant.alerm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.srttata.MainActivity;

public class AlarmReceiver extends   BroadcastReceiver {

    String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                LocalData localData = new LocalData(context);
           //     AlermManager.setReminder(context, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                return;
            }
        }
        Log.d(TAG, "onReceiveer: "+"yes");
        //Trigger the notification
        AlermManager.showNotification(context, Alerm.class,
                "You have 5 unwatched videos", "Watch them now?");
    }
}