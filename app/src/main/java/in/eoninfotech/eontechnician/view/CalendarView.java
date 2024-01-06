package in.eoninfotech.eontechnician.view;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;

import in.eoninfotech.eontechnician.R;

/**
 * Created by root on 20/9/16.
 */
public class CalendarView extends LinearLayout {
    private static final String LOGTAG = "Calendar View";
    ArrayList<Date> m_events;
    ArrayList<String> m_kms;
    // how many days to show, defaults to six weeks, 42 days
    private static final int DAYS_COUNT = 42;

    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";

    // date format
    private String dateFormat;

    // current displayed month
    private Calendar currentDate = Calendar.getInstance();

    //event handling
    private EventHandler eventHandler = null;

    // internal components
    private LinearLayout header;
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDate;
    private GridView grid;

    // seasons' rainbow
    String today_c = "#4588da", greyed_out = "#c7c7c7", summer = "44eebd82";
    String rainbow[] = {"#44eebd82", "#44d8d27e", "#44a1c1da", "#448da64b"};

    // month-season association (northern hemisphere, sorry australia :)
    int[] monthSeason = new int[]{2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    /**
     * Load control xml layout
     */
    private void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_calendar, this);

        loadDateFormat(attrs);
        assignUiElements();
        assignClickHandlers();

        updateCalendar();
    }

    private void loadDateFormat(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        try {
            // try to load provided date format, and fallback to default otherwise
            dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
            if (dateFormat == null)
                dateFormat = DATE_FORMAT;
        } finally {
            ta.recycle();
        }
    }

    private void assignUiElements() {
        // layout is inflated, assign local variables to components
        header = (LinearLayout) findViewById(R.id.calendar_header);
        btnPrev = (ImageView) findViewById(R.id.calendar_prev_button);
        btnNext = (ImageView) findViewById(R.id.calendar_next_button);
        txtDate = (TextView) findViewById(R.id.calendar_date_display);
        grid = (GridView) findViewById(R.id.calendar_grid);
    }

    private void assignClickHandlers() {
        // add one month and refresh UI
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1);
                updateCalendar();
            }
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });

        // long-pressing a day
        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> view, View cell, int position, long id) {
                // handle long-press
                if (eventHandler == null)
                    return false;


                eventHandler.onDayLongPress((Date) view.getItemAtPosition(position));
                //eventHandler.onDayLongPress((Date) view.getItemAtPosition(position), (String) m_kms.get(position));
                return true;
            }
        });
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar() {
        updateCalendar(m_events, m_kms);
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar(ArrayList<Date> events, ArrayList<String> kms) {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) currentDate.clone();
        m_events = events;
        m_kms = kms;
        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // update grid
        grid.setAdapter(new CalendarAdapter(getContext(), cells, events, kms));

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        txtDate.setText(sdf.format(currentDate.getTime()));

        // set header color according to current season
        int month = currentDate.get(Calendar.MONTH);
        int season = monthSeason[month];
        String color = rainbow[season];

        header.setBackgroundColor(Color.parseColor(color));
    }


    private class CalendarAdapter extends ArrayAdapter<Date> {
        // days with events
        private ArrayList<Date> eventDays;
        ArrayList<String> kms = new ArrayList<>();
        // for view inflation
        private LayoutInflater inflater;

        public CalendarAdapter(Context context, ArrayList<Date> days, ArrayList<Date> eventDays, ArrayList<String> s_kms) {
            super(context, R.layout.calendar_item, days);
            this.eventDays = eventDays;
            kms = s_kms;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            // day in question
            Date date = getItem(position);
            Calendar cal = toCalendar(date);
            //int day = date.getDate();
            //int month = date.getMonth();
            //int year = date.getYear();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);

            // today
            Date today = new Date();
            Calendar cal_today = toCalendar(today);
            // inflate item if it does not exist yet
            if (view == null)
                view = inflater.inflate(R.layout.calendar_item, parent, false);
            ImageView dateicn = (ImageView) view.findViewById(R.id.date_icon);
            TextView txdate = (TextView) view.findViewById(R.id.date);
            TextView kms_txt = (TextView) view.findViewById(R.id.txt_kms);
            // if this day has an event, specify event image
            view.setBackgroundResource(0);
            txdate.setTypeface(null, Typeface.NORMAL);
            txdate.setTextColor(Color.parseColor(greyed_out));
            if (eventDays != null) {
                for (Date eventDate : eventDays) {
                    Calendar cal_event = toCalendar(eventDate);
                    if (cal_event.get(Calendar.DAY_OF_MONTH) == day &&
                            cal_event.get(Calendar.MONTH) == month &&
                            cal_event.get(Calendar.YEAR) == year) {
                        for (int i = 0; i < eventDays.size(); i++) {
                            // Log.i("****", String.valueOf(eventDays.get(i))+ " - " + String.valueOf(eventDate));
                            if (eventDays.get(i).equals(eventDate)) {
                                kms_txt.setText(kms.get(i));
                                kms_txt.setTextColor(Color.parseColor("#007bd9"));
                                txdate.setTypeface(null, Typeface.NORMAL);
                                txdate.setTextColor(Color.BLACK);
                            }

                        }
                        // mark this day for event
                        //  view.setBackgroundResource(R.drawable.reminder);

                        //dateicn.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }

            // clear styling
           /* if (month != cal_today.get(Calendar.MONTH) || year != cal_today.get(Calendar.YEAR)) {
                // if this day is outside current month, grey it out
                txdate.setTextColor(Color.parseColor(greyed_out));
            } else if (day == cal_today.get(Calendar.DAY_OF_MONTH)) {
                // if it is today, set it to blue/bold
                txdate.setTypeface(null, Typeface.BOLD);
                txdate.setTextColor(Color.parseColor(today_c));
            }*/

            // set text
            txdate.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
            return view;
        }
    }

    /**
     * Assign event handler to be passed needed events
     */
    public void setEventHandler(EventHandler eventHandler) {

        this.eventHandler = eventHandler;
    }

    public interface EventHandler {
        void onDayLongPress(Date date);
        //void onDayLongPress(Date date, String kms);
    }

    public static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}