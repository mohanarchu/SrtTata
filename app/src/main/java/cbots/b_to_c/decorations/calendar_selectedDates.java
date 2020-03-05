package cbots.b_to_c.decorations;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class calendar_selectedDates implements DayViewDecorator {

    CalendarDay date;

    public calendar_selectedDates(CalendarDay date) {
       this.date = date;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
      return  day.isBefore(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setDaysDisabled(true);
        view.addSpan(new ForegroundColorSpan(Color.LTGRAY));
    }
}