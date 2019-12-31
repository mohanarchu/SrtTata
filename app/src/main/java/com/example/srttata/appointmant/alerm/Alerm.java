package com.example.srttata.appointmant.alerm;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.srttata.BaseActivity;
import com.example.srttata.R;
import com.example.srttata.appointmant.CenterZoomLayoutManager;
import com.example.srttata.base.KeyboardSensitiveRelativeLayout;
import com.example.srttata.decorations.MySelectorDecorator;
import com.example.srttata.decorations.OffsetItemDecoration;
import com.example.srttata.holder.AmPmAdapter;
import com.example.srttata.holder.TimeAdapter;
import com.example.srttata.holder.TimerClicked;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("Registered")
public class Alerm extends BaseActivity implements OnDateSelectedListener, TimerClicked {


    @BindView(R.id.hours)
    RecyclerView hours;
    @BindView(R.id.minutes)
    RecyclerView minutes;
    @BindView(R.id.AmPm)
    RecyclerView AmPm;
    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.layout_alerm)
    RelativeLayout layoutAlerm;
    @BindView(R.id.timerChange)
    EditText timerChange;
    @BindView(R.id.minuteChange)
    EditText minuteChange;
    @BindView(R.id.timerVisibility)
    LinearLayout timerVisibility;
    @BindView(R.id.exit)
    ImageView exit;
    @BindView(R.id.commonSearch)
    ImageView commonSearch;
    private int prevCenterPos;
    ArrayList<String> hoursArray, minuteArray, AmPmArray = new ArrayList<>();
    MySelectorDecorator mySelectorDecorator;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    int selectedHour, selectedMinute, selectedPm;
    boolean initail = false;
    KeyboardSensitiveRelativeLayout keyboardSensitiveRelativeLayout;
    TimeAdapter timeAdapter;
    CenterZoomLayoutManager centerZoomLayoutManager;
    CenterZoomLayoutManager centerZoomLayoutManagers;
    LocalData localData;
    Calendar calendar;
    int[] InputetdHour;
    boolean isOpened = false;

    @Override
    protected void onViewBound() {
        ButterKnife.bind(this);
        exit.setVisibility(View.GONE);
        commonSearch.setVisibility(View.GONE);
        calendar = Calendar.getInstance();
        initail = false;
        timerVisibility.setVisibility(View.GONE);
        localData = new LocalData(getApplicationContext());
        mySelectorDecorator = new MySelectorDecorator(this, CalendarDay.today());
        calendarView.addDecorators(mySelectorDecorator, oneDayDecorator);
        calendarView.setOnDateChangedListener(this);
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR);
        int minute = currentTime.get(Calendar.MINUTE);
        int Am = currentTime.get(Calendar.AM_PM);
        int date = currentTime.get(Calendar.DATE);
        calendar.set(Calendar.DATE, date);
        selectedMinute = minute;
        if (hour != 0) {
            selectedHour = hour - 1;
        } else {
            selectedHour = 11;
        }

        centerZoomLayoutManagers =
                new CenterZoomLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false, minutes);
        hoursArray = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            hoursArray.add(i + "");
        }
        addDecorates(hours, hoursArray, false, false, hour, centerZoomLayoutManager);
        minuteArray = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            minuteArray.add(i + "");
        }
        addDecorate(minutes, minuteArray, false, true, minute, 11);
        AmPmArray = new ArrayList<>();
        AmPmArray.add(0, "0");
        AmPmArray.add(1, "AM");
        AmPmArray.add(2, "PM");
        AmPmArray.add(3, "0");
        addDecoraters(AmPm, AmPmArray, true, false, Am);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//

                calendar.set(Calendar.HOUR, selectedHour + 1);
                calendar.set(Calendar.MINUTE, selectedMinute);
                //calendar.setTimeZone(TimeZone.getTimeZone("GMT-7.30"));
                calendar.setTimeZone(TimeZone.getDefault());
                if (minuteArray.get(selectedPm).equals("AM")) {
                    calendar.set(Calendar.AM_PM, 1);
                } else {
                    calendar.set(Calendar.AM_PM, 0);
                }

                Log.i("TAG", "Get Times" + minuteArray.get(selectedMinute) + " " + hoursArray.get(selectedHour)
                        + "   " + minuteArray.get(selectedPm) + "  " + calendar.getTimeInMillis() + "   " + date);
                Intent intent = new Intent(Alerm.this, Notification2.class);
                PendingIntent p1 = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                if (Build.VERSION.SDK_INT >= 23) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(), p1);
                } else if (Build.VERSION.SDK_INT >= 19) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), p1);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), p1);
                }
            }
        });
        cancel.setOnClickListener(view -> finish());

        keyboardSensitiveRelativeLayout = new KeyboardSensitiveRelativeLayout(getApplicationContext());
        keyboardSensitiveRelativeLayout.setKeyboardListener(new KeyboardSensitiveRelativeLayout.OnKeyboardShowHideListener() {
            @Override
            public void onKeyboardShowHide(boolean visible) {
                Log.i("TAG", "Keyboard Visible" + visible);
            }
        });
        layoutAlerm.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                layoutAlerm.getWindowVisibleDisplayFrame(r);
                int screenHeight = layoutAlerm.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    isOpened = true;
                    timerVisibility.setVisibility(View.VISIBLE);
                    hours.setVisibility(View.INVISIBLE);
                    minutes.setVisibility(View.INVISIBLE);

                } else {
                    isOpened = false;
                    timerVisibility.setVisibility(View.INVISIBLE);
                    hours.setVisibility(View.VISIBLE);
                    minutes.setVisibility(View.VISIBLE);

                }
            }
        });

    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_alerm;
    }

    public String getFormatedTime(int h, int m) {
        final String OLD_FORMAT = "HH:mm";
        final String NEW_FORMAT = "hh:mm a";
        String oldDateString = h + ":" + m;
        String newDateString = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, getCurrentLocale());
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDateString;
    }


    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }

    void addDecorates(RecyclerView itemDecorate, ArrayList<String> arrayList, boolean values, boolean twoDigits, int hour, RecyclerView.LayoutManager sizeForView) {
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(itemDecorate);
        centerZoomLayoutManager = new CenterZoomLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false, itemDecorate);
        itemDecorate.setLayoutManager(centerZoomLayoutManager);
        centerZoomLayoutManager.scrollToPositionWithOffset(118 + hour, 20);
        timeAdapter = new TimeAdapter(arrayList, values, twoDigits, getApplicationContext(), this);
        itemDecorate.setAdapter(timeAdapter);

        itemDecorate.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    selectMiddleHour(centerZoomLayoutManager, itemDecorate);
                }
            }
        });
    }

    void addDecorate(RecyclerView itemDecorate, ArrayList<String> arrayList, boolean values, boolean twoDigits, int minute, int sizeForView) {
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(itemDecorate);
        centerZoomLayoutManagers = new CenterZoomLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false, itemDecorate);
        itemDecorate.setLayoutManager(centerZoomLayoutManagers);
        centerZoomLayoutManagers.scrollToPositionWithOffset(120 + minute, 20);
        timeAdapter = new TimeAdapter(arrayList, values, twoDigits, getApplicationContext(), this);
        itemDecorate.setAdapter(timeAdapter);
        if (values) {
            itemDecorate.addItemDecoration(new OffsetItemDecoration(getApplicationContext(), 0, 0));
        }
        // itemDecorate.addItemDecoration(new OffsetItemDecoration(getActivity(),0,0));
        itemDecorate.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    selectMiddleMinute(centerZoomLayoutManagers, itemDecorate);
                }
            }
        });
    }

    void addDecoraters(RecyclerView itemDecorate, ArrayList<String> arrayList, boolean values, boolean twoDigits, int minute) {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(itemDecorate);
        CenterZoomLayoutManager centerZoomLayoutManager = new CenterZoomLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false, itemDecorate);
        itemDecorate.setLayoutManager(centerZoomLayoutManager);
        if (minute == 0) {
            centerZoomLayoutManager.scrollToPositionWithOffset(1, 20);
        } else {
            centerZoomLayoutManager.scrollToPositionWithOffset(2, 20);

        }

        itemDecorate.setAdapter(new AmPmAdapter(arrayList, values, twoDigits, getApplicationContext()));
        if (values) {
            itemDecorate.addItemDecoration(new OffsetItemDecoration(getApplicationContext(), 0, 0));
        }
        // itemDecorate.addItemDecoration(new OffsetItemDecoration(getActivity(),0,0));
        itemDecorate.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    selectMiddleAm(centerZoomLayoutManager, itemDecorate);
                }
            }
        });

    }

    private void selectMiddleHour(CenterZoomLayoutManager layoutManager, RecyclerView itemDecorate) {
        int firstVisibleIndex = layoutManager.findFirstVisibleItemPosition();
        int lastVisibleIndex = layoutManager.findLastVisibleItemPosition();
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = firstVisibleIndex; i <= lastVisibleIndex; i++) {
            list.add(i);
        }
