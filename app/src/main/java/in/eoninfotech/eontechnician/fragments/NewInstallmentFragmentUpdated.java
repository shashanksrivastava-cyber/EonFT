
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
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
import android.view.ViewStub;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
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

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import dagger.hilt.android.AndroidEntryPoint;
import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.di.SharedPreferenceManager;
import in.eoninfotech.eontechnician.helper.InstallationValidator;
import in.eoninfotech.eontechnician.utils.ImageUtil;
import in.eoninfotech.eontechnician.UnderMaintenanceVehicles;
import in.eoninfotech.eontechnician.databinding.ActivityAddUmBinding;
import in.eoninfotech.eontechnician.databinding.RemovalFromUmBinding;
import in.eoninfotech.eontechnician.helper.InstallationFormHelper;
import in.eoninfotech.eontechnician.responses.DeviceTypeOtherAis;
import in.eoninfotech.eontechnician.responses.MainClientList;
import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.responses.PMethodDetail;
import in.eoninfotech.eontechnician.responses.PaymentMethodResponse;
import in.eoninfotech.eontechnician.responses.SrNoDeviceList;
import in.eoninfotech.eontechnician.callbacks.ReceiveDeviceListener;
import in.eoninfotech.eontechnician.controllers.NewInstallmentController;
import in.eoninfotech.eontechnician.NonScrollListView;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.responses.ClientDetails;
import in.eoninfotech.eontechnician.responses.ClientLocationDetail;
import in.eoninfotech.eontechnician.responses.ClientLocationResponse;
import in.eoninfotech.eontechnician.responses.ClientResponse;
import in.eoninfotech.eontechnician.responses.CollectedItemsResponse;
import in.eoninfotech.eontechnician.responses.DisconnectionDetail;
import in.eoninfotech.eontechnician.responses.DisconnectionResponse;
import in.eoninfotech.eontechnician.responses.FaultList;
import in.eoninfotech.eontechnician.responses.FaultResponse;
import in.eoninfotech.eontechnician.responses.ItemList;
import in.eoninfotech.eontechnician.responses.NotAvailActivityDetail;
import in.eoninfotech.eontechnician.responses.NotAvailActivityResponse;
import in.eoninfotech.eontechnician.responses.RemovalActivityDetail;
import in.eoninfotech.eontechnician.responses.RemovalActivityResponse;
import in.eoninfotech.eontechnician.responses.RemovalList;
import in.eoninfotech.eontechnician.responses.RemovalResponse;
import in.eoninfotech.eontechnician.responses.ReplaceReason;
import in.eoninfotech.eontechnician.responses.ReplaceReasonDetail;
import in.eoninfotech.eontechnician.responses.SimDetail;
import in.eoninfotech.eontechnician.responses.SimOperatorDetail;
import in.eoninfotech.eontechnician.responses.SimOperatorResponse;
import in.eoninfotech.eontechnician.responses.SimReplaceResponse;
import in.eoninfotech.eontechnician.responses.VTSDetail;
import in.eoninfotech.eontechnician.responses.VTSResponse;
import in.eoninfotech.eontechnician.responses.VehNotAvailReasonDetail;
import in.eoninfotech.eontechnician.responses.VehNotAvailReasonResponse;
import in.eoninfotech.eontechnician.responses.VehicleTypeDetail;
import in.eoninfotech.eontechnician.responses.VehicleTypeResponse;
import in.eoninfotech.eontechnician.responses.WorkTypeDetail;
import in.eoninfotech.eontechnician.responses.WorkTypeResponse;
import in.eoninfotech.eontechnician.callbacks.ClientListener;
import in.eoninfotech.eontechnician.controllers.ReceiveDeviceControllers;
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.helper.FileUtils;
import in.eoninfotech.eontechnician.helper.ProgressRequestBody;
import in.eoninfotech.eontechnician.utils.DialogUtils;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.view.MyTextView;
import in.eoninfotech.eontechnician.viewModel.DeviceViewModel;
import in.eoninfotech.eontechnician.viewModel.ViewModelClientLocation;
import in.eoninfotech.eontechnician.viewModel.ViewModelMainClient;
import in.eoninfotech.eontechnician.viewModel.ViewModelSubClient;
import in.eoninfotech.eontechnician.viewModel.ViewModelUM;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.DamageList;
import in.eoninfotech.eontechnician.webservice.DamageResponse;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import in.eoninfotech.eontechnician.webservice.UmVehicleDetail;
import in.eoninfotech.eontechnician.webservice.VTSTypeResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static in.eoninfotech.eontechnician.fragments.CallSheetFragment.hasPermissions;

import javax.inject.Inject;


@AndroidEntryPoint
public class NewInstallmentFragmentUpdated extends Fragment implements ClientListener, ReceiveDeviceListener,ProgressRequestBody.UploadCallbacks {

    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    View v;
    private boolean hasInitialized = false;
    private final Handler setupHandler = new Handler(Looper.getMainLooper());
    private Runnable pendingSetup;
    private final int SELECT_PHOTO = 1;
    public static final String IMAGE_DIRECTORY_NAME = "android_file";
    public static final int MEDIA_TYPE_IMAGE = 1;
    private int REQUEST_CAMERA = 0;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    private Handler handler = new Handler();
    int pStatus = 0, x, PERMISSION_ALL = 1, REQUEST_CODE_PERMISSION = 10, fuelVoltInt;
    CheckedTextView text1;
    ImageView checked;
    private boolean hasLoadedClients = false;
    private boolean isSubClientSpinnerReady = false;
    private boolean isLocationSpinnerReady = false;
    private AlertDialog progressDialog;
    private boolean deviceTypesLoaded = false;
    File file;
    Uri uri;
    Bitmap bmp;
    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    NewInstallmentController newInstallmentController;
    ReceiveDeviceControllers receiveDeviceControllers;
    private boolean isMainClientSpinnerReady = false;
    TextInputLayout  con_tilsrNo,tilVoltage, til_vts_miss, til_no_avail_vts, tilsrNo_notAvail, til_new_replace, til_old_replace, til_vts_remove, til_vts_sr_no, til_remove_sr, tilDeviceMiss, told_drsid, tnew_drsid, t_drs_veh_no, t_drs_vts_id, tilvtsno, tilsrNo, tilFaultVts,
            tilFaultSr, tilphnVts, tilphnSr, til_old_vltd_sr_no, til_new_vltd_sr_no, til_id_reinst, til_id_sr, til_sr_reinst, til_new_sr_replace, til_old_sr_replace, sensor_veh, sensor_veh_missing, sensor_veh_remove, sensor_veh_reinstall;
    String s_cust_type="",fuel_voltage = "0", path, drs_type, clientId,mainClientId="SELECT CLIENT", personName, personPhone, clientLocId, s_Vehicle_Name, drsStatus, device_type = "0", s_date, s_time, disttid, s_remove_reason, vts_id, user_id, uusername, version, selected_todate, selected_totime, current_date, fuel_sensor = "N", door_sensor = "N", veh_condition = "W", mgt_set = "N", sim_provider = "0", old_sim_no = "0", new_sim_no = "0", not_available_activity = "0", not_available_reason = "0", is_demo = "N", removal_type, baseImage = "", missing_type = "M", collection_amount, collection_date, collection_type, image, contact_person = "", contact_no = "0", payment_type = "C",
            buttonPressed = "0", buttonPressedActivity = "0", s_reg_no, s_rep_srNo, s_reinst_conf_reg_no, s_device_id, s_drs_id, s_new_drs_id, s_clientname = "SELECT CLIENT", s_remarks, status, s_work_type, s_Time, s_vehicletype="0", s_VehicleTypeInst, s_reason_repla = "0", removalReason = "0", itemsCollected = "0", others = "", s_work_id, s_new_device_id, s_e_device_id = "0", is_drs = "N", drs_dirction = "N", disconnection_reason = "0", ignition_sensor = "N", sim_reason = "0", missing_reason = "0", cut_off = "N", serial_no = "SELECT SR.NO.", confirmVehNo, panic = "N",
            s_old_serial_no="SELECT SR.NO.", s_vts_type = "SELECT VTS TYPE", tilt_sensor = "N", temp_sensor = "N", trans = "N", lid_status = "N", fuel_status = "N", panic_status = "N", sensor_old_veh_no, sen_vehicle_no, radioButtonChecked = "V", removeDeviceType = "D", missDeviceType = "D", reinstDevice = "D",id_dist,server_name,db_name,replace_type="D",silo_sensor="N",device_working_status="W",sensor_working_status="W";
    CheckConnection chk;
    CheckBox check_tel_supprt, magnet_set, magnetset_install;
    EditText et_um_contact_person_name,et_um_contact_person_phone,reinstallVoltage, installVoltage, vltd_sr_no_notAvail, e_reg_no, followUpPersonName, followUpPersonPhone, phSupportPersonName, phSupportPersonPhone, faultPersonName, faultPersonNumber, e_device_id, e_drs_id, e_remarks, old_deviceid, new_deviceid, fault_vts_id, t_install_date, t_install_Time, new_vehicleRegNo, remove_deviceid, remove_reg_no, old_deviceidreplace, new_deviceidReinstall, old_drsid, new_drsid, phsupport_vts_id, fault_reg_no, phSupport_reg_no, regNo, drs_vts_id, drs_veh_no, sim_vts_id, e_old_sim_no, e_new_sim_no, sim_vehicle_no, mDevice_vts_id, mDevice_reg_no, vehNotAvailVtsID, vehNotAvailRegNo,
            remove_sr_no, paymentDate, amount, vts_sr_no, vts_sr_no_reinst, con_in_reg_no, rep_srNo, reinst_conf_reg_no, old_sensor_veh_no, new_sensor_veh_no,con_vltd_sr_no,con_remove_sr_no,con_fault_sr_no,con_phone_sr_no,con_old_deviceidreplace,con_new_deviceid,old_vts_id_replace,new_vts_id_replace,con_old_vts_id_replace,con_new_vts_id_replace, vltd_sr_no, vltd_sr_no_fault, vltd_sr_no_miss, vltd_sr_no_phn, old_vltd_sr_no, new_vltd_sr_no, old_replace_sr_no,con_missing_sr_no,con_reinstall_sr_no,remove_vts_id,con_remove_vts_id,
            new_replace_sr_no, sensor_veh_no, sensor_veh_no_missing, sensor_veh_no_remove,accessory_reg_no,accessory_sr_no;
    MyTextView device_info, itemCollected;
    TextView txt_content_unavailable,plantName, imageName, imageNameFault, imageNameMissing, tv, payValue,text_to_show,text_to_show_remove,text_to_show_missing,
            text_to_show_fault,text_to_show_phone,text_to_show_replace_old,text_to_show_replace_new,text_to_show_reinstall,text_to_show_replace_sen;
    Button update_dataa, imageUpload, imageUploadfault, imageUploadMissing,delete_button,delete_button_e_series,delete_button_remove,delete_button_remove_e_series,delete_button_missing,delete_button_missing_e_series,delete_button_reinstall,
            delete_button_fault_e_series,delete_button_fault,delete_button_phone_e_series,delete_button_phone,delete_button_replace_old_serial,delete_button_replace_new_serial,delete_button_replace_old_id,delete_button_replace_new_id,delete_button_reinstall_ais;
    Calendar calen = Calendar.getInstance();
    int year, month, day, hour, minutes, seconds;
    NonScrollListView lv, lvItem,device_detail_list_receive;
    ArrayList<DeviceTypeOtherAis> deviceTypeOtherAis_arr = new ArrayList<>();
    ArrayList<UmVehicleDetail> getUmVehicle = new ArrayList<>();
    ArrayList<RemovalList> removalList = new ArrayList<>();
    ArrayList<DamageList> damageList = new ArrayList<>();
    ArrayList<ReplaceReasonDetail> arr_replaceReasons = new ArrayList<>();
    ArrayList<FaultList> list_change_values = new ArrayList<>();
    ArrayList<UnderMaintenanceVehicles> list_change_values_um = new ArrayList<>();
    ArrayList<SimDetail> simreplacereason = new ArrayList<>();
    ArrayList<SimOperatorDetail> simOperatorDetails = new ArrayList<>();
    ArrayList<VehNotAvailReasonDetail> vehNotAvailReasonDetails = new ArrayList<>();
    ArrayList<RemovalActivityDetail> removalActivityDetails = new ArrayList<>();
    ArrayList<ItemList> collected_items = new ArrayList<>();
    ArrayList<DisconnectionDetail> supportList = new ArrayList<>();
    ArrayList<WorkTypeDetail> workTypeList = new ArrayList<>();
    ArrayList<NotAvailActivityDetail> notAvailActivityDetails = new ArrayList<>();
    ArrayList<ClientDetails> clientList = new ArrayList<>();
    ArrayList<MainClientList> mainclientList = new ArrayList<>();
    ArrayList<SrNoDeviceList> srnoList = new ArrayList<>();
    ArrayList<SrNoDeviceList> removesrnoList = new ArrayList<>();
    ArrayList<VTSDetail> vtsList = new ArrayList<>();
    ArrayList<PMethodDetail> paymentmethod = new ArrayList<>();
    ArrayList<VehicleTypeDetail> vehicleList = new ArrayList<>();
    ArrayList<ClientLocationDetail> locationList = new ArrayList<>();
    ArrayList<String> clientDetail = new ArrayList<>();
    ArrayList<String> mainClientDetail = new ArrayList<>();
    ArrayList<String> srNoDetails = new ArrayList<>();
    ArrayList<String> removesrNoDetails = new ArrayList<>();
    ArrayList<String> workDetail = new ArrayList<>();
    ArrayList<String> removalDetail = new ArrayList<>();
    ArrayList<String> vehicleDetail = new ArrayList<>();
    ArrayList<String> locationDetail = new ArrayList<>();
    ArrayList<String> value_name = new ArrayList<>();
    ArrayList<String> value_name_um = new ArrayList<>();
    ArrayList<String> value_name_sr = new ArrayList<>();
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
    StringBuilder selectedRegNo;
    ProgressDialog pDialog;
    Spinner device_reinstall, device, vltddeviceNotAvail, vltddevice, vltddeviceReinst, vltddeviceReplace, vltddeviceFault, vltddeviceMiss, vltddevicephn, vltddsimReplace, vltddeviceRemove;
    ProgressBar progressBar, circularProgressbar, mProgress;
    LinearLayout linear_accessory,refuelVoltage, fuelVoltage, drsReInstall, payCollection, followUp, faultDetail, relaydrsType, drsReplacemsg, relaydrsTypeReplace, linearDoor, linearIgnition, drsInstall, linearPayment, linearvts, linearVehicleNotAvail, vehDetail, linearReplacement, linearInstall, linearReInstall, linearRemoval, linearDrs,linear_vts_id_replace,lin_vts_id_remove,
            linearFault, linearPhoneSupport, linearSimRepalace, linearDeviceMissing, linearOthers, oldDeviceType, options, vltdOptions, oldDevicesr_no, reinstText, deviceTypeReplace, replaceSrNo, lay_sensor_veh, linear_device_sr_no,linear_device_sr_no_e_series,linear_device_sr_no_remove,linear_device_sr_no_remove_e_series,linear_device_sr_no_fault_e_series,linear_device_sr_no_fault,
            linear_device_sr_no_phone,linear_device_sr_no_phone_e_series,linear_device_sr_no_replace_old,linear_device_sr_no_replace_new,linear_device_sr_no_missing_e_series,linear_device_sr_no_missing,linear_device_id_replace_old,linear_device_id_replace_new,linear_device_sr_no_reinstall,linear_device_sr_no_reinstall_e_series,linear_device_sr_no_reinstall_ais;
    RelativeLayout relativeCableConnected, relayLocation, relMissing, circularRelative,rel_sr_no;
    MySearchableSpinner client,new_main_clients, vehicleType, workType, location, reason_replace, reason_remove, new_in_vehicleTypeReins, discReason, sim_replace_reason, sim_operator, vehiclenoavailSpinner, notAvailReason, removalType, missingType, payment_method, vehicleTypeFault, vehicleTypeSim, vehicle_list_um, vehicle_list_pm,sr_no,spn_remove_sr_no,spn_old_sr_no_replace,spn_new_sr_no_replace,
            spn_fault_sr_no,spn_phone_sr_no,spn_missing_sr_no,spn_reinstall_sr_no,spn_replace_sensor;
    RadioGroup radioGroup, radiogroupPay,radiodeviceType1, radioGroupReinstall, drsReplace, radiodeviceType, radiodireplace, radiogroup, radioGrouptiltReinst,radiogroupdrsReinst, radioGroupfuelSensorReinst, radioGrouptempReinst, radioGrouptransReinst, radioGroupLidReinst, radioGrouptiltRemove, radioGrouptempRemove, radioGroupPanicRemove, radioGroupfuelRemove,
            radiogroupDoor, radioGroupCutoff, radiogroupCutOffReinst, is_Demo, radiodrsReInstall, radiodrsInstall, radioGroups, radioGrouptransRemove, radioGroupLidRemove,radioGroupdeviceworking,radioGroupsensorworking,radioGrouptiltMissing, radioGrouptempMissing, radioGroupPanicMissing, radioGroupfuelMissing, radioGrouptransMissing, radioGroupLidMissing,
            radioGroupMissing, reinstDeviceType, radioGroupPanic, radioGroupFuel, radioGroupPanicReinst, radioGroupFuelReinst, radioGroupTypeRemove, radioGroupMiss, radioGroupReinstallType,radioGroupdrsMissing,radioGroupdrsRemove,radioGroupSilo,
            radiovltdDevicetype, radioGrouptilt, radioGrouptrans, radioGrouptemp, radioGrouptiltReplace, radioGroupLid, radioGroupfuelSensor, radioGrouptempReplace, radioGroupPanicReplace, radioGrouptransReplace, radioGroupLidReplace;
    RadioButton radionormal, radiotype,acessories, old_Device, new_Device, radionormalrep, l_in, doorNo, cutoffNo, cut_off_no_reinst, radioNone, radioDevice, drs_no_reinst, panicNoReinst, tiltNoReinst, fuelSensorNewNoReinst, tempNoReinst, transNoReinst, lidNoneReinst,
            drsType, drsReeInstall, is_demo_no, damageDevice, voice, nonVoice, radionodrs, radioyesdrs, normal, reverse, tiltNoReplace, tempNoReplace, panicNoReplace, fuelSensorNoReplace, transNoReplace, lidNoneReplace, radioVTS, radioDeviceMiss, tiltMissingNo, tempNoMissing, lidNoneMissing,
            noreinst, radioyesdrsReInstall, replacenormal, replacereverse, nodrsReplace, radioyesdrsReplace, reinst_voice, radioDeviceRemove, tiltRemoveNo, tempNoRemove, panicNoRemove, fuelSensorNoRemove, transNoRemove, lidNoneRemove, panicNoMissing, fuelSensorNoMissing, transNoMissing,
            panicNo, fuelNo, fuelNoReinst, tiltNo, tempNo, tiltReplaceNo, transNo, lidNone, lidTop, lidRear, lidBoth, fuelSensorNewNo,deviceWorking,sensorWorking;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    ViewModelMainClient viewModelMainClient;
    ViewModelSubClient viewModelSubClient;
    ViewModelClientLocation viewModelClientLocation;
    ViewModelUM viewModelVehicleUM;
    DeviceViewModel deviceViewModel;
    ConstraintLayout remove_um_layout;
    ActivityAddUmBinding activityAddUmBinding;
    RemovalFromUmBinding removalFromUmBinding;
    ArrayAdapter<String> umVehicleAdapter;
    InstallationFormHelper formHelper;
    FrameLayout workTypeContainer;
    LayoutInflater layoutInflater;

