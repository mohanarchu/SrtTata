package cbots.b_to_c;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import cbots.b_to_c.Clients.MainInterface;
import cbots.b_to_c.appointmant.Appointment;
import cbots.b_to_c.appointmant.alerm.LocalData;
import cbots.b_to_c.appointmant.alerm.Notification2;
import cbots.b_to_c.appointmant.network.NetworkStateChecker;
import cbots.b_to_c.config.Checkers;
import cbots.b_to_c.decorations.fancy.Animation;
import cbots.b_to_c.decorations.fancy.FancyAlertDialog;
import cbots.b_to_c.decorations.fancy.FancyAlertDialogListener;
import cbots.b_to_c.decorations.fancy.Icon;
import cbots.b_to_c.details.UpdateModel;
import cbots.b_to_c.details.UpdatePresenter;
import cbots.b_to_c.hints.BlankFragment;
import cbots.b_to_c.home.DataModel;
import cbots.b_to_c.home.DataPojo;
import cbots.b_to_c.home.DataPresenter;
import cbots.b_to_c.home.Home_Frag;
import cbots.b_to_c.notification.Notification;
import cbots.b_to_c.search.SearchActivity;
import cbots.b_to_c.team_leader.TeamLeader;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DataModel , UpdateModel {
    private final static int REQUEST_READ_SMS_PERMISSION = 3004;
    private final static int REQUEST_CALL_PERMISSION = 3003;
    public final static String READ_SMS_PERMISSION_NOT_GRANTED = "Please allow " + "SRT" + " to access your SMS from setting";
    public static final String TAB_HOME = "home";
    public static final String TAB_APPINTMENT = "appointment";
    public static final String TAB_NOTIFI = "notification";
    public static final String TAB_HINTS = "hints";
    public static final String TAB_TL = "tl";
    Fragment fragments;
    public static   BottomNavigationView bottomNavigation;
    public static ImageView exit;
    public static View notificationBadge;
    FrameLayout mainContainer;
    boolean doubleBackToExitPressedOnce = false;
     ImageView commonSearch;
    Fragment fragmentTemp,  currentFragment;
    String current;
    RequestPermissionAction onPermissionCallBack;
    ImageView hints;
    DataPresenter dataPresenter;
    UpdatePresenter updatePresenter;
    LocalData localData;
    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSearch();
        if (Checkers.getRoleId(getApplicationContext()) == Integer.valueOf(MainInterface.TEAMLEADER)) {
            intTLScreen();
        } else {
            initSalesScreen();
        }
    }

    void intTLScreen() {
        current = TAB_TL;
        selectedTab(current);
        commonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    void initSearch() {
        exit = findViewById(R.id.exit);
        exit.setVisibility(View.VISIBLE);
        hints = findViewById(R.id.tips);
        commonSearch = findViewById(R.id.commonSearch);
        hints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(MainActivity.this, ChangePassword.class));

                commonSearch.setVisibility(View.GONE);
                hints.setVisibility(View.GONE);
                selectedTab(TAB_HINTS);
            }
        });
        exit.setOnClickListener(view -> new FancyAlertDialog.Builder(MainActivity.this)
                .setTitle("Do you really want to Exit ?")
                .setNegativeBtnText("No")
                .setPositiveBtnBackground(Color.RED)
                .setPositiveBtnText("Yes")
                .setNegativeBtnBackground(Color.GRAY )
                .setAnimation(Animation.POP)
                .isCancellable(true)
                .setIcon(R.drawable.ic_triangle, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(getApplicationContext(), "Thank you", Toast.LENGTH_SHORT).show();
                        Checkers.clearLoggedInEmailAddress(getApplicationContext());
                        Checkers.clearTempLogin(getApplicationContext());
                        Intent myAct = new Intent(MainActivity.this, LoginScreen.class);
                        myAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myAct);
                        finish();
                    }
                }).OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                    }
                })
                .build());

    }

    @SuppressLint("NewApi")
    void initSalesScreen() {
        current = TAB_HOME;
        selectedTab(current);
        localData = new LocalData(getApplicationContext());

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setVisibility(View.VISIBLE);
        dataPresenter = new DataPresenter(getApplicationContext(),this);
        updatePresenter = new UpdatePresenter(getApplicationContext(),this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        }
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigation.setItemIconTintList(null);
        bottomNavigation.setVisibility(View.VISIBLE);
        MainActivity.addFirstView(0,this);
        MainActivity.addSecondView(0,this);
        MainActivity.addThirdView(0,this);
        commonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                if (fragmentTemp  == null || fragmentTemp.getTag().equals("home") ) {
                    intent.putExtra("type",true);
                    intent.putExtra("bools",false);
                }else  if (fragmentTemp.getTag().equals("appointment")){
                    intent.putExtra("type",true);
                    intent.putExtra("bools",true);
                } else if (fragmentTemp.getTag().equals("notification")){
                    intent.putExtra("type",false);
                    intent.putExtra("bools",false);
                }
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    //  home documents total count
    @SuppressLint("SetTextI18n")
    public   static void addFirstView(int count, Context context) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigation.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(0);
        notificationBadge = LayoutInflater.from(context).inflate(R.layout.dot_view, menuView, false);
        TextView textView = notificationBadge.findViewById(R.id.notificationsBadgeTextView);
        textView.setText(count+"");
        itemView.addView(notificationBadge);
    }

    //  collected documents total count
    @SuppressLint("SetTextI18n")
    public   static void addSecondView(int count, Context context) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigation.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(1);
        notificationBadge = LayoutInflater.from(context).inflate(R.layout.dot_view, menuView, false);
        TextView textView = notificationBadge.findViewById(R.id.notificationsBadgeTextView);
        textView.setText(count+"");
        itemView.addView(notificationBadge);
    }

    // current date appointment count
    @SuppressLint("SetTextI18n")
    public static void addThirdView(int count, Context context) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigation.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(2);
        notificationBadge = LayoutInflater.from(context).inflate(R.layout.dot_view, menuView, false);
        TextView textView = notificationBadge.findViewById(R.id.notificationsBadgeTextView);
        textView.setText(count+"");
        itemView.addView(notificationBadge);
    }

    @Override
    public void onBackPressed() {

        if (fragmentTemp == null || !fragmentTemp.getTag().equals(TAB_HINTS)) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 1000);
        } else {
            selectedTab(current);
            if (Checkers.getRoleId(getApplicationContext()) == 1) {
                bottomNavigation.setVisibility(View.VISIBLE);
            }
            commonSearch.setVisibility(View.VISIBLE);
            hints.setVisibility(View.VISIBLE);

        }
    }

    @SuppressLint("NewApi")
    public void showFragmentWithTransition(Fragment current, Fragment newFragment, String tag, View sharedView, String sharedElementName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // check if the fragment is in back stack
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(tag, 0);
        if (fragmentPopped) {
            // fragment is pop from backStack
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                newFragment.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.default_transition));
                newFragment.setEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.no_transition));
            }

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_container, newFragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.addSharedElement(sharedView, sharedElementName);
            fragmentTransaction.commitAllowingStateLoss();

        }
    }

    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        bottomNavigation.setVisibility(View.VISIBLE);
        commonSearch.setVisibility(View.VISIBLE);
        switch (item.getItemId()) {
            case R.id.navigation_home:
                selectedTab(TAB_HOME);
                return true;
            case R.id.navigation_shoes:
                selectedTab(TAB_APPINTMENT);
                return true;
            case R.id.navigation_search:
                selectedTab(TAB_NOTIFI);
                return true;
        }
        return false;
    };


    private void selectedTab(String tabId) {
        if (!tabId.equals(TAB_HINTS)) {
            current = tabId;
        }
        switch (tabId) {
            case TAB_HOME:
                fragments = new Home_Frag();
                changeFragment(fragments, TAB_HOME);
                break;
            case TAB_APPINTMENT:
                changeFragment(new Appointment(), TAB_APPINTMENT);
                break;
            case TAB_NOTIFI:
                changeFragment(new Notification(), TAB_NOTIFI);
                break;
            case TAB_HINTS:
                changeFragment(new BlankFragment(),TAB_HINTS);
                break;
            case TAB_TL:
                changeFragment(new TeamLeader(), TAB_TL);
                break;
        }
    }

    public void changeFragment(Fragment fragment, String tagFragmentName) {



        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        Fragment  currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }
        fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(R.id.main_container, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction.show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }


    private boolean checkCallPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void getCallPermission(RequestPermissionAction onPermissionCallBack) {
        this.onPermissionCallBack = onPermissionCallBack;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkCallPermission()) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
                return;
            }
        }
        if (onPermissionCallBack != null)
            onPermissionCallBack.permissionGranted();
    }

    private boolean checkReadSMSPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
    public void getReadSMSPermission(RequestPermissionAction onPermissionCallBack) {
        this.onPermissionCallBack = onPermissionCallBack;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkReadSMSPermission()) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_READ_SMS_PERMISSION);
                return;
            }
        }
        if (onPermissionCallBack != null)
            onPermissionCallBack.permissionGranted();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (REQUEST_READ_SMS_PERMISSION == requestCode) {
                // TODO Request Granted for READ_SMS.
                System.out.println("REQUEST_READ_SMS_PERMISSION Permission Granted");
            } else if (REQUEST_CALL_PERMISSION == requestCode){
                System.out.println("REQUEST_READ_SMS_PERMISSION Permission Granted");
            }
            if (onPermissionCallBack != null)
                onPermissionCallBack.permissionGranted();
        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (REQUEST_READ_SMS_PERMISSION == requestCode) {
                // TODO REQUEST_READ_SMS_PERMISSION Permission is not Granted.
                // TODO Request Not Granted.
            }else if (REQUEST_CALL_PERMISSION == requestCode){
            }
            if (onPermissionCallBack != null)
                onPermissionCallBack.permissionDenied();
        }
    }

    @Override
    public void showDatas(DataPojo.Results[] results, DataPojo.Count[] counts, int total, int alarmCount) {
        List<DataPojo.Results> list =  new ArrayList<DataPojo.Results>(Arrays.asList(results));

        if (Checkers.getRoleId(getApplicationContext()) == 1)
        for (int i=0;i<list.size();i++){
            if (list.get(i).getAlarms() != null){
                for (DataPojo.Alarms alarms : list.get(i).getAlarms() ){
                    Calendar calendar = Calendar.getInstance();
                    List<String> timeSplit = Arrays.asList(alarms.getAlarmDate().split(","));
                    List<String> dateSplit = Arrays.asList(timeSplit.get(0).split("/"));
                    List<String> time = Arrays.asList(timeSplit.get(1).trim().split(" "));
                    List<String> minute = Arrays.asList(time.get(0).split(":"));
                    calendar.set(Calendar.DATE,Integer.valueOf(dateSplit.get(0).trim()));
                    calendar.set(Calendar.MONTH,Integer.valueOf(dateSplit.get(1).trim()) - 1);
                    calendar.set(Calendar.YEAR,Integer.valueOf(dateSplit.get(2).trim()));
                    calendar.set(Calendar.HOUR,Integer.valueOf(minute.get(0).trim()));
                    calendar.set(Calendar.MINUTE,Integer.valueOf(minute.get(1).trim()));
                    int amOm = 0;
                    if (time.get(1).trim().equals("PM")){
                        amOm = 1;
                    }
                    calendar.set(Calendar.AM_PM,amOm);
                    if (checkTime(calendar)) {
                        update(alarms.getAlarmInt(), list.get(i).getOrderNo());
                        localData.deleteId(String.valueOf(alarms.getAlarmInt()));
                    } else  {

                        if (Checkers.firtTime(getApplicationContext()) ) {
                            int randomCode = Integer.valueOf(alarms.getAlarmInt());
                            localData.deleteId(String.valueOf(randomCode));
                            List<String> address = Arrays.asList(list.get(i).getContactFullAddress().split(","));
                            localData.addItems( list.get(i).getContactName(),alarms.getAlarmDate(), list.get(i).getOrderNo() ,
                                    String.valueOf(randomCode),"true", address.get(0)+","+address.get(1),list.get(i).getContactPhones());
                                if (Integer.valueOf(minute.get(0).trim()) ==  12 && amOm == 1  ) {
                                    calendar.add(Calendar.DATE,-1);
                                }
                                Intent intent = new Intent(getApplicationContext(), Notification2.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), randomCode, intent, 0);
                                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                                alarmManager.cancel(pendingIntent);
                                Intent intents = new Intent(MainActivity.this, Notification2.class);
                                intents.putExtra("requestCode",randomCode);
                                PendingIntent p1 = PendingIntent.getBroadcast(getApplicationContext(), randomCode, intents, PendingIntent.FLAG_UPDATE_CURRENT);
                                AlarmManager alarmManagers = (AlarmManager) getSystemService(ALARM_SERVICE);
                                if (Build.VERSION.SDK_INT >= 23) {
                                    alarmManagers.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                                            calendar.getTimeInMillis(), p1);
                                } else if (Build.VERSION.SDK_INT >= 19) {
                                    alarmManagers.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), p1);
                                } else {
                                    alarmManagers.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), p1);
                                }
                         }
                    }
                }
            }
        }
    }
    private void update(String alarmValue,String orderNo){
        updatePresenter.cancelAlarm(orderNo,Integer.valueOf(alarmValue),0);
    }

    private   boolean checkTime(Calendar calendar){
        Calendar calendar1 = Calendar.getInstance();
        return calendar1.getTimeInMillis() > calendar.getTimeInMillis();
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

    }

    public interface RequestPermissionAction {
        void permissionDenied();
        void permissionGranted();
    }

}
