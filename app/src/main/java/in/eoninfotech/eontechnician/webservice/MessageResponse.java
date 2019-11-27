package in.eoninfotech.eontechnician.webservice;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.helper.ListIncentiveDetail;

/**
 * Created by root on 9/5/19.
 */

public class MessageResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("messages")
    ArrayList<MessageDetail> msg_list = new ArrayList<MessageDetail>();
    @SerializedName("msg_count")
    String msg_count;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<MessageDetail> getMsg_list() {
        return msg_list;
    }

    public void setMsg_list(ArrayList<MessageDetail> msg_list) {
        this.msg_list = msg_list;
    }

    public String getMsg_count() {
        return msg_count;
    }

    public void setMsg_count(String msg_count) {
        this.msg_count = msg_count;
    }
}
