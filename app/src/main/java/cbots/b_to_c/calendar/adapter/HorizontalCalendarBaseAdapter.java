package cbots.b_to_c.calendar.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cbots.b_to_c.calendar.HorizontalCalendar;
import cbots.b_to_c.calendar.HorizontalLayoutManager;
import cbots.b_to_c.calendar.model.CalendarEvent;
import cbots.b_to_c.calendar.model.CalendarItemStyle;
import cbots.b_to_c.calendar.utils.CalendarEventsPredicate;
import cbots.b_to_c.calendar.utils.HorizontalCalendarListener;
import cbots.b_to_c.calendar.utils.HorizontalCalendarPredicate;
import cbots.b_to_c.calendar.utils.Utils;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import cbots.b_to_c.R;


public abstract class HorizontalCalendarBaseAdapter<VH extends DateViewHolder, T extends Calendar> extends RecyclerView.Adapter<VH> {

    private final int itemResId;
    final HorizontalCalendar horizontalCalendar;
    private final HorizontalCalendarPredicate disablePredicate;
    private final CalendarEventsPredicate eventsPredicate;
    private final int cellWidth;
    private CalendarItemStyle disabledItemStyle;
    protected Calendar startDate;
    protected int itemsCount;
    Drawable drawable;
    protected HorizontalCalendarBaseAdapter(int itemResId, final HorizontalCalendar horizontalCalendar, Calendar startDate, Calendar endDate,
                                            HorizontalCalendarPredicate disablePredicate, CalendarEventsPredicate eventsPredicate) {
        this.itemResId = itemResId;
        this.horizontalCalendar = horizontalCalendar;
        this.disablePredicate = disablePredicate;
        this.startDate = startDate;
        if (disablePredicate != null) {
            this.disabledItemStyle = disablePredicate.style();
        }
        this.eventsPredicate = eventsPredicate;

        cellWidth = Utils.calculateCellWidth(horizontalCalendar.getContext(), horizontalCalendar.getNumberOfDatesOnScreen());
        itemsCount = calculateItemsCount(startDate, endDate);
    }

    @SuppressLint("NewApi")
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(itemResId, parent, false);

        final VH viewHolder = createViewHolder(itemView, cellWidth);
        viewHolder.itemView.setOnClickListener(new MyOnClickListener(viewHolder));
        viewHolder.itemView.setOnLongClickListener(new MyOnLongClickListener(viewHolder));

        if (eventsPredicate != null) {
            initEventsRecyclerView(viewHolder.eventsRecyclerView);
        } else {
            viewHolder.eventsRecyclerView.setVisibility(View.GONE);
        }
        drawable = parent.getContext().getDrawable(R.drawable.selector);
        return viewHolder;
    }

    private void initEventsRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new EventsAdapter(Collections.<CalendarEvent>emptyList()));
        GridLayoutManager layoutManager = new GridLayoutManager(recyclerView.getContext(), 4);
        recyclerView.setLayoutManager(layoutManager);
    }

    protected abstract VH createViewHolder(View itemView, int cellWidth);

    public abstract T getItem(int position);

    @Override
    public int getItemCount() {
        return itemsCount;
    }

    public boolean isDisabled(int position) {
        if (disablePredicate == null) {
            return false;
        }
        Calendar date = getItem(position);
        return disablePredicate.test(date);
    }

    void showEvents(VH viewHolder, Calendar date) {
        if (eventsPredicate == null) {
            return;
        }

        List<CalendarEvent> events = eventsPredicate.events(date);
        if ((events == null) || events.isEmpty()) {
            viewHolder.eventsRecyclerView.setVisibility(View.GONE);
        } else {
            viewHolder.eventsRecyclerView.setVisibility(View.VISIBLE);
            EventsAdapter eventsAdapter = (EventsAdapter) viewHolder.eventsRecyclerView.getAdapter();
            eventsAdapter.update(events);
        }
    }

    void applyStyle(VH viewHolder, Calendar date, int position) {
        int selectedItemPosition = horizontalCalendar.getSelectedDatePosition();

        if (disablePredicate != null) {
            boolean isDisabled = disablePredicate.test(date);
            viewHolder.itemView.setEnabled(!isDisabled);
            if (isDisabled && (disabledItemStyle != null)) {
                applyStyle(viewHolder, disabledItemStyle);
                viewHolder.selectionView.setVisibility(View.INVISIBLE);
                return;
            }
        }
        // Selected Day
        if (position == selectedItemPosition) {
           applyStyle(viewHolder, horizontalCalendar.getSelectedItemStyle());
           viewHolder.selectionView.setVisibility(View.INVISIBLE);
           viewHolder.textMiddle.setTextSize(14f);
           viewHolder.textTop.setTextSize(14f);
           viewHolder.textMiddle.setBackground(drawable);
           viewHolder.textMiddle.setTextColor(Color.parseColor("#000000"));
           LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        }
        // Unselected Days
        else {
            applyStyle(viewHolder, horizontalCalendar.getDefaultStyle());
            viewHolder.selectionView.setVisibility(View.INVISIBLE);
            viewHolder.textMiddle.setTextSize(14f);
            viewHolder.textTop.setTextSize(14f);
            viewHolder.textMiddle.setBackground(null);
            viewHolder.textMiddle.setTextColor(Color.WHITE);

        }
    }
    private void applyStyle(VH viewHolder, CalendarItemStyle itemStyle) {
        viewHolder.textTop.setTextColor(itemStyle.getColorTopText());
        viewHolder.textMiddle.setTextColor(itemStyle.getColorMiddleText());
        viewHolder.textMiddle.setTextSize(12f);
        viewHolder.textTop.setTextSize(12f);
        viewHolder.textBottom.setTextColor(itemStyle.getColorBottomText());
        if (Build.VERSION.SDK_INT >= 16) {
            viewHolder.itemView.setBackground(itemStyle.getBackground());
        } else {
            viewHolder.itemView.setBackgroundDrawable(itemStyle.getBackground());
        }
    }

    public void update(Calendar startDate, Calendar endDate, boolean notify) {
        this.startDate = startDate;
        itemsCount = calculateItemsCount(startDate, endDate);
        if (notify) {
            notifyDataSetChanged();
        }
    }

    protected abstract int calculateItemsCount(Calendar startDate, Calendar endDate);

    private class MyOnClickListener implements View.OnClickListener {
        private final RecyclerView.ViewHolder viewHolder;

        MyOnClickListener(RecyclerView.ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }

        @Override
        public void onClick(View v) {

           // Log.i("TAG","Item Clicked"+ viewHolder.getAdapterPosition());
            int position = viewHolder.getAdapterPosition();
            if (position == 3 )
            horizontalCalendar.getCalendarListener().onDateSelected(horizontalCalendar.getDateAt(position),position);
            if (position == -1)
                return;
            horizontalCalendar.getCalendarView().setSmoothScrollSpeed(HorizontalLayoutManager.SPEED_SLOW);
            horizontalCalendar.centerCalendarToPosition(position);

        }
    }

    private class MyOnLongClickListener implements View.OnLongClickListener {
        private final RecyclerView.ViewHolder viewHolder;

        MyOnLongClickListener(RecyclerView.ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }

        @Override
        public boolean onLongClick(View v) {
            HorizontalCalendarListener calendarListener = horizontalCalendar.getCalendarListener();
            if (calendarListener == null) {
                return false;
            }

            int position = viewHolder.getAdapterPosition();
            Calendar date = getItem(position);


            return calendarListener.onDateLongClicked(date, position);
        }
    }
}
