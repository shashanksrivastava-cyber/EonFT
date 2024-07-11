package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 25/9/18.
 */

public class DRSResponse implements Serializable {

        @SerializedName("type")
        Integer type;
        @SerializedName("client")
        ArrayList<DRSDetail> drsDetails = new ArrayList<DRSDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<DRSDetail> getDrsDetails() {
        return drsDetails;
    }

    public void setDrsDetails(ArrayList<DRSDetail> drsDetails) {
        this.drsDetails = drsDetails;
    }
}
