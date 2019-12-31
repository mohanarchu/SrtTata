package com.example.srttata.home;


import android.annotation.SuppressLint;
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
import com.example.srttata.base.BarChartCustomRenderer;
import com.example.srttata.base.FragmentBase;
import com.example.srttata.base.TooltipWindow;
import com.example.srttata.base.ValueFormatter;
import com.example.srttata.config.Checkers;
import com.example.srttata.details.DetailsView;
import com.example.srttata.details.SharedArray;
import com.example.srttata.expand.VisibleData;
import com.example.srttata.holder.A;
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
public class Home_Frag extends FragmentBase implements DataModel, OnChartValueSelectedListener {


    private DataPresenter dataPresenter;
    @BindView(R.id.activity_recycler)
    RecyclerView activityRecycler;


    @BindView(R.id.barChart)
    BarChart mChart;
    @BindView(R.id.searchActivities)
    ImageView searchActivities;
    boolean[] visibled;
    A adapter;
    List<DataPojo.Results> list;
    ArrayList<VisibleData> visibleData;
    ChartScreen chartScreen;
    private ArrayList<BarEntry> valueSet1 = new ArrayList<>();
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    @Override
    protected void onViewBound(View view) {

        dataPresenter = new DataPresenter(getActivity(),this);

        LinearLayoutManager centerZoomLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        activityRecycler.setLayoutManager(centerZoomLayoutManager);
        activityRecycler.setAdapter(adapter);
        chartScreen   = new ChartScreen(getActivity());
        mChart.setOnChartValueSelectedListener(this);

        dataPresenter.getDetails(Checkers.getUserToken(Objects.requireNonNull(getActivity())));

        mChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        MainActivity.commonSearch .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list != null){
                    SharedArray.setArray(list);
                    Intent intent = new Intent(getActivity(),SearchActivity.class);
                    intent.putExtra("bool",true);
                    startActivity(intent);
                }else {
                    showMessage("Details are empty");
                }
            }
        });

        Log.i("TAG","My counts"+ "Yes");
    }




    protected int layoutRes() {
        return R.layout.fragment_home_;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }
    public static void countFrequencies(ArrayList<String> list)
    {
        // hashmap to store the frequency of element
        Map<String, Integer> hm = new HashMap<String, Integer>();

        for (String i : list) {
            Integer j = hm.get(i);
            hm.put(i, (j == null) ? 1 : j + 1);
        }

        // displaying the occurrence of elements in the arraylist
        for (Map.Entry<String, Integer> val : hm.entrySet()) {
            System.out.println("Element " + val.getKey() + " "
                    + "occurs"
                    + ": " + val.getValue() + " times");
        }
    }

    @Override
    protected void backClicked() {
        super.backClicked();
    }

    @Override
    public void showDatas(DataPojo.Results[] results,DataPojo.Count[] counts,int total,int alarmCount) {
        list = new ArrayList<DataPojo.Results>(Arrays.asList(results));
        SharedArray.setArray(list);
        adapter   = new A(getActivity(), true, list,false,false);
        activityRecycler.setAdapter(adapter);
        ArrayList<DataPojo.Count>  list = new ArrayList<>(Arrays.asList(counts));
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
        MainActivity.addFirstView(results.length,getActivity());
        MainActivity.addSecondView(total,getActivity());
        MainActivity.addThirdView(alarmCount,getActivity());
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
        if ( e.getX() == 0){
            adapter.getFilter().filter("new");
        } else if (e.getX() == 1){
            adapter.getFilter().filter(">3days");
        }else if (e.getX() == 2){
            adapter.getFilter().filter(">1week");
        }else if (e.getX() == 3){
            adapter.getFilter().filter(">2weeks");
        }
    }

    @Override
    public void onNothingSelected() {
        adapter.getFilter().filter("");
    }


    public class SimpleRVAdapter extends RecyclerView.Adapter<My> {
        private String[] dataSource;
        public SimpleRVAdapter(String[] dataArgs){
            dataSource = dataArgs;
        }

        @Override
        public My onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view

            return new My(LayoutInflater.from(getContext()).inflate(R.layout.text_view, parent, false));
        }

        public  class SimpleViewHolder extends RecyclerView.ViewHolder{
            public TextView textView;
            public SimpleViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }
        }

        @Override
        public void onBindViewHolder(My holder, int position) {
            holder.date.setText(dataSource[position]);

        }

        @Override
        public int getItemCount() {
            return dataSource.length;
        }
    }
    class My extends RecyclerView.ViewHolder {
        @BindView(R.id.vocab)
        TextView date;

        My(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
