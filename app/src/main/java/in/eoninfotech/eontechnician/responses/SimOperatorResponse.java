package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 20/11/18.
 */

public class SimOperatorResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("operators")
    ArrayList<SimOperatorDetail> simOperatorDetails = new ArrayList<SimOperatorDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<SimOperatorDetail> getSimOperatorDetails() {
        return simOperatorDetails;
    }

    public void setSimOperatorDetails(ArrayList<SimOperatorDetail> simOperatorDetails) {
        this.simOperatorDetails = simOperatorDetails;
    }
}
