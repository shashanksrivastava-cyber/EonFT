package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 19/9/18.
 */

public class RemovalResponse implements Serializable {
    @SerializedName("type")
    Integer type;
    @SerializedName("removal")
    ArrayList<RemovalList> removalList = new ArrayList<RemovalList>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<RemovalList> getRemovalList() {
        return removalList;
    }

    public void setRemovalList(ArrayList<RemovalList> removalList) {
        this.removalList = removalList;
    }
}
