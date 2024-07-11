package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 12/9/18.
 */

public class VehicleTypeResponse implements Serializable {

    @SerializedName("type")
    Integer type;
    @SerializedName("vehicle_data")
    ArrayList<VehicleTypeDetail> vehicletypeList = new ArrayList<VehicleTypeDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<VehicleTypeDetail> getVehicletypeList() {
        return vehicletypeList;
    }

    public void setVehicletypeList(ArrayList<VehicleTypeDetail> vehicletypeList) {
        this.vehicletypeList = vehicletypeList;
    }
}
