package in.eoninfotech.eontechnician.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.Responses.DeviceTypeOtherAis;
import in.eoninfotech.eontechnician.Responses.MainResponse;
import in.eoninfotech.eontechnician.Responses.PMethodDetail;
import in.eoninfotech.eontechnician.Responses.PaymentMethodResponse;
import in.eoninfotech.eontechnician.Responses.SrNoDeviceList;
import in.eoninfotech.eontechnician.callbacks.ReceiveDeviceListener;
import in.eoninfotech.eontechnician.controllers.NewInstallmentController;
import in.eoninfotech.eontechnician.NonScrollListView;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.ClientDetails;
import in.eoninfotech.eontechnician.Responses.ClientLocationDetail;
import in.eoninfotech.eontechnician.Responses.ClientLocationResponse;
import in.eoninfotech.eontechnician.Responses.ClientResponse;
import in.eoninfotech.eontechnician.Responses.CollectedItemsResponse;
import in.eoninfotech.eontechnician.Responses.DisconnectionDetail;
import in.eoninfotech.eontechnician.Responses.DisconnectionResponse;
import in.eoninfotech.eontechnician.Responses.FaultList;
import in.eoninfotech.eontechnician.Responses.FaultResponse;
import in.eoninfotech.eontechnician.Responses.MainResponse;
import in.eoninfotech.eontechnician.Responses.ItemList;
import in.eoninfotech.eontechnician.Responses.NotAvailActivityDetail;
import in.eoninfotech.eontechnician.Responses.NotAvailActivityResponse;
import in.eoninfotech.eontechnician.Responses.RemovalActivityDetail;
import in.eoninfotech.eontechnician.Responses.RemovalActivityResponse;
import in.eoninfotech.eontechnician.Responses.RemovalList;
import in.eoninfotech.eontechnician.Responses.RemovalResponse;
import in.eoninfotech.eontechnician.Responses.ReplaceReason;
import in.eoninfotech.eontechnician.Responses.ReplaceReasonDetail;
import in.eoninfotech.eontechnician.Responses.SimDetail;
import in.eoninfotech.eontechnician.Responses.SimOperatorDetail;
import in.eoninfotech.eontechnician.Responses.SimOperatorResponse;
import in.eoninfotech.eontechnician.Responses.SimReplaceResponse;
import in.eoninfotech.eontechnician.Responses.VTSDetail;
import in.eoninfotech.eontechnician.Responses.VTSResponse;
import in.eoninfotech.eontechnician.Responses.VehNotAvailReasonDetail;
import in.eoninfotech.eontechnician.Responses.VehNotAvailReasonResponse;
import in.eoninfotech.eontechnician.Responses.VehicleTypeDetail;
import in.eoninfotech.eontechnician.Responses.VehicleTypeResponse;
import in.eoninfotech.eontechnician.Responses.WorkTypeDetail;
import in.eoninfotech.eontechnician.Responses.WorkTypeResponse;
import in.eoninfotech.eontechnician.callbacks.ClientListener;
import in.eoninfotech.eontechnician.controllers.ReceiveDeviceControllers;
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.helper.FileUtils;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.helper.ProgressRequestBody;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.view.MyTextView;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import in.eoninfotech.eontechnician.webservice.UmVehicleDetail;
import in.eoninfotech.eontechnician.webservice.UmVehicleResponse;
import in.eoninfotech.eontechnician.webservice.VTSTypeResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static pub.devrel.easypermissions.EasyPermissions.hasPermissions;

public class NewInstallmentFragment extends Fragment implements ClientListener, ReceiveDeviceListener,ProgressRequestBody.UploadCallbacks {

