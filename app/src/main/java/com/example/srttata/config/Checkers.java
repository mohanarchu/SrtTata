package com.example.srttata.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.EditText;

public class Checkers {
    static final String PREF_LOGGEDIN_USER_EMAIL = "logged_in_email";
    static final String NAME = "NAME";
    private static final String PREF_USER_LOGGEDIN_STATUS = "logged_in_status";
    private static final String VALUE = "value";
    public static boolean isEmtpy(EditText editText){
        return TextUtils.isEmpty(editText.getText().toString());
    }
    public static String getText(EditText editText){
        return  editText.getText().toString().trim();
    }
    public static void setUserToken(Context ctx, String token)
    {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(VALUE,Context.MODE_PRIVATE).edit();
        editor.putString(PREF_LOGGEDIN_USER_EMAIL, token);
        editor.apply();
    }
    public static void setName(Context ctx, String token)
    {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(VALUE,Context.MODE_PRIVATE).edit();
        editor.putString(NAME, token);
        editor.apply();
    }

    public static void setUserLoggedInStatus(Context ctx, boolean status)
    {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(VALUE,Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREF_USER_LOGGEDIN_STATUS, status);
        editor.apply();
    }
    public static String getUserToken(Context ctx)
    {
        return ctx. getSharedPreferences(VALUE,Context.MODE_PRIVATE).getString(PREF_LOGGEDIN_USER_EMAIL, "");
    }
    public static boolean getUserLoggedInStatus(Context ctx)
    {
        return ctx. getSharedPreferences(VALUE,Context.MODE_PRIVATE).getBoolean(PREF_USER_LOGGEDIN_STATUS, false);
    }
    public static String getName(Context ctx)
    {
        return ctx. getSharedPreferences(VALUE,Context.MODE_PRIVATE).getString(NAME, "");
    }
    public static void clearLoggedInEmailAddress(Context ctx)
    {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(VALUE,Context.MODE_PRIVATE).edit();
        editor.remove(PREF_USER_LOGGEDIN_STATUS);
        editor.apply();
    }
}
