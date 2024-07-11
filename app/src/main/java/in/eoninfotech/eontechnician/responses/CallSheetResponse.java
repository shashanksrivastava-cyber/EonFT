package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.helper.CallSheetDetail;

/**
 * Created by root on 13/10/17.
 */
public class CallSheetResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("sheets")
    ArrayList<CallSheetDetail> callsheet_list = new ArrayList<CallSheetDetail>();

    public Integer getType() {
        return type;
    }

    public ArrayList<CallSheetDetail> getCallsheet_list() {
        return callsheet_list;
    }
}
