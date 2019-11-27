package in.eoninfotech.eontechnician.helper;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 11/10/17.
 */
public class CriticalSitesData {
    @SerializedName("location")
    private String location;
    @SerializedName("cust_name")
    private String cust_name;
    @SerializedName("faulty_dev")
    private String faulty_dev;
    @SerializedName("faulty_drs")
    private String faulty_drs;

    public String getLocation() {
        return location;
    }

    public void setLocation(String date) {
        this.location = date;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getFaulty_dev() {
        return faulty_dev;
    }

    public void setFaulty_dev(String vts_new) {
        this.faulty_dev = vts_new;
    }

    public String getFaulty_drs() {
        return faulty_drs;
    }

    public void setFaulty_drs(String drs_new) {
        this.faulty_drs = drs_new;
    }

}
