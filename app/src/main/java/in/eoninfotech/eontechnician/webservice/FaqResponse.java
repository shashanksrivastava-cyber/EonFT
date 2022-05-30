package in.eoninfotech.eontechnician.webservice;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.Responses.DeviceTypeOtherAis;

public class FaqResponse {

    @SerializedName("type")
    String type;
    @SerializedName("status")
    String message;
    @SerializedName("faults")
    ArrayList<FaultsDetails> faultsDetails = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<FaultsDetails> getFaultsDetails() {
        return faultsDetails;
    }

    public void setFaultsDetails(ArrayList<FaultsDetails> faultsDetails) {
        this.faultsDetails = faultsDetails;
    }
}
