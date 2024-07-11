package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 14/11/18.
 */

public class LogResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("log_status")
    ArrayList<LogDetail> logDetails = new ArrayList<LogDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<LogDetail> getLogDetails() {
        return logDetails;
    }

    public void setLogDetails(ArrayList<LogDetail> logDetails) {
        this.logDetails = logDetails;
    }
}
