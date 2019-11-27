package in.eoninfotech.eontechnician.webservice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 9/5/19.
 */

public class MessageDetail {

    @SerializedName("id")
    private String id;
    @SerializedName("msg_type")
    private String msg_type;
    @SerializedName("cust_id")
    private String cust_id;
    @SerializedName("cust_name")
    private String cust_name;
    @SerializedName("loc_id")
    private String loc_id;
    @SerializedName("loc_name")
    private String loc_name;
    @SerializedName("title")
    private String title;
    @SerializedName("message")
    private String message;
    @SerializedName("date-time")
    private String datetime;
    @SerializedName("status")
    private String status;
    @SerializedName("sender")
    private String sender;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getLoc_id() {
        return loc_id;
    }

    public void setLoc_id(String loc_id) {
        this.loc_id = loc_id;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getLoc_name() {
        return loc_name;
    }

    public void setLoc_name(String loc_name) {
        this.loc_name = loc_name;
    }
}
