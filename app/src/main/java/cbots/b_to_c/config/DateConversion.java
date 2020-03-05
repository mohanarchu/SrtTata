package cbots.b_to_c.config;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConversion {

    public static String getDay(String date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate="";
        try {
            Date day = inputFormat.parse(date);
            formattedDate = outputFormat.format(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }
    public static String getServerDate(String date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("dd EEE yyyy,HH:mm a");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy,HH:mm a");
        String formattedDate="";
        try {
            Date day = inputFormat.parse(date);
            formattedDate = outputFormat.format(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }
    public static Date getDats(String date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate="";
        Date day = null;
        try {
            day  = inputFormat.parse(date);
            formattedDate = outputFormat.format(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return day;
    }
    public static String getAlarmDate(String date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("dd EEE yyyy,HH:mm a");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy,HH:mm a");
        String formattedDate="";
        try {
            Date day = inputFormat.parse(date);
            formattedDate = outputFormat.format(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }
    public static String getMonth(String date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("MM");
        String formattedDate="";
        try {
            Date day = inputFormat.parse(date);
            formattedDate = outputFormat.format(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }
}
