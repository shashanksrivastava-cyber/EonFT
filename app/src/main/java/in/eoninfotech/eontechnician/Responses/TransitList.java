package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

public class TransitList {


    @SerializedName("transit_id")
    String transit_id;
    @SerializedName("transit_name")
    String transit_name;

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
}
