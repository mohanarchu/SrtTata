package com.example.srttata.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.example.srttata.base.BarChartCustomRenderer;
import com.example.srttata.base.ValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

public class ChartScreen  {


    private Context context;
    public ChartScreen(Context context){
        this.context = context;
    }

    public void setCharts(BarChart mChart,ArrayList<BarEntry> valueSet1){


        ArrayList<Integer> myColors = new ArrayList<>();
        myColors.add(Color.parseColor("#34a853"));
        myColors.add(Color.parseColor("#f1761f"));
        myColors.add(Color.parseColor("#fbbc04"));
        myColors.add(Color.parseColor("#ea4335"));



        mChart.setDrawBarShadow(false);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawGridBackground(false);
        XAxis xaxis = mChart.getXAxis();
        xaxis.setDrawGridLines(false);
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xaxis.setDrawLabels(false);
        xaxis.setDrawAxisLine(false);
        xaxis.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        YAxis yAxisLeft = mChart.getAxisLeft();
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setEnabled(false);
        mChart.getAxisRight().setEnabled(false);

        mChart.setRenderer(new BarChartCustomRenderer(mChart, mChart.getAnimator(), mChart.getViewPortHandler(), myColors));
        mChart.setDrawValueAboveBar(true);

        Legend legend = mChart.getLegend();
        legend.setEnabled(true);

      /*  ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();


        valueSet1.add(new BarEntry(0,9));
        valueSet1.add(new BarEntry(1,4));
        valueSet1.add(new BarEntry(2,5));
        valueSet1.add(new BarEntry(3,3));*/
        ArrayList xVals = new ArrayList();
        xVals.add("New");
        xVals.add("> 3 days");
        xVals.add("> 1 week");
        xVals.add("> 2 weeks");
        mChart.getXAxis().setCenterAxisLabels(true);
        xaxis.setGranularity(1f);
        xaxis.setGranularityEnabled(true);
        xaxis.setCenterAxisLabels(false);
        xaxis.setAxisMaximum(4);
        xaxis.setTextSize(12f);
        xaxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
        List<LegendEntry> legendEntries = new ArrayList<>();
//        legendEntries.add(new LegendEntry("New",Legend.LegendForm.CIRCLE,10f,5f,
//                null,Color.parseColor("#34a853")));
//        legendEntries.add(new LegendEntry("> 3 days",Legend.LegendForm.CIRCLE,10f,5f,
//                null,Color.parseColor("#f1761f")));
//        legendEntries.add(new LegendEntry("> 1 week",Legend.LegendForm.CIRCLE,10f, 5f,
//                null,Color.parseColor("#f1761f")));
//        legendEntries.add(new LegendEntry("> 2 week",Legend.LegendForm.CIRCLE,10f,5f,
//                null,Color.parseColor("#ea4335")));
        legend.setCustom(legendEntries);
        legend.setXEntrySpace(25f);
        legend.setTextColor(Color.BLACK);
        legend.setTextSize(10f);
        mChart.getLegend().setWordWrapEnabled(true);
        mChart.setScaleEnabled(false);


 /*
         for (int i = 0; i < 4; ++i) {
            BarEntry entry = new BarEntry(i, (i + 1) * 10);
            valueSet1.add(entry);
        }
*/

        List<IBarDataSet> dataSets = new ArrayList<>();

        BarDataSet barDataSet = new BarDataSet(valueSet1, "yes");
        int colorBlack = Color.parseColor("#000000");
        barDataSet.setColors(myColors);
        barDataSet.setValueTextColor(colorBlack);
        barDataSet.setValueTextSize(14f);
        barDataSet.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));


        barDataSet.setValueFormatter(new ValueFormatter());

        dataSets.add(barDataSet);

        BarData data = new BarData(dataSets);


        mChart.setData(data);
    }
}
