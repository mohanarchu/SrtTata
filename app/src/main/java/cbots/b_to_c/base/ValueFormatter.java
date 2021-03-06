package cbots.b_to_c.base;

import com.github.mikephil.charting.components.AxisBase;

import java.text.DecimalFormat;

public class ValueFormatter extends com.github.mikephil.charting.formatter.ValueFormatter {

    private DecimalFormat mFormat;

    public ValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0");
    }

    @Override
    public String getFormattedValue(float value) {
        return   "" + ((int) value);
    }



    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value);
    }
}
