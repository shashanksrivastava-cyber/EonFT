package in.eoninfotech.eontechnician.webservice;

import com.google.gson.annotations.SerializedName;

public class BillDetails {

    @SerializedName("bill_no")
    String bill_no;
    @SerializedName("bill_amt")
    String bill_amt;
    @SerializedName("bill_date")
    String bill_date;
    @SerializedName("app_amt")
    String app_amt;
    @SerializedName("app_date")
    String app_date;
    @SerializedName("rej_date")
    String rej_date;
    @SerializedName("canc_date")
    String canc_date;
    @SerializedName("rec_date")
    String rec_date;
    @SerializedName("status")
    String status;
    @SerializedName("remarks")
    String remarks;

    public String getBill_no() {
        return bill_no;
    }

    public void setBill_no(String bill_no) {
        this.bill_no = bill_no;
    }

    public String getBill_amt() {
        return bill_amt;
    }

    public void setBill_amt(String bill_amt) {
        this.bill_amt = bill_amt;
    }

    public String getBill_date() {
        return bill_date;
    }

    public void setBill_date(String bill_date) {
        this.bill_date = bill_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getApp_amt() {
        return app_amt;
    }

    public void setApp_amt(String app_amt) {
        this.app_amt = app_amt;
    }

    public String getApp_date() {
        return app_date;
    }

    public void setApp_date(String app_date) {
        this.app_date = app_date;
    }

    public String getRej_date() {
        return rej_date;
    }

    public void setRej_date(String rej_date) {
        this.rej_date = rej_date;
    }

    public String getCanc_date() {
        return canc_date;
    }

    public void setCanc_date(String canc_date) {
        this.canc_date = canc_date;
    }

    public String getRec_date() {
        return rec_date;
    }

    public void setRec_date(String rec_date) {
        this.rec_date = rec_date;
    }
}
