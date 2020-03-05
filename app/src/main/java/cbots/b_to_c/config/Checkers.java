package cbots.b_to_c.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class Checkers {
    static final String PREF_LOGGEDIN_USER_EMAIL = "logged_in_email";
    static final String MOBILE = "Mobile";
    static final String NAME = "NAME";
    static final String ROLE_ID = "role_id";
    static final String ROLE_NAME = "role_name";
    private static final String PREF_USER_LOGGEDIN_STATUS = "logged_in_status";
    private static final String VALUE = "value";
    private static SharedPreferences prefs = null;
    public static boolean returns = false;
    private static final String TEAMDetails = "teamDetails";


    public static boolean isEmtpy(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString());
    }
    public static String getText(EditText editText){
        return  editText.getText().toString().trim();
    }
    public static void setTeams(Context ctx, String id)
    {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(VALUE, MODE_PRIVATE).edit();
        editor.putString(TEAMDetails, id);
        editor.apply();
    }

    public static void setRoleId(Context ctx, int id)
    {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(VALUE, MODE_PRIVATE).edit();
        editor.putInt(ROLE_ID, id);
        editor.apply();
    }
    public static void setRoleName(Context ctx, String role)
    {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(VALUE, MODE_PRIVATE).edit();
        editor.putString(ROLE_NAME, role);
        editor.apply();
    }
    public static void setUserToken(Context ctx, String token)
    {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(VALUE, MODE_PRIVATE).edit();
        editor.putString(PREF_LOGGEDIN_USER_EMAIL, token);
        editor.apply();
    }
    public static void setName(Context ctx, String token)
    {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(VALUE, MODE_PRIVATE).edit();
        editor.putString(NAME, token);
        editor.apply();
    }
    public static void setMobile(Context ctx, String token)
    {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(VALUE, MODE_PRIVATE).edit();
        editor.putString(MOBILE, token);
        editor.apply();
    }
    public static void setUserLoggedInStatus(Context ctx, boolean status)
    {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(VALUE, MODE_PRIVATE).edit();
        editor.putBoolean(PREF_USER_LOGGEDIN_STATUS, status);
        editor.apply();
    }
    public static String getUserToken(Context ctx)
    {
        return ctx. getSharedPreferences(VALUE, MODE_PRIVATE).getString(PREF_LOGGEDIN_USER_EMAIL, "");
    }
    public static String getMobile(Context ctx)
    {
        return ctx. getSharedPreferences(VALUE, MODE_PRIVATE).getString(MOBILE, "");
    }
    public static boolean getUserLoggedInStatus(Context ctx)
    {
        return ctx. getSharedPreferences(VALUE, MODE_PRIVATE).getBoolean(PREF_USER_LOGGEDIN_STATUS, false);
    }
    public static String getName(Context ctx)
    {
        return ctx. getSharedPreferences(VALUE, MODE_PRIVATE).getString(NAME, "");
    }

    public static int getRoleId(Context ctx)
    {
        return ctx. getSharedPreferences(VALUE, MODE_PRIVATE).getInt(ROLE_ID, 1);
    }
    public static String getRole(Context ctx)
    {
        return ctx. getSharedPreferences(VALUE, MODE_PRIVATE).getString(ROLE_NAME, "");
    }
    public static String getTeamsDetails(Context ctx)
    {
        return ctx. getSharedPreferences(VALUE, MODE_PRIVATE).getString(TEAMDetails, "");
    }
    public static void clearLoggedInEmailAddress(Context ctx)
    {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(VALUE, MODE_PRIVATE).edit();
        editor.remove(PREF_USER_LOGGEDIN_STATUS);
        editor.apply();
    }
    public static boolean isNetworkConnectionAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context. getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }
    public  static  boolean firtTime(Context context){
        prefs = context.getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {
            prefs.edit().putBoolean("firstrun", false).apply();
            returns = true;
        }
        return returns;
    }
    public static void clearTempLogin(Context ctx)
    {
        SharedPreferences.Editor editor = ctx.getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE).edit();
        editor.remove("firstrun");
        editor.apply();
    }

    public static String[] getTeamDetails(Context context){
        List<String> elephantList = Arrays.asList(Checkers.getTeamsDetails(Objects.requireNonNull(context)).split("//"));
        String[] strings = new String[elephantList.size()];
        for (int i=0;i<strings.length;i++){
            strings[i] = elephantList.get(i);
        }
        return  strings;
    }

}
