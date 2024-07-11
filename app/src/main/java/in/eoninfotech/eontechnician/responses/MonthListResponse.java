package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 9/3/19.
 */

public class MonthListResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("months")
    ArrayList<MonthDetail> monthDetail = new ArrayList<MonthDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<MonthDetail> getMonthDetail() {
        return monthDetail;
    }

    public void setMonthDetail(ArrayList<MonthDetail> monthDetail) {
        this.monthDetail = monthDetail;
    }
}
