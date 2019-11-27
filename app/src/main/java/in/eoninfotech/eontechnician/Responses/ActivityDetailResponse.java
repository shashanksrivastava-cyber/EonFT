package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 16/10/18.
 */

public class ActivityDetailResponse {

    @SerializedName("activity_id")
    private String activity_id;
    @SerializedName("activity")
    private String activity;
    @SerializedName("client_name")
    private String client_Name;
    @SerializedName("client_loc")
    private String client_loc;
    @SerializedName("reg_no")
    private String reg_no;
    @SerializedName("new_vts")
    private String new_vts;
    @SerializedName("old_vts")
    private String old_vts;
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
    @SerializedName("reasons")
    private String reasons;
    @SerializedName("remarks")
    private String remarks;
    @SerializedName("collected_amt")
    private String collected_amt;
    @SerializedName("collected_date")
    private String collected_date;
    @SerializedName("collected_img ")
    private String collected_img;
    @SerializedName("collected_mtd")
    private String collected_mtd;

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
}
