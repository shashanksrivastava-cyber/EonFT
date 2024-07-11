package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by root on 28/9/18.
 */

public class DisconnectionDetail implements Serializable {

    @SerializedName("id")
    Integer id;
    @SerializedName("name")
    String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
