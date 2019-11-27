package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 13/3/19.
 */

public class CollectionReportResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("total_amount")
    String total_amount;
    @SerializedName("data")
    ArrayList<CollectionReportDetail> collectionReportDetails = new ArrayList<CollectionReportDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public ArrayList<CollectionReportDetail> getCollectionReportDetails() {
        return collectionReportDetails;
    }

    public void setCollectionReportDetails(ArrayList<CollectionReportDetail> collectionReportDetails) {
        this.collectionReportDetails = collectionReportDetails;
    }
}
