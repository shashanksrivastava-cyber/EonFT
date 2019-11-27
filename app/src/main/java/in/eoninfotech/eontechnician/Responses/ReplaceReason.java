package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 19/9/18.
 */

public class ReplaceReason implements Serializable {
    @SerializedName("type")
    Integer type;
    @SerializedName("replace")
    ArrayList<ReplaceReasonDetail> replaceList = new ArrayList<ReplaceReasonDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<ReplaceReasonDetail> getReplaceList() {
        return replaceList;
    }

    public void setReplaceList(ArrayList<ReplaceReasonDetail> replaceList) {
        this.replaceList = replaceList;
    }
}

