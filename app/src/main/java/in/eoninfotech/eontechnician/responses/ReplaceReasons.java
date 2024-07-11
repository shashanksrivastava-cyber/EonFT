package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/7/18.
 */

public class ReplaceReasons {

    @SerializedName("r_id")
    String r_id;
    @SerializedName("r_name")
    String r_name;

    public String getR_id() {
        return r_id;
    }

    public String getR_name() {
        return r_name;
    }
}
