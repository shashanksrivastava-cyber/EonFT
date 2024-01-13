package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 19/9/18.
 */

public class VTSResponse implements Serializable {

    @SerializedName("type")
    Integer type;
//        @SerializedName("vts-rec")
//        ArrayList<VTSDetail> vtsDetails = new ArrayList<VTSDetail>();

    @SerializedName("vts-rec1")
    ArrayList<VTSDetail> vtsDetails = new ArrayList<VTSDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<VTSDetail> getVtsDetails() {
        return vtsDetails;
    }

    public void setVtsDetails(ArrayList<VTSDetail> vtsDetails) {
        this.vtsDetails = vtsDetails;
    }


//    public ArrayList<VTSDetail1> getVtsDetails() {
//        return vtsDetails;
//    }
//
//    public void setVtsDetails(ArrayList<VTSDetail1> vtsDetails) {
//        this.vtsDetails = vtsDetails;
//    }
}
