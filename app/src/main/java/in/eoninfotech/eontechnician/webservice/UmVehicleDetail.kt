package in.eoninfotech.eontechnician.webservice;

import com.google.gson.annotations.SerializedName;

public class UmVehicleDetail {

    @SerializedName("id")
    String id;
    @SerializedName("reg_no")
    String reg_no;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }
}
