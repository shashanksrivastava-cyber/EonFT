package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 16/4/18.
 */

public class OrderListDetail {
    @SerializedName("order_id")
    String id;
    @SerializedName("order_no")
    String order_no;
    @SerializedName("sale_id")
    String sale_id;

    public String getSale_id() { return sale_id; }

    public String getId() {
        return id;
    }

    public String getOrder_no() {
        return order_no;
    }

}
