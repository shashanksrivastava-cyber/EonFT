package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by root on 19/9/18.
 */

public class RemovalList implements Serializable {

    @SerializedName("r_id")
    private int removal_Id;
    @SerializedName("r_name")
    private String removal_Name;

    public int getRemoval_Id() {
        return removal_Id;
    }

    public void setRemoval_Id(int removal_Id) {
        this.removal_Id = removal_Id;
    }

    public String getRemoval_Name() {
        return removal_Name;
    }

    public void setRemoval_Name(String removal_Name) {
        this.removal_Name = removal_Name;
    }
}
