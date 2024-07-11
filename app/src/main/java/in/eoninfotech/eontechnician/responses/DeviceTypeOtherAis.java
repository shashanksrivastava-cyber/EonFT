package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

public class DeviceTypeOtherAis {

    @SerializedName("id")
    String id;
    @SerializedName("name")
    String name;

    public String getName() { return name; }

    public String getId() { return id; }


}
