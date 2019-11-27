package in.eoninfotech.eontechnician.helper;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 13/10/17.
 */
public class ListIncentiveDetail {

    @SerializedName("add_cnt")
    private String add_cnt;
    @SerializedName("add_color")
    private String add_color;
    @SerializedName("attendance")
    private String attendance;
    @SerializedName("activity")
    private String activity;
    @SerializedName("stock")
    private String stock;
    @SerializedName("call_sheets")
    private String call_sheets;
    @SerializedName("payment_collection")
    private String payment_collection;
    @SerializedName("incentive_amt")
    private String incentive_amt;

    public String getAdd_cnt() {
        return add_cnt;
    }

    public void setAdd_cnt(String add_cnt) {
        this.add_cnt = add_cnt;
    }

    public String getAdd_color() {
        return add_color;
    }

    public void setAdd_color(String add_color) {
        this.add_color = add_color;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCall_sheets() {
        return call_sheets;
    }

    public void setCall_sheets(String call_sheets) {
        this.call_sheets = call_sheets;
    }

    public String getPayment_collection() {
        return payment_collection;
    }

    public void setPayment_collection(String payment_collection) {
        this.payment_collection = payment_collection;
    }

    public String getIncentive_amt() {
        return incentive_amt;
    }

    public void setIncentive_amt(String incentive_amt) {
        this.incentive_amt = incentive_amt;
    }
}
