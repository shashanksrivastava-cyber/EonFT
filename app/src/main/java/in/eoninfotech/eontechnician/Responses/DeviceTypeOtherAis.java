package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DeviceTypeOtherAis {

    @SerializedName("id")
    String id;
    @SerializedName("name")
    String name;

    public String getName() { return name; }

    public String getId() { return id; }


}
