package in.eoninfotech.eontechnician.activity;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import in.eoninfotech.eontechnician.fragments.NewInstallmentFragment;

public class ClearDataManager {

    private final NewInstallmentFragment fragment;
    private final EditText old_deviceid, old_deviceidreplace, regNo, new_deviceid;
    private final EditText old_vts_id_replace, new_vts_id_replace, con_old_vts_id_replace, con_new_vts_id_replace;
    private final EditText plantName, e_drs_id, e_reg_no, e_remarks, e_device_id;
    private final EditText old_drsid, new_drsid, drs_veh_no, drs_vts_id;
    private final EditText reinstallVoltage, installVoltage, new_vehicleRegNo, new_deviceidReinstall;
    private final EditText vltd_sr_no_notAvail, vltd_sr_no_miss;
    private final EditText fault_reg_no, fault_vts_id, vltd_sr_no_fault;
    private final EditText new_vltd_sr_no, old_vltd_sr_no, old_replace_sr_no, new_replace_sr_no;
    private final EditText vltd_sr_no_phn, vltd_sr_no, remove_deviceid, phsupport_vts_id, sim_vts_id, remove_sr_no;
    private final EditText followUpPersonName, followUpPersonPhone;
    private final EditText e_old_sim_no, e_new_sim_no;
    private final EditText mDevice_reg_no, mDevice_vts_id;
    private final EditText vehNotAvailRegNo, vehNotAvailVtsID;
    private final EditText amount, imageName, imageNameFault, imageNameMissing;
    private final EditText rep_srNo, con_in_reg_no;
    private final EditText faultPersonNumber, faultPersonName;
    private final EditText vts_sr_no, vts_sr_no_reinst, reinst_conf_reg_no;
    private final EditText phSupportPersonName, phSupportPersonPhone;
    private final EditText sensor_veh_no, sensor_veh_no_missing, sensor_veh_no_remove, old_sensor_veh_no, new_sensor_veh_no;
    private final EditText con_fault_sr_no, con_missing_sr_no, con_new_deviceid, con_old_deviceidreplace;
    private final EditText con_phone_sr_no, con_reinstall_sr_no, con_remove_sr_no, con_vltd_sr_no;
    private final EditText accessory_sr_no, accessory_reg_no;

    // ✅ RadioButtons / CheckBoxes
    private final RadioButton lidNone, transNo, tempNo, tiltNo, is_demo_no, l_in, doorNo, cutoffNo, fuelSensorNewNo;
    private final RadioButton cut_off_no_reinst, drs_no_reinst, fuelNoReinst, panicNoReinst, tiltNoReinst,
            fuelSensorNewNoReinst, tempNoReinst, transNoReinst, lidNoneReinst;
    private final RadioButton tiltNoReplace, radioDevice, tempNoReplace, panicNoReplace, fuelSensorNoReplace, transNoReplace, lidNoneReplace;
    private final RadioButton radioDeviceRemove, tiltRemoveNo, tempNoRemove, panicNoRemove, fuelSensorNoRemove, transNoRemove, lidNoneRemove;
    private final RadioButton radioDeviceMiss, tiltMissingNo, tempNoMissing, panicNoMissing, fuelSensorNoMissing, transNoMissing, lidNoneMissing;

    private final CheckBox deviceWorking, sensorWorking;

    // ✅ Variables which were in fragment
    public String s_vts_type = "SELECT VTS TYPE";
    public String fuel_voltage = "0";
    public String buttonPressed = "0";
    public String others = "";
    public String s_cust_type = "";
    public String s_e_device_id = "0";
    public String s_new_device_id = "0";
    public String s_vehicletype = "0";
    public String s_device_id = "0";
    public String s_reg_no = "0";
    public String is_drs = "N";
    public String s_drs_id = "0";
    public String s_new_drs_id = "0";
    public String drs_dirction = "N";
    public String s_reason_repla = "0";
    public String removalReason = "0";
    public String itemsCollected = "0";
    public String s_remarks = "0";
    public String disconnection_reason = "0";
    public String ignition_sensor = "N";
    public String fuel_sensor = "N";
    public String door_sensor = "N";
    public String veh_condition = "W";
    public String mgt_set = "N";
    public String sim_provider = "0";
    public String old_sim_no = "0";
    public String new_sim_no = "0";
    public String sim_reason = "0";
    public String not_available_reason = "0";
    public String not_available_activity = "0";
    public String is_demo = "N";
    public String collection_amount = "0";
    public String collection_date = "0";
    public String collection_type = "0";
    public String missing_reason = "0";
    public String removal_type = "0";
    public String image = "";

