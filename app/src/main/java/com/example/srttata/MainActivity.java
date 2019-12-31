package com.example.srttata;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.srttata.appointmant.Appointment;
import com.example.srttata.base.FragmentBase;
import com.example.srttata.config.Checkers;
import com.example.srttata.home.Home_Frag;
import com.example.srttata.notification.Notification;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
public class MainActivity extends AppCompatActivity {
    public static final String TAB_HOME = "home";
    public static final String TAB_APPINTMENT = "appointment";
    public static final String TAB_NOTIFI = "notification";
    Fragment fragments;
    public static   BottomNavigationView bottomNavigation;
    public static ImageView exit;
    public static View notificationBadge;
    FrameLayout mainContainer;
    public  static  ImageView commonSearch;
    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Closed", Toast.LENGTH_SHORT).show();
                Checkers.clearLoggedInEmailAddress(getApplicationContext());
                Intent myAct = new Intent(MainActivity.this, LoginScreen.class);
                myAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myAct);
            }
        });



       MainActivity.addFirstView(0,this);
        MainActivity.addSecondView(0,this);
        MainActivity.addThirdView(0,this);

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

        List fragmentList = getSupportFragmentManager().getFragments();

        boolean handled = false;
        for(Object f : fragmentList) {
            if(f instanceof FragmentBase) {
                handled = ((FragmentBase)f).onBackPressed();

                if(handled) {
                    break;
                }
            }
        }

        if(!handled) {
            super.onBackPressed();
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

    /* private void oneStepBack() {
         FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
         FragmentManager fragmentManager = getSupportFragmentManager();
         if (fragmentManager.getBackStackEntryCount() >= 2) {
             fragmentManager.popBackStackImmediate();
             fts.commit();
         } else {
             doubleClickToExit();
         }
     }

     // double back pressed function
     private static long back_pressed;*/
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {

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
        }
    }

    public void changeFragment(Fragment fragment, String tagFragmentName) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }
        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
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
}
