package in.eoninfotech.eontechnician.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import in.eoninfotech.eontechnician.ImageCompressionAsyncTask;
import in.eoninfotech.eontechnician.ImageUtils;
import in.eoninfotech.eontechnician.Responses.MainResponse;
import in.eoninfotech.eontechnician.Responses.PMethodDetail;
import in.eoninfotech.eontechnician.Responses.PaymentMethodResponse;
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
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.helper.FileUtils;
import in.eoninfotech.eontechnician.helper.ProgressRequestBody;
import in.eoninfotech.eontechnician.salesteam.BillingDetailActivity;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.view.MyTextView;
import io.netopen.hotbitmapgg.library.view.RingProgressBar;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static pub.devrel.easypermissions.EasyPermissions.hasPermissions;

public class NewInstallmentFragmentBackup extends Fragment implements ClientListener,ProgressRequestBody.UploadCallbacks {

    View v, view;
    private final int SELECT_PHOTO = 1;
    String path;
    int REQUEST_CODE_PERMISSION = 10;
    public static final String IMAGE_DIRECTORY_NAME = "android_file";
    public static final int MEDIA_TYPE_IMAGE = 1;
    private int REQUEST_CAMERA = 0;
    private int GALLERY_KITKAT_INTENT_CALLED = 300;
    private int REQUEST_CODE_GALLERY = 200;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    int PERMISSION_ALL = 1;
    int pStatus = 0;
    ProgressBar mProgress;
    private Handler handler = new Handler();
    TextView tv;
    File file;
    Uri uri;
    String buttonPressed="0";
    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    NewInstallmentController newInstallmentController;
    TextInputLayout told_drsid,tnew_drsid,t_drs_veh_no,t_drs_vts_id;
    String clientId,clientLocId,s_Vehicle_Name,drsStatus,device_type="0",s_date,s_time,disttid,s_remove_reason,vts_id,drs_id,new_drs_id,uusername, version,selected_todate,selected_totime,current_date,fuel_sensor="0",door_sensor="0",veh_condition="0",mgt_set="0",sim_provider="0",old_sim_no="0",new_sim_no="0",not_available_activity="0",not_available_reason="0",is_demo="0";
    CheckConnection chk;
    CheckBox check_tel_supprt,magnet_set,magnetset_install;
    EditText e_reg_no, e_device_id, e_drs_id, e_remarks, old_deviceid,new_deviceid,fault_vts_id,t_install_date,t_install_Time,new_vehicleRegNo,remove_deviceid,remove_reg_no,old_deviceidreplace,new_deviceidReinstall,old_drsid,new_drsid,phsupport_vts_id,fault_reg_no,phSupport_reg_no,regNo,
            maintenance_vts_id,maintenance_reg_no,drs_vts_id,drs_veh_no,sim_vts_id,e_old_sim_no,e_new_sim_no,sim_vehicle_no,mDevice_vts_id,mDevice_reg_no,vehNotAvailVtsID,vehNotAvailRegNo,paymentDate,amount;
    MyTextView device_info;
    TextView plantName,imageName;
    String s_reg_no,s_device_id,s_drs_id,s_new_drs_id,s_clientname="SELECT CLIENT",s_remarks,status,s_work_type,s_Time,s_vehicletype,s_VehicleTypeInst,s_reason_repla="0",removalReason="0",itemsCollected="",others="",tel_support="",s_work_id,s_new_device_id,s_e_device_id="0",is_drs="0",drs_dirction="0",disconnection_reason="0",ignition_sensor="0",sim_reason="0",missing_reason;
    Button update_dataa,imageUpload;
    Calendar calen = Calendar.getInstance();
    int year, month, day ,hour,minutes;
    NonScrollListView lv,lvItem;
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
    ArrayList<VTSDetail> vtsList = new ArrayList<>();
    ArrayList<PMethodDetail> paymentmethod = new ArrayList<>();
    ArrayList<VehicleTypeDetail> vehicleList = new ArrayList<>();
    ArrayList<ClientLocationDetail> locationList = new ArrayList<>();
    ArrayList<String> clientDetail = new ArrayList<>();
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
    ArrayAdapter<String> adapter;
    String[] arraySpinner = new String[] {
            "Select Activity", "Fault Repair", "New Installation", "Replacement", "Reinstallation", "Removal/Collection", "Phone Support",
            "Sim Replacement","Missing Device","Vehicle Not Available","Payment Collection","Other Work"};
    ProgressDialog pDialog;
    ProgressBar progressBar,circularProgressbar;
   // RingProgressBar progressBar;
    LinearLayout linearOthers,drsReInstall,relaydrsType,drsReplacemsg,relaydrsTypeReplace,linearDoor,linearIgnition,drsInstall,linearPayment,linearvts,linearMaintenance,linearVehicleNotAvail,vehDetail;
    RelativeLayout relativeCableConnected,relayLocation,relMissing,circularRelative;
    MySearchableSpinner client,vehicleType,workType,location,reason_replace,reason_remove,new_in_vehicleTypeReins,discReason,sim_replace_reason,sim_operator,vehiclenoavailSpinner,notAvailReason,removalType,missingType,payment_method;
    LinearLayout linearReplacement,linearInstall,linearReInstall,linearRemoval,linearDrs,linearFault,linearPhoneSupport,linearSimRepalace,linearDeviceMissing;
    RadioGroup radioGroup,radioGroupReinstall,drsReplace,radiodeviceType,radiodireplace,radiogroup,radiogroupDoor,is_Demo,radiodrsReInstall,radiodrsInstall,radioGroups,radioMainReason,radioGroupMissing;
    RadioButton radionormal,radiotype,old_Device,new_Device,radionormalrep,l_in,doorNo,drsType,drsReeInstall,is_demo_no,damageDevice;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String removal_type, baseImage="",missing_type,collection_amount,collection_date,collection_type,collection_image;
    Bitmap bmp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_new_install, container, false);
        chk = new CheckConnection(v.getContext());
        sharedprefs = getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        uusername = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        Log.i("****stat dist n usr***", version + " " + uusername);
        e_device_id = (EditText) v.findViewById(R.id.new_in_deviceid);
        e_drs_id = (EditText) v.findViewById(R.id.new_in_drs_id);
        e_remarks = (EditText) v.findViewById(R.id.new_in_remarks);
        e_reg_no = (EditText) v.findViewById(R.id.new_in_reg_no);
        linearOthers = v.findViewById(R.id.linearOthers);
        amount = (EditText)v.findViewById(R.id.amount);
        client = (MySearchableSpinner) v.findViewById(R.id.new_in_clients);
        vehicleType = (MySearchableSpinner) v.findViewById(R.id.new_in_vehicleType);
        update_dataa = (Button) v.findViewById(R.id.new_in_update);
        radioGroup = (RadioGroup) v.findViewById(R.id.radiodirec);
        radioGroupReinstall = (RadioGroup) v.findViewById(R.id.radioGroupReinstall);
        radiodeviceType = (RadioGroup) v.findViewById(R.id.radiodeviceType);
        t_install_date = (EditText) v.findViewById(R.id.installDate);
        paymentDate = (EditText)v.findViewById(R.id.paymentDate);
        t_install_Time = (EditText) v.findViewById(R.id.installTime);
        vehNotAvailVtsID = (EditText)v.findViewById(R.id.vehNotAvailVtsID);
        vehNotAvailRegNo = (EditText)v.findViewById(R.id.vehNotAvailRegNo);
        workType = (MySearchableSpinner) v.findViewById(R.id.new_in_workType);
