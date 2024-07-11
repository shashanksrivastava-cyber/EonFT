package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.helper.CriticalSitesData;
import in.eoninfotech.eontechnician.helper.IncentiveDetail;
import in.eoninfotech.eontechnician.helper.PlantDetails;
import in.eoninfotech.eontechnician.helper.SalesCustDetail;
import in.eoninfotech.eontechnician.helper.ViewPlanDetail;

/**
 * Created by root on 5/10/17.
 */
public class UpdateDataResponse {
    // variable name should be same as in the json response from php
    @SerializedName("type")
    Integer type;
    @SerializedName("msg")
    String message;
    @SerializedName("img")
    String img;
    @SerializedName("target")
    String target;
    @SerializedName("achieved")
    String achieved;
    @SerializedName("faulty")
    String faulty_vts ;
    @SerializedName("range")
    String range;
    @SerializedName("client")
    ArrayList<PlantDetails> plantdetail = new ArrayList<PlantDetails>();
    @SerializedName("data")
    ArrayList<ViewPlanDetail> view_plan = new ArrayList<ViewPlanDetail>();
    @SerializedName("current_month")
    ArrayList<IncentiveDetail> current_month = new ArrayList<IncentiveDetail>();
    @SerializedName("last_month")
    ArrayList<IncentiveDetail> last_month = new ArrayList<IncentiveDetail>();
    @SerializedName("site")
    ArrayList<CriticalSitesData> site_data = new ArrayList<CriticalSitesData>();
    @SerializedName("technicians_plan")
    ArrayList<AdminShowNextDayPlanDetail> show_plan = new ArrayList<AdminShowNextDayPlanDetail>();
    @SerializedName("log_status")
    ArrayList<LogStatusResponse> log_status = new ArrayList<LogStatusResponse>();
    @SerializedName("sales_client_data")
    ArrayList<SalesCustDetail> sales_cust_detail = new ArrayList<>();
    @SerializedName("sales_data")
    ArrayList<ViewEntryResponse> view_sales_data = new ArrayList<>();
    @SerializedName("bill_data")
    ArrayList<ViewEntryResponse> view_bill = new ArrayList<>();
    @SerializedName("order_data")
    ArrayList<OrderListDetail> order_list = new ArrayList<>();
    @SerializedName("sale_data")
    ArrayList<ViewEntryResponse> edit_sales_data = new ArrayList<>();
    @SerializedName("replace")
    ArrayList<ReplaceReasons> replaceReasons = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public String getImg() {
        return img;
    }

    public Integer getType() {
        return type;
    }

    public String getAchieved() { return achieved; }

    public String getTarget() { return target; }

    public String getFaulty_vts() {
        return faulty_vts;
    }

    public void setFaulty_vts(String faulty_vts) {
        this.faulty_vts = faulty_vts;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public ArrayList<PlantDetails> getPlantdetail() {
        return plantdetail;
    }

    public ArrayList<ViewPlanDetail> getView_plan() {
        return view_plan;
    }

    public ArrayList<IncentiveDetail> getCurrent_month() {
        return current_month;
    }

    public ArrayList<IncentiveDetail> getLast_month() {
        return last_month;
    }

    public ArrayList<CriticalSitesData> getSite_data() {
        return site_data;
    }

    public ArrayList<AdminShowNextDayPlanDetail> getShow_plan() {
        return show_plan;
    }

    public ArrayList<LogStatusResponse> getLog_status() {
        return log_status;
    }

    public ArrayList<SalesCustDetail> getSales_cust_detail() { return sales_cust_detail; }

    public ArrayList<ViewEntryResponse> getView_sales_data() { return view_sales_data; }

    public ArrayList<ViewEntryResponse> getView_bill() { return view_bill; }

    public ArrayList<OrderListDetail> getOrder_list() { return order_list; }

    public ArrayList<ViewEntryResponse> getEdit_sales_data() { return edit_sales_data; }

    public ArrayList<ReplaceReasons> getReplaceReasons() { return replaceReasons; }

}

