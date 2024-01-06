package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Iterator;

public class MainResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("msg")
    String msg;
    @SerializedName("status")
    String message;
    @SerializedName("total_device_count")
    String total_device_count;
    @SerializedName("total_received_count")
    String total_received_count;
    @SerializedName("bill_no")
    String bill_no;
    @SerializedName("work_data")
    ArrayList<DeviceTypeOtherAis> deviceTypesArr = new ArrayList<>();
    @SerializedName("transit_list")
    ArrayList<TransitList> transit_list = new ArrayList<>();
    @SerializedName("dispatched_device_list")
    ArrayList<DispatchDeviceList> dispatched_device_list = new ArrayList<>();
    @SerializedName("dispatched_device_details")
    ArrayList<DispatchDeviceDetails> dispatched_device_details = new ArrayList<>();
    @SerializedName("item_list")
    ArrayList<ItemList> items_list = new ArrayList<>();

    @SerializedName("removal_device_list")
    ArrayList<DeviceList> device_list = new ArrayList<>();

    @SerializedName("srno_device_list")
    ArrayList<DeviceSerialNo> srno_device_list = new ArrayList<>();


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

    public ArrayList<DispatchDeviceList> getDispatched_device_list() {
        return dispatched_device_list;
    }

    public void setDispatched_device_list(ArrayList<DispatchDeviceList> dispatched_device_list) {
        this.dispatched_device_list = dispatched_device_list;
    }

    public ArrayList<DispatchDeviceDetails> getDispatched_device_details() {
        return dispatched_device_details;
    }

    public void setDispatched_device_details(ArrayList<DispatchDeviceDetails> dispatched_device_details) {
        this.dispatched_device_details = dispatched_device_details;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<ItemList> getItems_list() {
        return items_list;
    }

    public void setItems_list(ArrayList<ItemList> items_list) {
        this.items_list = items_list;
    }

    public String getTotal_received_count() {
        return total_received_count;
    }

    public void setTotal_received_count(String total_received_count) {
        this.total_received_count = total_received_count;
    }

    public ArrayList<DeviceList> getDevice_list() {
        return device_list;
    }

    public void setDevice_list(ArrayList<DeviceList> device_list) {
        this.device_list = device_list;
    }

    public ArrayList<DeviceSerialNo> getSrno_device_list() {
        return srno_device_list;
    }

    public void setSrno_device_list(ArrayList<DeviceSerialNo> srno_device_list) {
        this.srno_device_list = srno_device_list;
    }
}
