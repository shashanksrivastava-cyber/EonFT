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
    @SerializedName("umain_work")
    private String umain_work;
    @SerializedName("opr_per")
    private String opr_per;
    @SerializedName("cur_add")
    private int cur_add;
    @SerializedName("color")
    private String color;
    @SerializedName("add_21")
    private int add_21;
    @SerializedName("color21")
    private String color21;
    @SerializedName("tot_sos")
    private String tot_sos;
    @SerializedName("tot_lid")
    private String tot_lid;
    @SerializedName("tot_fuel")
    private String tot_fuel;
    @SerializedName("tot_temp")
    private String tot_temp;
    @SerializedName("faulty_sos")
    private String faulty_sos;
    @SerializedName("faulty_lid")
    private String faulty_lid;
    @SerializedName("faulty_fuel")
    private String faulty_fuel;
    @SerializedName("faulty_temp")
    private String faulty_temp;


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

    public String getUmain_work() {
        return umain_work;
    }

    public void setUmain_work(String umain_work) {
        this.umain_work = umain_work;
    }

    public String getTot_sos() {
        return tot_sos;
    }

    public void setTot_sos(String tot_sos) {
        this.tot_sos = tot_sos;
    }

    public String getTot_lid() {
        return tot_lid;
    }

    public void setTot_lid(String tot_lid) {
        this.tot_lid = tot_lid;
    }

    public String getTot_fuel() {
        return tot_fuel;
    }

    public void setTot_fuel(String tot_fuel) {
        this.tot_fuel = tot_fuel;
    }

    public String getTot_temp() {
        return tot_temp;
    }

    public void setTot_temp(String tot_temp) {
        this.tot_temp = tot_temp;
    }

    public String getFaulty_sos() {
        return faulty_sos;
    }

    public void setFaulty_sos(String faulty_sos) {
        this.faulty_sos = faulty_sos;
    }

    public String getFaulty_lid() {
        return faulty_lid;
    }

    public void setFaulty_lid(String faulty_lid) {
        this.faulty_lid = faulty_lid;
    }

    public String getFaulty_fuel() {
        return faulty_fuel;
    }

    public void setFaulty_fuel(String faulty_fuel) {
        this.faulty_fuel = faulty_fuel;
    }

    public String getFaulty_temp() {
        return faulty_temp;
    }

    public void setFaulty_temp(String faulty_temp) {
        this.faulty_temp = faulty_temp;
    }

    public String getOpr_per() {
        return opr_per;
    }

    public void setOpr_per(String opr_per) {
        this.opr_per = opr_per;
    }
}
