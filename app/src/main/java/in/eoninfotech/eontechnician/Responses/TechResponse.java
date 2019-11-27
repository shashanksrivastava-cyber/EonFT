package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 10/11/18.
 */

public class TechResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("tech")
    ArrayList<TechDetails> techList = new ArrayList<TechDetails>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<TechDetails> gettechList() {
        return techList;
    }

    public void settechList(ArrayList<TechDetails> techList) {
        this.techList = techList;
    }
}
