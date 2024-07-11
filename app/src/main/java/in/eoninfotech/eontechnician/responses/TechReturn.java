package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TechReturn {

    @SerializedName("device_list")
    ArrayList<DeviceList> device_list = new ArrayList<>();

    @SerializedName("device_items")
    ArrayList<DeviceItems> device_items = new ArrayList<>();


    public ArrayList<DeviceList> getDevice_list() {
        return device_list;
    }

    public void setDevice_list(ArrayList<DeviceList> device_list) {
        this.device_list = device_list;
    }

    public ArrayList<DeviceItems> getDevice_items() {
        return device_items;
    }

    public void setDevice_items(ArrayList<DeviceItems> device_items) {
        this.device_items = device_items;
    }

}
