package in.eoninfotech.eontechnician.webservice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 13/12/18.
 */

 public class AttResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("msg")
    String msg;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
