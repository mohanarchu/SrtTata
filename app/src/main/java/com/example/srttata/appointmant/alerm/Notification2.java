package com.example.srttata.appointmant.alerm;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
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
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.app.NotificationCompat;

import com.example.srttata.MainActivity;
import com.example.srttata.R;
import com.example.srttata.appointmant.alerm.modelclass.LocalArray;
import com.example.srttata.appointmant.received.ReceivedAlerm;
import com.example.srttata.config.Checkers;
import com.example.srttata.details.UpdateModel;
import com.example.srttata.details.UpdatePresenter;
import com.example.srttata.home.DataModel;
import com.example.srttata.home.DataPojo;
import com.example.srttata.home.DataPresenter;
import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
public class Notification2 extends BroadcastReceiver implements UpdateModel, DataModel {
    private static final int NOTIFICATION_ID = 1;
    String CHANNEL_ID = "my_channel_01";
    LocalData localData;
    DataPresenter dataPresenter ;
    UpdatePresenter updatePresenter;
    Context context;
    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        updatePresenter = new UpdatePresenter(context,this);
        dataPresenter = new DataPresenter(context,this);
        localData = new LocalData(context);
        int requestCode = Objects.requireNonNull(intent.getExtras()).getInt("requestCode");
        LocalArray localDa = localData.getDetails(String.valueOf(requestCode)).get(0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if(Objects.requireNonNull(myKM).inKeyguardRestrictedInputMode()) {
            Intent myAct = new Intent(context, ReceivedAlerm.class);
            myAct.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            myAct.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            myAct.putExtra("requestCode",requestCode);
            context.startActivity(myAct);
            localData.updateValue(localDa.getId(),"false");
        } else {
            try {
                boolean value =  new ForegroundCheckTask().execute(context).get();
                if (value){
                    updateDetails(localDa);
                    localData.deleteId(String.valueOf(requestCode));
                } else {
                    localData.updateValue(localDa.getId(),"false");
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            Intent intentAction = new Intent(context,ActionReceiver.class);
            intentAction.putExtra("num",localDa.getMobilenumber());
            PendingIntent   pIntentlogin = PendingIntent.getBroadcast(context,1,intentAction,PendingIntent.FLAG_UPDATE_CURRENT);
            Intent contentIntent = new Intent(context, MainActivity.class);
            contentIntent.putExtra("id",localDa.getId());
            contentIntent.putExtra("name",localDa.getName());
            PendingIntent contentPendingIntent = PendingIntent.getActivity(context,
                    NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "01")
                    .setContentTitle(localDa.getName())
                    .setSmallIcon(R.drawable.srt_logo)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(localDa.getMobilenumber() +"\n"+localDa.getOrdernumber()+
                                    "\n"+localDa.getAddress()))
                    .setAutoCancel(true)
                    .addAction(android.R.drawable.sym_action_call,"Call",pIntentlogin)
                    .setSound(alarmSound)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(contentPendingIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {       // For Oreo and greater than it, we required Notification Channel.
                CharSequence name = "My New Channel";                   // The user-visible name of the channel.
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name, importance); //Create Notification Channel
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());
        }
    }

    void updateDetails( LocalArray localDa){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("alarm",false);
        jsonObject.addProperty("alarmDate", "");
        jsonObject.addProperty("alarmInt",0);
        updatePresenter.update(localDa.getOrdernumber(),jsonObject,0);
    }



    @Override
    public void showDatas(DataPojo.Results[] results, DataPojo.Count[] counts, int total, int alarmCount) {

    }
    @Override
    public void success() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void success(int pos) {
        dataPresenter.getDetails(Checkers.getUserToken(context));
    }
    class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }
        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }

}