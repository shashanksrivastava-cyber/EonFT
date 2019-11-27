package in.eoninfotech.eontechnician.webservice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 18/4/19.
 */

public class LocationsResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("msg")
    String message;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
