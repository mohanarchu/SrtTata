package com.example.srttata.notification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.srttata.MainActivity;
import com.example.srttata.R;
import com.example.srttata.base.FragmentBase;
import com.example.srttata.calendar.HorizontalCalendar;
import com.example.srttata.calendar.HorizontalCalendarView;
import com.example.srttata.calendar.utils.HorizontalCalendarListener;
import com.example.srttata.config.Checkers;
import com.example.srttata.details.SharedArray;
import com.example.srttata.holder.A;
import com.example.srttata.home.ChartScreen;
import com.example.srttata.home.DataModel;
import com.example.srttata.home.DataPojo;
import com.example.srttata.home.DataPresenter;
import com.example.srttata.search.SearchActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import butterknife.BindView;
public class Notification extends FragmentBase implements DataModel {
    @BindView(R.id.previousMonth)
    ImageView previousMonth;
    @BindView(R.id.calanderMonth)
    TextView calanderMonth;
    @BindView(R.id.nextMonth)
    ImageView nextMonth;
    @BindView(R.id.myCalender)
    HorizontalCalendarView calender;
    @BindView(R.id.searchNotifi)
    ImageView searchNotifi;
    @BindView(R.id.notoficationRecycler)
    RecyclerView notoficationRecycler;
    Calendar calendar;
    private A adapter;
    List<DataPojo.Results> list = new ArrayList<>();
    private HorizontalCalendar horizontalCalendar;
    @Override
    protected void onViewBound(View view) {
        initiateCalendar(view);
        MainActivity.commonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list != null){
                    SharedArray.setArray(list);
                    Intent intent = new Intent(getActivity(),SearchActivity.class);
                    intent.putExtra("bool",false);
                    startActivity(intent);
                }
            }
        });
        DataPresenter dataPresenter = new DataPresenter(getActivity(),this);
        LinearLayoutManager centerZoomLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        notoficationRecycler.setLayoutManager(centerZoomLayoutManager);
        notoficationRecycler.setAdapter(adapter);
       // dataPresenter.getDetails(Checkers.getUserToken(Objects.requireNonNull(getActivity())));
        if (SharedArray.getResult() != null){
            list = SharedArray.getResult();
            adapter   = new A(getActivity(), false, list,false,false);
            notoficationRecycler.setAdapter(adapter);
        }
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_notification;
    }

    private void initiateCalendar(View view) {
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH,0);
        /* end after 2 months from now */
        setDates(Calendar.getInstance());
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 4);
        horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.myCalender)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .configure()
                .formatTopText("EEE")
                .formatMiddleText("dd")
                .formatBottomText("MMM")
                .textSize(12f, 14f, 14f)
                .showTopText(true)
                .showBottomText(false)
                .textColor(Color.WHITE, Color.WHITE).end()
                .build();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                horizontalCalendar.moveToNextMonth(calendar,true);

            }
        },200);

        horizontalCalendar.getCalendarView().invalidate();
        nextMonth.setOnClickListener(view1 -> {
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            Calendar calenda1 = Calendar.getInstance();
            calenda1.set(Calendar.MONTH, month + 1);
            calenda1.set(Calendar.YEAR, year);
            calenda1.set(Calendar.DATE, 1);
            horizontalCalendar.moveToNextMonth(calenda1, false);
        });
        previousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                Calendar calenda1 = Calendar.getInstance();
                calenda1.set(Calendar.MONTH, month - 1);
                calenda1.set(Calendar.DATE, 1);
                calenda1.set(Calendar.YEAR, year);
                horizontalCalendar.moveToNextMonth(calenda1, false);
            }
        });

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                setDates(date);
                //Toast.makeText(getContext(), DateFormat.format("EEE, MMM d, yyyy", date) + " is selected!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setDates(Calendar date){
        @SuppressLint("DefaultLocale") String interval = String.format(
                "%s %4d",
                String.format(Locale.US,"%tB",date),
                date.get(Calendar.YEAR)
        );
        calanderMonth.setText(interval);
        calendar = date;
    }

    @Override
    public void showDatas(DataPojo.Results[] results,DataPojo.Count[] counts,int total,int alarmCount) {
        list = new ArrayList<DataPojo.Results>(Arrays.asList(results));
        adapter   = new A(getActivity(), true, list,false,false);
        notoficationRecycler.setAdapter(adapter);
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
}
