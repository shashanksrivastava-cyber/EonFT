package in.eoninfotech.eontechnician.helper;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 3/4/18.
 */

public class SalesCustDetail {
    @SerializedName("cust_name")
    private String cust_name;
    @SerializedName("cust_street_name")
    private String cust_street_name;
    @SerializedName("cust_city")
    String cust_city;
    @SerializedName("cust_office_number")
    String cust_office_number;
    @SerializedName("cust_district")
    String cust_district;
    @SerializedName("cust_state")
    String cust_state;
    @SerializedName("cust_pincode")
    String cust_pincode;
    @SerializedName("cust_p_name")
    String cust_p_name;
    @SerializedName("cust_p_number")
    String cust_p_number;
    @SerializedName("cust_p_id")
    String cust_p_id;

    public String getCust_name() {
        return cust_name;
    }

    public String getCust_street_name() {
        return cust_street_name;
    }

    public String getCust_city() {
        return cust_city;
    }

    public String getCust_office_number() {
        return cust_office_number;
    }

    public String getCust_district() { return cust_district; }

    public String getCust_pincode() { return cust_pincode; }

    public String getCust_state() { return cust_state; }

    public String getCust_p_name() { return cust_p_name; }

    public String getCust_p_number() { return cust_p_number; }

    public String getCust_p_id() { return cust_p_id; }

}
