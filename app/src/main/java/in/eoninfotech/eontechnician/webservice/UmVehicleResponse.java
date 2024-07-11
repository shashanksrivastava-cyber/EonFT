package in.eoninfotech.eontechnician.webservice;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UmVehicleResponse {

    @SerializedName("type")
    String type;
    @SerializedName("um_vehicle_data")
    ArrayList<UmVehicleDetail> umVehicleDetails = new ArrayList<UmVehicleDetail>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<UmVehicleDetail> getUmVehicleDetails() {
        return umVehicleDetails;
    }

    public void setUmVehicleDetails(ArrayList<UmVehicleDetail> umVehicleDetails) {
        this.umVehicleDetails = umVehicleDetails;
    }




}
