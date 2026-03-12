package in.eoninfotech.eontechnician.helper;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class InstallationValidator {

    // ─── Result class ─────────────────────────────────────────────
    public static class ValidationResult {
        public final boolean isValid;
        public final String errorMessage;      // for Toast
        public final View errorView;           // for setError (can be null)
        public final String errorFieldMessage; // message to show on errorView

        private ValidationResult(boolean isValid, String errorMessage, View errorView, String errorFieldMessage) {
            this.isValid = isValid;
            this.errorMessage = errorMessage;
            this.errorView = errorView;
            this.errorFieldMessage = errorFieldMessage;
        }

        public static ValidationResult ok() {
            return new ValidationResult(true, null, null, null);
        }

        public static ValidationResult toast(String message) {
            return new ValidationResult(false, message, null, null);
        }

        public static ValidationResult fieldError(View view, String message) {
            return new ValidationResult(false, null, view, message);
        }
    }


    // ─── Common header checks (client, location, work type) ───────
    public static ValidationResult validateHeader(
            String mainClientId,
            String s_clientname,
            Spinner location,
            Spinner workType) {

        if (mainClientId == null || mainClientId.equalsIgnoreCase(""))
            return ValidationResult.toast("Please Select Client");

        if (s_clientname == null || s_clientname.equalsIgnoreCase("SELECT Client"))
            return ValidationResult.toast("Please Select Client");

        if (location.getSelectedItem().toString().equalsIgnoreCase("Select Location"))
            return ValidationResult.toast("Please Select Location");

        if (workType.getSelectedItem().toString().equalsIgnoreCase("Select Work Type"))
            return ValidationResult.toast("Please Select Work Type");

        return ValidationResult.ok();
    }


    // ─── Work Type 2: Installation ────────────────────────────────
    public static ValidationResult validateInstallation(
            String s_vts_type,
            String serial_no,
            String s_reg_no,
            String confirmVehNo,
            String s_work_type,
            String fuel_voltage,
            int fuelVoltInt,
            String s_drs_id,
            String s_remarks,
            View vts_sr_no,
            View vltd_sr_no,
            View linear_accessory,
            View con_tilsrNo,
            View e_reg_no,
            View con_in_reg_no,
            View fuelVoltage,
            View linearDrs,
            Spinner vehicleType,
            EditText con_vltd_sr_no,
            EditText accessory_sr_no,
            EditText installVoltage,
            EditText e_drs_id) {

        if (s_vts_type.equalsIgnoreCase("SELECT VTS TYPE"))
            return ValidationResult.toast("Please Select Device Type");

        if (serial_no.equals("") && vts_sr_no.getVisibility() == View.VISIBLE)
            return ValidationResult.fieldError(vts_sr_no, "Sr no can't be blank");

        if (serial_no.equals("") && vltd_sr_no.getVisibility() == View.VISIBLE)
            return ValidationResult.fieldError(vltd_sr_no, "Sr no can't be blank");

        if (serial_no.equals("") && linear_accessory.getVisibility() == View.VISIBLE)
            return ValidationResult.fieldError(accessory_sr_no, "Sr no can't be blank");

        if (con_tilsrNo.getVisibility() == View.VISIBLE &&
                !con_vltd_sr_no.getText().toString().matches(serial_no))
            return ValidationResult.fieldError(con_vltd_sr_no, "Sr. No. not match");

        if (e_reg_no.getVisibility() == View.VISIBLE &&
                (s_reg_no.equals("") || s_reg_no.equals("0")))
            return ValidationResult.fieldError((EditText) e_reg_no, "Reg No. can't be null");

        if (confirmVehNo.equals("") && con_in_reg_no.getVisibility() == View.VISIBLE)
            return ValidationResult.fieldError(con_in_reg_no, "Please fill the no");

        if (con_in_reg_no.getVisibility() == View.VISIBLE && !s_reg_no.equals(confirmVehNo))
            return ValidationResult.fieldError(con_in_reg_no, "No. not match");

        if (s_work_type.equalsIgnoreCase("Select Work Type"))
            return ValidationResult.toast("Please Select Work Type");

        if (vehicleType.getSelectedItem().toString().equalsIgnoreCase("Select Vehicle Type"))
            return ValidationResult.toast("Please Select Vehicle type");

        if (linearDrs.getVisibility() == View.VISIBLE && s_drs_id.equals(""))
            return ValidationResult.fieldError(e_drs_id, "Fill DRS Id");

        if (fuelVoltage.getVisibility() == View.VISIBLE && fuel_voltage.equals(""))
            return ValidationResult.fieldError(installVoltage, "Voltage value can't be null");

        if (fuelVoltage.getVisibility() == View.VISIBLE && fuelVoltInt > 14)
            return ValidationResult.fieldError(installVoltage, "Voltage should be less than 14");

        if (fuelVoltage.getVisibility() == View.VISIBLE &&
                fuel_voltage.equals("0") && s_remarks.equals(""))
            return ValidationResult.fieldError((EditText) null, "Please Specify Reason");

        return ValidationResult.ok();
    }


    // ─── Work Type 4: ReInstallation ──────────────────────────────
    public static ValidationResult validateReInstallation(
            String s_vts_type,
            String s_old_serial_no,
            String s_e_device_id,
            String serial_no,
            String s_reg_no,
            String s_reinst_conf_reg_no,
            String s_VehicleTypeInst,
            String s_drs_id,
            String fuel_voltage,
            int fuelVoltInt,
            String s_remarks,
            String reinstDevice,
            View til_old_vltd_sr_no,
            View til_id_reinst,
            View til_sr_reinst,
            View refuelVoltage,
            View linearDrs,
            View lay_sensor_veh,
            View new_deviceidReinstall,
            View til_new_vltd_sr_no,
            Spinner vltddeviceReinst,
            EditText old_vltd_sr_no,
            EditText old_deviceid,
            EditText vts_sr_no_reinst,
            EditText con_reinstall_sr_no,
            EditText new_vehicleRegNo,
            EditText reinst_conf_reg_no,
            EditText e_drs_id,
            EditText reinstallVoltage,
            EditText old_sensor_veh_no,
            EditText new_sensor_veh_no,
            EditText new_deviceidReinstallField) {

        if (s_vts_type.equalsIgnoreCase("SELECT VTS TYPE"))
            return ValidationResult.toast("Please Select Device Type");

        if (s_old_serial_no.equalsIgnoreCase("SELECT SR.NO."))
            return ValidationResult.toast("Sr. No. can't be null");

        if (s_old_serial_no.equals("") &&
                til_old_vltd_sr_no.getVisibility() == View.VISIBLE &&
                !reinstDevice.equalsIgnoreCase("S"))
            return ValidationResult.fieldError(old_vltd_sr_no, "Sr. No. can't be null");

        if (vltddeviceReinst.getSelectedItem().toString().equalsIgnoreCase("AIS 140") &&
                !s_old_serial_no.matches(con_reinstall_sr_no.getText().toString()))
            return ValidationResult.fieldError(con_reinstall_sr_no, "Sr. no. not match");

        if (s_e_device_id.equals("") &&
                til_id_reinst.getVisibility() == View.VISIBLE &&
                !reinstDevice.equalsIgnoreCase("S"))
            return ValidationResult.fieldError(old_deviceid, "Vts Id can't be null");

        if (serial_no.equals("") &&
                til_sr_reinst.getVisibility() == View.VISIBLE &&
                !reinstDevice.equalsIgnoreCase("S"))
            return ValidationResult.fieldError(vts_sr_no_reinst, "Sr. no. can't be null");

        if (vltddeviceReinst.getSelectedItem().toString().equalsIgnoreCase("E124") &&
                !s_old_serial_no.matches(con_reinstall_sr_no.getText().toString()))
            return ValidationResult.fieldError(con_reinstall_sr_no, "Sr. no. not match");

        if (!reinstDevice.equalsIgnoreCase("S") &&
                (s_reg_no.equals("") || s_reg_no.equals("0")))
            return ValidationResult.fieldError(new_vehicleRegNo, "Reg no. can't be null");

        if (s_reinst_conf_reg_no.equals("") && !reinstDevice.equalsIgnoreCase("S"))
            return ValidationResult.fieldError(reinst_conf_reg_no, "No. can't be null");

        if (!s_reg_no.equals(s_reinst_conf_reg_no) && !reinstDevice.equalsIgnoreCase("S"))
            return ValidationResult.fieldError(reinst_conf_reg_no, "Value doesn't match");

        if (s_VehicleTypeInst.equalsIgnoreCase("Select Vehicle Type") &&
                !reinstDevice.equalsIgnoreCase("S"))
            return ValidationResult.toast("Please select vehicle type");

        if (linearDrs.getVisibility() == View.VISIBLE &&
                s_drs_id.equals("") && !reinstDevice.equalsIgnoreCase("S"))
            return ValidationResult.fieldError(e_drs_id, "Fill DRS Id");

        if (refuelVoltage.getVisibility() == View.VISIBLE &&
                fuel_voltage.equals("") && !reinstDevice.equalsIgnoreCase("S"))
            return ValidationResult.fieldError(reinstallVoltage, "Fuel Voltage can't be null");

        if (refuelVoltage.getVisibility() == View.VISIBLE &&
                fuelVoltInt > 14 && !reinstDevice.equalsIgnoreCase("S"))
            return ValidationResult.fieldError(reinstallVoltage, "Voltage should be less than 14");

        if (refuelVoltage.getVisibility() == View.VISIBLE &&
                fuel_voltage.equals("0") && s_remarks.equals("") &&
                !reinstDevice.equalsIgnoreCase("S"))
            return ValidationResult.fieldError(e_drs_id, "Please specify reason");

        if (reinstDevice.equalsIgnoreCase("S") && lay_sensor_veh.getVisibility() == View.GONE)
            return ValidationResult.toast("Please select Sensor");

        if (reinstDevice.equalsIgnoreCase("S") &&
                lay_sensor_veh.getVisibility() == View.VISIBLE &&
                old_sensor_veh_no.getText().toString().equalsIgnoreCase(""))
            return ValidationResult.fieldError(old_sensor_veh_no, "Enter Vehicle No");

        if (reinstDevice.equalsIgnoreCase("S") &&
                lay_sensor_veh.getVisibility() == View.VISIBLE &&
                new_sensor_veh_no.getText().toString().equalsIgnoreCase(""))
            return ValidationResult.fieldError(new_sensor_veh_no, "Enter Vehicle no");

        if (reinstDevice.equalsIgnoreCase("S") &&
                old_sensor_veh_no.getText().toString()
                        .equals(new_sensor_veh_no.getText().toString()) &&
                lay_sensor_veh.getVisibility() == View.VISIBLE)
            return ValidationResult.toast("Vehicle no. not same");

        return ValidationResult.ok();
    }


    // ─── Work Type 3: Replacement (VTS only branch) ───────────────
    public static ValidationResult validateReplacementVts(
            String s_vts_type,
            String s_old_serial_no,
            String serial_no,
            String s_e_device_id,
            String s_new_device_id,
            String s_reg_no,
            String radioButtonChecked,
            Spinner vltddeviceReplace,
            Spinner reason_replace,
            View linear_device_id_replace_old,
            View linear_device_id_replace_new,
            View linear_device_sr_no_replace_old,
            View linear_device_sr_no_replace_new,
            View linear_vts_id_replace,
            View til_old_sr_replace,
            EditText old_deviceidreplace,
            EditText new_deviceid,
            EditText con_old_deviceidreplace,
            EditText con_new_deviceid,
            EditText old_replace_sr_no,
            EditText new_replace_sr_no,
            EditText old_vts_id_replace,
            EditText con_old_vts_id_replace,
            EditText new_vts_id_replace,
            EditText con_new_vts_id_replace,
            EditText regNo) {

        if (s_vts_type.equalsIgnoreCase("SELECT VTS TYPE"))
            return ValidationResult.toast("Please Select Device Type");

        if (s_old_serial_no.equalsIgnoreCase("SELECT SR.NO."))
            return ValidationResult.toast("Sr. No. can't be null");

        if (serial_no.equalsIgnoreCase("SELECT SR.NO."))
            return ValidationResult.toast("New Sr. No. can't be null");

        String deviceType = vltddeviceReplace.getSelectedItem().toString();
        boolean isE124 = deviceType.equalsIgnoreCase("E124");
        boolean isAIS = deviceType.equalsIgnoreCase("AIS 140");
        boolean notN = !radioButtonChecked.equalsIgnoreCase("N");

        if (isE124 && linear_device_id_replace_old.getVisibility() == View.VISIBLE &&
                s_old_serial_no.equals("") && notN)
            return ValidationResult.fieldError(old_deviceidreplace, "Sr no can't be null");

        if (isE124 && linear_device_id_replace_new.getVisibility() == View.VISIBLE &&
                serial_no.equals("") && notN)
            return ValidationResult.fieldError(new_deviceid, "Sr no can't be null");

        if (isE124 && !serial_no.matches(con_new_deviceid.getText().toString()) && notN)
            return ValidationResult.fieldError(con_new_deviceid, "Sr. no. not match");

        if (linear_device_sr_no_replace_old.getVisibility() == View.VISIBLE &&
                s_old_serial_no.equals("") &&
                til_old_sr_replace.getVisibility() == View.VISIBLE && notN)
            return ValidationResult.fieldError(old_replace_sr_no, "Sr.no.can't be empty");

        if (isAIS && !s_old_serial_no.matches(con_old_deviceidreplace.getText().toString()) && notN)
            return ValidationResult.fieldError(con_old_deviceidreplace, "Sr. no. not match");

        if (linear_device_sr_no_replace_new.getVisibility() == View.VISIBLE &&
                serial_no.equals("") && notN)
            return ValidationResult.fieldError(new_replace_sr_no, "Sr.no.can't be empty");

        if (isAIS && !serial_no.matches(con_new_deviceid.getText().toString()) && notN)
            return ValidationResult.fieldError(con_new_deviceid, "Sr. no. not match");

        if (linear_vts_id_replace.getVisibility() == View.VISIBLE &&
                (s_e_device_id.equalsIgnoreCase("") || s_e_device_id.equalsIgnoreCase("Null")))
            return ValidationResult.fieldError(old_vts_id_replace, "VTS ID can't be null");

        if (linear_vts_id_replace.getVisibility() == View.VISIBLE &&
                !s_e_device_id.matches(con_old_vts_id_replace.getText().toString()))
            return ValidationResult.fieldError(con_old_vts_id_replace, "VTS id not match");

        if (linear_vts_id_replace.getVisibility() == View.VISIBLE &&
                (s_new_device_id.equalsIgnoreCase("") || s_new_device_id.equalsIgnoreCase("Null")))
            return ValidationResult.fieldError(new_vts_id_replace, "VTS ID can't be null");

        if (linear_vts_id_replace.getVisibility() == View.VISIBLE &&
                !s_new_device_id.matches(con_new_vts_id_replace.getText().toString()))
            return ValidationResult.fieldError(con_new_vts_id_replace, "VTS id not match");

        if (notN && (s_reg_no.equals("") || s_reg_no.equals("0") ||
                s_reg_no.equals("null") || s_reg_no.equals("Please Enter Registr")))
            return ValidationResult.fieldError(regNo, "Registration no. can't be null or zero");

        if (reason_replace.getSelectedItem().toString().equalsIgnoreCase("Select Reason") && notN)
            return ValidationResult.toast("Please Select Reason");

        return ValidationResult.ok();
    }


    // ─── Work Type 3: Replacement (DRS only branch) ───────────────
    public static ValidationResult validateReplacementDrs(
            String s_old_serial_no,
            String serial_no,
            String s_e_device_id,
            String s_new_device_id,
            String s_reg_no,
            String radioButtonChecked,
            Spinner vltddeviceReplace,
            Spinner reason_replace,
            View linear_device_id_replace_old,
            View linear_device_id_replace_new,
            View linear_device_sr_no_replace_old,
            View linear_device_sr_no_replace_new,
            View linear_vts_id_replace,
            View til_old_sr_replace,
            EditText old_deviceidreplace,
            EditText new_deviceid,
            EditText con_old_deviceidreplace,
            EditText con_new_deviceid,
            EditText old_replace_sr_no,
            EditText new_replace_sr_no,
            EditText old_vts_id_replace,
            EditText con_old_vts_id_replace,
            EditText new_vts_id_replace,
            EditText con_new_vts_id_replace,
            EditText regNo,
            EditText old_drsid,
            EditText new_drsid) {

        // Reuse VTS checks (serial, device type, reg no, reason)
        ValidationResult base = validateReplacementVts(
                "any", // already passed VTS type check in caller
                s_old_serial_no, serial_no, s_e_device_id, s_new_device_id,
                s_reg_no, radioButtonChecked, vltddeviceReplace, reason_replace,
                linear_device_id_replace_old, linear_device_id_replace_new,
                linear_device_sr_no_replace_old, linear_device_sr_no_replace_new,
                linear_vts_id_replace, til_old_sr_replace,
                old_deviceidreplace, new_deviceid, con_old_deviceidreplace, con_new_deviceid,
                old_replace_sr_no, new_replace_sr_no, old_vts_id_replace, con_old_vts_id_replace,
                new_vts_id_replace, con_new_vts_id_replace, regNo);

        if (!base.isValid) return base;

        if (old_drsid.getText().toString().equals(""))
            return ValidationResult.fieldError(old_drsid, "OLD DRS Id can't be null");

        if (new_drsid.getText().toString().equals(""))
            return ValidationResult.fieldError(new_drsid, "New DRS Id can't be null");

        return ValidationResult.ok();
    }


    // ─── Work Type 3: Replacement (DRS only swap branch) ─────────
    public static ValidationResult validateReplacementDrsSwap(
            EditText drs_vts_id,
            EditText drs_veh_no,
            EditText old_drsid,
            EditText new_drsid) {

        if (drs_vts_id.getText().toString().equals(""))
            return ValidationResult.fieldError(drs_vts_id, "Sr No. can't be null");

        if (drs_veh_no.getText().toString().equals(""))
            return ValidationResult.fieldError(drs_veh_no, "Reg no. can't be null");

        if (old_drsid.getText().toString().equals(""))
            return ValidationResult.fieldError(old_drsid, "Drs id can't be null");

        if (old_drsid.getText().toString().equals(new_drsid.getText().toString()))
            return ValidationResult.fieldError(new_drsid, "Id could not be same");

        return ValidationResult.ok();
    }


    // ─── Work Type 3: Replacement (sensor only branch) ────────────
    public static ValidationResult validateReplacementSensor(
            String s_vts_type,
            String radioButtonChecked,
            View sensor_veh,
            View sensor_veh_no_view,
            EditText sensor_veh_no) {

        if (s_vts_type.equalsIgnoreCase("SELECT VTS TYPE"))
            return ValidationResult.toast("Please Select Device Type");

        if (radioButtonChecked.equalsIgnoreCase("N") && sensor_veh.getVisibility() == View.GONE)
            return ValidationResult.toast("Please Select Sensor");

        if (sensor_veh_no_view.getVisibility() == View.VISIBLE &&
                sensor_veh_no.getText().toString().equalsIgnoreCase(""))
            return ValidationResult.fieldError(sensor_veh_no, "Enter Vehicle No");

        return ValidationResult.ok();
    }


    // ─── Work Type 5: Removal ─────────────────────────────────────
    public static ValidationResult validateRemoval(
            String s_old_serial_no,
            String s_e_device_id,
            String s_reg_no,
            String s_remove_reason,
            String itemsCollected,
            String removal_type,
            String removeDeviceType,
            Spinner vltddeviceRemove,
            View til_remove_sr,
            View lin_vts_id_remove,
            View sensor_veh_remove,
            EditText remove_sr_no,
            EditText con_remove_sr_no,
            EditText remove_vts_id,
            EditText con_remove_vts_id,
            EditText remove_reg_no,
            EditText sensor_veh_no_remove) {

        if (s_old_serial_no.equals("") ||
                s_old_serial_no.equalsIgnoreCase("SELECT SR.NO."))
            return ValidationResult.toast("Select Serial No.");

        if (s_old_serial_no.equals("") &&
                til_remove_sr.getVisibility() == View.VISIBLE &&
                !removeDeviceType.equalsIgnoreCase("S"))
            return ValidationResult.fieldError(remove_sr_no, "Sr. no. can't be null");

        String deviceType = vltddeviceRemove.getSelectedItem().toString();
        if ((deviceType.equalsIgnoreCase("AIS 140") || deviceType.equalsIgnoreCase("E124")) &&
                !s_old_serial_no.matches(con_remove_sr_no.getText().toString()))
            return ValidationResult.fieldError(con_remove_sr_no, "Sr.No. not match");

        if (lin_vts_id_remove.getVisibility() == View.VISIBLE &&
                (s_e_device_id.equalsIgnoreCase("") ||
                        s_e_device_id.equalsIgnoreCase("Null") ||
                        s_e_device_id.equalsIgnoreCase("0")))
            return ValidationResult.fieldError(remove_vts_id, "VTS Id can't be null");

        if (lin_vts_id_remove.getVisibility() == View.VISIBLE &&
                !s_e_device_id.matches(con_remove_vts_id.getText().toString()))
            return ValidationResult.fieldError(con_remove_vts_id, "VTS id not match");

        if (!removeDeviceType.equalsIgnoreCase("S") &&
                (s_reg_no.equals("") || s_reg_no.equals("0") || s_reg_no.equals("null")))
            return ValidationResult.fieldError(remove_reg_no,
                    "Registration no.can't be null or zero");

        if (s_remove_reason.equalsIgnoreCase("Select Removal Reason") &&
                !removeDeviceType.equalsIgnoreCase("S"))
            return ValidationResult.toast("Select Reason");

        if (itemsCollected.equals("") && !removeDeviceType.equalsIgnoreCase("S"))
            return ValidationResult.toast("Select Items Collected");

        if (sensor_veh_remove.getVisibility() == View.VISIBLE &&
                sensor_veh_no_remove.getText().toString().equalsIgnoreCase("") &&
                !removeDeviceType.equalsIgnoreCase("V"))
            return ValidationResult.fieldError(sensor_veh_no_remove,
                    "Please provide vehicle no");

        return ValidationResult.ok();
    }


    // ─── Work Type 1: Fault ───────────────────────────────────────
    public static ValidationResult validateFault(
            String s_vts_type,
            String s_old_serial_no,
            String s_reg_no,
            String others,
            Spinner vltddeviceFault,
            Spinner vehicleTypeFault,
            View linear_device_sr_no_fault,
            View faultDetail,
            EditText vltd_sr_no_fault,
            EditText con_fault_sr_no,
            EditText fault_reg_no,
            EditText faultPersonName,
            EditText faultPersonNumber) {

        String MobilePattern = "[0-9]{10}";

        if (s_vts_type.equalsIgnoreCase("SELECT VTS TYPE"))
            return ValidationResult.toast("Please Select Device Type");

        if (s_old_serial_no.equalsIgnoreCase("SELECT SR.NO."))
            return ValidationResult.toast("Sr. No. can't be null");

        if (linear_device_sr_no_fault.getVisibility() == View.VISIBLE &&
                s_old_serial_no.equals("") &&
                vltd_sr_no_fault.getVisibility() == View.VISIBLE)
            return ValidationResult.fieldError(vltd_sr_no_fault, "Sr No can't be null");

        String deviceType = vltddeviceFault.getSelectedItem().toString();
        if ((deviceType.equalsIgnoreCase("AIS 140") || deviceType.equalsIgnoreCase("E124")) &&
                !s_old_serial_no.matches(con_fault_sr_no.getText().toString()))
            return ValidationResult.fieldError(con_fault_sr_no, "Sr.No. not match");

        if (s_reg_no.equals("") || s_reg_no.equals("0") || s_reg_no.equals("null"))
            return ValidationResult.fieldError(fault_reg_no,
                    "Registration no.can't be null or zero");

        if (others.equals(""))
            return ValidationResult.toast("Select changed values");

        if (vehicleTypeFault.getSelectedItem().toString()
                .equalsIgnoreCase("Select Vehicle Type"))
            return ValidationResult.toast("Please Select Vehicle type");

        if (faultDetail.getVisibility() == View.VISIBLE &&
                faultPersonName.getText().toString().equalsIgnoreCase(""))
            return ValidationResult.fieldError(faultPersonName, "Person name can't be null");

        if (faultDetail.getVisibility() == View.VISIBLE &&
                faultPersonNumber.getText().toString().equalsIgnoreCase(""))
            return ValidationResult.fieldError(faultPersonNumber, "Contact No. can't be null");

        if (faultDetail.getVisibility() == View.VISIBLE &&
                !faultPersonNumber.getText().toString().matches(MobilePattern))
            return ValidationResult.fieldError(faultPersonNumber,
                    "Please Enter valid Mobile No.");

        return ValidationResult.ok();
    }


    // ─── Work Type 6: Phone Support ───────────────────────────────
    public static ValidationResult validatePhoneSupport(
            String s_vts_type,
            String s_old_serial_no,
            String s_reg_no,
            Spinner vltddevicephn,
            Spinner discReason,
            View linear_device_sr_no_phone,
            View vltd_sr_no_phn_view,
            EditText vltd_sr_no_phn,
            EditText con_phone_sr_no,
            EditText phSupport_reg_no,
            EditText phSupportPersonName,
            EditText phSupportPersonPhone) {

        String MobilePattern = "[0-9]{10}";

        if (s_vts_type.equalsIgnoreCase("SELECT VTS TYPE"))
            return ValidationResult.toast("Please Select Device Type");

        if (s_old_serial_no.equalsIgnoreCase("SELECT SR.NO."))
            return ValidationResult.toast("Sr. No. can't be null");

        if (linear_device_sr_no_phone.getVisibility() == View.VISIBLE &&
                s_old_serial_no.equals("") &&
                vltd_sr_no_phn_view.getVisibility() == View.VISIBLE)
            return ValidationResult.fieldError(vltd_sr_no_phn, "Sr No can't be null");

        String deviceType = vltddevicephn.getSelectedItem().toString();
        if ((deviceType.equalsIgnoreCase("AIS 140") || deviceType.equalsIgnoreCase("E124")) &&
                !s_old_serial_no.matches(con_phone_sr_no.getText().toString()))
            return ValidationResult.fieldError(con_phone_sr_no, "Sr.No. not match");

        if (s_reg_no.equals("") || s_reg_no.equals("0") || s_reg_no.equals("null"))
            return ValidationResult.fieldError(phSupport_reg_no,
                    "Registration no.can't be null or zero");

        if (discReason.getSelectedItem().toString().equalsIgnoreCase("Select Reason"))
            return ValidationResult.toast("Please Select Reason");

        if (phSupportPersonName.getText().toString().equalsIgnoreCase(""))
            return ValidationResult.fieldError(phSupportPersonName, "Person name can't be null");

        if (phSupportPersonPhone.getText().toString().equalsIgnoreCase(""))
            return ValidationResult.fieldError(phSupportPersonPhone, "Phone no can't be null");

        if (!phSupportPersonPhone.getText().toString().matches(MobilePattern))
            return ValidationResult.fieldError(phSupportPersonPhone,
                    "Please Enter valid Mobile No");

        return ValidationResult.ok();
    }


    // ─── Work Type 7: Sim Replacement ────────────────────────────
    public static ValidationResult validateSimReplacement(
            String s_e_device_id,
            String s_reg_no,
            String old_sim_no,
            String new_sim_no,
            Spinner sim_replace_reason,
            Spinner sim_operator,
            EditText sim_vts_id,
            EditText sim_vehicle_no,
            EditText e_old_sim_no,
            EditText e_new_sim_no) {

        if (s_e_device_id.equals(""))
            return ValidationResult.fieldError(sim_vts_id, "Vts Id can't be null");

        if (s_reg_no.equalsIgnoreCase("Please Enter Registration No.") ||
                s_reg_no.equals("") || s_reg_no.equals("0") || s_reg_no.equals("null"))
            return ValidationResult.fieldError(sim_vehicle_no,
                    "Please Enter valid no. or Zero");

        if (old_sim_no.equals(""))
            return ValidationResult.fieldError(e_old_sim_no, "Please enter old Sim No.");

        if (new_sim_no.equals(""))
            return ValidationResult.fieldError(e_new_sim_no, "Please enter new Sim No.");

        if (!(old_sim_no.length() == 10 || old_sim_no.length() == 13))
            return ValidationResult.fieldError(e_old_sim_no,
                    "Please enter valid 10 or 13 digit mobile no.");

        if (!(new_sim_no.length() == 10 || new_sim_no.length() == 13))
            return ValidationResult.fieldError(e_new_sim_no,
                    "Please enter valid 10 or 13 digit mobile no.");

        if (sim_replace_reason.getSelectedItem().toString()
                .equalsIgnoreCase("Replacement Reason"))
            return ValidationResult.toast("Please Select Reason");

        if (sim_operator.getSelectedItem().toString().equalsIgnoreCase("New Sim Provider"))
            return ValidationResult.toast("Please Select Sim Provider");

        return ValidationResult.ok();
    }


    // ─── Work Type 8: Missing Device ──────────────────────────────
    public static ValidationResult validateMissingDevice(
            String s_vts_type,
            String s_old_serial_no,
            String s_reg_no,
            String missDeviceType,
            Spinner vltddeviceMiss,
            Spinner missingType,
            View relMissing,
            View sensor_veh_missing,
            EditText vltd_sr_no_miss,
            EditText con_missing_sr_no,
            EditText mDevice_reg_no,
            EditText sensor_veh_no_missing) {

        if (s_vts_type.equalsIgnoreCase("SELECT VTS TYPE"))
            return ValidationResult.toast("Please Select Device Type");

        if (s_old_serial_no.equals("") && !missDeviceType.equalsIgnoreCase("S"))
            return ValidationResult.fieldError(vltd_sr_no_miss, "Sr. no. can't be null");

        String deviceType = vltddeviceMiss.getSelectedItem().toString();
        if ((deviceType.equalsIgnoreCase("AIS 140") || deviceType.equalsIgnoreCase("E124")) &&
                !s_old_serial_no.matches(con_missing_sr_no.getText().toString()))
            return ValidationResult.fieldError(con_missing_sr_no, "Sr.No. not match");

        if (!missDeviceType.equalsIgnoreCase("S") &&
                (s_reg_no.equals("") || s_reg_no.equals("0") || s_reg_no.equals("null")))
            return ValidationResult.fieldError(mDevice_reg_no,
                    "Registration no. can't be null or zero");

        if (relMissing.getVisibility() == View.VISIBLE &&
                missingType.getSelectedItem().toString().equalsIgnoreCase("Select Reason") &&
                !missDeviceType.equalsIgnoreCase("S"))
            return ValidationResult.toast("Please Select Reason");

        if (missDeviceType.equalsIgnoreCase("S") &&
                sensor_veh_missing.getVisibility() == View.GONE)
            return ValidationResult.toast("Please Select Sensor");

        if (sensor_veh_missing.getVisibility() == View.VISIBLE &&
                sensor_veh_no_missing.getText().toString().equalsIgnoreCase("") &&
                (missDeviceType.equalsIgnoreCase("S") || missDeviceType.equalsIgnoreCase("B")))
            return ValidationResult.fieldError(sensor_veh_no_missing,
                    "Please provide Vehicle No");

        return ValidationResult.ok();
    }


    // ─── Work Type 9: Vehicle Not Available ───────────────────────
    public static ValidationResult validateVehicleNotAvail(
            String s_e_device_id,
            String s_reg_no,
            String not_available_reason,
            String s_remarks,
            Spinner vehiclenoavailSpinner,
            Spinner notAvailReason,
            View vehDetail,
            View vehNotAvailRegNo_view,
            EditText vehNotAvailVtsID,
            EditText vehNotAvailRegNo) {

        if (vehDetail.getVisibility() == View.VISIBLE && s_e_device_id.equals(""))
            return ValidationResult.fieldError(vehNotAvailVtsID, "Vts Id can't be null");

        if (vehDetail.getVisibility() == View.VISIBLE &&
                (s_reg_no.equals("") || s_reg_no.equals("0") || s_reg_no.equals("null")) &&
                vehNotAvailRegNo_view.getVisibility() == View.VISIBLE)
            return ValidationResult.fieldError(vehNotAvailRegNo,
                    "Registration no.can't be null or zero");

        if (vehiclenoavailSpinner.getSelectedItem().toString()
                .equalsIgnoreCase("Select Activity"))
            return ValidationResult.toast("Please Select Activity");

        if (notAvailReason.getSelectedItem().toString().equalsIgnoreCase("Select Reason"))
            return ValidationResult.toast("Please Select Reason");

        if (not_available_reason.equals("3") && s_remarks.equals(""))
            return ValidationResult.fieldError(null, "Please Fill Remarks");

        if (not_available_reason.equals("7") && s_remarks.equals(""))
            return ValidationResult.fieldError(null, "Please Fill Remarks");

        return ValidationResult.ok();
    }


    // ─── Work Type 10: Payment (Collection) ───────────────────────
    public static ValidationResult validatePaymentCollection(
            String abc,
            int x,
            Spinner payment_method) {

        if (abc.equalsIgnoreCase(""))
            return ValidationResult.fieldError(null, "Fill Field");

        if (payment_method.getSelectedItem().toString()
                .equalsIgnoreCase("Select Payment Method"))
            return ValidationResult.toast("Please Select Payment Method");

        if (x < 200)
            return ValidationResult.toast("Amount should be at least 200");

        return ValidationResult.ok();
    }


    // ─── Work Type 10: Payment (Follow Up) ────────────────────────
    public static ValidationResult validatePaymentFollowUp(
            EditText followUpPersonName,
            EditText followUpPersonPhone) {

        String MobilePattern = "[0-9]{10}";

        if (followUpPersonName.getText().toString().equalsIgnoreCase(""))
            return ValidationResult.fieldError(followUpPersonName,
                    "Person name can't be null");

        if (followUpPersonPhone.getText().toString().equalsIgnoreCase(""))
            return ValidationResult.fieldError(followUpPersonPhone, "Phone no can't be null");

        if (!followUpPersonPhone.getText().toString().matches(MobilePattern))
            return ValidationResult.fieldError(followUpPersonPhone,
                    "Please enter valid mobile no");

        return ValidationResult.ok();
    }


    // ─── Work Type 11: Other Work ─────────────────────────────────
    public static ValidationResult validateOtherWork(EditText e_remarks) {
        if (e_remarks.getText().toString().equals(""))
            return ValidationResult.fieldError(e_remarks, "Please Fill Details");

        return ValidationResult.ok();
    }


    // ─── Work Type 12/13: UM ──────────────────────────────────────
    public static ValidationResult validateUM(
            EditText et_um_contact_person_name,
            EditText et_um_contact_person_phone) {

        if (et_um_contact_person_name.getText().toString().equalsIgnoreCase(""))
            return ValidationResult.toast("Please provide Contact Person Name");

        if (et_um_contact_person_phone.getText().toString().equalsIgnoreCase(""))
            return ValidationResult.toast("Please provide Contact Person Phone Number");

        return ValidationResult.ok();
    }
}