package com.example.srttata.appointmant.alerm;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.app.NotificationCompat;

import com.example.srttata.MainActivity;
import com.example.srttata.R;
import com.example.srttata.appointmant.received.ReceivedAlerm;

import java.util.Calendar;
import java.util.Objects;

public class Notification2 extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1;
    String CHANNEL_ID = "my_channel_01";
    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onReceive(Context context, Intent intent) {



        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.i("TAG", "Get Times" +" rereived");

        KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if( Objects.requireNonNull(myKM).inKeyguardRestrictedInputMode()) {
            Intent myAct = new Intent(context, ReceivedAlerm.class);
            myAct.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            myAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myAct);
        } else {
            Intent contentIntent = new Intent(context, Alerm.class);
            PendingIntent contentPendingIntent = PendingIntent.getActivity(
                    context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "01")
                    .setContentTitle("Hi")
                    .setSmallIcon(R.drawable.srt_logo)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("hi"))
                    .setContentText("hello")
                    .setAutoCancel(true)
                    .setSound(alarmSound)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(contentPendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {       // For Oreo and greater than it, we required Notification Channel.
                CharSequence name = "My New Channel";                   // The user-visible name of the channel.
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name, importance); //Create Notification Channel
                notificationManager.createNotificationChannel(channel);
            }    notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());

        }




    }

    private static PendingIntent createPendingIntent(Context context) {
        Intent intent = new Intent(context, Alerm.class);
        return PendingIntent.getService(context, 777, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    @SuppressLint("NewApi")
    private static String createNotificationChannel(String channelId, String channalName, Context context){
        @SuppressLint("InlinedApi") NotificationChannel chan = new  NotificationChannel(channelId, channalName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor( Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(chan);
        return channelId;
    }
}