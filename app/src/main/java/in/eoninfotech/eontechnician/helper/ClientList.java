package in.eoninfotech.eontechnician.helper;
//*********************************************************************************************
//Copyright EON Infotech Ltd., unpublished work, created Aug 2012.
//This computer program includes Confidential, Proprietary Information
//and is
//a Trade Secret of EON Infotech Ltd. All use, disclosure, and/or
//reproduction
//is prohibited unless authorised in writing by an authorised officer of EON Infotech Ltd.
//All Rights Reserved.
//********************************************************************************************
//SITE           : EON Infotech Ltd., C-180, Phase 8B, Ind. Area, Mohali
//PROJECT        : CTU
//TITLE          : CTU
//FILE           : StopList.java
//Original Author: Harman tj
//Created Date   : Oct,2016
//Revision       :
//
//Revision History
//+-------+--------+-------+----------------------------+-----------+----------+
//|  SNo. |  Date  |   By  |    Changes made/Ticket No. |  Remarks  |New RevNo.|
//+-------+--------+-------+----------------------------+-----------+----------+
//
//
//*********************************************************************************************
//
//DESCRIPTION    :   This class contains all the getter and setter method.
//
//*********************************************************************************************

/**
 * Created by harman on 12/10/16.
 */
public class ClientList {

    private String clientid;
    private String clientname;
    String drs_status;
    String timee;
    String taskk;

    public String getDrs_status() {
        return drs_status;
    }

    public void setDrs_status(String name) {
        this.drs_status = name;
    }

    public String getTimee() {
        return timee;
    }

    public void setTimee(String name) {
        this.timee = name;
    }


    public String getTaskk() {
        return taskk;
    }

    public void setTaskk(String name) {
        this.taskk = name;
    }


    public String getClientid() {
        return clientid;
    }


    public void setClientid(String bus) {
        this.clientid = bus;
    }


    public String getClientname() {
        return clientname;
    }


    public void setClientname(String type) {
        this.clientname = type;
    }

}
