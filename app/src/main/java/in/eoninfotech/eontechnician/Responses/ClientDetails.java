package in.eoninfotech.eontechnician.Responses;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by root on 12/9/18.
 */

 public class ClientDetails implements Serializable {

    @SerializedName("c_id")
    private int client_Id;
    @SerializedName("c_name")
    private String client_Name;
    @SerializedName("c_drs")
    private int drs_status;
    @SerializedName("c_port")
    private int client_Port;

    @SerializedName("server_name")
    private String server_name;

    @SerializedName("db_name")
    private String db_name;

    @SerializedName("id_dist")
    private String id_dist;

    public int getClient_Id() {
        return client_Id;
    }

    public void setClient_Id(int client_Id) {
        this.client_Id = client_Id;
    }


    public String getClient_Name() {
        return client_Name;
    }

    public void setClient_Name(String client_Name) {
        this.client_Name = client_Name;
    }

    public int getDrs_status() {
        return drs_status;
    }

    public void setDrs_status(int drs_status) {
        this.drs_status = drs_status;
    }

    public int getClient_Port() {
        return client_Port;
    }

    public void setClient_Port(int client_Port) {
        this.client_Port = client_Port;
    }

    public String getServer_name() {
        return server_name;
    }

    public void setServer_name(String server_name) {
        this.server_name = server_name;
    }

    public String getDb_name() {
        return db_name;
    }

    public void setDb_name(String db_name) {
        this.db_name = db_name;
    }

    public String getId_dist() {
        return id_dist;
    }

    public void setId_dist(String id_dist) {
        this.id_dist = id_dist;
    }
}
