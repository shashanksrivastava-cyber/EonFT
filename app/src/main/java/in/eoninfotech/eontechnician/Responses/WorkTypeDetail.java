package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by root on 12/9/18.
 */

public class WorkTypeDetail implements Serializable {

    @SerializedName("id")
    private int work_Id;
    @SerializedName("name")
    private String work_Name;

    public int getWork_Id() {
        return work_Id;
    }

    public void setWork_Id(int work_Id) {
        this.work_Id = work_Id;
    }

    public String getWork_Name() {
        return work_Name;
    }

    public void setWork_Name(String work_Name) {
        this.work_Name = work_Name;
    }
}