    View v;
    private final int SELECT_PHOTO = 1;
    public static final String IMAGE_DIRECTORY_NAME = "android_file";
    public static final int MEDIA_TYPE_IMAGE = 1;
    private int REQUEST_CAMERA = 0;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    private Handler handler = new Handler();
    int pStatus = 0, x, PERMISSION_ALL = 1, REQUEST_CODE_PERMISSION = 10, fuelVoltInt;
    CheckedTextView text1;
    ImageView checked;
    private AlertDialog progressDialog;
    File file;
    Uri uri;
    Bitmap bmp;
    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    NewInstallmentController newInstallmentController;
    ReceiveDeviceControllers receiveDeviceControllers;
    TextInputLayout tilVoltage, til_vts_miss, til_no_avail_vts, tilsrNo_notAvail, til_new_replace, til_old_replace, til_vts_remove, til_vts_sr_no,
            til_remove_sr, tilDeviceMiss, told_drsid, tnew_drsid, t_drs_veh_no, t_drs_vts_id, tilvtsno, tilsrNo, tilFaultVts,
            tilFaultSr, tilphnVts, tilphnSr, til_old_vltd_sr_no, til_new_vltd_sr_no, til_id_reinst, til_id_sr, til_sr_reinst,
            til_new_sr_replace, til_old_sr_replace, sensor_veh, sensor_veh_missing, sensor_veh_remove, sensor_veh_reinstall;
    String fuel_voltage = "0", path, drs_type, clientId, personName, personPhone, clientLocId, s_Vehicle_Name, drsStatus, device_type = "0", s_date, s_time, disttid, s_remove_reason, vts_id, user_id, uusername, version, selected_todate, selected_totime, current_date, fuel_sensor = "N", door_sensor = "N", veh_condition = "W", mgt_set = "N", sim_provider = "0", old_sim_no = "0", new_sim_no = "0", not_available_activity = "0", not_available_reason = "0", is_demo = "N", removal_type, baseImage = "", missing_type = "M", collection_amount, collection_date, collection_type, image, contact_person = "", contact_no = "0", payment_type = "C",
            buttonPressed = "0", buttonPressedActivity = "0", s_reg_no, s_rep_srNo, s_reinst_conf_reg_no, s_device_id, s_drs_id, s_new_drs_id, s_clientname = "SELECT CLIENT", s_remarks, status, s_work_type, s_Time, s_vehicletype="0", s_VehicleTypeInst, s_reason_repla = "0", removalReason = "0", itemsCollected = "0", others = "", s_work_id, s_new_device_id, s_e_device_id = "0", is_drs = "N", drs_dirction = "N", disconnection_reason = "0", ignition_sensor = "N", sim_reason = "0", missing_reason = "0", cut_off = "N", serial_no = "SELECT SR.NO.", confirmVehNo, panic = "N",
            s_old_serial_no="SELECT SR.NO.", s_vts_type = "SELECT VTS TYPE", tilt_sensor = "N", temp_sensor = "N", trans = "N", lid_status = "N", fuel_status = "N", panic_status = "N", sensor_old_veh_no, sen_vehicle_no, radioButtonChecked = "V", removeDeviceType = "D", missDeviceType = "D", reinstDevice = "D",id_dist,server_name,db_name,replace_type="D";
    CheckConnection chk;
    CheckBox check_tel_supprt, magnet_set, magnetset_install;
    EditText reinstallVoltage, installVoltage, vltd_sr_no_notAvail, e_reg_no, followUpPersonName, followUpPersonPhone, phSupportPersonName, phSupportPersonPhone, faultPersonName, faultPersonNumber, e_device_id, e_drs_id, e_remarks, old_deviceid, new_deviceid, fault_vts_id, t_install_date, t_install_Time, new_vehicleRegNo, remove_deviceid, remove_reg_no, old_deviceidreplace, new_deviceidReinstall, old_drsid, new_drsid, phsupport_vts_id, fault_reg_no, phSupport_reg_no, regNo, drs_vts_id, drs_veh_no, sim_vts_id, e_old_sim_no, e_new_sim_no, sim_vehicle_no, mDevice_vts_id, mDevice_reg_no, vehNotAvailVtsID, vehNotAvailRegNo,
            remove_sr_no, paymentDate, amount, vts_sr_no, vts_sr_no_reinst, con_in_reg_no, rep_srNo, reinst_conf_reg_no, old_sensor_veh_no, new_sensor_veh_no,con_vltd_sr_no,con_remove_sr_no,con_fault_sr_no,con_phone_sr_no,con_old_deviceidreplace,con_new_deviceid,old_vts_id_replace,new_vts_id_replace,con_old_vts_id_replace,con_new_vts_id_replace,
            vltd_sr_no, vltd_sr_no_fault, vltd_sr_no_miss, vltd_sr_no_phn, old_vltd_sr_no, new_vltd_sr_no, old_replace_sr_no,con_missing_sr_no,con_reinstall_sr_no,remove_vts_id,con_remove_vts_id,
            new_replace_sr_no, sensor_veh_no, sensor_veh_no_missing, sensor_veh_no_remove;
    MyTextView device_info, itemCollected;
    TextView plantName, imageName, imageNameFault, imageNameMissing, tv, payValue,text_to_show,text_to_show_remove,text_to_show_missing,
            text_to_show_fault,text_to_show_phone,text_to_show_replace_old,text_to_show_replace_new,text_to_show_reinstall;
    Button update_dataa, imageUpload, imageUploadfault, imageUploadMissing,delete_button,delete_button_e_series,delete_button_remove,delete_button_remove_e_series,delete_button_missing,delete_button_missing_e_series,delete_button_reinstall,
            delete_button_fault_e_series,delete_button_fault,delete_button_phone_e_series,delete_button_phone,delete_button_replace_old_serial,delete_button_replace_new_serial,delete_button_replace_old_id,delete_button_replace_new_id,delete_button_reinstall_ais;
    Calendar calen = Calendar.getInstance();
    int year, month, day, hour, minutes, seconds;
    NonScrollListView lv, lvItem;
    ArrayList<DeviceTypeOtherAis> deviceTypeOtherAis_arr = new ArrayList<>();
    ArrayList<UmVehicleDetail> getUmVehicle = new ArrayList<>();
    ArrayList<RemovalList> removalList = new ArrayList<>();
    ArrayList<RemovalList> damageList = new ArrayList<>();
    ArrayList<ReplaceReasonDetail> arr_replaceReasons = new ArrayList<>();
    ArrayList<FaultList> list_change_values = new ArrayList<>();
    ArrayList<SimDetail> simreplacereason = new ArrayList<>();
    ArrayList<SimOperatorDetail> simOperatorDetails = new ArrayList<>();
    ArrayList<VehNotAvailReasonDetail> vehNotAvailReasonDetails = new ArrayList<>();
    ArrayList<RemovalActivityDetail> removalActivityDetails = new ArrayList<>();
    ArrayList<ItemList> collected_items = new ArrayList<>();
    ArrayList<DisconnectionDetail> supportList = new ArrayList<>();
    ArrayList<WorkTypeDetail> workTypeList = new ArrayList<>();
    ArrayList<NotAvailActivityDetail> notAvailActivityDetails = new ArrayList<>();
    ArrayList<ClientDetails> clientList = new ArrayList<>();
    ArrayList<SrNoDeviceList> srnoList = new ArrayList<>();
    ArrayList<SrNoDeviceList> removesrnoList = new ArrayList<>();
    ArrayList<VTSDetail> vtsList = new ArrayList<>();
    ArrayList<PMethodDetail> paymentmethod = new ArrayList<>();
    ArrayList<VehicleTypeDetail> vehicleList = new ArrayList<>();
    ArrayList<ClientLocationDetail> locationList = new ArrayList<>();
    ArrayList<String> clientDetail = new ArrayList<>();
    ArrayList<String> srNoDetails = new ArrayList<>();
    ArrayList<String> removesrNoDetails = new ArrayList<>();
    ArrayList<String> workDetail = new ArrayList<>();
    ArrayList<String> removalDetail = new ArrayList<>();
    ArrayList<String> vehicleDetail = new ArrayList<>();
    ArrayList<String> locationDetail = new ArrayList<>();
    ArrayList<String> value_name = new ArrayList<>();
    ArrayList<String> disc_reason = new ArrayList<>();
    ArrayList<String> simReplaceReason = new ArrayList<>();
    ArrayList<String> simOperator = new ArrayList<>();
    ArrayList<String> pMethod = new ArrayList<>();
    ArrayList<String> vehNotAvailReason = new ArrayList<>();
    ArrayList<String> removalActivity = new ArrayList<>();
    ArrayList<String> item_name = new ArrayList<>();
    ArrayList<String> notAvailActivity = new ArrayList<>();
    ArrayList<String> arr_reasons_s = new ArrayList<>();
    ArrayList<String> arr_device_types = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ProgressDialog pDialog;
    Spinner device_reinstall, device, vltddeviceNotAvail, vltddevice, vltddeviceReinst, vltddeviceReplace, vltddeviceFault, vltddeviceMiss, vltddevicephn, vltddsimReplace, vltddeviceRemove;
    ProgressBar progressBar, circularProgressbar, mProgress;
    LinearLayout refuelVoltage, fuelVoltage, drsReInstall, payCollection, followUp, faultDetail, relaydrsType, drsReplacemsg, relaydrsTypeReplace, linearDoor, linearIgnition, drsInstall, linearPayment, linearvts, linearVehicleNotAvail, vehDetail, linearReplacement, linearInstall, linearReInstall, linearRemoval, linearDrs,linear_vts_id_replace,lin_vts_id_remove,
            linearFault, linearPhoneSupport, linearSimRepalace, linearDeviceMissing, linearOthers, oldDeviceType, options, vltdOptions, oldDevicesr_no, reinstText, deviceTypeReplace, replaceSrNo, lay_sensor_veh, linear_device_sr_no,linear_device_sr_no_e_series,linear_device_sr_no_remove,linear_device_sr_no_remove_e_series,linear_device_sr_no_fault_e_series,linear_device_sr_no_fault,
            linear_device_sr_no_phone,linear_device_sr_no_phone_e_series,linear_device_sr_no_replace_old,linear_device_sr_no_replace_new,linear_device_sr_no_missing_e_series,linear_device_sr_no_missing,linear_device_id_replace_old,linear_device_id_replace_new,linear_device_sr_no_reinstall,linear_device_sr_no_reinstall_e_series,linear_device_sr_no_reinstall_ais;
    RelativeLayout relativeCableConnected, relayLocation, relMissing, circularRelative;
    MySearchableSpinner client, vehicleType, workType, location, reason_replace, reason_remove, new_in_vehicleTypeReins, discReason, sim_replace_reason, sim_operator, vehiclenoavailSpinner, notAvailReason, removalType, missingType, payment_method, vehicleTypeFault, vehicleTypeSim, vehicle_list_um, vehicle_list_pm,sr_no,spn_remove_sr_no,spn_old_sr_no_replace,spn_new_sr_no_replace,
            spn_fault_sr_no,spn_phone_sr_no,spn_missing_sr_no,spn_reinstall_sr_no,spn_replace_sensor;
    RadioGroup radioGroup, radiogroupPay, radioGroupReinstall, drsReplace, radiodeviceType, radiodireplace, radiogroup, radioGrouptiltReinst,radiogroupdrsReinst, radioGroupfuelSensorReinst, radioGrouptempReinst, radioGrouptransReinst, radioGroupLidReinst, radioGrouptiltRemove, radioGrouptempRemove, radioGroupPanicRemove, radioGroupfuelRemove,
            radiogroupDoor, radioGroupCutoff, radiogroupCutOffReinst, is_Demo, radiodrsReInstall, radiodrsInstall, radioGroups, radioGrouptransRemove, radioGroupLidRemove, radioGrouptiltMissing, radioGrouptempMissing, radioGroupPanicMissing, radioGroupfuelMissing, radioGrouptransMissing, radioGroupLidMissing,
            radioGroupMissing, reinstDeviceType, radioGroupPanic, radioGroupFuel, radioGroupPanicReinst, radioGroupFuelReinst, radioGroupTypeRemove, radioGroupMiss, radioGroupReinstallType,radioGroupdrsMissing,radioGroupdrsRemove,
            radiovltdDevicetype, radioGrouptilt, radioGrouptrans, radioGrouptemp, radioGrouptiltReplace, radioGroupLid, radioGroupfuelSensor, radioGrouptempReplace, radioGroupPanicReplace, radioGrouptransReplace, radioGroupLidReplace;
    RadioButton radionormal, radiotype, old_Device, new_Device, radionormalrep, l_in, doorNo, cutoffNo, cut_off_no_reinst, radioNone, radioDevice, drs_no_reinst, panicNoReinst, tiltNoReinst, fuelSensorNewNoReinst, tempNoReinst, transNoReinst, lidNoneReinst,
            drsType, drsReeInstall, is_demo_no, damageDevice, voice, nonVoice, radionodrs, radioyesdrs, normal, reverse, tiltNoReplace, tempNoReplace, panicNoReplace, fuelSensorNoReplace, transNoReplace, lidNoneReplace, radioVTS, radioDeviceMiss, tiltMissingNo, tempNoMissing, lidNoneMissing,
            noreinst, radioyesdrsReInstall, replacenormal, replacereverse, nodrsReplace, radioyesdrsReplace, reinst_voice, radioDeviceRemove, tiltRemoveNo, tempNoRemove, panicNoRemove, fuelSensorNoRemove, transNoRemove, lidNoneRemove, panicNoMissing, fuelSensorNoMissing, transNoMissing,
            panicNo, fuelNo, fuelNoReinst, tiltNo, tempNo, tiltReplaceNo, transNo, lidNone, lidTop, lidRear, lidBoth, fuelSensorNewNo;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_new_install, container, false);
        chk = new CheckConnection(v.getContext());
        sharedprefs = getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        uusername = sharedprefs.getString("s_uuser", "");
        user_id = sharedprefs.getString("s_user_id", "");
        version = sharedprefs.getString("version", "");
        l_in = v.findViewById(R.id.l_in);
        regNo = v.findViewById(R.id.regNo);
        nodrsReplace = v.findViewById(R.id.nodrsReplace);
        radioyesdrsReplace = v.findViewById(R.id.radioyesdrsReplace);
        amount = v.findViewById(R.id.amount);
        lv = v.findViewById(R.id.other_list);
        doorNo = v.findViewById(R.id.doorNo);
        cutoffNo = v.findViewById(R.id.cutoffNo);
        cut_off_no_reinst = v.findViewById(R.id.cut_off_no_reinst);
        radioDevice = v.findViewById(R.id.radioDevice);
        drs_no_reinst = v.findViewById(R.id.drs_no_reinst);
        fuelNoReinst = v.findViewById(R.id.fuelNoReinst);
        panicNoReinst = v.findViewById(R.id.panicNoReinst);
        tempNoReinst = v.findViewById(R.id.tempNoReinst);
        tiltNoReinst = v.findViewById(R.id.tiltNoReinst);
        transNoReinst = v.findViewById(R.id.transNoReinst);
        fuelSensorNewNoReinst = v.findViewById(R.id.fuelSensorNewNoReinst);
        lidNoneReinst = v.findViewById(R.id.lidNoneReinst);
        tiltNoReplace = v.findViewById(R.id.tiltNoReplace);
        tempNoReplace = v.findViewById(R.id.tempNoReplace);
        panicNoReplace = v.findViewById(R.id.panicNoReplace);
        fuelSensorNoReplace = v.findViewById(R.id.fuelSensorNoReplace);
        transNoReplace = v.findViewById(R.id.transNoReplace);
        lidNoneReplace = v.findViewById(R.id.lidNoneReplace);
        noreinst = v.findViewById(R.id.noreinst);
        sensor_veh_no = v.findViewById(R.id.sensor_veh_no);
        sensor_veh_no_missing = v.findViewById(R.id.sensor_veh_no_missing);
        sensor_veh_no_remove = v.findViewById(R.id.sensor_veh_no_remove);
        sensor_veh = v.findViewById(R.id.sensor_veh);
        sensor_veh_missing = v.findViewById(R.id.sensor_veh_missing);
        sensor_veh_remove = v.findViewById(R.id.sensor_veh_remove);
        replacenormal = v.findViewById(R.id.replacenormal);
        replacereverse = v.findViewById(R.id.replacereverse);
        til_vts_sr_no = v.findViewById(R.id.til_vts_sr_no);
        radioyesdrsReInstall = v.findViewById(R.id.radioyesdrsReInstall);
        radioGrouptiltReplace = v.findViewById(R.id.radioGrouptiltReplace);
        til_no_avail_vts = v.findViewById(R.id.til_no_avail_vts);
        radioDeviceRemove = v.findViewById(R.id.radioDeviceRemove);
        tiltRemoveNo = v.findViewById(R.id.tiltRemoveNo);
        tempNoRemove = v.findViewById(R.id.tempNoRemove);
        panicNoRemove = v.findViewById(R.id.panicNoRemove);
        fuelSensorNoRemove = v.findViewById(R.id.fuelSensorNoRemove);
        transNoRemove = v.findViewById(R.id.transNoRemove);
        lidNoneRemove = v.findViewById(R.id.lidNoneRemove);
        radioDeviceMiss = v.findViewById(R.id.radioDeviceMiss);
        tiltMissingNo = v.findViewById(R.id.tiltMissingNo);
        tempNoMissing = v.findViewById(R.id.tempNoMissing);
        lidNoneMissing = v.findViewById(R.id.lidNoneMissing);
        panicNoMissing = v.findViewById(R.id.panicNoMissing);
        fuelSensorNoMissing = v.findViewById(R.id.fuelSensorNoMissing);
        transNoMissing = v.findViewById(R.id.transNoMissing);
        followUpPersonName = v.findViewById(R.id.followUpPersonName);
        followUpPersonPhone = v.findViewById(R.id.followUpPersonPhone);
        phSupportPersonName = v.findViewById(R.id.phSupportPersonName);
        phSupportPersonPhone = v.findViewById(R.id.phSupportPersonPhone);
        faultPersonName = v.findViewById(R.id.faultPersonName);
        faultPersonNumber = v.findViewById(R.id.faultPersonNumber);
        oldDevicesr_no = v.findViewById(R.id.oldDevicesr_no);
        vltddeviceReinst = v.findViewById(R.id.vltddeviceReinst);
        vltddeviceReplace = v.findViewById(R.id.vltddeviceReplace);
        vltddeviceFault = v.findViewById(R.id.vltddeviceFault);
        vltddeviceMiss = v.findViewById(R.id.vltddeviceMiss);
        refuelVoltage = v.findViewById(R.id.refuelVoltage);
        replaceSrNo = v.findViewById(R.id.replaceSrNo);
        device = v.findViewById(R.id.device);
        device_reinstall = v.findViewById(R.id.device_reinstall);
        vltddevicephn = v.findViewById(R.id.vltddevicephn);
        til_vts_remove = v.findViewById(R.id.til_vts_remove);
        til_remove_sr = v.findViewById(R.id.til_remove_sr);
        tilVoltage = v.findViewById(R.id.tilVoltage);
        til_new_replace = v.findViewById(R.id.til_new_replace);
        til_old_replace = v.findViewById(R.id.til_old_replace);
        radioVTS = v.findViewById(R.id.radioVTS);
        vltddeviceRemove = v.findViewById(R.id.vltddeviceRemove);
        tilDeviceMiss = v.findViewById(R.id.tilDeviceMiss);
        vltd_sr_no_miss = v.findViewById(R.id.vltd_sr_no_miss);
        vltddeviceNotAvail = v.findViewById(R.id.vltddeviceNotAvail);
        vltd_sr_no_notAvail = v.findViewById(R.id.vltd_sr_no_notAvail);
        tilsrNo_notAvail = v.findViewById(R.id.tilsrNo_notAvail);
        reinstallVoltage = v.findViewById(R.id.reinstallVoltage);
        fuelVoltage = v.findViewById(R.id.fuelVoltage);
        tilsrNo = v.findViewById(R.id.tilsrNo);
        tiltNo = v.findViewById(R.id.tiltNo);
        tempNo = v.findViewById(R.id.tempNo);
        remove_sr_no = v.findViewById(R.id.remove_sr_no);
        til_id_reinst = v.findViewById(R.id.til_id_reinst);
        til_sr_reinst = v.findViewById(R.id.til_sr_reinst);
        til_old_sr_replace = v.findViewById(R.id.til_old_sr_replace);
        til_new_sr_replace = v.findViewById(R.id.til_new_sr_replace);
        til_id_sr = v.findViewById(R.id.til_id_sr);
        reinstText = v.findViewById(R.id.reinstText);
        installVoltage = v.findViewById(R.id.installVoltage);
        til_old_vltd_sr_no = v.findViewById(R.id.til_old_vltd_sr_no);
        til_new_vltd_sr_no = v.findViewById(R.id.til_new_vltd_sr_no);
        old_vltd_sr_no = v.findViewById(R.id.old_vltd_sr_no);
        new_vltd_sr_no = v.findViewById(R.id.new_vltd_sr_no);
        tilFaultVts = v.findViewById(R.id.tilFaultVts);
        tilFaultSr = v.findViewById(R.id.tilFaultSr);
        tilphnVts = v.findViewById(R.id.tilphnVts);
        tilphnSr = v.findViewById(R.id.tilphnSr);
        tilvtsno = v.findViewById(R.id.tilvtsno);
        is_Demo = v.findViewById(R.id.is_demo);
        voice = v.findViewById(R.id.voice);
        normal = v.findViewById(R.id.normal);
        reverse = v.findViewById(R.id.reverse);
        nonVoice = v.findViewById(R.id.nonVoice);
        linearDrs = v.findViewById(R.id.linearDrs);
        linearvts = v.findViewById(R.id.linearvts);
        old_drsid = v.findViewById(R.id.old_drsid);
        new_drsid = v.findViewById(R.id.new_drsid);
        text1 = v.findViewById(R.id.text1);
        checked = v.findViewById(R.id.checked);
        lvItem = v.findViewById(R.id.item_list);
        payValue = v.findViewById(R.id.payValue);
        plantName = v.findViewById(R.id.plantName);
        vehDetail = v.findViewById(R.id.vehDetail);
        radionodrs = v.findViewById(R.id.radionodrs);
        vltd_sr_no = v.findViewById(R.id.vltd_sr_no);
        options = v.findViewById(R.id.options);
        vltdOptions = v.findViewById(R.id.vltdOptions);
        vehicleTypeSim = v.findViewById(R.id.vehicleTypeSim);
        vehicle_list_um = v.findViewById(R.id.vehicle_list_um);
        vehicle_list_pm = v.findViewById(R.id.vehicle_list_pm);
        radioyesdrs = v.findViewById(R.id.radioyesdrsInstall);
        imageName = v.findViewById(R.id.imageName);
        imageNameFault = v.findViewById(R.id.imageNameFault);
        imageNameMissing = v.findViewById(R.id.imageNameMissing);
        faultDetail = v.findViewById(R.id.faultDetail);
        linearDoor = v.findViewById(R.id.linearDoor);
        is_demo_no = v.findViewById(R.id.is_demo_no);
        drsInstall = v.findViewById(R.id.drsInstall);
        radiogroup = v.findViewById(R.id.radiogroup);
        radiogroupPay = v.findViewById(R.id.radiogroupPay);
        payCollection = v.findViewById(R.id.payCollection);
        oldDeviceType = v.findViewById(R.id.oldDeviceType);
        followUp = v.findViewById(R.id.followUp);
        magnet_set = v.findViewById(R.id.magnet_set);
        discReason = v.findViewById(R.id.discReason);
        drs_vts_id = v.findViewById(R.id.drs_vts_id);
        drs_veh_no = v.findViewById(R.id.drs_veh_no);
        old_Device = v.findViewById(R.id.old_Device);
        new_Device = v.findViewById(R.id.new_Device);
        radioGroup = v.findViewById(R.id.radiodirec);
        tnew_drsid = v.findViewById(R.id.tnew_drsid);
        relMissing = v.findViewById(R.id.relMissing);
        sim_vts_id = v.findViewById(R.id.sim_vts_id);
        client = v.findViewById(R.id.new_in_clients);
        e_reg_no = v.findViewById(R.id.new_in_reg_no);
        con_in_reg_no = v.findViewById(R.id.con_in_reg_no);
        deviceTypeReplace = v.findViewById(R.id.deviceTypeReplace);
        reinst_conf_reg_no = v.findViewById(R.id.reinst_conf_reg_no);
        vltd_sr_no_phn = v.findViewById(R.id.vltd_sr_no_phn);
        old_sensor_veh_no = v.findViewById(R.id.old_sensor_veh_no);
        new_sensor_veh_no = v.findViewById(R.id.new_sensor_veh_no);
        e_drs_id = v.findViewById(R.id.new_in_drs_id);
        paymentDate = v.findViewById(R.id.paymentDate);
        imageUpload = v.findViewById(R.id.imageUpload);
        imageUploadfault = v.findViewById(R.id.imageUploadfault);
        imageUploadMissing = v.findViewById(R.id.imageUploadMissing);
        vltd_sr_no_fault = v.findViewById(R.id.vltd_sr_no_fault);
        old_replace_sr_no = v.findViewById(R.id.old_replace_sr_no);
        new_replace_sr_no = v.findViewById(R.id.new_replace_sr_no);
        e_old_sim_no = v.findViewById(R.id.old_sim_no);
        e_new_sim_no = v.findViewById(R.id.new_sim_no);
        told_drsid = v.findViewById(R.id.t_old_drsid);
        missingType = v.findViewById(R.id.missingType);
        removalType = v.findViewById(R.id.removalType);
        device_info = v.findViewById(R.id.device_info);
        fault_reg_no = v.findViewById(R.id.fault_reg_no);
        radioGroups = v.findViewById(R.id.radioGroup);
        linearFault = v.findViewById(R.id.linearFault);
        drsReplacemsg = v.findViewById(R.id.drsReplace);
        progressBar = v.findViewById(R.id.progressBar);
        damageDevice = v.findViewById(R.id.damageDevice);
        fault_vts_id = v.findViewById(R.id.fault_vts_id);
        drsReplace = v.findViewById(R.id.radiodrsReplace);
        workType = v.findViewById(R.id.new_in_workType);
        relaydrsType = v.findViewById(R.id.relaydrsType);
        t_drs_veh_no = v.findViewById(R.id.t_drs_veh_no);
        t_drs_vts_id = v.findViewById(R.id.t_drs_vts_id);
        location = v.findViewById(R.id.new_in_locations);
        drsReInstall = v.findViewById(R.id.drsReInstall);
        old_deviceid = v.findViewById(R.id.old_deviceid);
        new_deviceid = v.findViewById(R.id.new_deviceid);
        sim_operator = v.findViewById(R.id.sim_operator);
        e_remarks = v.findViewById(R.id.new_in_remarks);
        update_dataa = v.findViewById(R.id.new_in_update);
        t_install_date = v.findViewById(R.id.installDate);
        t_install_Time = v.findViewById(R.id.installTime);
        e_device_id = v.findViewById(R.id.new_in_deviceid);
        vts_sr_no = v.findViewById(R.id.vts_sr_no);
        rep_srNo = v.findViewById(R.id.rep_srNo);
        vts_sr_no_reinst = v.findViewById(R.id.vts_sr_no_reinst);
        linearInstall = v.findViewById(R.id.linearInstall);
        linearPayment = v.findViewById(R.id.linearPayment);
        relayLocation = v.findViewById(R.id.relayLocation);
        linearRemoval = v.findViewById(R.id.linearRemoval);
        reason_replace = v.findViewById(R.id.replace_reason);
        reason_remove = v.findViewById(R.id.removal_Reason);
        remove_reg_no = v.findViewById(R.id.remove_reg_no);
        itemCollected = v.findViewById(R.id.itemCollected);
        payment_method = v.findViewById(R.id.payment_method);
        notAvailReason = v.findViewById(R.id.notAvailReason);
        radiogroupDoor = v.findViewById(R.id.radiogroupDoor);
        radioGroupFuel = v.findViewById(R.id.radioGroupFuel);
        radioGroupPanic = v.findViewById(R.id.radioGroupPanic);
        radioGroupCutoff = v.findViewById(R.id.radioGroupCutoff);
        radiogroupCutOffReinst = v.findViewById(R.id.radiogroupCutOffReinst);
        radioGroupPanicReinst = v.findViewById(R.id.radioGroupPanicReinst);
        radioGroupFuelReinst = v.findViewById(R.id.radioGroupFuelReinst);
        radioGroupTypeRemove = v.findViewById(R.id.radioGroupTypeRemove);
        radioGroupReinstallType = v.findViewById(R.id.radioGroupReinstallType);
        radioGroupMiss = v.findViewById(R.id.radioGroupMiss);
        radioGrouptemp = v.findViewById(R.id.radioGrouptemp);
        radioGrouptemp = v.findViewById(R.id.radioGrouptemp);
        radioGroupdrsMissing = v.findViewById(R.id.radioGroupdrsMissing);
        radioGroupdrsRemove = v.findViewById(R.id.radioGroupdrsRemove);
        linearIgnition = v.findViewById(R.id.linearIgnition);
        radiodireplace = v.findViewById(R.id.radiodireplace);
        radiodrsInstall = v.findViewById(R.id.radiodrsInstall);
        linearReInstall = v.findViewById(R.id.linearReInstall);
        radiodeviceType = v.findViewById(R.id.radiodeviceType);
        vehicleType = v.findViewById(R.id.new_in_vehicleType);
        vltddevice = v.findViewById(R.id.vltddevice);
        sim_vehicle_no = v.findViewById(R.id.sim_vehicle_no);
        mDevice_vts_id = v.findViewById(R.id.mDevice_vts_id);
        mDevice_reg_no = v.findViewById(R.id.mDevice_reg_no);
        vehNotAvailVtsID = v.findViewById(R.id.vehNotAvailVtsID);
        vehNotAvailRegNo = v.findViewById(R.id.vehNotAvailRegNo);
        remove_deviceid = v.findViewById(R.id.remove_deviceid);
        phSupport_reg_no = v.findViewById(R.id.phSupport_reg_no);
        vehicleTypeFault = v.findViewById(R.id.vehicleTypeFault);
        phsupport_vts_id = v.findViewById(R.id.phsupport_vts_id);
        check_tel_supprt = v.findViewById(R.id.check_tel_supprt);
        circularRelative = v.findViewById(R.id.circularRelative);
        new_vehicleRegNo = v.findViewById(R.id.new_vehicleRegNo);
        magnetset_install = v.findViewById(R.id.magnetset_install);
        radiodrsReInstall = v.findViewById(R.id.radiodrsReInstall);
        reinstDeviceType = v.findViewById(R.id.reinstDeviceType);
        radioGroupMissing = v.findViewById(R.id.radiogroupMissing);
        linearSimRepalace = v.findViewById(R.id.linearSimRepalace);
        sim_replace_reason = v.findViewById(R.id.sim_replace_reason);
        old_deviceidreplace = v.findViewById(R.id.old_deviceidreplace);
        relaydrsTypeReplace = v.findViewById(R.id.relaydrsTypeReplace);
        circularProgressbar = v.findViewById(R.id.circularProgressbar);
        linearPhoneSupport = v.findViewById(R.id.linearPhoneSupport);
        linearOthers = v.findViewById(R.id.linearOthers);
        linearReplacement = v.findViewById(R.id.linearReplacement);
        linearDeviceMissing = v.findViewById(R.id.linearDeviceMissing);
        radioGroupReinstall = v.findViewById(R.id.radioGroupReinstall);
        linearVehicleNotAvail = v.findViewById(R.id.linearVehicleNotAvail);
        vehiclenoavailSpinner = v.findViewById(R.id.vehiclenoavailSpinner);
        new_deviceidReinstall = v.findViewById(R.id.new_deviceidReinstall);
        relativeCableConnected = v.findViewById(R.id.relativeCableConnected);
        new_in_vehicleTypeReins = v.findViewById(R.id.new_in_vehicleTypeReins);
        radioGrouptilt = v.findViewById(R.id.radioGrouptilt);
        radioGrouptrans = v.findViewById(R.id.radioGrouptrans);
        radioGroupLid = v.findViewById(R.id.radioGroupLid);
        radioGroupfuelSensor = v.findViewById(R.id.radioGroupfuelSensor);
        radioGrouptempReplace = v.findViewById(R.id.radioGrouptempReplace);
        radioGroupPanicReplace = v.findViewById(R.id.radioGroupPanicReplace);
        radioGrouptransReplace = v.findViewById(R.id.radioGrouptransReplace);
        radioGroupLidReplace = v.findViewById(R.id.radioGroupLidReplace);
        radioGrouptiltReinst = v.findViewById(R.id.radioGrouptiltReinst);
        radiogroupdrsReinst = v.findViewById(R.id.radiogroupdrsReinst);
        radioGroupfuelSensorReinst = v.findViewById(R.id.radioGroupfuelSensorReinst);
        radioGrouptempReinst = v.findViewById(R.id.radioGrouptempReinst);
        radioGrouptransReinst = v.findViewById(R.id.radioGrouptransReinst);
        radioGroupLidReinst = v.findViewById(R.id.radioGroupLidReinst);
        radioGrouptiltRemove = v.findViewById(R.id.radioGrouptiltRemove);
        radioGrouptempRemove = v.findViewById(R.id.radioGrouptempRemove);
        radioGroupPanicRemove = v.findViewById(R.id.radioGroupPanicRemove);
        radioGroupfuelRemove = v.findViewById(R.id.radioGroupfuelRemove);
        radioGrouptransRemove = v.findViewById(R.id.radioGrouptransRemove);
        radioGroupLidRemove = v.findViewById(R.id.radioGroupLidRemove);
        radioGrouptiltMissing = v.findViewById(R.id.radioGrouptiltMissing);
        radioGrouptempMissing = v.findViewById(R.id.radioGrouptempMissing);
        radioGroupPanicMissing = v.findViewById(R.id.radioGroupPanicMissing);
        radioGroupfuelMissing = v.findViewById(R.id.radioGroupfuelMissing);
        radioGrouptransMissing = v.findViewById(R.id.radioGrouptransMissing);
        radioGroupLidMissing = v.findViewById(R.id.radioGroupLidMissing);
        lay_sensor_veh = v.findViewById(R.id.lay_sensor_veh);
        lidNone = v.findViewById(R.id.lidNone);
        lidTop = v.findViewById(R.id.lidTop);
        lidRear = v.findViewById(R.id.lidRear);
        lidBoth = v.findViewById(R.id.lidBoth);
        transNo = v.findViewById(R.id.transNo);
        sr_no = v.findViewById(R.id.sr_no);
        text_to_show = v.findViewById(R.id.text_to_show);
        text_to_show_remove = v.findViewById(R.id.text_to_show_remove);
        text_to_show_missing = v.findViewById(R.id.text_to_show_missing);
        text_to_show_fault = v.findViewById(R.id.text_to_show_fault);
        text_to_show_phone = v.findViewById(R.id.text_to_show_phone);
        text_to_show_replace_old = v.findViewById(R.id.text_to_show_replace_old);
        text_to_show_replace_new = v.findViewById(R.id.text_to_show_replace_new);
        text_to_show_reinstall = v.findViewById(R.id.text_to_show_reinstall);
        linear_device_sr_no = v.findViewById(R.id.linear_device_sr_no);
        linear_device_sr_no_e_series = v.findViewById(R.id.linear_device_sr_no_e_series);
        linear_device_sr_no_remove = v.findViewById(R.id.linear_device_sr_no_remove);
        linear_device_sr_no_remove_e_series = v.findViewById(R.id.linear_device_sr_no_remove_e_series);
        linear_device_sr_no_fault_e_series = v.findViewById(R.id.linear_device_sr_no_fault_e_series);
        linear_device_sr_no_phone = v.findViewById(R.id.linear_device_sr_no_phone);
        linear_device_sr_no_phone_e_series = v.findViewById(R.id.linear_device_sr_no_phone_e_series);
        linear_device_sr_no_replace_old = v.findViewById(R.id.linear_device_sr_no_replace_old);
        linear_device_sr_no_replace_new = v.findViewById(R.id.linear_device_sr_no_replace_new);
        linear_device_sr_no_missing_e_series = v.findViewById(R.id.linear_device_sr_no_missing_e_series);
        linear_device_sr_no_missing = v.findViewById(R.id.linear_device_sr_no_missing);
        linear_device_id_replace_old = v.findViewById(R.id.linear_device_id_replace_old);
        linear_device_id_replace_new = v.findViewById(R.id.linear_device_id_replace_new);
        linear_device_sr_no_reinstall = v.findViewById(R.id.linear_device_sr_no_reinstall);
        linear_device_sr_no_reinstall_ais = v.findViewById(R.id.linear_device_sr_no_reinstall_ais);
        linear_device_sr_no_fault = v.findViewById(R.id.linear_device_sr_no_fault);
        con_vltd_sr_no = v.findViewById(R.id.con_vltd_sr_no);
        con_remove_sr_no = v.findViewById(R.id.con_remove_sr_no);
        con_missing_sr_no = v.findViewById(R.id.con_missing_sr_no);
        con_reinstall_sr_no = v.findViewById(R.id.con_reinstall_sr_no);
        con_fault_sr_no = v.findViewById(R.id.con_fault_sr_no);
        con_phone_sr_no = v.findViewById(R.id.con_phone_sr_no);
        con_old_deviceidreplace = v.findViewById(R.id.con_old_deviceidreplace);
        con_new_deviceid = v.findViewById(R.id.con_new_deviceid);
        delete_button = v.findViewById(R.id.delete_button);
        delete_button_e_series = v.findViewById(R.id.delete_button_e_series);
        delete_button_remove = v.findViewById(R.id.delete_button_remove);
        delete_button_remove_e_series = v.findViewById(R.id.delete_button_remove_e_series);
        delete_button_fault_e_series = v.findViewById(R.id.delete_button_fault_e_series);
        delete_button_fault = v.findViewById(R.id.delete_button_fault);
        delete_button_missing = v.findViewById(R.id.delete_button_missing);
        delete_button_missing_e_series = v.findViewById(R.id.delete_button_missing_e_series);
        delete_button_phone_e_series = v.findViewById(R.id.delete_button_phone_e_series);
        delete_button_reinstall = v.findViewById(R.id.delete_button_reinstall);
        delete_button_phone = v.findViewById(R.id.delete_button_phone);
        delete_button_replace_old_serial = v.findViewById(R.id.delete_button_replace_old_serial);
        delete_button_replace_new_serial = v.findViewById(R.id.delete_button_replace_new_serial);
        delete_button_replace_old_id = v.findViewById(R.id.delete_button_replace_old_id);
        delete_button_replace_new_id = v.findViewById(R.id.delete_button_replace_new_id);
        delete_button_reinstall_ais = v.findViewById(R.id.delete_button_reinstall_ais);
        spn_remove_sr_no = v.findViewById(R.id.spn_remove_sr_no);
        spn_missing_sr_no = v.findViewById(R.id.spn_missing_sr_no);
        spn_reinstall_sr_no = v.findViewById(R.id.spn_reinstall_sr_no);
        spn_replace_sensor = v.findViewById(R.id.spn_replace_sensor);
        spn_fault_sr_no = v.findViewById(R.id.spn_fault_sr_no);
        spn_phone_sr_no = v.findViewById(R.id.spn_phone_sr_no);
        spn_old_sr_no_replace = v.findViewById(R.id.spn_old_sr_no_replace);
        spn_new_sr_no_replace = v.findViewById(R.id.spn_new_sr_no_replace);
        til_vts_miss = v.findViewById(R.id.til_vts_miss);
        fuelSensorNewNo = v.findViewById(R.id.fuelSensorNewNo);
        old_vts_id_replace = v.findViewById(R.id.old_vts_id_replace);
        new_vts_id_replace = v.findViewById(R.id.new_vts_id_replace);
        con_new_vts_id_replace = v.findViewById(R.id.con_new_vts_id_replace);
        con_old_vts_id_replace = v.findViewById(R.id.con_old_vts_id_replace);
        linear_vts_id_replace = v.findViewById(R.id.linear_vts_id_replace);
        lin_vts_id_remove = v.findViewById(R.id.lin_vts_id_remove);
        remove_vts_id = v.findViewById(R.id.remove_vts_id);
        con_remove_vts_id = v.findViewById(R.id.con_remove_vts_id);
        t_install_Time.setInputType(InputType.TYPE_NULL);
        t_install_date.setInputType(InputType.TYPE_NULL);
        paymentDate.setInputType(InputType.TYPE_NULL);
        newInstallmentController = new NewInstallmentController();
        receiveDeviceControllers = new ReceiveDeviceControllers();
        progressDialog = new SpotsDialog(getActivity(), R.style.CustomInstallation);
        fuelVoltage.setVisibility(View.GONE);
        refuelVoltage.setVisibility(View.GONE);
        tilsrNo.setVisibility(View.GONE);
        til_vts_sr_no.setVisibility(View.GONE);
        til_id_sr.setVisibility(View.GONE);
        tilphnSr.setVisibility(View.GONE);
        tilDeviceMiss.setVisibility(View.GONE);
        ShowProgressBar(false);
        Progress(false);

        setDateAndTime();
        location.setEnabled(false);
        workType.setEnabled(false);
        if (chk.isConnected()) {
            addclients();
            addWorkType();
        } else {
            chk.showConnectionErrorDialog();
        }

        e_remarks.setOnTouchListener((v, event) -> {
            if (e_remarks.hasFocus()) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_SCROLL:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                }
            }
            return false;
        });
        client.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                clientId = String.valueOf(clientList.get(i).getClient_Id());
                s_clientname = clientList.get(i).getClient_Name();
                id_dist = clientList.get(i).getId_dist();
                server_name = clientList.get(i).getServer_name();
                db_name = clientList.get(i).getDb_name();
                clearData();
                addLocation();
                location.setEnabled(true);
                drsStatus = String.valueOf(clientList.get(i).getDrs_status());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        device.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                device_type = deviceTypeOtherAis_arr.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        device_reinstall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                device_type = deviceTypeOtherAis_arr.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        removalType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                removal_type = String.valueOf(removalActivityDetails.get(i).getA_id());
                remove_reg_no.setText("");
                remove_deviceid.setText("");
                e_remarks.setText("");
                if (removal_type.equals("1")) {
                    itemCollected.setText("Items Handed Over");
                } else {
                    itemCollected.setText("Items Collected");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        payment_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                collection_type = String.valueOf(paymentmethod.get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        discReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                disconnection_reason = String.valueOf((supportList.get(i).getId()));
                if (discReason.getSelectedItem().toString().equalsIgnoreCase("Power Cable Disconnection")) {
                    relativeCableConnected.setVisibility(View.GONE);
                } else {
                    relativeCableConnected.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                clientLocId = String.valueOf((locationList.get(i).getLoc_Id()));
                workType.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        reason_remove.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                removalReason = String.valueOf((removalList.get(i).getRemoval_Id()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        missingType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                missing_reason = String.valueOf((damageList.get(i).getRemoval_Id()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        sim_replace_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                sim_reason = simreplacereason.get(i).getS_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        sim_operator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                sim_provider = simOperatorDetails.get(i).getSp_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        vehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                s_vehicletype = String.valueOf(vehicleList.get(i).getVehicle_Id());
                s_Vehicle_Name = vehicleList.get(i).getVehicle_Name();
                drs_type = vehicleList.get(i).getDrs_type();
                if (drs_type.equalsIgnoreCase("Y")) {
                    drsInstall.setVisibility(View.VISIBLE);
                } else {
                    drsInstall.setVisibility(View.GONE);
                    linearDrs.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        vehicleTypeFault.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                s_vehicletype = String.valueOf(vehicleList.get(i).getVehicle_Id());
                s_Vehicle_Name = vehicleList.get(i).getVehicle_Name();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        vehicleTypeSim.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                s_vehicletype = String.valueOf(vehicleList.get(i).getVehicle_Id());
                s_Vehicle_Name = vehicleList.get(i).getVehicle_Name();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        new_in_vehicleTypeReins.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                s_vehicletype = String.valueOf(vehicleList.get(i).getVehicle_Id());
                s_Vehicle_Name = vehicleList.get(i).getVehicle_Name();
                drs_type = vehicleList.get(i).getDrs_type();
                if (drs_type.equalsIgnoreCase("Y")) {
                    drsReInstall.setVisibility(View.VISIBLE);
                } else {
                    drsReInstall.setVisibility(View.GONE);
                    linearDrs.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        vehiclenoavailSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                not_available_activity = String.valueOf(notAvailActivityDetails.get(i).getId());
                if (not_available_activity.equals("1") || (not_available_activity.equals("2"))) {
                    vehDetail.setVisibility(View.GONE);
                    s_vehicletype = "0";
                    device_type = "0";
                } else {
                    vehDetail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        notAvailReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                not_available_reason = vehNotAvailReasonDetails.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        workType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                s_work_type = workType.getSelectedItem().toString();
                s_work_id = String.valueOf(workTypeList.get(i).getWork_Id());
                if (s_work_id.equalsIgnoreCase("3")) {
                    device_info.setText("Replacement Details");
                    e_remarks.setHint("Remarks");
                    RadioButton radio = v.findViewById(R.id.radioVTS);
                    radio.setChecked(true);
                    int DELAY = 500;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clearData();
                            mgt_set = "N";
                            linearReplacement.setVisibility(View.VISIBLE);
                            linearPhoneSupport.setVisibility(View.GONE);
                            linearReInstall.setVisibility(View.GONE);
                            linearInstall.setVisibility(View.GONE);
                            linearRemoval.setVisibility(View.GONE);
                            linearFault.setVisibility(View.GONE);
                            linearDrs.setVisibility(View.GONE);
                            old_drsid.setVisibility(View.GONE);
                            new_drsid.setVisibility(View.GONE);
                            magnet_set.setVisibility(View.GONE);
                            magnetset_install.setVisibility(View.GONE);
                            relaydrsTypeReplace.setVisibility(View.GONE);
                            linearSimRepalace.setVisibility(View.GONE);
                            drsInstall.setVisibility(View.GONE);
                            linearDrs.setVisibility(View.GONE);
                            linearDeviceMissing.setVisibility(View.GONE);
                            linearVehicleNotAvail.setVisibility(View.GONE);
                            vehDetail.setVisibility(View.GONE);
                            linearPayment.setVisibility(View.GONE);
                            linearOthers.setVisibility(View.GONE);
                            imageName.setText("");
                            imageNameMissing.setText("");
                            imageNameFault.setText("");
                            fetchReasons();
                            getSerialNo();
                            ShowProgressBar(false);
                        }
                    }, DELAY);
                    old_deviceidreplace.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                vts_id = old_deviceidreplace.getText().toString();
                                getVTSDetail();
                            } else {
                                regNo.setText("");
                                plantName.setText("");
                                // drsReplacemsg.setVisibility(View.GONE);
                            }
                            regNo.setOnTouchListener((v1, event) -> {
                                regNo.setText("");
                                regNo.setTextColor(getResources().getColor(R.color.black));
                                return false;
                            });
                        }
                    });
                    old_replace_sr_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean b) {
                            if (!b) {
                                vts_id = old_replace_sr_no.getText().toString();
                                getVTSDetail();
                            } else {
                                regNo.setText("");
                                plantName.setText("");
                            }
                            regNo.setOnTouchListener((v1, event) -> {
                                regNo.setText("");
                                regNo.setTextColor(getResources().getColor(R.color.black));
                                return false;
                            });
                        }
                    });

                    radioGrouptiltReplace.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.tiltNoReplace) {
                                tilt_sensor = "N";
                                sensor_veh.setVisibility(View.GONE);
                            } else {
                                tilt_sensor = "Y";
                                sensor_veh.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGrouptempReplace.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.tempNoReplace) {
                                temp_sensor = "N";
                                sensor_veh.setVisibility(View.GONE);
                            } else {
                                temp_sensor = "Y";
                                sensor_veh.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGroupPanicReplace.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.panicNoReplace) {
                                panic_status = "N";
                                sensor_veh.setVisibility(View.GONE);
                            } else {
                                panic_status = "Y";
                                sensor_veh.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGrouptransReplace.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {

                            if (id == R.id.transNoReplace) {
                                trans = "N";
                                sensor_veh.setVisibility(View.GONE);
                            } else {
                                trans = "Y";
                                sensor_veh.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGroupLidReplace.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.lidNoneReplace) {
                                lid_status = "N";
                                sensor_veh.setVisibility(View.GONE);
                            } else if (id == R.id.lidTopReplace) {
                                lid_status = "T";
                                sensor_veh.setVisibility(View.VISIBLE);
                            } else if (id == R.id.lidRearReplace) {
                                lid_status = "R";
                                sensor_veh.setVisibility(View.VISIBLE);
                            } else {
                                lid_status = "B";
                                sensor_veh.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    vltddeviceReplace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i == 0) {
                                return;
                            } else {
                                i = i - 1;
                            }
                            clearTextView();
                            if (vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("AIS 140")) {
                                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
                                linear_vts_id_replace.setVisibility(View.GONE);
                                linear_device_id_replace_old.setVisibility(View.GONE);
                                linear_device_id_replace_new.setVisibility(View.GONE);
                                linear_device_sr_no_replace_old.setVisibility(View.GONE);
                                linear_device_sr_no_replace_new.setVisibility(View.GONE);
                            } else {
                                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
                                linear_vts_id_replace.setVisibility(View.VISIBLE);
                                linear_device_id_replace_old.setVisibility(View.GONE);
                                linear_device_id_replace_new.setVisibility(View.GONE);
                                linear_device_sr_no_replace_old.setVisibility(View.GONE);
                                linear_device_sr_no_replace_new.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    spn_old_sr_no_replace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                            if (i == 0) {
                                return;
                            } else {
                                i = i - 1;
                            }
                                s_old_serial_no = removesrnoList.get(i).getPcb_sr_no();
                                s_e_device_id="0";
                                vts_id = s_old_serial_no;
                                getVTSDetail();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    spn_new_sr_no_replace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                            if (i == 0) {
                                return;
                            } else {
                                i = i - 1;
                            }
                                serial_no = srnoList.get(i).getPcb_sr_no();
                                vts_id = serial_no;
                                getVTSDetail();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    text_to_show_replace_old.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("AIS 140")){
                                linear_device_sr_no_replace_old.setVisibility(View.VISIBLE);
                                linear_device_id_replace_old.setVisibility(View.GONE);
                            }
                            else {
                                linear_device_id_replace_old.setVisibility(View.VISIBLE);
                                linear_device_sr_no_replace_old.setVisibility(View.GONE);
                            }
                        }
                    });

                    text_to_show_replace_new.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("AIS 140")){
                                linear_device_sr_no_replace_new.setVisibility(View.VISIBLE);
                                linear_device_id_replace_new.setVisibility(View.GONE);
                            } else {
                                linear_device_sr_no_replace_new.setVisibility(View.GONE);
                                linear_device_id_replace_new.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    delete_button_replace_old_id.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            linear_device_id_replace_old.setVisibility(View.GONE);
                        }
                    });

                    delete_button_replace_new_id.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            linear_device_id_replace_new.setVisibility(View.GONE);
                        }
                    });

                    delete_button_replace_old_serial.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            linear_device_sr_no_replace_old.setVisibility(View.GONE);
                        }
                    });

                    delete_button_replace_new_serial.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            linear_device_sr_no_replace_new.setVisibility(View.GONE);
                        }
                    });

                    radioGroups.setOnCheckedChangeListener((radioGroup, i12) -> {
                        if (i12 == R.id.radioVTS) {
                            clearData();
                            replace_type="D";
                            radioButtonChecked = "V";
                            linearvts.setVisibility(View.VISIBLE);
                            told_drsid.setVisibility(View.GONE);
                            tnew_drsid.setVisibility(View.GONE);
                            old_drsid.setVisibility(View.GONE);
                            new_drsid.setVisibility(View.GONE);
                            magnet_set.setVisibility(View.GONE);
                            drs_vts_id.setVisibility(View.GONE);
                            drs_veh_no.setVisibility(View.GONE);
                            t_drs_veh_no.setVisibility(View.GONE);
                            t_drs_vts_id.setVisibility(View.GONE);
                            relaydrsTypeReplace.setVisibility(View.GONE);
                            drsReplacemsg.setVisibility(View.GONE);
                            drsInstall.setVisibility(View.GONE);
                            linearDrs.setVisibility(View.GONE);
                            linearDeviceMissing.setVisibility(View.GONE);
                            linearVehicleNotAvail.setVisibility(View.GONE);
                            vehDetail.setVisibility(View.GONE);
                            linearPayment.setVisibility(View.GONE);
                            linearOthers.setVisibility(View.GONE);
                            imageName.setText("");
                            imageNameMissing.setText("");
                            imageNameFault.setText("");
                            deviceTypeReplace.setVisibility(View.VISIBLE);
                        } else if (i12 == R.id.radioDRS) {
                            clearData();
                            replace_type="S";
                            radioButtonChecked = "D";
                            linearvts.setVisibility(View.GONE);
                            told_drsid.setVisibility(View.VISIBLE);
                            tnew_drsid.setVisibility(View.VISIBLE);
                            old_drsid.setVisibility(View.VISIBLE);
                            new_drsid.setVisibility(View.VISIBLE);
                            magnet_set.setVisibility(View.VISIBLE);
                            drs_vts_id.setVisibility(View.VISIBLE);
                            drs_veh_no.setVisibility(View.VISIBLE);
                            t_drs_veh_no.setVisibility(View.VISIBLE);
                            t_drs_vts_id.setVisibility(View.VISIBLE);
                            relaydrsTypeReplace.setVisibility(View.VISIBLE);
                            drsReplacemsg.setVisibility(View.VISIBLE);
                            drsInstall.setVisibility(View.GONE);
                            linearDrs.setVisibility(View.GONE);
                            linearDeviceMissing.setVisibility(View.GONE);
                            linearVehicleNotAvail.setVisibility(View.GONE);
                            vehDetail.setVisibility(View.GONE);
                            linearPayment.setVisibility(View.GONE);
                            linearOthers.setVisibility(View.GONE);
                            imageName.setText("");
                            imageNameMissing.setText("");
                            imageNameFault.setText("");
                        } else if (i12 == R.id.radioBoth) {
                            clearData();
                            replace_type="B";
                            radioButtonChecked = "B";
                            til_old_replace.setVisibility(View.VISIBLE);
                            til_new_replace.setVisibility(View.VISIBLE);
                            if (rep_srNo.getVisibility() == View.GONE) {
                                rep_srNo.setVisibility(View.VISIBLE);
                            }
                            //s_vts_type = "2";
                            linearvts.setVisibility(View.VISIBLE);
                            told_drsid.setVisibility(View.VISIBLE);
                            tnew_drsid.setVisibility(View.VISIBLE);
                            old_drsid.setVisibility(View.VISIBLE);
                            new_drsid.setVisibility(View.VISIBLE);
                            magnet_set.setVisibility(View.VISIBLE);
                            drs_vts_id.setVisibility(View.GONE);
                            drs_veh_no.setVisibility(View.GONE);
                            t_drs_veh_no.setVisibility(View.GONE);
                            t_drs_vts_id.setVisibility(View.GONE);
                            relaydrsTypeReplace.setVisibility(View.VISIBLE);
                            drsReplacemsg.setVisibility(View.VISIBLE);
                            drsInstall.setVisibility(View.GONE);
                            linearDrs.setVisibility(View.GONE);
                            linearDeviceMissing.setVisibility(View.GONE);
                            linearVehicleNotAvail.setVisibility(View.GONE);
                            vehDetail.setVisibility(View.GONE);
                            linearPayment.setVisibility(View.GONE);
                            linearOthers.setVisibility(View.GONE);
                            imageName.setText("");
                            imageNameMissing.setText("");
                            imageNameFault.setText("");
                            old_deviceidreplace.setVisibility(View.VISIBLE);
                            new_deviceid.setVisibility(View.VISIBLE);
                            replaceSrNo.setVisibility(View.GONE);
                        } else {
                            radioButtonChecked = "N";
                        }
                        drs_vts_id.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i12, int i1, int i2) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i12, int i1, int i2) {
                                vts_id = drs_vts_id.getText().toString();
                                if (vts_id.equals("")) {
                                    drs_veh_no.setText("");
                                } else {
                                    getVTSDetails();
                                }
                            }
                            @Override
                            public void afterTextChanged(Editable editable) {
                            }
                        });
                        drs_veh_no.setOnTouchListener((v, event) -> {
                            drs_veh_no.setText("");
                            drs_veh_no.setTextColor(getResources().getColor(R.color.black));
                            return false;
                        });
                    });
                } else if (s_work_id.equalsIgnoreCase("2")) {
                    device_info.setText("Installation Details");
                    e_remarks.setHint("Remarks");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        clearData();
                        e_device_id.setInputType(InputType.TYPE_CLASS_NUMBER);
                        linearPhoneSupport.setVisibility(View.GONE);
                        linearReplacement.setVisibility(View.GONE);
                        linearInstall.setVisibility(View.VISIBLE);
                        linearReInstall.setVisibility(View.GONE);
                        linearRemoval.setVisibility(View.GONE);
                        linearFault.setVisibility(View.GONE);
                        linearSimRepalace.setVisibility(View.GONE);
                        magnetset_install.setVisibility(View.GONE);
                        drsInstall.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearDeviceMissing.setVisibility(View.GONE);
                        linearVehicleNotAvail.setVisibility(View.GONE);
                        vehDetail.setVisibility(View.GONE);
                        linearPayment.setVisibility(View.GONE);
                        linearOthers.setVisibility(View.GONE);
                        imageName.setText("");
                        imageNameMissing.setText("");
                        imageNameFault.setText("");
                        addVehType();
                        getDevice();
                        getSerialNo();
                        ShowProgressBar(false);
                    }, DELAY);
                    radiodrsInstall.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            if (i == R.id.radioyesdrsInstall) {
                                linearDrs.setVisibility(View.VISIBLE);
                                magnetset_install.setVisibility(View.VISIBLE);
                            } else {
                                linearDrs.setVisibility(View.GONE);
                                magnetset_install.setVisibility(View.GONE);
                            }
                        }
                    });

                    sr_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                            if (i == 0) {
                                return;
                            } else {
                                i = i - 1;
                            }
                            serial_no = srnoList.get(i).getPcb_sr_no();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    radioGroupFuel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int i) {
                            if (i == R.id.fuelNo) {
                                fuelVoltage.setVisibility(View.GONE);
                            } else {
                                fuelVoltage.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGrouptilt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.tiltNo) {
                                tilt_sensor = "N";
                            } else {
                                tilt_sensor = "Y";
                            }
                        }
                    });

                    radioGrouptemp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.tempNo) {
                                temp_sensor = "N";
                            } else {
                                temp_sensor = "Y";
                            }
                        }
                    });

                    radioGrouptrans.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {

                            if (id == R.id.transNo) {
                                trans = "N";
                            } else {
                                trans = "Y";
                            }
                        }
                    });

                    radioGroupfuelSensor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.fuelSensorNewNo) {
                                fuel_status = "N";
                            } else {
                                fuel_status = "Y";
                            }
                        }
                    });

                    radioGroupLid.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.lidNone) {
                                lid_status = "N";
                            } else if (id == R.id.lidTop) {
                                lid_status = "T";
                            } else if (id == R.id.lidRear) {
                                lid_status = "R";
                            } else {
                                lid_status = "B";
                            }
                        }
                    });

                    vltddevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i == 0) {
                                return;
                            } else {
                                i = i - 1;
                            }
                            if (vltddevice.getSelectedItem().toString().equalsIgnoreCase("E124")) {
                                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
                                tilsrNo.setVisibility(View.GONE);
                                tilvtsno.setVisibility(View.VISIBLE);
                                oldDeviceType.setVisibility(View.VISIBLE);
                                options.setVisibility(View.VISIBLE);
                                vltdOptions.setVisibility(View.VISIBLE);
                                e_device_id.setVisibility(View.VISIBLE);
                                vts_sr_no.setVisibility(View.VISIBLE);
                                til_vts_sr_no.setVisibility(View.GONE);
                                vltd_sr_no.setVisibility(View.GONE);
                            } else {
                                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
                                tilsrNo.setVisibility(View.GONE);
                                tilvtsno.setVisibility(View.GONE);
                                oldDeviceType.setVisibility(View.GONE);
                                vltdOptions.setVisibility(View.VISIBLE);
                                e_device_id.setVisibility(View.GONE);
                                s_new_device_id = "0";
                                vts_sr_no.setVisibility(View.GONE);
                                til_vts_sr_no.setVisibility(View.GONE);
                                vltd_sr_no.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    text_to_show.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(vltddevice.getSelectedItem().toString().equalsIgnoreCase("E124")){
                                til_vts_sr_no.setVisibility(View.VISIBLE);
                                linear_device_sr_no.setVisibility(View.GONE);
                                linear_device_sr_no_e_series.setVisibility(View.VISIBLE);
                            }else {
                                linear_device_sr_no.setVisibility(View.VISIBLE);
                                linear_device_sr_no_e_series.setVisibility(View.GONE);
                                tilsrNo.setVisibility(View.VISIBLE);
                                vltd_sr_no.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    delete_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            vltd_sr_no.setText("");
                            linear_device_sr_no.setVisibility(View.GONE);
                        }
                    });

                    delete_button_e_series.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            vts_sr_no.setText("");
                            linear_device_sr_no_e_series.setVisibility(View.GONE);
                        }
                    });

                } else if (s_work_id.equalsIgnoreCase("4")) {
                    device_info.setText("ReInstallation Details");
                    e_remarks.setHint("Remarks");
                    old_deviceid.setHint("Device ID");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        clearData();
                        linearReInstall.setVisibility(View.VISIBLE);
                        linearPhoneSupport.setVisibility(View.GONE);
                        linearReplacement.setVisibility(View.GONE);
                        linearInstall.setVisibility(View.GONE);
                        linearRemoval.setVisibility(View.GONE);
                        linearFault.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearSimRepalace.setVisibility(View.GONE);
                        magnetset_install.setVisibility(View.GONE);
                        drsInstall.setVisibility(View.GONE);
                        drsReInstall.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearDeviceMissing.setVisibility(View.GONE);
                        linearVehicleNotAvail.setVisibility(View.GONE);
                        vehDetail.setVisibility(View.GONE);
                        linearPayment.setVisibility(View.GONE);
                        linearOthers.setVisibility(View.GONE);
                        imageName.setText("");
                        imageNameMissing.setText("");
                        imageNameFault.setText("");
                        addVehType();
                        getSerialNo();
                        ShowProgressBar(false);
                    }, DELAY);

                    radioGroupReinstallType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.radioDevice) {
                                reinstDevice = "D";
                            } else if (id == R.id.radioSensor) {
                                reinstDevice = "S";
                            } else {
                                reinstDevice = "B";
                            }
                        }
                    });

                    radioGroupReinstall.setOnCheckedChangeListener((radioGroup, i14) -> {
                        if (i14 == R.id.old_Device) {
                            new_deviceidReinstall.setVisibility(View.GONE);
                            til_id_sr.setVisibility(View.GONE);
                            new_deviceidReinstall.setText("");
                            old_deviceid.setHint("Device ID");
                        } else {
                            new_deviceidReinstall.setVisibility(View.VISIBLE);
                            til_id_sr.setVisibility(View.VISIBLE);
                            old_deviceid.setHint("Old Device ID");
                            old_deviceid.setText("");
                            new_vehicleRegNo.setText("");
                        }
                    });
                    radioGroupFuelReinst.setOnCheckedChangeListener((radioGroup, i14) -> {
                        if (i14 == R.id.fuelNoReinst) {
                            refuelVoltage.setVisibility(View.GONE);
                        } else {
                            refuelVoltage.setVisibility(View.VISIBLE);
                        }
                    });

                    radiogroupdrsReinst.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.drs_no_reinst) {
                                drsStatus = "N";
                                lay_sensor_veh.setVisibility(View.GONE);
                            } else {
                                drsStatus = "Y";
                                lay_sensor_veh.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGroupPanicReinst.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.drs_no_reinst) {
                                panic_status = "N";
                                lay_sensor_veh.setVisibility(View.GONE);
                            } else {
                                panic_status = "Y";
                                lay_sensor_veh.setVisibility(View.VISIBLE);
                            }
                        }
                    });


                    radioGrouptiltReinst.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.tiltNoReinst) {
                                tilt_sensor = "N";
                                lay_sensor_veh.setVisibility(View.GONE);
                            } else {
                                tilt_sensor = "Y";
                                lay_sensor_veh.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGroupfuelSensorReinst.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.fuelSensorNewNoReinst) {
                                fuel_status = "N";
                                lay_sensor_veh.setVisibility(View.GONE);
                            } else {
                                fuel_status = "Y";
                                lay_sensor_veh.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGrouptempReinst.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.tempNoReinst) {
                                temp_sensor = "N";
                                lay_sensor_veh.setVisibility(View.GONE);
                            } else {
                                temp_sensor = "Y";
                                lay_sensor_veh.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGrouptransReinst.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.transNoReinst) {
                                trans = "N";
                                lay_sensor_veh.setVisibility(View.GONE);
                            } else {
                                trans = "Y";
                                lay_sensor_veh.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGroupLidReinst.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.lidNoneReinst) {
                                lid_status = "N";
                                lay_sensor_veh.setVisibility(View.GONE);
                            } else if (id == R.id.lidTopReinst) {
                                lid_status = "T";
                                lay_sensor_veh.setVisibility(View.VISIBLE);
                            } else if (id == R.id.lidRearReinst) {
                                lid_status = "R";
                                lay_sensor_veh.setVisibility(View.VISIBLE);
                            } else {
                                lid_status = "B";
                                lay_sensor_veh.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    vltddeviceReinst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i == 0) {
                                return;
                            } else {
                                i = i - 1;
                            }
                            if (vltddeviceReinst.getSelectedItem().toString().equalsIgnoreCase("AIS 140")) {
                                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
                                getSerialNo();
                                reinstText.setVisibility(View.GONE);
                                til_id_reinst.setVisibility(View.GONE);
                                til_id_sr.setVisibility(View.GONE);
                                til_sr_reinst.setVisibility(View.GONE);
                                til_old_vltd_sr_no.setVisibility(View.VISIBLE);
                                //til_new_vltd_sr_no.setVisibility(View.VISIBLE);
                                new_deviceidReinstall.setVisibility(View.GONE);
                                til_id_sr.setVisibility(View.GONE);
                            } else {
                                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
                                getSerialNo();
                                reinstText.setVisibility(View.VISIBLE);
                                til_id_reinst.setVisibility(View.VISIBLE);
                                til_id_sr.setVisibility(View.VISIBLE);
                                til_sr_reinst.setVisibility(View.VISIBLE);
                                til_old_vltd_sr_no.setVisibility(View.GONE);
                                til_new_vltd_sr_no.setVisibility(View.GONE);
                                til_id_sr.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    spn_reinstall_sr_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                            if (i == 0) {
                                return;
                            } else {
                                i = i - 1;
                            }
                            //if(vltddeviceReinst.getSelectedItem().toString().equalsIgnoreCase("AIS 140")){
                            s_old_serial_no = removesrnoList.get(i).getPcb_sr_no();
                            serial_no="0";
                            s_new_device_id="0";
//                            }else {
//                                serial_no = removesrnoList.get(i).getPcb_sr_no();
//                                s_old_serial_no="0";
//                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    text_to_show_reinstall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           if(vltddeviceReinst.getSelectedItem().toString().equalsIgnoreCase("AIS 140")){
                               linear_device_sr_no_reinstall_ais.setVisibility(View.VISIBLE);
                           }else {
                               linear_device_sr_no_reinstall.setVisibility(View.VISIBLE);
                           }
                        }
                    });

                    delete_button_reinstall_ais.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            old_vltd_sr_no.setText("");
                            linear_device_sr_no_reinstall.setVisibility(View.GONE);
                        }
                    });

                    delete_button_reinstall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            linear_device_sr_no_reinstall.setVisibility(View.GONE);
                        }
                    });

                    old_deviceid.setOnFocusChangeListener((v, hasFocus) -> {
                        if (!hasFocus) {
                            vts_id = old_deviceid.getText().toString();
                            getVTSDetail();
                        } else {
                        }
                    });

                    old_vltd_sr_no.setOnFocusChangeListener((v, hasFocus) -> {
                        if (!hasFocus) {
                            vts_id = old_vltd_sr_no.getText().toString();
                            getVTSDetail();
                        } else {
                        }
                    });

                    radiodrsReInstall.setOnCheckedChangeListener((radioGroup, i15) -> {
                        if (i15 == R.id.radioyesdrsReInstall) {
                            linearDrs.setVisibility(View.VISIBLE);
                            magnetset_install.setVisibility(View.VISIBLE);
                        } else {
                            linearDrs.setVisibility(View.GONE);
                            magnetset_install.setVisibility(View.GONE);
                        }
                    });

                    radioGrouptilt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.tiltNo) {
                                tilt_sensor = "N";
                            } else {
                                tilt_sensor = "Y";
                            }
                        }
                    });

                    radioGrouptemp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.tempNo) {
                                temp_sensor = "N";
                            } else {
                                temp_sensor = "Y";
                            }
                        }
                    });

                    radioGrouptrans.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {

                            if (id == R.id.transNo) {
                                trans = "N";
                            } else {
                                trans = "Y";
                            }
                        }
                    });

                    radioGroupLid.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.lidNone) {
                                lid_status = "N";
                            } else if (id == R.id.lidTop) {
                                lid_status = "T";
                            } else if (id == R.id.lidRear) {
                                lid_status = "R";
                            } else {
                                lid_status = "B";
                            }
                        }
                    });

                } else if (s_work_id.equalsIgnoreCase("5")) {
                    device_info.setText("Removal/Collection Details");
                    e_remarks.setHint("Remarks");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        clearData();
                        remove_reg_no.setEnabled(true);
                        linearPhoneSupport.setVisibility(View.GONE);
                        linearReplacement.setVisibility(View.GONE);
                        linearRemoval.setVisibility(View.VISIBLE);
                        linearReInstall.setVisibility(View.GONE);
                        linearInstall.setVisibility(View.GONE);
                        linearFault.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearSimRepalace.setVisibility(View.GONE);
                        magnetset_install.setVisibility(View.GONE);
                        drsInstall.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearDeviceMissing.setVisibility(View.GONE);
                        linearVehicleNotAvail.setVisibility(View.GONE);
                        vehDetail.setVisibility(View.GONE);
                        linearPayment.setVisibility(View.GONE);
                        linearOthers.setVisibility(View.GONE);
                        imageName.setText("");
                        imageNameMissing.setText("");
                        imageNameFault.setText("");
                        removal_type();
                        addReasonRemove();
                        getItemCollectList();
                        getSerialNo();
                        ShowProgressBar(false);
                    }, DELAY);
                    remove_deviceid.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            vts_id = remove_deviceid.getText().toString();
                            if (vts_id.equals("")) {
                                remove_reg_no.setText("");
                            } else {
                                getVTSDetail();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });

                    remove_sr_no.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            vts_id = remove_sr_no.getText().toString();
                            if (vts_id.equals("")) {
                                remove_reg_no.setText("");
                            } else {
                                getVTSDetail();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });

                    vltddeviceRemove.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i == 0) {
                                return;
                            } else {
                                i = i - 1;
                            }
                            if (vltddeviceRemove.getSelectedItem().toString().equalsIgnoreCase("AIS 140")) {
                                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
                                lin_vts_id_remove.setVisibility(View.GONE);

                            } else {
                                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
                                lin_vts_id_remove.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    text_to_show_remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (vltddeviceRemove.getSelectedItem().toString().equalsIgnoreCase("AIS 140")) {
                                linear_device_sr_no_remove.setVisibility(View.VISIBLE);
                                til_remove_sr.setVisibility(View.VISIBLE);
                                remove_sr_no.setVisibility(View.VISIBLE);
                                linear_device_sr_no_remove_e_series.setVisibility(View.GONE);
                                til_vts_remove.setVisibility(View.GONE);
                                remove_deviceid.setVisibility(View.GONE);
                            } else {
                                linear_device_sr_no_remove.setVisibility(View.GONE);
                                til_remove_sr.setVisibility(View.GONE);
                                remove_sr_no.setVisibility(View.VISIBLE);
                                linear_device_sr_no_remove_e_series.setVisibility(View.VISIBLE);
                                til_vts_remove.setVisibility(View.VISIBLE);
                                remove_deviceid.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    delete_button_remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            remove_sr_no.setText("");
                            linear_device_sr_no_remove.setVisibility(View.GONE);
                        }
                    });

                    delete_button_remove_e_series.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            remove_deviceid.setText("");
                            linear_device_sr_no_remove_e_series.setVisibility(View.GONE);
                        }
                    });

                    radioGroupTypeRemove.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.radioDeviceRemove) {
                                removeDeviceType = "D";
                            } else if (id == R.id.radioSensorRemove) {
                                removeDeviceType = "S";
                            } else {
                                removeDeviceType = "B";
                            }
                        }
                    });

                    radioGrouptiltRemove.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.tiltRemoveNo) {
                                tilt_sensor = "N";
                                sensor_veh_remove.setVisibility(View.GONE);
                            } else {
                                tilt_sensor = "Y";
                                sensor_veh_remove.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGroupdrsRemove.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.drsRemoveNo) {
                                drsStatus = "N";
                                sensor_veh_remove.setVisibility(View.GONE);
                            } else {
                                drsStatus = "Y";
                                sensor_veh_remove.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGrouptempRemove.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.tempNoRemove) {
                                temp_sensor = "N";
                                sensor_veh_remove.setVisibility(View.GONE);
                            } else {
                                temp_sensor = "Y";
                                sensor_veh_remove.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGroupPanicRemove.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.panicNoRemove) {
                                panic_status = "N";
                                sensor_veh_remove.setVisibility(View.GONE);
                            } else {
                                panic_status = "Y";
                                sensor_veh_remove.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGroupfuelRemove.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {

                            if (id == R.id.fuelSensorNoRemove) {
                                trans = "N";
                                sensor_veh_remove.setVisibility(View.GONE);
                            } else {
                                trans = "Y";
                                sensor_veh_remove.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    spn_remove_sr_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                            if (i == 0) {
                                return;
                            } else {
                                i = i - 1;
                            }
//                            if(vltddeviceRemove.getSelectedItem().toString().equalsIgnoreCase("AIS 140")){
                                s_old_serial_no = removesrnoList.get(i).getPcb_sr_no();
                                vts_id = s_old_serial_no;
                                getVTSDetail();
                           // }
//                            else {
//                                s_e_device_id = removesrnoList.get(i).getPcb_sr_no();
//                                vts_id = s_e_device_id;
//                                getVTSDetail();
//                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    radioGrouptransRemove.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {

                            if (id == R.id.transNoRemove) {
                                trans = "N";
                                sensor_veh_remove.setVisibility(View.GONE);
                            } else {
                                trans = "Y";
                                sensor_veh_remove.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGroupLidRemove.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.lidNoneRemove) {
                                lid_status = "N";
                                sensor_veh_remove.setVisibility(View.GONE);
                            } else if (id == R.id.lidTopRemove) {
                                lid_status = "T";
                                sensor_veh_remove.setVisibility(View.VISIBLE);
                            } else if (id == R.id.lidRearRemove) {
                                lid_status = "R";
                                sensor_veh_remove.setVisibility(View.VISIBLE);
                            } else {
                                lid_status = "B";
                                sensor_veh_remove.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    remove_reg_no.setOnTouchListener((v, event) -> {
                        remove_reg_no.setText("");
                        remove_reg_no.setTextColor(getResources().getColor(R.color.black));
                        return false;
                    });
                } else if (s_work_id.equalsIgnoreCase("1")) {
                    device_info.setText("Fault Details");
                    e_remarks.setHint("Remarks");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        clearData();
                        faultDetail.setVisibility(View.GONE);
                        fault_reg_no.setEnabled(true);
                        linearPhoneSupport.setVisibility(View.GONE);
                        linearReplacement.setVisibility(View.GONE);
                        linearReInstall.setVisibility(View.GONE);
                        linearFault.setVisibility(View.VISIBLE);
                        linearInstall.setVisibility(View.GONE);
                        linearRemoval.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearSimRepalace.setVisibility(View.GONE);
                        magnetset_install.setVisibility(View.GONE);
                        drsInstall.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearDeviceMissing.setVisibility(View.GONE);
                        linearVehicleNotAvail.setVisibility(View.GONE);
                        vehDetail.setVisibility(View.GONE);
                        linearPayment.setVisibility(View.GONE);
                        linearOthers.setVisibility(View.GONE);
                        imageName.setText("");
                        imageNameMissing.setText("");
                        imageNameFault.setText("");
                        faultDetail.setVisibility(View.GONE);
                        getFaultList();
                        addVehType();
                        getSerialNo();
                        ShowProgressBar(false);
                    }, DELAY);
                    fault_vts_id.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            vts_id = fault_vts_id.getText().toString();
                            if (vts_id.equals("")) {
                                fault_reg_no.setText("");
                            } else {
                                getVTSDetail();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            vts_id = fault_vts_id.getText().toString();
                            if (vts_id.equals("")) {
                                fault_reg_no.setText("");
                            } else {
                                getVTSDetail();
                            }
                        }
                    });

                    vltd_sr_no_fault.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            vts_id = vltd_sr_no_fault.getText().toString();
                            if (vts_id.equals("")) {
                                fault_reg_no.setText("");
                            } else {
                                getVTSDetail();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            vts_id = vltd_sr_no_fault.getText().toString();
                            if (vts_id.equals("")) {
                                fault_reg_no.setText("");
                            } else {
                                getVTSDetail();
                            }
                        }
                    });

                    vltddeviceFault.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i == 0) {
                                return;
                            } else {
                                i = i - 1;
                            }
                            if (vltddeviceFault.getSelectedItem().toString().equalsIgnoreCase("AIS 140")) {
                                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
//
                            } else {
                                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
//
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    spn_fault_sr_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                            if (i == 0) {
                                return;
                            } else {
                                i = i - 1;
                            }
//                            if(vltddeviceFault.getSelectedItem().toString().equalsIgnoreCase("AIS 140")){
                                s_old_serial_no = removesrnoList.get(i).getPcb_sr_no();
                                vts_id = s_old_serial_no;
                                getVTSDetail();
//                            }else {
//                                s_e_device_id = removesrnoList.get(i).getPcb_sr_no();
//                                vts_id = s_e_device_id;
//                                getVTSDetail();
//                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    text_to_show_fault.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           if(vltddeviceFault.getSelectedItem().toString().equalsIgnoreCase("AIS 140")){
                               linear_device_sr_no_fault.setVisibility(View.VISIBLE);
                               vltd_sr_no_fault.setVisibility(View.VISIBLE);
                               tilFaultSr.setVisibility(View.VISIBLE);
                               linear_device_sr_no_fault_e_series.setVisibility(View.GONE);
                           } else {
                               linear_device_sr_no_fault.setVisibility(View.GONE);
                               linear_device_sr_no_fault_e_series.setVisibility(View.VISIBLE);
                               fault_vts_id.setVisibility(View.VISIBLE);
                               tilFaultVts.setVisibility(View.VISIBLE);
                           }
                        }
                    });

                    delete_button_fault_e_series.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            linear_device_sr_no_fault_e_series.setVisibility(View.GONE);
                        }
                    });

                    delete_button_fault.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            linear_device_sr_no_fault.setVisibility(View.GONE);
                        }
                    });

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            others = others + (list_change_values.get(position).getId()) + ":";
                            if (others.contains("8")) {
                                faultDetail.setVisibility(View.VISIBLE);
                            } else {
                                faultDetail.setVisibility(View.GONE);
                            }
                            SparseBooleanArray checked = lv.getCheckedItemPositions();
                            String abc = String.valueOf(checked);
                            String currentString = abc;
                            String[] separated = currentString.split("\\{");
                            String positions = separated[1];
                            String[] separate = positions.split("\\}");
                            String status = separate[0];
                            if (status.contains("9=true")) {
                                faultDetail.setVisibility(View.VISIBLE);
                            } else {
                                faultDetail.setVisibility(View.GONE);
                            }
                        }
                    });
                    fault_reg_no.setOnTouchListener((v, event) -> {
                        fault_reg_no.setText("");
                        fault_reg_no.setTextColor(getResources().getColor(R.color.black));
                        return false;
                    });
                } else if (s_work_id.equalsIgnoreCase("6")) {
                    device_info.setText("Phone Support");
                    e_remarks.setHint("Remarks");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        clearData();
                        phSupport_reg_no.setEnabled(true);
                        relativeCableConnected.setVisibility(View.GONE);
                        linearPhoneSupport.setVisibility(View.VISIBLE);
                        linearReplacement.setVisibility(View.GONE);
                        linearReInstall.setVisibility(View.GONE);
                        linearInstall.setVisibility(View.GONE);
                        linearRemoval.setVisibility(View.GONE);
                        linearFault.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearSimRepalace.setVisibility(View.GONE);
                        magnetset_install.setVisibility(View.GONE);
                        drsInstall.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearDeviceMissing.setVisibility(View.GONE);
                        linearVehicleNotAvail.setVisibility(View.GONE);
                        vehDetail.setVisibility(View.GONE);
                        linearPayment.setVisibility(View.GONE);
                        linearOthers.setVisibility(View.GONE);
                        imageName.setText("");
                        imageNameMissing.setText("");
                        imageNameFault.setText("");
                        getPhoneSupportList();
                        getSerialNo();
                        ShowProgressBar(false);
                    }, DELAY);
                    phsupport_vts_id.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            vts_id = phsupport_vts_id.getText().toString();
                            if (vts_id.equals("")) {
                                phSupport_reg_no.setText("");
                            } else {
                                getVTSDetail();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });

                    vltd_sr_no_phn.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            vts_id = vltd_sr_no_phn.getText().toString();
                            if (vts_id.equals("")) {
                                phSupport_reg_no.setText("");
                            } else {
                                getVTSDetail();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });
                    vltddevicephn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i == 0) {
                                return;
                            } else {
                                i = i - 1;
                            }
                            if (vltddevicephn.getSelectedItem().toString().equalsIgnoreCase("AIS 140")) {
                                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
//
                            } else {
                                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
//
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    text_to_show_phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(vltddevicephn.getSelectedItem().toString().equalsIgnoreCase("AIS 140")){
                                linear_device_sr_no_phone.setVisibility(View.VISIBLE);
                                vltd_sr_no_phn.setVisibility(View.VISIBLE);
                                tilphnSr.setVisibility(View.VISIBLE);
                                phsupport_vts_id.setVisibility(View.GONE);
                                tilphnVts.setVisibility(View.GONE);
                                linear_device_sr_no_phone_e_series.setVisibility(View.GONE);
                            } else {
                                linear_device_sr_no_phone.setVisibility(View.GONE);
                                linear_device_sr_no_phone_e_series.setVisibility(View.VISIBLE);
                                tilphnVts.setVisibility(View.VISIBLE);
                                tilphnSr.setVisibility(View.GONE);
                                vltd_sr_no_phn.setVisibility(View.GONE);
                                phsupport_vts_id.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    delete_button_phone_e_series.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            phsupport_vts_id.setText("");
                            linear_device_sr_no_phone_e_series.setVisibility(View.GONE);
                        }
                    });

                    delete_button_phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            vltd_sr_no_phn.setText("");
                            linear_device_sr_no_phone.setVisibility(View.GONE);
                        }
                    });

                    spn_phone_sr_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                            if (i == 0) {
                                return;
                            } else {
                                i = i - 1;
                            }
                            //if(vltddevicephn.getSelectedItem().toString().equalsIgnoreCase("AIS 140")){
                                s_old_serial_no = removesrnoList.get(i).getPcb_sr_no();
                                s_e_device_id="0";
                                vts_id = s_old_serial_no;
                                getVTSDetail();
