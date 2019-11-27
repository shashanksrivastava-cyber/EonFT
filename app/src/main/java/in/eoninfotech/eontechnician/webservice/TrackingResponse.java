package in.eoninfotech.eontechnician.webservice;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.Responses.AttendanceDetail;

/**
 * Created by root on 27/4/19.
 */

public class TrackingResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("data")
    ArrayList<TrackingDetail> trackingDetails = new ArrayList<TrackingDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<TrackingDetail> getTrackingDetails() {
        return trackingDetails;
    }

    public void setTrackingDetails(ArrayList<TrackingDetail> trackingDetails) {
        this.trackingDetails = trackingDetails;
    }
}
