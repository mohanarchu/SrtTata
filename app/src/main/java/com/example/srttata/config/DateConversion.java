package com.example.srttata.config;

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

        System.out.println(formattedDate); // prints 10-04-2018
        return formattedDate;
    }
}
