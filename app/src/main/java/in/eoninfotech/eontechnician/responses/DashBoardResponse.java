package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 1/11/18.
 */

public class DashBoardResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("dashboard")
    ArrayList<TechDashboardDetail> techDashboardDetails = new ArrayList<TechDashboardDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<TechDashboardDetail> getTechDashboardDetails() {
        return techDashboardDetails;
    }

    public void setTechDashboardDetails(ArrayList<TechDashboardDetail> techDashboardDetails) {
        this.techDashboardDetails = techDashboardDetails;
    }
}
