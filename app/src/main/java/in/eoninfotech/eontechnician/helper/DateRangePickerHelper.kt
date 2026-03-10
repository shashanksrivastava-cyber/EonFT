package `in`.eoninfotech.eontechnician.helper

import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.core.util.Pair

object DateRangePickerHelper {

    fun showDateRangePicker(
        fragmentManager: FragmentManager,
        startDate: Long?,
        endDate: Long?,
        onDateSelected: (display: String, apiStart: String, apiEnd: String, startLong: Long, endLong: Long) -> Unit
    ) {

        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select Date Range")
            .setSelection(
                if (startDate != null && endDate != null)
                    Pair(startDate, endDate)   // This must be androidx.core.util.Pair
                else null
            )
            .build()

        picker.show(fragmentManager, "DATE_RANGE")

        picker.addOnPositiveButtonClickListener { selection ->

            val start = selection.first
            val end = selection.second

            val displayFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val apiFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val startDateObj = Date(start)
            val endDateObj = Date(end)

            val display = "${displayFormat.format(startDateObj)} - ${displayFormat.format(endDateObj)}"

            val apiStart = apiFormat.format(startDateObj)
            val apiEnd = apiFormat.format(endDateObj)

            onDateSelected(display, apiStart, apiEnd, start, end)
        }
    }


    fun getLast7Days(): Pair<Long, Long> {

        val calendar = Calendar.getInstance()

        val endDate = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_MONTH, -6)

        val startDate = calendar.timeInMillis

        return Pair(startDate, endDate)
    }
}