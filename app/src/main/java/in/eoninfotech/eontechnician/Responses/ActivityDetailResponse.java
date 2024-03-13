package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 16/10/18.
 */

public class ActivityDetailResponse {

    @SerializedName("id")
    private String id;
    @SerializedName("activity_id")
    private String activity_id;
    @SerializedName("activity_name")
    private String activity;
    @SerializedName("customer_name")
    private String client_Name;
    @SerializedName("location_name")
    private String client_loc;
    @SerializedName("vts_type")
    private String vts_type;
    @SerializedName("reg_no")
    private String reg_no;
    @SerializedName("new_device_id")
    private String new_vts;
    @SerializedName("old_device_id")
    private String old_vts;
    @SerializedName("new_serial_no")
    private String new_serial_no;
    @SerializedName("old_serial_no")
    private String old_serial_no;
    @SerializedName("new_drs")
    private String new_drs;
    @SerializedName("old_drs")
    private String old_drs;
    @SerializedName("sim_opr")
    private String sim_opr;
    @SerializedName("old_sim")
    private String old_sim;
    @SerializedName("new_sim")
    private String new_sim;
    @SerializedName("lid_status")
    private String lid_status;
    @SerializedName("trans_receiver")
    private String trans_receiver;
    @SerializedName("temp_sensor")
    private String temp_sensor;
    @SerializedName("tilt_sensor")
    private String tilt_sensor;
    @SerializedName("fuel_status")
    private String fuel_status;
    @SerializedName("panic_status")
    private String panic_status;
    @SerializedName("sensor_veh_no")
    private String sensor_veh_no;
    @SerializedName("sensor_old_veh_no")
    private String sensor_old_veh_no;

    @SerializedName("reasons")
    private String reasons;
    @SerializedName("remarks")
    private String remarks;
    @SerializedName("collected_amt")
    private String collected_amt;
    @SerializedName("collected_date")
    private String collected_date;
    @SerializedName("image")
    private String collected_img;
    @SerializedName("collected_mtd")
    private String collected_mtd;

    @SerializedName("verified_date")
    private String verified_date;

    @SerializedName("verify_by")
    private String verify_by;

    @SerializedName("missing_type")
    private String missing_type;

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity() {return activity;}

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getClient_Name() {
        return client_Name;
    }

    public void setClient_Name(String client_Name) {
        this.client_Name = client_Name;
    }

    public String getClient_loc() {
        return client_loc;
    }

    public void setClient_loc(String client_loc) {
        this.client_loc = client_loc;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getNew_vts() {return new_vts;}

    public void setNew_vts(String new_vts) {
        this.new_vts = new_vts;
    }

    public String getOld_vts() {
        return old_vts;
    }

    public void setOld_vts(String old_vts) {
        this.old_vts = old_vts;
    }

    public String getNew_drs() {
        return new_drs;
    }

    public void setNew_drs(String new_drs) {
        this.new_drs = new_drs;
    }

    public String getOld_drs() {
        return old_drs;
    }

    public void setOld_drs(String old_drs) {
        this.old_drs = old_drs;
    }

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSim_opr() {
        return sim_opr;
    }

    public void setSim_opr(String sim_opr) {
        this.sim_opr = sim_opr;
    }

    public String getOld_sim() {
        return old_sim;
    }

    public void setOld_sim(String old_sim) {
        this.old_sim = old_sim;
    }

    public String getNew_sim() {
        return new_sim;
    }

    public void setNew_sim(String new_sim) {
        this.new_sim = new_sim;
    }

    public String getCollected_amt() {
        return collected_amt;
    }

    public void setCollected_amt(String collected_amt) {
        this.collected_amt = collected_amt;
    }

    public String getCollected_date() {
        return collected_date;
    }

    public void setCollected_date(String collected_date) {
        this.collected_date = collected_date;
    }

    public String getCollected_img() {
        return collected_img;
    }

    public void setCollected_img(String collected_img) {
        this.collected_img = collected_img;
    }

    public String getCollected_mtd() {
        return collected_mtd;
    }

    public void setCollected_mtd(String collected_mtd) {
        this.collected_mtd = collected_mtd;
    }

    public String getNew_serial_no() {
        return new_serial_no;
    }

    public void setNew_serial_no(String new_serial_no) {
        this.new_serial_no = new_serial_no;
    }

    public String getOld_serial_no() {
        return old_serial_no;
    }

    public void setOld_serial_no(String old_serial_no) {
        this.old_serial_no = old_serial_no;
    }

    public String getVts_type() {
        return vts_type;
    }

    public void setVts_type(String vts_type) {
        this.vts_type = vts_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLid_status() {
        return lid_status;
    }

    public void setLid_status(String lid_status) {
        this.lid_status = lid_status;
    }

    public String getTrans_receiver() {
        return trans_receiver;
    }

    public void setTrans_receiver(String trans_receiver) {
        this.trans_receiver = trans_receiver;
    }

    public String getTemp_sensor() {
        return temp_sensor;
    }

    public void setTemp_sensor(String temp_sensor) {
        this.temp_sensor = temp_sensor;
    }

    public String getTilt_sensor() {
        return tilt_sensor;
    }

    public void setTilt_sensor(String tilt_sensor) {
        this.tilt_sensor = tilt_sensor;
    }

    public String getFuel_status() {
        return fuel_status;
    }

    public void setFuel_status(String fuel_status) {
        this.fuel_status = fuel_status;
    }

    public String getPanic_status() {
        return panic_status;
    }

    public void setPanic_status(String panic_status) {
        this.panic_status = panic_status;
    }

    public String getSensor_veh_no() {
        return sensor_veh_no;
    }

    public void setSensor_veh_no(String sensor_veh_no) {
        this.sensor_veh_no = sensor_veh_no;
    }

    public String getSensor_old_veh_no() {
        return sensor_old_veh_no;
    }

    public void setSensor_old_veh_no(String sensor_old_veh_no) {
        this.sensor_old_veh_no = sensor_old_veh_no;
    }

    public String getVerified_date() {
        return verified_date;
    }

    public void setVerified_date(String verified_date) {
        this.verified_date = verified_date;
    }

    public String getVerify_by() {
        return verify_by;
    }

    public void setVerify_by(String verify_by) {
        this.verify_by = verify_by;
    }

    public String getMissing_type() {
        return missing_type;
    }

    public void setMissing_type(String missing_type) {
        this.missing_type = missing_type;
    }
}
