package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 12/9/18.
 */

public class ClientResponse implements Serializable {

    @SerializedName("type")
    Integer type;
    @SerializedName("cust_data")
    ArrayList<ClientDetails> clientList = new ArrayList<ClientDetails>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<ClientDetails> getClientList() {
        return clientList;
    }

    public void setClientList(ArrayList<ClientDetails> clientList) {
        this.clientList = clientList;
    }
}
