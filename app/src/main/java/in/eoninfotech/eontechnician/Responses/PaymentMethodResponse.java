package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 15/1/19.
 */

public class PaymentMethodResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("methods")
    ArrayList<PMethodDetail> methods = new ArrayList<PMethodDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<PMethodDetail> getMethods() {
        return methods;
    }

    public void setMethods(ArrayList<PMethodDetail> methods) {
        this.methods = methods;
    }
}
