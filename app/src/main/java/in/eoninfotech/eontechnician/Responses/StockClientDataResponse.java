package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 7/1/19.
 */

public class StockClientDataResponse {

    private String vts_w_id;

    private String vts_wrk;

    private String y_cable;

    private String mtrs_2;

    private String remarks;

    private String mgt_set;

    private String drs_id;

    private String drs;

    private String mtrs_7;

    private String vts_falt;

    private String vts_f_id;

    private String drs_f_id;

    public String getVts_w_id ()
    {
        return vts_w_id;
    }

    public void setVts_w_id (String vts_w_id)
    {
        this.vts_w_id = vts_w_id;
    }

    public String getVts_wrk ()
    {
        return vts_wrk;
    }

    public void setVts_wrk (String vts_wrk)
    {
        this.vts_wrk = vts_wrk;
    }

    public String getY_cable ()
    {
        return y_cable;
    }

    public void setY_cable (String y_cable)
    {
        this.y_cable = y_cable;
    }

    public String getMtrs_2 ()
    {
        return mtrs_2;
    }

    public void setMtrs_2 (String mtrs_2)
    {
        this.mtrs_2 = mtrs_2;
    }

    public String getRemarks ()
    {
        return remarks;
    }

    public void setRemarks (String remarks)
    {
        this.remarks = remarks;
    }

    public String getMgt_set ()
    {
        return mgt_set;
    }

    public void setMgt_set (String mgt_set)
    {
        this.mgt_set = mgt_set;
    }

    public String getDrs_id ()
    {
        return drs_id;
    }

    public void setDrs_id (String drs_id)
    {
        this.drs_id = drs_id;
    }

    public String getDrs ()
    {
        return drs;
    }

    public void setDrs (String drs)
    {
        this.drs = drs;
    }

    public String getMtrs_7 ()
    {
        return mtrs_7;
    }

    public void setMtrs_7 (String mtrs_7)
    {
        this.mtrs_7 = mtrs_7;
    }

    public String getVts_falt ()
    {
        return vts_falt;
    }

    public void setVts_falt (String vts_falt)
    {
        this.vts_falt = vts_falt;
    }

    public String getVts_f_id ()
    {
        return vts_f_id;
    }

    public void setVts_f_id (String vts_f_id)
    {
        this.vts_f_id = vts_f_id;
    }

    public String getDrs_f_id() {
        return drs_f_id;
    }

    public void setDrs_f_id(String drs_f_id) {
        this.drs_f_id = drs_f_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [vts_w_id = "+vts_w_id+", vts_wrk = "+vts_wrk+", y_cable = "+y_cable+", mtrs_2 = "+mtrs_2+", remarks = "+remarks+", mgt_set = "+mgt_set+", drs_id = "+drs_id+", drs = "+drs+", mtrs_7 = "+mtrs_7+", vts_falt = "+vts_falt+", vts_f_id = "+vts_f_id+"]";
    }
}
