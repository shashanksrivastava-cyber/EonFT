package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

public class MainClientList {

    @SerializedName("c_id")
    private int client_Id;
    @SerializedName("c_name")
    private String client_Name;
    @SerializedName("db")
    private String db;
    @SerializedName("server")
    private String server;


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

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
