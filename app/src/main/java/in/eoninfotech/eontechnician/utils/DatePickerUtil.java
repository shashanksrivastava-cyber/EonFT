package in.eoninfotech.eontechnician.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Locale;

public class DatePickerUtil {

    public static void showDatePicker(Context context,
                                      final EditText targetEditText,
                                      int allowPastDays,
                                      int allowPastMonths,
                                      boolean allowFuture) {

        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        int day = today.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    // Format date as dd-MM-yyyy
                    String formattedDate = String.format(Locale.getDefault(),
                            "%02d-%02d-%d", selectedDay, (selectedMonth + 1), selectedYear);

                    targetEditText.setText(formattedDate);
                },
                year, month, day
        );

        // ✅ Disallow future dates if requested
        if (!allowFuture) {
            datePickerDialog.getDatePicker().setMaxDate(today.getTimeInMillis());
        }

        // ✅ Allow only a specific past range if defined
        if (allowPastDays > 0 || allowPastMonths > 0) {
            Calendar minDate = (Calendar) today.clone();

            if (allowPastMonths > 0)
                minDate.add(Calendar.MONTH, -allowPastMonths);

            if (allowPastDays > 0)
                minDate.add(Calendar.DAY_OF_MONTH, -allowPastDays);

            datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        }

        datePickerDialog.show();
    }

    /**
     * Simpler overloaded method: only disallow future dates.
     */
    public static void showDatePicker(Context context, final EditText targetEditText) {
        showDatePicker(context, targetEditText, 0, 0, false);
    }
}
