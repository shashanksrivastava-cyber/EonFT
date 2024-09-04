package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

public class AdditionalDeviceDetails {

    @SerializedName("cust_name")
    String cust_name;
    @SerializedName("device_type")
    String device_type;
    @SerializedName("quantity")
    String quantity;

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
