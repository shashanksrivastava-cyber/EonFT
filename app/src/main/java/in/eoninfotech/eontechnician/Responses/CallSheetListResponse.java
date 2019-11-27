package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.helper.CallSheetDetail;

/**
 * Created by root on 7/3/19.
 */

public class CallSheetListResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("sheets")
    ArrayList<CallSheetListDetail> callsheetlist = new ArrayList<CallSheetListDetail>();

    public Integer getType() {
        return type;
    }

    public ArrayList<CallSheetListDetail> getCallsheetlist() {
        return callsheetlist;
    }

    public void setCallsheetlist(ArrayList<CallSheetListDetail> callsheetlist) {
        this.callsheetlist = callsheetlist;
    }
}
