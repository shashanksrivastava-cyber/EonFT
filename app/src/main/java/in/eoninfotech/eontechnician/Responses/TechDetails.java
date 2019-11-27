package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 10/11/18.
 */

public class TechDetails {

    @SerializedName("id_no")
    private String id_no;
    @SerializedName("name")
    private String name;
    @SerializedName("zone")
    private String zone;

    public String getId_no() {
        return id_no;
    }

    public void setId_no(String id_no) {
        this.id_no = id_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