//        maintenance_vts_id = (EditText) v.findViewById(R.id.maintenance_vts_id);
//        maintenance_reg_no = (EditText) v.findViewById(R.id.maintenance_reg_no);
        linearReplacement = (LinearLayout) v.findViewById(R.id.linearReplacement);
        linearInstall = (LinearLayout) v.findViewById(R.id.linearInstall);
        linearReInstall = (LinearLayout) v.findViewById(R.id.linearReInstall);
        linearVehicleNotAvail = (LinearLayout)v.findViewById(R.id.linearVehicleNotAvail);
        vehDetail = (LinearLayout)v.findViewById(R.id.vehDetail);
        linearPayment = (LinearLayout)v.findViewById(R.id.linearPayment);
        relaydrsType = (LinearLayout) v.findViewById(R.id.relaydrsType);
        linearDeviceMissing = (LinearLayout) v.findViewById(R.id.linearDeviceMissing);
        relayLocation = (RelativeLayout) v.findViewById(R.id.relayLocation);
        relMissing = (RelativeLayout)v.findViewById(R.id.relMissing);
        imageUpload = (Button)v.findViewById(R.id.imageUpload);
        imageName = (TextView)v.findViewById(R.id.imageName);
        relativeCableConnected = (RelativeLayout) v.findViewById(R.id.relativeCableConnected);
        t_drs_veh_no = (TextInputLayout) v.findViewById(R.id.t_drs_veh_no);
        t_drs_vts_id = (TextInputLayout) v.findViewById(R.id.t_drs_vts_id);
        location = (MySearchableSpinner) v.findViewById(R.id.new_in_locations);
        drsReInstall = (LinearLayout) v.findViewById(R.id.drsReInstall);
        old_deviceid = (EditText) v.findViewById(R.id.old_deviceid);
        new_deviceid = (EditText) v.findViewById(R.id.new_deviceid);
        told_drsid = (TextInputLayout) v.findViewById(R.id.t_old_drsid);
        tnew_drsid = (TextInputLayout) v.findViewById(R.id.tnew_drsid);
        new_vehicleRegNo = (EditText) v.findViewById(R.id.new_vehicleRegNo);
        linearRemoval = (LinearLayout) v.findViewById(R.id.linearRemoval);
        linearPhoneSupport = (LinearLayout) v.findViewById(R.id.linearPhoneSupport);
       // linearMaintenance = (LinearLayout) v.findViewById(R.id.linearMaintenance);
        reason_replace = (MySearchableSpinner) v.findViewById(R.id.replace_reason);
        reason_remove = (MySearchableSpinner) v.findViewById(R.id.removal_Reason);
        missingType = (MySearchableSpinner)v.findViewById(R.id.missingType);
        discReason = (MySearchableSpinner) v.findViewById(R.id.discReason);
        payment_method = (MySearchableSpinner)v.findViewById(R.id.payment_method);
        vehiclenoavailSpinner = (MySearchableSpinner)v.findViewById(R.id.vehiclenoavailSpinner);
        notAvailReason = (MySearchableSpinner)v.findViewById(R.id.notAvailReason);
        removalType = (MySearchableSpinner)v.findViewById(R.id.removalType);
        sim_replace_reason = (MySearchableSpinner) v.findViewById(R.id.sim_replace_reason);
        sim_operator = (MySearchableSpinner) v.findViewById(R.id.sim_operator);
        drs_vts_id = (EditText) v.findViewById(R.id.drs_vts_id);
        drs_veh_no = (EditText) v.findViewById(R.id.drs_veh_no);
        lv = (NonScrollListView) v.findViewById(R.id.other_list);
        lvItem = (NonScrollListView) v.findViewById(R.id.item_list);
        regNo = (EditText) v.findViewById(R.id.regNo);
        plantName = (TextView) v.findViewById(R.id.plantName);
        sim_vts_id = (EditText) v.findViewById(R.id.sim_vts_id);
        sim_vehicle_no = (EditText) v.findViewById(R.id.sim_vehicle_no);
        imageUpload = (Button)v.findViewById(R.id.imageUpload);
        e_old_sim_no = (EditText) v.findViewById(R.id.old_sim_no);
        e_new_sim_no = (EditText) v.findViewById(R.id.new_sim_no);
        circularRelative = (RelativeLayout)v.findViewById(R.id.circularRelative);
        mDevice_vts_id = (EditText) v.findViewById(R.id.mDevice_vts_id);
        mDevice_reg_no = (EditText) v.findViewById(R.id.mDevice_reg_no);
        linearDrs = (LinearLayout) v.findViewById(R.id.linearDrs);
        remove_deviceid = (EditText) v.findViewById(R.id.remove_deviceid);
        remove_reg_no = (EditText) v.findViewById(R.id.remove_reg_no);
        fault_reg_no = (EditText) v.findViewById(R.id.fault_reg_no);
        phSupport_reg_no = (EditText) v.findViewById(R.id.phSupport_reg_no);
        linearFault = (LinearLayout) v.findViewById(R.id.linearFault);
        linearSimRepalace = (LinearLayout) v.findViewById(R.id.linearSimRepalace);
        linearvts = (LinearLayout) v.findViewById(R.id.linearvts);
        device_info = (MyTextView) v.findViewById(R.id.device_info);
        old_deviceidreplace = (EditText) v.findViewById(R.id.old_deviceidreplace);
        old_Device = (RadioButton) v.findViewById(R.id.old_Device);
        new_Device = (RadioButton) v.findViewById(R.id.new_Device);
        radioGroups = (RadioGroup) v.findViewById(R.id.radioGroup);
       // radioMainReason = (RadioGroup) v.findViewById(R.id.radioMainReason);
        new_in_vehicleTypeReins = (MySearchableSpinner) v.findViewById(R.id.new_in_vehicleTypeReins);
        l_in = (RadioButton) v.findViewById(R.id.l_in);
        new_deviceidReinstall = (EditText) v.findViewById(R.id.new_deviceidReinstall);
        old_drsid = (EditText) v.findViewById(R.id.old_drsid);
        new_drsid = (EditText) v.findViewById(R.id.new_drsid);
        radiodrsReInstall = (RadioGroup) v.findViewById(R.id.radiodrsReInstall);
        radiogroupDoor = (RadioGroup) v.findViewById(R.id.radiogroupDoor);
        radioGroupMissing = (RadioGroup)v.findViewById(R.id.radiogroupMissing);
        is_Demo  =(RadioGroup)v.findViewById(R.id.is_demo);
        phsupport_vts_id = (EditText) v.findViewById(R.id.phsupport_vts_id);
        relaydrsTypeReplace = (LinearLayout) v.findViewById(R.id.relaydrsTypeReplace);
        drsReplace = (RadioGroup) v.findViewById(R.id.radiodrsReplace);
        drsReplacemsg = (LinearLayout) v.findViewById(R.id.drsReplace);
        drsInstall = (LinearLayout) v.findViewById(R.id.drsInstall);
        radiodrsInstall = (RadioGroup) v.findViewById(R.id.radiodrsInstall);
        radiodireplace = (RadioGroup) v.findViewById(R.id.radiodireplace);
        radiogroup = (RadioGroup) v.findViewById(R.id.radiogroup);
        check_tel_supprt = (CheckBox) v.findViewById(R.id.check_tel_supprt);
        magnet_set = (CheckBox) v.findViewById(R.id.magnet_set);
        magnetset_install = (CheckBox) v.findViewById(R.id.magnetset_install);
        fault_vts_id = (EditText) v.findViewById(R.id.fault_vts_id);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        circularProgressbar = (ProgressBar)v.findViewById(R.id.circularProgressbar);
       // progressBar = (RingProgressBar) v.findViewById(R.id.progressBar);
        linearDoor = (LinearLayout) v.findViewById(R.id.linearDoor);
        linearIgnition = (LinearLayout) v.findViewById(R.id.linearIgnition);
        doorNo = (RadioButton) v.findViewById(R.id.doorNo);
        is_demo_no = (RadioButton)v.findViewById(R.id.is_demo_no);
        damageDevice = (RadioButton)v.findViewById(R.id.damageDevice);
        t_install_Time.setInputType(InputType.TYPE_NULL);
        t_install_date.setInputType(InputType.TYPE_NULL);
        paymentDate.setInputType(InputType.TYPE_NULL);

        newInstallmentController = new NewInstallmentController();

        ShowProgressBar(false);
        Progress(false);
        addclients();addLocation();addVehType();addWorkType();setDateAndTime();
        location.setEnabled(false);
        workType.setEnabled(false);
        if (chk.isConnected()) {
        } else {
            chk.showConnectionErrorDialog();
        }
        e_remarks.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (e_remarks.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });
        client.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }clientId = String.valueOf(clientList.get(i).getClient_Id());
                s_clientname = clientList.get(i).getClient_Name();
                location.setEnabled(true);
                addLocation();
                drsStatus = String.valueOf(clientList.get(i).getDrs_status());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        removalType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }removal_type = String.valueOf(removalActivityDetails.get(i).getA_id());
                remove_reg_no.setText("");
                remove_deviceid.setText("");
                e_remarks.setText("");
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
                }collection_type = String.valueOf(paymentmethod.get(i).getId());
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
                }disconnection_reason = String.valueOf((supportList.get(i).getId()));
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
                }clientLocId = String.valueOf((locationList.get(i).getLoc_Id()));
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
                }removalReason = String.valueOf((removalList.get(i).getRemoval_Id()));
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
                }missing_reason = String.valueOf((damageList.get(i).getRemoval_Id()));
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
                }sim_reason = simreplacereason.get(i).getS_id();
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
                }sim_provider = simOperatorDetails.get(i).getSp_id();
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
                }s_vehicletype = String.valueOf(vehicleList.get(i).getVehicle_Id());
                s_Vehicle_Name = vehicleList.get(i).getVehicle_Name();
                if (s_vehicletype.equalsIgnoreCase("2")) {
                    drsInstall.setVisibility(View.VISIBLE);
                } else {
                    drsInstall.setVisibility(View.GONE);
                }
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
                }s_vehicletype = String.valueOf(vehicleList.get(i).getVehicle_Id());
                s_Vehicle_Name = vehicleList.get(i).getVehicle_Name();
                if (s_vehicletype.equalsIgnoreCase("2")) {
                    drsReInstall.setVisibility(View.VISIBLE);
                } else {
                    drsReInstall.setVisibility(View.GONE);
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
                }not_available_activity = String.valueOf(notAvailActivityDetails.get(i).getId());
                if(not_available_activity.equals("1")||(not_available_activity.equals("2"))){
                    vehDetail.setVisibility(View.GONE);s_vehicletype="0";device_type="0";
                }else{
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
                }not_available_reason = vehNotAvailReasonDetails.get(i).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        workType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }s_work_type = workType.getSelectedItem().toString();
                if (s_work_type.equals("Fault Repair")) {
                    s_work_id = "1";
                } else if (s_work_type.equals("New Installation")) {
                    s_work_id = "2";
                } else if (s_work_type.equals("Replacement")) {
                    s_work_id = "3";
                } else if (s_work_type.equals("Reinstallation")) {
                    s_work_id = "4";
                } else if (s_work_type.equals("Removal/Collection")) {
                    s_work_id = "5";
                } else if (s_work_type.equals("Phone Support")) {
                    s_work_id = "6";
                } else if (s_work_type.equals("Sim Replacement")) {
                    s_work_id = "7";
                } else if (s_work_type.equals("Missing Device")) {
                    s_work_id = "8";
                } else if (s_work_type.equals("Vehicle Not Available")) {
                    s_work_id = "9";
                } else if(s_work_type.equals("Payment Collection")) {
                    s_work_id = "10";
                }else if(s_work_type.equals("Other Work")) {
                        s_work_id = "11";
                }else{}
                if (s_work_type.equalsIgnoreCase("Replacement")) {
                    device_info.setText("Replacement Details");
                    e_remarks.setHint("Remarks");
                    RadioButton radio = (RadioButton) v.findViewById(R.id.radioVTS);
                    radio.setChecked(true);
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clearData();
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
                            fetchReasons();
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
                            }
                            regNo.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    regNo.setText("");
                                    regNo.setTextColor(getResources().getColor(R.color.black));
                                    return false;
                                }
                            });
                        }
                    });
                    radioGroups.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            if (i == R.id.radioVTS) {
                                clearData();
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
                            } else if (i == R.id.radioDRS) {
                                clearData();
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
                            } else if (i == R.id.radioBoth) {
                                clearData();
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
                            }
                            drs_vts_id.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                }
                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
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
                            drs_veh_no.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    drs_veh_no.setText("");
                                    drs_veh_no.setTextColor(getResources().getColor(R.color.black));
                                    return false;
                                }
                            });
                        }
                    });
                } else if (s_work_type.equalsIgnoreCase("New Installation")) {
                    device_info.setText("Installation Details");
                    e_remarks.setHint("Remarks");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
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
                            addVehType();
                            ShowProgressBar(false);
                        }
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
                } else if (s_work_type.equalsIgnoreCase("Reinstallation")) {
                    device_info.setText("ReInstallation Details");
                    old_deviceid.setHint("Device ID");
                    e_remarks.setHint("Remarks");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
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
                            linearDrs.setVisibility(View.GONE);
                            linearDeviceMissing.setVisibility(View.GONE);
                            linearVehicleNotAvail.setVisibility(View.GONE);
                            vehDetail.setVisibility(View.GONE);
                            linearPayment.setVisibility(View.GONE);
                            linearOthers.setVisibility(View.GONE);
                            addVehType();
                            ShowProgressBar(false);
                        }
                    }, DELAY);
                    radioGroupReinstall.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            if (i == R.id.old_Device) {
                                new_deviceidReinstall.setVisibility(View.GONE);
                                new_deviceidReinstall.setText("");
                                old_deviceid.setHint("Device ID");
                            } else {
                                new_deviceidReinstall.setVisibility(View.VISIBLE);
                                old_deviceid.setHint("Old Device ID");
                                old_deviceid.setText("");
                                new_vehicleRegNo.setText("");
                            }
                        }
                    });
                    old_deviceid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                vts_id = old_deviceid.getText().toString();
                                getVTSDetail();
                            } else {
                            }
                        }
                    });
                    radiodrsReInstall.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            if (i == R.id.radioyesdrsReInstall) {
                                linearDrs.setVisibility(View.VISIBLE);
                                magnetset_install.setVisibility(View.VISIBLE);
                            } else {
                                linearDrs.setVisibility(View.GONE);
                                magnetset_install.setVisibility(View.GONE);
                            }
                        }
                    });
                } else if (s_work_type.equalsIgnoreCase("Removal/Collection")) {
                    device_info.setText("Removal Details");
                    e_remarks.setHint("Remarks");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
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
                            removal_type();
                            addReasonRemove();
                            getItemCollectList();
                            ShowProgressBar(false);
                        }
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
                    remove_reg_no.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            remove_reg_no.setText("");
                            remove_reg_no.setTextColor(getResources().getColor(R.color.black));
                            return false;
                        }
                    });
                } else if (s_work_type.equalsIgnoreCase("Fault Repair")) {
                    device_info.setText("Fault Details");
                    e_remarks.setHint("Remarks");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clearData();
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
                            getFaultList();
                            ShowProgressBar(false);
                        }
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
                    fault_reg_no.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            fault_reg_no.setText("");
                            fault_reg_no.setTextColor(getResources().getColor(R.color.black));
                            return false;
                        }
                    });
                } else if (s_work_type.equalsIgnoreCase("Phone Support")) {
                    device_info.setText("Phone Support");
                    e_remarks.setHint("Remarks");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
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
                            getPhoneSupportList();
                            ShowProgressBar(false);
                        }
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
                    phSupport_reg_no.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            phSupport_reg_no.setText("");
                            phSupport_reg_no.setTextColor(getResources().getColor(R.color.black));
                            return false;
                        }
                    });
                } else if (s_work_type.equalsIgnoreCase("Sim Replacement")) {
                    device_info.setText("Sim Replacement");
                    e_remarks.setHint("Remarks");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
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
                            getSimReasonList();
                            getSimOperator();
                            ShowProgressBar(false);
                        }
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
                    sim_vehicle_no.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            sim_vehicle_no.setText("");
                            sim_vehicle_no.setTextColor(getResources().getColor(R.color.black));
                            return false;
                        }
                    });
                } else if (s_work_type.equalsIgnoreCase("Missing Device")) {
                    device_info.setText("Report Missing Device");
                    e_remarks.setHint("Remarks");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
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
                            linearDeviceMissing.setVisibility(View.VISIBLE);
                            linearVehicleNotAvail.setVisibility(View.GONE);
                            vehDetail.setVisibility(View.GONE);
                            linearPayment.setVisibility(View.GONE);
                            linearOthers.setVisibility(View.GONE);
                            damageReason();
                            ShowProgressBar(false);
                        }
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
                    mDevice_reg_no.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            mDevice_reg_no.setText("");
                            mDevice_reg_no.setTextColor(getResources().getColor(R.color.black));
                            return false;
                        }
                    });
                    radioGroupMissing.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.reportMissing:
                                    relMissing.setVisibility(View.GONE);
                                    missing_type = "1";
                                    mDevice_reg_no.setText("");
                                    mDevice_vts_id.setText("");
                                    break;
                                case R.id.damageDevice:
                                    relMissing.setVisibility(View.VISIBLE);
                                    missing_type = "2";
                                    mDevice_reg_no.setText("");
                                    mDevice_vts_id.setText("");
                                    break;
                            }
                        }
                    });
                } else if (s_work_type.equalsIgnoreCase("Vehicle Not Available")) {
                    device_info.setText("Report Vehicle Not Available");
                    e_remarks.setHint("Remarks");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
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
                            vehDetail.setVisibility(View.VISIBLE);
                            linearPayment.setVisibility(View.GONE);
                            linearOthers.setVisibility(View.GONE);
                            addActivity();
                            addVehNotAvailReason();
                            ShowProgressBar(false);
                        }
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
                    vehNotAvailRegNo.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            vehNotAvailRegNo.setText("");
                            vehNotAvailRegNo.setTextColor(getResources().getColor(R.color.black));
                            return false;
                        }
                    });
                } else if (s_work_type.equalsIgnoreCase("Payment Collection")) {
                    device_info.setText("Payment Collection");
                    e_remarks.setHint("Remarks");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
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
                            linearPayment.setVisibility(View.VISIBLE);
                            linearOthers.setVisibility(View.GONE);
                            pMethod();
                            ShowProgressBar(false);
                            paymentDate.setInputType(InputType.TYPE_NULL);
                        }
                    }, DELAY);
                }else if (s_work_id.equalsIgnoreCase("11")) {
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
                        ShowProgressBar(false);
                    }, DELAY);
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
                }s_reason_repla = String.valueOf(arr_replaceReasons.get(i).getReplace_Id());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        update_dataa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s_clientname.equalsIgnoreCase("SELECT Client") || (s_clientname.equals(null))) {
                    Toast.makeText(getContext(), "Please Select Client", Toast.LENGTH_LONG).show();
                } else if (location.getSelectedItem().toString().equalsIgnoreCase("Select Location")) {
                    Toast.makeText(getContext(), "Please Select Location", Toast.LENGTH_LONG).show();
                } else if (workType.getSelectedItem().toString().equalsIgnoreCase("Select Work Type")) {
                    Toast.makeText(getContext(), "Please Select Work Type", Toast.LENGTH_LONG).show();
                } else if (s_work_type.equalsIgnoreCase("New Installation")) {
                    s_drs_id = e_drs_id.getText().toString();
                    s_reg_no = e_reg_no.getText().toString();
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_remarks = e_remarks.getText().toString();
                    if (magnetset_install.isChecked()) {
                        mgt_set = "1";
                    } else {
                        mgt_set = "0";
                    }int radioDrs = radiodrsInstall.getCheckedRadioButtonId();
                    drsType = (RadioButton)v.findViewById(radioDrs);
                    String drsTypes = drsType.getText().toString();
                    if(drsTypes.equalsIgnoreCase("Yes")&& (drsInstall.getVisibility()==View.VISIBLE)){
                        is_drs="1";
                    }else if(drsTypes.equalsIgnoreCase("No")&&(drsInstall.getVisibility()==View.VISIBLE)){
                        is_drs="2";
                    }else{
                        is_drs="0";
                    }int selectedType = radiodeviceType.getCheckedRadioButtonId();
                    radiotype = (RadioButton) v.findViewById(selectedType);
                    String type = radiotype.getText().toString();
                    if (type.equalsIgnoreCase("Voice")) {
                        device_type = "1";
                    } else {
                        device_type = "2";
                    }int isDemo = is_Demo.getCheckedRadioButtonId();
                    is_demo_no = (RadioButton) v.findViewById(isDemo);
                    String demo = is_demo_no.getText().toString();
                    if (demo.equalsIgnoreCase("No")) {
                        is_demo = "0";
                    } else {
                        is_demo = "1";
                    }int selectedIgnition = radiogroup.getCheckedRadioButtonId();
                    l_in = (RadioButton) v.findViewById(selectedIgnition);
                    String typeIgnition = l_in.getText().toString();
                    if (typeIgnition.equalsIgnoreCase("No")) {
                        ignition_sensor = "0";
                    } else {
                        ignition_sensor = "1";
                    }int selectedTypes = radiogroupDoor.getCheckedRadioButtonId();
                    doorNo = (RadioButton) v.findViewById(selectedTypes);
                    String typesensor = doorNo.getText().toString();
                    if (typesensor.equalsIgnoreCase("No")) {
                        door_sensor = "0";
                    } else {
                        door_sensor = "1";
                    }int selectedId = radioGroup.getCheckedRadioButtonId();
                    radionormal = (RadioButton) v.findViewById(selectedId);
                    String drs = radionormal.getText().toString();
                    if (drs.equalsIgnoreCase("Normal") && (linearDrs.getVisibility() == View.VISIBLE)) {
                        drs_dirction = "1";
                    } else if (drs.equalsIgnoreCase("Reverse") && (linearDrs.getVisibility() == View.VISIBLE)) {
                        drs_dirction = "2";
                    }else {drs_dirction="0";
                    }s_new_device_id = e_device_id.getText().toString();
                    if (s_new_device_id.equals("")) {
                        e_device_id.setError("Vts Id can't be null");
                    } else if (s_reg_no.equals("")||s_reg_no.equals(null)||s_reg_no.equals("0")) {
                        e_reg_no.setError("Reg No.can't be null");
                    } else if (s_work_type.equalsIgnoreCase("Select Work Type")) {
                        Toast.makeText(getContext(), "Please Select Work Type", Toast.LENGTH_LONG).show();
                    } else if ((vehicleType.getSelectedItem().toString().equalsIgnoreCase("Select Vehicle Type"))) {
                        Toast.makeText(getContext(), "Please Select Vehicle type", Toast.LENGTH_LONG).show();
                    } else if ((linearDrs.getVisibility() == View.VISIBLE)&&(s_drs_id.equals(""))) {
                        e_drs_id.setError("Fill DRS Id");
                    }else if ((linearDrs.getVisibility() == View.VISIBLE)) {
                        s_drs_id = e_drs_id.getText().toString();
                        s_new_drs_id = "0";not_available_activity="0";not_available_reason="0";itemsCollected = "";removalReason = "0";others="";baseImage="0";removal_type="0";
                        s_reason_repla = "0";veh_condition="0";missing_type = "0";collection_amount="0";collection_date="0";collection_type="0";collection_image="0";missing_reason="0";
                        updateInstallationData();
                    } else {
                        drs_dirction = "0";s_drs_id = "0";s_new_drs_id = "0";old_sim_no="0";new_sim_no="0";sim_reason="0";sim_provider="0";removalReason = "0";others="";
                        s_e_device_id = "0";disconnection_reason = "0";not_available_activity="0";not_available_reason="0";itemsCollected = "";baseImage="0";removal_type="0";
                        s_reason_repla = "0";veh_condition="0";missing_type = "0";collection_amount="0";collection_date="0";collection_type="0";collection_image="0";missing_reason="0";
                        updateInstallationData();
                    }
                } else if (s_work_type.equalsIgnoreCase("Reinstallation")) {
                    s_reg_no = new_vehicleRegNo.getText().toString();
                    s_remarks = e_remarks.getText().toString();
                    s_drs_id = e_drs_id.getText().toString();
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    if (new_deviceidReinstall.getVisibility() == View.VISIBLE) {
                        s_new_device_id = new_deviceidReinstall.getText().toString();
                    } else {
                        s_new_device_id = "0";
                    } if (magnetset_install.isChecked()) {
                        mgt_set = "1";
                    } else {
                        mgt_set = "0";
                    }int radioDrs = radiodrsReInstall.getCheckedRadioButtonId();
                    drsReeInstall = (RadioButton)v.findViewById(radioDrs);
                    String drsTypes = drsReeInstall.getText().toString();
                    if(drsTypes.equalsIgnoreCase("Yes")&& (drsReInstall.getVisibility()==View.VISIBLE)){
                        is_drs="1";
                    }else if(drsTypes.equalsIgnoreCase("No")&&(drsReInstall.getVisibility()==View.VISIBLE)){
                        is_drs="2";
                    }else{
                        is_drs="0";
                    }int selectedId = radioGroup.getCheckedRadioButtonId();
                    radionormal = (RadioButton) v.findViewById(selectedId);
                    String drs = radionormal.getText().toString();
                    if (drs.equalsIgnoreCase("Normal") && (linearDrs.getVisibility() == View.VISIBLE)) {
                        drs_dirction = "1";
                    } else if (drs.equalsIgnoreCase("Reverse") && (linearDrs.getVisibility() == View.VISIBLE)) {
                        drs_dirction = "2";
                    } else {
                        drs_dirction = "0";
                    } int selectedIgnition = radiogroup.getCheckedRadioButtonId();
                    l_in = (RadioButton) v.findViewById(selectedIgnition);
                    String typeIgnition = l_in.getText().toString();
                    if (typeIgnition.equalsIgnoreCase("No")) {
                        ignition_sensor = "0";
                    } else {
                        ignition_sensor = "0";
                    }int selectedTypes = radiogroupDoor.getCheckedRadioButtonId();
                    doorNo = (RadioButton) v.findViewById(selectedTypes);
                    String typesensor = doorNo.getText().toString();
                    if (typesensor.equalsIgnoreCase("No")) {
                        door_sensor = "0";
                    } else {
                        door_sensor = "0";
                    }s_VehicleTypeInst = new_in_vehicleTypeReins.getSelectedItem().toString();
                    s_e_device_id = old_deviceid.getText().toString();
                    if (s_e_device_id.equals("")) {
                        old_deviceid.setError("Vts Id can't be null");
                    } else if (s_new_device_id.equals("") && (new_deviceidReinstall.getVisibility() == View.VISIBLE)) {
                        new_deviceidReinstall.setError("Vts Id can't be null");
                    } else if (s_reg_no.equals("")||s_reg_no.equals("0")||s_reg_no.equals(null)) {
                        new_vehicleRegNo.setError("Reg no. can't be null");
                    } else if ((s_VehicleTypeInst.equalsIgnoreCase("Select Vehicle Type"))) {
                        Toast.makeText(getContext(), "please select vehicle type", Toast.LENGTH_LONG).show();
                    } else if (new_deviceidReinstall.getVisibility() == View.VISIBLE) {
                        s_new_device_id = new_deviceidReinstall.getText().toString();
                        s_new_drs_id = "0";
                        if (new_deviceidReinstall.getText().toString().equals(old_deviceid.getText().toString())) {
                            new_deviceidReinstall.setError("Id can't be same");
                            Toast.makeText(getContext(), "Id can't be same", Toast.LENGTH_SHORT).show();
                        } else {
                            s_new_device_id = new_deviceidReinstall.getText().toString();
                            if(e_drs_id.getText().toString().equals("")){
                                s_drs_id="0";s_new_drs_id = "0";not_available_activity="0";not_available_reason="0";old_sim_no="0";new_sim_no="0";sim_reason="0";sim_provider="0";itemsCollected = "";removalReason = "0";others="";removal_type="0";
                                s_reason_repla = "0";veh_condition="0";is_demo = "0";missing_type = "0";collection_amount="0";collection_date="0";collection_type="0";collection_image="0";missing_type="0";baseImage="0";missing_reason="0";
                                updateInstallationData();
                            }else{
                                s_drs_id = e_drs_id.getText().toString();s_new_drs_id = "0";not_available_activity="0";not_available_reason="0";old_sim_no="0";new_sim_no="0";sim_reason="0";sim_provider="0";itemsCollected = "";baseImage="0";
                                removalReason = "0";others="";s_reason_repla = "0";veh_condition="0";is_demo = "0";missing_type = "0";collection_amount="0";collection_date="0";collection_type="0";collection_image="0";missing_reason="0";removal_type="0";
                                updateInstallationData();
                            }}
                    } else if ((linearDrs.getVisibility() == View.VISIBLE)&& (s_drs_id.equals(""))) {
                        e_drs_id.setError("Fill DRS Id");
                    }else if ((linearDrs.getVisibility() == View.VISIBLE)) {
                        s_drs_id = e_drs_id.getText().toString();s_new_device_id = "0";s_new_drs_id = "0";not_available_activity="0";not_available_reason="0";old_sim_no="0";new_sim_no="0";sim_reason="0";sim_provider="0";itemsCollected = "";removalReason = "0";others="";
                        s_reason_repla = "0";veh_condition="0";is_demo = "0";collection_amount="0";collection_date="0";collection_type="0";collection_image="0";missing_type="0";baseImage="0";missing_reason="0";removal_type="0";
                        updateInstallationData();
                    }else {
                        s_new_drs_id = "0";s_new_device_id = "0";s_drs_id = "0";disconnection_reason = "0";not_available_activity="0";not_available_reason="0";old_sim_no="0";new_sim_no="0";sim_reason="0";sim_provider="0";itemsCollected = "";removalReason = "0";others="";
                        s_reason_repla = "0";veh_condition="0";is_demo = "0";collection_amount="0";collection_date="0";collection_type="0";collection_image="0";missing_type="0";baseImage="0";missing_reason="0";removal_type="0";
                        updateInstallationData();
                    }
                } else if (s_work_type.equalsIgnoreCase("Replacement")) {
                    s_new_device_id = new_deviceid.getText().toString();
                    s_reg_no = regNo.getText().toString();
                    int selectedId = radiodireplace.getCheckedRadioButtonId();
                    radionormalrep = (RadioButton) v.findViewById(selectedId);
                    linearDrs.setVisibility(View.GONE);
                    String drs = radionormalrep.getText().toString();
                    if (drs.equalsIgnoreCase("Normal") && (relaydrsTypeReplace.getVisibility() == View.VISIBLE)) {
                        drs_dirction = "1";
                    } else if (drs.equalsIgnoreCase("Reverse") && (relaydrsTypeReplace.getVisibility() == View.VISIBLE)) {
                        drs_dirction = "2";
                    } else {
                        drs_dirction = "0";
                    }int selectedIgnition = radiogroup.getCheckedRadioButtonId();
                    l_in = (RadioButton) v.findViewById(selectedIgnition);
                    String typeIgnition = l_in.getText().toString();
                    if (typeIgnition.equalsIgnoreCase("No")) {
                        ignition_sensor = "0";
                    } else {
                        ignition_sensor = "0";
                    }int selectedTypes = radiogroupDoor.getCheckedRadioButtonId();
                    doorNo = (RadioButton) v.findViewById(selectedTypes);
                    String typesensor = doorNo.getText().toString();
                    if (typesensor.equalsIgnoreCase("No")) {
                        door_sensor = "0";
                    } else {
                        door_sensor = "0";
                    }s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    int selectedType = drsReplace.getCheckedRadioButtonId();
                    radiotype = (RadioButton) v.findViewById(selectedType);
                    String type = radiotype.getText().toString();
                    if (type.equalsIgnoreCase("YES")) {
                        is_drs = "1";
                    } else {
                        is_drs = "0";
                    }if (magnet_set.isChecked()) {
                        mgt_set = "1";
                    } else {
                        mgt_set = "0";
                    }s_e_device_id = old_deviceidreplace.getText().toString();
                    if((linearvts.getVisibility()==View.VISIBLE)&&(new_drsid.getVisibility()==View.GONE)) {
                        if (s_e_device_id.equals("")) {
                            old_deviceidreplace.setError("Vts id can't be null");
                        } else if (s_reg_no.equals("") || s_reg_no.equals("0") || s_reg_no.equals("null")) {
                            regNo.setError("Registration no. can't be null or zero");
                        } else if (s_new_device_id.equals("")) {
                            new_deviceid.setError("Vts Id can't be null");
                        } else if ((reason_replace.getSelectedItem().toString().equalsIgnoreCase("Select Reason"))) {
                            Toast.makeText(getContext(), "Please Select Reason", Toast.LENGTH_LONG).show();
                        } else {
                            s_drs_id = "0";s_new_drs_id = "0";s_remarks = e_remarks.getText().toString();missing_type = "0";collection_amount="0";collection_date="0";collection_type="0";collection_image="0";missing_reason="0";
                            disconnection_reason = "0";not_available_activity="0";not_available_reason="0";itemsCollected = "";removalReason = "0";others="";veh_condition="0";is_demo = "0";missing_type="0";baseImage="0";removal_type="0";
                            updateInstallationData();
                        }
                    }else if ((old_deviceidreplace.getVisibility() == View.VISIBLE) && (drs_veh_no.getVisibility() == View.GONE)) {
                        if (old_drsid.getText().toString().equals("")) {
                            old_drsid.setError("Vts Id can't be null");
                        } else if (new_drsid.getText().toString().equals("")) {
                            new_drsid.setError("Vts Id can't be null");
                        } else if (s_reg_no.equals("") || s_reg_no.equals("0") || s_reg_no.equals("null")) {
                            regNo.setError("Registration no. can't be null or zero");
                        } else if ((reason_replace.getSelectedItem().toString().equalsIgnoreCase("Select Reason"))) {
                            Toast.makeText(getContext(), "Please Select Reason", Toast.LENGTH_LONG).show();
                        }else {
                            s_new_drs_id = old_drsid.getText().toString();
                            s_drs_id = new_drsid.getText().toString();is_drs = "1";itemsCollected = "";removalReason = "0";others="";veh_condition="0";is_demo = "0";missing_type = "0";collection_amount="0";collection_date="0";collection_type="0";collection_image="0";
                            disconnection_reason = "0";not_available_activity="0";not_available_reason="0";old_sim_no="0";new_sim_no="0";sim_reason="0";sim_provider="0";s_remarks = e_remarks.getText().toString();missing_type="0";baseImage="0";missing_reason="0";removal_type="0";
                            updateInstallationData();
                        }
                    } else if ((drs_veh_no.getVisibility() == View.VISIBLE) && (drs_vts_id.getVisibility() == View.VISIBLE)) {
                        if (drs_vts_id.getText().toString().equals("")) {
                            drs_vts_id.setError("Vts Id can't be null");
                        } else if (drs_veh_no.getText().toString().equals("")) {
                            drs_veh_no.setError("Reg no. can't be null");
                        } else if (old_drsid.getText().toString().equals(new_drsid.getText().toString())) {
                            new_drsid.setError("Id could not be same");
                        }else if (s_reg_no.equals("") || s_reg_no.equals("0") || s_reg_no.equals("null")) {
                            regNo.setError("Registration no. can't be null or zero");
                        } else {
                            s_new_drs_id = old_drsid.getText().toString();s_drs_id = new_drsid.getText().toString();
                            is_drs = "1";s_reg_no = drs_veh_no.getText().toString();missing_type = "0";
                            s_e_device_id = drs_vts_id.getText().toString();s_new_device_id = "0";s_reason_repla = "0";itemsCollected = "";removalReason = "0";others="";veh_condition="0";collection_amount="0";collection_date="0";collection_type="0";collection_image="0";removal_type="0";
                            disconnection_reason = "0";not_available_activity="0";not_available_reason="0";old_sim_no="0";new_sim_no="0";sim_reason="0";sim_provider="0";is_demo = "0";s_remarks = e_remarks.getText().toString();missing_type="0";baseImage="0";missing_reason="0";
                            updateInstallationData();
                        }
                    } else {
                        s_drs_id = "0";s_new_drs_id = "0";s_remarks = e_remarks.getText().toString();missing_type = "0";collection_amount="0";collection_date="0";collection_type="0";collection_image="0";missing_reason="0";
                        disconnection_reason = "0";not_available_activity="0";not_available_reason="0";itemsCollected = "";removalReason = "0";others="";veh_condition="0";is_demo = "0";missing_type="0";baseImage="0";removal_type="0";
                        updateInstallationData();
                    }
                } else if (s_work_type.equalsIgnoreCase("Removal/Collection")) {
                    if(removal_type.equalsIgnoreCase("Select Reason")){
                        Toast.makeText(getContext(), "Select Removal Type", Toast.LENGTH_SHORT).show();
                    }s_e_device_id = remove_deviceid.getText().toString();
                    s_reg_no = remove_reg_no.getText().toString();
                    if(s_reg_no.equals("")){
                        s_reg_no="0";
                    }s_remove_reason = reason_remove.getSelectedItem().toString();
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_new_drs_id = "0";s_new_device_id = "0";s_drs_id = "0";veh_condition="0";others="";disconnection_reason = "0";mgt_set="0";not_available_activity="0";not_available_reason="0";old_sim_no="0";new_sim_no="0";baseImage="0";is_drs="0";
                    sim_reason="0";sim_provider="0";mgt_set="0";door_sensor="0";ignition_sensor="0";is_demo = "0";missing_type = "0";collection_amount="0";collection_date="0";collection_type="0";collection_image="0";missing_type="0";missing_reason="0";
                    SparseBooleanArray checked = lvItem.getCheckedItemPositions();
                    itemsCollected = "";
                    for (int i = 0; i < checked.size(); i++) {
                        int key = checked.keyAt(i);
                        itemsCollected = itemsCollected + (collected_items.get(key).getId()) + ":";
                    }s_remarks = e_remarks.getText().toString();
                    if(removal_type.equals("1")||(removal_type.equals("2"))) {
                        if (s_e_device_id.equals("")) {
                            remove_deviceid.setError("Vts Id can't be null");
                        } else if (s_reg_no.equals("") || s_reg_no.equals("0") || s_reg_no.equals("null")) {
                            remove_reg_no.setError("Registration no.can't be null or zero");
                        } else if (s_remove_reason.equalsIgnoreCase("Select Removal Reason")) {
                            Toast.makeText(getContext(), "Select Reason", Toast.LENGTH_LONG).show();
                        } else if (itemsCollected.equals("")) {
                            Toast.makeText(getActivity(), "Select Items Collected", Toast.LENGTH_SHORT).show();
                        } else {
                            updateInstallationData();
                        }
                    }if(removal_type.equals("3")){
                        if (s_e_device_id.equals("")) {
                            remove_deviceid.setError("Vts Id can't be null");
                        } else if (s_remove_reason.equalsIgnoreCase("Select Removal Reason")) {
                            Toast.makeText(getContext(), "Select Reason", Toast.LENGTH_LONG).show();
                        } else if (itemsCollected.equals("")) {
                            Toast.makeText(getActivity(), "Select Items Collected", Toast.LENGTH_SHORT).show();
                        } else {
                            updateInstallationData();
                        }
                    }
                } else if (s_work_type.equalsIgnoreCase("Fault Repair")) {
                    s_e_device_id = fault_vts_id.getText().toString();
                    s_reg_no = fault_reg_no.getText().toString();
                    if (check_tel_supprt.isChecked()) {
                        tel_support = "1";
                    } else {
                        tel_support = "";
                    }s_device_id = "0";s_drs_id = "0";s_new_drs_id = "0";status = "0";s_VehicleTypeInst = "0";s_reason_repla = "0";removalReason = "0";old_sim_no="0";new_sim_no="0";mgt_set="0";door_sensor="0";ignition_sensor="0";missing_type="0";baseImage="0";missing_reason="0";missing_reason="0";
                    itemsCollected = "";s_new_device_id = "0";is_drs = "0";drs_dirction = "0";disconnection_reason = "0";not_available_activity="0";not_available_reason="0";is_demo = "0";missing_type = "0";collection_amount="0";collection_date="0";collection_type="0";collection_image="0";removal_type="0";
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    SparseBooleanArray checked = lv.getCheckedItemPositions();
                    others = "";
                    for (int i = 0; i < checked.size(); i++) {
                        int key = checked.keyAt(i);
                        others = others + (list_change_values.get(key).getId()) + ":";
                    }if(others.contains("8")){
                        veh_condition = "1";
                    }else{
                        veh_condition="0";
                    }s_remarks = e_remarks.getText().toString();
                    if (s_e_device_id.equals("")) {
                        fault_vts_id.setError("Vts Id can't be null");
                    } else if (s_reg_no.equals("")||s_reg_no.equals("0")||s_reg_no.equals("null")) {
                        fault_reg_no.setError("Registration no.can't be null or zero");
                    } else if (others.equals("")) {
                        Toast.makeText(getActivity(), "Select Values", Toast.LENGTH_SHORT).show();
                    } else {
                        s_remarks = e_remarks.getText().toString();
                        updateInstallationData();
                    }
                } else if (s_work_type.equalsIgnoreCase("Phone Support")) {
                    s_e_device_id = phsupport_vts_id.getText().toString();
                    s_reg_no =phSupport_reg_no.getText().toString();
                    s_device_id = "0";s_drs_id = "0";s_new_drs_id = "0";status = "0";s_VehicleTypeInst = "0";s_reason_repla = "0";old_sim_no="0";new_sim_no="0";sim_reason="0";sim_provider="0";mgt_set="0";door_sensor="0";ignition_sensor="0";missing_type="0";baseImage="0";missing_reason="0";removal_type="0";
                    removalReason = "0";itemsCollected = "";s_new_device_id = "0";is_drs = "0";drs_dirction = "0";mgt_set="0";not_available_activity="0";not_available_reason = "0";is_demo = "0";missing_type = "0";collection_amount="0";collection_date="0";collection_type="0";collection_image="0";
                    if(disconnection_reason.equals("1")){
                        veh_condition="1";
                    }else {
                        veh_condition = "0";
                    }s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_remarks = e_remarks.getText().toString();
                    if (s_e_device_id.equals("")) {
                        phsupport_vts_id.setError("Vts Id can't be null");
                    }  else if (s_reg_no.equals("")||s_reg_no.equals("0")||s_reg_no.equals("null")) {
                        phSupport_reg_no.setError("Registration no.can't be null or zero");
                    } else if ((discReason.getSelectedItem().toString().equalsIgnoreCase("Select Reason"))) {
                        Toast.makeText(getContext(), "Please Select Reason", Toast.LENGTH_LONG).show();
                    } else {
                        s_remarks = e_remarks.getText().toString();
                        updateInstallationData();
                    }
                } else if (s_work_type.equalsIgnoreCase("Sim Replacement")) {
                    String MobilePattern = "[0-9]{10}";
                    s_e_device_id = sim_vts_id.getText().toString();
                    new_sim_no = e_new_sim_no.getText().toString();
                    old_sim_no = e_old_sim_no.getText().toString();
                    s_reg_no = sim_vehicle_no.getText().toString();
                    if(s_reg_no.equalsIgnoreCase("Please Enter Registration No.")){
                        sim_vehicle_no.setError("Please Enter valid no. or Zero");
                    }else {
                        s_reg_no = sim_vehicle_no.getText().toString();
                    } if(old_sim_no.equals("0")) {
                        old_sim_no="0000000000";
                    }else{
                        old_sim_no = e_old_sim_no.getText().toString();
                    }s_device_id = "0";s_drs_id = "0";s_new_drs_id = "0";status = "0";s_VehicleTypeInst = "0";s_reason_repla = "0";mgt_set="0";door_sensor="0";ignition_sensor="0";is_demo = "0";missing_type = "0";collection_amount="0";collection_date="0";collection_type="0";collection_image="0";
                    removalReason = "0";itemsCollected = "";s_new_device_id = "0";is_drs = "0";drs_dirction = "0";veh_condition = "0";disconnection_reason="0";mgt_set="0";not_available_reason= "0";not_available_activity="0";missing_type="0";baseImage="0";missing_reason="0";removal_type="0";
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_remarks = e_remarks.getText().toString();
                    if (s_e_device_id.equals("")) {
                        sim_vts_id.setError("Vts Id can't be null");
                    }else if(old_sim_no.equals("")){
                        e_old_sim_no.setError("Please enter old Sim No.");
                    } else if(new_sim_no.equals("")){
                        e_new_sim_no.setError("Please enter new Sim No.");
                    }else if(!e_new_sim_no.getText().toString().matches(MobilePattern)) {
                        e_new_sim_no.setError("Please enter valid 10 digit mobile no. ");
                    } else if ((sim_replace_reason.getSelectedItem().toString().equalsIgnoreCase("Replacement Reason"))) {
                        Toast.makeText(getContext(), "Please Select Reason", Toast.LENGTH_LONG).show();
                    }else if ((sim_operator.getSelectedItem().toString().equalsIgnoreCase("New Sim Provider"))) {
                        Toast.makeText(getContext(), "Please Select Sim Provider", Toast.LENGTH_LONG).show();
                    } else {
                        s_remarks = e_remarks.getText().toString();
                        updateInstallationData();
                    }
                }else if (s_work_type.equalsIgnoreCase("Missing Device")) {
                    s_e_device_id = mDevice_vts_id.getText().toString();
                    s_reg_no = mDevice_reg_no.getText().toString();
                    s_device_id = "0";s_drs_id = "0";s_new_drs_id = "0";status = "0";s_VehicleTypeInst = "0";s_reason_repla = "0";removalReason = "0";old_sim_no="0";new_sim_no="0";sim_reason="0";sim_provider="0";mgt_set="0";door_sensor="0";ignition_sensor="0";removal_type="0";
                    itemsCollected = "";s_new_device_id = "0";is_drs = "0";drs_dirction = "0";disconnection_reason = "0";veh_condition="0";mgt_set="0";not_available_activity="0";not_available_reason="0";is_demo = "0";collection_amount="0";collection_date="0";collection_type="0";collection_image="0";
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_remarks = e_remarks.getText().toString();
                    if (s_e_device_id.equals("")) {
                        mDevice_vts_id.setError("Vts Id can't be null");
                    } else if (s_reg_no.equals("")||s_reg_no.equals("0")||s_reg_no.equals("null")) {
                        mDevice_reg_no.setError("Registration no.can't be null or zero");
                    }else if((relMissing.getVisibility()==View.VISIBLE)&&(missingType.getSelectedItem().toString().equalsIgnoreCase("Select Reason"))){
                        Toast.makeText(getActivity(), "Please Select Reason", Toast.LENGTH_SHORT).show();
                    } else {
                        s_remarks = e_remarks.getText().toString();
                        updateInstallationData();
                    }
                }else if (s_work_type.equalsIgnoreCase("Vehicle Not Available")) {
                    s_e_device_id = vehNotAvailVtsID.getText().toString();
                    if(s_e_device_id.equals("")){
                        s_e_device_id="0";
                    }s_reg_no = vehNotAvailRegNo.getText().toString();
                    if(s_reg_no.equals("")) {
                        s_reg_no = "0";
                    }s_device_id = "0";s_drs_id = "0";s_new_drs_id = "0";status = "0";s_VehicleTypeInst = "0";s_reason_repla = "0";removalReason = "0";old_sim_no="0";new_sim_no="0";baseImage="0";missing_reason="0";removal_type="0";
                    itemsCollected = "";s_new_device_id = "0";is_drs = "0";drs_dirction = "0";disconnection_reason ="0";veh_condition="0";mgt_set="0";is_demo = "0";missing_type = "0";collection_amount="0";collection_date="0";collection_type="0";collection_image="0";
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_remarks = e_remarks.getText().toString();
                    if ((vehDetail.getVisibility()==View.VISIBLE)&&(s_e_device_id.equals(""))) {
                        vehNotAvailVtsID.setError("Vts Id can't be null");
                    } else if((vehDetail.getVisibility()==View.VISIBLE)&&(s_reg_no.equals("")||s_reg_no.equals("0")||s_reg_no.equals("null")&&(vehNotAvailRegNo.getVisibility()==View.VISIBLE))){
                        vehNotAvailRegNo.setError("Registration no.can't be null or zero");
                    } else if(vehiclenoavailSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Activity")){
                        Toast.makeText(getContext(), "Please Select Activity", Toast.LENGTH_LONG).show();
                    } else if(notAvailReason.getSelectedItem().toString().equalsIgnoreCase("Select Reason")){
                        Toast.makeText(getContext(), "Please Select Reason", Toast.LENGTH_LONG).show();
                    } else if(not_available_reason.equals("3")&&(s_remarks.equals(""))){
                        e_remarks.setError("Please Fill Remarks");
                    }else if(not_available_activity.equals("7")&&(s_remarks.equals(""))){
                        e_remarks.setError("Please Fill Remarks");
                    } else {
                        s_remarks = e_remarks.getText().toString();
                        updateInstallationData();
                    }
                }else if(s_work_type.equalsIgnoreCase("Payment Collection")){
                    String abc = amount.getText().toString();
                    if(abc.equalsIgnoreCase("")){
                        amount.setError("Fill Field");
                    }else {
                        collection_amount = abc+".00";
                    }collection_date =paymentDate.getText().toString();
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_remarks = e_remarks.getText().toString();
                    String image = imageName.getText().toString();
                    s_vehicletype = "0";s_reg_no = "0";device_type = "0";s_device_id = "0";s_drs_id = "0";s_new_drs_id = "0";status = "0";removal_type="0";
                    s_VehicleTypeInst = "0";s_reason_repla = "0";removalReason = "0";old_sim_no = "0";new_sim_no = "0";sim_reason = "0";sim_provider = "0";
                    mgt_set = "0";door_sensor = "0";ignition_sensor = "0";itemsCollected = "";s_new_device_id = "0";is_drs = "0";drs_dirction = "0";disconnection_reason = "0";veh_condition = "0";mgt_set = "0";
                    not_available_activity = "0";not_available_reason = "0";is_demo = "0";missing_type = "0";missing_reason="0";
                    if(payment_method.getSelectedItem().toString().equalsIgnoreCase("Select Payment Method")) {
                        Toast.makeText(getActivity(), "Please Select Payment Method", Toast.LENGTH_SHORT).show();
                    } else{
                        s_remarks = e_remarks.getText().toString();
                        if(!image.equals("")){
                            mProgress.setProgress(0);
                            updateInstallationDataImage();
                        }else {
                            updateInstallationData();
                        }
                    }
                    }else if(s_work_id.equalsIgnoreCase("11")) {
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_remarks = e_remarks.getText().toString();
                    if(s_remarks.equals("")){
                        e_remarks.setError("Please Fill Details");
                    } else {
                        s_remarks = e_remarks.getText().toString();
                        collection_image = "";collection_date = "0";s_vehicletype = "0";s_reg_no = "0";collection_amount = "0";device_type = "0";removal_type = "0";s_device_id = "0";s_drs_id = "0";s_new_drs_id = "0";status = "0";s_VehicleTypeInst = "0";s_reason_repla = "0";
                        removalReason = "0";old_sim_no = "0";new_sim_no = "0";sim_reason = "0";sim_provider = "0";mgt_set = "0";door_sensor = "0";ignition_sensor = "0";itemsCollected = "";s_new_device_id = "0";is_drs = "0";drs_dirction = "0";disconnection_reason = "0";
                        veh_condition = "0";mgt_set = "0";not_available_activity = "0";not_available_reason = "0";is_demo = "0";missing_type = "0";missing_reason = "0";collection_type = "0";
                        updateInstallationData();
                    }
                }
                }
        });
        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowImagePopup(getActivity());
            }
        });
        return v;
    }
    private void ShowImagePopup(FragmentActivity activity) {

        View alet_view = null;
        final Dialog alertDialogBuilder = new Dialog(activity);
        alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater mInflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        alet_view = mInflater.inflate(R.layout.custom_popup_image, null);
        final ImageView cross = (ImageView) alet_view.findViewById(R.id.cross);
        final TextView galary = (TextView) alet_view.findViewById(R.id.gallery);
        final TextView cammera = (TextView) alet_view.findViewById(R.id.cammera);
        alertDialogBuilder.setContentView(alet_view);
        alertDialogBuilder.getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        alertDialogBuilder.setCanceledOnTouchOutside(true);
        alertDialogBuilder.show();
        galary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogBuilder.dismiss();
                buttonPressed = "2";
                galleryIntent();
            }
        });
        cammera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed = "1";
                    if (Build.VERSION.SDK_INT < 24) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        uri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        try {
                          //  startActivityForResult(intent, REQUEST_CAMERA);
                            openCameraIntent();
                            alertDialogBuilder.dismiss();
                        } catch (SecurityException e) {
                            e.printStackTrace();
                            try {
                                if (hasPermissions(getActivity(), PERMISSIONS)) {
                                } else {
                                    EasyPermissions.requestPermissions(this, "Access for storage",101, PERMISSIONS);
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
                                EasyPermissions.requestPermissions(this, "Access for storage",101, PERMISSIONS);
                            }
                        } catch (Exception qe) {
                            qe.printStackTrace();
                        }
                        openCameraIntent();
                        alertDialogBuilder.dismiss();
                    }
            }
            File getOutputMediaFile(int type) {
                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.d("****", "Oops! Failed create "+ IMAGE_DIRECTORY_NAME +" directory");
                        return null;
                    }
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
                File mediaFile;
                if (type == MEDIA_TYPE_IMAGE) {
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator+ "IMG_" + timeStamp + ".jpg");
                } else {
                    return null;
                }
                return mediaFile;
            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBuilder.dismiss();
            }
        });
    }
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_PHOTO);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_CAMERA) {
            if (Build.VERSION.SDK_INT < 24) {
                try {
                    path = uri.getPath();
                    file = new File(path);
                    String abc = path;
                    String filename=abc.substring(abc.lastIndexOf("/")+1);
                    imageName.setText(filename);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Click again", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == REQUEST_CAPTURE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                bmp = BitmapFactory.decodeFile(path);
                String abc = path;
                String filename=abc.substring(abc.lastIndexOf("/")+1);
                imageName.setText(filename);
            }
        }else if (requestCode == SELECT_PHOTO)
            try {
                onSelectFromGalleryResult(data);
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
            }
        }
    }
    private void onSelectFromGalleryResult(Intent data) throws FileNotFoundException {
        if (data != null) {
            uri = data.getData();
            file = FileUtils.getFile(getActivity(),uri);
            path = file.getPath();
            compressImage(path);
            File file = new File(path);
            imageName.setText(file.getName());
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
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
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
    }public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "DCIM/Camera");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
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
    private File bitmapToFile(Bitmap bitmap,String fileName){
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
        newInstallmentController.reqeuestVtsDetails(vts_id,this);
    }private void getVTSDetail() {
        newInstallmentController.reqeuestVtsDetail(vts_id,this);
    }private void addclients() {
        newInstallmentController.reqeuestClientList(this);
    }private void removal_type() {
        newInstallmentController.requestRemovalActivityResponse(this);
    }private void addVehNotAvailReason() {
        newInstallmentController.requestVehNotAvailReasonResponse(this);
    }private void getSimOperator() {
        newInstallmentController.reqeuestSimOperatorResponse(this);
    }private void getSimReasonList() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestSimReplaceResponse(this);
    }private void getFaultList() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestFaultList(this);
    }private void getPhoneSupportList() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestDisconnection(this);
    }private void getItemCollectList() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestCollectedItemList(this);
    }private void addReasonRemove() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestRemovalReason(this);
    }private void fetchReasons() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestReplaceReason(this);
    }private void addLocation() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestClientLocation(clientId,this);
    }private void addActivity(){
        ShowProgressBar(true);
        newInstallmentController.notAvailActivityResponse(this);
    }private void addWorkType() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestworkType(this);
    }private void addVehType() {
        ShowProgressBar(true);
        newInstallmentController.reqeuestvehicleType(this);
    }private void damageReason(){
        newInstallmentController.reqeuestDamageReason(this);
    }private void pMethod(){
        newInstallmentController.reqeuestPMethod(this);
    }
    private void updateInstallationData(){
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Updating DATA...");
        pDialog.show();
        RequestBody work_id = RequestBody.create(MediaType.parse("text/plain"), s_work_id);
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), uusername);
        RequestBody clientID = RequestBody.create(MediaType.parse("text/plain"), clientId);
        RequestBody clientLocID = RequestBody.create(MediaType.parse("text/plain"), clientLocId);
        RequestBody reg_no = RequestBody.create(MediaType.parse("text/plain"), s_reg_no);
        RequestBody vehicle_type = RequestBody.create(MediaType.parse("text/plain"), s_vehicletype);
        RequestBody device_Type = RequestBody.create(MediaType.parse("text/plain"), device_type);
        RequestBody device_id = RequestBody.create(MediaType.parse("text/plain"), s_e_device_id);
        RequestBody new_device_ID = RequestBody.create(MediaType.parse("text/plain"), s_new_device_id);
        RequestBody is_DRS = RequestBody.create(MediaType.parse("text/plain"), is_drs);
        RequestBody drs_id = RequestBody.create(MediaType.parse("text/plain"), s_drs_id);
        RequestBody new_drs_id = RequestBody.create(MediaType.parse("text/plain"), s_new_drs_id);
        RequestBody drs_Direction = RequestBody.create(MediaType.parse("text/plain"), drs_dirction);
        RequestBody reason_replace = RequestBody.create(MediaType.parse("text/plain"), s_reason_repla);
        RequestBody removeReason = RequestBody.create(MediaType.parse("text/plain"), removalReason);
        RequestBody itemCollected = RequestBody.create(MediaType.parse("text/plain"), itemsCollected);
        RequestBody other = RequestBody.create(MediaType.parse("text/plain"), others);
        RequestBody tel_Support = RequestBody.create(MediaType.parse("text/plain"), tel_support);
        RequestBody date = RequestBody.create(MediaType.parse("text/plain"), s_date);
        RequestBody time = RequestBody.create(MediaType.parse("text/plain"), s_Time);
        RequestBody remarks = RequestBody.create(MediaType.parse("text/plain"), s_remarks);
        RequestBody disconnectReason = RequestBody.create(MediaType.parse("text/plain"), disconnection_reason);
        RequestBody ignSensor = RequestBody.create(MediaType.parse("text/plain"), ignition_sensor);
        RequestBody fuelSensor = RequestBody.create(MediaType.parse("text/plain"), fuel_sensor);
        RequestBody doorSensor = RequestBody.create(MediaType.parse("text/plain"), door_sensor);
        RequestBody veh_Condition = RequestBody.create(MediaType.parse("text/plain"), veh_condition);
        RequestBody Mgt_Set = RequestBody.create(MediaType.parse("text/plain"), mgt_set);
        RequestBody simProvider = RequestBody.create(MediaType.parse("text/plain"), sim_provider);
        RequestBody oldSimNo = RequestBody.create(MediaType.parse("text/plain"), old_sim_no);
        RequestBody newSimNo = RequestBody.create(MediaType.parse("text/plain"), new_sim_no);
        RequestBody simReason = RequestBody.create(MediaType.parse("text/plain"), sim_reason);
        RequestBody notAvailReason = RequestBody.create(MediaType.parse("text/plain"), not_available_reason);
        RequestBody notAvailActivity = RequestBody.create(MediaType.parse("text/plain"), not_available_activity);
        RequestBody isDemo = RequestBody.create(MediaType.parse("text/plain"), is_demo);
        RequestBody missingType = RequestBody.create(MediaType.parse("text/plain"), missing_type);
        RequestBody collectionAmount = RequestBody.create(MediaType.parse("text/plain"), collection_amount);
        RequestBody collectionDate = RequestBody.create(MediaType.parse("text/plain"), collection_date);
        RequestBody collectionType = RequestBody.create(MediaType.parse("text/plain"), collection_type);
        MultipartBody.Part collection_image = null;
         if(buttonPressed.equals("0")){
             collection_image=null;
         } else if(buttonPressed.equals("1")){
             try {
                 File file = bitmapToFile(bmp,"image_call");
                 long length = file.length();
                 ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
                 collection_image = MultipartBody.Part.createFormData("collection_image", file.getName(),fileBody);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }else if(buttonPressed.equals("2")) {
             try {
                 file = new File(path);
                 ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
                 collection_image = MultipartBody.Part.createFormData("collection_image", path,fileBody);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
         RequestBody missingReason = RequestBody.create(MediaType.parse("text/plain"), missing_reason);
         RequestBody removalType = RequestBody.create(MediaType.parse("text/plain"), removal_type);
//         newInstallmentController.postInstallationsData(work_id,username,clientID,clientLocID,reg_no,vehicle_type,device_Type,device_id,new_device_ID,is_DRS,drs_id,new_drs_id,drs_Direction,reason_replace,removeReason,itemCollected,other,
//                tel_Support,date,time,remarks,disconnectReason,ignSensor,fuelSensor,doorSensor,veh_Condition,Mgt_Set,simProvider,oldSimNo,newSimNo,simReason,notAvailReason,notAvailActivity,isDemo,
//                missingType,collectionAmount,collectionDate,collectionType,collection_image,missingReason,removalType,this);
    }
    private void updateInstallationDataImage(){
        circularRelative.setVisibility(View.VISIBLE);
        pStatus =0;
        Progress(true);
        RequestBody work_id = RequestBody.create(MediaType.parse("text/plain"), s_work_id);
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), uusername);
        RequestBody clientID = RequestBody.create(MediaType.parse("text/plain"), clientId);
        RequestBody clientLocID = RequestBody.create(MediaType.parse("text/plain"), clientLocId);
        RequestBody reg_no = RequestBody.create(MediaType.parse("text/plain"), s_reg_no);
        RequestBody vehicle_type = RequestBody.create(MediaType.parse("text/plain"), s_vehicletype);
        RequestBody device_Type = RequestBody.create(MediaType.parse("text/plain"), device_type);
        RequestBody device_id = RequestBody.create(MediaType.parse("text/plain"), s_e_device_id);
        RequestBody new_device_ID = RequestBody.create(MediaType.parse("text/plain"), s_new_device_id);
        RequestBody is_DRS = RequestBody.create(MediaType.parse("text/plain"), is_drs);
        RequestBody drs_id = RequestBody.create(MediaType.parse("text/plain"), s_drs_id);
        RequestBody new_drs_id = RequestBody.create(MediaType.parse("text/plain"), s_new_drs_id);
        RequestBody drs_Direction = RequestBody.create(MediaType.parse("text/plain"), drs_dirction);
        RequestBody reason_replace = RequestBody.create(MediaType.parse("text/plain"), s_reason_repla);
        RequestBody removeReason = RequestBody.create(MediaType.parse("text/plain"), removalReason);
        RequestBody itemCollected = RequestBody.create(MediaType.parse("text/plain"), itemsCollected);
        RequestBody other = RequestBody.create(MediaType.parse("text/plain"), others);
        RequestBody tel_Support = RequestBody.create(MediaType.parse("text/plain"), tel_support);
        RequestBody date = RequestBody.create(MediaType.parse("text/plain"), s_date);
        RequestBody time = RequestBody.create(MediaType.parse("text/plain"), s_Time);
        RequestBody remarks = RequestBody.create(MediaType.parse("text/plain"), s_remarks);
        RequestBody disconnectReason = RequestBody.create(MediaType.parse("text/plain"), disconnection_reason);
        RequestBody ignSensor = RequestBody.create(MediaType.parse("text/plain"), ignition_sensor);
        RequestBody fuelSensor = RequestBody.create(MediaType.parse("text/plain"), fuel_sensor);
        RequestBody doorSensor = RequestBody.create(MediaType.parse("text/plain"), door_sensor);
        RequestBody veh_Condition = RequestBody.create(MediaType.parse("text/plain"), veh_condition);
        RequestBody Mgt_Set = RequestBody.create(MediaType.parse("text/plain"), mgt_set);
        RequestBody simProvider = RequestBody.create(MediaType.parse("text/plain"), sim_provider);
        RequestBody oldSimNo = RequestBody.create(MediaType.parse("text/plain"), old_sim_no);
        RequestBody newSimNo = RequestBody.create(MediaType.parse("text/plain"), new_sim_no);
        RequestBody simReason = RequestBody.create(MediaType.parse("text/plain"), sim_reason);
        RequestBody notAvailReason = RequestBody.create(MediaType.parse("text/plain"), not_available_reason);
        RequestBody notAvailActivity = RequestBody.create(MediaType.parse("text/plain"), not_available_activity);
        RequestBody isDemo = RequestBody.create(MediaType.parse("text/plain"), is_demo);
        RequestBody missingType = RequestBody.create(MediaType.parse("text/plain"), missing_type);
        RequestBody collectionAmount = RequestBody.create(MediaType.parse("text/plain"), collection_amount);
        RequestBody collectionDate = RequestBody.create(MediaType.parse("text/plain"), collection_date);
        RequestBody collectionType = RequestBody.create(MediaType.parse("text/plain"), collection_type);
        MultipartBody.Part collection_image = null;
        if(buttonPressed.equals("0")){
            collection_image=null;
        } else if(buttonPressed.equals("1")){
            try {
                File file = bitmapToFile(bmp,"image_call");
                long length = file.length();
                ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
                collection_image = MultipartBody.Part.createFormData("collection_image", file.getName(),fileBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(buttonPressed.equals("2")) {
            try {
                file = new File(path);
                ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
                collection_image = MultipartBody.Part.createFormData("collection_image", path,fileBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RequestBody missingReason = RequestBody.create(MediaType.parse("text/plain"), missing_reason);
        RequestBody removalType = RequestBody.create(MediaType.parse("text/plain"), removal_type);
//        newInstallmentController.postInstallationsData(work_id,username,clientID,clientLocID,reg_no,vehicle_type,device_Type,device_id,new_device_ID,is_DRS,drs_id,new_drs_id,drs_Direction,reason_replace,removeReason,itemCollected,other,
//                tel_Support,date,time,remarks,disconnectReason,ignSensor,fuelSensor,doorSensor,veh_Condition,Mgt_Set,simProvider,oldSimNo,newSimNo,simReason,notAvailReason,notAvailActivity,isDemo,
//                missingType,collectionAmount,collectionDate,collectionType,collection_image,missingReason,removalType,this);
    }
    private void setDateAndTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        s_time = dateFormat.format(calen.getTime());
        year = calen.get(Calendar.YEAR);month = calen.get(Calendar.MONTH);day = calen.get(Calendar.DAY_OF_MONTH);
        month = month + 1;hour = calen.get(Calendar.HOUR_OF_DAY);minutes = calen.get(Calendar.MINUTE);
        if (month + 1 < 10) {
            current_date = day + "-0"+month + "-" + year;
        }else {
            current_date = day + "-" + month + "-" + year;
        }if((hour<10)&& (minutes<10)){
            s_time = "0"+hour+":"+"0"+minutes;
        }else{
            s_time = hour +":"+minutes;
        }t_install_date.setText(current_date);
        paymentDate.setText(current_date);
        t_install_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }});
        paymentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDateforPayment();
            }});
        t_install_Time.setText(s_time);
        t_install_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTime();
            }
        });
    }
    private void getTime() {
        TimePickerDialog tpd =new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                selected_totime = hours + ":"+ minutes;
                t_install_Time.setText(selected_totime);
            }
        },hour,minutes,false);
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
        c.add( Calendar.DATE, -4);
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
                }paymentDate.setText(selected_todate);
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
            }}
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void clearData() {
        old_deviceid.setText("");old_deviceidreplace.setText("");regNo.setText("");new_deviceid.setText("");plantName.setText("");
        e_drs_id.setText("");e_reg_no.setText("");e_remarks.setText("");e_device_id.setText("");old_drsid.setText("");
        new_drsid.setText("");drs_veh_no.setText("");drs_vts_id.setText("");new_vehicleRegNo.setText("");new_deviceidReinstall.setText("");
        fault_reg_no.setText("");fault_vts_id.setText("");remove_deviceid.setText("");phsupport_vts_id.setText("");sim_vts_id.setText("");
        e_old_sim_no.setText("");e_new_sim_no.setText("");mDevice_reg_no.setText("");mDevice_vts_id.setText("");vehNotAvailRegNo.setText("");
        vehNotAvailVtsID.setText("");amount.setText("");imageName.setText("");
    }
    @Override
    public void clientResponse(ClientResponse response) {
        try{
            clientList = response.getClientList();
            try {
                try {
                    clientDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }   clientDetail.add(" SELECT CLIENT");
                for (int i = 0; i < clientList.size(); i++) {
                    clientDetail.add(clientList.get(i).getClient_Name());
                }adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, clientDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                client.setAdapter(adapter);
                ShowProgressBar(false);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }}
    @Override
    public void locationResponse(ClientLocationResponse response) {
        try{
            locationList = response.getClientLoc();
            try {
                try {
                    locationDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }locationDetail.add("SELECT LOCATION");
                for (int i = 0; i < locationList.size(); i++) {
                    locationDetail.add(locationList.get(i).getLoc_Name());
                }adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item,locationDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                location.setAdapter(adapter);
                ShowProgressBar(false);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }}
    @Override
    public void workTypeResponse(WorkTypeResponse response) {
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workType.setAdapter(adapter);
//        try{
//            workTypeList = response.getWorktypeList();
//            try {
//                try {
//                    workDetail.clear();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }workDetail.add("SELECT WORK TYPE");
//                for (int i = 0; i < workTypeList.size(); i++) {
//                    workDetail.add(workTypeList.get(i).getWork_Name());
//                }adapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_custom_spinner_item, workDetail);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                workType.setAdapter(adapter);
//                ShowProgressBar(false);
//            } catch (NullPointerException npe) {
//                npe.printStackTrace();
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }}
    }
    @Override
    public void vehicleTypeResponse(VehicleTypeResponse response) {
        try{
            vehicleList = response.getVehicletypeList();
            try {
                try {
                    vehicleDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }vehicleDetail.add("SELECT VEHICLE TYPE");
                for (int i = 0; i < vehicleList.size(); i++) {
                    vehicleDetail.add(vehicleList.get(i).getVehicle_Name());
                }adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, vehicleDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                vehicleType.setAdapter(adapter);
                new_in_vehicleTypeReins.setAdapter(adapter);
                ShowProgressBar(false);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }}
    @Override
    public void faultListResponse(FaultResponse response) {
        try{
            list_change_values = response.getFaultLists();
            try {
                try {
                    value_name.clear();
                } catch (Exception e) {
                    e.printStackTrace();}
                if (list_change_values.size() > 0) {
                    for (int i = 0; i < list_change_values.size(); i++) {
                        value_name.add(list_change_values.get(i).getName());}
                    if (list_change_values.size() > 5) {
                        lv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * list_change_values.size() + 1));}
                    else {lv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * list_change_values.size()));}
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_list_item, value_name);
                    lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    lv.setAdapter(adapter);
                    ShowProgressBar(false);
                } else {}
            }catch (NullPointerException npe) {
                npe.printStackTrace();
            }}catch (Exception e) {
            e.printStackTrace();
        }}
    @Override
    public void replaceResponse(ReplaceReason response) {
        try {
            arr_replaceReasons = response.getReplaceList();
            try {
                try {
                    arr_reasons_s.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }arr_reasons_s.add("SELECT REASON");
                for (int i = 0; i < arr_replaceReasons.size(); i++) {
                    arr_reasons_s.add(arr_replaceReasons.get(i).getReplace_Name());
                }adapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_custom_spinner_item, arr_reasons_s);
                reason_replace.setAdapter(adapter);
                ShowProgressBar(false);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }}
    @Override
    public void disconnectionResponse(DisconnectionResponse response) {
        try {
            supportList = response.getDisconnectionDetails();
            try {
                try {
                    disc_reason.clear();
                } catch (Exception e) {
                    e.printStackTrace();}
                disc_reason.add("SELECT REASON");
                if (supportList.size() > 0) {
                    for (int i = 0; i < supportList.size(); i++) {
                        disc_reason.add(supportList.get(i).getName());
                    }adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, disc_reason);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    discReason.setAdapter(adapter);
                    ShowProgressBar(false);
                } else {}
            }catch (NullPointerException npe) {
                npe.printStackTrace();
            }}catch (Exception e) {
            e.printStackTrace();
        }}
    @Override
    public void removalActivityResponse(RemovalActivityResponse response) {
        try{
            removalActivityDetails = response.getRemovalActivityDetails();
            try {
                try {
                    removalActivity.clear();
                } catch (Exception e) {
                    e.printStackTrace();}
                removalActivity.add("SELECT REMOVAL TYPE");
                if (removalActivityDetails.size() > 0) {
                    for (int i = 0; i < removalActivityDetails.size(); i++) {
                        removalActivity.add(removalActivityDetails.get(i).getA_name());
                    }adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, removalActivity);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    removalType.setAdapter(adapter);
                }else{}
            }catch (NullPointerException npe) {
                npe.printStackTrace();
            }}catch (Exception e) {
            e.printStackTrace();
        }}
    @Override
    public void removalResponse(RemovalResponse response) {
        try{
            removalList = response.getRemovalList();
            try {
                try {
                    removalDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }removalDetail.add("SELECT REMOVAL REASON");
                for (int i = 0; i < removalList.size(); i++) {
                    removalDetail.add(removalList.get(i).getRemoval_Name());
                }adapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_custom_spinner_item, removalDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                reason_remove.setAdapter(adapter);
                ShowProgressBar(false);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }}catch (Exception e) {
            e.printStackTrace();
        }}
    @Override
    public void damageResponse(RemovalResponse response) {
        try{
            damageList = response.getRemovalList();
            try {
                try {
                    removalDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }removalDetail.add("SELECT REASON");
                for (int i = 0; i < damageList.size(); i++) {
                    removalDetail.add(damageList.get(i).getRemoval_Name());
                }adapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_custom_spinner_item, removalDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                missingType.setAdapter(adapter);
                ShowProgressBar(false);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }}catch (Exception e) {
            e.printStackTrace();
        }}
    @Override
    public void collectItemResponse(CollectedItemsResponse response) {
        try{
            collected_items = response.getItemLists();
            try {
                try {
                    item_name.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }if (collected_items.size() > 0) {
                    for (int i = 0; i < collected_items.size(); i++) {
                        item_name.add(collected_items.get(i).getName());
                    }if (collected_items.size() > 5) {
                        lvItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * collected_items.size() + 1));}
                    else {
                        lvItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * collected_items.size() + 1));}
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_list_item, item_name);
                    lvItem.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    lvItem.setAdapter(adapter);
                    ShowProgressBar(false);
                } else {}
            }catch (NullPointerException npe) {
                npe.printStackTrace();
            }}catch (Exception e) {
            e.printStackTrace();
        }}
    @Override
    public void simOperatorResponse(SimOperatorResponse response) {
        try{
            simOperatorDetails = response.getSimOperatorDetails();
            try {
                try {
                    simOperator.clear();
                } catch (Exception e) {
                    e.printStackTrace();}
                simOperator.add("NEW SIM PROVIDER");
                if (simOperatorDetails.size() > 0) {
                    for (int i = 0; i < simOperatorDetails.size(); i++) {
                        simOperator.add(simOperatorDetails.get(i).getSp_name());
                    }adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, simOperator);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sim_operator.setAdapter(adapter);
                }else{}
            }catch (NullPointerException npe) {
                npe.printStackTrace();
            }}catch (Exception e) {
            e.printStackTrace();
        }}
    @Override
    public void simReplaceReason(SimReplaceResponse response) {
        try{
            simreplacereason = response.getSimDetails();
            try {
                try {
                    simReplaceReason.clear();
                } catch (Exception e) {
                    e.printStackTrace();}
                simReplaceReason.add("REPLACEMENT REASON");
                if (simreplacereason.size() > 0) {
                    for (int i = 0; i < simreplacereason.size(); i++) {
                        simReplaceReason.add(simreplacereason.get(i).getS_name());
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, simReplaceReason);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sim_replace_reason.setAdapter(adapter);
                    ShowProgressBar(false);
                }else{}
            }catch (NullPointerException npe) {
                npe.printStackTrace();
            }}catch (Exception e) {
            e.printStackTrace();
        }}
    @Override
    public void notAvailActivity(NotAvailActivityResponse response) {
        try{
            notAvailActivityDetails = response.getNotAvailActivityDetails();
            try {
                try {
                    notAvailActivity.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }notAvailActivity.add("SELECT ACTIVITY");
                for (int i = 0; i < notAvailActivityDetails.size(); i++) {
                    notAvailActivity.add(notAvailActivityDetails.get(i).getActivity());
                }adapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_custom_spinner_item, notAvailActivity);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                vehiclenoavailSpinner.setAdapter(adapter);
                ShowProgressBar(false);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }}catch (Exception e) {
            e.printStackTrace();
        }}
    @Override
    public void vehicleNotAvailReason(VehNotAvailReasonResponse response) {
        try{
            vehNotAvailReasonDetails = response.getVehNotAvailReasonDetails();
            try {
                try {
                    vehNotAvailReason.clear();
                } catch (Exception e) {
                    e.printStackTrace();}
                vehNotAvailReason.add("SELECT REASON");
                if (vehNotAvailReasonDetails.size() > 0) {
                    for (int i = 0; i < vehNotAvailReasonDetails.size(); i++) {
                        vehNotAvailReason.add(vehNotAvailReasonDetails.get(i).getReason());
                    }adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, vehNotAvailReason);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    notAvailReason.setAdapter(adapter);
                }else{}
            }catch (NullPointerException npe) {
                npe.printStackTrace();
            }}catch (Exception e) {
            e.printStackTrace();
        }}
    @Override
    public void vtsResponses(VTSResponse response) {
        try{
            vtsList = response.getVtsDetails();
            try {
                if (s_work_id.equalsIgnoreCase("3")) {
                    if (vtsList.size() == 0) {
                    } else {
                        for (int i = 0; i < vtsList.size(); i++) {
                            s_reg_no = String.valueOf(vtsList.get(i).getReg_no());
                            s_vehicletype = vtsList.get(i).getVeh_type_id();
                            device_type = vtsList.get(i).getDevice_type();
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
            }catch (Exception e) {
            }}catch (Exception e) {
            e.printStackTrace();
        }}
    @Override
    public void vtsResponse(VTSResponse response) {
        try{
            vtsList = response.getVtsDetails();
            try {
                if (vtsList.size() == 0) {
                } else {
                    for (int i = 0; i < vtsList.size(); i++) {
                        s_reg_no = String.valueOf(vtsList.get(i).getReg_no());
                        s_vehicletype = vtsList.get(i).getVeh_type_id();
                        device_type = vtsList.get(i).getDevice_type();
                        String location_Name = vtsList.get(i).getLocation_name();
                        if(s_work_id.equalsIgnoreCase("3")){
                            if (s_reg_no.equalsIgnoreCase("null") ||(s_reg_no.equalsIgnoreCase("") || (s_reg_no.equals("0")))) {
                                regNo.setText("Please Enter Registration no.");
                                regNo.setTextColor(getResources().getColor(R.color.greyed_out));
                                regNo.setEnabled(true);
                            } else{
                                regNo.setText(s_reg_no);
                                regNo.setTextColor(getResources().getColor(R.color.black));
                                plantName.setText(location_Name);
                            }
                        } else if(s_work_id.equalsIgnoreCase("1")){
                            if(s_reg_no.equalsIgnoreCase("null")||(s_reg_no.equalsIgnoreCase("")||(s_reg_no.equals("0")))) {
                                fault_reg_no.setText("Please Enter Registration no.");
                                fault_reg_no.setTextColor(getResources().getColor(R.color.greyed_out));
                                fault_reg_no.setEnabled(true);
                            }else {
                                fault_reg_no.setText(s_reg_no);
                                fault_reg_no.setTextColor(getResources().getColor(R.color.black));
                            }
                        }else if(s_work_id.equalsIgnoreCase("5")){
                            if(s_reg_no.equalsIgnoreCase("null")||(s_reg_no.equalsIgnoreCase("")||(s_reg_no.equals("0")))) {
                                remove_reg_no.setText("Please Enter Registration no.");
                                remove_reg_no.setTextColor(getResources().getColor(R.color.greyed_out));
                                remove_reg_no.setEnabled(true);
                            }else {
                                remove_reg_no.setText(s_reg_no);
                                remove_reg_no.setTextColor(getResources().getColor(R.color.black));
                            }
                        }else if(s_work_id.equalsIgnoreCase("6")){
                            if(s_reg_no.equalsIgnoreCase("null")||(s_reg_no.equalsIgnoreCase("")||(s_reg_no.equals("0")))) {
                                phSupport_reg_no.setText("Please Enter Registration No.");
                                phSupport_reg_no.setTextColor(getResources().getColor(R.color.greyed_out));
                                phSupport_reg_no.setEnabled(true);
                            }else {
                                phSupport_reg_no.setText(s_reg_no);
                                phSupport_reg_no.setTextColor(getResources().getColor(R.color.black));
                            }
                        }else if(s_work_id.equalsIgnoreCase("7")){
                            if(s_reg_no.equalsIgnoreCase("null")||(s_reg_no.equalsIgnoreCase("")||(s_reg_no.equals("0")))) {
                                sim_vehicle_no.setText("Please Enter Registration No.");
                                sim_vehicle_no.setTextColor(getResources().getColor(R.color.greyed_out));
                                sim_vehicle_no.setEnabled(true);
                            }else {
                                sim_vehicle_no.setText(s_reg_no);
                                sim_vehicle_no.setTextColor(getResources().getColor(R.color.black));
                            }
                        }else if(s_work_id.equalsIgnoreCase("8")){
                            if(s_reg_no.equalsIgnoreCase("null")||(s_reg_no.equalsIgnoreCase("")||(s_reg_no.equals("0")))) {
                                mDevice_reg_no.setText("Please Enter Registration No.");
                                mDevice_reg_no.setTextColor(getResources().getColor(R.color.greyed_out));
                                mDevice_reg_no.setEnabled(true);
                            }else {
                                mDevice_reg_no.setText(s_reg_no);
                                mDevice_reg_no.setTextColor(getResources().getColor(R.color.black));
                            }
                        }else if(s_work_id.equalsIgnoreCase("9")){
                            s_reg_no = String.valueOf(vtsList.get(i).getReg_no());
                            String abc = vtsList.get(i).getVeh_type_id();
                            if(abc.equals("")){
                                s_vehicletype = "0";
                            }else {
                                s_vehicletype = vtsList.get(i).getVeh_type_id();
                            }
                            device_type = vtsList.get(i).getDevice_type();
                            if(s_reg_no.equalsIgnoreCase("null")||(s_reg_no.equalsIgnoreCase("")||(s_reg_no.equals("0")))) {
                                vehNotAvailRegNo.setText("Please Enter Registration No.");
                                vehNotAvailRegNo.setTextColor(getResources().getColor(R.color.greyed_out));
                                vehNotAvailRegNo.setEnabled(true);
                            }else {
                                vehNotAvailRegNo.setText(s_reg_no);
                                vehNotAvailRegNo.setTextColor(getResources().getColor(R.color.black));
                            }
                        }else if(s_work_id.equalsIgnoreCase("4")){
                            device_type = vtsList.get(i).getDevice_type();
                        }
                    }
                }
            }catch (Exception e) {
            }}catch (Exception e) {
            e.printStackTrace();
        }}
    @Override
    public void pMethod(PaymentMethodResponse response) {
        try{
            paymentmethod = response.getMethods();
            try {
                try {
                    pMethod.clear();
                } catch (Exception e) {
                    e.printStackTrace();}
                pMethod.add("SELECT PAYMENT METHOD");
                if (paymentmethod.size() > 0) {
                    for (int i = 0; i < paymentmethod.size(); i++) {
                        pMethod.add(paymentmethod.get(i).getMethod());
                    }adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, pMethod);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    payment_method.setAdapter(adapter);
                }else{}
            }catch (NullPointerException npe) {
                npe.printStackTrace();
            }}catch (Exception e) {
            e.printStackTrace();
        }}
    @Override
    public void updateDataResponse(MainResponse response) {
        clearData();
        pMethod();
        try{
            pStatus =0;
            Toast.makeText(getContext(), "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            if (response.getType()==1) {
                circularRelative.setVisibility(View.GONE);
                Progress(false);
                pDialog.hide();
                clearData();
                for (int i = 0; i < lvItem.getCount(); i++) {
                    View view = lvItem.getChildAt(i);
                    CheckedTextView mCheckBox = (CheckedTextView) view.findViewById(R.id.text1);
                    mCheckBox.setChecked(false);
                    lvItem.clearChoices();
                }for (int i = 0; i < lv.getCount(); i++) {
                    View view = lv.getChildAt(i);
                    CheckedTextView mCheckBox = (CheckedTextView) view.findViewById(R.id.text1);
                    mCheckBox.setChecked(false);
                    lv.clearChoices();
                }if(s_work_type.equalsIgnoreCase("New Installation")){
                    addVehType();
                }else if(s_work_type.equalsIgnoreCase("Reinstallation")){
                    addVehType();
                }else if(s_work_type.equalsIgnoreCase("Replacement")){
                    fetchReasons();
                }else if(s_work_type.equalsIgnoreCase("Removal/Collection")){
                    removal_type();
                    addReasonRemove();
                }else if(s_work_type.equalsIgnoreCase("Phone Support")){
                    getPhoneSupportList();
                }else if(s_work_type.equalsIgnoreCase("Sim Replacement")){
                    getSimReasonList();
                    getSimOperator();
                }else if(s_work_type.equalsIgnoreCase("Vehicle not Available")){
                    addActivity();
                    addVehNotAvailReason();
                }else if(s_work_type.equalsIgnoreCase("Missing Device")){
                    damageReason();
                }else if(s_work_type.equalsIgnoreCase("Payment Collection")){
                    pMethod();
                }
            } else {
                pDialog.hide();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }}


    @Override
    public void onProgressUpdate(int percentage) {
    }
    @Override
    public void onError() {
    }
    @Override
    public void onFinish() {
    }

    private void Progress(boolean show) {
            Resources res = getResources();
            Drawable drawable = res.getDrawable(R.drawable.circular);
            mProgress = (ProgressBar) v.findViewById(R.id.circularProgressbar);
            mProgress.setProgress(0);   // Main Progress
            mProgress.setSecondaryProgress(100); // Secondary Progress
            mProgress.setMax(100); // Maximum Progress
            mProgress.setProgressDrawable(drawable);
            tv = (TextView) v.findViewById(R.id.tv);
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
                            Thread.sleep(8); //thread will take approx 1.5 seconds to finish
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
    }

}