    private Trace mainTrace;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        mainTrace = FirebasePerformance.getInstance().newTrace("NewInstall_Load");
        mainTrace.start();
        v = inflater.inflate(R.layout.fragment_new_install_updated, container, false);

        long t1 = System.currentTimeMillis();

        v = inflater.inflate(R.layout.fragment_new_install_updated, container, false);
        Log.d("PERF", "inflate: " + (System.currentTimeMillis() - t1) + "ms");

        long t2 = System.currentTimeMillis();
        // all your findViewById calls
        Log.d("PERF", "findViews: " + (System.currentTimeMillis() - t2) + "ms");

        long t3 = System.currentTimeMillis();
        Log.d("PERF", "viewModels: " + (System.currentTimeMillis() - t3) + "ms");

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        formHelper = new InstallationFormHelper(v);

        chk = new CheckConnection(v.getContext());
        uusername = sharedPreferenceManager.getUsername();
        user_id = sharedPreferenceManager.getUserId();
        version  = sharedPreferenceManager.getVersionName();

        initViews();          // move all findViewById here
        // controllers
        initViewModels();     // viewmodels
        observeViewModels();  // observers
        // click listeners
        ShowProgressBar(false);
        setDateAndTime();
        location.setEnabled(false);
        workType.setEnabled(false);

