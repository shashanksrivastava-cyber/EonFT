package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 20/12/18.
 */

public class VehNotAvailReasonDetail {

    @SerializedName("id")
    String id;
    @SerializedName("reason")
    String reason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
