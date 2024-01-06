package in.eoninfotech.eontechnician.helper;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 10/10/17.
 */
public class IncentiveDetail {

    @SerializedName("up_time")
    private String up_time;
    @SerializedName("stock_upd")
    private String stock_upd;
    @SerializedName("activity_upd")
    private String activity_upd;
    @SerializedName("next_day_plan")
    private String next_day_plan;
    @SerializedName("unauth_abs")
    private String unauth_abs;
    @SerializedName("dc_sheet")
    private String dc_sheet;
    @SerializedName("total_inc")
    private String total_inc;

    public String getUp_time() {
        return up_time;
    }

    public void setUp_time(String date) {
        this.up_time = date;
    }

    public String getStock_upd() {
        return stock_upd;
    }

    public void setStock_upd(String stock_upd) {
        this.stock_upd = stock_upd;
    }

    public String getActivity_upd() {
        return activity_upd;
    }

    public void setActivity_upd(String vts_new) {
        this.activity_upd = vts_new;
    }

    public String getNext_day_plan() {
        return next_day_plan;
    }

    public void setNext_day_plan(String drs_new) {
        this.next_day_plan = drs_new;
    }

    public String getUnauth_abs() {
        return unauth_abs;
    }

    public void setUnauth_abs(String date) {
        this.unauth_abs = date;
    }

    public String getDc_sheet() {
        return dc_sheet;
    }

    public void setDc_sheet(String cust_name) {
        this.dc_sheet = cust_name;  }

    public String getTotal_inc() {
        return total_inc;
    }

    public void setTotal_inc(String vts_new) {
        this.total_inc = vts_new;
    }


}
