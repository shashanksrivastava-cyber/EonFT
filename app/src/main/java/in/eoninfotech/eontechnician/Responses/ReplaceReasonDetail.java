package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by root on 19/9/18.
 */

public class ReplaceReasonDetail implements Serializable {

    @SerializedName("r_id")
    private int replace_Id;
    @SerializedName("r_name")
    private String replace_Name;

    public int getReplace_Id() {
        return replace_Id;
    }

    public void setReplace_Id(int replace_Id) {
        this.replace_Id = replace_Id;
    }

    public String getReplace_Name() {
        return replace_Name;
    }

    public void setReplace_Name(String replace_Name) {
        this.replace_Name = replace_Name;
    }
}
