package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 20/11/18.
 */

public class SimOperatorDetail {

    @SerializedName("sp_id")
    String sp_id;
    @SerializedName("sp_name")
    String sp_name;

    public String getSp_id() {
        return sp_id;
    }

    public void setSp_id(String sp_id) {
        this.sp_id = sp_id;
    }

    public String getSp_name() {
        return sp_name;
    }

    public void setSp_name(String sp_name) {
        this.sp_name = sp_name;
    }
}
