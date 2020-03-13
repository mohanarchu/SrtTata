package cbots.b_to_c;

import android.content.Intent;
import android.os.Handler;

import cbots.b_to_c.CA.CreateCustomerActivity;
import cbots.b_to_c.config.Checkers;

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
               // startActivity(new Intent(SplashScreen.this, CreateCustomerActivity.class));
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
