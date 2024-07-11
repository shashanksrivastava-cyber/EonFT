package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 31/10/18.
 */

public class FaultyDevices {

    @SerializedName("type")
    Integer type;
    @SerializedName("data")
    ArrayList<FaultyDevicesDetails> faultyDevices = new ArrayList<FaultyDevicesDetails>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<FaultyDevicesDetails> getFaultyDevices() {
        return faultyDevices;
    }

    public void setFaultyDevices(ArrayList<FaultyDevicesDetails> faultyDevices) {
        this.faultyDevices = faultyDevices;
    }
}
