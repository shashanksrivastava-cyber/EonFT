package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 20/12/18.
 */

public class VehNotAvailReasonResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("reason_data")
    ArrayList<VehNotAvailReasonDetail> vehNotAvailReasonDetails = new ArrayList<VehNotAvailReasonDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<VehNotAvailReasonDetail> getVehNotAvailReasonDetails() {
        return vehNotAvailReasonDetails;
    }

    public void setVehNotAvailReasonDetails(ArrayList<VehNotAvailReasonDetail> vehNotAvailReasonDetails) {
        this.vehNotAvailReasonDetails = vehNotAvailReasonDetails;
    }
}
