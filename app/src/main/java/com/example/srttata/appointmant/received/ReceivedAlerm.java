package com.example.srttata.appointmant.received;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.srttata.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReceivedAlerm extends AppCompatActivity {
    MediaPlayer player;
    @BindView(R.id.alermTime)
    TextView alermTime;
    @BindView(R.id.alermAmPm)
    TextView alermAmPm;
    @BindView(R.id.alermDate)
    TextView alermDate;
    @BindView(R.id.dismissAlerm)
    LinearLayout dismissAlerm;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_alerm);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        player = MediaPlayer.create(this, notification);
        player.setLooping(true);
        player.start();

        Calendar calendar = Calendar.getInstance();



        @SuppressLint("DefaultLocale") final String interval = String.format(
                "%d:%02d",
                calendar.get(Calendar.HOUR) != 0 ? calendar.get(Calendar.HOUR) : 12 ,
                calendar.get(Calendar.MINUTE)
        );

        alermAmPm.setText(calendar.get(Calendar.AM_PM) == Calendar.PM ? "PM" : "AM");
        alermTime.setText(interval);
        String days = new SimpleDateFormat("EEE").format(new Date());
        String month = new SimpleDateFormat("MMM").format(new Date());
        String dates = new SimpleDateFormat("dd").format(new Date());
        calendar.get(Calendar.DAY_OF_WEEK);
        @SuppressLint("DefaultLocale") final String date = String.format(
                "%s,%s %s",
               days,month,dates);
        alermDate.setText(date);
        dismissAlerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.stop();
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        player.stop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
        finish();
    }
}
