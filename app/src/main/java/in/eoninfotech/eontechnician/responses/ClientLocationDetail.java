package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by root on 12/9/18.
 */

public class ClientLocationDetail implements Serializable {

    @SerializedName("loc_id")
    private int loc_Id;
    @SerializedName("loc_name")
    private String loc_Name;
    @SerializedName("loc_address")
    private String loc_address;

    public int getLoc_Id() {
        return loc_Id;
    }

    public void setLoc_Id(int loc_Id) {
        this.loc_Id = loc_Id;
    }

    public String getLoc_Name() {
        return loc_Name;
    }

    public void setLoc_Name(String loc_Name) {
        this.loc_Name = loc_Name;
    }

    public String getLoc_address() {
        return loc_address;
    }

    public void setLoc_address(String loc_address) {
        this.loc_address = loc_address;
    }
}