    // ✅ Constructor (Pass ALL Views from fragment)
    public ClearDataManager(
            NewInstallmentFragment fragment,

            EditText old_deviceid, EditText old_deviceidreplace, EditText regNo, EditText new_deviceid,
            EditText old_vts_id_replace, EditText new_vts_id_replace, EditText con_old_vts_id_replace, EditText con_new_vts_id_replace,
            EditText plantName, EditText e_drs_id, EditText e_reg_no, EditText e_remarks, EditText e_device_id,
            EditText old_drsid, EditText new_drsid, EditText drs_veh_no, EditText drs_vts_id,
            EditText reinstallVoltage, EditText installVoltage, EditText new_vehicleRegNo, EditText new_deviceidReinstall,
            EditText vltd_sr_no_notAvail, EditText vltd_sr_no_miss,
            EditText fault_reg_no, EditText fault_vts_id, EditText vltd_sr_no_fault,
            EditText new_vltd_sr_no, EditText old_vltd_sr_no, EditText old_replace_sr_no, EditText new_replace_sr_no,
            EditText vltd_sr_no_phn, EditText vltd_sr_no, EditText remove_deviceid,
            EditText phsupport_vts_id, EditText sim_vts_id, EditText remove_sr_no,
            EditText followUpPersonName, EditText followUpPersonPhone,
            EditText e_old_sim_no, EditText e_new_sim_no,
            EditText mDevice_reg_no, EditText mDevice_vts_id,
            EditText vehNotAvailRegNo, EditText vehNotAvailVtsID,
            EditText amount, EditText imageName, EditText imageNameFault, EditText imageNameMissing,
            EditText rep_srNo, EditText con_in_reg_no,
            EditText faultPersonNumber, EditText faultPersonName,
            EditText vts_sr_no, EditText vts_sr_no_reinst, EditText reinst_conf_reg_no,
            EditText phSupportPersonName, EditText phSupportPersonPhone,
            EditText sensor_veh_no, EditText sensor_veh_no_missing, EditText sensor_veh_no_remove,
            EditText old_sensor_veh_no, EditText new_sensor_veh_no,
            EditText con_fault_sr_no, EditText con_missing_sr_no, EditText con_new_deviceid, EditText con_old_deviceidreplace,
            EditText con_phone_sr_no, EditText con_reinstall_sr_no, EditText con_remove_sr_no, EditText con_vltd_sr_no,
            EditText accessory_sr_no, EditText accessory_reg_no,

            RadioButton lidNone, RadioButton transNo, RadioButton tempNo, RadioButton tiltNo, RadioButton is_demo_no,
            RadioButton l_in, RadioButton doorNo, RadioButton cutoffNo, RadioButton fuelSensorNewNo,

            RadioButton cut_off_no_reinst, RadioButton drs_no_reinst, RadioButton fuelNoReinst, RadioButton panicNoReinst,
            RadioButton tiltNoReinst, RadioButton fuelSensorNewNoReinst, RadioButton tempNoReinst,
            RadioButton transNoReinst, RadioButton lidNoneReinst,

            RadioButton tiltNoReplace, RadioButton radioDevice, RadioButton tempNoReplace, RadioButton panicNoReplace,
            RadioButton fuelSensorNoReplace, RadioButton transNoReplace, RadioButton lidNoneReplace,

            RadioButton radioDeviceRemove, RadioButton tiltRemoveNo, RadioButton tempNoRemove, RadioButton panicNoRemove,
            RadioButton fuelSensorNoRemove, RadioButton transNoRemove, RadioButton lidNoneRemove,

            RadioButton radioDeviceMiss, RadioButton tiltMissingNo, RadioButton tempNoMissing, RadioButton panicNoMissing,
            RadioButton fuelSensorNoMissing, RadioButton transNoMissing, RadioButton lidNoneMissing,

            CheckBox deviceWorking, CheckBox sensorWorking
    ) {
        this.fragment = fragment;

        this.old_deviceid = old_deviceid;
        this.old_deviceidreplace = old_deviceidreplace;
        this.regNo = regNo;
        this.new_deviceid = new_deviceid;

        this.old_vts_id_replace = old_vts_id_replace;
        this.new_vts_id_replace = new_vts_id_replace;
        this.con_old_vts_id_replace = con_old_vts_id_replace;
        this.con_new_vts_id_replace = con_new_vts_id_replace;

        this.plantName = plantName;
        this.e_drs_id = e_drs_id;
        this.e_reg_no = e_reg_no;
        this.e_remarks = e_remarks;
        this.e_device_id = e_device_id;

        this.old_drsid = old_drsid;
        this.new_drsid = new_drsid;
        this.drs_veh_no = drs_veh_no;
        this.drs_vts_id = drs_vts_id;

        this.reinstallVoltage = reinstallVoltage;
        this.installVoltage = installVoltage;
        this.new_vehicleRegNo = new_vehicleRegNo;
        this.new_deviceidReinstall = new_deviceidReinstall;

        this.vltd_sr_no_notAvail = vltd_sr_no_notAvail;
        this.vltd_sr_no_miss = vltd_sr_no_miss;

        this.fault_reg_no = fault_reg_no;
        this.fault_vts_id = fault_vts_id;
        this.vltd_sr_no_fault = vltd_sr_no_fault;

        this.new_vltd_sr_no = new_vltd_sr_no;
        this.old_vltd_sr_no = old_vltd_sr_no;
        this.old_replace_sr_no = old_replace_sr_no;
        this.new_replace_sr_no = new_replace_sr_no;

        this.vltd_sr_no_phn = vltd_sr_no_phn;
        this.vltd_sr_no = vltd_sr_no;
        this.remove_deviceid = remove_deviceid;
        this.phsupport_vts_id = phsupport_vts_id;
        this.sim_vts_id = sim_vts_id;
        this.remove_sr_no = remove_sr_no;

        this.followUpPersonName = followUpPersonName;
        this.followUpPersonPhone = followUpPersonPhone;

        this.e_old_sim_no = e_old_sim_no;
        this.e_new_sim_no = e_new_sim_no;

        this.mDevice_reg_no = mDevice_reg_no;
        this.mDevice_vts_id = mDevice_vts_id;

        this.vehNotAvailRegNo = vehNotAvailRegNo;
        this.vehNotAvailVtsID = vehNotAvailVtsID;

        this.amount = amount;
        this.imageName = imageName;
        this.imageNameFault = imageNameFault;
        this.imageNameMissing = imageNameMissing;

        this.rep_srNo = rep_srNo;
        this.con_in_reg_no = con_in_reg_no;

        this.faultPersonNumber = faultPersonNumber;
        this.faultPersonName = faultPersonName;

        this.vts_sr_no = vts_sr_no;
        this.vts_sr_no_reinst = vts_sr_no_reinst;
        this.reinst_conf_reg_no = reinst_conf_reg_no;

        this.phSupportPersonName = phSupportPersonName;
        this.phSupportPersonPhone = phSupportPersonPhone;

        this.sensor_veh_no = sensor_veh_no;
        this.sensor_veh_no_missing = sensor_veh_no_missing;
        this.sensor_veh_no_remove = sensor_veh_no_remove;
        this.old_sensor_veh_no = old_sensor_veh_no;
        this.new_sensor_veh_no = new_sensor_veh_no;

        this.con_fault_sr_no = con_fault_sr_no;
        this.con_missing_sr_no = con_missing_sr_no;
        this.con_new_deviceid = con_new_deviceid;
        this.con_old_deviceidreplace = con_old_deviceidreplace;

        this.con_phone_sr_no = con_phone_sr_no;
        this.con_reinstall_sr_no = con_reinstall_sr_no;
        this.con_remove_sr_no = con_remove_sr_no;
        this.con_vltd_sr_no = con_vltd_sr_no;

        this.accessory_sr_no = accessory_sr_no;
        this.accessory_reg_no = accessory_reg_no;

        this.lidNone = lidNone;
        this.transNo = transNo;
        this.tempNo = tempNo;
        this.tiltNo = tiltNo;
        this.is_demo_no = is_demo_no;
        this.l_in = l_in;
        this.doorNo = doorNo;
        this.cutoffNo = cutoffNo;
        this.fuelSensorNewNo = fuelSensorNewNo;

        this.cut_off_no_reinst = cut_off_no_reinst;
        this.drs_no_reinst = drs_no_reinst;
        this.fuelNoReinst = fuelNoReinst;
        this.panicNoReinst = panicNoReinst;
        this.tiltNoReinst = tiltNoReinst;
        this.fuelSensorNewNoReinst = fuelSensorNewNoReinst;
        this.tempNoReinst = tempNoReinst;
        this.transNoReinst = transNoReinst;
        this.lidNoneReinst = lidNoneReinst;

        this.tiltNoReplace = tiltNoReplace;
        this.radioDevice = radioDevice;
        this.tempNoReplace = tempNoReplace;
        this.panicNoReplace = panicNoReplace;
        this.fuelSensorNoReplace = fuelSensorNoReplace;
        this.transNoReplace = transNoReplace;
        this.lidNoneReplace = lidNoneReplace;

        this.radioDeviceRemove = radioDeviceRemove;
        this.tiltRemoveNo = tiltRemoveNo;
        this.tempNoRemove = tempNoRemove;
        this.panicNoRemove = panicNoRemove;
        this.fuelSensorNoRemove = fuelSensorNoRemove;
        this.transNoRemove = transNoRemove;
        this.lidNoneRemove = lidNoneRemove;

        this.radioDeviceMiss = radioDeviceMiss;
        this.tiltMissingNo = tiltMissingNo;
        this.tempNoMissing = tempNoMissing;
        this.panicNoMissing = panicNoMissing;
        this.fuelSensorNoMissing = fuelSensorNoMissing;
        this.transNoMissing = transNoMissing;
        this.lidNoneMissing = lidNoneMissing;

        this.deviceWorking = deviceWorking;
        this.sensorWorking = sensorWorking;
    }