        if (mainTrace != null) {
            mainTrace.stop();
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

        update_dataa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ─── Step 1: Header validation ────────────────────────────────
                InstallationValidator.ValidationResult headerResult =
                        InstallationValidator.validateHeader(
                                mainClientId, s_clientname, location, workType);
                if (!headerResult.isValid) {
                    showValidationError(headerResult);
                    return;
                }

                // ─── Step 2: Work-type specific validation ────────────────────
                InstallationValidator.ValidationResult result =
                        InstallationValidator.ValidationResult.ok();

                if (s_work_id.equalsIgnoreCase("2")) {

                    // collect fields
                    s_drs_id = e_drs_id.getText().toString();
                    if (e_reg_no.getVisibility() == View.VISIBLE) {
                        s_reg_no = e_reg_no.getText().toString();
                    } else {
                        s_reg_no = accessory_reg_no.getText().toString();
                    }
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_remarks = e_remarks.getText().toString();
                    confirmVehNo = con_in_reg_no.getText().toString();
                    mgt_set = magnetset_install.isChecked() ? "Y" : "N";

                    int radioDrs = radiodrsInstall.getCheckedRadioButtonId();
                    drsType = v.findViewById(radioDrs);
                    String drsTypes = drsType.getText().toString();
                    if (drsTypes.equalsIgnoreCase("Yes") && linearDrs.getVisibility() == View.VISIBLE) {
                        is_drs = "Y";
                    } else if (drsTypes.equalsIgnoreCase("No") && linearDrs.getVisibility() == View.VISIBLE) {
                        is_drs = "P";
                    } else {
                        is_drs = "N";
                    }

                    int isDemo = is_Demo.getCheckedRadioButtonId();
                    is_demo_no = v.findViewById(isDemo);
                    is_demo = is_demo_no.getText().toString().equalsIgnoreCase("No") ? "N" : "Y";

                    int selectedIgnition = radiogroup.getCheckedRadioButtonId();
                    l_in = v.findViewById(selectedIgnition);
                    ignition_sensor = l_in.getText().toString().equalsIgnoreCase("No") ? "N" : "Y";

                    int selectedTypes = radiogroupDoor.getCheckedRadioButtonId();
                    doorNo = v.findViewById(selectedTypes);
                    door_sensor = doorNo.getText().toString().equalsIgnoreCase("No") ? "N" : "Y";

                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    radionormal = v.findViewById(selectedId);
                    String drsDir = radionormal.getText().toString();
                    if (drsDir.equalsIgnoreCase("Normal") && linearDrs.getVisibility() == View.VISIBLE) {
                        reverse.setChecked(false); drs_dirction = "N";
                    } else if (drsDir.equalsIgnoreCase("Reverse") && linearDrs.getVisibility() == View.VISIBLE) {
                        normal.setChecked(false); drs_dirction = "R";
                    }

                    int cutoffType = radioGroupCutoff.getCheckedRadioButtonId();
                    cutoffNo = v.findViewById(cutoffType);
                    cut_off = cutoffNo.getText().toString().equalsIgnoreCase("No") ? "N" : "Y";

                    int panicType = radioGroupPanic.getCheckedRadioButtonId();
                    panicNo = v.findViewById(panicType);
                    panic = panicNo.getText().toString().equalsIgnoreCase("No") ? "N" : "Y";

                    int fuelType = radioGroupFuel.getCheckedRadioButtonId();
                    fuelNo = v.findViewById(fuelType);
                    fuel_sensor = fuelNo.getText().toString().equalsIgnoreCase("No") ? "N" : "Y";

                    s_new_device_id = e_device_id.getVisibility() == View.VISIBLE
                            ? e_device_id.getText().toString() : "0";

                    if (linear_device_sr_no_e_series.getVisibility() == View.VISIBLE)
                        serial_no = vts_sr_no.getText().toString();
                    if (linear_device_sr_no.getVisibility() == View.VISIBLE)
                        serial_no = vltd_sr_no.getText().toString();
                    if (linear_accessory.getVisibility() == View.VISIBLE)
                        serial_no = accessory_sr_no.getText().toString();

                    if (fuelVoltage.getVisibility() == View.VISIBLE &&
                            !installVoltage.getText().toString().equals("")) {
                        fuel_voltage = installVoltage.getText().toString();
                        fuelVoltInt = fuel_voltage.contains(".")
                                ? (int) Double.parseDouble(fuel_voltage)
                                : Integer.parseInt(fuel_voltage);
                    } else if (fuelVoltage.getVisibility() == View.VISIBLE &&
                            installVoltage.getText().toString().equals("")) {
                        fuel_voltage = ""; fuelVoltInt = 0;
                    } else {
                        fuel_voltage = "0"; fuelVoltInt = 0;
                    }

                    result = InstallationValidator.validateInstallation(
                            s_vts_type, serial_no, s_reg_no, confirmVehNo, s_work_type,
                            fuel_voltage, fuelVoltInt, s_drs_id, s_remarks,
                            vts_sr_no, vltd_sr_no, linear_accessory, con_tilsrNo,
                            e_reg_no, con_in_reg_no, fuelVoltage, linearDrs,
                            vehicleType, con_vltd_sr_no, accessory_sr_no,
                            installVoltage, e_drs_id);

                } else if (s_work_id.equalsIgnoreCase("4")) {

                    // collect fields
                    s_reg_no = new_vehicleRegNo.getText().toString();
                    s_remarks = e_remarks.getText().toString();
                    s_drs_id = e_drs_id.getText().toString();
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_reinst_conf_reg_no = reinst_conf_reg_no.getText().toString();
                    s_new_device_id = new_deviceidReinstall.getVisibility() == View.VISIBLE
                            ? new_deviceidReinstall.getText().toString() : "0";
                    mgt_set = magnetset_install.isChecked() ? "Y" : "N";

                    int radioDrsReinst = radiodrsReInstall.getCheckedRadioButtonId();
                    drsReeInstall = v.findViewById(radioDrsReinst);
                    String drsTypesReinst = drsReeInstall.getText().toString();
                    if (drsTypesReinst.equalsIgnoreCase("Yes") && drsReInstall.getVisibility() == View.VISIBLE) {
                        noreinst.setChecked(false); is_drs = "Y";
                    } else if (drsTypesReinst.equalsIgnoreCase("No") && drsReInstall.getVisibility() == View.VISIBLE) {
                        radioyesdrsReInstall.setChecked(false); is_drs = "P";
                    } else {
                        is_drs = "N";
                    }

                    s_VehicleTypeInst = new_in_vehicleTypeReins.getSelectedItem().toString();

                    if (til_id_reinst.getVisibility() == View.VISIBLE) {
                        s_e_device_id = old_deviceid.getText().toString();
                        s_new_device_id = "0";
                    } else {
                        s_e_device_id = "0";
                        s_new_device_id = "0";
                    }

                    if (refuelVoltage.getVisibility() == View.VISIBLE &&
                            !reinstallVoltage.getText().toString().equals("")) {
                        fuel_voltage = reinstallVoltage.getText().toString();
                        fuelVoltInt = fuel_voltage.contains(".")
                                ? (int) Double.parseDouble(fuel_voltage)
                                : Integer.parseInt(fuel_voltage);
                    } else if (refuelVoltage.getVisibility() == View.VISIBLE &&
                            reinstallVoltage.getText().toString().equals("")) {
                        fuel_voltage = ""; fuelVoltInt = 0;
                    } else {
                        fuel_voltage = "0"; fuelVoltInt = 0;
                    }

                    if (linear_device_sr_no_reinstall.getVisibility() == View.VISIBLE) {
                        s_old_serial_no = vts_sr_no_reinst.getText().toString(); serial_no = "0";
                    }
                    if (linear_device_sr_no_reinstall_ais.getVisibility() == View.VISIBLE) {
                        s_old_serial_no = old_vltd_sr_no.getText().toString(); serial_no = "0";
                    }

                    result = InstallationValidator.validateReInstallation(
                            s_vts_type, s_old_serial_no, s_e_device_id, serial_no,
                            s_reg_no, s_reinst_conf_reg_no, s_VehicleTypeInst,
                            s_drs_id, fuel_voltage, fuelVoltInt, s_remarks, reinstDevice,
                            til_old_vltd_sr_no, til_id_reinst, til_sr_reinst,
                            refuelVoltage, linearDrs, lay_sensor_veh,
                            new_deviceidReinstall, til_new_vltd_sr_no,
                            vltddeviceReinst, old_vltd_sr_no, old_deviceid,
                            vts_sr_no_reinst, con_reinstall_sr_no, new_vehicleRegNo,
                            reinst_conf_reg_no, e_drs_id, reinstallVoltage,
                            old_sensor_veh_no, new_sensor_veh_no, new_deviceidReinstall);

                } else if (s_work_id.equalsIgnoreCase("3")) {

                    // collect fields
                    s_reg_no = regNo.getText().toString();
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_remarks = e_remarks.getText().toString();
                    mgt_set = magnet_set.isChecked() ? "Y" : "N";

                    int selectedIgnition3 = radiogroup.getCheckedRadioButtonId();
                    l_in = v.findViewById(selectedIgnition3);
                    ignition_sensor = l_in.getText().toString().equalsIgnoreCase("No") ? "N" : "Y";

                    int selectedTypes3 = radiogroupDoor.getCheckedRadioButtonId();
                    doorNo = v.findViewById(selectedTypes3);
                    door_sensor = doorNo.getText().toString().equalsIgnoreCase("No") ? "N" : "Y";

                    int selectedId3 = radiodireplace.getCheckedRadioButtonId();
                    radionormalrep = v.findViewById(selectedId3);
                    linearDrs.setVisibility(View.GONE);
                    String drs3 = radionormalrep.getText().toString();
                    if (drs3.equalsIgnoreCase("Normal") && relaydrsTypeReplace.getVisibility() == View.VISIBLE) {
                        replacereverse.setChecked(false); drs_dirction = "N";
                    } else if (drs3.equalsIgnoreCase("Reverse") && relaydrsTypeReplace.getVisibility() == View.VISIBLE) {
                        replacenormal.setChecked(false); drs_dirction = "R";
                    }

                    int selectedType3 = drsReplace.getCheckedRadioButtonId();
                    radiotype = v.findViewById(selectedType3);
                    String type3 = radiotype.getText().toString();
                    if (type3.equalsIgnoreCase("YES")) {
                        nodrsReplace.setChecked(false); is_drs = "Y";
                    } else {
                        radioyesdrsReplace.setChecked(false); is_drs = "N";
                    }

                    if (linear_vts_id_replace.getVisibility() == View.VISIBLE) {
                        s_e_device_id = old_vts_id_replace.getText().toString();
                        s_new_device_id = new_vts_id_replace.getText().toString();
                    }
                    if (linear_device_id_replace_old.getVisibility() == View.VISIBLE &&
                            vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("E124")) {
                        s_old_serial_no = old_deviceidreplace.getText().toString();
                    }
                    if (linear_device_id_replace_new.getVisibility() == View.VISIBLE &&
                            vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("E124")) {
                        serial_no = new_deviceid.getText().toString();
                    }
                    if (linear_device_sr_no_replace_old.getVisibility() == View.VISIBLE &&
                            vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("AIS 140")) {
                        s_old_serial_no = old_replace_sr_no.getText().toString();
                    }
                    if (linear_device_sr_no_replace_new.getVisibility() == View.VISIBLE &&
                            vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("AIS 140")) {
                        serial_no = new_replace_sr_no.getText().toString();
                    }

                    // branch selection
                    if (linearvts.getVisibility() == View.VISIBLE &&
                            new_drsid.getVisibility() == View.GONE &&
                            !radioButtonChecked.equalsIgnoreCase("N")) {

                        result = InstallationValidator.validateReplacementVts(
                                s_vts_type, s_old_serial_no, serial_no, s_e_device_id, s_new_device_id,
                                s_reg_no, radioButtonChecked, vltddeviceReplace, reason_replace,
                                linear_device_id_replace_old, linear_device_id_replace_new,
                                linear_device_sr_no_replace_old, linear_device_sr_no_replace_new,
                                linear_vts_id_replace, til_old_sr_replace,
                                old_deviceidreplace, new_deviceid,
                                con_old_deviceidreplace, con_new_deviceid,
                                old_replace_sr_no, new_replace_sr_no,
                                old_vts_id_replace, con_old_vts_id_replace,
                                new_vts_id_replace, con_new_vts_id_replace, regNo);

                    } else if (old_deviceidreplace.getVisibility() == View.VISIBLE &&
                            drs_veh_no.getVisibility() == View.GONE &&
                            !radioButtonChecked.equalsIgnoreCase("N")) {

                        result = InstallationValidator.validateReplacementDrs(
                                s_old_serial_no, serial_no, s_e_device_id, s_new_device_id,
                                s_reg_no, radioButtonChecked, vltddeviceReplace, reason_replace,
                                linear_device_id_replace_old, linear_device_id_replace_new,
                                linear_device_sr_no_replace_old, linear_device_sr_no_replace_new,
                                linear_vts_id_replace, til_old_sr_replace,
                                old_deviceidreplace, new_deviceid,
                                con_old_deviceidreplace, con_new_deviceid,
                                old_replace_sr_no, new_replace_sr_no,
                                old_vts_id_replace, con_old_vts_id_replace,
                                new_vts_id_replace, con_new_vts_id_replace,
                                regNo, old_drsid, new_drsid);

                    } else if (drs_veh_no.getVisibility() == View.VISIBLE &&
                            drs_vts_id.getVisibility() == View.VISIBLE &&
                            !radioButtonChecked.equalsIgnoreCase("N")) {

                        result = InstallationValidator.validateReplacementDrsSwap(
                                drs_vts_id, drs_veh_no, old_drsid, new_drsid);

                    } else if (radioButtonChecked.equalsIgnoreCase("N")) {

                        result = InstallationValidator.validateReplacementSensor(
                                s_vts_type, radioButtonChecked, sensor_veh,
                                sensor_veh_no, sensor_veh_no);
                    }

                } else if (s_work_id.equalsIgnoreCase("5")) {

                    // collect fields
                    if (linear_device_sr_no_remove_e_series.getVisibility() == View.VISIBLE)
                        s_old_serial_no = remove_deviceid.getText().toString();
                    if (linear_device_sr_no_remove.getVisibility() == View.VISIBLE)
                        s_old_serial_no = remove_sr_no.getText().toString();
                    s_e_device_id = lin_vts_id_remove.getVisibility() == View.VISIBLE
                            ? remove_vts_id.getText().toString() : "0";
                    s_reg_no = remove_reg_no.getText().toString();
                    if (s_reg_no.equals("")) s_reg_no = "0";
                    s_remove_reason = reason_remove.getSelectedItem().toString();
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_remarks = e_remarks.getText().toString();
                    sensor_old_veh_no = sensor_veh_no_remove.getText().toString();
                    SparseBooleanArray checkedRemoval = lvItem.getCheckedItemPositions();
                    itemsCollected = "0";
                    for (int i = 0; i < checkedRemoval.size(); i++) {
                        int key = checkedRemoval.keyAt(i);
                        itemsCollected = itemsCollected + (collected_items.get(key).getId()) + ":";
                    }

                    if (removal_type.equals("1") || removal_type.equals("2") ||
                            removal_type.equals("4") ||
                            (removal_type.equals("5") && !removeDeviceType.equalsIgnoreCase("S"))) {

                        result = InstallationValidator.validateRemoval(
                                s_old_serial_no, s_e_device_id, s_reg_no, s_remove_reason,
                                itemsCollected, removal_type, removeDeviceType,
                                vltddeviceRemove, til_remove_sr, lin_vts_id_remove,
                                sensor_veh_remove, remove_sr_no, con_remove_sr_no,
                                remove_vts_id, con_remove_vts_id, remove_reg_no,
                                sensor_veh_no_remove);

                    } else if (removal_type.equals("3")) {

                        result = InstallationValidator.validateRemoval(
                                s_old_serial_no, s_e_device_id, s_reg_no, s_remove_reason,
                                itemsCollected, removal_type, removeDeviceType,
                                vltddeviceRemove, til_remove_sr, lin_vts_id_remove,
                                sensor_veh_remove, remove_sr_no, con_remove_sr_no,
                                remove_vts_id, con_remove_vts_id, remove_reg_no,
                                sensor_veh_no_remove);

                    } else if (removal_type.equalsIgnoreCase("0")) {
                        result = InstallationValidator.ValidationResult.toast(
                                "Select Action Type");
                    }

                    if (s_vts_type.equalsIgnoreCase("SELECT VTS TYPE")) {
                        result = InstallationValidator.ValidationResult.toast(
                                "Please Select Device Type");
                    }

                } else if (s_work_id.equalsIgnoreCase("1")) {

                    // collect fields
                    if (linear_device_sr_no_fault_e_series.getVisibility() == View.VISIBLE) {
                        s_old_serial_no = fault_vts_id.getText().toString(); s_e_device_id = "0";
                    }
                    if (linear_device_sr_no_fault.getVisibility() == View.VISIBLE) {
                        s_old_serial_no = vltd_sr_no_fault.getText().toString(); s_e_device_id = "0";
                    }
                    s_reg_no = fault_reg_no.getText().toString();
                    contact_person = faultPersonName.getText().toString();
                    contact_no = faultDetail.getVisibility() == View.VISIBLE
                            ? faultPersonNumber.getText().toString() : "0";
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_remarks = e_remarks.getText().toString();

                    SparseBooleanArray checkedFault = lv.getCheckedItemPositions();
                    others = "";
                    for (int i = 0; i < checkedFault.size(); i++) {
                        int key = checkedFault.keyAt(i);
                        others = others + (list_change_values.get(key).getId()) + ":";
                    }
                    veh_condition = others.contains("6") ? "U" : "W";

                    result = InstallationValidator.validateFault(
                            s_vts_type, s_old_serial_no, s_reg_no, others,
                            vltddeviceFault, vehicleTypeFault,
                            linear_device_sr_no_fault, faultDetail,
                            vltd_sr_no_fault, con_fault_sr_no,
                            fault_reg_no, faultPersonName, faultPersonNumber);

                } else if (s_work_id.equalsIgnoreCase("6")) {

                    // collect fields
                    if (linear_device_sr_no_phone_e_series.getVisibility() == View.VISIBLE) {
                        s_old_serial_no = phsupport_vts_id.getText().toString(); s_e_device_id = "0";
                    }
                    if (linear_device_sr_no_phone.getVisibility() == View.VISIBLE) {
                        s_old_serial_no = vltd_sr_no_phn.getText().toString(); s_e_device_id = "0";
                    }
                    s_reg_no = phSupport_reg_no.getText().toString();
                    personName = phSupportPersonName.getText().toString();
                    personPhone = phSupportPersonPhone.getText().toString();
                    contact_person = personName;
                    contact_no = personPhone;
                    disconnection_reason = String.valueOf(discReason.getSelectedItemPosition());
                    veh_condition = disconnection_reason.equals("1") ? "U" : "W";
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_remarks = e_remarks.getText().toString();

                    result = InstallationValidator.validatePhoneSupport(
                            s_vts_type, s_old_serial_no, s_reg_no,
                            vltddevicephn, discReason,
                            linear_device_sr_no_phone, vltd_sr_no_phn,
                            vltd_sr_no_phn, con_phone_sr_no,
                            phSupport_reg_no, phSupportPersonName, phSupportPersonPhone);

                } else if (s_work_id.equalsIgnoreCase("7")) {

                    // collect fields
                    s_e_device_id = sim_vts_id.getText().toString();
                    new_sim_no = e_new_sim_no.getText().toString();
                    old_sim_no = e_old_sim_no.getText().toString();
                    if (old_sim_no.equals("0")) old_sim_no = "0000000000000";
                    s_reg_no = sim_vehicle_no.getText().toString();
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_remarks = e_remarks.getText().toString();

                    result = InstallationValidator.validateSimReplacement(
                            s_e_device_id, s_reg_no, old_sim_no, new_sim_no,
                            sim_replace_reason, sim_operator,
                            sim_vts_id, sim_vehicle_no,
                            e_old_sim_no, e_new_sim_no);

                } else if (s_work_id.equalsIgnoreCase("8")) {

                    // collect fields
                    if (linear_device_sr_no_missing_e_series.getVisibility() == View.VISIBLE)
                        s_old_serial_no = mDevice_vts_id.getText().toString();
                    if (linear_device_sr_no_missing.getVisibility() == View.VISIBLE)
                        s_old_serial_no = vltd_sr_no_miss.getText().toString();
                    s_reg_no = mDevice_reg_no.getText().toString();
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_remarks = e_remarks.getText().toString();
                    sensor_old_veh_no = sensor_veh_no_missing.getText().toString();

                    result = InstallationValidator.validateMissingDevice(
                            s_vts_type, s_old_serial_no, s_reg_no, missDeviceType,
                            vltddeviceMiss, missingType,
                            relMissing, sensor_veh_missing,
                            vltd_sr_no_miss, con_missing_sr_no,
                            mDevice_reg_no, sensor_veh_no_missing);

                } else if (s_work_id.equalsIgnoreCase("9")) {

                    // collect fields
                    s_old_serial_no = vehNotAvailVtsID.getText().toString();
                    s_reg_no = vehNotAvailRegNo.getText().toString();
                    if (s_reg_no.equals("")) s_reg_no = "0";
                    s_e_device_id = "0";
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_remarks = e_remarks.getText().toString();

                    result = InstallationValidator.validateVehicleNotAvail(
                            s_e_device_id, s_reg_no, not_available_reason, s_remarks,
                            vehiclenoavailSpinner, notAvailReason,
                            vehDetail, vehNotAvailRegNo,
                            vehNotAvailVtsID, vehNotAvailRegNo);

                } else if (s_work_id.equalsIgnoreCase("10")) {

                    if (followUp.getVisibility() == View.GONE) {
                        // collect payment fields
                        String abc = amount.getText().toString();
                        if (!abc.equalsIgnoreCase("")) {
                            x = Integer.parseInt(abc);
                            collection_amount = abc + ".00";
                        }
                        collection_date = paymentDate.getText().toString();
                        s_date = t_install_date.getText().toString();
                        s_Time = t_install_Time.getText().toString();
                        s_remarks = e_remarks.getText().toString();

                        result = InstallationValidator.validatePaymentCollection(
                                abc, x, payment_method);

                    } else {
                        // collect follow up fields
                        s_date = t_install_date.getText().toString();
                        s_Time = t_install_Time.getText().toString();
                        s_remarks = e_remarks.getText().toString();

                        result = InstallationValidator.validatePaymentFollowUp(
                                followUpPersonName, followUpPersonPhone);

                        if (result.isValid) {
                            personName = followUpPersonName.getText().toString();
                            personPhone = followUpPersonPhone.getText().toString();
                            contact_person = personName;
                            contact_no = personPhone;
                        }
                    }

                } else if (s_work_id.equalsIgnoreCase("11")) {

                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_remarks = e_remarks.getText().toString();
                    result = InstallationValidator.validateOtherWork(e_remarks);

                } else if (s_work_id.equalsIgnoreCase("12") ||
                        s_work_id.equalsIgnoreCase("13")) {

                    result = InstallationValidator.validateUM(
                            et_um_contact_person_name, et_um_contact_person_phone);
                }

                // ─── Step 3: Show error or proceed ────────────────────────────
                if (!result.isValid) {
                    showValidationError(result);
                    return;
                }

                setDefaultsAndSubmit();
            }
        });

        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
                } else {
                    buttonPressedActivity = "1";
                    ShowImagePopup(getActivity());
                }
            }
        });
        imageUploadfault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
                } else {
                    buttonPressedActivity = "2";
                    ShowImagePopup(getActivity());
                }
            }
        });
        imageUploadMissing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
                } else {
                    buttonPressedActivity = "3";
                    ShowImagePopup(getActivity());
                }
            }
        });

        // ── Main Client Spinner ───────────────────────────────────────────
        new_main_clients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isMainClientSpinnerReady) {
                    isMainClientSpinnerReady = true;
                    return;
                }
                if (position == 0) return;
                int index = position - 1;
                mainClientId = String.valueOf(mainclientList.get(index).getClient_Id());
                progressDialog.show();
                isSubClientSpinnerReady = false; // ← reset sub client guard
                viewModelSubClient.fetchSubClients(mainClientId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // ── Sub Client Spinner ────────────────────────────────────────────
        client.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (!isSubClientSpinnerReady) {
                    isSubClientSpinnerReady = true;
                    return;
                }
                if (position == 0) return;
                int index = position - 1;
                clientId = String.valueOf(clientList.get(index).getClient_Id());
                s_clientname = clientList.get(index).getClient_Name();
                id_dist = clientList.get(index).getId_dist();
                server_name = clientList.get(index).getServer_name();
                db_name = clientList.get(index).getDb_name();
                drsStatus = String.valueOf(clientList.get(index).getDrs_status());
                clearData();
                if (s_clientname.equalsIgnoreCase("OTHERS")) {
                    clientLocId = "0";
                    location.setEnabled(false);
                    workType.setEnabled(true);
                } else {
                    isLocationSpinnerReady = false; // ← reset location guard
                    viewModelClientLocation.fetchClientLocation(id_dist, server_name, db_name);
                    location.setEnabled(true);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // ── Location Spinner ──────────────────────────────────────────────
        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (!isLocationSpinnerReady) {
                    isLocationSpinnerReady = true;
                    return;
                }
                if (position == 0) return;
                int index = position - 1;
                clientLocId = String.valueOf(locationList.get(index).getLoc_Id());
                workType.setEnabled(true);
                getSerialNo();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // ── Work Type Spinner ─────────────────────────────────────────────
        workType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) return;
                int index = position - 1;
                s_work_type = workType.getSelectedItem().toString();
                s_work_id = String.valueOf(workTypeList.get(index).getWork_Id());
                handleWorkTypeSelected(s_work_id);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // ── Device Type Spinner (Installation) ────────────────────────────
        device.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return;
                int index = position - 1;
                device_type = deviceTypeOtherAis_arr.get(index).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // ── Device Reinstall Spinner ──────────────────────────────────────
        device_reinstall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return;
                int index = position - 1;
                device_type = deviceTypeOtherAis_arr.get(index).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // ── Removal Type Spinner ──────────────────────────────────────────
        removalType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) return;
                int index = position - 1;
                removal_type = String.valueOf(removalActivityDetails.get(index).getA_id());
                remove_reg_no.setText("");
                remove_deviceid.setText("");
                e_remarks.setText("");
                itemCollected.setText(removal_type.equals("1") ? "Items Handed Over" : "Items Collected");
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // ── Payment Method Spinner ────────────────────────────────────────
        payment_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) return;
                int index = position - 1;
                collection_type = String.valueOf(paymentmethod.get(index).getId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // ── Disconnection Reason Spinner ──────────────────────────────────
        discReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) return;
                int index = position - 1;
                disconnection_reason = String.valueOf(supportList.get(index).getId());
                relativeCableConnected.setVisibility(View.GONE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // ── Reason Replace Spinner ────────────────────────────────────────
        reason_replace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return;
                int index = position - 1;
                s_reason_repla = String.valueOf(arr_replaceReasons.get(index).getReplace_Id());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // ── Reason Remove Spinner ─────────────────────────────────────────
        reason_remove.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) return;
                int index = position - 1;
                removalReason = String.valueOf(removalList.get(index).getRemoval_Id());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // ── Missing Type Spinner ──────────────────────────────────────────
        missingType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) return;
                int index = position - 1;
                missing_reason = String.valueOf(damageList.get(index).getDamage_Id());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // ── Sim Replace Reason Spinner ────────────────────────────────────
        sim_replace_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) return;
                int index = position - 1;
                sim_reason = simreplacereason.get(index).getS_id();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // ── Sim Operator Spinner ──────────────────────────────────────────
        sim_operator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) return;
                int index = position - 1;
                sim_provider = simOperatorDetails.get(index).getSp_id();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // ── Vehicle Type Spinner (Installation) ───────────────────────────
        vehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) return;
                int index = position - 1;
                s_vehicletype = String.valueOf(vehicleList.get(index).getVehicle_Id());
                s_Vehicle_Name = vehicleList.get(index).getVehicle_Name();
                drs_type = vehicleList.get(index).getDrs_type();
                if (drs_type.equalsIgnoreCase("Y")) {
                    drsInstall.setVisibility(View.VISIBLE);
                } else {
                    drsInstall.setVisibility(View.GONE);
                    linearDrs.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // ── Vehicle Type Fault Spinner ────────────────────────────────────
        vehicleTypeFault.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) return;
                int index = position - 1;
                s_vehicletype = String.valueOf(vehicleList.get(index).getVehicle_Id());
                s_Vehicle_Name = vehicleList.get(index).getVehicle_Name();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // ── Vehicle Type Sim Spinner ──────────────────────────────────────
        vehicleTypeSim.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) return;
                int index = position - 1;
                s_vehicletype = String.valueOf(vehicleList.get(index).getVehicle_Id());
                s_Vehicle_Name = vehicleList.get(index).getVehicle_Name();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // ── Vehicle Type ReInstall Spinner ────────────────────────────────
        new_in_vehicleTypeReins.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) return;
                int index = position - 1;
                s_vehicletype = String.valueOf(vehicleList.get(index).getVehicle_Id());
                s_Vehicle_Name = vehicleList.get(index).getVehicle_Name();
                drs_type = vehicleList.get(index).getDrs_type();
                if (drs_type.equalsIgnoreCase("Y")) {
                    drsReInstall.setVisibility(View.VISIBLE);
                } else {
                    drsReInstall.setVisibility(View.GONE);
                    linearDrs.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // ── Vehicle Not Avail Spinner ─────────────────────────────────────
        vehiclenoavailSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) return;
                int index = position - 1;
                not_available_activity = String.valueOf(notAvailActivityDetails.get(index).getId());
                if (not_available_activity.equals("1") || not_available_activity.equals("2")) {
                    vehDetail.setVisibility(View.GONE);
                    s_vehicletype = "0";
                    device_type = "0";
                } else {
                    vehDetail.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // ── Not Avail Reason Spinner ──────────────────────────────────────
        notAvailReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) return;
                int index = position - 1;
                not_available_reason = vehNotAvailReasonDetails.get(index).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void initViews() {
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
        remove_um_layout = v.findViewById(R.id.remove_um_layout);
        fuelSensorNoReplace = v.findViewById(R.id.fuelSensorNoReplace);
        transNoReplace = v.findViewById(R.id.transNoReplace);
        lidNoneReplace = v.findViewById(R.id.lidNoneReplace);
        noreinst = v.findViewById(R.id.noreinst);
        sensor_veh_no = v.findViewById(R.id.sensor_veh_no);
        sensor_veh_no_missing = v.findViewById(R.id.sensor_veh_no_missing);
        sensor_veh_no_remove = v.findViewById(R.id.sensor_veh_no_remove);
        txt_content_unavailable = v.findViewById(R.id.txt_content_unavailable);
        sensor_veh = v.findViewById(R.id.sensor_veh);
        sensor_veh_missing = v.findViewById(R.id.sensor_veh_missing);
        sensor_veh_remove = v.findViewById(R.id.sensor_veh_remove);
        accessory_reg_no = v.findViewById(R.id.accessory_reg_no);
        accessory_sr_no = v.findViewById(R.id.accessory_sr_no);
        replacenormal = v.findViewById(R.id.replacenormal);
        replacereverse = v.findViewById(R.id.replacereverse);
        til_vts_sr_no = v.findViewById(R.id.til_vts_sr_no);
        radioyesdrsReInstall = v.findViewById(R.id.radioyesdrsReInstall);
        radioGrouptiltReplace = v.findViewById(R.id.radioGrouptiltReplace);
        til_no_avail_vts = v.findViewById(R.id.til_no_avail_vts);
        radioDeviceRemove = v.findViewById(R.id.radioDeviceRemove);
        rel_sr_no = v.findViewById(R.id.rel_sr_no);
        linear_accessory = v.findViewById(R.id.linear_accessory);
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
        deviceWorking = v.findViewById(R.id.deviceWorking);
        sensorWorking = v.findViewById(R.id.sensorWorking);
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
        et_um_contact_person_name = v.findViewById(R.id.um_contact_person_name);
        et_um_contact_person_phone = v.findViewById(R.id.um_contact_person_phone);
        device = v.findViewById(R.id.device);
        device_reinstall = v.findViewById(R.id.device_reinstall);
        vltddevicephn = v.findViewById(R.id.vltddevicephn);
        til_vts_remove = v.findViewById(R.id.til_vts_remove);
        til_remove_sr = v.findViewById(R.id.til_remove_sr);
        tilVoltage = v.findViewById(R.id.tilVoltage);
        con_tilsrNo = v.findViewById(R.id.con_tilsrNo);
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
        device_detail_list_receive = v.findViewById(R.id.device_detail_list_receive);
        payValue = v.findViewById(R.id.payValue);
        plantName = v.findViewById(R.id.plantName);
        vehDetail = v.findViewById(R.id.vehDetail);
        radionodrs = v.findViewById(R.id.radionodrs);
        vltd_sr_no = v.findViewById(R.id.vltd_sr_no);
        options = v.findViewById(R.id.options);
        vltdOptions = v.findViewById(R.id.vltdOptions);
        vehicleTypeSim = v.findViewById(R.id.vehicleTypeSim);
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
        radiodeviceType1 = v.findViewById(R.id.radiodeviceType1);
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
        new_main_clients = v.findViewById(R.id.new_main_clients);
        e_reg_no = v.findViewById(R.id.new_in_reg_no);
        con_in_reg_no = v.findViewById(R.id.con_in_reg_no);
        deviceTypeReplace = v.findViewById(R.id.deviceTypeReplace);
        reinst_conf_reg_no = v.findViewById(R.id.reinst_conf_reg_no);
        vltd_sr_no_phn = v.findViewById(R.id.vltd_sr_no_phn);
        old_sensor_veh_no = v.findViewById(R.id.old_sensor_veh_no);
        new_sensor_veh_no = v.findViewById(R.id.new_sensor_veh_no);
        e_drs_id = v.findViewById(R.id.new_in_drs_id);
        linearPayment = v.findViewById(R.id.linearPayment);
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
        radioGroupSilo = v.findViewById(R.id.radioGroupSilo);
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
        radioGroupdeviceworking = v.findViewById(R.id.radioGroupdeviceworking);
        radioGroupsensorworking = v.findViewById(R.id.radioGroupsensorworking);
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
        text_to_show_replace_sen = v.findViewById(R.id.text_to_show_replace_sen);
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
        workTypeContainer = v.findViewById(R.id.workTypeContainer);
        layoutInflater = LayoutInflater.from(getContext());
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
    }

    private void postSetup(Runnable r) {
        if (pendingSetup != null) setupHandler.removeCallbacks(pendingSetup);
        pendingSetup = r;
        setupHandler.postDelayed(pendingSetup, 300);
    }

    private void loadWorkTypeLayout(String workType){

        workTypeContainer.removeAllViews();

        View view = null;

        switch (workType){

            case "Installation":
                device_info.setText("Installation Details");
                view = layoutInflater.inflate(R.layout.install_layout, workTypeContainer, false);
                break;

            case "Reinstallation":
                device_info.setText("Reinstallation Details");
                view = layoutInflater.inflate(R.layout.reinstall_layout, workTypeContainer, false);
                break;

            case "Replacement":
                device_info.setText("Replacement Details");
                view = layoutInflater.inflate(R.layout.replace_layout, workTypeContainer, false);
                break;

            case "Removal":
                device_info.setText("Removal Details");
                view = layoutInflater.inflate(R.layout.remove_layout, workTypeContainer, false);
                break;

            case "Fault":
                device_info.setText("Fault Details");
                view = layoutInflater.inflate(R.layout.fault_layout, workTypeContainer, false);
                break;

            case "Phone Support":
                view = layoutInflater.inflate(R.layout.phone_support_layout, workTypeContainer, false);
                break;

            case "SIM Replacement":
                view = layoutInflater.inflate(R.layout.simreplacement, workTypeContainer, false);
                break;

            case "Device Missing":
                view = layoutInflater.inflate(R.layout.devicemissing, workTypeContainer, false);
                break;

            case "Vehicle Not Available":
                view = layoutInflater.inflate(R.layout.vehiclenotavailable, workTypeContainer, false);
                break;

            case "Payment Collection":
                view = layoutInflater.inflate(R.layout.paymentcollection, workTypeContainer, false);
                break;

            case "Other Activity":
                view = layoutInflater.inflate(R.layout.other_activity, workTypeContainer, false);
                break;

            case "Removal From UM":
                view = layoutInflater.inflate(R.layout.removal_from_um, workTypeContainer, false);
                break;

            case "Preventive Maintenance":
                view = layoutInflater.inflate(R.layout.preventive_maintenance, workTypeContainer, false);
                break;
        }

        if(view != null){
            workTypeContainer.addView(view);
        }
    }

    private void setDefaultsAndSubmit() {
        // These fields are always reset regardless of work type
        // Only override what is NOT already set by the collect block above
        if (s_old_serial_no == null)    s_old_serial_no = "";
        if (serial_no == null)          serial_no = "";
        if (s_new_drs_id == null)       s_new_drs_id = "0";
        if (s_drs_id == null)           s_drs_id = "0";
        if (s_new_device_id == null)    s_new_device_id = "0";
        if (s_e_device_id == null)      s_e_device_id = "0";
        if (others == null)             others = "";
        if (itemsCollected == null)     itemsCollected = "0";
        if (removalReason == null)      removalReason = "0";
        if (missing_reason == null)     missing_reason = "0";
        if (removal_type == null)       removal_type = "0";
        if (s_reason_repla == null)     s_reason_repla = "0";
        if (disconnection_reason == null) disconnection_reason = "0";
        if (not_available_activity == null) not_available_activity = "0";
        if (not_available_reason == null)   not_available_reason = "0";
        if (old_sim_no == null)         old_sim_no = "0";
        if (new_sim_no == null)         new_sim_no = "0";
        if (sim_reason == null)         sim_reason = "0";
        if (sim_provider == null)       sim_provider = "0";
        if (veh_condition == null)      veh_condition = "W";
        if (missing_type == null)       missing_type = "M";
        if (collection_amount == null)  collection_amount = "0";
        if (collection_date == null)    collection_date = "0";
        if (collection_type == null)    collection_type = "0";
        if (image == null)              image = "0";
        if (is_demo == null)            is_demo = "N";
        if (cut_off == null)            cut_off = "N";
        if (is_drs == null)             is_drs = "N";
        if (drs_dirction == null)       drs_dirction = "N";
        if (mgt_set == null)            mgt_set = "N";
        if (door_sensor == null)        door_sensor = "N";
        if (ignition_sensor == null)    ignition_sensor = "N";
        if (confirmVehNo == null)       confirmVehNo = "";
        if (s_reinst_conf_reg_no == null) s_reinst_conf_reg_no = "";
        if (contact_person == null)     contact_person = "";
        if (contact_no == null)         contact_no = "0";
        if (payment_type == null)       payment_type = "C";
        if (s_rep_srNo == null)         s_rep_srNo = "";

        updateInstallationData();
    }

    private void showValidationError(InstallationValidator.ValidationResult result) {
        if (result.errorMessage != null) {
            Toast.makeText(getContext(), result.errorMessage, Toast.LENGTH_LONG).show();
        } else if (result.errorView instanceof EditText) {
            ((EditText) result.errorView).setError(result.errorFieldMessage);
            result.errorView.requestFocus();
        } else if (result.errorView instanceof TextView) {
            ((TextView) result.errorView).setError(result.errorFieldMessage);
            result.errorView.requestFocus();
        } else if (result.errorFieldMessage != null) {
            // fallback — no specific view, use toast
            Toast.makeText(getContext(), result.errorFieldMessage, Toast.LENGTH_LONG).show();
        }
}

