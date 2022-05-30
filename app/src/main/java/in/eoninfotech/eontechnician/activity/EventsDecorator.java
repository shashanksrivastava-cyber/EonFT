package in.eoninfotech.eontechnician.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.Calendar;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.AttendanceResponse;
import retrofit2.Callback;

import static java.security.AccessController.getContext;


/**
 * Created by root on 10/4/19.
 */

public class EventsDecorator implements DayViewDecorator {

    Activity context;
    public EventsDecorator(Activity context) {
        this.context = context;
    }

    public EventsDecorator(Callback<AttendanceResponse> callback) {
    }
    public EventsDecorator(OnMonthChangedListener onMonthChangedListener) {

    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {

        return day.getCalendar().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }
    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.WHITE));
        view.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.grey_circle));
    }
}
