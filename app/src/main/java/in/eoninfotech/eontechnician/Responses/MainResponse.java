package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MainResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("status")
    String message;
    @SerializedName("bill_no")
    String bill_no;
    @SerializedName("work_data")
    ArrayList<DeviceTypeOtherAis> deviceTypesArr = new ArrayList<>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<DeviceTypeOtherAis> getDeviceTypesArr() { return deviceTypesArr; }

    public String getBill_no() {
        return bill_no;
    }

    public void setBill_no(String bill_no) {
        this.bill_no = bill_no;
    }
}
