package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 6/4/18.
 */

public class ViewEntryResponse {
    @SerializedName("sale_id")
    String sale_id;
    @SerializedName("date")//
            String date;
    @SerializedName("order_no")//
            String order_no;
    @SerializedName("install_type")//
            String install_type;
    @SerializedName("duration")
    String duration;
    @SerializedName("cust_type")//
            String cust_type;
    @SerializedName("cust_name")//
            String cust_name;
    @SerializedName("cust_street_name")//
            String cust_street_name;
    @SerializedName("cust_city")//
            String cust_city;
    @SerializedName("cust_office_number")//
            String cust_office_number;
    @SerializedName("cust_district")//
            String cust_district;
    @SerializedName("cust_state")//
            String cust_state;
    @SerializedName("cust_pincode")//
            String cust_pincode;
    @SerializedName("cust_p_name")//
            String cust_p_name;
    @SerializedName("cust_p_number")//
            String cust_p_number;
    @SerializedName("cust_p_id")//
            String cust_p_id;
    @SerializedName("cust_s_name")//
            String cust_s_name;
    @SerializedName("cust_s_number")//
            String cust_s_number;
    @SerializedName("cust_s_id")//
            String cust_s_id;
    @SerializedName("install_street_name")
    String install_street_name;
    @SerializedName("install_city")
    String install_city;
    @SerializedName("install_office_number")
    String install_office_number;
    @SerializedName("install_district")
    String install_district;
    @SerializedName("install_state")
    String install_state;
    @SerializedName("install_pincode")
    String install_pincode;
    @SerializedName("vehicle_type")
    String vehicle_type;
    @SerializedName("battery_voltage")
    String battery_voltage;
    @SerializedName("vts_quantity")
    String vts_quantity;
    @SerializedName("accessory")
    String accessory;
    @SerializedName("drs_quantity")
    String drs_quantity;
    @SerializedName("fuel_sensor_quantity")
    String fuel_sensor_quantity;
    @SerializedName("door_sensor_quantity")
    String door_sensor_quantity;
    @SerializedName("high_count")
    String high_count;
    @SerializedName("low_count")
    String low_count;
    @SerializedName("normal_count")
    String normal_count;
    @SerializedName("priority_reason")
    String priority_reason;
    @SerializedName("ignition")
    String ignition;
    @SerializedName("remarks")//
            String remarks;


    @SerializedName("sales_person_name")
    String sales_person_name;
    @SerializedName("units_billed")
    String units_billed;
    @SerializedName("billing_date")
    String billing_date;
    @SerializedName("hardware_with_voice_price")
    String hardware_price;
    @SerializedName("monthly_recurring_price")
    String monthly_recurring_price;
    @SerializedName("setup_charges")
    String setup_charges;
    @SerializedName("installation_price")
    String installation_price;
    @SerializedName("accessories_price")
    String accessories_price;
    @SerializedName("other_price")
    String other_price;
    @SerializedName("hardware_with_voice_tax")
    String hardware_tax;
    @SerializedName("monthly_recurring_tax")
    String monthly_recurring_tax;
    @SerializedName("setup_charges_tax")
    String setup_charges_tax;
    @SerializedName("installation_tax")
    String installation_tax;
    @SerializedName("accessories_tax")
    String accessories_tax;
    @SerializedName("other_tax")
    String other_tax;
    @SerializedName("amc_charges")
    String amc_charges;
    @SerializedName("start_date_amc")
    String start_date_amc;
    @SerializedName("sales_person")
    String sales_person;
    @SerializedName("gst_no")
    String gst_no;
    @SerializedName("pan_no")
    String pan_no;
    @SerializedName("hardware_without_voice_price")
    String hardware_without_voice_price;
    @SerializedName("hardware_without_voice_tax")
    String hardware_without_voice_tax;
    @SerializedName("hardware_pump_price")
    String hardware_pump_price;
    @SerializedName("hardware_pump_tax")
    String hardware_pump_tax;

