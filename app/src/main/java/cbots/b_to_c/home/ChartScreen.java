package cbots.b_to_c.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;

import androidx.annotation.RequiresApi;

import cbots.b_to_c.base.BarChartCustomRenderer;
import cbots.b_to_c.base.ValueFormatter;
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
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChartScreen  {


    private Context context;
    public ChartScreen(Context context){
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setCharts(BarChart mChart, ArrayList<BarEntry> valueSet1 , boolean stacked, String[] chartLabel){


        ArrayList<Integer> myColors = new ArrayList<>();
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
        Legend legend = mChart.getLegend();
        mChart.getAxisRight().setEnabled(false);
        legend.setEnabled(true);
        BarDataSet barDataSet = new BarDataSet(valueSet1, "yes");
        mChart.getXAxis().setCenterAxisLabels(true);
        xaxis.setGranularity(1f);
        xaxis.setGranularityEnabled(true);
        xaxis.setCenterAxisLabels(false);
        xaxis.setTextSize(12f);
        ArrayList xVals = new ArrayList();

        List<LegendEntry> legendEntries = new ArrayList<>();
        if (stacked){
            xaxis.setAxisMaximum(chartLabel.length);
            mChart.setVisibleXRangeMaximum(5);
            mChart.setNestedScrollingEnabled(true);
            xVals = new ArrayList<String>(Arrays.asList(chartLabel));
            mChart.setDrawValueAboveBar(true);
            myColors.add(Color.parseColor("#fbbc04"));
            myColors.add(Color.parseColor("#ea4335"));
            mChart.getLegend().setEnabled(true);

        } else {
            xaxis.setAxisMaximum(4);
            xVals.add("New");
            xVals.add("> 3 days");
            xVals.add("> 1 week");
            xVals.add("> 2 weeks");
            mChart.setDrawValueAboveBar(true);
            myColors.add(Color.parseColor("#FF0000"));
            myColors.add(Color.parseColor("#9B870C"));
            myColors.add(Color.parseColor("#008000"));

//            myColors.add(Color.parseColor("#34a853"));
//            myColors.add(Color.parseColor("#f1761f"));
//            myColors.add(Color.parseColor("#fbbc04"));
//            myColors.add(Color.parseColor("#ea4335"));

        }
        //mChart.setRenderer(new BarChartCustomRenderer(mChart, mChart.getAnimator(), mChart.getViewPortHandler(), myColors));
        xaxis.setValueFormatter(new IndexAxisValueFormatter(xVals));

       // mChart.setDrawValueAboveBar(true);
        legend.setCustom(legendEntries);
        legend.setXEntrySpace(25f);
        legend.setTextColor(Color.BLACK);
        legend.setTextSize(10f);
        mChart.getLegend().setWordWrapEnabled(true);
        mChart.setScaleEnabled(false);
        List<IBarDataSet> dataSets = new ArrayList<>();
        barDataSet.setColors(myColors);
        int colorBlack = Color.parseColor("#000000");
        barDataSet.setValueTextColor(colorBlack);
        barDataSet.setValueTextSize(12f);
        barDataSet.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        barDataSet.setValueFormatter(new ValueFormatter());
        dataSets.add(barDataSet);
        BarData data = new BarData(dataSets);
        mChart.setData(data);


    }

}
