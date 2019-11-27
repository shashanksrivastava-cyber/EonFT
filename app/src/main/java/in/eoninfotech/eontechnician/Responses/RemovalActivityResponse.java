package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 11/1/19.
 */

public class RemovalActivityResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("activities")
    ArrayList<RemovalActivityDetail> removalActivityDetails = new ArrayList<RemovalActivityDetail>();


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<RemovalActivityDetail> getRemovalActivityDetails() {
        return removalActivityDetails;
    }

    public void setRemovalActivityDetails(ArrayList<RemovalActivityDetail> removalActivityDetails) {
        this.removalActivityDetails = removalActivityDetails;
    }
}
