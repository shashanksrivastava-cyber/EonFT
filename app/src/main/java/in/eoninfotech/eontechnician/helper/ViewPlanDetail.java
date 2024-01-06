package in.eoninfotech.eontechnician.helper;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 10/10/17.
 */
public class ViewPlanDetail {
    @SerializedName("date")
    private String date;
    @SerializedName("cust_name")
    private String cust_name;
    @SerializedName("client_name")
    private String client_name;
    @SerializedName("client_number")
    private String client_number;
    @SerializedName("spoken_to_client")
    private String spoken_to_client;
    @SerializedName("veh_chk")
    private String veh_chk;
    @SerializedName("drs_chk")
    private String drs_chk;
    @SerializedName("remarks")
    private String remarks;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String vts_new) {
        this.client_name = vts_new;
    }

    public String getClient_number() {
        return client_number;
    }

    public void setClient_number(String drs_new) {
        this.client_number = drs_new;
    }

    public String getSpoken_to_client() {
        return spoken_to_client;
    }

    public void setSpoken_to_client(String date) {
        this.spoken_to_client = date;
    }

    public String getVeh_chk() {
        return veh_chk;
    }

    public void setVeh_chk(String cust_name) {
        this.veh_chk = cust_name;
    }

    public String getDrs_chk() {
        return drs_chk;
    }

    public void setDrs_chk(String vts_new) {
        this.drs_chk = vts_new;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String drs_new) {
        this.remarks = drs_new;
    }


}
