package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 5/11/18.
 */

public class UnderMaintenanceResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("data")
    ArrayList<UnderMaintenanceDetail> uMainDetail = new ArrayList<UnderMaintenanceDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<UnderMaintenanceDetail> getuMainDetail() {
        return uMainDetail;
    }

    public void setuMainDetail(ArrayList<UnderMaintenanceDetail> uMainDetail) {
        this.uMainDetail = uMainDetail;
    }
}
