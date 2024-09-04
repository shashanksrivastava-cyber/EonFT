package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TechRequirementDetails {

    @SerializedName("device_dtls")
    ArrayList<AdditionalDeviceDetails> device_dtls = new ArrayList<>();

    @SerializedName("accs_dtls")
    ArrayList<AdditionalAccessoryDetails> accs_dtls = new ArrayList<>();

    public ArrayList<AdditionalDeviceDetails> getDevice_dtls() {
        return device_dtls;
    }

    public void setDevice_dtls(ArrayList<AdditionalDeviceDetails> device_dtls) {
        this.device_dtls = device_dtls;
    }

    public ArrayList<AdditionalAccessoryDetails> getAccs_dtls() {
        return accs_dtls;
    }

    public void setAccs_dtls(ArrayList<AdditionalAccessoryDetails> accs_dtls) {
        this.accs_dtls = accs_dtls;
    }
}
