package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 16/10/18.
 */

public class ActivityResponse implements Serializable {
    @SerializedName("type")
    Integer type;
    @SerializedName("data")
    ArrayList<ActivityDetailResponse> activityList = new ArrayList<ActivityDetailResponse>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<ActivityDetailResponse> getActivityList() {
        return activityList;
    }

    public void setActivityList(ArrayList<ActivityDetailResponse> activityList) {

        this.activityList = activityList;
    }
}
