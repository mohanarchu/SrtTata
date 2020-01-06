package com.example.srttata.config;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SendSMs {



    public   static String  MultipleSMS(String[] MobNumber, String phoneNumber, String message,Context context) {

        Log.i("TAG","My texts"+ message.length());
        final String[] successmessage = {""};
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(
                SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(DELIVERED), 0);
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context,"send successfull",Toast.LENGTH_LONG).show();
                        ContentValues values = new ContentValues();
                        for (int i = 0; i < MobNumber.length; i++) {
                            values.put("address", MobNumber[i]);
                            // txtPhoneNo.getText().toString());
                            values.put("sms_body", "hello");
                        }
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                      successmessage[0] = "No service1..!";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        successmessage[0] = "No service1..!";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        successmessage[0] = "No service1..!";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        successmessage[0] = "No service1..!";
                        break;
                }
            }
        }, new IntentFilter(SENT));
      context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        successmessage[0] = "Delivered";
                        Log.i("TAG", "MessageDel" + " Delivered");
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i("TAG", "MessageDel" + " not delivered");
//                        Toast.makeText(getActivity(), "SMS not delivered",
//                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(String.valueOf(phoneNumber), null, message, sentPI, deliveredPI);
        return successmessage[0];
    }
}
