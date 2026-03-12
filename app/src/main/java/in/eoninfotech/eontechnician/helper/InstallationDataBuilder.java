package in.eoninfotech.eontechnician.builders;

import in.eoninfotech.eontechnician.model.InstallationRequest;

public class InstallationDataBuilder {

    public static InstallationRequest build(
            String user_id, String s_date, String s_Time,
            String clientId, String clientLocId, String is_demo,
            String s_work_id, String s_vts_type, String is_drs,
            String device_type, String s_e_device_id, String s_new_device_id,
            String s_old_serial_no, String serial_no, String s_reg_no,
            String s_vehicletype, String s_drs_id, String s_new_drs_id,
            String drs_dirction, String mgt_set, String ignition_sensor,
            String fuel_sensor, String door_sensor, String panic,
            String cut_off, String s_reason_repla, String removal_type,
            String removalReason, String disconnection_reason,
            String missing_type, String missing_reason,
            String not_available_activity, String not_available_reason,
            String collection_date, String collection_type,
            String collection_amount, String payment_type,
            String contact_person, String contact_no,
            String sim_provider, String old_sim_no, String new_sim_no,
            String sim_reason, String veh_condition, String s_remarks,
            String itemsCollected, String others, String fuel_voltage,
            String lid_status, String trans, String temp_sensor,
            String tilt_sensor, String fuel_status, String panic_status,
            String sen_vehicle_no, String sensor_old_veh_no,
            String missDeviceType, String drsStatus, String replace_type,
            String device_working_status, String sensor_working_status,
            String mainClientId, String s_cust_type) {

        InstallationRequest req = new InstallationRequest();

        req.user_id                 = user_id;
        req.s_date                  = s_date;
        req.s_Time                  = s_Time;
        req.clientId                = clientId;
        req.clientLocId             = clientLocId;
        req.is_demo                 = is_demo;
        req.s_work_id               = s_work_id;
        req.s_vts_type              = s_vts_type;
        req.is_drs                  = is_drs;
        req.device_type             = device_type;
        req.s_e_device_id           = s_e_device_id;
        req.s_new_device_id         = s_new_device_id;
        req.s_old_serial_no         = s_old_serial_no;
        req.serial_no               = serial_no;
        req.s_reg_no                = s_reg_no;
        req.s_vehicletype           = s_vehicletype;
        req.s_drs_id                = s_drs_id;
        req.s_new_drs_id            = s_new_drs_id;
        req.drs_dirction            = drs_dirction;
        req.mgt_set                 = mgt_set;
        req.ignition_sensor         = ignition_sensor;
        req.fuel_sensor             = fuel_sensor;
        req.door_sensor             = door_sensor;
        req.panic                   = panic;
        req.cut_off                 = cut_off;
        req.s_reason_repla          = s_reason_repla;
        req.removal_type            = removal_type;
        req.removalReason           = removalReason;
        req.disconnection_reason    = disconnection_reason;
        req.missing_type            = missing_type;
        req.missing_reason          = missing_reason;
        req.not_available_activity  = not_available_activity;
        req.not_available_reason    = not_available_reason;
        req.collection_date         = collection_date;
        req.collection_type         = collection_type;
        req.collection_amount       = collection_amount;
        req.payment_type            = payment_type;
        req.contact_person          = contact_person;
        req.contact_no              = contact_no;
        req.sim_provider            = sim_provider;
        req.old_sim_no              = old_sim_no;
        req.new_sim_no              = new_sim_no;
        req.sim_reason              = sim_reason;
        req.veh_condition           = veh_condition;
        req.s_remarks               = s_remarks;
        req.itemsCollected          = itemsCollected;
        req.others                  = others;
        req.fuel_voltage            = fuel_voltage;
        req.lid_status              = lid_status;
        req.trans                   = trans;
        req.temp_sensor             = temp_sensor;
        req.tilt_sensor             = tilt_sensor;
        req.fuel_status             = fuel_status;
        req.panic_status            = panic_status;
        req.sen_vehicle_no          = sen_vehicle_no;
        req.sensor_old_veh_no       = sensor_old_veh_no;
        req.missDeviceType          = missDeviceType;
        req.drsStatus               = drsStatus;
        req.replace_type            = replace_type;
        req.device_working_status   = device_working_status;
        req.sensor_working_status   = sensor_working_status;
        req.mainClientId            = mainClientId;
        req.s_cust_type             = s_cust_type;

        return req;
    }
}