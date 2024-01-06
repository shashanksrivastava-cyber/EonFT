package in.eoninfotech.eontechnician.helper;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 5/10/17.
 */
public class PlantDetails {
    @SerializedName("clid")
    private String plant_id;
    @SerializedName("clname")
    private String plant_name;
    @SerializedName("dev_detail")
    private List<String> regno = new ArrayList<String>();
    @SerializedName("drs_detail")
    private List<String> drs_details = new ArrayList<String>();


    public String getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(String plant_id) {
        this.plant_id = plant_id;
    }

    public String getPlant_name() {
        return plant_name;
    }

    public void setPlant_name(String plant_name) {
        this.plant_name = plant_name;
    }

    public List<String> getRegno() {
        return regno;
    }

    public void setRegno(List<String> vts_new) {
        this.regno = vts_new;
    }

    public List<String> getDrs_details() {
        return drs_details;
    }

    public void setDrs_detailsw(List<String> drs_new) {
        this.drs_details = drs_new;
    }
}