    public String getDate() {
        return date;
    }

    public String getOrder_no() {
        return order_no;
    }

    public String getInstall_type() {
        return install_type;
    }

    public String getDuration() {
        return duration;
    }

    public String getCust_type() {
        return cust_type;
    }

    public String getCust_name() {
        return cust_name;
    }

    public String getCust_street_name() {
        return cust_street_name;
    }

    public String getCust_city() {
        return cust_city;
    }

    public String getCust_office_number() {
        return cust_office_number;
    }

    public String getCust_district() {
        return cust_district;
    }

    public String getCust_state() {
        return cust_state;
    }

    public String getCust_pincode() {
        return cust_pincode;
    }

    public String getCust_p_name() {
        return cust_p_name;
    }

    public String getCust_p_number() {
        return cust_p_number;
    }

    public String getCust_p_id() {
        return cust_p_id;
    }

    public String getCust_s_name() {
        return cust_s_name;
    }

    public String getCust_s_number() {
        return cust_s_number;
    }

    public String getCust_s_id() {
        return cust_s_id;
    }

    public String getInstall_street_name() {
        return install_street_name;
    }

    public String getInstall_city() {
        return install_city;
    }

    public String getInstall_office_number() {
        return install_office_number;
    }

    public String getInstall_district() {
        return install_district;
    }

    public String getInstall_state() {
        return install_state;
    }

    public String getInstall_pincode() {
        return install_pincode;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public String getBattery_voltage() {
        return battery_voltage;
    }

    public String getVts_quantity() {
        return vts_quantity;
    }

    public String getAccessory() {
        return accessory;
    }

    public String getDrs_quantity() {
        return drs_quantity;
    }

    public String getFuel_sensor_quantity() {
        return fuel_sensor_quantity;
    }

    public String getHigh_count() {
        return high_count;
    }

    public String getLow_count() {
        return low_count;
    }

    public String getNormal_count() {
        return normal_count;
    }

    public String getIgnition() {
        return ignition;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getSales_person_name() {
        return sales_person_name;
    }

    public String getUnits_billed() {
        return units_billed;
    }

    public String getBilling_date() {
        return billing_date;
    }

    public String getHardware_price() {
        return hardware_price;
    }

    public String getMonthly_recurring_price() {
        return monthly_recurring_price;
    }

    public String getSetup_charges() {
        return setup_charges;
    }

    public String getInstallation_price() {
        return installation_price;
    }

    public String getAccessories_price() {
        return accessories_price;
    }

    public String getOther_price() {
        return other_price;
    }

    public String getHardware_tax() {
        return hardware_tax;
    }

    public String getMonthly_recurring_tax() {
        return monthly_recurring_tax;
    }

    public String getSetup_charges_tax() {
        return setup_charges_tax;
    }

    public String getInstallation_tax() {
        return installation_tax;
    }

    public String getAccessories_tax() {
        return accessories_tax;
    }

    public String getOther_tax() {
        return other_tax;
    }

    public String getAmc_charges() {
        return amc_charges;
    }

    public String getStart_date_amc() {
        return start_date_amc;
    }

    public String getSales_person() {
        return sales_person;
    }

    public String getGst_no() {
        return gst_no;
    }

    public String getPan_no() {
        return pan_no;
    }

    public String getHardware_without_voice_price() {
        return hardware_without_voice_price;
    }

    public String getHardware_without_voice_tax() {
        return hardware_without_voice_tax;
    }

    public String getHardware_pump_price() {
        return hardware_pump_price;
    }

    public String getHardware_pump_tax() {
        return hardware_pump_tax;
    }

    public String getSale_id() {
        return sale_id;
    }

    public String getDoor_sensor_quantity() {
        return door_sensor_quantity;
    }

    public String getPriority_reason() {
        return priority_reason;
    }
}

