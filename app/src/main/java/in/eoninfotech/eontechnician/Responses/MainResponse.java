package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MainResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("status")
    String message;

    @SerializedName("total_device_count")
    String total_device_count;
    @SerializedName("bill_no")
    String bill_no;
    @SerializedName("work_data")
    ArrayList<DeviceTypeOtherAis> deviceTypesArr = new ArrayList<>();

    @SerializedName("transit_list")
    ArrayList<TransitList> transit_list = new ArrayList<>();

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

    public String getTotal_device_count() {
        return total_device_count;
    }

    public void setTotal_device_count(String total_device_count) {
        this.total_device_count = total_device_count;
    }

    public void setDeviceTypesArr(ArrayList<DeviceTypeOtherAis> deviceTypesArr) {
        this.deviceTypesArr = deviceTypesArr;
    }

    public ArrayList<TransitList> getTransit_list() {
        return transit_list;
    }

    public void setTransit_list(ArrayList<TransitList> transit_list) {
        this.transit_list = transit_list;
    }
}
