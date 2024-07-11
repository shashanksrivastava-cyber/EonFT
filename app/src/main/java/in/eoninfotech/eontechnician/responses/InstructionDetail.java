package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 30/11/18.
 */

public class InstructionDetail {

    @SerializedName("msg_id")
    private String msg_id;
    @SerializedName("cust_name")
    private String cust_name;
    @SerializedName("loc_name")
    private String loc_name;
    @SerializedName("date")
    private String date;
    @SerializedName("sender")
    private String sender;
    @SerializedName("title")
    private String title;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
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
