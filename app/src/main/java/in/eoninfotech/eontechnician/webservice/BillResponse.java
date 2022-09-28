package in.eoninfotech.eontechnician.webservice;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BillResponse {
    @SerializedName("type")
    String type;
    @SerializedName("bill_details")
    ArrayList<BillDetails> billDetails = new ArrayList<BillDetails>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<BillDetails> getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(ArrayList<BillDetails> billDetails) {
        this.billDetails = billDetails;
    }
}
