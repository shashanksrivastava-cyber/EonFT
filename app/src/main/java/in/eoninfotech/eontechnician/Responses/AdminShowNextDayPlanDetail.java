package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.helper.ViewPlanDetail;

/**
 * Created by root on 31/10/17.
 */
public class AdminShowNextDayPlanDetail {

    @SerializedName("technician")
    private String tech_name;
    @SerializedName("tech_record")
    ArrayList<ViewPlanDetail> view_plan = new ArrayList<ViewPlanDetail>();

    public String getTech_name() {
        return tech_name;
    }

    public ArrayList<ViewPlanDetail> getView_plan() {
        return view_plan;
    }

}