    // ✅ MAIN METHOD (API + CLEAR)
    public void clearData() {

        // ✅ API Calls from Fragment
        fragment.getDeviceTypes();
        fragment.getFaultList();
        fragment.getSimOperator();
        fragment.getSimReasonList();
        fragment.getPhoneSupportList();
        fragment.addVehType();
        fragment.addReasonRemove();
        fragment.addVehNotAvailReason();
        fragment.getDevice();
        fragment.getItemCollectList();
        fragment.addReasonRemove();
        fragment.removal_type();
        fragment.damageReason();
        fragment.getSerialNo();
        fragment.fetchReasons();

        // ✅ Reset Views
        clearTextView();

        // ✅ Reset Radio/Checkbox
        lidNone.setChecked(true);
        transNo.setChecked(true);
        tempNo.setChecked(true);
        tiltNo.setChecked(true);
        is_demo_no.setChecked(true);
        l_in.setChecked(true);
        doorNo.setChecked(true);
        cutoffNo.setChecked(true);
        fuelSensorNewNo.setChecked(true);

        cut_off_no_reinst.setChecked(true);
        drs_no_reinst.setChecked(true);
        fuelNoReinst.setChecked(true);
        panicNoReinst.setChecked(true);
        tiltNoReinst.setChecked(true);
        fuelSensorNewNoReinst.setChecked(true);
        tempNoReinst.setChecked(true);
        transNoReinst.setChecked(true);
        lidNoneReinst.setChecked(true);

        tiltNoReplace.setChecked(true);
        radioDevice.setChecked(true);
        tempNoReplace.setChecked(true);
        panicNoReplace.setChecked(true);
        fuelSensorNoReplace.setChecked(true);
        transNoReplace.setChecked(true);
        lidNoneReplace.setChecked(true);

        radioDeviceRemove.setChecked(true);
        tiltRemoveNo.setChecked(true);
        tempNoRemove.setChecked(true);
        panicNoRemove.setChecked(true);
        fuelSensorNoRemove.setChecked(true);
        transNoRemove.setChecked(true);
        lidNoneRemove.setChecked(true);

        radioDeviceMiss.setChecked(true);
        tiltMissingNo.setChecked(true);
        tempNoMissing.setChecked(true);
        panicNoMissing.setChecked(true);
        fuelSensorNoMissing.setChecked(true);
        transNoMissing.setChecked(true);
        lidNoneMissing.setChecked(true);

        deviceWorking.setChecked(true);
        sensorWorking.setChecked(true);
    }

