package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 2/2/18.
 */
public class LogStatusResponse {
    @SerializedName("address")
    String address;
    @SerializedName("status")
    private String status;
    @SerializedName("time")
    private String time;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public String getLatitude() { return latitude; }

    public String getLongitude() { return longitude; }

}
