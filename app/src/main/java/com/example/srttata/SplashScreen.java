package com.example.srttata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.srttata.config.Checkers;

public class SplashScreen extends BaseActivity {



    @Override
    protected int layoutRes() {
        return R.layout.activity_splash_screen;
    }

    @Override
    protected void onViewBound() {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (Checkers.getUserLoggedInStatus(getApplicationContext())) {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                } else   {
                    startActivity(new Intent(SplashScreen.this, LoginScreen.class));
                    finish();
                }
            }
        }, 1000);

    }
}
