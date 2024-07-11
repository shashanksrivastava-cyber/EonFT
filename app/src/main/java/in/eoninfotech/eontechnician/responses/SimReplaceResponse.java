package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 19/11/18.
 */

public class SimReplaceResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("reasons")
    ArrayList<SimDetail> simDetails = new ArrayList<SimDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<SimDetail> getSimDetails() {
        return simDetails;
    }

    public void setSimDetails(ArrayList<SimDetail> simDetails) {
        this.simDetails = simDetails;
    }
}