private void handleWorkTypeSelected(String sWorkId) {
        switch (sWorkId) {
            case "1":  setupFaultWorkType();           break;
            case "2":  setupInstallationWorkType();    break;
            case "3":  setupReplacementWorkType();     break;
            case "4":  setupReInstallWorkType();       break;
            case "5":  setupRemovalWorkType();         break;
            case "6":  setupPhoneSupportWorkType();    break;
            case "7":  setupSimReplacementWorkType();  break;
            case "8":  setupMissingDeviceWorkType();   break;
            case "9":  setupVehicleNotAvailWorkType(); break;
            case "10": setupPaymentWorkType();         break;
            case "11": setupOtherWorkType();           break;
            case "12": setupAddUMWorkType();           break;
            case "13": setupRemoveUMWorkType();        break;
        }
    }

    private void setupFaultWorkType() {
        device_info.setText("Fault Details");
        e_remarks.setHint("Remarks");
        setupFaultListeners();
        postSetup(() -> {
            clearFormData();
            fault_reg_no.setEnabled(true);
            showOnlyLayout(linearFault);
            faultDetail.setVisibility(View.GONE);
            getDeviceTypes();
            getFaultList();
            addVehType();
            getSerialNo();
        });
    }

    private void setupFaultListeners() {
        fault_vts_id.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                vts_id = fault_vts_id.getText().toString();
                if (vts_id.equals("")) fault_reg_no.setText("");
                else getVTSDetail();
            }
            @Override public void afterTextChanged(Editable e) {
                vts_id = fault_vts_id.getText().toString();
                if (vts_id.equals("")) fault_reg_no.setText("");
                else getVTSDetail();
            }
        });

        vltd_sr_no_fault.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                vts_id = vltd_sr_no_fault.getText().toString();
                if (vts_id.equals("")) fault_reg_no.setText("");
                else getVTSDetail();
            }
            @Override public void afterTextChanged(Editable e) {
                vts_id = vltd_sr_no_fault.getText().toString();
                if (vts_id.equals("")) fault_reg_no.setText("");
                else getVTSDetail();
            }
        });

        vltddeviceFault.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) return;
                i = i - 1;
                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spn_fault_sr_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) return;
                i = i - 1;
                s_old_serial_no = removesrnoList.get(i).getPcb_sr_no();
                vts_id = s_old_serial_no;
                getVTSDetail();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        text_to_show_fault.setOnClickListener(view -> {
            if (vltddeviceFault.getSelectedItem().toString().equalsIgnoreCase("AIS 140")) {
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
        });

        delete_button_fault_e_series.setOnClickListener(v ->
                linear_device_sr_no_fault_e_series.setVisibility(View.GONE));

        delete_button_fault.setOnClickListener(v ->
                linear_device_sr_no_fault.setVisibility(View.GONE));

        lv.setOnItemClickListener((parent, view, position, id) -> {
            others = others + (list_change_values.get(position).getId()) + ":";
            SparseBooleanArray checked = lv.getCheckedItemPositions();
            String abc = String.valueOf(checked);
            String[] separated = abc.split("\\{");
            String[] separate = separated[1].split("\\}");
            String status = separate[0];
            faultDetail.setVisibility(status.contains("9=true") ? View.VISIBLE : View.GONE);
        });

        fault_reg_no.setOnTouchListener((v, event) -> {
            fault_reg_no.setText("");
            fault_reg_no.setTextColor(getResources().getColor(R.color.black));
            return false;
        });
    }


    // ─── Work Type 2: Installation ────────────────────────────────────
    private void setupInstallationWorkType() {
        device_info.setText("Installation Details");
        e_remarks.setHint("Remarks");
        setupInstallationListeners();
        postSetup(() -> {
            clearFormData();
            e_device_id.setInputType(InputType.TYPE_CLASS_NUMBER);
            showOnlyLayout(linearInstall);
            getDeviceTypes();
            addVehType();
            getDevice();
            getSerialNo();
        });
    }

    private void setupInstallationListeners() {
        radiodrsInstall.setOnCheckedChangeListener((group, id) -> {
            if (id == R.id.radioyesdrsInstall) {
                linearDrs.setVisibility(View.VISIBLE);
                magnetset_install.setVisibility(View.VISIBLE);
            } else {
                linearDrs.setVisibility(View.GONE);
                magnetset_install.setVisibility(View.GONE);
            }
        });

        radiodeviceType1.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.acessories) {
                linearDrs.setVisibility(View.VISIBLE);
                magnetset_install.setVisibility(View.VISIBLE);
                e_reg_no.setVisibility(View.GONE);
                con_in_reg_no.setVisibility(View.GONE);
                con_tilsrNo.setVisibility(View.GONE);
                rel_sr_no.setVisibility(View.GONE);
                linear_accessory.setVisibility(View.VISIBLE);
                clearData();
                clearTextView();
            } else {
                linearDrs.setVisibility(View.GONE);
                magnetset_install.setVisibility(View.GONE);
                con_vltd_sr_no.setVisibility(View.VISIBLE);
                e_reg_no.setVisibility(View.VISIBLE);
                con_tilsrNo.setVisibility(View.VISIBLE);
                rel_sr_no.setVisibility(View.VISIBLE);
                con_in_reg_no.setVisibility(View.VISIBLE);
                linear_accessory.setVisibility(View.GONE);
                clearData();
                clearTextView();
            }
        });

        accessory_reg_no.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                s_reg_no = accessory_reg_no.getText().toString();
                if (s_reg_no.equals("")) accessory_sr_no.setText("");
                else getAccSerialNo(s_reg_no);
            }
            @Override public void afterTextChanged(Editable s) {
                s_reg_no = accessory_reg_no.getText().toString();
                if (s_reg_no.equals("")) accessory_sr_no.setText("");
                else getAccSerialNo(s_reg_no);
            }
        });

        sr_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) return;
                i = i - 1;
                serial_no = srnoList.get(i).getPcb_sr_no();
                s_cust_type = srnoList.get(i).getCust_type();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        radioGroupFuel.setOnCheckedChangeListener((group, id) ->
                fuelVoltage.setVisibility(id == R.id.fuelNo ? View.GONE : View.VISIBLE));

        radioGrouptilt.setOnCheckedChangeListener((group, id) ->
                tilt_sensor = (id == R.id.tiltNo) ? "N" : "Y");

        radioGrouptemp.setOnCheckedChangeListener((group, id) ->
                temp_sensor = (id == R.id.tempNo) ? "N" : "Y");

        radioGrouptrans.setOnCheckedChangeListener((group, id) ->
                trans = (id == R.id.transNo) ? "N" : "Y");

        radioGroupfuelSensor.setOnCheckedChangeListener((group, id) ->
                fuel_status = (id == R.id.fuelSensorNewNo) ? "N" : "Y");

        radioGroupLid.setOnCheckedChangeListener((group, id) -> {
            if (id == R.id.lidNone) lid_status = "N";
            else if (id == R.id.lidTop) lid_status = "T";
            else if (id == R.id.lidRear) lid_status = "R";
            else lid_status = "B";
        });

        vltddevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) return;
                i = i - 1;
                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
                boolean isE124 = vltddevice.getSelectedItem().toString().equalsIgnoreCase("E124");
                oldDeviceType.setVisibility(isE124 ? View.VISIBLE : View.GONE);
                options.setVisibility(View.VISIBLE);
                vltdOptions.setVisibility(View.VISIBLE);
                vts_sr_no.setVisibility(isE124 ? View.VISIBLE : View.GONE);
                tilsrNo.setVisibility(View.GONE);
                tilvtsno.setVisibility(View.GONE);
                e_device_id.setVisibility(View.GONE);
                til_vts_sr_no.setVisibility(View.GONE);
                vltd_sr_no.setVisibility(View.GONE);
                if (!isE124) s_new_device_id = "0";
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        text_to_show.setOnClickListener(v -> {
            boolean isE124 = vltddevice.getSelectedItem().toString().equalsIgnoreCase("E124");
            til_vts_sr_no.setVisibility(isE124 ? View.VISIBLE : View.GONE);
            linear_device_sr_no.setVisibility(isE124 ? View.GONE : View.VISIBLE);
            linear_device_sr_no_e_series.setVisibility(isE124 ? View.VISIBLE : View.GONE);
            if (!isE124) {
                tilsrNo.setVisibility(View.VISIBLE);
                vltd_sr_no.setVisibility(View.VISIBLE);
            }
        });

        delete_button.setOnClickListener(v -> {
            vltd_sr_no.setText("");
            linear_device_sr_no.setVisibility(View.GONE);
        });

        delete_button_e_series.setOnClickListener(v -> {
            vts_sr_no.setText("");
            linear_device_sr_no_e_series.setVisibility(View.GONE);
        });
    }


    // ─── Work Type 3: Replacement ─────────────────────────────────────
    private void setupReplacementWorkType() {
        device_info.setText("Replacement Details");
        e_remarks.setHint("Remarks");
        setupReplacementListeners();
        postSetup(() -> {
            clearFormData();
            mgt_set = "N";
            showOnlyLayout(linearReplacement);
            relaydrsTypeReplace.setVisibility(View.GONE);
            old_drsid.setVisibility(View.GONE);
            new_drsid.setVisibility(View.GONE);
            getDeviceTypes();
            fetchReasons();
            getSerialNo();
        });
    }

    private void setupReplacementListeners() {
        old_deviceidreplace.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                vts_id = old_deviceidreplace.getText().toString();
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
        });

        old_replace_sr_no.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                vts_id = old_replace_sr_no.getText().toString();
                getVTSDetail();
            } else {
                regNo.setText("");
                plantName.setText("");
            }
        });

        radioGrouptiltReplace.setOnCheckedChangeListener((group, id) -> {
            tilt_sensor = (id == R.id.tiltNoReplace) ? "N" : "Y";
            sensor_veh.setVisibility(id == R.id.tiltNoReplace ? View.GONE : View.VISIBLE);
        });

        radioGrouptempReplace.setOnCheckedChangeListener((group, id) -> {
            temp_sensor = (id == R.id.tempNoReplace) ? "N" : "Y";
            sensor_veh.setVisibility(id == R.id.tempNoReplace ? View.GONE : View.VISIBLE);
        });

        radioGroupPanicReplace.setOnCheckedChangeListener((group, id) -> {
            panic_status = (id == R.id.panicNoReplace) ? "N" : "Y";
            sensor_veh.setVisibility(id == R.id.panicNoReplace ? View.GONE : View.VISIBLE);
        });

        radioGrouptransReplace.setOnCheckedChangeListener((group, id) -> {
            trans = (id == R.id.transNoReplace) ? "N" : "Y";
            sensor_veh.setVisibility(id == R.id.transNoReplace ? View.GONE : View.VISIBLE);
        });

        radioGroupLidReplace.setOnCheckedChangeListener((group, id) -> {
            if (id == R.id.lidNoneReplace) { lid_status = "N"; sensor_veh.setVisibility(View.GONE); }
            else if (id == R.id.lidTopReplace) { lid_status = "T"; sensor_veh.setVisibility(View.VISIBLE); }
            else if (id == R.id.lidRearReplace) { lid_status = "R"; sensor_veh.setVisibility(View.VISIBLE); }
            else { lid_status = "B"; sensor_veh.setVisibility(View.VISIBLE); }
        });

        vltddeviceReplace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) return;
                i = i - 1;
                clearTextView();
                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
                boolean isAIS = vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("AIS 140");
                linear_vts_id_replace.setVisibility(isAIS ? View.GONE : View.VISIBLE);
                linear_device_id_replace_old.setVisibility(View.GONE);
                linear_device_id_replace_new.setVisibility(View.GONE);
                linear_device_sr_no_replace_old.setVisibility(View.GONE);
                linear_device_sr_no_replace_new.setVisibility(View.GONE);
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spn_old_sr_no_replace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) return;
                i = i - 1;
                s_old_serial_no = removesrnoList.get(i).getPcb_sr_no();
                s_e_device_id = "0";
                vts_id = s_old_serial_no;
                getVTSDetail();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        spn_new_sr_no_replace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) return;
                i = i - 1;
                serial_no = srnoList.get(i).getPcb_sr_no();
                vts_id = serial_no;
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        text_to_show_replace_old.setOnClickListener(view -> {
            boolean isAIS = vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("AIS 140");
            linear_device_sr_no_replace_old.setVisibility(isAIS ? View.VISIBLE : View.GONE);
            linear_device_id_replace_old.setVisibility(isAIS ? View.GONE : View.VISIBLE);
        });

        text_to_show_replace_new.setOnClickListener(view -> {
            boolean isAIS = vltddeviceReplace.getSelectedItem().toString().equalsIgnoreCase("AIS 140");
            linear_device_sr_no_replace_new.setVisibility(isAIS ? View.VISIBLE : View.GONE);
            linear_device_id_replace_new.setVisibility(isAIS ? View.GONE : View.VISIBLE);
        });

        delete_button_replace_old_id.setOnClickListener(v -> linear_device_id_replace_old.setVisibility(View.GONE));
        delete_button_replace_new_id.setOnClickListener(v -> linear_device_id_replace_new.setVisibility(View.GONE));
        delete_button_replace_old_serial.setOnClickListener(v -> linear_device_sr_no_replace_old.setVisibility(View.GONE));
        delete_button_replace_new_serial.setOnClickListener(v -> linear_device_sr_no_replace_new.setVisibility(View.GONE));

        radioGroups.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            clearData();
            if (checkedId == R.id.radioVTS) {
                replace_type = "D"; radioButtonChecked = "V";
                linearvts.setVisibility(View.VISIBLE);
                told_drsid.setVisibility(View.GONE);
                tnew_drsid.setVisibility(View.GONE);
                old_drsid.setVisibility(View.GONE);
                new_drsid.setVisibility(View.GONE);
                magnet_set.setVisibility(View.GONE);
                relaydrsTypeReplace.setVisibility(View.GONE);
                deviceTypeReplace.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.radioDRS) {
                replace_type = "S"; radioButtonChecked = "D";
                linearvts.setVisibility(View.GONE);
                told_drsid.setVisibility(View.VISIBLE);
                tnew_drsid.setVisibility(View.VISIBLE);
                old_drsid.setVisibility(View.VISIBLE);
                new_drsid.setVisibility(View.VISIBLE);
                magnet_set.setVisibility(View.VISIBLE);
                relaydrsTypeReplace.setVisibility(View.VISIBLE);
                drsReplacemsg.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.radioBoth) {
                replace_type = "B"; radioButtonChecked = "B";
                linearvts.setVisibility(View.VISIBLE);
                told_drsid.setVisibility(View.VISIBLE);
                tnew_drsid.setVisibility(View.VISIBLE);
                old_drsid.setVisibility(View.VISIBLE);
                new_drsid.setVisibility(View.VISIBLE);
                magnet_set.setVisibility(View.VISIBLE);
                relaydrsTypeReplace.setVisibility(View.VISIBLE);
            } else {
                radioButtonChecked = "N";
            }
            setupDrsVtsTextWatcher();
        });
    }

    private void setupDrsVtsTextWatcher() {
        drs_vts_id.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                vts_id = drs_vts_id.getText().toString();
                if (vts_id.equals("")) drs_veh_no.setText("");
                else getVTSDetails();
            }
            @Override public void afterTextChanged(Editable e) {}
        });
        drs_veh_no.setOnTouchListener((v, event) -> {
            drs_veh_no.setText("");
            drs_veh_no.setTextColor(getResources().getColor(R.color.black));
            return false;
        });
    }


    // ─── Work Type 4: ReInstallation ──────────────────────────────────
    private void setupReInstallWorkType() {
        device_info.setText("ReInstallation Details");
        e_remarks.setHint("Remarks");
        old_deviceid.setHint("Device ID");
        setupReInstallListeners();
        postSetup(() -> {
            clearFormData();
            showOnlyLayout(linearReInstall);
            getDeviceTypes();
            addVehType();
            getSerialNo();
        });
    }

    private void setupReInstallListeners() {
        radioGroupReinstallType.setOnCheckedChangeListener((group, id) -> {
            if (id == R.id.radioDevice) reinstDevice = "D";
            else if (id == R.id.radioSensor) reinstDevice = "S";
            else reinstDevice = "B";
        });

        radioGroupReinstall.setOnCheckedChangeListener((radioGroup, id) -> {
            if (id == R.id.old_Device) {
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

        radioGroupFuelReinst.setOnCheckedChangeListener((group, id) ->
                refuelVoltage.setVisibility(id == R.id.fuelNoReinst ? View.GONE : View.VISIBLE));

        radiogroupdrsReinst.setOnCheckedChangeListener((group, id) -> {
            drsStatus = (id == R.id.drs_no_reinst) ? "N" : "Y";
            lay_sensor_veh.setVisibility(id == R.id.drs_no_reinst ? View.GONE : View.VISIBLE);
        });

        radioGroupPanicReinst.setOnCheckedChangeListener((group, id) -> {
            panic_status = (id == R.id.drs_no_reinst) ? "N" : "Y";
            lay_sensor_veh.setVisibility(id == R.id.drs_no_reinst ? View.GONE : View.VISIBLE);
        });

        radioGrouptiltReinst.setOnCheckedChangeListener((group, id) -> {
            tilt_sensor = (id == R.id.tiltNoReinst) ? "N" : "Y";
            lay_sensor_veh.setVisibility(id == R.id.tiltNoReinst ? View.GONE : View.VISIBLE);
        });

        radioGroupfuelSensorReinst.setOnCheckedChangeListener((group, id) -> {
            fuel_status = (id == R.id.fuelSensorNewNoReinst) ? "N" : "Y";
            lay_sensor_veh.setVisibility(id == R.id.fuelSensorNewNoReinst ? View.GONE : View.VISIBLE);
        });

        radioGrouptempReinst.setOnCheckedChangeListener((group, id) -> {
            temp_sensor = (id == R.id.tempNoReinst) ? "N" : "Y";
            lay_sensor_veh.setVisibility(id == R.id.tempNoReinst ? View.GONE : View.VISIBLE);
        });

        radioGrouptransReinst.setOnCheckedChangeListener((group, id) -> {
            trans = (id == R.id.transNoReinst) ? "N" : "Y";
            lay_sensor_veh.setVisibility(id == R.id.transNoReinst ? View.GONE : View.VISIBLE);
        });

        radioGroupLidReinst.setOnCheckedChangeListener((group, id) -> {
            if (id == R.id.lidNoneReinst) { lid_status = "N"; lay_sensor_veh.setVisibility(View.GONE); }
            else if (id == R.id.lidTopReinst) { lid_status = "T"; lay_sensor_veh.setVisibility(View.VISIBLE); }
            else if (id == R.id.lidRearReinst) { lid_status = "R"; lay_sensor_veh.setVisibility(View.VISIBLE); }
            else { lid_status = "B"; lay_sensor_veh.setVisibility(View.VISIBLE); }
        });

        vltddeviceReinst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) return;
                i = i - 1;
                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
                boolean isAIS = vltddeviceReinst.getSelectedItem().toString().equalsIgnoreCase("AIS 140");
                getSerialNo();
                reinstText.setVisibility(isAIS ? View.GONE : View.VISIBLE);
                til_id_reinst.setVisibility(isAIS ? View.GONE : View.VISIBLE);
                til_sr_reinst.setVisibility(isAIS ? View.GONE : View.VISIBLE);
                til_old_vltd_sr_no.setVisibility(isAIS ? View.VISIBLE : View.GONE);
                til_new_vltd_sr_no.setVisibility(View.GONE);
                new_deviceidReinstall.setVisibility(View.GONE);
                til_id_sr.setVisibility(View.GONE);
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spn_reinstall_sr_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) return;
                i = i - 1;
                s_old_serial_no = removesrnoList.get(i).getPcb_sr_no();
                serial_no = "0";
                s_new_device_id = "0";
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        text_to_show_reinstall.setOnClickListener(view -> {
            boolean isAIS = vltddeviceReinst.getSelectedItem().toString().equalsIgnoreCase("AIS 140");
            linear_device_sr_no_reinstall_ais.setVisibility(isAIS ? View.VISIBLE : View.GONE);
            linear_device_sr_no_reinstall.setVisibility(isAIS ? View.GONE : View.VISIBLE);
        });

        delete_button_reinstall_ais.setOnClickListener(view -> {
            old_vltd_sr_no.setText("");
            linear_device_sr_no_reinstall.setVisibility(View.GONE);
        });

        delete_button_reinstall.setOnClickListener(view ->
                linear_device_sr_no_reinstall.setVisibility(View.GONE));

        old_deviceid.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) { vts_id = old_deviceid.getText().toString(); getVTSDetail(); }
        });

        old_vltd_sr_no.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) { vts_id = old_vltd_sr_no.getText().toString(); getVTSDetail(); }
        });

        radiodrsReInstall.setOnCheckedChangeListener((group, id) -> {
            if (id == R.id.radioyesdrsReInstall) {
                linearDrs.setVisibility(View.VISIBLE);
                magnetset_install.setVisibility(View.VISIBLE);
            } else {
                linearDrs.setVisibility(View.GONE);
                magnetset_install.setVisibility(View.GONE);
            }
        });
    }


    // ─── Work Type 5: Removal ─────────────────────────────────────────
    private void setupRemovalWorkType() {
        device_info.setText("Removal/Collection Details");
        e_remarks.setHint("Remarks");
        setupRemovalListeners();
        postSetup(() -> {
            clearFormData();
            remove_reg_no.setEnabled(true);
            showOnlyLayout(linearRemoval);
            getDeviceTypes();
            removal_type();
            addReasonRemove();
            getItemCollectList();
            getSerialNo();
        });
    }

    private void setupRemovalListeners() {
        remove_deviceid.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                vts_id = remove_deviceid.getText().toString();
                if (vts_id.equals("")) remove_reg_no.setText("");
                else getVTSDetail();
            }
            @Override public void afterTextChanged(Editable e) {}
        });

        remove_sr_no.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                vts_id = remove_sr_no.getText().toString();
                if (vts_id.equals("")) remove_reg_no.setText("");
                else getVTSDetail();
            }
            @Override public void afterTextChanged(Editable e) {}
        });

        vltddeviceRemove.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) return;
                i = i - 1;
                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
                boolean isAIS = vltddeviceRemove.getSelectedItem().toString().equalsIgnoreCase("AIS 140");
                lin_vts_id_remove.setVisibility(isAIS ? View.GONE : View.VISIBLE);
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        text_to_show_remove.setOnClickListener(v -> {
            boolean isAIS = vltddeviceRemove.getSelectedItem().toString().equalsIgnoreCase("AIS 140");
            linear_device_sr_no_remove.setVisibility(isAIS ? View.VISIBLE : View.GONE);
            til_remove_sr.setVisibility(isAIS ? View.VISIBLE : View.GONE);
            linear_device_sr_no_remove_e_series.setVisibility(isAIS ? View.GONE : View.VISIBLE);
            til_vts_remove.setVisibility(isAIS ? View.GONE : View.VISIBLE);
        });

        delete_button_remove.setOnClickListener(v -> {
            remove_sr_no.setText("");
            linear_device_sr_no_remove.setVisibility(View.GONE);
        });

        delete_button_remove_e_series.setOnClickListener(v -> {
            remove_deviceid.setText("");
            linear_device_sr_no_remove_e_series.setVisibility(View.GONE);
        });

        radioGroupTypeRemove.setOnCheckedChangeListener((group, id) -> {
            if (id == R.id.radioDeviceRemove) removeDeviceType = "D";
            else if (id == R.id.radioSensorRemove) removeDeviceType = "S";
            else removeDeviceType = "B";
        });

        radioGrouptiltRemove.setOnCheckedChangeListener((group, id) -> {
            tilt_sensor = (id == R.id.tiltRemoveNo) ? "N" : "Y";
            sensor_veh_remove.setVisibility(id == R.id.tiltRemoveNo ? View.GONE : View.VISIBLE);
        });

        radioGroupdrsRemove.setOnCheckedChangeListener((group, id) -> {
            drsStatus = (id == R.id.drsRemoveNo) ? "N" : "Y";
            sensor_veh_remove.setVisibility(id == R.id.drsRemoveNo ? View.GONE : View.VISIBLE);
        });

        radioGrouptempRemove.setOnCheckedChangeListener((group, id) -> {
            temp_sensor = (id == R.id.tempNoRemove) ? "N" : "Y";
            sensor_veh_remove.setVisibility(id == R.id.tempNoRemove ? View.GONE : View.VISIBLE);
        });

        radioGroupPanicRemove.setOnCheckedChangeListener((group, id) -> {
            panic_status = (id == R.id.panicNoRemove) ? "N" : "Y";
            sensor_veh_remove.setVisibility(id == R.id.panicNoRemove ? View.GONE : View.VISIBLE);
        });

        radioGroupfuelRemove.setOnCheckedChangeListener((group, id) -> {
            trans = (id == R.id.fuelSensorNoRemove) ? "N" : "Y";
            sensor_veh_remove.setVisibility(id == R.id.fuelSensorNoRemove ? View.GONE : View.VISIBLE);
        });

        radioGrouptransRemove.setOnCheckedChangeListener((group, id) -> {
            trans = (id == R.id.transNoRemove) ? "N" : "Y";
            sensor_veh_remove.setVisibility(id == R.id.transNoRemove ? View.GONE : View.VISIBLE);
        });

        radioGroupdeviceworking.setOnCheckedChangeListener((group, checkedId) ->
                device_working_status = (checkedId == R.id.deviceWorking) ? "W" : "F");

        radioGroupsensorworking.setOnCheckedChangeListener((group, checkedId) ->
                sensor_working_status = (checkedId == R.id.sensorWorking) ? "W" : "F");

        radioGroupLidRemove.setOnCheckedChangeListener((group, id) -> {
            if (id == R.id.lidNoneRemove) { lid_status = "N"; sensor_veh_remove.setVisibility(View.GONE); }
            else if (id == R.id.lidTopRemove) { lid_status = "T"; sensor_veh_remove.setVisibility(View.VISIBLE); }
            else if (id == R.id.lidRearRemove) { lid_status = "R"; sensor_veh_remove.setVisibility(View.VISIBLE); }
            else { lid_status = "B"; sensor_veh_remove.setVisibility(View.VISIBLE); }
        });

        spn_remove_sr_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) return;
                i = i - 1;
                s_old_serial_no = removesrnoList.get(i).getPcb_sr_no();
                vts_id = s_old_serial_no;
                getVTSDetail();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        remove_reg_no.setOnTouchListener((v, event) -> {
            remove_reg_no.setText("");
            remove_reg_no.setTextColor(getResources().getColor(R.color.black));
            return false;
        });
    }


    // ─── Work Type 6: Phone Support ───────────────────────────────────
    private void setupPhoneSupportWorkType() {
        device_info.setText("Phone Support");
        e_remarks.setHint("Remarks");
        setupPhoneSupportListeners();
        postSetup(() -> {
            clearFormData();
            phSupport_reg_no.setEnabled(true);
            showOnlyLayout(linearPhoneSupport);
            getDeviceTypes();
            getPhoneSupportList();
            getSerialNo();
        });
    }

    private void setupPhoneSupportListeners() {
        phsupport_vts_id.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                vts_id = phsupport_vts_id.getText().toString();
                if (vts_id.equals("")) phSupport_reg_no.setText("");
                else getVTSDetail();
            }
            @Override public void afterTextChanged(Editable e) {}
        });

        vltd_sr_no_phn.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                vts_id = vltd_sr_no_phn.getText().toString();
                if (vts_id.equals("")) phSupport_reg_no.setText("");
                else getVTSDetail();
            }
            @Override public void afterTextChanged(Editable e) {}
        });

        vltddevicephn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) return;
                i = i - 1;
                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        text_to_show_phone.setOnClickListener(v -> {
            boolean isAIS = vltddevicephn.getSelectedItem().toString().equalsIgnoreCase("AIS 140");
            linear_device_sr_no_phone.setVisibility(isAIS ? View.VISIBLE : View.GONE);
            vltd_sr_no_phn.setVisibility(isAIS ? View.VISIBLE : View.GONE);
            tilphnSr.setVisibility(isAIS ? View.VISIBLE : View.GONE);
            phsupport_vts_id.setVisibility(isAIS ? View.GONE : View.VISIBLE);
            tilphnVts.setVisibility(isAIS ? View.GONE : View.VISIBLE);
            linear_device_sr_no_phone_e_series.setVisibility(isAIS ? View.GONE : View.VISIBLE);
        });

        delete_button_phone_e_series.setOnClickListener(view -> {
            phsupport_vts_id.setText("");
            linear_device_sr_no_phone_e_series.setVisibility(View.GONE);
        });

        delete_button_phone.setOnClickListener(view -> {
            vltd_sr_no_phn.setText("");
            linear_device_sr_no_phone.setVisibility(View.GONE);
        });

        spn_phone_sr_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) return;
                i = i - 1;
                s_old_serial_no = removesrnoList.get(i).getPcb_sr_no();
                s_e_device_id = "0";
                vts_id = s_old_serial_no;
                getVTSDetail();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        phSupport_reg_no.setOnTouchListener((v, event) -> {
            phSupport_reg_no.setText("");
            phSupport_reg_no.setTextColor(getResources().getColor(R.color.black));
            return false;
        });
    }


    // ─── Work Type 7: Sim Replacement ────────────────────────────────
    private void setupSimReplacementWorkType() {
        device_info.setText("Sim Replacement");
        e_remarks.setHint("Remarks");
        // listeners stay outside postSetup
        sim_vts_id.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                vts_id = sim_vts_id.getText().toString();
                if (vts_id.equals("")) sim_vehicle_no.setText("");
                else getVTSDetail();
            }
            @Override public void afterTextChanged(Editable e) {}
        });
        sim_vehicle_no.setOnTouchListener((v, event) -> {
            sim_vehicle_no.setText("");
            sim_vehicle_no.setTextColor(getResources().getColor(R.color.black));
            return false;
        });
        postSetup(() -> {
            clearFormData();
            showOnlyLayout(linearSimRepalace);
            getDeviceTypes();
            getSimReasonList();
            getSimOperator();
        });
    }


    // ─── Work Type 8: Missing Device ──────────────────────────────────
    private void setupMissingDeviceWorkType() {
        device_info.setText("Report Missing Device");
        e_remarks.setHint("Remarks");
        setupMissingDeviceListeners();
        postSetup(() -> {
            clearFormData();
            missing_type = "M";
            showOnlyLayout(linearDeviceMissing);
            getDeviceTypes();
            damageReason();
            getSerialNo();
        });
    }

    private void setupMissingDeviceListeners() {
        mDevice_vts_id.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                vts_id = mDevice_vts_id.getText().toString();
                if (vts_id.equals("")) mDevice_reg_no.setText("");
                else getVTSDetail();
            }
            @Override public void afterTextChanged(Editable e) {}
        });

        radioGroupMiss.setOnCheckedChangeListener((group, id) -> {
            if (id == R.id.radioDeviceMiss) missDeviceType = "D";
            else if (id == R.id.radioSensorMiss) missDeviceType = "S";
            else missDeviceType = "B";
        });

        vltddeviceMiss.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) return;
                i = i - 1;
                s_vts_type = String.valueOf(deviceTypeOtherAis_arr.get(i).getId());
                boolean isAIS = vltddeviceMiss.getSelectedItem().toString().equalsIgnoreCase("AIS 140");
                tilDeviceMiss.setVisibility(isAIS ? View.VISIBLE : View.GONE);
                vltd_sr_no_miss.setVisibility(isAIS ? View.VISIBLE : View.GONE);
                mDevice_vts_id.setVisibility(isAIS ? View.GONE : View.VISIBLE);
                til_vts_miss.setVisibility(isAIS ? View.GONE : View.VISIBLE);
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spn_missing_sr_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) return;
                i = i - 1;
                s_old_serial_no = removesrnoList.get(i).getPcb_sr_no();
                vts_id = s_old_serial_no;
                getVTSDetail();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        text_to_show_missing.setOnClickListener(view -> {
            boolean isAIS = vltddeviceMiss.getSelectedItem().toString().equalsIgnoreCase("AIS 140");
            linear_device_sr_no_missing.setVisibility(isAIS ? View.VISIBLE : View.GONE);
            linear_device_sr_no_missing_e_series.setVisibility(isAIS ? View.GONE : View.VISIBLE);
        });

        delete_button_missing.setOnClickListener(view ->
                linear_device_sr_no_missing.setVisibility(View.GONE));

        delete_button_missing_e_series.setOnClickListener(view ->
                linear_device_sr_no_missing_e_series.setVisibility(View.GONE));

        radioGroupMissing.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.reportMissing) {
                relMissing.setVisibility(View.GONE);
                missing_type = "M";
            } else if (checkedId == R.id.damageDevice) {
                relMissing.setVisibility(View.VISIBLE);
                missing_type = "D";
            }
            mDevice_reg_no.setText("");
            mDevice_vts_id.setText("");
        });

        radioGrouptiltMissing.setOnCheckedChangeListener((group, id) -> {
            tilt_sensor = (id == R.id.tiltMissingNo) ? "N" : "Y";
            sensor_veh_missing.setVisibility(id == R.id.tiltMissingNo ? View.GONE : View.VISIBLE);
        });

        radioGroupdrsMissing.setOnCheckedChangeListener((group, id) -> {
            drsStatus = (id == R.id.drsMissingNo) ? "N" : "Y";
            sensor_veh_missing.setVisibility(id == R.id.drsMissingNo ? View.GONE : View.VISIBLE);
        });

        radioGrouptempMissing.setOnCheckedChangeListener((group, id) -> {
            temp_sensor = (id == R.id.tempNoMissing) ? "N" : "Y";
            sensor_veh_missing.setVisibility(id == R.id.tempNoMissing ? View.GONE : View.VISIBLE);
        });

        radioGroupPanicMissing.setOnCheckedChangeListener((group, id) -> {
            panic_status = (id == R.id.panicNoMissing) ? "N" : "Y";
            sensor_veh_missing.setVisibility(id == R.id.panicNoMissing ? View.GONE : View.VISIBLE);
        });

        radioGroupfuelMissing.setOnCheckedChangeListener((group, id) -> {
            trans = (id == R.id.fuelSensorNoMissing) ? "N" : "Y";
            sensor_veh_missing.setVisibility(id == R.id.fuelSensorNoMissing ? View.GONE : View.VISIBLE);
        });

        radioGrouptransMissing.setOnCheckedChangeListener((group, id) -> {
            trans = (id == R.id.transNoMissing) ? "N" : "Y";
            sensor_veh_missing.setVisibility(id == R.id.transNoMissing ? View.GONE : View.VISIBLE);
        });

        radioGroupLidMissing.setOnCheckedChangeListener((group, id) -> {
            if (id == R.id.lidNoneMissing) { lid_status = "N"; sensor_veh_missing.setVisibility(View.GONE); }
            else if (id == R.id.lidTopMissing) { lid_status = "T"; sensor_veh_missing.setVisibility(View.VISIBLE); }
            else if (id == R.id.lidRearMissing) { lid_status = "R"; sensor_veh_missing.setVisibility(View.VISIBLE); }
            else { lid_status = "B"; sensor_veh_missing.setVisibility(View.VISIBLE); }
        });

        mDevice_reg_no.setOnTouchListener((v, event) -> {
            mDevice_reg_no.setText("");
            mDevice_reg_no.setTextColor(getResources().getColor(R.color.black));
            return false;
        });
    }


    // ─── Work Type 9: Vehicle Not Available ───────────────────────────
    private void setupVehicleNotAvailWorkType() {
        device_info.setText("Report Vehicle Not Available");
        e_remarks.setHint("Remarks");
        // listeners stay outside
        vehNotAvailVtsID.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                vts_id = vehNotAvailVtsID.getText().toString();
                if (!vts_id.equals("")) getVTSDetail();
            }
            @Override public void afterTextChanged(Editable e) {}
        });
        vehNotAvailRegNo.setOnTouchListener((v, event) -> {
            vehNotAvailRegNo.setText("");
            vehNotAvailRegNo.setTextColor(getResources().getColor(R.color.black));
            return false;
        });
        postSetup(() -> {
            clearFormData();
            showOnlyLayout(linearVehicleNotAvail);
            getDeviceTypes();
            addActivity();
            addVehNotAvailReason();
        });
    }


    // ─── Work Type 10: Payment ────────────────────────────────────────
    private void setupPaymentWorkType() {
        device_info.setText("Payment Activity");
        e_remarks.setHint("Remarks");
        radiogroupPay.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.collection) {
                payCollection.setVisibility(View.VISIBLE);
                followUp.setVisibility(View.GONE);
                payment_type = "C";
            } else if (checkedId == R.id.followup) {
                followUp.setVisibility(View.VISIBLE);
                payCollection.setVisibility(View.GONE);
                payment_type = "F";
            }
        });
        postSetup(() -> {
            clearFormData();
            payment_type = "C";
            showOnlyLayout(linearPayment);
            getDeviceTypes();
            pMethod();
        });
    }


    // ─── Work Type 11: Other Work ─────────────────────────────────────
    private void setupOtherWorkType() {
        device_info.setText("Other Work");
        e_remarks.setHint("Add Activity Detail");
        postSetup(() -> {
            clearFormData();
            showOnlyLayout(linearOthers);
            getDeviceTypes();
        });
    }


    // ─── Work Type 12: Add to UM ──────────────────────────────────────
    private void setupAddUMWorkType() {
        device_info.setText("Add In Under Maintenance");
        e_remarks.setHint("Add Remark");
        postSetup(() -> {
            clearFormData();
            showOnlyLayout(null);
            remove_um_layout.setVisibility(View.VISIBLE);
            loadUMData("0");
        });
    }

    private void setupRemoveUMWorkType() {
        device_info.setText("Remove From Under Maintenance");
        e_remarks.setHint("Add Remark");
        postSetup(() -> {
            clearFormData();
            showOnlyLayout(null);
            remove_um_layout.setVisibility(View.VISIBLE);
            loadUMData("1");
        });
    }
    private void showAddVehicleConfirmationDialog(String serialWithReg, String reg_no,String status,String activity_type, String um_contact_person_name, String um_contact_person_phone) {
        DialogUtils.showConfirmationDialog(
                getActivity(), // Activity context
                "Confirm Addition",
                "Are you sure you want to add the selected vehicle(s)?",
                () -> {
                    progressDialog.show();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        observeViewModelAddVehicle(serialWithReg, reg_no, status, activity_type, um_contact_person_name, um_contact_person_phone);
                    });
                }

        );
    }

    private void observeViewModelAddVehicle(String serialWithReg, String regNo,String status,String activity_type, String um_contact_person_name, String um_contact_person_phone) {
        viewModelVehicleUM.addInUM(mainClientId, clientId, clientLocId,status, regNo,activity_type,s_time,t_install_date.getText().toString(),serialWithReg,"",um_contact_person_name,um_contact_person_phone,e_remarks.getText().toString(),user_id)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response.getType() == 1) {
                        Toast.makeText(getActivity(), "" + response.getMsg(), Toast.LENGTH_SHORT).show();
                        //loadUMData();
                        device_detail_list_receive.clearChoices();
                        value_name_um.clear();
                        list_change_values_um.clear();

                        // ✅ Notify adapter to reset UI immediately
                        if (umVehicleAdapter != null) {
                            umVehicleAdapter.notifyDataSetChanged();
                        }
                        // ✅ Reload the fresh list
                        if(status.equalsIgnoreCase("1")){
                            loadUMData("0");
                        }else {
                            loadUMData("1");
                        }
                        e_remarks.setText("");
                        et_um_contact_person_name.setText("");
                        et_um_contact_person_phone.setText("");

                    } else {
                        Toast.makeText(getActivity(), "" + response.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                });
    }


    private void observeViewModels() {
        viewModelMainClient.getMainClientRepository().observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                progressDialog.hide();
                return;
            }
            if (response.getType() == 1) {
                mainclientList.clear();
                mainclientList.addAll(response.getMain_client_list());
                mainClientDetail.clear();
                mainClientDetail.add("SELECT CLIENT");
                for (MainClientList client : mainclientList) {
                    mainClientDetail.add(client.getClient_Name());
                }
                adapter = new ArrayAdapter<>(getContext(), R.layout.simple_custom_spinner_item, mainClientDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                new_main_clients.setAdapter(adapter);
            } else {
                mainClientDetail.clear();
                mainClientDetail.add("NO DATA AVAILABLE");
                Toast.makeText(getContext(), "Failed to load main clients", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        });

        viewModelSubClient.getSubClientLiveData().observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                Toast.makeText(getActivity(), "Null response from server", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
                return;
            }
            if (response.getType() == 1) {
                clientList.clear();
                clientList.addAll(response.getClientList());
                clientDetail.clear();
                clientDetail.add("SELECT CLIENT");
                for (ClientDetails client : clientList) {
                    clientDetail.add(client.getClient_Name());
                }
                adapter = new ArrayAdapter<>(getContext(), R.layout.simple_custom_spinner_item, clientDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                client.setAdapter(adapter);
            } else {
                clientDetail.clear();
                clientDetail.add("NO DATA AVAILABLE");
            }
            progressDialog.dismiss();
        });

        viewModelClientLocation.getClientLocationLiveData().observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                Toast.makeText(getActivity(), "Null response from server", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
                return;
            }

            if (response.getType() == 1) {
                locationList.clear();
                locationList.addAll(response.getClientLoc());
                locationDetail.clear();
                locationDetail.add("SELECT LOCATION");
                for (ClientLocationDetail loc : locationList) {
                    locationDetail.add(loc.getLoc_Name());
                }
                adapter = new ArrayAdapter<>(getContext(), R.layout.simple_custom_spinner_item, locationDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                location.setAdapter(adapter);
            } else {
                locationDetail.clear();
                locationDetail.add("NO DATA AVAILABLE");
            }
            progressDialog.dismiss();
        });

    }

    private void initViewModels() {
        viewModelMainClient = new ViewModelProvider(this).get(ViewModelMainClient.class);
        viewModelSubClient = new ViewModelProvider(this).get(ViewModelSubClient.class);
        viewModelClientLocation = new ViewModelProvider(this).get(ViewModelClientLocation.class);
        viewModelVehicleUM = new ViewModelProvider(this).get(ViewModelUM.class);
    }

    private void getAccSerialNo(String s_reg_no) {

        newInstallmentController.reqeuestAccVtsDetails(db_name,server_name,s_reg_no, this);
    }

    private void addMainClients() {
        progressDialog.show();
        viewModelMainClient.getMainClientRepository();
    }
    private void loadUMData(String number) {
        progressDialog.show();
        observeViewModelUmVehicles(number);
    }
    private void observeViewModelUmVehicles(String number) {
        viewModelVehicleUM.getUmRepository(mainClientId, clientId, clientLocId, number)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response.getType() == 1) {
                        try {
                            list_change_values_um = response.getUm_vehicles();
                            value_name_um.clear();

                            if (list_change_values_um != null && list_change_values_um.size() > 0) { // ← FIXED
                                for (int i = 0; i < list_change_values_um.size(); i++) {
                                    String regNo = list_change_values_um.get(i).reg_no;
                                    value_name_um.add((i + 1) + ". " + regNo);
                                }
                                umVehicleAdapter = new ArrayAdapter<>(getActivity(),
                                        R.layout.simple_custom_list_item, value_name_um);
                                device_detail_list_receive.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                                device_detail_list_receive.setAdapter(umVehicleAdapter);
                                device_detail_list_receive.setVisibility(View.VISIBLE);
                            } else {
                                device_detail_list_receive.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        device_detail_list_receive.setVisibility(View.GONE);
                    }
                    progressDialog.hide(); // ← moved outside, always hides
                });
    }

    public void getSerialNo() {
        progressDialog.show();
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
        cammera.setOnClickListener(v -> {
            buttonPressed = "1";
            if (activity == null) return;

            if (!ImageUtil.checkAndRequestCameraPermission(activity)) {
                return; // wait for user to grant permission
            }

            File photoFile = ImageUtil.getOutputMediaFile("MyAppImages");
            if (photoFile == null) {
                Log.e("Camera", "Error creating image file");
                return;
            }
            openCameraIntent();

            alertDialogBuilder.dismiss();
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
            ImageUtil.compressImage(path);
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
        File photoFile = ImageUtil.createImageFile(requireContext());
        if (photoFile != null) {
            path = photoFile.getAbsolutePath(); // ✅ Store here
            uri = FileProvider.getUriForFile(
                    requireContext(),
                    "in.eoninfotech.eontechnician.provider",
                    photoFile
            );

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
        } else {
            Toast.makeText(requireContext(), "Failed to create file", Toast.LENGTH_SHORT).show();
        }
    }

    private void getVTSDetails() {
        newInstallmentController.reqeuestVtsDetails(vts_id,server_name,db_name, this);
    }

    private void getVTSDetail() {
        newInstallmentController.reqeuestVtsDetail(vts_id, server_name,db_name,this);
    }

    public void removal_type() {
        newInstallmentController.requestRemovalActivityResponse(this);
    }

    public void addVehNotAvailReason() {
        newInstallmentController.requestVehNotAvailReasonResponse(this);
    }

    public void getSimOperator() {
        newInstallmentController.reqeuestSimOperatorResponse(this);
    }

    public void getSimReasonList() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestSimReplaceResponse(this);
    }

    public void getFaultList() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestFaultList(this);
    }

    public void getPhoneSupportList() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestDisconnection(this);
    }

    public void getItemCollectList() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestCollectedItemList(this);
    }

    public void addReasonRemove() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestRemovalReason(this);
    }

    public void fetchReasons() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestReplaceReason(this);
    }

    private void addActivity() {
        ShowProgressBar(true);
        newInstallmentController.notAvailActivityResponse(this);
    }

    private void addWorkType() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestworkType(this);
    }

    public void addVehType() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestvehicleType(this);
    }

    public void damageReason() {
        newInstallmentController.reqeuestDamageReason(this);
    }

    private void pMethod() {
        newInstallmentController.reqeuestPMethod(this);
    }


    private void updateInstallationData() {
        progressDialog.show();

        // ─── Build request object ─────────────────────────────────────
        in.eoninfotech.eontechnician.model.InstallationRequest req = in.eoninfotech.eontechnician.builders.InstallationDataBuilder.build(
                user_id, s_date, s_Time,
                clientId, clientLocId, is_demo,
                s_work_id, s_vts_type, is_drs,
                device_type, s_e_device_id, s_new_device_id,
                s_old_serial_no, serial_no, s_reg_no,
                s_vehicletype, s_drs_id, s_new_drs_id,
                drs_dirction, mgt_set, ignition_sensor,
                fuel_sensor, door_sensor, panic,
                cut_off, s_reason_repla, removal_type,
                removalReason, disconnection_reason,
                missing_type, missing_reason,
                not_available_activity, not_available_reason,
                collection_date, collection_type,
                collection_amount, payment_type,
                contact_person, contact_no,
                sim_provider, old_sim_no, new_sim_no,
                sim_reason, veh_condition, s_remarks,
                itemsCollected, others, fuel_voltage,
                lid_status, trans, temp_sensor,
                tilt_sensor, fuel_status, panic_status,
                sen_vehicle_no, sensor_old_veh_no,
                missDeviceType, drsStatus, replace_type,
                device_working_status, sensor_working_status,
                mainClientId, s_cust_type);

        Map<String, RequestBody> bodyMap = req.toRequestBodyMap();

        // ─── Build image part ─────────────────────────────────────────
        MultipartBody.Part imagePart = buildImagePart();

        // ─── Submit ───────────────────────────────────────────────────
        newInstallmentController.postInstallationsData(
                bodyMap.get("technician_id"),
                bodyMap.get("activity_date"),
                bodyMap.get("activity_time"),
                bodyMap.get("customer"),
                bodyMap.get("customer_location"),
                bodyMap.get("isDemo"),
                bodyMap.get("activity_type"),
                bodyMap.get("vts_type"),
                bodyMap.get("deviceType"),
                bodyMap.get("old_device_id"),
                bodyMap.get("new_device_id"),
                bodyMap.get("old_serial_no"),
                bodyMap.get("new_serial_no"),
                bodyMap.get("reg_no"),
                bodyMap.get("veh_type"),
                bodyMap.get("is_DRS"),
                bodyMap.get("old_drs"),
                bodyMap.get("new_drs"),
                bodyMap.get("drs_direction"),
                bodyMap.get("Mgt_set"),
                bodyMap.get("ignSensor"),
                bodyMap.get("fuelSensor"),
                bodyMap.get("doorSensor"),
                bodyMap.get("panic_button"),
                bodyMap.get("cutOff"),
                bodyMap.get("replacement_reason"),
                bodyMap.get("removalType"),
                bodyMap.get("removeReason"),
                bodyMap.get("disconnectReason"),
                bodyMap.get("missingType"),
                bodyMap.get("missingReason"),
                bodyMap.get("notAvailActivity"),
                bodyMap.get("notAvailReason"),
                bodyMap.get("collectionDate"),
                bodyMap.get("collectionType"),
                bodyMap.get("collectionAmount"),
                bodyMap.get("paymentType"),
                bodyMap.get("contactPerson"),
                bodyMap.get("contactNo"),
                bodyMap.get("simProvider"),
                bodyMap.get("oldSimNo"),
                bodyMap.get("newSimNo"),
                bodyMap.get("simReason"),
                bodyMap.get("veh_Condition"),
                bodyMap.get("remarks"),
                bodyMap.get("itemCollected"),
                bodyMap.get("faults_checked"),
                bodyMap.get("fuel_reading"),
                bodyMap.get("lid_statu"),
                bodyMap.get("tran"),
                bodyMap.get("temp_senso"),
                bodyMap.get("tilt_senso"),
                bodyMap.get("fuel_statu"),
                bodyMap.get("panic_statu"),
                bodyMap.get("sensor_veh_n"),
                bodyMap.get("sensor_old_veh_n"),
                bodyMap.get("remove_type"),
                bodyMap.get("drs_status"),
                bodyMap.get("replacetype"),
                bodyMap.get("deviceworkingstatus"),
                bodyMap.get("sensorworkingstatus"),
                bodyMap.get("mainclientid"),
                bodyMap.get("cust_type"),
                imagePart,
                this);
    }

    // ─── Image builder extracted from updateInstallationData ─────────
    private MultipartBody.Part buildImagePart() {
        if (buttonPressed.equals("0")) {
            return null;
        } else if (buttonPressed.equals("1")) {
            try {
                File file = ImageUtil.bitmapToFile(requireContext(), bmp, "image_call");
                ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
                return MultipartBody.Part.createFormData("image", file.getName(), fileBody);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else if (buttonPressed.equals("2")) {
            try {
                File file = new File(path);
                ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
                return MultipartBody.Part.createFormData("image", path, fileBody);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
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

    private void clearFormData() {

        // ── Variable resets ───────────────────────────────────────────
        s_vts_type          = "SELECT VTS TYPE";
        s_e_device_id       = "0";
        s_new_device_id     = "0";
        s_vehicletype       = "0";
        s_device_id         = "0";
        s_reg_no            = "0";
        is_drs              = "N";
        s_drs_id            = "0";
        s_new_drs_id        = "0";
        drs_dirction        = "N";
        s_reason_repla      = "0";
        removalReason       = "0";
        itemsCollected      = "0";
        others              = "";
        s_remarks           = "0";
        disconnection_reason = "0";
        ignition_sensor     = "N";
        fuel_sensor         = "N";
        door_sensor         = "N";
        veh_condition       = "W";
        mgt_set             = "N";
        sim_provider        = "0";
        old_sim_no          = "0";
        new_sim_no          = "0";
        sim_reason          = "0";
        not_available_reason = "0";
        not_available_activity = "0";
        is_demo             = "N";
        collection_amount   = "0";
        collection_date     = "0";
        collection_type     = "0";
        missing_reason      = "0";
        removal_type        = "0";
        fuel_voltage        = "0";
        buttonPressed       = "0";
        image               = "";
        s_cust_type         = "";

        // ── EditText resets ───────────────────────────────────────────
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
        accessory_sr_no.setText("");
        accessory_reg_no.setText("");
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

        // ── Radio button resets ───────────────────────────────────────
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

    private void reloadDropdownsForWorkType(String workId) {
        if (workId == null || workId.isEmpty()) return;

        // These are needed by ALL work types
        getDeviceTypes();
        getSerialNo();

        switch (workId) {
            case "1": // Fault
                getFaultList();
                addVehType();
                break;

            case "2": // Installation
                addVehType();
                getDevice();
                break;

            case "3": // Replacement
                fetchReasons();
                break;

            case "4": // ReInstallation
                addVehType();
                break;

            case "5": // Removal
                removal_type();
                addReasonRemove();
                getItemCollectList();
                break;

            case "6": // Phone Support
                getPhoneSupportList();
                break;

            case "7": // Sim Replacement
                getSimReasonList();
                getSimOperator();
                break;

            case "8": // Missing Device
                damageReason();
                break;

            case "9": // Vehicle Not Available
                addActivity();
                addVehNotAvailReason();
                break;

            case "10": // Payment
                pMethod();
                break;

            case "11": // Other Work
                // no dropdowns needed
                break;

            case "12": // Add UM
            case "13": // Remove UM
                // UM data loaded separately via loadUMData()
                break;
        }
    }

    // ─── Full reset WITH API reloads (called on work type change) ────
    public void clearData() {
        reloadDropdownsForWorkType(s_work_id);
        clearFormData();    // ✅ then reset all fields
    }

    // ─── UI reset only, NO API reloads (called on device type change)
    public void clearTextView() {
        clearFormData();    // ✅ just field reset, no API calls
    }



    @Override
    public void clientResponse(ClientResponse response) {
    }

    @Override
    public void locationResponse(ClientLocationResponse response) {
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
    public void damageResponse(DamageResponse response) {
        try {
            damageList = response.getDamageLists();
            try {
                try {
                    removalDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                removalDetail.add("SELECT REASON");
                for (int i = 0; i < damageList.size(); i++) {
                    removalDetail.add(damageList.get(i).getDamage_Name());
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
                if (!collected_items.isEmpty()) {
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
                if (!simreplacereason.isEmpty()) {
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
                if (!vehNotAvailReasonDetails.isEmpty()) {
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
        try {
            Toast.makeText(getContext(), "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            if (response.getType() == 1) {
                circularRelative.setVisibility(View.GONE);
                progressDialog.hide();

                clearFormData();  // ← reset fields only, NO API calls

                // clear list selections
                for (int i = 0; i < lvItem.getCount(); i++) {
                    View view = lvItem.getChildAt(i);
                    if (view != null) {
                        CheckedTextView mCheckBox = view.findViewById(R.id.text1);
                        if (mCheckBox != null) mCheckBox.setChecked(false);
                        lvItem.clearChoices();
                    }
                }
                for (int i = 0; i < lv.getCount(); i++) {
                    View view = lv.getChildAt(i);
                    if (view != null) {
                        CheckedTextView mCheckBox = view.findViewById(R.id.text1);
                        if (mCheckBox != null) mCheckBox.setChecked(false);
                        lv.clearChoices();
                    }
                }

                // reload only what current work type needs
                reloadDropdownsForWorkType(s_work_id);

            } else {
                progressDialog.hide();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mainClientResponse(MainResponse response) {
    }

    @Override
    public void vtsAccResponses(MainResponse response) {
        accessory_sr_no.setText(response.getSerial_no());
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

    public void getDeviceTypes() {
        if (deviceTypesLoaded) return;
        ApiHolder get_list = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
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
                            deviceTypesLoaded = true;
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
                DialogUtils.showFailureSnack(v, "Something went wrong!");
            }
        });
    }

    public void getDevice() {
        ApiHolder get_list = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
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
                DialogUtils.showFailureSnack(v, "Something went wrong!");
            }
        });
    }

    @Override
    public void receiveDeviceResponse(MainResponse response) {

        progressDialog.hide();
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
                    //srNoDetails.add(srnoList.get(i).getPcb_sr_no());
                    srNoDetails.add(srnoList.get(i).getPcb_sr_no()+srnoList.get(i).getCust_type());
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
                spn_replace_sensor.setAdapter(adapter);
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

    private void showOnlyLayout(LinearLayout layoutToShow) {

        // All layouts that should be hidden by default
        LinearLayout[] allLayouts = {
                linearInstall,
                linearReInstall,
                linearRemoval,
                linearFault,
                linearPhoneSupport,
                linearReplacement,
                linearSimRepalace,
                linearDeviceMissing,
                linearVehicleNotAvail,
                linearPayment,
                linearOthers,
                linearDrs,
                drsInstall,
                drsReInstall
        };

        // Hide all layouts
        for (LinearLayout layout : allLayouts) {
            layout.setVisibility(View.GONE);
        }

        // Show only the requested one
        if (layoutToShow != null) {
            layoutToShow.setVisibility(View.VISIBLE);
        }

        // These always reset regardless of work type
        magnet_set.setVisibility(View.GONE);
        magnetset_install.setVisibility(View.GONE);
        relativeCableConnected.setVisibility(View.GONE);
        remove_um_layout.setVisibility(View.GONE);
        vehDetail.setVisibility(View.GONE);
        faultDetail.setVisibility(View.GONE);
        imageName.setText("");
        imageNameMissing.setText("");
        imageNameFault.setText("");
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!hasInitialized) {
            hasInitialized = true;
            addMainClients();   // ← only fires once ever
            addWorkType();      // ← only fires once ever
        }
    }
}

