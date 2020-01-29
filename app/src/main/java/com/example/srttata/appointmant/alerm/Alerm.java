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
import com.example.srttata.decorations.calendar_selectedDates;
import com.example.srttata.details.SharedArray;
import com.example.srttata.details.UpdateModel;
import com.example.srttata.details.UpdatePresenter;
import com.example.srttata.holder.AmPmAdapter;
import com.example.srttata.holder.TimeAdapter;
import com.example.srttata.holder.TimerClicked;
import com.example.srttata.home.DataPojo;
import com.google.gson.JsonObject;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.LocalDate;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("Registered")
public class Alerm extends BaseActivity implements OnDateSelectedListener, TimerClicked , UpdateModel {


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


    private int prevCenterPos;
    ArrayList<String> hoursArray, minuteArray, AmPmArray = new ArrayList<>();
    MySelectorDecorator mySelectorDecorator;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    int selectedHour, selectedMinute, selectedPm;

    KeyboardSensitiveRelativeLayout keyboardSensitiveRelativeLayout;
    TimeAdapter timeAdapter;
    CenterZoomLayoutManager centerZoomLayoutManager;
    CenterZoomLayoutManager centerZoomLayoutManagers;
    LocalData localData;
    Calendar calendar;
    int[] InputetdHour;
    boolean isOpened = false;
    List<DataPojo.Results> commonArray;
    DataPojo.Results  results ;
    UpdatePresenter updatePresenter;

