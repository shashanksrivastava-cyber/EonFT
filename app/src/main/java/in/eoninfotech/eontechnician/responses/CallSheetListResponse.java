package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 7/3/19.
 */

public class CallSheetListResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("sheets")
    ArrayList<CallSheetListDetail> callsheetlist = new ArrayList<CallSheetListDetail>();

    @SerializedName("list")
    ArrayList<CallSheetListDetail> add_material_list = new ArrayList<CallSheetListDetail>();

    public Integer getType() {
        return type;
    }

    public ArrayList<CallSheetListDetail> getCallsheetlist() {
        return callsheetlist;
    }

    public void setCallsheetlist(ArrayList<CallSheetListDetail> callsheetlist) {
        this.callsheetlist = callsheetlist;
    }

    public ArrayList<CallSheetListDetail> getAdd_material_list() {
        return add_material_list;
    }

    public void setAdd_material_list(ArrayList<CallSheetListDetail> add_material_list) {
        this.add_material_list = add_material_list;
    }
}
