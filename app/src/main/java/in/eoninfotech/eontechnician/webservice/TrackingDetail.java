package in.eoninfotech.eontechnician.webservice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 27/4/19.
 */

public class TrackingDetail {

    @SerializedName("track_status")
    String track_status;
    @SerializedName("track_interval")
    String track_interval;
    @SerializedName("user_status")
    String user_status;
    @SerializedName("start_time")
    String start_time;
    @SerializedName("end_time")
    String end_time;

    public String getTrack_status() {
        return track_status;
    }

    public void setTrack_status(String track_status) {
        this.track_status = track_status;
    }

    public String getTrack_interval() {
        return track_interval;
    }

    public void setTrack_interval(String track_interval) {
        this.track_interval = track_interval;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
