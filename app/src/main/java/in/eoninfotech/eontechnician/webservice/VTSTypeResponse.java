package in.eoninfotech.eontechnician.webservice;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.Responses.DeviceTypeOtherAis;

public class VTSTypeResponse {

    @SerializedName("type")
    String type;
    @SerializedName("vts_types")
    ArrayList<DeviceTypeOtherAis> deviceTypesArr = new ArrayList<>();

    public String getType() {
        return type;
    }

    public ArrayList<DeviceTypeOtherAis> getDeviceTypesArr() {
        return deviceTypesArr;
    }
}
