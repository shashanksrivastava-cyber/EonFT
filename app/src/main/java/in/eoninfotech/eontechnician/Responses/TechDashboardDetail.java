package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 1/11/18.
 */

public class TechDashboardDetail {

    @SerializedName("tot_dev")
    private int tot_dev;
    @SerializedName("tot_drs")
    private String tot_drs;
    @SerializedName("faulty_dev")
    private String faulty_dev;
    @SerializedName("faulty_drs")
    private int faulty_drs;
    @SerializedName("umain")
    private String umain;
    @SerializedName("cur_add")
    private int cur_add;
    @SerializedName("color")
    private String color;
    @SerializedName("add_21")
    private int add_21;
    @SerializedName("color21")
    private String color21;


    public int getTot_dev() {
        return tot_dev;
    }

    public void setTot_dev(int tot_dev) {
        this.tot_dev = tot_dev;
    }

    public String getTot_drs() {
        return tot_drs;
    }

    public void setTot_drs(String tot_drs) {
        this.tot_drs = tot_drs;
    }

    public String getFaulty_dev() {
        return faulty_dev;
    }

    public void setFaulty_dev(String faulty_dev) {
        this.faulty_dev = faulty_dev;
    }

    public int getFaulty_drs() {
        return faulty_drs;
    }

    public void setFaulty_drs(int faulty_drs) {
        this.faulty_drs = faulty_drs;
    }

    public String getUmain() {
        return umain;
    }

    public void setUmain(String umain) {
        this.umain = umain;
    }

    public int getCur_add() {
        return cur_add;
    }

    public void setCur_add(int cur_add) {
        this.cur_add = cur_add;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getAdd_21() {
        return add_21;
    }

    public void setAdd_21(int add_21) {
        this.add_21 = add_21;
    }

    public String getColor21() {
        return color21;
    }

    public void setColor21(String color21) {
        this.color21 = color21;
    }
}
