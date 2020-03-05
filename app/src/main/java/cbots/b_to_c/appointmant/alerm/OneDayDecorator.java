package cbots.b_to_c.appointmant.alerm;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import org.threeten.bp.LocalDate;

class OneDayDecorator implements DayViewDecorator {

    private CalendarDay date;


    public OneDayDecorator() {
        date = CalendarDay.today();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {


    }


    public void setDate(LocalDate date) {
        this.date = CalendarDay.from(date);
    }
}
