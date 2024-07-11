package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 18/10/18.
 */

public class TechnicianMonthResponse implements Serializable {
    @SerializedName("type")
    Integer type;
    @SerializedName("tech_data")
    ArrayList<TechnicianMonthDetail> techList = new ArrayList<TechnicianMonthDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<TechnicianMonthDetail> getTechList() {
        return techList;
    }

    public void setTechList(ArrayList<TechnicianMonthDetail> techList) {
        this.techList = techList;
    }
}
