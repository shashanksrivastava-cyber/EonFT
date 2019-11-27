package in.eoninfotech.eontechnician;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.util.Calendar.*;

/**
 * Created by root on 3/4/19.
 */

public class CurrentDayDecatator implements DayViewDecorator {

    private Drawable drawable;

    CalendarDay currentDay = CalendarDay.from(new Date());

    public CurrentDayDecatator(Activity context) {

        drawable = ContextCompat.getDrawable(context,R.drawable.first_day_month);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(currentDay);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }

}
