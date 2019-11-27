package in.eoninfotech.eontechnician;

import android.support.v4.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

import static com.thefinestartist.Base.getContext;

/**
 * Created by root on 9/4/19.
 */

public class DayEnableDecorator implements DayViewDecorator {

        private HashSet<CalendarDay> enabledDates;
        public DayEnableDecorator(Collection<CalendarDay> enabledDates) {
            this.enabledDates = new HashSet<>(enabledDates);
        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {

            return enabledDates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true);
            view.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.grey_circle));
        }
    }
