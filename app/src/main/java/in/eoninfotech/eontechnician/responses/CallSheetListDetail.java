package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 7/3/19.
 */

public class CallSheetListDetail {

    @SerializedName("date")
    private String date;
    @SerializedName("image")
    private String image;
    @SerializedName("remarks")
    private String remarks;
    @SerializedName("id")
    private String id;
    @SerializedName("req_no")
    private String req_no;
    @SerializedName("tent_date")
    private String tent_date;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReq_no() {
        return req_no;
    }

    public void setReq_no(String req_no) {
        this.req_no = req_no;
    }

    public String getTent_date() {
        return tent_date;
    }

    public void setTent_date(String tent_date) {
        this.tent_date = tent_date;
    }
}
