package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 21/9/18.
 */

public class CollectedItems {
    @SerializedName("type")
    Integer type;
    @SerializedName("items")
    ArrayList<CollectedItemsDetail> collectedItemsDetails = new ArrayList<CollectedItemsDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<CollectedItemsDetail> getCollectedItemsDetails() {
        return collectedItemsDetails;
    }

    public void setCollectedItemsDetails(ArrayList<CollectedItemsDetail> collectedItemsDetails) {
        this.collectedItemsDetails = collectedItemsDetails;
    }
}
