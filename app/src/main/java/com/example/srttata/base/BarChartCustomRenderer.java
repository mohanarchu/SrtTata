package com.example.srttata.base;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

public class BarChartCustomRenderer extends BarChartRenderer {

    private Paint myPaint;
    private ArrayList<Integer> myColors;
    public BarChartCustomRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler, ArrayList<Integer> myColors) {
        super(chart, animator, viewPortHandler);
        this.myPaint = new Paint();
        this.myColors = myColors;
    }

    @Override
    public void drawValues(Canvas c) {
        super.drawValues(c);
        // you can modify the original method
        // so that everything is drawn on the canvas inside a single loop
        // also you can add logic here to meet your requirements
        int colorIndex = 0;
        for (int i = 0; i < mChart.getBarData().getDataSetCount(); i++) {
            BarBuffer buffer = mBarBuffers[i];
            float left, right, top, bottom;
            for (int j = 0; j < buffer.buffer.length * mAnimator.getPhaseX(); j += 4) {
                myPaint.setColor(myColors.get(colorIndex++));
                left = buffer.buffer[j];
                right = buffer.buffer[j + 2];
                top = buffer.buffer[j + 1];
                bottom = buffer.buffer[j + 3];
//                myPaint.setShader(new LinearGradient(left,top,right,bottom, Color.CYAN, myColors.get(colorIndex++), Shader.TileMode.MIRROR ));
                c.drawRect(left, top, right, top+5f, myPaint);
            }
        }
    }

    @Override
    public void drawValue(Canvas c, String valueText, float x, float y, int color) {
        super.drawValue(c, valueText, x, y, color);
    }

    public void drawValue(Canvas c, IValueFormatter formatter, float value, Entry entry, int dataSetIndex, float x, float y, int color) {
        String text = formatter.getFormattedValue(value, entry, dataSetIndex, mViewPortHandler);
        String[] splitText;
        if(text.contains(",")){
            splitText = text.split(",");
            Paint paintStyleOne = new Paint(mValuePaint);
            Paint paintStyleTwo = new Paint(mValuePaint);
            paintStyleOne.setColor(Color.BLACK);
            paintStyleTwo.setColor(Color.BLUE);
            c.drawText(splitText[0], x, y-20f, paintStyleOne);
            c.drawText(splitText[1], x, y, paintStyleTwo);
        }
        //else{
//            super.drawValue(c, formatter, value, entry, dataSetIndex, x, y, color);
        //}
    }
}