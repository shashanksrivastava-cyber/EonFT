package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 20/12/18.
 */

public class NotAvailActivityResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("activity_data")
    ArrayList<NotAvailActivityDetail> notAvailActivityDetails = new ArrayList<NotAvailActivityDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<NotAvailActivityDetail> getNotAvailActivityDetails() {
        return notAvailActivityDetails;
    }

    public void setNotAvailActivityDetails(ArrayList<NotAvailActivityDetail> notAvailActivityDetails) {
        this.notAvailActivityDetails = notAvailActivityDetails;
    }
}
