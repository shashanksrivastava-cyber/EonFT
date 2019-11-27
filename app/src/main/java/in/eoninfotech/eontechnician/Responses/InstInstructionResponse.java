package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 30/11/18.
 */

public class InstInstructionResponse {

    @SerializedName("type")
    Integer type;
    @SerializedName("message")
    ArrayList<InstructionDetail> instDetail = new ArrayList<InstructionDetail>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<InstructionDetail> getInstDetail() {
        return instDetail;
    }

    public void setInstDetail(ArrayList<InstructionDetail> instDetail) {
        this.instDetail = instDetail;
    }
}
