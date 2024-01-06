package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TransitList {

    @SerializedName("transit_id")
    String transit_id;
    @SerializedName("transit_name")
    String transit_name;

    @SerializedName("detail_array")
    ArrayList<ItemList> detail_array = new ArrayList<>();

    public String getTransit_id() {
        return transit_id;
    }

    public void setTransit_id(String transit_id) {
        this.transit_id = transit_id;
    }

    public String getTransit_name() {
        return transit_name;
    }

    public void setTransit_name(String transit_name) {
        this.transit_name = transit_name;
    }

    public ArrayList<ItemList> getDetail_array() {
        return detail_array;
    }

    public void setDetail_array(ArrayList<ItemList> detail_array) {
        this.detail_array = detail_array;
    }
}