//        int location = list.get(1);
//        View prevView = itemDecorate.getLayoutManager().findViewByPosition(location);
        selectedHour = list.get(2) % hoursArray.size();

        list.clear();
    }


    private void selectMiddleAm(CenterZoomLayoutManager layoutManager, RecyclerView itemDecorate) {
        int firstVisibleIndex = layoutManager.findFirstVisibleItemPosition();
        int lastVisibleIndex = layoutManager.findLastVisibleItemPosition();
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = firstVisibleIndex; i <= lastVisibleIndex; i++) {
            list.add(i);
        }

        selectedPm = list.get(1);
        list.clear();
    }

    private void selectMiddleMinute(CenterZoomLayoutManager layoutManager, RecyclerView itemDecorate) {
        int firstVisibleIndex = layoutManager.findFirstVisibleItemPosition();
        int lastVisibleIndex = layoutManager.findLastVisibleItemPosition();
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = firstVisibleIndex; i <= lastVisibleIndex; i++) {
            list.add(i);
        }


//         textView.setText("2");
        selectedMinute = list.get(2) % minuteArray.size();

        list.clear();
    }

    void hideView(View rootView) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int heightDiff = rootView.getRootView().getHeight() - (r.bottom - r.top);

                if (heightDiff >= 100) { // if more than 100 pixels, its probably a keyboard...
                    //ok now we know the keyboard is up...
                    Toast.makeText(getApplicationContext(), "IS visibled", Toast.LENGTH_SHORT).show();
                } else {
                    //ok now we know the keyboard is down...
                    Toast.makeText(getApplicationContext(), "IS not", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected
    ) {
        mySelectorDecorator.setDate(date.getDate());
        Log.i("TAG", "Get Times" + " " + date.getDay());

        calendar.set(Calendar.DATE, date.getDay());
        calendar.set(Calendar.YEAR, date.getYear());
        calendar.set(Calendar.MONTH, date.getMonth() - 1);

        widget.invalidateDecorators();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void clicked(int position, boolean isHour) {
        isOpened = true;
        timerVisibility.setVisibility(View.VISIBLE);
        hours.setVisibility(View.INVISIBLE);
        minutes.setVisibility(View.INVISIBLE);
        if (isHour) {
            initail = true;
            timerChange.setText(hoursArray.get(position % hoursArray.size()));
            setDone(timerChange);
            setDone(minuteChange);
            timerChange.setCursorVisible(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    shoeKeyboard(timerChange);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            timerChange.setFocusable(true);
                            timerChange.requestFocus();
                            timerChange.setSelectAllOnFocus(true);
                            timerChange.selectAll();
                            timerChange.setSelection(0, timerChange.getText().toString().length());
                        }
                    }, 400);

                    doneCLicked(timerChange);
                    doneCLicked(minuteChange);

                }
            }, 300);


        } else {

            minuteChange.setText(minuteArray.get(position % minuteArray.size()));
            setDone(minuteChange);
            new Handler().postDelayed(() -> {
                shoeKeyboard(minuteChange);
                new Handler().postDelayed(() -> {
                    minuteChange.setFocusable(true);
                    minuteChange.requestFocus();
                    minuteChange.setSelectAllOnFocus(true);
                    minuteChange.selectAll();
                    minuteChange.setSelection(0, minuteChange.getText().toString().length());
                }, 300);
                doneCLicked(minuteChange);
                doneCLicked(timerChange);
            }, 300);
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    void shoeKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }


    void setDone(EditText editText) {
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);

    }

    void doneCLicked(EditText editText) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    centerZoomLayoutManager.scrollToPositionWithOffset(118 + Integer.valueOf(timerChange.getText().toString()), 20);
                    centerZoomLayoutManagers.scrollToPositionWithOffset(119 + Integer.valueOf(minuteChange.getText().toString()), 20);
                    hideKeyboardFrom(getApplicationContext(), timerChange);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (isOpened) {
            Log.i("TAG", "Inputed Hour" + timerChange.getText().toString() + "  " + "Yes");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    //    centerZoomLayoutManager.scrollToPositionWithOffset(120+Integer.valueOf(minuteChange.getText().toString()),20);
                }
            }, 400);
        } else {
            finish();
        }
    }
}
