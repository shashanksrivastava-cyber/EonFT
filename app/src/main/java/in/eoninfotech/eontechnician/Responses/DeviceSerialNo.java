package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DeviceSerialNo {

    @SerializedName("new_sr_no")
    ArrayList<SrNoDeviceList> new_sr_no = new ArrayList<>();

    @SerializedName("old_sr_no")
    ArrayList<SrNoDeviceList> old_sr_no = new ArrayList<>();

    public ArrayList<SrNoDeviceList> getNew_sr_no() {
        return new_sr_no;
    }

    public void setNew_sr_no(ArrayList<SrNoDeviceList> new_sr_no) {
        this.new_sr_no = new_sr_no;
    }

    public ArrayList<SrNoDeviceList> getOld_sr_no() {
        return old_sr_no;
    }

    public void setOld_sr_no(ArrayList<SrNoDeviceList> old_sr_no) {
        this.old_sr_no = old_sr_no;
    }
}
