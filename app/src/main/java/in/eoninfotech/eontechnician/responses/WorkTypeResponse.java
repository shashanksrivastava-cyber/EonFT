package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 12/9/18.
 */

public class WorkTypeResponse implements Serializable {

    @SerializedName("type")
    Integer type;
    @SerializedName("activites")
    ArrayList<WorkTypeDetail> worktypeList = new ArrayList<WorkTypeDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<WorkTypeDetail> getWorktypeList() {
        return worktypeList;
    }

    public void setWorktypeList(ArrayList<WorkTypeDetail> worktypeList) {
        this.worktypeList = worktypeList;
    }
}
