package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.helper.IncentiveDetail;
import in.eoninfotech.eontechnician.helper.ListIncentiveDetail;

/**
 * Created by root on 13/10/17.
 */
public class IncentiveResponse {
    @SerializedName("type")
    Integer type;
    @SerializedName("data")
    ArrayList<ListIncentiveDetail> incentv_list = new ArrayList<ListIncentiveDetail>();

    public Integer getType() {
        return type;
    }

    public ArrayList<ListIncentiveDetail> getIncentv_list() {
        return incentv_list;
    }


}
