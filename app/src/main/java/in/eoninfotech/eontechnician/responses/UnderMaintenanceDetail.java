package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 5/11/18.
 */

public class UnderMaintenanceDetail {

    @SerializedName("customer")
    private String customer;
    @SerializedName("location")
    private String location;
    @SerializedName("total_device")
    private String total_device;
    @SerializedName("dev_detail")
    private String dev_detail;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTotal_device() {
        return total_device;
    }

    public void setTotal_device(String total_device) {
        this.total_device = total_device;
    }

    public String getDev_detail() {
        return dev_detail;
    }

    public void setDev_detail(String dev_detail) {
        this.dev_detail = dev_detail;
    }
}
