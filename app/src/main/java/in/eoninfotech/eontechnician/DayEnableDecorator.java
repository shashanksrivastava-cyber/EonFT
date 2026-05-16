package in.eoninfotech.eontechnician;

import android.content.Context;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

import androidx.core.content.ContextCompat;


/**
 * Created by root on 9/4/19.
 */

public class DayEnableDecorator implements DayViewDecorator {

        Context context;

        private HashSet<CalendarDay> enabledDates;
        public DayEnableDecorator(Collection<CalendarDay> enabledDates) {
            this.context = context;
            this.enabledDates = new HashSet<>(enabledDates);
        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {

            return enabledDates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
//            view.setDaysDisabled(true);
//            view.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.grey_circle));

            if (context != null) {
                view.setBackgroundDrawable(
                        ContextCompat.getDrawable(context, R.drawable.grey_circle)
                );
            }
        }
    }
