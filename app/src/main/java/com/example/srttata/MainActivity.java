package com.example.srttata;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.srttata.appointmant.Appointment;
import com.example.srttata.appointmant.network.NetworkStateChecker;
import com.example.srttata.base.FragmentBase;
import com.example.srttata.config.ChangePassword;
import com.example.srttata.config.Checkers;
import com.example.srttata.decorations.fancy.Animation;
import com.example.srttata.decorations.fancy.FancyAlertDialog;
import com.example.srttata.decorations.fancy.FancyAlertDialogListener;
import com.example.srttata.decorations.fancy.Icon;
import com.example.srttata.details.SharedArray;
import com.example.srttata.hints.BlankFragment;
import com.example.srttata.home.Home_Frag;
import com.example.srttata.notification.Notification;
import com.example.srttata.search.SearchActivity;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
public class MainActivity extends AppCompatActivity {
    private final static int REQUEST_READ_SMS_PERMISSION = 3004;
    private final static int REQUEST_CALL_PERMISSION = 3003;
    public final static String READ_SMS_PERMISSION_NOT_GRANTED = "Please allow " + "SRT" + " to access your SMS from setting";
    public static final String TAB_HOME = "home";
    public static final String TAB_APPINTMENT = "appointment";
    public static final String TAB_NOTIFI = "notification";
    public static final String TAB_HINTS = "hints";
    Fragment fragments;
    public static   BottomNavigationView bottomNavigation;
    public static ImageView exit;
    public static View notificationBadge;
    FrameLayout mainContainer;
    boolean doubleBackToExitPressedOnce = false;
     ImageView commonSearch;
    Fragment fragmentTemp,  currentFragment;
    String current=TAB_HOME;
    RequestPermissionAction onPermissionCallBack;
    ImageView hints;
    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hints = findViewById(R.id.tips);
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        exit = findViewById(R.id.exit);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        commonSearch = findViewById(R.id.commonSearch);
        exit.setVisibility(View.VISIBLE);
        bottomNavigation.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        }
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigation.setItemIconTintList(null);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new Home_Frag()).commit();
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
        Intent intent = getIntent();
        if (intent != null){
            String id = intent.getStringExtra("id");
            Log.i("TAG","String get"+id);
        }


        hints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, ChangePassword.class));

//                bottomNavigation.setVisibility(View.GONE);
//                commonSearch.setVisibility(View.GONE);
//                hints.setVisibility(View.GONE);
//                selectedTab(TAB_HINTS);
            }
        });


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
                 }else if (fragmentTemp.getTag().equals("notification")){
                     intent.putExtra("type",false);
                     intent.putExtra("bools",false);
                 }
                 startActivity(intent);
             }
         });
    }

    @SuppressLint("SetTextI18n")
    public   static void addFirstView(int count, Context context) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigation.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(0);
        notificationBadge = LayoutInflater.from(context).inflate(R.layout.dot_view, menuView, false);
        TextView textView = notificationBadge.findViewById(R.id.notificationsBadgeTextView);
        textView.setText(count+"");
        itemView.addView(notificationBadge);
    }

    @SuppressLint("SetTextI18n")
    public   static void addSecondView(int count, Context context) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigation.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(1);
        notificationBadge = LayoutInflater.from(context).inflate(R.layout.dot_view, menuView, false);
        TextView textView = notificationBadge.findViewById(R.id.notificationsBadgeTextView);
        textView.setText(count+"");
        itemView.addView(notificationBadge);
    }

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
        }else {
            selectedTab(current);
            bottomNavigation.setVisibility(View.VISIBLE);
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
             //   current.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.default_transition));
               // current.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.no_transition));
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
            }else if (REQUEST_CALL_PERMISSION == requestCode){
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

    public interface RequestPermissionAction {
        void permissionDenied();

        void permissionGranted();
    }

}
