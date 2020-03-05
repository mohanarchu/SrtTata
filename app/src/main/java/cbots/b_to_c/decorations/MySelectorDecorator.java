package cbots.b_to_c.decorations;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;


import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import org.threeten.bp.LocalDate;

import cbots.b_to_c.R;

public class MySelectorDecorator implements DayViewDecorator {

  private final Drawable drawable;
  CalendarDay calendarDay;

  public MySelectorDecorator(Activity context, CalendarDay day) {
    drawable = context.getResources().getDrawable(R.drawable.date_selecter);
    calendarDay = day;

  }
  @Override
  public boolean shouldDecorate(CalendarDay day) {
    return day.equals(calendarDay);
  }
  @Override
  public void decorate(DayViewFacade view) {
    view.addSpan(new ForegroundColorSpan(Color.rgb(255, 255, 255)));
    view.addSpan(new StyleSpan(Typeface.NORMAL));
    view.addSpan(new RelativeSizeSpan(1.1f));
    view.setSelectionDrawable(drawable);
  }
  public void setDate(LocalDate date) {
    this.calendarDay = CalendarDay.from(date);
  }

}
