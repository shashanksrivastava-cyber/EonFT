package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 9/3/19.
 */

public class YearListResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("years")
    ArrayList<YearDetail> yearDetail = new ArrayList<YearDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<YearDetail> getYearDetail() {
        return yearDetail;
    }

    public void setYearDetail(ArrayList<YearDetail> yearDetail) {
        this.yearDetail = yearDetail;
    }
}
