package in.eoninfotech.eontechnician.webservice;

import com.google.gson.annotations.SerializedName;

public class FaultsData {

    @SerializedName("name")
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