    @Override
    protected void onViewBound() {
        ButterKnife.bind(this);
        commonArray = SharedArray.getFilterResult();
        Intent intent = getIntent();
        if (intent != null){
            if (commonArray != null)
            results = commonArray.get(intent.getIntExtra("position",0));
        }
        calendar = Calendar.getInstance();

        timerVisibility.setVisibility(View.GONE);
        updatePresenter = new UpdatePresenter(getApplicationContext(),this);
        localData = new LocalData(getApplicationContext());
        mySelectorDecorator = new MySelectorDecorator(this, CalendarDay.today());
        calendarView.addDecorators(mySelectorDecorator, oneDayDecorator);
        calendarView.setOnDateChangedListener(this);
        org.threeten.bp.LocalDate min =  getLocalDate(calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+1+"-"+calendar.get(Calendar.DATE));
        calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(min.getYear(),   min.getMonthValue(), 1))
                .setMaximumDate(CalendarDay.from(min.getYear(),   min.getMonthValue()+5, min.getDayOfMonth()))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        calendarView.addDecorator(new calendar_selectedDates(CalendarDay.from(min.getYear(),   min.getMonthValue(), min.getDayOfMonth())));
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR);
        int minute = currentTime.get(Calendar.MINUTE);
        int Am = currentTime.get(Calendar.AM_PM);
        int date = currentTime.get(Calendar.DATE);
        calendar.set(Calendar.AM_PM,Am);
        Log.i("TAG","Get Times"+ Am +"  "+ hour);
        calendar.set(Calendar.DATE, date);
        selectedPm = Am;
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
                calendar.set(Calendar.HOUR, selectedHour + 1);
                calendar.set(Calendar.MINUTE, selectedMinute);
                //calendar.setTimeZone(TimeZone.getTimeZone("GMT-7.30"));
                calendar.setTimeZone(TimeZone.getDefault());
                calendar.set(Calendar.AM_PM,selectedPm);
                Log.i("TAG", "Get Times"+calendar.get(Calendar.HOUR_OF_DAY) +"  "+calendar.get(Calendar.HOUR) );
                if (calendar.get(Calendar.HOUR_OF_DAY) == 0){
                    if (selectedPm ==  0) {
                        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
                        calendar.set(Calendar.AM_PM, Calendar.AM);
                    } else {
                        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
                        calendar.set(Calendar.AM_PM, Calendar.AM);
                    }
                }
                Log.i("TAG", "Get Times" + calendar.get(Calendar.HOUR_OF_DAY)   +"  "+selectedPm +
                        "  " + calendar.getTimeInMillis() + " " );

               if (checkTime(calendar)){
                   showMessage("kindly choose future time");
               }else {
                   if (commonArray != null){
                       assert results.getAlarmInt() != null;
                       if (Boolean.valueOf(results.getAlarm())){
                           Intent intent = new Intent(getApplicationContext(), Notification2.class);
                           PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), Integer.valueOf(results.getAlarmInt()), intent, 0);
                           AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                           alarmManager.cancel(pendingIntent);
                           localData.deleteId(results.getAlarmInt());
                           Intent intents = new Intent(Alerm.this, Notification2.class);
                           int randomCode = getRandom();
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
                      updateAlarm(calendar,randomCode,results);
                       }  else {
                           int randomCode = getRandom();
                           Intent intents = new Intent(Alerm.this, Notification2.class);
                           intents.putExtra("requestCode",randomCode);
                           PendingIntent p1 = PendingIntent.getBroadcast(getApplicationContext(),randomCode, intents, PendingIntent.FLAG_UPDATE_CURRENT);
                           AlarmManager alarmManagers = (AlarmManager) getSystemService(ALARM_SERVICE);
                           if (Build.VERSION.SDK_INT >= 23) {
                               alarmManagers.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                                       calendar.getTimeInMillis(), p1);
                           } else if (Build.VERSION.SDK_INT >= 19) {
                               alarmManagers.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), p1);
                           } else {
                               alarmManagers.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), p1);
                           }
                    updateAlarm(calendar,randomCode,results);
                       }
                   }
               }
            }
        });
        cancel.setOnClickListener(view -> finish());
        keyboardSensitiveRelativeLayout = new KeyboardSensitiveRelativeLayout(getApplicationContext());
        keyboardSensitiveRelativeLayout.setKeyboardListener(visible -> {

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
       // @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    boolean checkTime(Calendar calendar){
        Calendar calendar1 = Calendar.getInstance();
        return calendar1.getTimeInMillis() > calendar.getTimeInMillis();
    }

    @SuppressLint("NewApi")
    org.threeten.bp.LocalDate getLocalDate(String date) {
        final String DATE_FORMAT = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        try {
            Date input = sdf.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(input);
            return org.threeten.bp.LocalDate.of(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH));


        } catch (NullPointerException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    int getRandom(){
        final int min = 10;
        final int max = 1000;
        final int random = new Random().nextInt((max - min) + 1) + min;
        return random;
    }

    void updateAlarm(Calendar calendar,int random, DataPojo.Results commonArray){

        NumberFormat f = new DecimalFormat("00");

        String value ="";
        if (calendar.get(Calendar.HOUR_OF_DAY) == 0){
             value = "PM";
        }else {
             value =  calendar.get(Calendar.AM_PM) == Calendar.PM ? "PM" : "AM";
        }

        int hour  =  calendar.get(Calendar.HOUR) == 0 ? 12 : calendar.get(Calendar.HOUR);

        calendar.setTimeZone(TimeZone.getDefault());
        String date  = (f.format(calendar.get(Calendar.DATE)) + "/" + calendar.get(Calendar.MONTH) + 1 + "/" + calendar.get(Calendar.YEAR) + ", " +
                hour +":"+ f.format(calendar.get(Calendar.MINUTE))+" "+ value);
                JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("alarm",true);
        jsonObject.addProperty("alarmDate", date);
        jsonObject.addProperty("alarmInt",random);


//        Log.i("TAG", "Get Times" + calendar.get(Calendar.HOUR_OF_DAY)  + minuteArray.get(selectedMinute) + " " + hoursArray.get(selectedHour)
//                + "   " + minuteArray.get(selectedPm) + "  " + calendar.getTimeInMillis() + " " );

        updatePresenter.update(results.getOrderNo(),jsonObject,0);
        List<String> dates = Arrays.asList(commonArray.getContactFullAddress().split(","));
        localData.addItems(commonArray.getContactName(),date,commonArray.getOrderNo() ,
                String.valueOf(random),"true",dates.get(0)+","+dates.get(1),commonArray.getContactPhones());
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
                    View centerView = snapHelper.findSnapView(itemDecorate.getLayoutManager());
                    selectedPm = Objects.requireNonNull(itemDecorate.getLayoutManager()).getPosition(centerView) - 1;
                    Log.i("TAG", "Get Times"+selectedPm);
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
          //  Log.i("TAG","Selected Pms"+ i);
        }

       // selectedPm = list.get(1);

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

            timerChange.setText(hoursArray.get(position % hoursArray.size()));
            timerChange.setCursorVisible(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    timerChange.setFocusable(true);
                    timerChange.requestFocus();
                    timerChange.setSelectAllOnFocus(true);
                    shoeKeyboard(timerChange);
                    shoeKeyboard(timerChange);
                    setDone(timerChange);
                    setDone(minuteChange);
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
            }, 400);
        } else {
            minuteChange.setText(minuteArray.get(position % minuteArray.size()));
            setDone(minuteChange);
            setDone(timerChange);
            new Handler().postDelayed(() -> {
                minuteChange.setFocusable(true);
                minuteChange.requestFocus();
                minuteChange.setSelectAllOnFocus(true);
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
            }, 400);
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

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMessage(String message) {

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success(int pos) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }


}
