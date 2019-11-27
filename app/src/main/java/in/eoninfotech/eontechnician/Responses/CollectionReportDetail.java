package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 13/3/19.
 */

public class CollectionReportDetail {

    @SerializedName("customer_name")
    String customer_name;
    @SerializedName("customer_location")
    String customer_location;
    @SerializedName("collection_method")
    String collection_method;
    @SerializedName("collection_date")
    String collection_date;
    @SerializedName("collection_amount")
    String collection_amount;
    @SerializedName("verified_status")
    String verified_status;

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_location() {
        return customer_location;
    }

    public void setCustomer_location(String customer_location) {
        this.customer_location = customer_location;
    }

    public String getCollection_method() {
        return collection_method;
    }

    public void setCollection_method(String collection_method) {
        this.collection_method = collection_method;
    }

    public String getCollection_date() {
        return collection_date;
    }

    public void setCollection_date(String collection_date) {
        this.collection_date = collection_date;
    }

    public String getCollection_amount() {
        return collection_amount;
    }

    public void setCollection_amount(String collection_amount) {
        this.collection_amount = collection_amount;
    }

    public String getVerified_status() {
        return verified_status;
    }

    public void setVerified_status(String verified_status) {
        this.verified_status = verified_status;
    }
}
