package in.eoninfotech.eontechnician.helper;

/**
 * Created by root on 7/7/16.
 */
/***************************************************************************/
// Copyright EON Infotech Ltd., unpublished work, created 2016.          //
// This computer program includes Confidential, Proprietary information  //
// and is a trade secret of EON Infotech Ltd. All use, disclosure and/or //
// reproduction is prohibited unless authorised in writing by an         //
// authorised officer of EON Infotech Ltd. All rights reserved.          //

import com.google.gson.annotations.SerializedName;

/**************************************************************************/
public class StockDetail {

    String clientid;
    @SerializedName("client_name")
    String clientname;
    @SerializedName("vts_working")
    String working_vts_qty;
    @SerializedName("vts_w_id")
    String working_vts_srno;
    @SerializedName("vts_faulty")
    String faulty_vts_qty;
    @SerializedName("vts_f_id")
    String faulty_vts_srno;
    @SerializedName("mtrs_7")
    String cable_7_meter;
    @SerializedName("mtrs_2")
    String cable_2_meter;
    @SerializedName("drs")
    String drum_sensor_qty;
    String drum_sensor_id;
    @SerializedName("mgt_set")
    String magnet_set;
    @SerializedName("location")
    String location;
    @SerializedName("remarks")
    String remarks;
    @SerializedName("drs_w_id")
    String drs_w_id;
    @SerializedName("drs_f_id")
    String drs_f_id;
    String technician_name;
    String technician_id;

    public String getTechnician_name() {
        return technician_name;
    }

    public void setTechnician_name(String name) {
        this.technician_name = name;
    }


    public String getTechnician_id() {
        return technician_id;
    }

    public void setTechnician_id(String name) {
        this.technician_id = name;
    }

    public String getDrum_sensor_id() {
        return drum_sensor_id;
    }

    public void setDrum_sensor_id(String name) {
        this.drum_sensor_id = name;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String name) {
        this.clientid = name;
    }

    public String getCable_2_meter() {
        return cable_2_meter;
    }

    public void setCable_2_meter(String name) {
        this.cable_2_meter = name;
    }

    public String getDrum_sensor_qty() {
        return drum_sensor_qty;
    }

    public void setDrum_sensor_qty(String designation) {
        this.drum_sensor_qty = designation;
    }

  public String getFaulty_vts_qty() {
        return faulty_vts_qty;
    }

    public void setFaulty_vts_qty(String mobile) {
        this.faulty_vts_qty = mobile;
    }


    public String getWorking_vts_srno() {
        return working_vts_srno;
    }

    public void setWorking_vts_srno(String name) {
        this.working_vts_srno = name;
    }


    public String getClientname() {
        return clientname;
    }

    public void setClientname(String name) {
        this.clientname = name;
    }

    public String getWorking_vts_qty() {
        return working_vts_qty;
    }

    public void setWorking_vts_qty(String designation) {
        this.working_vts_qty = designation;
    }

    public String getCable_7_meter() {
        return cable_7_meter;
    }

    public void setCable_7_meter(String mobile) {
        this.cable_7_meter = mobile;
    }


    public String getMagnet_set() {
        return magnet_set;
    }

    public void setMagnet_set(String mobile) {
        this.magnet_set = mobile;
    }

    public String getFaulty_vts_srno() {
        return faulty_vts_srno;
    }

    public void setFaulty_vts_srno(String name) {
        this.faulty_vts_srno = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String name) {
        this.location = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String name) {
        this.remarks = name;
    }

    public String getDrs_w_id() {
        return drs_w_id;
    }

    public void setDrs_w_id(String drs_w_id) {
        this.drs_w_id = drs_w_id;
    }

    public String getDrs_f_id() {
        return drs_f_id;
    }

    public void setDrs_f_id(String drs_f_id) {
        this.drs_f_id = drs_f_id;
    }
}