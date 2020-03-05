package cbots.b_to_c.calendar.utils;


import cbots.b_to_c.calendar.model.CalendarEvent;

import java.util.Calendar;
import java.util.List;

/**
 * @author Mulham-Raee
 * @since v1.3.2
 */
public interface CalendarEventsPredicate {


    List<CalendarEvent> events(Calendar date);
}
