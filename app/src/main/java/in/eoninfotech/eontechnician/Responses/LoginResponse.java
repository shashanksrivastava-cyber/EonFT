package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 2/11/18.
 */

public class LoginResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("logind")
    ArrayList<LoginDetail> loginDetails = new ArrayList<LoginDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<LoginDetail> getLoginDetails() {
        return loginDetails;
    }

    public void setLoginDetails(ArrayList<LoginDetail> loginDetails) {
        this.loginDetails = loginDetails;
    }
}
