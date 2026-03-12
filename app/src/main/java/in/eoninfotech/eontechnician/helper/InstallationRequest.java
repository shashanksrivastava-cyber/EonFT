package in.eoninfotech.eontechnician.model;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import java.util.HashMap;
import java.util.Map;

public class InstallationRequest {

    // ─── Fields ───────────────────────────────────────────────────
    public String user_id;
    public String s_date;
    public String s_Time;
    public String clientId;
    public String clientLocId;
    public String is_demo;
    public String s_work_id;
    public String s_vts_type;
    public String is_drs;
    public String device_type;
    public String s_e_device_id;
    public String s_new_device_id;
    public String s_old_serial_no;
    public String serial_no;
    public String s_reg_no;
    public String s_vehicletype;
    public String s_drs_id;
    public String s_new_drs_id;
    public String drs_dirction;
    public String mgt_set;
    public String ignition_sensor;
    public String fuel_sensor;
    public String door_sensor;
    public String panic;
    public String cut_off;
    public String s_reason_repla;
    public String removal_type;
    public String removalReason;
    public String disconnection_reason;
    public String missing_type;
    public String missing_reason;
    public String not_available_activity;
    public String not_available_reason;
    public String collection_date;
    public String collection_type;
    public String collection_amount;
    public String payment_type;
    public String contact_person;
    public String contact_no;
    public String sim_provider;
    public String old_sim_no;
    public String new_sim_no;
    public String sim_reason;
    public String veh_condition;
    public String s_remarks;
    public String itemsCollected;
    public String others;
    public String fuel_voltage;
    public String lid_status;
    public String trans;
    public String temp_sensor;
    public String tilt_sensor;
    public String fuel_status;
    public String panic_status;
    public String sen_vehicle_no;
    public String sensor_old_veh_no;
    public String missDeviceType;
    public String drsStatus;
    public String replace_type;
    public String device_working_status;
    public String sensor_working_status;
    public String mainClientId;
    public String s_cust_type;

    // ─── Convert all fields to RequestBody map ────────────────────
    public Map<String, RequestBody> toRequestBodyMap() {
        Map<String, RequestBody> map = new HashMap<>();

        map.put("technician_id",        toBody(user_id));
        map.put("activity_date",        toBody(s_date));
        map.put("activity_time",        toBody(s_Time));
        map.put("customer",             toBody(clientId));
        map.put("customer_location",    toBody(clientLocId));
        map.put("isDemo",               toBody(is_demo));
        map.put("activity_type",        toBody(s_work_id));
        map.put("vts_type",             toBody(s_vts_type));
        map.put("is_DRS",               toBody(is_drs));
        map.put("deviceType",           toBody(device_type));
        map.put("old_device_id",        toBody(s_e_device_id));
        map.put("new_device_id",        toBody(s_new_device_id));
        map.put("old_serial_no",        toBody(s_old_serial_no));
        map.put("new_serial_no",        toBody(serial_no));
        map.put("reg_no",               toBody(s_reg_no));
        map.put("veh_type",             toBody(s_vehicletype));
        map.put("old_drs",              toBody(s_drs_id));
        map.put("new_drs",              toBody(s_new_drs_id));
        map.put("drs_direction",        toBody(drs_dirction));
        map.put("Mgt_set",              toBody(mgt_set));
        map.put("ignSensor",            toBody(ignition_sensor));
        map.put("fuelSensor",           toBody(fuel_sensor));
        map.put("doorSensor",           toBody(door_sensor));
        map.put("panic_button",         toBody(panic));
        map.put("cutOff",               toBody(cut_off));
        map.put("replacement_reason",   toBody(s_reason_repla));
        map.put("removalType",          toBody(removal_type));
        map.put("removeReason",         toBody(removalReason));
        map.put("disconnectReason",     toBody(disconnection_reason));
        map.put("missingType",          toBody(missing_type));
        map.put("missingReason",        toBody(missing_reason));
        map.put("notAvailActivity",     toBody(not_available_activity));
        map.put("notAvailReason",       toBody(not_available_reason));
        map.put("collectionDate",       toBody(collection_date));
        map.put("collectionType",       toBody(collection_type));
        map.put("collectionAmount",     toBody(collection_amount));
        map.put("paymentType",          toBody(payment_type));
        map.put("contactPerson",        toBody(contact_person));
        map.put("contactNo",            toBody(contact_no));
        map.put("simProvider",          toBody(sim_provider));
        map.put("oldSimNo",             toBody(old_sim_no));
        map.put("newSimNo",             toBody(new_sim_no));
        map.put("simReason",            toBody(sim_reason));
        map.put("veh_Condition",        toBody(veh_condition));
        map.put("remarks",              toBody(s_remarks));
        map.put("itemCollected",        toBody(itemsCollected));
        map.put("faults_checked",       toBody(others));
        map.put("fuel_reading",         toBody(fuel_voltage));
        map.put("lid_statu",            toBody(lid_status));
        map.put("tran",                 toBody(trans));
        map.put("temp_senso",           toBody(temp_sensor));
        map.put("tilt_senso",           toBody(tilt_sensor));
        map.put("fuel_statu",           toBody(fuel_status));
        map.put("panic_statu",          toBody(panic_status));
        map.put("sensor_veh_n",         toBody(sen_vehicle_no));
        map.put("sensor_old_veh_n",     toBody(sensor_old_veh_no));
        map.put("remove_type",          toBody(missDeviceType));
        map.put("drs_status",           toBody(drsStatus));
        map.put("replacetype",          toBody(replace_type));
        map.put("deviceworkingstatus",  toBody(device_working_status));
        map.put("sensorworkingstatus",  toBody(sensor_working_status));
        map.put("mainclientid",         toBody(mainClientId));
        map.put("cust_type",            toBody(s_cust_type));

        return map;
    }

    // ─── Helper ───────────────────────────────────────────────────
    private RequestBody toBody(String value) {
        return RequestBody.create(
                MediaType.parse("text/plain"),
                value != null ? value : ""
        );
    }
}