    public void clearTextView() {

        old_deviceid.setText("");
        old_deviceidreplace.setText("");
        regNo.setText("");
        new_deviceid.setText("");

        old_vts_id_replace.setText("");
        new_vts_id_replace.setText("");
        con_old_vts_id_replace.setText("");
        con_new_vts_id_replace.setText("");

        plantName.setText("");
        e_drs_id.setText("");
        e_reg_no.setText("");
        e_remarks.setText("");
        e_device_id.setText("");

        old_drsid.setText("");
        new_drsid.setText("");
        drs_veh_no.setText("");
        drs_vts_id.setText("");

        reinstallVoltage.setText("");
        installVoltage.setText("");
        new_vehicleRegNo.setText("");
        new_deviceidReinstall.setText("");

        vltd_sr_no_notAvail.setText("");
        vltd_sr_no_miss.setText("");
        fault_reg_no.setText("");
        fault_vts_id.setText("");
        vltd_sr_no_fault.setText("");

        new_vltd_sr_no.setText("");
        old_vltd_sr_no.setText("");
        old_replace_sr_no.setText("");
        new_replace_sr_no.setText("");

        vltd_sr_no_phn.setText("");
        vltd_sr_no.setText("");
        remove_deviceid.setText("");
        phsupport_vts_id.setText("");
        sim_vts_id.setText("");
        remove_sr_no.setText("");

        followUpPersonName.setText("");
        followUpPersonPhone.setText("");
        e_old_sim_no.setText("");
        e_new_sim_no.setText("");

        mDevice_reg_no.setText("");
        mDevice_vts_id.setText("");
        vehNotAvailRegNo.setText("");
        vehNotAvailVtsID.setText("");
        amount.setText("");

        imageName.setText("");
        imageNameFault.setText("");
        imageNameMissing.setText("");

        rep_srNo.setText("");
        con_in_reg_no.setText("");
        faultPersonNumber.setText("");
        faultPersonName.setText("");

        vts_sr_no.setText("");
        vts_sr_no_reinst.setText("");
        reinst_conf_reg_no.setText("");

        phSupportPersonName.setText("");
        phSupportPersonPhone.setText("");

        sensor_veh_no.setText("");
        sensor_veh_no_missing.setText("");
        sensor_veh_no_remove.setText("");
        old_sensor_veh_no.setText("");
        new_sensor_veh_no.setText("");

        con_fault_sr_no.setText("");
        con_missing_sr_no.setText("");
        con_new_deviceid.setText("");
        con_old_deviceidreplace.setText("");

        con_phone_sr_no.setText("");
        con_reinstall_sr_no.setText("");
        con_remove_sr_no.setText("");
        con_vltd_sr_no.setText("");

        accessory_sr_no.setText("");
        accessory_reg_no.setText("");
    }
}
