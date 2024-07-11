package in.eoninfotech.eontechnician.activity;

import android.app.Activity;
import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.Calendar;

import androidx.core.content.ContextCompat;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.responses.AttendanceResponse;
import retrofit2.Callback;


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
