package com.example.srttata.home;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Visibility;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.srttata.LoginScreen;
import com.example.srttata.MainActivity;
import com.example.srttata.R;
import com.example.srttata.appointmant.alerm.Alerm;
import com.example.srttata.base.BarChartCustomRenderer;
import com.example.srttata.base.FragmentBase;
import com.example.srttata.base.TooltipWindow;
import com.example.srttata.base.ValueFormatter;
import com.example.srttata.config.Checkers;
import com.example.srttata.details.DetailsView;
import com.example.srttata.details.SharedArray;
import com.example.srttata.expand.VisibleData;
import com.example.srttata.holder.A;
import com.example.srttata.holder.HolderClicked;
import com.example.srttata.search.SearchActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.skydoves.progressview.ProgressView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home_Frag extends FragmentBase implements DataModel, OnChartValueSelectedListener, HolderClicked {


    private DataPresenter dataPresenter;
    @BindView(R.id.activity_recycler)
    RecyclerView activityRecycler;


    @BindView(R.id.barChart)
    BarChart mChart;
    @BindView(R.id.searchActivities)
    ImageView referesh;
    boolean[] visibled;
    private A adapter;
    private List<DataPojo.Results> list;
    ArrayList<VisibleData> visibleData;
    private ChartScreen chartScreen;
    private ArrayList<BarEntry> valueSet1 = new ArrayList<>();
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    boolean value = true,value1 = false;
    @Override
    protected void onViewBound(View view) {

        LinearLayoutManager centerZoomLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        activityRecycler.setLayoutManager(centerZoomLayoutManager);

        chartScreen   = new ChartScreen(getActivity());
        mChart.setOnChartValueSelectedListener(this);
        mChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        referesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Checkers.isNetworkConnectionAvailable(getActivity()))
                    getResult();
                else
                    showMessage("Check your internet connection");
            }
        });

        if (Checkers.isNetworkConnectionAvailable(getActivity()))
            getResult();
        else
            showMessage("Check your internet connection");
    }

    @SuppressLint("NewApi")
    private void showResult(){

        adapter   = new A(getActivity(), true, getList(),false,false,this,null);
        activityRecycler.setAdapter(adapter);
    }
    @SuppressLint("NewApi")
    List<DataPojo.Results> getList(){
        List<DataPojo.Results> list = SharedArray.getResult();
        list = list.stream().filter(pulse ->  pulse.getPendingDocsCount() == null  || !pulse.getPendingDocsCount().equals("0")).collect(Collectors.toList());
        return list;
    }


    protected int layoutRes() {
        return R.layout.fragment_home_;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isHidden()){

            showResult();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
                dataPresenter.getDetails(Checkers.getUserToken(Objects.requireNonNull(getActivity())));
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Handler handler = new Handler();
        Runnable timedTask = new Runnable(){

            @Override
            public void run() {
                getResult();

            }};
        handler.postDelayed(timedTask,1800000000);
    }
    private void getResult(){
        dataPresenter = new DataPresenter(getActivity(),this);
        dataPresenter.getDetails(Checkers.getUserToken(Objects.requireNonNull(getActivity())));
    }



    @SuppressLint("NewApi")
    @Override
    public void showDatas(DataPojo.Results[] results,DataPojo.Count[] counts,int total,int alarmCount) {
       // list.clear();
        valueSet1.clear();
        list = new ArrayList<DataPojo.Results>(Arrays.asList(results));
        List<DataPojo.Count>  list = new ArrayList<>(Arrays.asList(counts));
        valueSet1.add(new BarEntry(0,0));
        valueSet1.add(new BarEntry(1,0));
        valueSet1.add(new BarEntry(2,0));
        valueSet1.add(new BarEntry(3,0));
        for (DataPojo.Count lists : list){
           if (lists.get_id().getAgeing().equals(">1week") && lists.get_id().getAgeing() != null )
               valueSet1.set(2, new BarEntry(2,Integer.valueOf(lists.getCount())));
//               valueSet1.add(2, new BarEntry(2,Integer.valueOf(lists.getCount())));
           else if (lists.get_id().getAgeing().equals("new") && lists.get_id().getAgeing() != null  )
               valueSet1.set(0, new BarEntry(0,Integer.valueOf(lists.getCount())));
           //    valueSet1.add(0, new BarEntry(0,Integer.valueOf(lists.getCount())));
           else if ( lists.get_id().getAgeing().equals(">3days") &&  lists.get_id().getAgeing() != null )
               valueSet1.set(1, new BarEntry(1,Integer.valueOf(lists.getCount())));
               //   valueSet1.add(new BarEntry(1,Integer.valueOf(lists.getCount())));
           else if (lists.get_id().getAgeing().equals(">2weeks") &&  lists.get_id().getAgeing() != null )
               valueSet1.set(3, new BarEntry(3,Integer.valueOf(lists.getCount())));
              // valueSet1.add(new BarEntry(3,Integer.valueOf(lists.getCount())));
        }
        chartScreen.setCharts(mChart,valueSet1);
        mChart.invalidate();
        showResult();

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
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if ( e.getX() == 0 && e.getY() != 0 ){
            adapter.getFilter().filter("new");
        } else if ( e.getX() == 1 && e.getY() != 0 ){
            adapter.getFilter().filter(">3days");
        }else if (e.getX() == 2 && e.getY() != 0 ){
            adapter.getFilter().filter(">1week");
        }else if (e.getX() == 3 && e.getY() != 0 ){
            adapter.getFilter().filter(">2weeks");
        }

    }

    @Override
    public void onNothingSelected() {
        adapter.getFilter().filter("");
    }

    @Override
    public void clicked(int position, boolean type, List<DataPojo.Results> list,boolean alarm) {
        if (alarm){
            Intent intent = new Intent(getActivity(), Alerm.class);
            intent.putExtra("position",position);
            SharedArray.setFilterResult(list);
            startActivityForResult(intent,100);
        } else {
            Intent intent = new Intent(getActivity(), DetailsView.class);
            intent.putExtra("transitionName", "");
            intent.putExtra("position", position);
            intent.putExtra("type", type);
            intent.putExtra("filter", false);
            SharedArray.setFilterResult(list);
            startActivityForResult(intent, 100);
        }
    }
}
