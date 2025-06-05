package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.LiveStatusSerialNo;


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
    @SerializedName("serial_no")
    String serial_no;
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

    @SerializedName("data")
    ArrayList<DeviceLiveStatus> data = new ArrayList<>();

    @SerializedName("tech_return_device_list")
    ArrayList<TechReturnDevice> tech_return_device_list = new ArrayList<>();

    @SerializedName("tech_return_details")
    ArrayList<TechReturn> tech_return_details = new ArrayList<>();

    @SerializedName("device_count")
    ArrayList<DeviceCount> device_count = new ArrayList<>();

    @SerializedName("main_client_list")
    ArrayList<MainClientList> main_client_list = new ArrayList<>();

    @SerializedName("device_count_detail")
    ArrayList<DeviceCountDetail> device_count_detail = new ArrayList<>();

    @SerializedName("tech_req_dtls")
    ArrayList<TechRequirementDetails> tech_req_dtls = new ArrayList<>();

    @SerializedName("tech_devices")
    ArrayList<LiveStatusSerialNo> live_status_serial_no = new ArrayList<>();

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

    public ArrayList<DeviceLiveStatus> getData() {
        return data;
    }

    public void setData(ArrayList<DeviceLiveStatus> data) {
        this.data = data;
    }

    public ArrayList<TechReturnDevice> getTech_return_device_list() {
        return tech_return_device_list;
    }

    public void setTech_return_device_list(ArrayList<TechReturnDevice> tech_return_device_list) {
        this.tech_return_device_list = tech_return_device_list;
    }

    public ArrayList<TechReturn> getTech_return_details() {
        return tech_return_details;
    }

    public void setTech_return_details(ArrayList<TechReturn> tech_return_details) {
        this.tech_return_details = tech_return_details;
    }

    public ArrayList<DeviceCount> getDevice_count() {
        return device_count;
    }

    public void setDevice_count(ArrayList<DeviceCount> device_count) {
        this.device_count = device_count;
    }

    public ArrayList<MainClientList> getMain_client_list() {
        return main_client_list;
    }

    public void setMain_client_list(ArrayList<MainClientList> main_client_list) {
        this.main_client_list = main_client_list;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public ArrayList<DeviceCountDetail> getDevice_count_detail() {
        return device_count_detail;
    }

    public void setDevice_count_detail(ArrayList<DeviceCountDetail> device_count_detail) {
        this.device_count_detail = device_count_detail;
    }

    public ArrayList<TechRequirementDetails> getTech_req_dtls() {
        return tech_req_dtls;
    }

    public void setTech_req_dtls(ArrayList<TechRequirementDetails> tech_req_dtls) {
        this.tech_req_dtls = tech_req_dtls;
    }

    public ArrayList<LiveStatusSerialNo> getLive_status_serial_no() {
        return live_status_serial_no;
    }

    public void setLive_status_serial_no(ArrayList<LiveStatusSerialNo> live_status_serial_no) {
        this.live_status_serial_no = live_status_serial_no;
    }
}
