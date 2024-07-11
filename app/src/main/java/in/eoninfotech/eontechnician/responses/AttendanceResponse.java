package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 5/4/19.
 */

public class AttendanceResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("dates")
    ArrayList<AttendanceDetail> attendanceDetails = new ArrayList<AttendanceDetail>();
    @SerializedName("present")
    String present;
    @SerializedName("absent")
    String absent;
    @SerializedName("leave")
    String leave;
    @SerializedName("travel")
    String travel;
    @SerializedName("comp_off")
    String comp_off;
    @SerializedName("holiday")
    String holiday;
    @SerializedName("avg_hours")
    String avg_hours;
    @SerializedName("colorCode")
    String colorCode;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<AttendanceDetail> getAttendanceDetails() {
        return attendanceDetails;
    }

    public void setAttendanceDetails(ArrayList<AttendanceDetail> attendanceDetails) {
        this.attendanceDetails = attendanceDetails;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getAbsent() {
        return absent;
    }

    public void setAbsent(String absent) {
        this.absent = absent;
    }

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

    public String getComp_off() {
        return comp_off;
    }

    public void setComp_off(String comp_off) {
        this.comp_off = comp_off;
    }

    public String getAvg_hours() {
        return avg_hours;
    }

    public void setAvg_hours(String avg_hours) {
        this.avg_hours = avg_hours;
    }

    public String getTravel() {
        return travel;
    }

    public void setTravel(String travel) {
        this.travel = travel;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}
