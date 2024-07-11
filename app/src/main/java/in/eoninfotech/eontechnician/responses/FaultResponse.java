package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 18/9/18.
 */

public class FaultResponse implements Serializable {
    @SerializedName("type")
    Integer type;
    @SerializedName("item")
    ArrayList<FaultList> faultLists = new ArrayList<FaultList>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<FaultList> getFaultLists() {
        return faultLists;
    }

    public void setFaultLists(ArrayList<FaultList> faultLists) {
        this.faultLists = faultLists;
    }
}
