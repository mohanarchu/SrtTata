package cbots.b_to_c.calendar.utils;


import cbots.b_to_c.calendar.HorizontalCalendarView;

import java.util.Calendar;


/**
 * @author Mulham-Raee
 * @since v1.0.0
 */
public abstract class HorizontalCalendarListener {

    public abstract void onDateSelected(Calendar date, int position);

    public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy) {
    }

    public boolean onDateLongClicked(Calendar date, int position) {
        return false;
    }

    public  void onMonthSelected(Calendar date, int position){

    }
}