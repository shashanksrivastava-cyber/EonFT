package in.eoninfotech.eontechnician.webservice;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DamageList implements Serializable {

    @SerializedName("r_id")
    private int damage_Id;
    @SerializedName("r_name")
    private String damage_Name;

    public int getDamage_Id() {
        return damage_Id;
    }

    public void setDamage_Id(int damage_Id) {
        this.damage_Id = damage_Id;
    }

    public String getDamage_Name() {
        return damage_Name;
    }

    public void setDamage_Name(String damage_Name) {
        this.damage_Name = damage_Name;
    }
}