//                            }else {
//                                s_e_device_id = removesrnoList.get(i).getPcb_sr_no();
//                                s_old_serial_no="";
//                                vts_id = s_e_device_id;
//                                getVTSDetail();
//                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    phSupport_reg_no.setOnTouchListener((v, event) -> {
                        phSupport_reg_no.setText("");
                        phSupport_reg_no.setTextColor(getResources().getColor(R.color.black));
                        return false;
                    });
                } else if (s_work_id.equalsIgnoreCase("7")) {
                    device_info.setText("Sim Replacement");
                    e_remarks.setHint("Remarks");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        clearData();
                        linearPhoneSupport.setVisibility(View.GONE);
                        linearReplacement.setVisibility(View.GONE);
                        linearReInstall.setVisibility(View.GONE);
                        linearFault.setVisibility(View.GONE);
                        linearInstall.setVisibility(View.GONE);
                        linearRemoval.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearSimRepalace.setVisibility(View.VISIBLE);
                        magnetset_install.setVisibility(View.GONE);
                        drsInstall.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearDeviceMissing.setVisibility(View.GONE);
                        linearVehicleNotAvail.setVisibility(View.GONE);
                        vehDetail.setVisibility(View.GONE);
                        linearPayment.setVisibility(View.GONE);
                        linearOthers.setVisibility(View.GONE);
                        imageName.setText("");
                        imageNameMissing.setText("");
                        imageNameFault.setText("");
                        getSimReasonList();
                        getSimOperator();
                        ShowProgressBar(false);
                    }, DELAY);
                    sim_vts_id.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            vts_id = sim_vts_id.getText().toString();
                            if (vts_id.equals("")) {
                                sim_vehicle_no.setText("");
                            } else {
                                getVTSDetail();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });
                    sim_vehicle_no.setOnTouchListener((v, event) -> {
                        sim_vehicle_no.setText("");
                        sim_vehicle_no.setTextColor(getResources().getColor(R.color.black));
                        return false;
                    });
                } else if (s_work_id.equalsIgnoreCase("8")) {
                    device_info.setText("Report Missing Device");
                    e_remarks.setHint("Remarks");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        clearData();
                        missing_type = "M";
                        relativeCableConnected.setVisibility(View.GONE);
                        linearPhoneSupport.setVisibility(View.GONE);
                        linearReplacement.setVisibility(View.GONE);
                        linearReInstall.setVisibility(View.GONE);
                        linearInstall.setVisibility(View.GONE);
                        linearRemoval.setVisibility(View.GONE);
                        linearFault.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearSimRepalace.setVisibility(View.GONE);
                        magnetset_install.setVisibility(View.GONE);
                        drsInstall.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearDeviceMissing.setVisibility(View.VISIBLE);
                        linearVehicleNotAvail.setVisibility(View.GONE);
                        vehDetail.setVisibility(View.GONE);
                        linearPayment.setVisibility(View.GONE);
                        linearOthers.setVisibility(View.GONE);
                        imageName.setText("");
                        imageNameMissing.setText("");
                        imageNameFault.setText("");
                        damageReason();
                        getSerialNo();
                        ShowProgressBar(false);
                    }, DELAY);
                    mDevice_vts_id.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            vts_id = mDevice_vts_id.getText().toString();
                            if (vts_id.equals("")) {
                                mDevice_reg_no.setText("");
                            } else {
                                getVTSDetail();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });
                    radioGroupMiss.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.radioDeviceMiss) {
                                missDeviceType = "D";
                            } else if (id == R.id.radioSensorMiss) {
                                missDeviceType = "S";
                            } else {
                                missDeviceType = "B";
                            }
                        }
                    });

                    vltddeviceMiss.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i == 0) {
                                return;
                            } else {
                                i = i - 1;
                            }
                            if (vltddeviceMiss.getSelectedItem().toString().equalsIgnoreCase("AIS 140")) {
                                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
                                tilDeviceMiss.setVisibility(View.VISIBLE);
                                vltd_sr_no_miss.setVisibility(View.VISIBLE);
                                mDevice_vts_id.setVisibility(View.GONE);
                                til_vts_miss.setVisibility(View.GONE);
                            } else {
                                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
                                vltd_sr_no_miss.setVisibility(View.GONE);
                                tilDeviceMiss.setVisibility(View.GONE);
                                mDevice_vts_id.setVisibility(View.VISIBLE);
                                til_vts_miss.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    spn_missing_sr_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                            if (i == 0) {
                                return;
                            } else {
                                i = i - 1;
                            }
                            //if(vltddeviceMiss.getSelectedItem().toString().equalsIgnoreCase("AIS 140")){
                                s_old_serial_no = removesrnoList.get(i).getPcb_sr_no();
                                vts_id = s_old_serial_no;
                                getVTSDetail();
//                            }else {
//                                s_e_device_id = removesrnoList.get(i).getPcb_sr_no();
//                                vts_id = s_e_device_id;
//                                getVTSDetail();
//                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    text_to_show_missing.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(vltddeviceMiss.getSelectedItem().toString().equalsIgnoreCase("AIS 140")){
                                linear_device_sr_no_missing.setVisibility(View.VISIBLE);
                            }else {
                                linear_device_sr_no_missing_e_series.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    delete_button_missing.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            linear_device_sr_no_missing.setVisibility(View.GONE);
                        }
                    });

                    delete_button_missing_e_series.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            linear_device_sr_no_missing_e_series.setVisibility(View.GONE);
                        }
                    });

                    mDevice_reg_no.setOnTouchListener((v, event) -> {
                        mDevice_reg_no.setText("");
                        mDevice_reg_no.setTextColor(getResources().getColor(R.color.black));
                        return false;
                    });
                    radioGroupMissing.setOnCheckedChangeListener((group, checkedId) -> {
                        switch (checkedId) {
                            case R.id.reportMissing:
                                relMissing.setVisibility(View.GONE);
                                missing_type = "M";
                                mDevice_reg_no.setText("");
                                mDevice_vts_id.setText("");
                                missing_reason = "0";
                                break;
                            case R.id.damageDevice:
                                relMissing.setVisibility(View.VISIBLE);
                                missing_type = "D";
                                mDevice_reg_no.setText("");
                                mDevice_vts_id.setText("");
                                break;
                        }
                    });

                    radioGrouptiltMissing.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.tiltMissingNo) {
                                tilt_sensor = "N";
                                sensor_veh_missing.setVisibility(View.GONE);
                            } else {
                                tilt_sensor = "Y";
                                sensor_veh_missing.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGroupdrsMissing.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.drsMissingNo) {
                                drsStatus = "N";
                                sensor_veh_missing.setVisibility(View.GONE);
                            } else {
                                drsStatus = "Y";
                                sensor_veh_missing.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGrouptempMissing.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.tempNoMissing) {
                                temp_sensor = "N";
                                sensor_veh_missing.setVisibility(View.GONE);
                            } else {
                                temp_sensor = "Y";
                                sensor_veh_missing.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGroupPanicMissing.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.panicNoMissing) {
                                panic_status = "N";
                                sensor_veh_missing.setVisibility(View.GONE);
                            } else {
                                panic_status = "Y";
                                sensor_veh_missing.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGroupfuelMissing.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {

                            if (id == R.id.fuelSensorNoMissing) {
                                trans = "N";
                                sensor_veh_missing.setVisibility(View.GONE);
                            } else {
                                trans = "Y";
                                sensor_veh_missing.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGrouptransMissing.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {

                            if (id == R.id.transNoMissing) {
                                trans = "N";
                                sensor_veh_missing.setVisibility(View.GONE);
                            } else {
                                trans = "Y";
                                sensor_veh_missing.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    radioGroupLidMissing.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int id) {
                            if (id == R.id.lidNoneMissing) {
                                lid_status = "N";
                                sensor_veh_missing.setVisibility(View.GONE);
                            } else if (id == R.id.lidTopMissing) {
                                lid_status = "T";
                                sensor_veh_missing.setVisibility(View.VISIBLE);
                            } else if (id == R.id.lidRearMissing) {
                                lid_status = "R";
                                sensor_veh_missing.setVisibility(View.VISIBLE);
                            } else {
                                lid_status = "B";
                                sensor_veh_missing.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                } else if (s_work_id.equalsIgnoreCase("9")) {
                    device_info.setText("Report Vehicle Not Available");
                    e_remarks.setHint("Remarks");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        clearData();
                        relativeCableConnected.setVisibility(View.GONE);
                        linearPhoneSupport.setVisibility(View.GONE);
                        linearReplacement.setVisibility(View.GONE);
                        linearReInstall.setVisibility(View.GONE);
                        linearInstall.setVisibility(View.GONE);
                        linearRemoval.setVisibility(View.GONE);
                        linearFault.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearSimRepalace.setVisibility(View.GONE);
                        magnetset_install.setVisibility(View.GONE);
                        drsInstall.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearDeviceMissing.setVisibility(View.GONE);
                        linearVehicleNotAvail.setVisibility(View.VISIBLE);
                        vehDetail.setVisibility(View.GONE);
                        linearPayment.setVisibility(View.GONE);
                        linearOthers.setVisibility(View.GONE);
                        imageName.setText("");
                        imageNameMissing.setText("");
                        imageNameFault.setText("");
                        addActivity();
                        addVehNotAvailReason();
                        ShowProgressBar(false);
                    }, DELAY);
                    vehNotAvailVtsID.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            vts_id = vehNotAvailVtsID.getText().toString();
                            if (vts_id.equals("")) {
                            } else {
                                getVTSDetail();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });

                    vehNotAvailRegNo.setOnTouchListener((v, event) -> {
                        vehNotAvailRegNo.setText("");
                        vehNotAvailRegNo.setTextColor(getResources().getColor(R.color.black));
                        return false;
                    });
                } else if (s_work_id.equalsIgnoreCase("10")) {
                    device_info.setText("Payment Activity");
                    e_remarks.setHint("Remarks");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        clearData();
                        payment_type = "C";
                        relativeCableConnected.setVisibility(View.GONE);
                        linearPhoneSupport.setVisibility(View.GONE);
                        linearReplacement.setVisibility(View.GONE);
                        linearReInstall.setVisibility(View.GONE);
                        linearInstall.setVisibility(View.GONE);
                        linearRemoval.setVisibility(View.GONE);
                        linearFault.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearSimRepalace.setVisibility(View.GONE);
                        magnetset_install.setVisibility(View.GONE);
                        drsInstall.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearDeviceMissing.setVisibility(View.GONE);
                        linearVehicleNotAvail.setVisibility(View.GONE);
                        vehDetail.setVisibility(View.GONE);
                        linearPayment.setVisibility(View.VISIBLE);
                        linearOthers.setVisibility(View.GONE);
                        imageName.setText("");
                        imageNameMissing.setText("");
                        imageNameFault.setText("");
                        pMethod();
                        ShowProgressBar(false);
                    }, DELAY);
                    radiogroupPay.setOnCheckedChangeListener((group, checkedId) -> {
                        switch (checkedId) {
                            case R.id.collection:
                                // clearData();
                                payCollection.setVisibility(View.VISIBLE);
                                followUp.setVisibility(View.GONE);
                                payment_type = "C";
                                break;
                            case R.id.followup:
                                // clearData();
                                followUp.setVisibility(View.VISIBLE);
                                payCollection.setVisibility(View.GONE);
                                payment_type = "F";
                                break;
                        }
                    });
                } else if (s_work_id.equalsIgnoreCase("11")) {
                    device_info.setText("Other Work");
                    e_remarks.setHint("Add Activity Detail");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        clearData();
                        relativeCableConnected.setVisibility(View.GONE);
                        linearPhoneSupport.setVisibility(View.GONE);
                        linearReplacement.setVisibility(View.GONE);
                        linearReInstall.setVisibility(View.GONE);
                        linearInstall.setVisibility(View.GONE);
                        linearRemoval.setVisibility(View.GONE);
                        linearFault.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearSimRepalace.setVisibility(View.GONE);
                        magnetset_install.setVisibility(View.GONE);
                        drsInstall.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearDeviceMissing.setVisibility(View.GONE);
                        linearVehicleNotAvail.setVisibility(View.GONE);
                        vehDetail.setVisibility(View.GONE);
                        linearPayment.setVisibility(View.GONE);
                        linearOthers.setVisibility(View.VISIBLE);
                        imageName.setText("");
                        imageNameMissing.setText("");
                        imageNameFault.setText("");
                        ShowProgressBar(false);
                    }, DELAY);
                } else if (s_work_id.equalsIgnoreCase("12")) {
                    device_info.setText("Remove From Under Maint");
                    e_remarks.setHint("Add Remark");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        clearData();
                        relativeCableConnected.setVisibility(View.GONE);
                        linearPhoneSupport.setVisibility(View.GONE);
                        linearReplacement.setVisibility(View.GONE);
                        linearReInstall.setVisibility(View.GONE);
                        linearInstall.setVisibility(View.GONE);
                        linearRemoval.setVisibility(View.GONE);
                        linearFault.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearSimRepalace.setVisibility(View.GONE);
                        magnetset_install.setVisibility(View.GONE);
                        drsInstall.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearDeviceMissing.setVisibility(View.GONE);
                        linearVehicleNotAvail.setVisibility(View.GONE);
                        vehDetail.setVisibility(View.GONE);
                        linearPayment.setVisibility(View.GONE);
                        linearOthers.setVisibility(View.VISIBLE);
                        imageName.setText("");
                        imageNameMissing.setText("");
                        imageNameFault.setText("");
                        ShowProgressBar(false);
                        getUmVehicle();
                    }, DELAY);

                    vehicle_list_um.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                            if (i == 0) {
                                return;
                            } else {
                                i = i - 1;
                            }
                            sensor_old_veh_no = getUmVehicle.get(i).getReg_no();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (s_work_id.equalsIgnoreCase("13")) {
                    device_info.setText("Preventive Maintenance");
                    e_remarks.setHint("Add Remark");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        clearData();
                        relativeCableConnected.setVisibility(View.GONE);
                        linearPhoneSupport.setVisibility(View.GONE);
                        linearReplacement.setVisibility(View.GONE);
                        linearReInstall.setVisibility(View.GONE);
                        linearInstall.setVisibility(View.GONE);
                        linearRemoval.setVisibility(View.GONE);
                        linearFault.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearSimRepalace.setVisibility(View.GONE);
                        magnetset_install.setVisibility(View.GONE);
                        drsInstall.setVisibility(View.GONE);
                        linearDrs.setVisibility(View.GONE);
                        linearDeviceMissing.setVisibility(View.GONE);
                        linearVehicleNotAvail.setVisibility(View.GONE);
                        vehDetail.setVisibility(View.GONE);
                        linearPayment.setVisibility(View.GONE);
                        linearOthers.setVisibility(View.VISIBLE);
                        imageName.setText("");
                        imageNameMissing.setText("");
                        imageNameFault.setText("");
                        ShowProgressBar(false);
                        getUmVehicle();
                    }, DELAY);

                    vehicle_list_pm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                            if (i == 0) {
                                return;
                            } else {
                                i = i - 1;
                            }
                            sensor_old_veh_no = getUmVehicle.get(i).getReg_no();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        reason_replace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                s_reason_repla = String.valueOf(arr_replaceReasons.get(i).getReplace_Id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        update_dataa.setOnClickListener(view -> {
            if (s_clientname.equalsIgnoreCase("SELECT Client") || (s_clientname.equals(null))) {
                Toast.makeText(getContext(), "Please Select Client", Toast.LENGTH_LONG).show();
            } else if (location.getSelectedItem().toString().equalsIgnoreCase("Select Location")) {
                Toast.makeText(getContext(), "Please Select Location", Toast.LENGTH_LONG).show();
            } else if (workType.getSelectedItem().toString().equalsIgnoreCase("Select Work Type")) {
                Toast.makeText(getContext(), "Please Select Work Type", Toast.LENGTH_LONG).show();
            } else if (s_work_id.equalsIgnoreCase("2")) {
                s_drs_id = e_drs_id.getText().toString();
                s_reg_no = e_reg_no.getText().toString();
                s_date = t_install_date.getText().toString();
                s_Time = t_install_Time.getText().toString();
                s_remarks = e_remarks.getText().toString();
                confirmVehNo = con_in_reg_no.getText().toString();
                if (magnetset_install.isChecked()) {
                    mgt_set = "Y";
                } else {
                    mgt_set = "N";
                }
                int radioDrs = radiodrsInstall.getCheckedRadioButtonId();
                drsType = v.findViewById(radioDrs);
                String drsTypes = drsType.getText().toString();
                if (drsTypes.equalsIgnoreCase("Yes") && (drsInstall.getVisibility() == View.VISIBLE)) {
                    is_drs = "Y";
                } else if (drsTypes.equalsIgnoreCase("No") && (drsInstall.getVisibility() == View.VISIBLE)) {
                    is_drs = "P";
                } else {
                    is_drs = "N";
                }
                int isDemo = is_Demo.getCheckedRadioButtonId();
                is_demo_no = v.findViewById(isDemo);
                String demo = is_demo_no.getText().toString();
                if (demo.equalsIgnoreCase("No")) {
                    is_demo = "N";
                } else {
                    is_demo = "Y";
                }
                int selectedIgnition = radiogroup.getCheckedRadioButtonId();
                l_in = v.findViewById(selectedIgnition);
                String typeIgnition = l_in.getText().toString();
                if (typeIgnition.equalsIgnoreCase("No")) {
                    ignition_sensor = "N";
                } else {
                    ignition_sensor = "Y";
                }
                int selectedTypes = radiogroupDoor.getCheckedRadioButtonId();
                doorNo = v.findViewById(selectedTypes);
                String typesensor = doorNo.getText().toString();
                if (typesensor.equalsIgnoreCase("No")) {
                    door_sensor = "N";
                } else {
                    door_sensor = "Y";
                }
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radionormal = v.findViewById(selectedId);
                String drs = radionormal.getText().toString();
                if (drs.equalsIgnoreCase("Normal") && (linearDrs.getVisibility() == View.VISIBLE)) {
                    reverse.setChecked(false);
                    drs_dirction = "N";
                } else if (drs.equalsIgnoreCase("Reverse") && (linearDrs.getVisibility() == View.VISIBLE)) {
                    normal.setChecked(false);
                    drs_dirction = "R";
                }
                int cutoffType = radioGroupCutoff.getCheckedRadioButtonId();
                cutoffNo = v.findViewById(cutoffType);
                String cutoff = cutoffNo.getText().toString();
                if (cutoff.equalsIgnoreCase("No")) {
                    cut_off = "N";
                } else {
                    cut_off = "Y";
                }
                int panicType = radioGroupPanic.getCheckedRadioButtonId();
                panicNo = v.findViewById(panicType);
                String panic_button = panicNo.getText().toString();
                if (panic_button.equalsIgnoreCase("No")) {
                    panic = "N";
                } else {
                    panic = "Y";
                }
                int fuelType = radioGroupFuel.getCheckedRadioButtonId();
                fuelNo = v.findViewById(fuelType);
                String fuelSens = fuelNo.getText().toString();
                if (fuelSens.equalsIgnoreCase("No")) {
                    fuel_sensor = "N";
                } else {
                    fuel_sensor = "Y";
                }
                if (e_device_id.getVisibility() == View.VISIBLE) {
                    s_new_device_id = e_device_id.getText().toString();
                } else {
                    s_new_device_id = "0";
                }
                if (linear_device_sr_no_e_series.getVisibility() == View.VISIBLE) {
                    serial_no = vts_sr_no.getText().toString();
                }
                if(linear_device_sr_no.getVisibility()==View.VISIBLE) {
                    serial_no = vltd_sr_no.getText().toString();
                }
                if ((fuelVoltage.getVisibility() == View.VISIBLE) && (!installVoltage.getText().toString().equals(""))) {
                    fuel_voltage = installVoltage.getText().toString();
                    if (fuel_voltage.contains(".")) {
                        double d = Double.parseDouble(fuel_voltage);
                        fuelVoltInt = (int) d;
                    } else {
                        fuelVoltInt = Integer.parseInt(fuel_voltage);
                    }
                } else if ((fuelVoltage.getVisibility() == View.VISIBLE) && (installVoltage.getText().toString().equals(""))) {
                    fuel_voltage = "";
                    fuelVoltInt = 0;
                } else {
                    fuel_voltage = "0";
                    fuelVoltInt = 0;
                }
                if (s_vts_type.equalsIgnoreCase("SELECT VTS TYPE")) {
                    Toast.makeText(getContext(), "Please Select Device Type", Toast.LENGTH_LONG).show();
                } else if (s_new_device_id.equals("") && (e_device_id.getVisibility() == View.VISIBLE)) {
                    e_device_id.setError("Vts Id can't be null");
                } else if (serial_no.equals("") && (vts_sr_no.getVisibility() == View.VISIBLE)) {
                    vts_sr_no.setError("Sr no can't be blank");
                } else if (serial_no.equals("") && (vltd_sr_no.getVisibility() == View.VISIBLE)) {
                    vltd_sr_no.setError("Sr no can't be blank");
                } else if(!(con_vltd_sr_no.getText().toString()).matches(serial_no)){
                    con_vltd_sr_no.setError("Sr. No. not match");
                } else if (s_reg_no.equals("") || s_reg_no.equals(null) || s_reg_no.equals("0")) {
                    e_reg_no.setError("Reg No.can't be null");
                } else if ((confirmVehNo.equals(""))) {
                    con_in_reg_no.setError("Please fill the no");
                } else if (!(s_reg_no.equals(confirmVehNo))) {
                    con_in_reg_no.setError("No.not match");
                } else if (s_work_type.equalsIgnoreCase("Select Work Type")) {
                    Toast.makeText(getContext(), "Please Select Work Type", Toast.LENGTH_LONG).show();
                } else if ((vehicleType.getSelectedItem().toString().equalsIgnoreCase("Select Vehicle Type"))) {
                    Toast.makeText(getContext(), "Please Select Vehicle type", Toast.LENGTH_LONG).show();
                } else if ((linearDrs.getVisibility() == View.VISIBLE) && (s_drs_id.equals(""))) {
                    e_drs_id.setError("Fill DRS Id");
                } else if ((fuelVoltage.getVisibility() == View.VISIBLE) && (fuel_voltage.equals(""))) {
                    installVoltage.setError("Voltage value can't be null");
                } else if ((fuelVoltage.getVisibility() == View.VISIBLE) && (fuelVoltInt > 14)) {
                    installVoltage.setError("Voltage should be less than 14");
                } else if ((fuelVoltage.getVisibility() == View.VISIBLE) && (fuel_voltage.equals("0")) && (s_remarks.equals(""))) {
                    e_remarks.setError("Please Specify Reason");
                } else if ((linearDrs.getVisibility() == View.VISIBLE)) {
                    s_old_serial_no = "";
                    s_new_drs_id = e_drs_id.getText().toString();
                    s_e_device_id = "0";
                    s_drs_id = "0";
                    not_available_activity = "0";
                    not_available_reason = "0";
                    itemsCollected = "0";
                    removalReason = "0";
                    others = "";
                    missing_reason = "0";
                    removal_type = "0";
                    s_reason_repla = "0";
                    veh_condition = "W";
                    missing_type = "M";
                    collection_amount = "0";
                    collection_date = "0";
                    collection_type = "0";
                    image = "0";
                    s_rep_srNo = "";
                    s_reinst_conf_reg_no = "";
                    contact_person = "";
                    contact_no = "0";
                    payment_type = "C";
                    updateInstallationData();
                } else {
                    s_old_serial_no = "";
                    drs_dirction = "N";
                    s_drs_id = "0";
                    s_new_drs_id = "0";
                    old_sim_no = "0";
                    new_sim_no = "0";
                    sim_reason = "0";
                    sim_provider = "0";
                    removalReason = "0";
                    others = "";
                    s_e_device_id = "0";
                    disconnection_reason = "0";
                    not_available_activity = "0";
                    not_available_reason = "0";
                    itemsCollected = "0";
                    missing_reason = "0";
                    s_reason_repla = "0";
                    veh_condition = "W";
                    missing_type = "M";
                    collection_amount = "0";
                    collection_date = "0";
                    collection_type = "0";
                    image = "0";
                    removal_type = "0";
                    s_rep_srNo = "";
                    s_reinst_conf_reg_no = "";
                    contact_person = "";
                    contact_no = "0";
                    payment_type = "C";
                    updateInstallationData();
                }
            } else if (s_work_id.equalsIgnoreCase("4")) {
                s_reg_no = new_vehicleRegNo.getText().toString();
                s_remarks = e_remarks.getText().toString();
                s_drs_id = e_drs_id.getText().toString();
                s_date = t_install_date.getText().toString();
                s_Time = t_install_Time.getText().toString();
                s_reinst_conf_reg_no = reinst_conf_reg_no.getText().toString();
                if (new_deviceidReinstall.getVisibility() == View.VISIBLE) {
                    s_new_device_id = new_deviceidReinstall.getText().toString();
                } else {
                    s_new_device_id = "0";
                }
                if (magnetset_install.isChecked()) {
                    mgt_set = "Y";
                } else {
                    mgt_set = "N";
                }
                int radioDrs = radiodrsReInstall.getCheckedRadioButtonId();
                drsReeInstall = v.findViewById(radioDrs);
                String drsTypes = drsReeInstall.getText().toString();
                if (drsTypes.equalsIgnoreCase("Yes") && (drsReInstall.getVisibility() == View.VISIBLE)) {
                    noreinst.setChecked(false);
                    is_drs = "Y";
                } else if (drsTypes.equalsIgnoreCase("No") && (drsReInstall.getVisibility() == View.VISIBLE)) {
                    radioyesdrsReInstall.setChecked(false);
                    is_drs = "P";
                } else {
                    is_drs = "N";
                }
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radionormal = v.findViewById(selectedId);
                String drs = radionormal.getText().toString();
                if (drs.equalsIgnoreCase("Normal") && (linearDrs.getVisibility() == View.VISIBLE)) {
                    reverse.setChecked(false);
                    drs_dirction = "N";
                } else if (drs.equalsIgnoreCase("Reverse") && (linearDrs.getVisibility() == View.VISIBLE)) {
                    normal.setChecked(false);
                    drs_dirction = "R";
                }
                int selectedIgnition = radiogroup.getCheckedRadioButtonId();
                l_in = v.findViewById(selectedIgnition);
                String typeIgnition = l_in.getText().toString();
                if (typeIgnition.equalsIgnoreCase("No")) {
                    ignition_sensor = "N";
                } else {
                    ignition_sensor = "N";
                }
                int selectedTypes = radiogroupDoor.getCheckedRadioButtonId();
                doorNo = v.findViewById(selectedTypes);
                String typesensor = doorNo.getText().toString();
                if (typesensor.equalsIgnoreCase("No")) {
                    door_sensor = "N";
                } else {
                    door_sensor = "Y";
                }
                int cutoffTypeReinst = radiogroupCutOffReinst.getCheckedRadioButtonId();
                cut_off_no_reinst = v.findViewById(cutoffTypeReinst);
                String cutoffReinst = cut_off_no_reinst.getText().toString();
                if (cutoffReinst.equalsIgnoreCase("No")) {
                    cut_off = "N";
                } else {
                    cut_off = "Y";
                }
                int panicReinst = radioGroupPanicReinst.getCheckedRadioButtonId();
                panicNoReinst = v.findViewById(panicReinst);
                String panicnoReinst = panicNoReinst.getText().toString();
                if (panicnoReinst.equalsIgnoreCase("No")) {
                    panic = "N";
                } else {
                    panic = "Y";
                }
                int fuelReinst = radioGroupFuelReinst.getCheckedRadioButtonId();
                fuelNoReinst = v.findViewById(fuelReinst);
                String fuelReins = fuelNoReinst.getText().toString();
                if (fuelReins.equalsIgnoreCase("No")) {
                    fuel_sensor = "N";
                } else {
                    fuel_sensor = "Y";
                }
                s_VehicleTypeInst = new_in_vehicleTypeReins.getSelectedItem().toString();
                if (til_id_reinst.getVisibility() == View.VISIBLE) {
                    s_e_device_id = old_deviceid.getText().toString();
                    s_new_device_id="0";
                } else {
                    s_e_device_id = "0";
                    s_new_device_id="0";
                }
                if ((refuelVoltage.getVisibility() == View.VISIBLE) && (!reinstallVoltage.getText().toString().equals(""))) {
                    fuel_voltage = (reinstallVoltage.getText().toString());
                    if (fuel_voltage.contains(".")) {
                        double d = Double.parseDouble(fuel_voltage);
                        fuelVoltInt = (int) d;
                    } else {
                        fuelVoltInt = Integer.parseInt(fuel_voltage);
                    }
                    //uelVoltInt = Integer.parseInt(reinstallVoltage.getText().toString());
                } else if ((refuelVoltage.getVisibility() == View.VISIBLE) && (reinstallVoltage.getText().toString().equals(""))) {
                    fuel_voltage = "";
                    fuelVoltInt = 0;
                } else {
                    fuel_voltage = "0";
                    fuelVoltInt = 0;
                }
                if (linear_device_sr_no_reinstall.getVisibility() == View.VISIBLE) {
                    s_old_serial_no = vts_sr_no_reinst.getText().toString();
                    serial_no="0";
                }

                if ((linear_device_sr_no_reinstall_ais).getVisibility() == View.VISIBLE) {
                    s_old_serial_no = old_vltd_sr_no.getText().toString();
                    serial_no="0";
                }
                if (lay_sensor_veh.getVisibility() == View.VISIBLE) {
                    if (old_sensor_veh_no.getText().toString().equalsIgnoreCase("")) {
                        old_sensor_veh_no.setError("Please provide Old Vehicle No");
                    } else if (new_sensor_veh_no.getText().toString().equalsIgnoreCase("")) {
                        new_sensor_veh_no.setError("Please provide New Vehicle No");
                    } else {
                        sensor_old_veh_no = old_sensor_veh_no.getText().toString();
                        sen_vehicle_no = new_sensor_veh_no.getText().toString();
                    }
                }
                if (s_vts_type.equalsIgnoreCase("SELECT VTS TYPE")) {
                    Toast.makeText(getContext(), "Please Select Device Type", Toast.LENGTH_LONG).show();
                } else if ((s_old_serial_no.equalsIgnoreCase("SELECT SR.NO."))) {
                    Toast.makeText(getActivity(), "Sr. No. can't be null", Toast.LENGTH_SHORT).show();
                } else if (s_old_serial_no.equals("") && (til_old_vltd_sr_no.getVisibility() == View.VISIBLE) && (!reinstDevice.equalsIgnoreCase("S"))) {
                    old_vltd_sr_no.setError("Sr No. can't be null");
                } else if((vltddeviceReinst.getSelectedItem().toString().equalsIgnoreCase("AIS 140"))&&(!(s_old_serial_no.matches(con_reinstall_sr_no.getText().toString())))){
                    con_reinstall_sr_no.setError("Sr. no. not match");
                } else if (s_e_device_id.equals("") && (til_id_reinst.getVisibility() == View.VISIBLE) && (!reinstDevice.equalsIgnoreCase("S"))) {
                    old_deviceid.setError("Vts Id can't be null");
                }  else if (serial_no.equals("") && (til_sr_reinst.getVisibility() == View.VISIBLE) && (!reinstDevice.equalsIgnoreCase("S"))) {
                    vts_sr_no_reinst.setError("Sr no. can't be null");
                } else if(((vltddeviceReinst.getSelectedItem().toString().equalsIgnoreCase("E124"))&&(!(s_old_serial_no.matches(con_reinstall_sr_no.getText().toString()))))){
                    con_reinstall_sr_no.setError("Sr. no. not match");
                } else if ((!reinstDevice.equalsIgnoreCase("S")) && (s_reg_no.equals("") || s_reg_no.equals("0") || s_reg_no.equals(null))) {
                    new_vehicleRegNo.setError("Reg no. can't be null");
                } else if (s_reinst_conf_reg_no.equals("") && (!reinstDevice.equalsIgnoreCase("S"))) {
                    reinst_conf_reg_no.setError("No. can't be null");
                } else if (!(s_reg_no).equals(s_reinst_conf_reg_no) && (!reinstDevice.equalsIgnoreCase("S"))) {
                    reinst_conf_reg_no.setError("Value doesn't match");
                } else if ((s_VehicleTypeInst.equalsIgnoreCase("Select Vehicle Type") && (!reinstDevice.equalsIgnoreCase("S")))) {
                    Toast.makeText(getContext(), "Please select vehicle type", Toast.LENGTH_LONG).show();
                } else if ((linearDrs.getVisibility() == View.VISIBLE) && (s_drs_id.equals("")) && (!reinstDevice.equalsIgnoreCase("S"))) {
                    e_drs_id.setError("Fill DRS Id");
                } else if ((refuelVoltage.getVisibility() == View.VISIBLE) && (fuel_voltage.equals("")) && (!reinstDevice.equalsIgnoreCase("S"))) {
                    reinstallVoltage.setError("Fuel Voltage can't be null");
                } else if ((refuelVoltage.getVisibility() == View.VISIBLE) && (fuelVoltInt > 14) && (!reinstDevice.equalsIgnoreCase("S"))) {
                    reinstallVoltage.setError("Voltage should be less than 14");
                } else if ((refuelVoltage.getVisibility() == View.VISIBLE) && (fuel_voltage.equals("0") && (s_remarks.equals("")) && (!reinstDevice.equalsIgnoreCase("S")))) {
                    e_remarks.setError("Please specify reason");
                } else if (reinstDevice.equalsIgnoreCase("S") && (lay_sensor_veh.getVisibility() == View.GONE)) {
                    Toast.makeText(getContext(), "Please select Sensor", Toast.LENGTH_LONG).show();
                } else if ((reinstDevice.equalsIgnoreCase("S") && (lay_sensor_veh.getVisibility() == View.VISIBLE) && (old_sensor_veh_no.getText().toString().equalsIgnoreCase("")))) {
                    old_sensor_veh_no.setError("Enter Vehicle No");
                } else if ((reinstDevice.equalsIgnoreCase("S") && (lay_sensor_veh.getVisibility() == View.VISIBLE) && (new_sensor_veh_no.getText().toString().equalsIgnoreCase("")))) {
                    new_sensor_veh_no.setError("Enter Vehicle no");
                } else if ((reinstDevice.equalsIgnoreCase("S") && (old_sensor_veh_no.getText().toString().equals(new_sensor_veh_no.getText().toString()) && (lay_sensor_veh.getVisibility() == View.VISIBLE)))) {
                    Toast.makeText(getContext(), "Vehicle no. not same", Toast.LENGTH_LONG).show();
                } else if ((new_deviceidReinstall.getVisibility() == View.VISIBLE) && (!reinstDevice.equalsIgnoreCase("S"))) {
                    if(new_deviceidReinstall.getText().toString().equalsIgnoreCase("")){
                        s_new_device_id = "0";
                    }else {
                        s_new_device_id = new_deviceidReinstall.getText().toString();
                    }
                    s_new_drs_id = "0";
                    if (new_deviceidReinstall.getText().toString().equals(old_deviceid.getText().toString()) && (!reinstDevice.equalsIgnoreCase("S"))) {
                        new_deviceidReinstall.setError("Id can't be same");
                    } else {

                        if(new_deviceidReinstall.getText().toString().equalsIgnoreCase("")){
                            s_new_device_id = "0";
                        }else {
                            s_new_device_id = new_deviceidReinstall.getText().toString();
                        }
                        if (e_drs_id.getText().toString().equals("")) {
                            s_drs_id = "0";
                            s_new_drs_id = "0";
                            not_available_activity = "0";
                            not_available_reason = "0";
                            old_sim_no = "0";
                            new_sim_no = "0";
                            sim_reason = "0";
                            sim_provider = "0";
                            itemsCollected = "0";
                            removalReason = "0";
                            others = "";
                            s_reason_repla = "0";
                            veh_condition = "W";
                            is_demo = "N";
                            missing_type = "M";
                            collection_amount = "0";
                            collection_date = "0";
                            collection_type = "0";
                            image = "0";
                            missing_reason = "0";
                            removal_type = "0";
                            s_rep_srNo = "";
                            confirmVehNo = "";
                            s_reinst_conf_reg_no = "";
                            contact_person = "";
                            contact_no = "0";
                            payment_type = "C";
                            updateInstallationData();
                        } else {
                            s_drs_id = e_drs_id.getText().toString();
                            s_new_drs_id = "0";
                            not_available_activity = "0";
                            not_available_reason = "0";
                            old_sim_no = "0";
                            new_sim_no = "0";
                            sim_reason = "0";
                            sim_provider = "0";
                            itemsCollected = "0";
                            removalReason = "0";
                            others = "";
                            s_reason_repla = "0";
                            veh_condition = "W";
                            is_demo = "N";
                            missing_type = "M";
                            collection_amount = "0";
                            collection_date = "0";
                            collection_type = "0";
                            image = "0";
                            missing_reason = "0";
                            removal_type = "0";
                            s_rep_srNo = "";
                            confirmVehNo = "";
                            s_reinst_conf_reg_no = "";
                            contact_person = "";
                            contact_no = "0";
                            payment_type = "C";
                            updateInstallationData();
                        }
                    }
                } else if ((linearDrs.getVisibility() == View.VISIBLE) && (s_drs_id.equals(""))) {
                    e_drs_id.setError("Fill DRS Id");
                } else if ((linearDrs.getVisibility() == View.VISIBLE)) {
                    s_drs_id = e_drs_id.getText().toString();
                    s_new_device_id = "0";
                    s_new_drs_id = "0";
                    not_available_activity = "0";
                    not_available_reason = "0";
                    old_sim_no = "0";
                    new_sim_no = "0";
                    sim_reason = "0";
                    sim_provider = "0";
                    itemsCollected = "0";
                    removalReason = "0";
                    others = "";
                    s_reason_repla = "0";
                    veh_condition = "W";
                    is_demo = "N";
                    missing_type = "M";
                    collection_amount = "0";
                    collection_date = "0";
                    collection_type = "0";
                    image = "0";
                    missing_reason = "0";
                    removal_type = "0";
                    s_rep_srNo = "";
                    confirmVehNo = "";
                    s_reinst_conf_reg_no = "";
                    contact_person = "";
                    contact_no = "0";
                    payment_type = "C";
                    updateInstallationData();
                } else if (til_new_vltd_sr_no.getVisibility() == View.VISIBLE) {
                    s_e_device_id = "0";
                    s_new_drs_id = "0";
                    s_new_device_id = "0";
                    s_drs_id = "0";
                    disconnection_reason = "0";
                    not_available_activity = "0";
                    not_available_reason = "0";
                    old_sim_no = "0";
                    new_sim_no = "0";
                    sim_reason = "0";
                    sim_provider = "0";
                    itemsCollected = "0";
                    removalReason = "0";
                    others = "";
                    s_reason_repla = "0";
                    veh_condition = "W";
                    is_demo = "N";
                    missing_type = "M";
                    collection_amount = "0";
                    collection_date = "0";
                    collection_type = "0";
                    image = "0";
                    missing_reason = "0";
                    removal_type = "0";
                    s_rep_srNo = "";
                    confirmVehNo = "";
                    s_reinst_conf_reg_no = "";
                    contact_person = "";
                    contact_no = "0";
                    payment_type = "C";
                    updateInstallationData();
                } else {
                    s_new_drs_id = "0";
                    s_new_device_id = "0";
                    s_drs_id = "0";
                    disconnection_reason = "0";
                    not_available_activity = "0";
                    not_available_reason = "0";
                    old_sim_no = "0";
                    new_sim_no = "0";
                    sim_reason = "0";
                    sim_provider = "0";
                    itemsCollected = "0";
                    removalReason = "0";
                    others = "";
                    s_reason_repla = "0";
                    veh_condition = "W";
                    is_demo = "N";
                    missing_type = "M";
                    collection_amount = "0";
                    collection_date = "0";
                    collection_type = "0";
                    image = "0";
                    missing_reason = "0";
                    removal_type = "0";
                    s_rep_srNo = "";
                    confirmVehNo = "";
                    s_reinst_conf_reg_no = "";
                    contact_person = "";
                    contact_no = "0";
                    payment_type = "C";
                    updateInstallationData();
                }
            } else if (s_work_id.equalsIgnoreCase("3")) {
                s_reg_no = regNo.getText().toString();
                int selectedId = radiodireplace.getCheckedRadioButtonId();
                radionormalrep = v.findViewById(selectedId);
                linearDrs.setVisibility(View.GONE);
                String drs = radionormalrep.getText().toString();
                if (drs.equalsIgnoreCase("Normal") && (relaydrsTypeReplace.getVisibility() == View.VISIBLE)) {
                    replacereverse.setChecked(false);
                    drs_dirction = "N";
                } else if (drs.equalsIgnoreCase("Reverse") && (relaydrsTypeReplace.getVisibility() == View.VISIBLE)) {
                    replacenormal.setChecked(false);
                    drs_dirction = "R";
                }
                int selectedIgnition = radiogroup.getCheckedRadioButtonId();
                l_in = v.findViewById(selectedIgnition);
                String typeIgnition = l_in.getText().toString();
                if (typeIgnition.equalsIgnoreCase("No")) {
                    ignition_sensor = "N";
                } else {
                    ignition_sensor = "Y";
                }
                int selectedTypes = radiogroupDoor.getCheckedRadioButtonId();
                doorNo = v.findViewById(selectedTypes);
                String typesensor = doorNo.getText().toString();
                if (typesensor.equalsIgnoreCase("No")) {
                    door_sensor = "N";
                } else {
                    door_sensor = "Y";
                }
                s_date = t_install_date.getText().toString();
                s_Time = t_install_Time.getText().toString();
                int selectedType = drsReplace.getCheckedRadioButtonId();
                radiotype = v.findViewById(selectedType);
                String type = radiotype.getText().toString();
                if (type.equalsIgnoreCase("YES")) {
                    nodrsReplace.setChecked(false);
                    is_drs = "Y";
                } else {
                    radioyesdrsReplace.setChecked(false);
                    is_drs = "N";
                }
                if (magnet_set.isChecked()) {
                    mgt_set = "Y";
                } else {
                    mgt_set = "N";
                }
                if(linear_vts_id_replace.getVisibility()==View.VISIBLE){
                    s_e_device_id = old_vts_id_replace.getText().toString();
                    s_new_device_id = new_vts_id_replace.getText().toString();
                }
                if ((linear_device_id_replace_old.getVisibility() == View.VISIBLE)&&(vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("E124"))) {
                    s_old_serial_no = old_deviceidreplace.getText().toString();
                    vts_id = s_old_serial_no;
                    getVTSDetail();
                }
                if((linear_device_id_replace_new.getVisibility() == View.VISIBLE)&&(vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("E124"))) {
                    serial_no = new_deviceid.getText().toString();
                }
                if ((linear_device_sr_no_replace_old.getVisibility() == View.VISIBLE)&&((vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("AIS 140")))) {
                    s_old_serial_no = old_replace_sr_no.getText().toString();
                    vts_id = s_old_serial_no;
                    getVTSDetail();
                }
                if ((linear_device_sr_no_replace_new.getVisibility() == View.VISIBLE)&&((vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("AIS 140")))) {
                    serial_no = new_replace_sr_no.getText().toString();
                }
                if ((linearvts.getVisibility() == View.VISIBLE) && (new_drsid.getVisibility() == View.GONE) && (!(radioButtonChecked.equalsIgnoreCase("N")))) {
                    if (s_vts_type.equalsIgnoreCase("SELECT VTS TYPE")) {
                        Toast.makeText(getContext(), "Please Select Device Type", Toast.LENGTH_LONG).show();
                    } else if ((s_old_serial_no.equalsIgnoreCase("SELECT SR.NO."))) {
                        Toast.makeText(getActivity(), "Sr. No. can't be null", Toast.LENGTH_SHORT).show();
                    } else if ((serial_no.equalsIgnoreCase("SELECT SR.NO."))) {
                        Toast.makeText(getActivity(), "New Sr. No. can't be null", Toast.LENGTH_SHORT).show();
                    } else if ((vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("E124")&&(linear_device_id_replace_old.getVisibility() == View.VISIBLE)&&(s_old_serial_no.equals("") && (!(radioButtonChecked.equalsIgnoreCase("N")))))) {
                        old_deviceidreplace.setError("Sr no can't be null");
                    } else if ((vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("E124")&&(linear_device_id_replace_new.getVisibility() == View.VISIBLE)&&(serial_no.equals("") && (!(radioButtonChecked.equalsIgnoreCase("N")))))) {
                        new_deviceid.setError("Sr no can't be null");
                    } else if((vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("E124")&&(!(s_old_serial_no.matches(con_old_deviceidreplace.getText().toString())&&(!(radioButtonChecked.equalsIgnoreCase("N"))))))){
                        con_old_deviceidreplace.setError("Sr. no. not match");
                    } else if((vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("E124")&&(!(serial_no.matches(con_new_deviceid.getText().toString())&&(!(radioButtonChecked.equalsIgnoreCase("N"))))))){
                        con_new_deviceid.setError("Sr. no. not match");
                    } else if ((linear_device_sr_no_replace_old.getVisibility() == View.VISIBLE)&&(s_old_serial_no.equals("") && (til_old_sr_replace.getVisibility() == View.VISIBLE) && (!(radioButtonChecked.equalsIgnoreCase("N"))))) {
                        old_replace_sr_no.setError("Sr.no.can't be empty");
                    } else if ((vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("AIS 140")&&(!(s_old_serial_no.matches(con_old_deviceidreplace.getText().toString())&&(!(radioButtonChecked.equalsIgnoreCase("N"))))))) {
                        con_old_deviceidreplace.setError("Sr. no. not match");
                    } else if ((linear_device_sr_no_replace_new.getVisibility() == View.VISIBLE)&&(serial_no.equals("")&& (!(radioButtonChecked.equalsIgnoreCase("N"))))) {
                        new_replace_sr_no.setError("Sr.no.can't be empty");
                    } else if ((vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("AIS 140"))&&(!(serial_no.matches(con_new_deviceid.getText().toString())&&(!(radioButtonChecked.equalsIgnoreCase("N")))))) {
                        con_new_deviceid.setError("Sr. no. not match");
                    } else if((linear_vts_id_replace.getVisibility()==View.VISIBLE)&&(s_e_device_id.equalsIgnoreCase("")||(s_e_device_id.equalsIgnoreCase("Null")))){
                           old_vts_id_replace.setError("VTS ID can't be null");
                    } else if((linear_vts_id_replace.getVisibility()==View.VISIBLE)&&(!(s_e_device_id.matches((con_old_vts_id_replace.getText().toString()))))){
                            con_old_vts_id_replace.setError("VTS id not match");
                    } else if((linear_vts_id_replace.getVisibility()==View.VISIBLE)&&(s_new_device_id.equalsIgnoreCase("")||(s_new_device_id.equalsIgnoreCase("Null")))){
                            new_vts_id_replace.setError("VTS ID can't be null");
                    } else if((linear_vts_id_replace.getVisibility()==View.VISIBLE)&&(!(s_new_device_id).matches(con_new_vts_id_replace.getText().toString()))) {
                            con_new_vts_id_replace.setError("VTS id not match");
                    } else if ((!(radioButtonChecked.equalsIgnoreCase("N")) && (s_reg_no.equals("") || s_reg_no.equals("0") || s_reg_no.equals("null")|| s_reg_no.equals("Please Enter Registr")))) {
                        regNo.setError("Registration no. can't be null or zero");
                    }  else if ((reason_replace.getSelectedItem().toString().equalsIgnoreCase("Select Reason") && (!(radioButtonChecked.equalsIgnoreCase("N"))))) {
                        Toast.makeText(getContext(), "Please Select Reason", Toast.LENGTH_LONG).show();
                    } else {
                        s_drs_id = "0";
                        s_new_drs_id = "0";
                        s_remarks = e_remarks.getText().toString();
                        missing_type = "M";
                        collection_amount = "0";
                        collection_date = "0";
                        collection_type = "0";
                        image = "0";
                        is_drs = "N";
                        cut_off = "N";
                        disconnection_reason = "0";
                        not_available_activity = "0";
                        not_available_reason = "0";
                        itemsCollected = "0";
                        removalReason = "0";
                        others = "";
                        veh_condition = "W";
                        is_demo = "N";
                        missing_reason = "0";
                        removal_type = "0";
                        confirmVehNo = "";
                        s_reinst_conf_reg_no = "";
                        contact_person = "";
                        contact_no = "0";
                        payment_type = "C";
                        updateInstallationData();
                    }
                } else if ((old_deviceidreplace.getVisibility() == View.VISIBLE) && (drs_veh_no.getVisibility() == View.GONE) && (!(radioButtonChecked.equalsIgnoreCase("N")))) {
                    if (s_vts_type.equalsIgnoreCase("SELECT VTS TYPE")) {
                        Toast.makeText(getContext(), "Please Select Device Type", Toast.LENGTH_LONG).show();
                    } else if ((s_old_serial_no.equalsIgnoreCase("SELECT SR.NO."))) {
                        Toast.makeText(getActivity(), "Sr. No. can't be null", Toast.LENGTH_SHORT).show();
                    } else if ((serial_no.equalsIgnoreCase("SELECT SR.NO."))) {
                        Toast.makeText(getActivity(), "New Sr. No. can't be null", Toast.LENGTH_SHORT).show();
                    } else if ((vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("E124")&&(linear_device_id_replace_old.getVisibility() == View.VISIBLE)&&(s_old_serial_no.equals("") && (!(radioButtonChecked.equalsIgnoreCase("N")))))) {
                        old_deviceidreplace.setError("Sr no can't be null");
                    } else if ((vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("E124")&&(linear_device_id_replace_new.getVisibility() == View.VISIBLE)&&(serial_no.equals("") && (!(radioButtonChecked.equalsIgnoreCase("N")))))) {
                        new_deviceid.setError("Sr no can't be null");
                    } else if((vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("E124")&&(!(s_old_serial_no.matches(con_old_deviceidreplace.getText().toString())&&(!(radioButtonChecked.equalsIgnoreCase("N"))))))){
                        con_old_deviceidreplace.setError("Sr. no. not match");
                    } else if((vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("E124")&&(!(serial_no.matches(con_new_deviceid.getText().toString())&&(!(radioButtonChecked.equalsIgnoreCase("N"))))))){
                        con_new_deviceid.setError("Sr. no. not match");
                    } else if ((linear_device_sr_no_replace_old.getVisibility() == View.VISIBLE)&&(s_old_serial_no.equals("") && (til_old_sr_replace.getVisibility() == View.VISIBLE) && (!(radioButtonChecked.equalsIgnoreCase("N"))))) {
                        old_replace_sr_no.setError("Sr.no. can't be empty");
                    } else if ((vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("AIS 140")&&(!(s_old_serial_no.matches(con_old_deviceidreplace.getText().toString())&&(!(radioButtonChecked.equalsIgnoreCase("N"))))))) {
                        con_old_deviceidreplace.setError("Sr. no. not match");
                    } else if ((linear_device_sr_no_replace_new.getVisibility() == View.VISIBLE)&&(serial_no.equals("")&& (!(radioButtonChecked.equalsIgnoreCase("N"))))) {
                        new_replace_sr_no.setError("Sr.no. can't be empty");
                    } else if ((vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("AIS 140"))&&(!(serial_no.matches(con_new_deviceid.getText().toString())&&(!(radioButtonChecked.equalsIgnoreCase("N")))))) {
                        con_new_deviceid.setError("Sr. no. not match");
                    } else if((linear_vts_id_replace.getVisibility()==View.VISIBLE)&&(s_e_device_id.equalsIgnoreCase("")||(s_e_device_id.equalsIgnoreCase("Null")))){
                        old_vts_id_replace.setError("VTS ID can't be null");
                    } else if((linear_vts_id_replace.getVisibility()==View.VISIBLE)&&(!(s_e_device_id.matches((con_old_vts_id_replace.getText().toString()))))){
                        con_old_vts_id_replace.setError("VTS id not match");
                    } else if((linear_vts_id_replace.getVisibility()==View.VISIBLE)&&(s_new_device_id.equalsIgnoreCase("")||(s_new_device_id.equalsIgnoreCase("Null")))){
                        new_vts_id_replace.setError("VTS ID can't be null");
                    } else if((linear_vts_id_replace.getVisibility()==View.VISIBLE)&&(!(s_new_device_id).matches(con_new_vts_id_replace.getText().toString()))) {
                        con_new_vts_id_replace.setError("VTS id not match");
                    } else if ((!(radioButtonChecked.equalsIgnoreCase("N")) && (s_reg_no.equals("") || s_reg_no.equals("0") || s_reg_no.equals("null")|| s_reg_no.equals("Please Enter Registr")))) {
                        regNo.setError("Registration no. can't be null or zero");
                    } else if ((reason_replace.getSelectedItem().toString().equalsIgnoreCase("Select Reason"))) {
                        Toast.makeText(getContext(), "Please Select Reason", Toast.LENGTH_LONG).show();
                    } else if (old_drsid.getText().toString().equals("")) {
                        old_drsid.setError("OLD DRS Id can't be null");
                    } else if (new_drsid.getText().toString().equals("")) {
                        new_drsid.setError("New DRS Id can't be null");
                    }  else {
                        s_new_drs_id = new_drsid.getText().toString();
                        s_drs_id = old_drsid.getText().toString();
                        is_drs = "Y";
                        if ((linear_device_sr_no_replace_old.getVisibility() == View.VISIBLE)) {
                            s_old_serial_no = old_replace_sr_no.getText().toString();
                        }
                        if ((linear_device_sr_no_replace_new.getVisibility() == View.VISIBLE)) {
                            serial_no = new_replace_sr_no.getText().toString();
                        }
                        itemsCollected = "0";
                        removalReason = "0";
                        others = "";
                        veh_condition = "W";
                        is_demo = "N";
                        missing_type = "M";
                        collection_amount = "0";
                        collection_date = "0";
                        collection_type = "0";
                        image = "0";
                        disconnection_reason = "0";
                        not_available_activity = "0";
                        not_available_reason = "0";
                        old_sim_no = "0";
                        new_sim_no = "0";
                        sim_reason = "0";
                        sim_provider = "0";
                        s_remarks = e_remarks.getText().toString();
                        missing_reason = "0";
                        removal_type = "0";
                        cut_off = "N";
                        confirmVehNo = "";
                        s_reinst_conf_reg_no = "";
                        contact_person = "";
                        contact_no = "0";
                        payment_type = "C";
                        updateInstallationData();
                    }
                } else if ((drs_veh_no.getVisibility() == View.VISIBLE) && (drs_vts_id.getVisibility() == View.VISIBLE) && (!(radioButtonChecked.equalsIgnoreCase("N")))) {
                    if (drs_vts_id.getText().toString().equals("")) {
                        drs_vts_id.setError("Sr No. can't be null");
                    }  else if (drs_veh_no.getText().toString().equals("")) {
                        drs_veh_no.setError("Reg no. can't be null");
                    } else if (old_drsid.getText().toString().equals("")) {
                        old_drsid.setError("Drs id can't be null");
                    } else if (old_drsid.getText().toString().equals(new_drsid.getText().toString())) {
                        new_drsid.setError("Id could not be same");
                    } else {
                        s_new_drs_id = new_drsid.getText().toString();
                        s_drs_id = old_drsid.getText().toString();
                        is_drs = "Y";
                        s_old_serial_no = drs_vts_id.getText().toString();
                        s_reg_no = drs_veh_no.getText().toString();
                        missing_type = "M";
                        cut_off = "N";
                        serial_no = "";
                        s_e_device_id = "0";
                        s_new_device_id = "0";
                        s_reason_repla = "0";
                        itemsCollected = "0";
                        removalReason = "0";
                        others = "";
                        veh_condition = "W";
                        collection_amount = "0";
                        collection_date = "0";
                        collection_type = "0";
                        image = "0";
                        disconnection_reason = "0";
                        not_available_activity = "0";
                        not_available_reason = "0";
                        old_sim_no = "0";
                        new_sim_no = "0";
                        sim_reason = "0";
                        sim_provider = "0";
                        is_demo = "N";
                        s_remarks = e_remarks.getText().toString();
                        missing_reason = "0";
                        removal_type = "0";
                        s_rep_srNo = "";
                        confirmVehNo = "";
                        s_reinst_conf_reg_no = "";
                        contact_person = "";
                        contact_no = "0";
                        payment_type = "C";
                        updateInstallationData();
                    }
                } else if ((radioButtonChecked.equalsIgnoreCase("N"))) {
                    sensor_old_veh_no = sensor_veh_no.getText().toString();
                    if (s_vts_type.equalsIgnoreCase("SELECT VTS TYPE")) {
                        Toast.makeText(getContext(), "Please Select Device Type", Toast.LENGTH_LONG).show();
                    } else if ((radioButtonChecked.equalsIgnoreCase("N") && (sensor_veh.getVisibility() == View.GONE))) {
                        Toast.makeText(getContext(), "Please Select Sensor", Toast.LENGTH_LONG).show();
                    } else if ((sensor_veh_no.getVisibility() == View.VISIBLE) && (sensor_old_veh_no.equalsIgnoreCase(""))) {
                        sensor_veh_no.setError("Enter Vehicle No");
                    } else {
                        sen_vehicle_no = "";
                        s_drs_id = "0";
                        s_new_drs_id = "0";
                        s_remarks = e_remarks.getText().toString();
                        missing_type = "M";
                        collection_amount = "0";
                        collection_date = "0";
                        collection_type = "0";
                        s_new_device_id = "0";
                        image = "0";
                        cut_off = "N";
                        serial_no = "0";
                        s_e_device_id = "0";
                        s_reg_no = "0";
                        s_new_device_id = "0";
                        s_old_serial_no = "";
                        serial_no = "";
                        disconnection_reason = "0";
                        not_available_activity = "0";
                        not_available_reason = "0";
                        itemsCollected = "0";
                        removalReason = "0";
                        others = "";
                        veh_condition = "W";
                        is_demo = "N";
                        missing_reason = "0";
                        removal_type = "0";
                        s_rep_srNo = "";
                        s_reinst_conf_reg_no = "";
                        contact_person = "";
                        contact_no = "0";
                        payment_type = "C";
                        updateInstallationData();
                    }
                } else {
                    s_drs_id = "0";
                    s_new_drs_id = "0";
                    s_remarks = e_remarks.getText().toString();
                    missing_type = "M";
                    collection_amount = "0";
                    collection_date = "0";
                    collection_type = "0";
                    image = "0";
                    cut_off = "N";
                    disconnection_reason = "0";
                    not_available_activity = "0";
                    not_available_reason = "0";
                    itemsCollected = "0";
                    removalReason = "0";
                    others = "";
                    veh_condition = "W";
                    is_demo = "N";
                    missing_reason = "0";
                    removal_type = "0";
                    s_rep_srNo = "";
                    s_reinst_conf_reg_no = "";
                    contact_person = "";
                    contact_no = "0";
                    payment_type = "C";
                    updateInstallationData();
                }
            } else if (s_work_id.equalsIgnoreCase("5")) {
                if (s_vts_type.equalsIgnoreCase("SELECT VTS TYPE")) {
                    Toast.makeText(getContext(), "Please Select Device Type", Toast.LENGTH_LONG).show();
                } else if (removal_type.equalsIgnoreCase("0")) {
                    Toast.makeText(getContext(), "Select Action Type", Toast.LENGTH_SHORT).show();
                }
                if (linear_device_sr_no_remove_e_series.getVisibility() == View.VISIBLE) {
                    s_old_serial_no = remove_deviceid.getText().toString();
                }
                if (linear_device_sr_no_remove.getVisibility() == View.VISIBLE) {
                    s_old_serial_no = remove_sr_no.getText().toString();
                }
                if(lin_vts_id_remove.getVisibility()==View.VISIBLE){
                    s_e_device_id = remove_vts_id.getText().toString();
                }else {
                    s_e_device_id="0";
                }
                s_reg_no = remove_reg_no.getText().toString();
                if (s_reg_no.equals("")) {
                    s_reg_no = "0";
                }
                s_remove_reason = reason_remove.getSelectedItem().toString();
                s_date = t_install_date.getText().toString();
                s_Time = t_install_Time.getText().toString();
                s_new_drs_id = "0";
                s_new_device_id = "0";
                s_drs_id = "0";
                veh_condition = "W";
                others = "";
                disconnection_reason = "0";
                mgt_set = "N";
                not_available_activity = "0";
                not_available_reason = "0";
                old_sim_no = "0";
                new_sim_no = "0";
                cut_off = "N";
                serial_no = "";
                sim_reason = "0";
                sim_provider = "0";
                mgt_set = "N";
                door_sensor = "N";
                ignition_sensor = "N";
                is_demo = "N";
                missing_type = "M";
                collection_amount = "0";
                collection_date = "0";
                collection_type = "0";
                image = "0";
                missing_reason = "0";
                is_drs = "N";
                s_rep_srNo = "";
                confirmVehNo = "";
                s_reinst_conf_reg_no = "";
                contact_person = "";
                contact_no = "0";
                payment_type = "C";
                sensor_old_veh_no = sensor_veh_no_remove.getText().toString();
                SparseBooleanArray checked = lvItem.getCheckedItemPositions();
                itemsCollected = "0";
                for (int i = 0; i < checked.size(); i++) {
                    int key = checked.keyAt(i);
                    itemsCollected = itemsCollected + (collected_items.get(key).getId()) + ":";
                }
                s_remarks = e_remarks.getText().toString();
                if (removal_type.equals("1") || (removal_type.equals("2")) || (removal_type.equals("4") || (removal_type.equals("5") && (!removeDeviceType.equalsIgnoreCase("S"))))) {
                    if(s_old_serial_no.equals("")||(s_old_serial_no.equalsIgnoreCase("SELECT SR.NO."))){
                        Toast.makeText(getContext(), "Select Serial No.", Toast.LENGTH_LONG).show();
                    } else if (s_old_serial_no.equals("") && (til_remove_sr.getVisibility() == View.VISIBLE) && (!removeDeviceType.equalsIgnoreCase("S"))) {
                        remove_sr_no.setError("Sr. no. can't be null");
                    } else if((vltddeviceRemove.getSelectedItem().toString().equalsIgnoreCase("AIS 140"))&&(!(s_old_serial_no.matches(con_remove_sr_no.getText().toString())))){
                        con_remove_sr_no.setError("Sr.No. not match");
                    }else if((vltddeviceRemove.getSelectedItem().toString().equalsIgnoreCase("E124"))&&(!(s_old_serial_no.matches(con_remove_sr_no.getText().toString())))){
                        con_remove_sr_no.setError("Sr.No. not match");
                    } else if((lin_vts_id_remove.getVisibility()==View.VISIBLE)&&(s_e_device_id.equalsIgnoreCase("")||(s_e_device_id.equalsIgnoreCase("Null"))||(s_e_device_id.equalsIgnoreCase("0")))){
                        remove_vts_id.setError("VTS Id can't be null");
                    }else if((lin_vts_id_remove.getVisibility()==View.VISIBLE)&&(!(s_e_device_id.matches(con_remove_vts_id.getText().toString())))){
                        con_remove_vts_id.setError("VTS id not match");
                    } else if ((!removeDeviceType.equalsIgnoreCase("S")) && ((s_reg_no.equals("") || s_reg_no.equals("0") || s_reg_no.equals("null")))) {
                        remove_reg_no.setError("Registration no.can't be null or zero");
                    } else if (s_remove_reason.equalsIgnoreCase("Select Removal Reason") && (!removeDeviceType.equalsIgnoreCase("S"))) {
                        Toast.makeText(getContext(), "Select Reason", Toast.LENGTH_LONG).show();
                    } else if (itemsCollected.equals("") && (!removeDeviceType.equalsIgnoreCase("S"))) {
                        Toast.makeText(getActivity(), "Select Items Collected", Toast.LENGTH_SHORT).show();
                    } else if (!(removeDeviceType.equalsIgnoreCase("D")) && ((sensor_veh_remove.getVisibility() == View.GONE))) {
                        Toast.makeText(getContext(), "Please Select Sensor", Toast.LENGTH_SHORT).show();
                    } else if ((sensor_veh_remove.getVisibility() == View.VISIBLE) && (sensor_veh_no_remove.getText().toString().equalsIgnoreCase("") && (!(removeDeviceType.equalsIgnoreCase("V"))))) {
                        sensor_veh_no_remove.setError("Please provide vehicle no");
                    } else {
                        updateInstallationData();
                    }
                }
                if (removal_type.equals("3")) {
                    if ((s_old_serial_no.equalsIgnoreCase("SELECT SR.NO."))) {
                        Toast.makeText(getActivity(), "Sr. No. can't be null", Toast.LENGTH_SHORT).show();
                    } else if (s_remove_reason.equalsIgnoreCase("Select Removal Reason")) {
                        Toast.makeText(getContext(), "Select Reason", Toast.LENGTH_LONG).show();
                    } else if (itemsCollected.equals("")) {
                        Toast.makeText(getActivity(), "Select Items Collected", Toast.LENGTH_SHORT).show();
                    } else if ((sensor_veh_remove.getVisibility() == View.VISIBLE) && (sensor_veh_no_remove.getText().toString().equalsIgnoreCase(""))) {
                        sensor_veh_no_remove.setError("Please provide vehicle no");
                    } else {
                        updateInstallationData();
                    }
                }
            } else if (s_work_id.equalsIgnoreCase("1")) {
                String MobilePattern = "[0-9]{10}";
                if ((linear_device_sr_no_fault_e_series.getVisibility() == View.VISIBLE)) {
                    s_old_serial_no = fault_vts_id.getText().toString();
                    s_e_device_id="0";
                }
                if (linear_device_sr_no_fault.getVisibility() == View.VISIBLE) {
                    s_old_serial_no = vltd_sr_no_fault.getText().toString();
                    s_e_device_id="0";
                }

                s_reg_no = fault_reg_no.getText().toString();
                contact_person = faultPersonName.getText().toString();
                if (faultDetail.getVisibility() == View.VISIBLE) {
                    contact_no = faultPersonNumber.getText().toString();
                } else {
                    contact_no = "0";
                }
                serial_no = "";
                s_device_id = "0";
                s_drs_id = "0";
                s_new_drs_id = "0";
                status = "0";
                s_VehicleTypeInst = "0";
                s_reason_repla = "0";
                removalReason = "0";
                old_sim_no = "0";
                new_sim_no = "0";
                mgt_set = "N";
                s_rep_srNo = "";
                door_sensor = "N";
                ignition_sensor = "N";
                missing_reason = "0";
                removal_type = "0";
                cut_off = "N";
                itemsCollected = "0";
                s_new_device_id = "0";
                is_drs = "N";
                drs_dirction = "N";
                disconnection_reason = "0";
                not_available_activity = "0";
                not_available_reason = "0";
                is_demo = "N";
                missing_type = "M";
                collection_amount = "0";
                collection_date = "0";
                collection_type = "0";
                image = "0";
                confirmVehNo = "";
                s_reinst_conf_reg_no = "";
                payment_type = "C";
                s_date = t_install_date.getText().toString();
                s_Time = t_install_Time.getText().toString();
                String image = imageNameFault.getText().toString();
                SparseBooleanArray checked = lv.getCheckedItemPositions();
                others = "";
                for (int i = 0; i < checked.size(); i++) {
                    int key = checked.keyAt(i);
                    others = others + (list_change_values.get(key).getId()) + ":";
                }
                if (others.contains("6")) {
                    veh_condition = "U";
                } else {
                    veh_condition = "W";
                }
                s_remarks = e_remarks.getText().toString();
                if (s_vts_type.equalsIgnoreCase("SELECT VTS TYPE")) {
                    Toast.makeText(getContext(), "Please Select Device Type", Toast.LENGTH_LONG).show();
                } else if ((s_old_serial_no.equalsIgnoreCase("SELECT SR.NO."))) {
                    Toast.makeText(getActivity(), "Sr. No. can't be null", Toast.LENGTH_SHORT).show();
                } else if ((linear_device_sr_no_fault.getVisibility() == View.VISIBLE)&&(s_old_serial_no.equals("") && (vltd_sr_no_fault.getVisibility() == View.VISIBLE))) {
                    vltd_sr_no_fault.setError("Sr No can't be null");
                } else if((vltddeviceFault.getSelectedItem().toString().equalsIgnoreCase("AIS 140"))&&(!(s_old_serial_no.matches(con_fault_sr_no.getText().toString())))){
                    con_fault_sr_no.setError("Sr.No. not match");
                }else if((vltddeviceFault.getSelectedItem().toString().equalsIgnoreCase("E124"))&&(!(s_old_serial_no.matches(con_fault_sr_no.getText().toString())))){
                    con_fault_sr_no.setError("Sr.No. not match");
                } else if (s_reg_no.equals("") || s_reg_no.equals("0") || s_reg_no.equals("null")) {
                    fault_reg_no.setError("Registration no.can't be null or zero");
                } else if (others.equals("")) {
                    Toast.makeText(getActivity(), "Select changed values", Toast.LENGTH_SHORT).show();
                } else if ((vehicleTypeFault.getSelectedItem().toString().equalsIgnoreCase("Select Vehicle Type"))) {
                    Toast.makeText(getContext(), "Please Select Vehicle type", Toast.LENGTH_LONG).show();
                } else if ((faultDetail.getVisibility() == View.VISIBLE) && (faultPersonName.getText().toString().equalsIgnoreCase(""))) {
                    faultPersonName.setError("Person name can't be null");
                } else if ((faultDetail.getVisibility() == View.VISIBLE) && (faultPersonNumber.getText().toString().equalsIgnoreCase(""))) {
                    faultPersonNumber.setError("Contact No. can't be null");
                } else if ((faultDetail.getVisibility() == View.VISIBLE) && (!faultPersonNumber.getText().toString().matches(MobilePattern))) {
                    faultPersonNumber.setError("Please Enter valid Mobile No.");
                } else {
                    s_remarks = e_remarks.getText().toString();
                    if (!image.equals("")) {
                        mProgress.setProgress(0);
                        updateInstallationDataImage();
                    } else {
                        updateInstallationData();
                    }
                }
            } else if (s_work_id.equalsIgnoreCase("6")) {
                String MobilePattern = "[0-9]{10}";
                if (linear_device_sr_no_phone_e_series.getVisibility() == View.VISIBLE) {
                    s_old_serial_no = phsupport_vts_id.getText().toString();
                    s_e_device_id="0";
                }
                if (linear_device_sr_no_phone.getVisibility() == View.VISIBLE) {
                    s_old_serial_no = vltd_sr_no_phn.getText().toString();
                    s_e_device_id="0";
                }
                serial_no = "";
                s_reg_no = phSupport_reg_no.getText().toString();
                device_type="0";
                s_device_id = "0";
                s_drs_id = "0";
                s_new_drs_id = "0";
                status = "0";
                s_VehicleTypeInst = "0";
                s_reason_repla = "0";
                old_sim_no = "0";
                new_sim_no = "0";
                sim_reason = "0";
                sim_provider = "0";
                mgt_set = "N";
                door_sensor = "N";
                ignition_sensor = "N";
                missing_reason = "0";
                baseImage = "0";
                removal_type = "0";
                cut_off = "N";
                removalReason = "0";
                itemsCollected = "0";
                s_new_device_id = "0";
                is_drs = "N";
                drs_dirction = "N";
                mgt_set = "N";
                not_available_activity = "0";
                not_available_reason = "0";
                is_demo = "N";
                missing_type = "M";
                collection_amount = "0";
                collection_date = "0";
                collection_type = "0";
                image = "0";
                s_rep_srNo = "";
                confirmVehNo = "";
                s_reinst_conf_reg_no = "";
                payment_type = "C";
                personName = phSupportPersonName.getText().toString();
                personPhone = phSupportPersonPhone.getText().toString();
                contact_person = personName;
                contact_no = personPhone;
                if (disconnection_reason.equals("1")) {
                    veh_condition = "U";
                } else {
                    veh_condition = "W";
                }
                s_date = t_install_date.getText().toString();
                s_Time = t_install_Time.getText().toString();
                s_remarks = e_remarks.getText().toString();
                if (s_vts_type.equalsIgnoreCase("SELECT VTS TYPE")) {
                    Toast.makeText(getContext(), "Please Select Device Type", Toast.LENGTH_LONG).show();
                } else if ((s_old_serial_no.equalsIgnoreCase("SELECT SR.NO."))) {
                    Toast.makeText(getActivity(), "Sr. No. can't be null", Toast.LENGTH_SHORT).show();
                } else if ((linear_device_sr_no_phone.getVisibility() == View.VISIBLE)&&(s_old_serial_no.equals("") && (vltd_sr_no_phn.getVisibility() == View.VISIBLE))) {
                    vltd_sr_no_phn.setError("Sr No can't be null");
                } else if((vltddevicephn.getSelectedItem().toString().equalsIgnoreCase("AIS 140"))&&(!(s_old_serial_no.matches(con_phone_sr_no.getText().toString())))){
                    con_phone_sr_no.setError("Sr.No. not match");
                }else if((vltddevicephn.getSelectedItem().toString().equalsIgnoreCase("E124"))&&(!(s_old_serial_no.matches(con_phone_sr_no.getText().toString())))){
                    con_phone_sr_no.setError("Sr.No. not match");
                } else if (s_reg_no.equals("") || s_reg_no.equals("0") || s_reg_no.equals("null")) {
                    phSupport_reg_no.setError("Registration no.can't be null or zero");
                } else if ((discReason.getSelectedItem().toString().equalsIgnoreCase("Select Reason"))) {
                    Toast.makeText(getContext(), "Please Select Reason", Toast.LENGTH_LONG).show();
                } else if (phSupportPersonName.getText().toString().equalsIgnoreCase("")) {
                    phSupportPersonName.setError("Person name can't be null");
                } else if (phSupportPersonPhone.getText().toString().equalsIgnoreCase("")) {
                    phSupportPersonPhone.setError("Phone no can't be null");
                } else if (!phSupportPersonPhone.getText().toString().matches(MobilePattern)) {
                    phSupportPersonPhone.setError("Please Enter valid Mobile No");
                } else {
                    s_remarks = e_remarks.getText().toString();
                    updateInstallationData();
                }
            } else if (s_work_id.equalsIgnoreCase("7")) {
                s_e_device_id = sim_vts_id.getText().toString();
                new_sim_no = e_new_sim_no.getText().toString();
                old_sim_no = e_old_sim_no.getText().toString();
                s_reg_no = sim_vehicle_no.getText().toString();
                if (old_sim_no.equals("0")) {
                    old_sim_no = "0000000000000";
                } else {
                    old_sim_no = e_old_sim_no.getText().toString();
                }
                s_old_serial_no = "";
                s_vts_type = "2";
                s_device_id = "0";
                s_drs_id = "0";
                s_new_drs_id = "0";
                status = "0";
                s_VehicleTypeInst = "0";
                s_reason_repla = "0";
                mgt_set = "N";
                door_sensor = "N";
                ignition_sensor = "N";
                is_demo = "N";
                missing_type = "M";
                collection_amount = "0";
                collection_date = "0";
                collection_type = "0";
                image = "0";
                removalReason = "0";
                itemsCollected = "0";
                s_new_device_id = "0";
                is_drs = "N";
                drs_dirction = "N";
                veh_condition = "W";
                disconnection_reason = "0";
                mgt_set = "N";
                not_available_reason = "0";
                not_available_activity = "0";
                missing_reason = "0";
                removal_type = "0";
                cut_off = "N";
                serial_no = "";
                contact_person = "";
                contact_no = "0";
                payment_type = "C";
                s_date = t_install_date.getText().toString();
                s_Time = t_install_Time.getText().toString();
                s_remarks = e_remarks.getText().toString();
                if (s_e_device_id.equals("")) {
                    sim_vts_id.setError("Vts Id can't be null");
                } else if (s_reg_no.equalsIgnoreCase("Please Enter Registration No.") || s_reg_no.equals("") || s_reg_no.equals("0") || s_reg_no.equals("null")) {
                    sim_vehicle_no.setError("Please Enter valid no. or Zero");
                } else if (old_sim_no.equals("")) {
                    e_old_sim_no.setError("Please enter old Sim No.");
                } else if (new_sim_no.equals("")) {
                    e_new_sim_no.setError("Please enter new Sim No.");
                } else if (!(old_sim_no.length() == 10 || old_sim_no.length() == 13)) {
                    e_old_sim_no.setError("Please enter valid 10 or 13 digit mobile no. ");
                } else if (!(new_sim_no.length() == 10 || new_sim_no.length() == 13)) {
                    e_new_sim_no.setError("Please enter valid 10 or 13 digit mobile no. ");
                } else if ((sim_replace_reason.getSelectedItem().toString().equalsIgnoreCase("Replacement Reason"))) {
                    Toast.makeText(getContext(), "Please Select Reason", Toast.LENGTH_LONG).show();
                } else if ((sim_operator.getSelectedItem().toString().equalsIgnoreCase("New Sim Provider"))) {
                    Toast.makeText(getContext(), "Please Select Sim Provider", Toast.LENGTH_LONG).show();
                } else {
                    s_remarks = e_remarks.getText().toString();
                    updateInstallationData();
                }
            } else if (s_work_id.equalsIgnoreCase("8")) {
                if (linear_device_sr_no_missing_e_series.getVisibility() == View.VISIBLE) {
                    s_old_serial_no = mDevice_vts_id.getText().toString();
                }
                if (linear_device_sr_no_missing.getVisibility() == View.VISIBLE) {
                    s_old_serial_no = vltd_sr_no_miss.getText().toString();
                }
                s_reg_no = mDevice_reg_no.getText().toString();
                serial_no = "";
                s_device_id = "0";
                s_drs_id = "0";
                s_new_drs_id = "0";
                status = "0";
                s_VehicleTypeInst = "0";
                s_reason_repla = "0";
                removalReason = "0";
                old_sim_no = "0";
                new_sim_no = "0";
                sim_reason = "0";
                sim_provider = "0";
                mgt_set = "N";
                door_sensor = "N";
                ignition_sensor = "N";
                removal_type = "0";
                cut_off = "N";
                cut_off = "N";
                itemsCollected = "0";
                s_new_device_id = "0";
                is_drs = "N";
                drs_dirction = "N";
                disconnection_reason = "0";
                veh_condition = "W";
                mgt_set = "N";
                not_available_activity = "0";
                not_available_reason = "0";
                is_demo = "N";
                collection_amount = "0";
                collection_date = "0";
                collection_type = "0";
                image = "0";
                confirmVehNo = "";
                s_reinst_conf_reg_no = "";
                contact_person = "";
                contact_no = "0";
                payment_type = "C";
                s_date = t_install_date.getText().toString();
                s_Time = t_install_Time.getText().toString();
                s_remarks = e_remarks.getText().toString();
                sensor_old_veh_no = sensor_veh_no_missing.getText().toString();
                String image = imageNameMissing.getText().toString();
                if (s_vts_type.equalsIgnoreCase("SELECT VTS TYPE")) {
                    Toast.makeText(getContext(), "Please Select Device Type", Toast.LENGTH_LONG).show();
                } else if (s_old_serial_no.equals("")  && (!(missDeviceType.equalsIgnoreCase("S")))) {
                    vltd_sr_no_miss.setError("Sr. no. can't be null");
                } else if((vltddeviceMiss.getSelectedItem().toString().equalsIgnoreCase("AIS 140"))&&(!(s_old_serial_no.matches(con_missing_sr_no.getText().toString())))){
                    con_missing_sr_no.setError("Sr.No. not match");
                }else if((vltddeviceMiss.getSelectedItem().toString().equalsIgnoreCase("E124"))&&(!(s_old_serial_no.matches(con_missing_sr_no.getText().toString())))){
                    con_missing_sr_no.setError("Sr.No. not match");
                } else if ((!missDeviceType.equalsIgnoreCase("S")) && ((s_reg_no.equals("") || s_reg_no.equals("0") || s_reg_no.equals("null")))) {
                    mDevice_reg_no.setError("Registration no. can't be null or zero");
                } else if ((relMissing.getVisibility() == View.VISIBLE) && (missingType.getSelectedItem().toString().equalsIgnoreCase("Select Reason") && (!(missDeviceType.equalsIgnoreCase("S"))))) {
                    Toast.makeText(getActivity(), "Please Select Reason", Toast.LENGTH_SHORT).show();
                } else if (missDeviceType.equalsIgnoreCase("S") && (sensor_veh_missing.getVisibility() == View.GONE)) {
                    Toast.makeText(getActivity(), "Please Select Sensor", Toast.LENGTH_SHORT).show();
                } else if ((sensor_veh_missing.getVisibility() == View.VISIBLE) && (sensor_veh_no_missing.getText().toString().equalsIgnoreCase("") && (missDeviceType.equalsIgnoreCase("S")))) {
                    sensor_veh_no_missing.setError("Please provide Vehicle No");
                } else if ((sensor_veh_missing.getVisibility() == View.VISIBLE) && (sensor_veh_no_missing.getText().toString().equalsIgnoreCase("") && (missDeviceType.equalsIgnoreCase("B")))) {
                    sensor_veh_no_missing.setError("Please provide Vehicle No");
                } else {
                    s_remarks = e_remarks.getText().toString();
                    if (!image.equals("")) {
                        mProgress.setProgress(0);
                        updateInstallationDataImage();
                    } else {
                        updateInstallationData();
                    }
                }
            } else if (s_work_id.equalsIgnoreCase("9")) {
                s_e_device_id = vehNotAvailVtsID.getText().toString();
                if (s_e_device_id.equals("")) {
                    s_e_device_id = "0";
                }
                s_reg_no = vehNotAvailRegNo.getText().toString();
                if (s_reg_no.equals("")) {
                    s_reg_no = "0";
                }
                s_vts_type = "2";
                s_old_serial_no = "";
                s_device_id = "0";
                s_drs_id = "0";
                s_new_drs_id = "0";
                status = "0";
                s_VehicleTypeInst = "0";
                s_reason_repla = "0";
                removalReason = "0";
                old_sim_no = "0";
                new_sim_no = "0";
                missing_reason = "0";
                removal_type = "0";
                cut_off = "N";
                serial_no = "";
                itemsCollected = "0";
                s_new_device_id = "0";
                is_drs = "N";
                drs_dirction = "N";
                disconnection_reason = "0";
                veh_condition = "W";
                mgt_set = "N";
                is_demo = "N";
                missing_type = "M";
                collection_amount = "0";
                collection_date = "0";
                collection_type = "0";
                image = "0";
                s_rep_srNo = "";
                s_reinst_conf_reg_no = "";
                contact_person = "";
                contact_no = "0";
                payment_type = "C";
                s_date = t_install_date.getText().toString();
                s_Time = t_install_Time.getText().toString();
                s_remarks = e_remarks.getText().toString();
                if ((vehDetail.getVisibility() == View.VISIBLE) && (s_e_device_id.equals(""))) {
                    vehNotAvailVtsID.setError("Vts Id can't be null");
                } else if ((vehDetail.getVisibility() == View.VISIBLE) && (s_reg_no.equals("") || s_reg_no.equals("0") || s_reg_no.equals("null") && (vehNotAvailRegNo.getVisibility() == View.VISIBLE))) {
                    vehNotAvailRegNo.setError("Registration no.can't be null or zero");
                } else if (vehiclenoavailSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Activity")) {
                    Toast.makeText(getContext(), "Please Select Activity", Toast.LENGTH_LONG).show();
                } else if (notAvailReason.getSelectedItem().toString().equalsIgnoreCase("Select Reason")) {
                    Toast.makeText(getContext(), "Please Select Reason", Toast.LENGTH_LONG).show();
                } else if (not_available_reason.equals("3") && (s_remarks.equals(""))) {
                    e_remarks.setError("Please Fill Remarks");
                } else if (not_available_activity.equals("7") && (s_remarks.equals(""))) {
                    e_remarks.setError("Please Fill Remarks");
                } else {
                    s_remarks = e_remarks.getText().toString();
                    updateInstallationData();
                }
            } else if (s_work_id.equalsIgnoreCase("10")) {
                String abc = amount.getText().toString();
                if (followUp.getVisibility() == View.GONE) {
                    if (abc.equalsIgnoreCase("")) {
                        amount.setError("Fill Field");
                    } else {
                        x = Integer.parseInt(abc);
                        collection_amount = abc + ".00";
                    }
                    collection_date = paymentDate.getText().toString();
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_remarks = e_remarks.getText().toString();
                    String images = imageName.getText().toString();
                    s_vts_type = "2";
                    s_old_serial_no = "";
                    s_reg_no = "0";
                    device_type = "0";
                    removal_type = "0";
                    s_device_id = "0";
                    s_drs_id = "0";
                    s_new_drs_id = "0";
                    status = "0";
                    s_VehicleTypeInst = "0";
                    s_reason_repla = "0";
                    removalReason = "0";
                    old_sim_no = "0";
                    new_sim_no = "0";
                    sim_reason = "0";
                    sim_provider = "0";
                    mgt_set = "N";
                    door_sensor = "N";
                    ignition_sensor = "N";
                    itemsCollected = "0";
                    s_new_device_id = "0";
                    is_drs = "N";
                    drs_dirction = "N";
                    disconnection_reason = "0";
                    veh_condition = "W";
                    mgt_set = "N";
                    not_available_activity = "0";
                    not_available_reason = "0";
                    is_demo = "N";
                    missing_type = "M";
                    missing_reason = "0";
                    cut_off = "N";
                    serial_no = "0";
                    s_rep_srNo = "";
                    confirmVehNo = "";
                    s_reinst_conf_reg_no = "";
                    contact_person = "";
                    contact_no = "0";
                    if (payment_method.getSelectedItem().toString().equalsIgnoreCase("Select Payment Method")) {
                        Toast.makeText(getActivity(), "Please Select Payment Method", Toast.LENGTH_SHORT).show();
                    } else if (x < 200) {
                        payValue.setVisibility(View.VISIBLE);
                    } else {
                        payValue.setVisibility(View.GONE);
                        s_remarks = e_remarks.getText().toString();
                        if (!images.equals("")) {
                            mProgress.setProgress(0);
                            updateInstallationDataImage();
                        } else {
                            updateInstallationData();
                        }
                    }
                } else {
                    String MobilePattern = "[0-9]{10}";
                    if (followUpPersonName.getText().toString().equalsIgnoreCase("")) {
                        followUpPersonName.setError("Person name can't be null");
                    } else if (followUpPersonPhone.getText().toString().equalsIgnoreCase("")) {
                        followUpPersonPhone.setError("Phone no can't be null");
                    } else if (!followUpPersonPhone.getText().toString().matches(MobilePattern)) {
                        followUpPersonPhone.setError("Please enter valid mobile no");
                    } else {
                        s_date = t_install_date.getText().toString();
                        s_Time = t_install_Time.getText().toString();
                        personName = followUpPersonName.getText().toString();
                        personPhone = followUpPersonPhone.getText().toString();
                        contact_person = personName;
                        contact_no = personPhone;
                        s_vts_type = "2";
                        s_remarks = e_remarks.getText().toString();
                        image = "";
                        collection_date = "0";
                        s_reg_no = "0";
                        collection_amount = "0";
                        device_type = "0";
                        removal_type = "0";
                        s_device_id = "0";
                        s_drs_id = "0";
                        s_new_drs_id = "0";
                        status = "0";
                        s_VehicleTypeInst = "0";
                        s_reason_repla = "0";
                        removalReason = "0";
                        old_sim_no = "0";
                        new_sim_no = "0";
                        sim_reason = "0";
                        sim_provider = "0";
                        mgt_set = "N";
                        door_sensor = "N";
                        ignition_sensor = "N";
                        itemsCollected = "0";
                        s_new_device_id = "0";
                        is_drs = "N";
                        drs_dirction = "N";
                        disconnection_reason = "0";
                        veh_condition = "W";
                        mgt_set = "N";
                        not_available_activity = "0";
                        not_available_reason = "0";
                        is_demo = "N";
                        missing_type = "M";
                        missing_reason = "0";
                        collection_type = "0";
                        image = "0";
                        cut_off = "N";
                        serial_no = "";
                        s_rep_srNo = "";
                        confirmVehNo = "";
                        s_reinst_conf_reg_no = "";
                        updateInstallationData();
                    }
                }
            } else if (s_work_id.equalsIgnoreCase("11")) {
                s_date = t_install_date.getText().toString();
                s_Time = t_install_Time.getText().toString();
                s_remarks = e_remarks.getText().toString();
                if (s_remarks.equals("")) {
                    e_remarks.setError("Please Fill Details");
                } else {
                    s_remarks = e_remarks.getText().toString();
                    s_vts_type = "2";
                    s_old_serial_no = "";
                    image = "";
                    collection_date = "0";
                    s_reg_no = "0";
                    collection_amount = "0";
                    device_type = "0";
                    removal_type = "0";
                    s_device_id = "0";
                    s_drs_id = "0";
                    s_new_drs_id = "0";
                    status = "0";
                    s_VehicleTypeInst = "0";
                    s_reason_repla = "0";
                    removalReason = "0";
                    old_sim_no = "0";
                    new_sim_no = "0";
                    sim_reason = "0";
                    sim_provider = "0";
                    mgt_set = "N";
                    door_sensor = "N";
                    ignition_sensor = "N";
                    itemsCollected = "0";
                    s_new_device_id = "0";
                    is_drs = "N";
                    drs_dirction = "N";
                    disconnection_reason = "0";
                    veh_condition = "W";
                    mgt_set = "N";
                    not_available_activity = "0";
                    not_available_reason = "0";
                    is_demo = "N";
                    missing_type = "M";
                    missing_reason = "0";
                    collection_type = "0";
                    image = "0";
                    cut_off = "N";
                    serial_no = "";
                    s_rep_srNo = "";
                    contact_person = "";
                    contact_no = "0";
                    payment_type = "C";
                    confirmVehNo = "";
                    s_reinst_conf_reg_no = "";
                    updateInstallationData();
                }
            }
        });
        imageUpload.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
            } else {
                buttonPressedActivity = "1";
                ShowImagePopup(getActivity());
            }
        });
        imageUploadfault.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
            } else {
                buttonPressedActivity = "2";
                ShowImagePopup(getActivity());
            }
        });
        imageUploadMissing.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
            } else {
                buttonPressedActivity = "3";
                ShowImagePopup(getActivity());
            }
        });
        return v;
    }

    private void getSerialNo() {
        receiveDeviceControllers.get_serial_no(user_id,id_dist,clientLocId,s_work_id,db_name,server_name,this);
    }

    private void ShowImagePopup(FragmentActivity activity) {
        View alet_view = null;
        final Dialog alertDialogBuilder = new Dialog(activity);
        alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        alet_view = mInflater.inflate(R.layout.custom_popup_image, null);
        final ImageView cross = alet_view.findViewById(R.id.cross);
        final TextView galary = alet_view.findViewById(R.id.gallery);
        final TextView cammera = alet_view.findViewById(R.id.cammera);
        alertDialogBuilder.setContentView(alet_view);
        alertDialogBuilder.getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        alertDialogBuilder.setCanceledOnTouchOutside(true);
        alertDialogBuilder.show();
        galary.setOnClickListener((View view) -> {
            alertDialogBuilder.dismiss();
            buttonPressed = "2";
            galleryIntent();
        });
        cammera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed = "1";
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
                    if (Build.VERSION.SDK_INT < 24) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        uri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        try {
                            //startActivityForResult(intent, REQUEST_CAMERA);
                            openCameraIntent();
                            alertDialogBuilder.dismiss();
                        } catch (SecurityException e) {
                            e.printStackTrace();
                            try {
                                if (hasPermissions(getActivity(), PERMISSIONS)) {
                                } else {
                                    EasyPermissions.requestPermissions(this, "Access for storage", 101, PERMISSIONS);
                                }
                            } catch (Exception qe) {
                                qe.printStackTrace();
                            }
                            openCameraIntent();
                            alertDialogBuilder.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            alertDialogBuilder.dismiss();
                        }
                    } else {
                        try {
                            if (hasPermissions(getActivity(), PERMISSIONS)) {
                            } else {
                                EasyPermissions.requestPermissions(this, "Access for storage", 101, PERMISSIONS);
                            }
                        } catch (Exception qe) {
                            qe.printStackTrace();
                        }
                        openCameraIntent();
                        alertDialogBuilder.dismiss();
                    }
                }
            }

            File getOutputMediaFile(int type) {
                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.d("****", "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                        return null;
                    }
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                File mediaFile;
                if (type == MEDIA_TYPE_IMAGE) {
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                    Log.d("****", "");
                } else {
                    return null;
                }
                return mediaFile;
            }
        });
        cross.setOnClickListener(v -> alertDialogBuilder.dismiss());
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (Build.VERSION.SDK_INT < 24) {
                try {
                    path = uri.getPath();
                    file = new File(path);
                    String abc = path;
                    String filename = abc.substring(abc.lastIndexOf("/") + 1);
                    if (buttonPressedActivity.equals("1")) {
                        imageName.setText(filename);
                    } else if (buttonPressedActivity.equals("2")) {
                        imageNameFault.setText(filename);
                    } else if (buttonPressedActivity.equals("3")) {
                        imageNameMissing.setText(filename);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Click Again", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == REQUEST_CAPTURE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                bmp = BitmapFactory.decodeFile(path);
                String abc = path;
                String filename = abc.substring(abc.lastIndexOf("/") + 1);
                // imageName.setText(filename);
                if (buttonPressedActivity.equals("1")) {
                    imageName.setText(filename);
                } else if (buttonPressedActivity.equals("2")) {
                    imageNameFault.setText(filename);
                } else if (buttonPressedActivity.equals("3")) {
                    imageNameMissing.setText(filename);
                }
            }
        } else if (requestCode == SELECT_PHOTO)
            onSelectFromGalleryResult(data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            uri = data.getData();
            file = FileUtils.getFile(getActivity(), uri);
            path = file.getPath();
            compressImage(path);
            File file = new File(path);
            // imageName.setText(file.getName());
            if (buttonPressedActivity.equals("1")) {
                imageName.setText(file.getName());
            } else if (buttonPressedActivity.equals("2")) {
                imageNameFault.setText(file.getName());
            } else if (buttonPressedActivity.equals("3")) {
                imageNameMissing.setText(file.getName());
            }
        }
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createImageFile();
            if (photoFile != null) {
                uri = FileProvider.getUriForFile(getActivity(), "in.eoninfotech.eontechnician.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    public String compressImage(String imageUri) {
        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        try {
            bmp = BitmapFactory.decodeFile(path, options);

        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        ExifInterface exif;
        try {
            exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        path = getFilename();
        try {
            out = new FileOutputStream(path);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "DCIM/Camera");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(imageFileName,/* prefix */".jpg",/* suffix */storageDir/* directory */);
        } catch (IOException e) {
            e.printStackTrace();
        }
        path = image.getAbsolutePath();
        return image;
    }

    private File bitmapToFile(Bitmap bitmap, String fileName) {
        File filesDir = getActivity().getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, fileName + ".jpg");
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);
            os.flush();
            os.close();
            return imageFile;
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return null;
    }

    private void getVTSDetails() {
        newInstallmentController.reqeuestVtsDetails(vts_id,server_name,db_name, this);
    }

    private void getVTSDetail() {
        newInstallmentController.reqeuestVtsDetail(vts_id, server_name,db_name,this);
    }

    private void addclients() {
        newInstallmentController.reqeuestClientList(this);
    }

    private void removal_type() {
        newInstallmentController.requestRemovalActivityResponse(this);
    }

    private void addVehNotAvailReason() {
        newInstallmentController.requestVehNotAvailReasonResponse(this);
    }

    private void getSimOperator() {
        newInstallmentController.reqeuestSimOperatorResponse(this);
    }

    private void getSimReasonList() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestSimReplaceResponse(this);
    }

    private void getFaultList() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestFaultList(this);
    }

    private void getPhoneSupportList() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestDisconnection(this);
    }

    private void getItemCollectList() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestCollectedItemList(this);
    }

    private void addReasonRemove() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestRemovalReason(this);
    }

    private void fetchReasons() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestReplaceReason(this);
    }

    private void addLocation() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestClientLocation(id_dist,server_name,db_name, this);
    }

    private void addActivity() {
        ShowProgressBar(true);
        newInstallmentController.notAvailActivityResponse(this);
    }

    private void addWorkType() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestworkType(this);
    }

    private void addVehType() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestvehicleType(this);
    }

    private void damageReason() {
        newInstallmentController.reqeuestDamageReason(this);
    }

    private void pMethod() {
        newInstallmentController.reqeuestPMethod(this);
    }

    private void getUmVehicle() {
        ApiHolder get_list = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<UmVehicleResponse> call = get_list.get_veh_for_um(clientId, clientLocId);
        call.enqueue(new Callback<UmVehicleResponse>() {
            @Override
            public void onResponse(Call<UmVehicleResponse> call, Response<UmVehicleResponse> response) {
                try {
                    if (response.body().getType().equalsIgnoreCase("1")) {
                        getUmVehicle = response.body().getUmVehicleDetails();
                        try {
                            try {
                                arr_device_types.clear();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            arr_device_types.add(" SELECT VEHICLE");
                            for (int i = 0; i < getUmVehicle.size(); i++) {
                                arr_device_types.add(getUmVehicle.get(i).getReg_no());
                            }
                            adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, arr_device_types);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            vehicle_list_um.setAdapter(adapter);
                            vehicle_list_pm.setAdapter(adapter);
                        } catch (NullPointerException npe) {
                            npe.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        Toast.makeText(getActivity(), "No data", Toast.LENGTH_LONG).show();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UmVehicleResponse> call, Throwable t) {
                t.printStackTrace();
                failureData();
            }
        });
    }

    private void updateInstallationData() {
        progressDialog.show();
        RequestBody technician_id = RequestBody.create(MediaType.parse("text/plain"), user_id);
        RequestBody activity_date = RequestBody.create(MediaType.parse("text/plain"), s_date);
        RequestBody activity_time = RequestBody.create(MediaType.parse("text/plain"), s_Time);
        RequestBody customer = RequestBody.create(MediaType.parse("text/plain"), clientId);
        RequestBody customer_location = RequestBody.create(MediaType.parse("text/plain"), clientLocId);
        RequestBody isDemo = RequestBody.create(MediaType.parse("text/plain"), is_demo);
        RequestBody activity_type = RequestBody.create(MediaType.parse("text/plain"), s_work_id);
        RequestBody vts_type = RequestBody.create(MediaType.parse("text/plain"), s_vts_type);
        RequestBody is_DRS = RequestBody.create(MediaType.parse("text/plain"), is_drs);
        RequestBody deviceType = RequestBody.create(MediaType.parse("text/plain"), device_type);
        RequestBody old_device_id = RequestBody.create(MediaType.parse("text/plain"), s_e_device_id);
        RequestBody new_device_id = RequestBody.create(MediaType.parse("text/plain"), s_new_device_id);
        RequestBody old_serial_no = RequestBody.create(MediaType.parse("text/plain"), s_old_serial_no);
        RequestBody new_serial_no = RequestBody.create(MediaType.parse("text/plain"), serial_no);
        RequestBody reg_no = RequestBody.create(MediaType.parse("text/plain"), s_reg_no);
        RequestBody veh_type = RequestBody.create(MediaType.parse("text/plain"), s_vehicletype);
        RequestBody old_drs = RequestBody.create(MediaType.parse("text/plain"), s_drs_id);
        RequestBody new_drs = RequestBody.create(MediaType.parse("text/plain"), s_new_drs_id);
        RequestBody drs_direction = RequestBody.create(MediaType.parse("text/plain"), drs_dirction);
        RequestBody Mgt_set = RequestBody.create(MediaType.parse("text/plain"), mgt_set);
        RequestBody ignSensor = RequestBody.create(MediaType.parse("text/plain"), ignition_sensor);
        RequestBody fuelSensor = RequestBody.create(MediaType.parse("text/plain"), fuel_sensor);
        RequestBody doorSensor = RequestBody.create(MediaType.parse("text/plain"), door_sensor);
        RequestBody panic_button = RequestBody.create(MediaType.parse("text/plain"), panic);
        RequestBody cutOff = RequestBody.create(MediaType.parse("text/plain"), cut_off);
        RequestBody replacement_reason = RequestBody.create(MediaType.parse("text/plain"), s_reason_repla);
        RequestBody removalType = RequestBody.create(MediaType.parse("text/plain"), removal_type);
        RequestBody removeReason = RequestBody.create(MediaType.parse("text/plain"), removalReason);
        RequestBody disconnectReason = RequestBody.create(MediaType.parse("text/plain"), disconnection_reason);
        RequestBody missingType = RequestBody.create(MediaType.parse("text/plain"), missing_type);
        RequestBody missingReason = RequestBody.create(MediaType.parse("text/plain"), missing_reason);
        RequestBody notAvailActivity = RequestBody.create(MediaType.parse("text/plain"), not_available_activity);
        RequestBody notAvailReason = RequestBody.create(MediaType.parse("text/plain"), not_available_reason);
        RequestBody collectionDate = RequestBody.create(MediaType.parse("text/plain"), collection_date);
        RequestBody collectionType = RequestBody.create(MediaType.parse("text/plain"), collection_type);
        RequestBody collectionAmount = RequestBody.create(MediaType.parse("text/plain"), collection_amount);
        RequestBody paymentType = RequestBody.create(MediaType.parse("text/plain"), payment_type);
        RequestBody contactPerson = RequestBody.create(MediaType.parse("text/plain"), contact_person);
        RequestBody contactNo = RequestBody.create(MediaType.parse("text/plain"), contact_no);
        RequestBody simProvider = RequestBody.create(MediaType.parse("text/plain"), sim_provider);
        RequestBody oldSimNo = RequestBody.create(MediaType.parse("text/plain"), old_sim_no);
        RequestBody newSimNo = RequestBody.create(MediaType.parse("text/plain"), new_sim_no);
        RequestBody simReason = RequestBody.create(MediaType.parse("text/plain"), sim_reason);
        RequestBody veh_Condition = RequestBody.create(MediaType.parse("text/plain"), veh_condition);
        RequestBody remarks = RequestBody.create(MediaType.parse("text/plain"), s_remarks);
        RequestBody itemCollected = RequestBody.create(MediaType.parse("text/plain"), itemsCollected);
        RequestBody faults_checked = RequestBody.create(MediaType.parse("text/plain"), others);
        RequestBody fuel_reading = RequestBody.create(MediaType.parse("text/plain"), fuel_voltage);
        RequestBody lid_statu = RequestBody.create(MediaType.parse("text/plain"), lid_status);
        RequestBody tran = RequestBody.create(MediaType.parse("text/plain"), trans);
        RequestBody temp_senso = RequestBody.create(MediaType.parse("text/plain"), temp_sensor);
        RequestBody tilt_senso = RequestBody.create(MediaType.parse("text/plain"), tilt_sensor);
        RequestBody fuel_statu = RequestBody.create(MediaType.parse("text/plain"), fuel_status);
        RequestBody panic_statu = RequestBody.create(MediaType.parse("text/plain"), panic_status);
        RequestBody sensor_veh_n = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(sen_vehicle_no));
        RequestBody sensor_old_veh_n = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(sensor_old_veh_no));
        RequestBody remove_type = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(missDeviceType));
        RequestBody drs_status = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(drsStatus));
        RequestBody replacetype = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(replace_type));

        MultipartBody.Part image = null;
        if (buttonPressed.equals("0")) {
            image = null;
        } else if (buttonPressed.equals("1")) {
            try {
                File file = bitmapToFile(bmp, "image_call");
                long length = file.length();
                ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
                image = MultipartBody.Part.createFormData("image", file.getName(), fileBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (buttonPressed.equals("2")) {
            try {
                file = new File(path);
                long length = file.length();
                ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
                image = MultipartBody.Part.createFormData("image", path, fileBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        newInstallmentController.postInstallationsData(technician_id, activity_date, activity_time, customer, customer_location, isDemo, activity_type, vts_type, deviceType, old_device_id, new_device_id, old_serial_no, new_serial_no, reg_no, veh_type, is_DRS, old_drs, new_drs, drs_direction, Mgt_set, ignSensor, fuelSensor, doorSensor,
                panic_button, cutOff, replacement_reason, removalType, removeReason, disconnectReason, missingType, missingReason, notAvailActivity, notAvailReason,
                collectionDate, collectionType, collectionAmount, paymentType, contactPerson, contactNo, simProvider, oldSimNo, newSimNo, simReason, veh_Condition, remarks, itemCollected, faults_checked, fuel_reading, lid_statu, tran, temp_senso, tilt_senso, fuel_statu, panic_statu, sensor_veh_n, sensor_old_veh_n,remove_type,drs_status,replacetype, image, this);
    }

    private void updateInstallationDataImage() {
        progressDialog.show();
        Progress(true);
        RequestBody technician_id = RequestBody.create(MediaType.parse("text/plain"), user_id);
        RequestBody activity_date = RequestBody.create(MediaType.parse("text/plain"), s_date);
        RequestBody activity_time = RequestBody.create(MediaType.parse("text/plain"), s_Time);
        RequestBody customer = RequestBody.create(MediaType.parse("text/plain"), clientId);
        RequestBody customer_location = RequestBody.create(MediaType.parse("text/plain"), clientLocId);
        RequestBody isDemo = RequestBody.create(MediaType.parse("text/plain"), is_demo);
        RequestBody activity_type = RequestBody.create(MediaType.parse("text/plain"), s_work_id);
        RequestBody vts_type = RequestBody.create(MediaType.parse("text/plain"), s_vts_type);
        RequestBody is_DRS = RequestBody.create(MediaType.parse("text/plain"), is_drs);
        RequestBody deviceType = RequestBody.create(MediaType.parse("text/plain"), device_type);
        RequestBody old_device_id = RequestBody.create(MediaType.parse("text/plain"), s_e_device_id);
        RequestBody new_device_id = RequestBody.create(MediaType.parse("text/plain"), s_new_device_id);
        RequestBody old_serial_no = RequestBody.create(MediaType.parse("text/plain"), s_old_serial_no);
        RequestBody new_serial_no = RequestBody.create(MediaType.parse("text/plain"), serial_no);
        RequestBody reg_no = RequestBody.create(MediaType.parse("text/plain"), s_reg_no);
        RequestBody veh_type = RequestBody.create(MediaType.parse("text/plain"), s_vehicletype);
        RequestBody old_drs = RequestBody.create(MediaType.parse("text/plain"), s_drs_id);
        RequestBody new_drs = RequestBody.create(MediaType.parse("text/plain"), s_new_drs_id);
        RequestBody drs_direction = RequestBody.create(MediaType.parse("text/plain"), drs_dirction);
        RequestBody Mgt_set = RequestBody.create(MediaType.parse("text/plain"), mgt_set);
        RequestBody ignSensor = RequestBody.create(MediaType.parse("text/plain"), ignition_sensor);
        RequestBody fuelSensor = RequestBody.create(MediaType.parse("text/plain"), fuel_sensor);
        RequestBody doorSensor = RequestBody.create(MediaType.parse("text/plain"), door_sensor);
        RequestBody panic_button = RequestBody.create(MediaType.parse("text/plain"), panic);
        RequestBody cutOff = RequestBody.create(MediaType.parse("text/plain"), cut_off);
        RequestBody replacement_reason = RequestBody.create(MediaType.parse("text/plain"), s_reason_repla);
        RequestBody removalType = RequestBody.create(MediaType.parse("text/plain"), removal_type);
        RequestBody removeReason = RequestBody.create(MediaType.parse("text/plain"), removalReason);
        RequestBody disconnectReason = RequestBody.create(MediaType.parse("text/plain"), disconnection_reason);
        RequestBody missingType = RequestBody.create(MediaType.parse("text/plain"), missing_type);
        RequestBody missingReason = RequestBody.create(MediaType.parse("text/plain"), missing_reason);
        RequestBody notAvailActivity = RequestBody.create(MediaType.parse("text/plain"), not_available_activity);
        RequestBody notAvailReason = RequestBody.create(MediaType.parse("text/plain"), not_available_reason);
        RequestBody collectionDate = RequestBody.create(MediaType.parse("text/plain"), collection_date);
        RequestBody collectionType = RequestBody.create(MediaType.parse("text/plain"), collection_type);
        RequestBody collectionAmount = RequestBody.create(MediaType.parse("text/plain"), collection_amount);
        RequestBody paymentType = RequestBody.create(MediaType.parse("text/plain"), payment_type);
        RequestBody contactPerson = RequestBody.create(MediaType.parse("text/plain"), contact_person);
        RequestBody contactNo = RequestBody.create(MediaType.parse("text/plain"), contact_no);
        RequestBody simProvider = RequestBody.create(MediaType.parse("text/plain"), sim_provider);
        RequestBody oldSimNo = RequestBody.create(MediaType.parse("text/plain"), old_sim_no);
        RequestBody newSimNo = RequestBody.create(MediaType.parse("text/plain"), new_sim_no);
        RequestBody simReason = RequestBody.create(MediaType.parse("text/plain"), sim_reason);
        RequestBody veh_Condition = RequestBody.create(MediaType.parse("text/plain"), veh_condition);
        RequestBody remarks = RequestBody.create(MediaType.parse("text/plain"), s_remarks);
        RequestBody itemCollected = RequestBody.create(MediaType.parse("text/plain"), itemsCollected);
        RequestBody faults_checked = RequestBody.create(MediaType.parse("text/plain"), others);
        RequestBody fuel_reading = RequestBody.create(MediaType.parse("text/plain"), fuel_voltage);
        RequestBody lid_statu = RequestBody.create(MediaType.parse("text/plain"), lid_status);
        RequestBody tran = RequestBody.create(MediaType.parse("text/plain"), trans);
        RequestBody temp_senso = RequestBody.create(MediaType.parse("text/plain"), temp_sensor);
        RequestBody tilt_senso = RequestBody.create(MediaType.parse("text/plain"), tilt_sensor);
        RequestBody fuel_statu = RequestBody.create(MediaType.parse("text/plain"), fuel_status);
        RequestBody panic_statu = RequestBody.create(MediaType.parse("text/plain"), panic_status);
        RequestBody sensor_veh_n = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(sen_vehicle_no));
        RequestBody sensor_old_veh_n = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(sensor_old_veh_no));
        RequestBody remove_type = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(missDeviceType));
        RequestBody drs_status = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(drsStatus));
        RequestBody replacetype = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(replace_type));

        MultipartBody.Part image = null;
        if (buttonPressed.equals("0")) {
            image = null;
        } else if (buttonPressed.equals("1")) {
            try {
                File file = bitmapToFile(bmp, "image_call");
                long length = file.length();
                ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
                image = MultipartBody.Part.createFormData("image", file.getName(), fileBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (buttonPressed.equals("2")) {
            try {
                file = new File(path);
                long length = file.length();
                ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
                image = MultipartBody.Part.createFormData("image", path, fileBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        newInstallmentController.postInstallationsData(technician_id, activity_date, activity_time, customer, customer_location, isDemo, activity_type, vts_type, deviceType, old_device_id, new_device_id, old_serial_no, new_serial_no, reg_no, veh_type, is_DRS, old_drs, new_drs, drs_direction, Mgt_set, ignSensor, fuelSensor, doorSensor,
                panic_button, cutOff, replacement_reason, removalType, removeReason, disconnectReason, missingType, missingReason, notAvailActivity, notAvailReason,
                collectionDate, collectionType, collectionAmount, paymentType, contactPerson, contactNo, simProvider, oldSimNo, newSimNo, simReason, veh_Condition, remarks, itemCollected, faults_checked, fuel_reading, lid_statu, tran, temp_senso, tilt_senso, fuel_statu, panic_statu, sensor_veh_n, sensor_old_veh_n,remove_type,drs_status,replacetype, image, this);
    }

    boolean run = true; //set it to false if you want to stop the timer
    Handler mHandler = new Handler();

    public void timer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (run) {
                    try {
                        Thread.sleep(60000);
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                Calendar c = Calendar.getInstance();
                                int min = c.get(Calendar.MINUTE);
                                int hour = c.get(Calendar.HOUR_OF_DAY);
                                s_time = hour + ":" + min;
                                t_install_Time.setText(s_time);
                            }
                        });
                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }

    private void setDateAndTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        s_time = dateFormat.format(calen.getTime());
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        hour = calen.get(Calendar.HOUR_OF_DAY);
        minutes = calen.get(Calendar.MINUTE);
        seconds = calen.get(Calendar.SECOND);
        if (month + 1 < 10) {
            current_date = day + "-0" + month + "-" + year;
        } else {
            current_date = day + "-" + month + "-" + year;
        }
        t_install_date.setText(current_date);
        paymentDate.setText(current_date);
        t_install_Time.setText(s_time);
        t_install_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });
        paymentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDateforPayment();
            }
        });
        t_install_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTime();
            }
        });
    }

    private void getTime() {
        TimePickerDialog tpd = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                selected_totime = hours + ":" + minutes;
                t_install_Time.setText(selected_totime);
            }
        }, hour, minutes, false);
        tpd.show();
    }

    private void getDate() {
        final DatePickerDialog dpdd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            // TODO Auto-generated method stub
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (monthOfYear + 1 < 10) {
                    selected_todate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
                } else {
                    selected_todate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                }
                t_install_date.setText(selected_todate);
            }
        }, year, month - 1, day);
        dpdd.getDatePicker().setMaxDate(calen.getTimeInMillis());
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        //c.add(Calendar.DATE, -4);
        long minDate = c.getTime().getTime();
        dpdd.getDatePicker().setMinDate(minDate);
        dpdd.show();
    }

    private void getDateforPayment() {
        final DatePickerDialog dpdd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            // TODO Auto-generated method stub
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (monthOfYear + 1 < 10) {
                    selected_todate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
                } else {
                    selected_todate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                }
                paymentDate.setText(selected_todate);
            }
        }, year, month - 1, day);
        dpdd.getDatePicker().setMaxDate(calen.getTimeInMillis());
        dpdd.show();
    }

    private void ShowProgressBar(boolean show) {
        try {
            if (show) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearData() {

        getDeviceTypes();
        getFaultList();
        getSimOperator();
        getSimReasonList();
        getPhoneSupportList();
        addVehType();
        addReasonRemove();
        addVehNotAvailReason();
        getDevice();
        getItemCollectList();
        addReasonRemove();
        removal_type();
        damageReason();
        getSerialNo();
        fetchReasons();
        s_vts_type = "SELECT VTS TYPE";
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
        fuel_voltage = "0";
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
        vltd_sr_no_fault.setText("");
        vltd_sr_no_phn.setText("");
        vltd_sr_no.setText("");
        remove_deviceid.setText("");
        phsupport_vts_id.setText("");
        sim_vts_id.setText("");
        remove_sr_no.setText("");
        followUpPersonName.setText("");
        followUpPersonPhone.setText("");
        buttonPressed = "0";
        others = "";
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
        s_e_device_id = "0";
        s_new_device_id = "0";
        s_vehicletype = "0";
        s_device_id = "0";
        s_reg_no = "0";
        is_drs = "N";
        s_drs_id = "0";
        s_new_drs_id = "0";
        drs_dirction = "N";
        s_reason_repla = "0";
        removalReason = "0";
        rep_srNo.setText("");
        con_in_reg_no.setText("");
        faultPersonNumber.setText("");
        faultPersonName.setText("");
        itemsCollected = "0";
        others = "";
        s_remarks = "0";
        disconnection_reason = "0";
        ignition_sensor = "N";
        fuel_sensor = "N";
        door_sensor = "N";
        veh_condition = "W";
        mgt_set = "N";
        sim_provider = "0";
        old_sim_no = "0";
        new_sim_no = "0";
        sim_reason = "0";
        not_available_reason = "0";
        not_available_activity = "0";
        is_demo = "N";
        collection_amount = "0";
        collection_date = "0";
        collection_type = "0";
        missing_reason = "0";
        removal_type = "0";
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
        con_in_reg_no.setText("");
        con_missing_sr_no.setText("");
        con_new_deviceid.setText("");
        con_old_deviceidreplace.setText("");
        con_phone_sr_no.setText("");
        con_reinstall_sr_no.setText("");
        con_remove_sr_no.setText("");
        con_vltd_sr_no.setText("");
        image = "";
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
    }

    public void clearTextView(){
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
        fuel_voltage = "0";
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
        vltd_sr_no_fault.setText("");
        vltd_sr_no_phn.setText("");
        vltd_sr_no.setText("");
        remove_deviceid.setText("");
        phsupport_vts_id.setText("");
        sim_vts_id.setText("");
        remove_sr_no.setText("");
        followUpPersonName.setText("");
        followUpPersonPhone.setText("");
        buttonPressed = "0";
        others = "";
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
        s_e_device_id = "0";
        s_new_device_id = "0";
        s_vehicletype = "0";
        s_device_id = "0";
        s_reg_no = "0";
        is_drs = "N";
        s_drs_id = "0";
        s_new_drs_id = "0";
        drs_dirction = "N";
        s_reason_repla = "0";
        removalReason = "0";
        rep_srNo.setText("");
        con_in_reg_no.setText("");
        faultPersonNumber.setText("");
        faultPersonName.setText("");
        itemsCollected = "0";
        others = "";
        s_remarks = "0";
        disconnection_reason = "0";
        ignition_sensor = "N";
        fuel_sensor = "N";
        door_sensor = "N";
        veh_condition = "W";
        mgt_set = "N";
        sim_provider = "0";
        old_sim_no = "0";
        new_sim_no = "0";
        sim_reason = "0";
        not_available_reason = "0";
        not_available_activity = "0";
        is_demo = "N";
        collection_amount = "0";
        collection_date = "0";
        collection_type = "0";
        missing_reason = "0";
        removal_type = "0";
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
        con_in_reg_no.setText("");
        con_missing_sr_no.setText("");
        con_new_deviceid.setText("");
        con_old_deviceidreplace.setText("");
        con_phone_sr_no.setText("");
        con_reinstall_sr_no.setText("");
        con_remove_sr_no.setText("");
        con_vltd_sr_no.setText("");
        image = "";
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
    }

    @Override
    public void clientResponse(ClientResponse response) {
        try {
            clientList = response.getClientList();
            try {
                try {
                    clientDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                clientDetail.add(" SELECT CLIENT");
                for (int i = 0; i < clientList.size(); i++) {
                    clientDetail.add(clientList.get(i).getClient_Name());
                }
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, clientDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                client.setAdapter(adapter);
                ShowProgressBar(false);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void locationResponse(ClientLocationResponse response) {
        try {
            locationList = response.getClientLoc();
            try {
                try {
                    locationDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                locationDetail.add("SELECT LOCATION");
                for (int i = 0; i < locationList.size(); i++) {
                    locationDetail.add(locationList.get(i).getLoc_Name());
                }
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, locationDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                location.setAdapter(adapter);
                ShowProgressBar(false);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void workTypeResponse(WorkTypeResponse response) {
        try {
            workTypeList = response.getWorktypeList();
            try {
                try {
                    workDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                workDetail.add("SELECT WORK TYPE");
                for (int i = 0; i < workTypeList.size(); i++) {
                    workDetail.add(workTypeList.get(i).getWork_Name());
                }
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, workDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                workType.setAdapter(adapter);
                ShowProgressBar(false);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void vehicleTypeResponse(VehicleTypeResponse response) {
        try {
            vehicleList = response.getVehicletypeList();
            try {
                try {
                    vehicleDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                vehicleDetail.add("SELECT VEHICLE TYPE");
                for (int i = 0; i < vehicleList.size(); i++) {
                    vehicleDetail.add(vehicleList.get(i).getVehicle_Name());
                }
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, vehicleDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                vehicleType.setAdapter(adapter);
                new_in_vehicleTypeReins.setAdapter(adapter);
                vehicleTypeFault.setAdapter(adapter);
                vehicleTypeSim.setAdapter(adapter);
                ShowProgressBar(false);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void faultListResponse(FaultResponse response) {
        try {
            list_change_values = response.getFaultLists();
            try {
                try {
                    value_name.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (list_change_values.size() > 0) {
                    for (int i = 0; i < list_change_values.size(); i++) {
                        value_name.add(list_change_values.get(i).getName());
                    }
                    if (list_change_values.size() > 5) {
                        lv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * list_change_values.size() + 1));
                    } else {
                        lv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * list_change_values.size()));
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_list_item, value_name);
                    lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    lv.setAdapter(adapter);
                    ShowProgressBar(false);
                } else {
                }
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void replaceResponse(ReplaceReason response) {
        try {
            arr_replaceReasons = response.getReplaceList();
            try {
                try {
                    arr_reasons_s.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                arr_reasons_s.add("SELECT REASON");
                for (int i = 0; i < arr_replaceReasons.size(); i++) {
                    arr_reasons_s.add(arr_replaceReasons.get(i).getReplace_Name());
                }
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, arr_reasons_s);
                reason_replace.setAdapter(adapter);
                ShowProgressBar(false);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnectionResponse(DisconnectionResponse response) {
        try {
            supportList = response.getDisconnectionDetails();
            try {
                try {
                    disc_reason.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                disc_reason.add("SELECT REASON");
                if (supportList.size() > 0) {
                    for (int i = 0; i < supportList.size(); i++) {
                        disc_reason.add(supportList.get(i).getName());
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, disc_reason);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    discReason.setAdapter(adapter);
                    ShowProgressBar(false);
                } else {
                }
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removalActivityResponse(RemovalActivityResponse response) {
        try {
            removalActivityDetails = response.getRemovalActivityDetails();
            try {
                try {
                    removalActivity.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                removalActivity.add("SELECT ACTION TYPE");
                if (removalActivityDetails.size() > 0) {
                    for (int i = 0; i < removalActivityDetails.size(); i++) {
                        removalActivity.add(removalActivityDetails.get(i).getA_name());
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, removalActivity);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    removalType.setAdapter(adapter);
                } else {
                }
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removalResponse(RemovalResponse response) {
        try {
            removalList = response.getRemovalList();
            try {
                try {
                    removalDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                removalDetail.add("SELECT REMOVAL REASON");
                for (int i = 0; i < removalList.size(); i++) {
                    removalDetail.add(removalList.get(i).getRemoval_Name());
                }
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, removalDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                reason_remove.setAdapter(adapter);
                ShowProgressBar(false);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void damageResponse(RemovalResponse response) {
        try {
            damageList = response.getRemovalList();
            try {
                try {
                    removalDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                removalDetail.add("SELECT REASON");
                for (int i = 0; i < damageList.size(); i++) {
                    removalDetail.add(damageList.get(i).getRemoval_Name());
                }
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, removalDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                missingType.setAdapter(adapter);
                ShowProgressBar(false);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void collectItemResponse(CollectedItemsResponse response) {
        try {
            collected_items = response.getItemLists();
            try {
                try {
                    item_name.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (collected_items.size() > 0) {
                    for (int i = 0; i < collected_items.size(); i++) {
                        item_name.add(collected_items.get(i).getName());
                    }
                    if (collected_items.size() > 5) {
                        lvItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * collected_items.size() + 1));
                    } else {
                        lvItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * collected_items.size() + 1));
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_list_item, item_name);
                    lvItem.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    lvItem.setAdapter(adapter);
                    ShowProgressBar(false);
                } else {
                }
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void simOperatorResponse(SimOperatorResponse response) {
        try {
            simOperatorDetails = response.getSimOperatorDetails();
            try {
                try {
                    simOperator.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                simOperator.add("NEW SIM PROVIDER");
                if (simOperatorDetails.size() > 0) {
                    for (int i = 0; i < simOperatorDetails.size(); i++) {
                        simOperator.add(simOperatorDetails.get(i).getSp_name());
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, simOperator);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sim_operator.setAdapter(adapter);
                } else {
                }
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void simReplaceReason(SimReplaceResponse response) {
        try {
            simreplacereason = response.getSimDetails();
            try {
                try {
                    simReplaceReason.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                simReplaceReason.add("REPLACEMENT REASON");
                if (simreplacereason.size() > 0) {
                    for (int i = 0; i < simreplacereason.size(); i++) {
                        simReplaceReason.add(simreplacereason.get(i).getS_name());
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, simReplaceReason);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sim_replace_reason.setAdapter(adapter);
                    ShowProgressBar(false);
                } else {
                }
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notAvailActivity(NotAvailActivityResponse response) {
        try {
            notAvailActivityDetails = response.getNotAvailActivityDetails();
            try {
                try {
                    notAvailActivity.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                notAvailActivity.add("SELECT ACTIVITY");
                for (int i = 0; i < notAvailActivityDetails.size(); i++) {
                    notAvailActivity.add(notAvailActivityDetails.get(i).getActivity());
                }
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, notAvailActivity);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                vehiclenoavailSpinner.setAdapter(adapter);
                ShowProgressBar(false);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void vehicleNotAvailReason(VehNotAvailReasonResponse response) {
        try {
            vehNotAvailReasonDetails = response.getVehNotAvailReasonDetails();
            try {
                try {
                    vehNotAvailReason.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                vehNotAvailReason.add("SELECT REASON");
                if (vehNotAvailReasonDetails.size() > 0) {
                    for (int i = 0; i < vehNotAvailReasonDetails.size(); i++) {
                        vehNotAvailReason.add(vehNotAvailReasonDetails.get(i).getReason());
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, vehNotAvailReason);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    notAvailReason.setAdapter(adapter);
                } else {
                }
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void vtsResponses(VTSResponse response) {
        try {
            vtsList = response.getVtsDetails();
            try {
                if (s_work_id.equalsIgnoreCase("3")) {
                    if (vtsList.size() == 0) {
                        regNo.setText("");
                        fault_reg_no.setText("");
                        remove_reg_no.setText("");
                        phSupport_reg_no.setText("");
                        s_vehicletype="0";
                    } else {
                        for (int i = 0; i < vtsList.size(); i++) {
                            s_reg_no = String.valueOf(vtsList.get(i).getReg_no());
                            if(vtsList.get(i).getVeh_type_id().equalsIgnoreCase("")){
                                s_vehicletype="0";
                            }else {
                                s_vehicletype = vtsList.get(i).getVeh_type_id();
                            }
                            device_type = vtsList.get(i).getDevice_type();
                            s_e_device_id = vtsList.get(i).getBus_id();
                            String location_Name = vtsList.get(i).getLocation_name();
                            if (s_reg_no.equalsIgnoreCase("null") || (s_reg_no.equalsIgnoreCase("") || (s_reg_no.equals("0")))) {
                                drs_veh_no.setText("Please Enter Registration no.");
                                drs_veh_no.setTextColor(getResources().getColor(R.color.greyed_out));
                                drs_veh_no.setEnabled(true);
                            } else {
                                drs_veh_no.setText(s_reg_no);
                                drs_veh_no.setTextColor(getResources().getColor(R.color.black));
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void vtsResponse(VTSResponse response) {
        try {
            vtsList = response.getVtsDetails();
            try {
                if (vtsList.size() == 0) {
                    regNo.setText("");
                    fault_reg_no.setText("");
                    remove_reg_no.setText("");
                    phSupport_reg_no.setText("");
                    s_vehicletype="0";
                } else {
                    for (int i = 0; i < vtsList.size(); i++) {
                        s_reg_no = String.valueOf(vtsList.get(i).getReg_no());
                        if(vtsList.get(i).getVeh_type_id().equalsIgnoreCase("")){
                            s_vehicletype = "0";
                        }else {
                            s_vehicletype = vtsList.get(i).getVeh_type_id();
                        }
                        device_type = vtsList.get(i).getDevice_type();
                        String location_Name = vtsList.get(i).getLocation_name();
                        if (s_work_id.equalsIgnoreCase("3")) {
                            if (s_reg_no.equalsIgnoreCase("null") || (s_reg_no.equalsIgnoreCase("") || (s_reg_no.equals("0")))) {
                                regNo.setText("Please Enter Registration no.");
                                regNo.setTextColor(getResources().getColor(R.color.greyed_out));
                                regNo.setEnabled(true);
                            } else {
                                regNo.setText(s_reg_no);
                                regNo.setTextColor(getResources().getColor(R.color.black));
                                plantName.setText(location_Name);
                            }
                        } else if (s_work_id.equalsIgnoreCase("1")) {
                            if (s_reg_no.equalsIgnoreCase("null") || (s_reg_no.equalsIgnoreCase("") || (s_reg_no.equals("0")))) {
                                fault_reg_no.setText("Please Enter Registration no.");
                                fault_reg_no.setTextColor(getResources().getColor(R.color.greyed_out));
                                fault_reg_no.setEnabled(true);
                            } else {
                                fault_reg_no.setText(s_reg_no);
                                fault_reg_no.setTextColor(getResources().getColor(R.color.black));
                                s_vehicletype = "0";
                            }
                        } else if (s_work_id.equalsIgnoreCase("5")) {
                            if (s_reg_no.equalsIgnoreCase("null") || (s_reg_no.equalsIgnoreCase("") || (s_reg_no.equals("0")))) {
                                remove_reg_no.setText("Please Enter Registration no.");
                                remove_reg_no.setTextColor(getResources().getColor(R.color.greyed_out));
                                remove_reg_no.setEnabled(true);
                            } else {
                                remove_reg_no.setText(s_reg_no);
                                remove_reg_no.setTextColor(getResources().getColor(R.color.black));
                            }
                        } else if (s_work_id.equalsIgnoreCase("6")) {
                            if (s_reg_no.equalsIgnoreCase("null") || (s_reg_no.equalsIgnoreCase("") || (s_reg_no.equals("0")))) {
                                phSupport_reg_no.setText("Please Enter Registration No.");
                                phSupport_reg_no.setTextColor(getResources().getColor(R.color.greyed_out));
                                phSupport_reg_no.setEnabled(true);
                            } else {
                                phSupport_reg_no.setText(s_reg_no);
                                phSupport_reg_no.setTextColor(getResources().getColor(R.color.black));
                            }
                        } else if (s_work_id.equalsIgnoreCase("7")) {
                            if (s_reg_no.equalsIgnoreCase("null") || (s_reg_no.equalsIgnoreCase("") || (s_reg_no.equals("0")))) {
                                sim_vehicle_no.setText("Please Enter Registration No.");
                                sim_vehicle_no.setTextColor(getResources().getColor(R.color.greyed_out));
                                sim_vehicle_no.setEnabled(true);
                            } else {
                                sim_vehicle_no.setText(s_reg_no);
                                sim_vehicle_no.setTextColor(getResources().getColor(R.color.black));
                            }
                        } else if (s_work_id.equalsIgnoreCase("8")) {
                            if (s_reg_no.equalsIgnoreCase("null") || (s_reg_no.equalsIgnoreCase("") || (s_reg_no.equals("0")))) {
                                mDevice_reg_no.setText("Please Enter Registration No.");
                                mDevice_reg_no.setTextColor(getResources().getColor(R.color.greyed_out));
                                mDevice_reg_no.setEnabled(true);
                            } else {
                                mDevice_reg_no.setText(s_reg_no);
                                mDevice_reg_no.setTextColor(getResources().getColor(R.color.black));
                            }
                        } else if (s_work_id.equalsIgnoreCase("9")) {
                            s_reg_no = String.valueOf(vtsList.get(i).getReg_no());
                            String abc = vtsList.get(i).getVeh_type_id();
                            if (abc.equals("")) {
                                s_vehicletype = "0";
                            } else {
                                s_vehicletype = vtsList.get(i).getVeh_type_id();
                            }
                            device_type = vtsList.get(i).getDevice_type();
                            if (s_reg_no.equalsIgnoreCase("null") || (s_reg_no.equalsIgnoreCase("") || (s_reg_no.equals("0")))) {
                                vehNotAvailRegNo.setText("Please Enter Registration No.");
                                vehNotAvailRegNo.setTextColor(getResources().getColor(R.color.greyed_out));
                                vehNotAvailRegNo.setEnabled(true);
                            } else {
                                vehNotAvailRegNo.setText(s_reg_no);
                                vehNotAvailRegNo.setTextColor(getResources().getColor(R.color.black));
                            }
                        } else if (s_work_id.equalsIgnoreCase("4")) {
                            device_type = vtsList.get(i).getDevice_type();
                        }
                    }
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pMethod(PaymentMethodResponse response) {
        try {
            paymentmethod = response.getMethods();
            try {
                try {
                    pMethod.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pMethod.add("SELECT PAYMENT METHOD");
                if (paymentmethod.size() > 0) {
                    for (int i = 0; i < paymentmethod.size(); i++) {
                        pMethod.add(paymentmethod.get(i).getMethod());
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, pMethod);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    payment_method.setAdapter(adapter);
                } else {
                }
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateDataResponse(MainResponse response) {
        clearData();
        pMethod();
        try {
            Toast.makeText(getContext(), "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            if (response.getType() == 1) {
                circularRelative.setVisibility(View.GONE);
                Progress(false);
                progressDialog.hide();
                clearData();
                for (int i = 0; i < lvItem.getCount(); i++) {
                    View view = lvItem.getChildAt(i);
                    CheckedTextView mCheckBox = view.findViewById(R.id.text1);
                    mCheckBox.setChecked(false);
                    lvItem.clearChoices();
                }
                for (int i = 0; i < lv.getCount(); i++) {
                    View view = lv.getChildAt(i);
                    CheckedTextView mCheckBox = view.findViewById(R.id.text1);
                    mCheckBox.setChecked(false);
                    lv.clearChoices();
                }
                is_Demo.check(is_demo_no.getId());
                is_demo_no.setChecked(true);
                l_in.setChecked(true);
                doorNo.setChecked(true);
                cutoffNo.setChecked(true);
                panicNo.setChecked(true);
                fuelNo.setChecked(true);
                if (s_work_id.equalsIgnoreCase("2")) {
                    addVehType();
                } else if (s_work_id.equalsIgnoreCase("4")) {
                    addVehType();
                } else if (s_work_id.equalsIgnoreCase("3")) {
                    fetchReasons();
                } else if (s_work_id.equalsIgnoreCase("5")) {
                    removal_type();
                    addReasonRemove();
                } else if (s_work_id.equalsIgnoreCase("6")) {
                    getPhoneSupportList();
                } else if (s_work_id.equalsIgnoreCase("7")) {
                    getSimReasonList();
                    getSimOperator();
                } else if (s_work_id.equalsIgnoreCase("9")) {
                    addActivity();
                    addVehNotAvailReason();
                } else if (s_work_id.equalsIgnoreCase("8")) {
                    damageReason();
                } else if (s_work_id.equalsIgnoreCase("10")) {
                    pMethod();
                } else if (s_work_id.equalsIgnoreCase("1")) {
                    faultDetail.setVisibility(View.GONE);
                    addVehType();
                }
            } else {
                progressDialog.hide();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
    }

    @Override
    public void onError() {
    }

    @Override
    public void onFinish() {
    }

    void getDeviceTypes() {
        ApiHolder get_list = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<VTSTypeResponse> call = get_list.getVTSTypes();
        call.enqueue(new Callback<VTSTypeResponse>() {
            @Override
            public void onResponse(Call<VTSTypeResponse> call, Response<VTSTypeResponse> response) {
                try {
                    if (response.body().getType().equalsIgnoreCase("1")) {
                        deviceTypeOtherAis_arr = response.body().getDeviceTypesArr();
                        try {
                            try {
                                arr_device_types.clear();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            arr_device_types.add(" SELECT VTS TYPE");
                            for (int i = 0; i < deviceTypeOtherAis_arr.size(); i++) {
                                arr_device_types.add(deviceTypeOtherAis_arr.get(i).getName());
                            }
                            adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, arr_device_types);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            vltddevice.setAdapter(adapter);
                            vltddeviceRemove.setAdapter(adapter);
                            vltddeviceNotAvail.setAdapter(adapter);
                            vltddeviceReinst.setAdapter(adapter);
                            vltddeviceReplace.setAdapter(adapter);
                            vltddeviceFault.setAdapter(adapter);
                            vltddeviceMiss.setAdapter(adapter);
                            vltddevicephn.setAdapter(adapter);
                            vltddsimReplace.setAdapter(adapter);
                        } catch (NullPointerException npe) {
                            npe.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        Toast.makeText(getActivity(), "No data", Toast.LENGTH_LONG).show();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<VTSTypeResponse> call, Throwable t) {
                t.printStackTrace();
                failureData();
            }
        });
    }

    private void getDevice() {
        ApiHolder get_list = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<MainResponse> call = get_list.getDeviceTypes();
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                try {
                    if (response.body().getType() == 1) {
                        deviceTypeOtherAis_arr = response.body().getDeviceTypesArr();
                        try {
                            try {
                                arr_device_types.clear();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            arr_device_types.add("SELECT DEVICE TYPE");
                            for (int i = 0; i < deviceTypeOtherAis_arr.size(); i++) {
                                arr_device_types.add(deviceTypeOtherAis_arr.get(i).getName());
                            }
                            adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, arr_device_types);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            device.setAdapter(adapter);
                            device_reinstall.setAdapter(adapter);
                        } catch (NullPointerException npe) {
                            npe.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        Toast.makeText(getActivity(), "No data", Toast.LENGTH_LONG).show();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                t.printStackTrace();
                failureData();
            }
        });
    }

    private void failureData() {
        try {
            TSnackbar snackbar = TSnackbar.make(v, K.TRY_AGAIN, TSnackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.RED);
            TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } catch (Exception e) {
            try {
                Toast.makeText(getActivity(), K.TRY_AGAIN, Toast.LENGTH_LONG).show();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void Progress(boolean show) {
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular);
        mProgress = v.findViewById(R.id.circularProgressbar);
        mProgress.setProgress(0);   // Main Progress
        mProgress.setSecondaryProgress(100); // Secondary Progress
        mProgress.setMax(100); // Maximum Progress
        mProgress.setProgressDrawable(drawable);
        tv = v.findViewById(R.id.tv);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (pStatus < 100) {
                    pStatus += 1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mProgress.setProgress(pStatus);
                            tv.setText(pStatus + "%");
                        }
                    });
                    try {
                        Thread.sleep(15); //thread will take approx 1.5 seconds to finish
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void receiveDeviceResponse(MainResponse response) {

        try {
            srnoList = response.getSrno_device_list().get(0).getNew_sr_no();
            removesrnoList = response.getSrno_device_list().get(0).getOld_sr_no();
            try {
                try {
                    srNoDetails.clear();
                    removesrNoDetails.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                srNoDetails.add(" SELECT SR.NO.");
                removesrNoDetails.add(" SELECT SR.NO.");
                for (int i = 0; i < srnoList.size(); i++) {
                    srNoDetails.add(srnoList.get(i).getPcb_sr_no());
                }
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, srNoDetails);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sr_no.setAdapter(adapter);
                spn_new_sr_no_replace.setAdapter(adapter);


                for (int i = 0; i < removesrnoList.size(); i++) {
                    removesrNoDetails.add(removesrnoList.get(i).getPcb_sr_no());
                }

                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, removesrNoDetails);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_remove_sr_no.setAdapter(adapter);
                spn_old_sr_no_replace.setAdapter(adapter);
                spn_fault_sr_no.setAdapter(adapter);
                spn_phone_sr_no.setAdapter(adapter);
                spn_missing_sr_no.setAdapter(adapter);
                spn_reinstall_sr_no.setAdapter(adapter);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveDispatchMaterial(MainResponse response) {

    }

    @Override
    public void returnDeviceresponse(MainResponse response) {

    }

    @Override
    public void dispatchFromTechResponse(MainResponse response) {

    }
}

