package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 12/9/18.
 */

public class ClientLocationResponse implements Serializable {

    @SerializedName("type")
    Integer type;
    @SerializedName("cust_loc")
    ArrayList<ClientLocationDetail> clientLoc = new ArrayList<ClientLocationDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<ClientLocationDetail> getClientLoc() {
        return clientLoc;
    }

    public void setClientLoc(ArrayList<ClientLocationDetail> clientLoc) {
        this.clientLoc = clientLoc;
    }
}
