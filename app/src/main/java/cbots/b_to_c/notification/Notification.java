package cbots.b_to_c.notification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import cbots.b_to_c.appointmant.alerm.Alerm;
import cbots.b_to_c.appointmant.alerm.Notification2;
import cbots.b_to_c.base.FragmentBase;
import cbots.b_to_c.calendar.HorizontalCalendar;
import cbots.b_to_c.calendar.HorizontalCalendarView;
import cbots.b_to_c.calendar.utils.HorizontalCalendarListener;
import cbots.b_to_c.config.Checkers;
import cbots.b_to_c.decorations.fancy.Animation;
import cbots.b_to_c.decorations.fancy.FancyAlertDialog;
import cbots.b_to_c.decorations.fancy.FancyAlertDialogListener;
import cbots.b_to_c.decorations.fancy.Icon;
import cbots.b_to_c.details.DetailsView;
import cbots.b_to_c.details.SharedArray;
import cbots.b_to_c.details.UpdateModel;
import cbots.b_to_c.details.UpdatePresenter;
import cbots.b_to_c.holder.A;
import cbots.b_to_c.holder.CancelClicked;
import cbots.b_to_c.holder.HolderClicked;
import cbots.b_to_c.home.DataModel;
import cbots.b_to_c.home.DataPojo;
import cbots.b_to_c.home.DataPresenter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import cbots.b_to_c.R;
import butterknife.BindView;

import static android.content.Context.ALARM_SERVICE;

public class Notification extends FragmentBase implements DataModel, HolderClicked , CancelClicked, UpdateModel {
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
    private Calendar calendar;
    private A adapter;
    private Calendar startDate;
    private DataPresenter dataPresenter;
    Calendar filterCalander;
    UpdatePresenter updatePresenter;
    private List<DataPojo.Results> list = new ArrayList<>();
    private HorizontalCalendar horizontalCalendar;
    boolean value = false,value1 = false;
    ArrayList<ArrayList<DateIntArray>> dateArrays = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onViewBound(View view) {

        updatePresenter = new UpdatePresenter(getActivity(),this);
        initiateCalendar(view);

        dataPresenter = new DataPresenter(getActivity(),this);
        LinearLayoutManager centerZoomLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        notoficationRecycler.setLayoutManager(centerZoomLayoutManager);
        notoficationRecycler.setAdapter(adapter);
        if (SharedArray.getResult() != null){
            list = SharedArray.getResult();
            Calendar calendars = Calendar.getInstance();
            dateFilter(calendars);
            filterCalander = calendars;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            dataPresenter.getDetails(Checkers.getUserToken(Objects.requireNonNull(getActivity())) , Checkers.getName(getActivity()),Checkers.getRole(getActivity()), new JsonObject());
        }
    }
    @SuppressLint("NewApi")
    List<DataPojo.Results> getList(){
        List<DataPojo.Results> list = SharedArray.getResult();
        list = list.stream().filter(pulse -> pulse.getPendingDocsCount() == null || !pulse.getPendingDocsCount().equals("0")).collect(Collectors.toList());
        return list;
    }
    @SuppressLint("NewApi")
    private void dateFilter(Calendar calendar){
       dateArrays.clear();
       List<DataPojo.Results> result = new ArrayList<DataPojo.Results>();
       NumberFormat f = new DecimalFormat("00");
        for (DataPojo.Results person : getList()) {

            if ( person.getAlarms() != null ){
                for (DataPojo.Alarms alarms : person.getAlarms()){

                    List<String> elephantList = Arrays.asList(alarms.getAlarmDate().split(","));
                    if (elephantList.get(0).equals(f.format(calendar.get(Calendar.DATE)) + "/" +f.format(calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR))) {
                        result.add(person);
                    }
                }
            }
        }
        adapter   = new A(getActivity(), false, result,false,false, this, this);
        notoficationRecycler.setAdapter(adapter);

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            value = false;
            value1 = false;
            dateFilter(filterCalander);
        }
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_notification;
    }

    private void initiateCalendar(View view) {
        startDate = Calendar.getInstance();
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
        horizontalCalendar.getCalendarView().setHasFixedSize(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                horizontalCalendar.moveToNextMonth(calendar,true);
            }
        },200);
        horizontalCalendar.getCalendarView().getAdapter().notifyDataSetChanged();
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
                dateFilter(date);
                filterCalander = date;

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
        dateFilter(filterCalander);

    }

    @Override
    public void success() {

    }

    @Override
    public void success(int position) {
        showMessage("Alarm canceled");
        dataPresenter.getDetails(Checkers.getUserToken(Objects.requireNonNull(getActivity())) ,
                Checkers.getName(getActivity()),String.valueOf(Checkers.getRoleId(getActivity())), new JsonObject());

//        dateFilter(filterCalander);
//        adapter.notifyItemRemoved(position);
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void clicked(int position, boolean type, List<DataPojo.Results> list,boolean alarm) {
        SharedArray.setFilterResult(list);
        if (alarm) {
            Intent intent = new Intent(getActivity(), Alerm.class);
            intent.putExtra("position",  position);
            startActivityForResult(intent, 101);
        } else {
            Intent intent = new Intent(getActivity(), DetailsView.class);
            intent.putExtra("transitionName", "");
            intent.putExtra("position", position);
            intent.putExtra("type", type);
            startActivityForResult(intent, 101);
        }

    }


    @Override
    public void cancel(int position, List<DataPojo.Results> list ,  String times ) {

            new FancyAlertDialog.Builder(getActivity())
                    .setTitle("Do you really want to cancel the alarm ?")
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
                            NumberFormat f = new DecimalFormat("00");
                            if (list.get(position).getAlarms() != null) {
                                for (DataPojo.Alarms alarms : list.get(position).getAlarms()) {
                                    List<String> timeSplit = Arrays.asList(alarms.getAlarmDate().split(","));
                                    String  time = timeSplit.get(1);
                                    Calendar calendar =  filterCalander;
                                    //calendar.add(Calendar.MONTH,1);
                                    //f.format(calendar.get(Calendar.DATE))+"/"+ f.format(calendar.get(Calendar.MONTH)+1)+"/"+
                                    //                                            calendar.get(Calendar.YEAR))
                                    if (time.equals(times)) {
                                        Intent intent = new Intent(getActivity(), Notification2.class);
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),
                                                Integer.valueOf(alarms.getAlarmInt()), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        AlarmManager alarmManager = (AlarmManager)getActivity(). getSystemService(ALARM_SERVICE);
                                        alarmManager.cancel(pendingIntent);
                                        updatePresenter.cancelAlarm(list.get(position).getOrderNo(),Integer.valueOf(alarms.getAlarmInt()),0);
                                    }
                                }
                            }
                        }
                    })
                    .OnNegativeClicked(() -> {

                    })
                    .build();
    }
}
