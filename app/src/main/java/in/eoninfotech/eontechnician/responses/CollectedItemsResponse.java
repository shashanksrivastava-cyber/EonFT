package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 25/9/18.
 */

public class CollectedItemsResponse implements Serializable {
    @SerializedName("type")
    Integer type;
    @SerializedName("items")
    ArrayList<ItemList> itemLists = new ArrayList<ItemList>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<ItemList> getItemLists() {
        return itemLists;
    }

    public void setItemLists(ArrayList<ItemList> itemLists) {
        this.itemLists = itemLists;
    }
}
