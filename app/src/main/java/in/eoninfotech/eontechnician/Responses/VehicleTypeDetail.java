package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by root on 12/9/18.
 */

public class VehicleTypeDetail implements Serializable {

    @SerializedName("v_id")
    private int vehicle_Id;
    @SerializedName("v_name")
    private String vehicle_Name;
    @SerializedName("drs_type")
    private String drs_type;

    public int getVehicle_Id() {
        return vehicle_Id;
    }

    public void setVehicle_Id(int vehicle_Id) {
        this.vehicle_Id = vehicle_Id;
    }

    public String getVehicle_Name() {
        return vehicle_Name;
    }

    public void setVehicle_Name(String vehicle_Name) {
        this.vehicle_Name = vehicle_Name;
    }

    public String getDrs_type() {
        return drs_type;
    }

    public void setDrs_type(String drs_type) {
        this.drs_type = drs_type;
    }
}
