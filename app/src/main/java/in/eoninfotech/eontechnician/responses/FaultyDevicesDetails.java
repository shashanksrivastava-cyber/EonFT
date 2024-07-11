package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by root on 1/11/18.
 */

public class FaultyDevicesDetails implements Serializable {

    @SerializedName("server")
    private String server;
    @SerializedName("database")
    private String database;
    @SerializedName("cust_id")
    private String cust_id;
    @SerializedName("customer")
    private String customer;
    @SerializedName("loc_id")
    private String loc_id;
    @SerializedName("location")
    private String location;
    @SerializedName("total_device")
    private String total_device;
    @SerializedName("faulty_dev_cnt")
    private String faulty_dev_cnt;
    @SerializedName("faulty_devices")
    private String faulty_devices;
    @SerializedName("faulty_drs_cnt")
    private String faulty_drs_cnt;
    @SerializedName("faulty_drs")
    private String faulty_drs;

    @SerializedName("faulty_fuel_count")
    private String faulty_fuel_count;

    @SerializedName("faulty_fuel")
    private String faulty_fuel;
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

    public String getFaulty_dev_cnt() {
        return faulty_dev_cnt;
    }

    public void setFaulty_dev_cnt(String faulty_dev_cnt) {
        this.faulty_dev_cnt = faulty_dev_cnt;
    }

    public String getFaulty_devices() {
        return faulty_devices;
    }

    public void setFaulty_devices(String faulty_devices) {
        this.faulty_devices = faulty_devices;
    }

    public String getFaulty_drs_cnt() {
        return faulty_drs_cnt;
    }

    public void setFaulty_drs_cnt(String faulty_drs_cnt) {
        this.faulty_drs_cnt = faulty_drs_cnt;
    }

    public String getFaulty_drs() {
        return faulty_drs;
    }

    public void setFaulty_drs(String faulty_drs) {
        this.faulty_drs = faulty_drs;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getLoc_id() {
        return loc_id;
    }

    public void setLoc_id(String loc_id) {
        this.loc_id = loc_id;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getFaulty_fuel_count() {
        return faulty_fuel_count;
    }

    public void setFaulty_fuel_count(String faulty_fuel_count) {
        this.faulty_fuel_count = faulty_fuel_count;
    }

    public String getFaulty_fuel() {
        return faulty_fuel;
    }

    public void setFaulty_fuel(String faulty_fuel) {
        this.faulty_fuel = faulty_fuel;
    }
}
