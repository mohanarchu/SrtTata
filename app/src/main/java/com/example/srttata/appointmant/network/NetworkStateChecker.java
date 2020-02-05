package com.example.srttata.appointmant.network;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.srttata.appointmant.alerm.LocalData;
import com.example.srttata.appointmant.alerm.Notification2;
import com.example.srttata.appointmant.alerm.modelclass.LocalArray;
import com.example.srttata.details.UpdateModel;
import com.example.srttata.details.UpdatePresenter;
import com.example.srttata.home.DataModel;
import com.example.srttata.home.DataPojo;
import com.example.srttata.home.DataPresenter;
import com.google.gson.JsonObject;

import java.util.List;

import static android.content.Context.ALARM_SERVICE;

public class NetworkStateChecker extends BroadcastReceiver implements UpdateModel, DataModel {
    
    //context and database helper object
    private Context context;

    UpdatePresenter presenter;
    LocalData localData;
    DataPresenter dataPresenter;
    List<LocalArray> localArrays;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        presenter = new UpdatePresenter(context,this);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        
        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                //getting all the unsynced names
                localData = new LocalData(context);
                localArrays = localData.getAllDetails();
                if (localArrays != null){
                    for (int i=0;i<localArrays.size();i++){
                        Intent intents = new Intent(context, Notification2.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                                Integer.valueOf(localArrays.get(i).getId()), intents, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager)context. getSystemService(ALARM_SERVICE);
                        alarmManager.cancel(pendingIntent);
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("alarm",false);
                        jsonObject.addProperty("alarmDate", "");
                        jsonObject.addProperty("alarmInt","");
                     //      presenter.cancelAlarm( ,"","" jsonObject);
                        Log.i("TAG","Datas appeared"+localArrays.get(i).getOrdernumber());
                    }
                }
            }
        }
    }


    /*
    * method taking two arguments
    * name that is to be saved and id of the name from SQLite
    * if the name is successfully sent
    * we will update the status as synced in SQLite
    * */
    private void saveName(final int id, final String name) {

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

        localData.deleteId(localArrays.get(pos).getId());
    }
}