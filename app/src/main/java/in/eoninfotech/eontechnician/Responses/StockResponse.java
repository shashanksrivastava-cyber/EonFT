package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.helper.StockDetail;

/**
 * Created by root on 14/10/17.
 */
public class StockResponse {
    @SerializedName("type")
    Integer type;
    @SerializedName("data")

    ArrayList<StockDetail> stock_list = new ArrayList<StockDetail>();


    public Integer getType() {
        return type;
    }

    public ArrayList<StockDetail> getStock_list() {
        return stock_list;
    }
}
