package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

public class AdditionalAccessoryDetails {

    @SerializedName("item_name")
    String item_name;
    @SerializedName("quantity")
    String quantity;

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
