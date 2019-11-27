package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 28/9/18.
 */

public class DisconnectionResponse implements Serializable {

    @SerializedName("type")
    Integer type;
    @SerializedName("disconnections")
    ArrayList<DisconnectionDetail> disconnectionDetails = new ArrayList<DisconnectionDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<DisconnectionDetail> getDisconnectionDetails() {
        return disconnectionDetails;
    }

    public void setDisconnectionDetails(ArrayList<DisconnectionDetail> disconnectionDetails) {
        this.disconnectionDetails = disconnectionDetails;
    }
}
