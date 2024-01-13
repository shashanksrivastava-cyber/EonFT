package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by root on 19/9/18.
 */

public class VTSDetail implements Serializable {

    @SerializedName("reg_no")
    private String reg_no;
    @SerializedName("veh_type_id")
    private String veh_type_id;
    @SerializedName("veh_type")
    private String veh_type;
    @SerializedName("client_id")
    private String client_id;
    @SerializedName("client_name")
    private String client_name;
    @SerializedName("location_id")
    private String location_id;
    @SerializedName("location_name")
    private String location_name;
    @SerializedName("device_type_id")
    private String device_type;

    @SerializedName("bus_id")
    private String bus_id;


    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getVeh_type_id() {
        return veh_type_id;
    }

    public void setVeh_type_id(String veh_type_id) {
        this.veh_type_id = veh_type_id;
    }

    public String getVeh_type() {
        return veh_type;
    }

    public void setVeh_type(String veh_type) {
        this.veh_type = veh_type;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getBus_id() {
        return bus_id;
    }

    public void setBus_id(String bus_id) {
        this.bus_id = bus_id;
    }
}
