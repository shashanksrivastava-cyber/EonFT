package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;
import com.prolificinteractive.materialcalendarview.CalendarDay;

/**
 * Created by root on 5/4/19.
 */

public class AttendanceDetail {

    @SerializedName("date")
    String date;
    @SerializedName("display")
    String display;

    public String getDate() {

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
