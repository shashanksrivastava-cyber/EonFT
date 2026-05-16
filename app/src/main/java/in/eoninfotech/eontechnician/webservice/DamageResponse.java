package in.eoninfotech.eontechnician.webservice;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import in.eoninfotech.eontechnician.responses.RemovalList;

public class DamageResponse implements Serializable {

    @SerializedName("type")
    Integer type;
    @SerializedName("removal")
    ArrayList<DamageList> damageLists = new ArrayList<DamageList>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<DamageList> getDamageLists() {
        return damageLists;
    }

    public void setDamageLists(ArrayList<DamageList> damageLists) {
        this.damageLists = damageLists;
    }
}
