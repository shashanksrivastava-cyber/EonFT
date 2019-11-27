package in.eoninfotech.eontechnician;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

import com.androidadvance.topsnackbar.TSnackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import in.eoninfotech.eontechnician.Responses.ClientDetails;
import in.eoninfotech.eontechnician.Responses.ClientLocationDetail;
import in.eoninfotech.eontechnician.Responses.ClientLocationResponse;
import in.eoninfotech.eontechnician.Responses.ClientResponse;
import in.eoninfotech.eontechnician.Responses.CollectedItemsResponse;
import in.eoninfotech.eontechnician.Responses.DRSDetail;
import in.eoninfotech.eontechnician.Responses.DRSResponse;
import in.eoninfotech.eontechnician.Responses.FaultList;
import in.eoninfotech.eontechnician.Responses.FaultResponse;
import in.eoninfotech.eontechnician.Responses.InstallResponse;
import in.eoninfotech.eontechnician.Responses.ItemList;
import in.eoninfotech.eontechnician.Responses.RemovalList;
import in.eoninfotech.eontechnician.Responses.RemovalResponse;
import in.eoninfotech.eontechnician.Responses.ReplaceReason;
import in.eoninfotech.eontechnician.Responses.ReplaceReasonDetail;
import in.eoninfotech.eontechnician.Responses.VTSDetail;
import in.eoninfotech.eontechnician.Responses.VTSResponse;
import in.eoninfotech.eontechnician.Responses.VehicleTypeDetail;
import in.eoninfotech.eontechnician.Responses.VehicleTypeResponse;
import in.eoninfotech.eontechnician.Responses.WorkTypeDetail;
import in.eoninfotech.eontechnician.Responses.WorkTypeResponse;
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.view.MyTextView;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 27/9/18.
 */

public class NewInstallMentFragmentCommented extends Fragment {

    View v;
    ImageView ivIdExist,ivIdnotExist,idExist,idnotExist,ivnotExistDRS,ivExistDRS,idExistNew, idnotExistnew,idExistReplace,idnotExistReplace,idExistReplaceDRS,idnotExistReplaceDRS, idExistReplaceDRSNew,idnotExistReplaceDRSNew,ivIdExistFault,ivIdnotExistFault,ivExistRemove,ivnotExistRemove;
    String clientId,clientLocId,s_Vehicle_Name,drsStatus,device_type,s_date,s_time,disttid,s_remove_reason,vts_id,drs_id,new_drs_id,uusername, version,selected_todate,selected_totime,current_date,fuel_sensor="0",door_sensor="0",veh_condition="0",mgt_set="0",sim_provider="0",old_sim_no="0",new_sim_no="0";
    CheckConnection chk;
    CheckBox check_tel_supprt;
    EditText e_reg_no, e_device_id, e_drs_id, e_remarks, old_deviceid,new_deviceid,fault_vts_id,t_install_date,t_install_Time,new_vehicleRegNo,remove_deviceid,remove_reg_no,old_deviceidreplace,new_deviceidReinstall,old_drsid,new_drsid;
    MyTextView device_info;
    TextView plantName,regNo;
    String s_reg_no, s_device_id, s_drs_id,s_new_drs_id, s_clientname="SELECT CLIENT", s_remarks, status,s_work_type,s_Time,s_vehicletype,s_VehicleTypeInst,s_reason_repla="0",removalReason="0",itemsCollected="",others="",tel_support="",s_work_id,s_new_device_id,s_e_device_id="0",is_drs="0",drs_dirction="0",disconnection_reason="",ignition_sensor="0",sim_reason="0";
    Button update_dataa;
    Calendar calen = Calendar.getInstance();
    int year, month, day ,hour,minutes;
    ListView lv,lvItem;
    ArrayList<RemovalList> removalList = new ArrayList<>();
    ArrayList<ReplaceReasonDetail> arr_replaceReasons = new ArrayList<>();
    ArrayList<FaultList> list_change_values = new ArrayList<>();
    ArrayList<ItemList> collected_items = new ArrayList<>();
    ArrayList<WorkTypeDetail> workTypeList = new ArrayList<>();
    ArrayList<ClientDetails> clientList = new ArrayList<>();
    ArrayList<VTSDetail> vtsList = new ArrayList<>();
    ArrayList<DRSDetail> drsList = new ArrayList<>();
    ArrayList<VehicleTypeDetail> vehicleList = new ArrayList<>();
    ArrayList<ClientLocationDetail> locationList = new ArrayList<>();
    ArrayList<String> clientDetail = new ArrayList<>();
    ArrayList<String> workDetail = new ArrayList<>();
    ArrayList<String> removalDetail = new ArrayList<>();
    ArrayList<String> vehicleDetail = new ArrayList<>();
    ArrayList<String> locationDetail = new ArrayList<>();
    ArrayList<String> value_name = new ArrayList<>();
    ArrayList<String> item_name = new ArrayList<>();
    ArrayList<String> arr_reasons_s = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ProgressDialog pDialog;
    ProgressBar pDialogText,progressReinstall,progressdrs,progressReinstallNew,progressReplace,progressReplaceDrs,progressReplaceDrsNew,progressfault,progressremove;
    RelativeLayout progressBar,relaydrsTypeReplace,relaydrsType;
    MySearchableSpinner client,vehicleType,workType,location,reason_replace,reason_remove,new_in_vehicleTypeReins;
    LinearLayout linearReplacement,linearInstall,linearReInstall,linearRemoval,linearDrs,linearFault;
    RadioGroup radioGroup,radioGroupReinstall,drsReplace,radiodeviceType,radiodireplace;
    RadioButton radionormal,radiotype,old_Device,new_Device,radionormalrep;
    private Call<InstallResponse> locCall;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_new_install, container, false);
        chk = new CheckConnection(v.getContext());
        disttid = getArguments().getString("disttid");
        uusername = getArguments().getString("usernme");
        version = getArguments().getString("version");
        Log.i("****stat dist n usr***", disttid + " " + uusername);
        e_device_id = (EditText) v.findViewById(R.id.new_in_deviceid);
        e_drs_id = (EditText) v.findViewById(R.id.new_in_drs_id);
        e_remarks = (EditText) v.findViewById(R.id.new_in_remarks);
        e_reg_no = (EditText) v.findViewById(R.id.new_in_reg_no);
        client = (MySearchableSpinner) v.findViewById(R.id.new_in_clients);
        vehicleType = (MySearchableSpinner)v.findViewById(R.id.new_in_vehicleType);
        progressdrs=(ProgressBar)v.findViewById(R.id.progressdrs);
        progressReinstallNew = (ProgressBar)v.findViewById(R.id.progressReinstallNew);
        progressReplace = (ProgressBar)v.findViewById(R.id.progressReplace);
//        progressReplaceDrs = (ProgressBar)v.findViewById(R.id.progressReplaceDrs);
//        progressReplaceDrsNew = (ProgressBar)v.findViewById(R.id.progressReplaceDrsNew);
        //progressfault = (ProgressBar)v.findViewById(R.id.progressfault);
        progressremove = (ProgressBar)v.findViewById(R.id.progressremove);
        update_dataa = (Button) v.findViewById(R.id.new_in_update);
        radioGroup = (RadioGroup) v.findViewById(R.id.radiodirec);
        radioGroupReinstall = (RadioGroup)v.findViewById(R.id.radioGroupReinstall);
        radiodeviceType = (RadioGroup)v.findViewById(R.id.radiodeviceType);
        t_install_date = (EditText)v.findViewById(R.id.installDate);
        t_install_Time = (EditText)v.findViewById(R.id.installTime);
        ivnotExistDRS = (ImageView)v.findViewById(R.id.ivnotExistDRS);
        ivExistDRS = (ImageView)v.findViewById(R.id.ivExistDRS);
        //ivIdExist = (ImageView)v.findViewById(R.id.ivIdExist);
        //ivIdnotExist = (ImageView)v.findViewById(R.id.ivIdnotExist);
        idnotExist = (ImageView)v.findViewById(R.id.idnotExist);
        idExist = (ImageView)v.findViewById(R.id.idExist);
        idExistNew = (ImageView)v.findViewById(R.id.idExistNew);
        idnotExistnew = (ImageView)v.findViewById(R.id.idnotExistnew);
        idExistReplace = (ImageView)v.findViewById(R.id.idExistReplace);
        idnotExistReplace =(ImageView)v.findViewById(R.id.idnotExistReplace);
//        idExistReplaceDRS = (ImageView)v.findViewById(R.id.idExistReplaceDRS);
//        idnotExistReplaceDRS = (ImageView)v.findViewById(R.id.idnotExistReplaceDRS);
//        idExistReplaceDRSNew = (ImageView)v.findViewById(R.id.idExistReplaceDRSNew);
//        idnotExistReplaceDRSNew = (ImageView)v.findViewById(R.id.idnotExistReplaceDRSNew);
//        ivIdExistFault = (ImageView)v.findViewById(R.id.ivIdExistFault);
//        ivIdnotExistFault = (ImageView)v.findViewById(R.id.ivIdnotExistFault);
        ivExistRemove = (ImageView)v.findViewById(R.id.ivExistRemove);
        ivnotExistRemove = (ImageView)v.findViewById(R.id.ivnotExistRemove);
        workType = (MySearchableSpinner)v.findViewById(R.id.new_in_workType);
        linearReplacement = (LinearLayout)v.findViewById(R.id.linearReplacement);
        linearInstall = (LinearLayout)v.findViewById(R.id.linearInstall);
        linearReInstall = (LinearLayout)v.findViewById(R.id.linearReInstall);
        relaydrsType = (RelativeLayout)v.findViewById(R.id.relaydrsType);
        location = (MySearchableSpinner)v.findViewById(R.id.new_in_locations);
        old_deviceid = (EditText)v.findViewById(R.id.old_deviceid);
        new_deviceid = (EditText)v.findViewById(R.id.new_deviceid);
        new_vehicleRegNo = (EditText)v.findViewById(R.id.new_vehicleRegNo);
        linearRemoval = (LinearLayout)v.findViewById(R.id.linearRemoval);
        reason_replace = (MySearchableSpinner)v.findViewById(R.id.replace_reason);
        reason_remove = (MySearchableSpinner)v.findViewById(R.id.removal_Reason);
        lv = (ListView)v.findViewById(R.id.other_list);
        lvItem = (ListView)v.findViewById(R.id.item_list);
       // pDialogText = (ProgressBar)v.findViewById(R.id.progress_with_text);
        progressReinstall = (ProgressBar)v.findViewById(R.id.progressReinstall);
        regNo = (TextView)v.findViewById(R.id.regNo);
        plantName = (TextView)v.findViewById(R.id.plantName);
        linearDrs =(LinearLayout)v.findViewById(R.id.linearDrs);
      //  new_in_vehicleTypeReins = (MySearchableSpinner)v.findViewById(R.id.new_in_vehicleTypeReins);
        remove_deviceid = (EditText)v.findViewById(R.id.remove_deviceid);
        remove_reg_no = (EditText)v.findViewById(R.id.remove_reg_no);
        linearFault = (LinearLayout)v.findViewById(R.id.linearFault);
        device_info =(MyTextView)v.findViewById(R.id.device_info);
        old_deviceidreplace = (EditText) v.findViewById(R.id.old_deviceidreplace);
        old_Device = (RadioButton)v.findViewById(R.id.old_Device);
        new_Device = (RadioButton)v.findViewById(R.id.new_Device);
        new_deviceidReinstall = (EditText) v.findViewById(R.id.new_deviceidReinstall);
        old_drsid = (EditText)v.findViewById(R.id.old_drsid);
        new_drsid = (EditText)v.findViewById(R.id.new_drsid);
        relaydrsTypeReplace = (RelativeLayout)v.findViewById(R.id.relaydrsTypeReplace);
        drsReplace = (RadioGroup)v.findViewById(R.id.radiodrsReplace);
        radiodireplace = (RadioGroup)v.findViewById(R.id.radiodireplace);
        check_tel_supprt = (CheckBox)v.findViewById(R.id.check_tel_supprt);
        fault_vts_id = (EditText)v.findViewById(R.id.fault_vts_id);
        progressBar = (RelativeLayout)v.findViewById(R.id.llayoutProgress);
        t_install_Time.setInputType(InputType.TYPE_NULL);
        t_install_date.setInputType(InputType.TYPE_NULL);

        ShowProgressBar(false);
        pDialogText.setVisibility(View.GONE);
        addclients();
        addLocation();
        addVehType();
        addWorkType();
        setDateAndTime();
        location.setEnabled(false);
        workType.setEnabled(false);
        if (chk.isConnected()) {
        }else {
            chk.showConnectionErrorDialog();
        }
        e_remarks.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (e_remarks.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
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
                }
                clientId = String.valueOf(clientList.get(i).getClient_Id());
                s_clientname = clientList.get(i).getClient_Name();
                Log.i("idd",""+clientId+s_clientname);
                location.setEnabled(true);
                addLocation();
                drsStatus = String.valueOf(clientList.get(i).getDrs_status());
                if(drsStatus.equals("1")) {
                    linearDrs.setVisibility(View.VISIBLE);
                } else {
                    linearDrs.setVisibility(View.GONE);
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
                Log.i("idd", "" + clientId);
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
                Log.i("idd",""+removalReason);
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
                Log.i("id vehicle",""+s_Vehicle_Name);
                if((vehicleType.getSelectedItem().toString().equalsIgnoreCase("TM"))) {
                    linearDrs.setVisibility(View.VISIBLE);
                }else {
                    linearDrs.setVisibility(View.GONE);
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
                }
                s_vehicletype = String.valueOf(vehicleList.get(i).getVehicle_Id());
                s_Vehicle_Name = vehicleList.get(i).getVehicle_Name();
                Log.i("id vehicle",""+s_Vehicle_Name);
                if((drsStatus.equals("1")&&(new_in_vehicleTypeReins.getSelectedItem().toString().equalsIgnoreCase("TM")))) {
                    linearDrs.setVisibility(View.VISIBLE);
                }else {
                    linearDrs.setVisibility(View.GONE);
                }
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
                s_work_id = String.valueOf(workTypeList.get(i).getWork_Id());
                Log.e("", "work Type" + s_work_type + s_work_id);
                if (s_work_type.equalsIgnoreCase("Replacement")) {
                    device_info.setText("Replacement Details");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            e_drs_id.setText("");
                            e_reg_no.setText("");
                            e_device_id.setText("");
                            e_remarks.setText("");
                            fault_vts_id.setText("");
                            remove_deviceid.setText("");
                            linearReplacement.setVisibility(View.VISIBLE);
                            linearInstall.setVisibility(View.GONE);
                            linearReInstall.setVisibility(View.GONE);
                            linearRemoval.setVisibility(View.GONE);
                            linearDrs.setVisibility(View.GONE);
                            linearFault.setVisibility(View.GONE);
                            fault_vts_id.setVisibility(View.GONE);
                            check_tel_supprt.setVisibility(View.GONE);
                            fetchReasons();
                            ShowProgressBar(false);
                        }
                    }, DELAY);
                    old_deviceidreplace.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                vts_id= old_deviceidreplace.getText().toString();
                                getVTSDetail();
                            } else{
                                regNo.setText("");
                                plantName.setText("");
                            }
                        }
                    });
                    drsReplace.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            if(i == R.id.radioyesdrsReplace){
                                old_drsid.setVisibility(View.VISIBLE);
                                new_drsid.setVisibility(View.VISIBLE);
                                relaydrsTypeReplace.setVisibility(View.VISIBLE);
                            }else {
                                old_drsid.setVisibility(View.GONE);
                                new_drsid.setVisibility(View.GONE);
                                relaydrsTypeReplace.setVisibility(View.GONE);
                            }
                        }
                        //    });
//                        new_deviceid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                            @Override
//                            public void onFocusChange(View v, boolean hasFocus) {
//                                if (!hasFocus) {
//                                    progressReplace.setVisibility(View.GONE);
//                                    vts_id= new_deviceid.getText().toString();
//                                    if(vts_id.equals("")){
//                                    } else {progressReplace.setVisibility(View.VISIBLE);
//                                        getVTSDetailNew();}
//                                } else{
//                                    idExistReplace.setVisibility(View.GONE);
//                                    idnotExistReplace.setVisibility(View.GONE);
//                                }
//                            }
//                        });
//                        old_drsid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                            @Override
//                            public void onFocusChange(View v, boolean hasFocus) {
//                                if (!hasFocus) {
//                                    drs_id = old_drsid.getText().toString();
//                                    if(drs_id.equals("")){
//                                    } else {progressReplaceDrs.setVisibility(View.VISIBLE);
//                                        getDRSDetail();
//                                    }
//                                } else{
//                                    idnotExistReplaceDRS.setVisibility(View.GONE);
//                                    idExistReplaceDRS.setVisibility(View.GONE);
//                                }
//                            }
//                        });
//                        new_drsid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                            @Override
//                            public void onFocusChange(View v, boolean hasFocus) {
//                                if (!hasFocus) {
//                                    drs_id = new_drsid.getText().toString();
//                                    if(drs_id.equals("")){
//                                    }else{progressReplaceDrsNew.setVisibility(View.VISIBLE);
//                                        getDRSDetailNew();}
//                                } else{
//                                    idExistReplaceDRSNew.setVisibility(View.GONE);
//                                    idnotExistReplaceDRSNew.setVisibility(View.GONE);
//                                }
//                            }
                    });
                } else if (s_work_type.equalsIgnoreCase("New Installation")) {
                    device_info.setText("Installation Details");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            e_drs_id.setText("");
                            e_reg_no.setText("");
                            e_device_id.setText("");
                            e_remarks.setText("");
                            fault_vts_id.setText("");
                            remove_deviceid.setText("");
                            e_device_id.setInputType(InputType.TYPE_CLASS_NUMBER);
                            linearReplacement.setVisibility(View.GONE);
                            linearInstall.setVisibility(View.VISIBLE);
                            linearReInstall.setVisibility(View.GONE);
                            linearRemoval.setVisibility(View.GONE);
                            pDialogText.setVisibility(View.GONE);
                            progressReinstall.setVisibility(View.GONE);
                            idnotExist.setVisibility(View.GONE);
                            idExist.setVisibility(View.GONE);
                            linearFault.setVisibility(View.GONE);
                            fault_vts_id.setVisibility(View.GONE);
                            check_tel_supprt.setVisibility(View.GONE);
                            ShowProgressBar(false);
                        }
                    }, DELAY);
//                        e_device_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                            @Override
//                            public void onFocusChange(View v, boolean hasFocus) {
//                                if (!hasFocus) {
//                                    vts_id= e_device_id.getText().toString();
//                                    if(vts_id.equals("")){
//                                    } else {
//                                        pDialogText.setVisibility(View.VISIBLE);
//                                        getVTSDetailNew();}
//                                } else{
//                                    ivIdExist.setVisibility(View.GONE);
//                                    ivIdnotExist.setVisibility(View.GONE);
//                                }
//                            }
//                        });
//                        e_drs_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                            @Override
//                            public void onFocusChange(View v, boolean hasFocus) {
//                                if (!hasFocus) {
//                                    drs_id = e_drs_id.getText().toString();
//                                    if(drs_id.equals("")){
//                                    } else {progressdrs.setVisibility(View.VISIBLE);
//                                        getDRSDetail();}
//                                } else{
//                                    ivnotExistDRS.setVisibility(View.GONE);
//                                    ivExistDRS.setVisibility(View.GONE);
//                                }
//                            }
//                        });
                } else if (s_work_type.equalsIgnoreCase("ReInstallation")) {
                    device_info.setText("ReInstallation Details");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            e_drs_id.setText("");
                            new_vehicleRegNo.setText("");
                            new_deviceidReinstall.setText("");
                            e_remarks.setText("");
                            old_deviceid.setText("");
                            fault_vts_id.setText("");
                            remove_deviceid.setText("");
                            linearReplacement.setVisibility(View.GONE);
                            linearInstall.setVisibility(View.GONE);
                            linearReInstall.setVisibility(View.VISIBLE);
                            linearRemoval.setVisibility(View.GONE);
                            if (linearDrs.getVisibility() == View.VISIBLE) {
                                linearDrs.setVisibility(View.VISIBLE);
                            } else {
                                linearDrs.setVisibility(View.GONE);}
                            linearFault.setVisibility(View.GONE);
                            fault_vts_id.setVisibility(View.GONE);
                            check_tel_supprt.setVisibility(View.GONE);
                            ShowProgressBar(false);
                        }
                    }, DELAY);
//                        old_deviceid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                            @Override
//                            public void onFocusChange(View v, boolean hasFocus) {
//                                if (!hasFocus) {
//                                    vts_id= old_deviceid.getText().toString();
//                                    if(vts_id.equals("")){
//                                    } else {progressReinstall.setVisibility(View.VISIBLE);
//                                        getVTSDetailNew();}
//                                } else{
//                                    idExist.setVisibility(View.GONE);
//                                    idnotExist.setVisibility(View.GONE);
//                                }
//                            }
//                        });
//                        new_deviceidReinstall.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                            @Override
//                            public void onFocusChange(View v, boolean hasFocus) {
//                                if (!hasFocus) {
//                                    vts_id= new_deviceidReinstall.getText().toString();
//                                   if(vts_id.equals("")){
//                                       new_deviceidReinstall.setError("fill field");
//                                   }else if(new_deviceidReinstall.getText().toString().equals(old_deviceid.getText().toString())) {
//                                       new_deviceidReinstall.setError("Id can't be same");
//                                       Toast.makeText(getContext(), "Id can't be same", Toast.LENGTH_SHORT).show();
//                                   } else{progressReinstallNew.setVisibility(View.VISIBLE);
//                                       getVTSDetailReins();}
//                                } else{
//                                    idExistNew.setVisibility(View.GONE);
//                                    idnotExistnew.setVisibility(View.GONE);
//                                }
//                            }
//                        });
                    radioGroupReinstall.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            if(i == R.id.old_Device){
                                new_deviceidReinstall.setVisibility(View.GONE);
                            }else {
                                new_deviceidReinstall.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } else if (s_work_type.equalsIgnoreCase("Removal")) {
                    device_info.setText("Removal Details");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            e_drs_id.setText("");
                            e_reg_no.setText("");
                            e_device_id.setText("");
                            e_remarks.setText("");
                            fault_vts_id.setText("");
                            remove_deviceid.setText("");
                            linearReplacement.setVisibility(View.GONE);
                            linearInstall.setVisibility(View.GONE);
                            linearReInstall.setVisibility(View.GONE);
                            linearRemoval.setVisibility(View.VISIBLE);
                            linearDrs.setVisibility(View.GONE);
                            linearFault.setVisibility(View.GONE);
                            fault_vts_id.setVisibility(View.GONE);
                            check_tel_supprt.setVisibility(View.GONE);
                            addReasonRemove();
                            getItemCollectList();
                            ShowProgressBar(false);
                        }
                    }, DELAY);
//                                remove_deviceid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    progressremove.setVisibility(View.VISIBLE);
//                    vts_id= remove_deviceid.getText().toString();
//                    getVTSDetail();
//                }else {
//                    ivnotExistRemove.setVisibility(View.GONE);
//                    ivExistRemove.setVisibility(View.GONE);
//                }
//            }
//        });
                } else if (s_work_type.equalsIgnoreCase("Fault")) {
                    device_info.setText("Fault Details");
                    int DELAY = 1000;
                    ShowProgressBar(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            e_drs_id.setText("");
                            e_reg_no.setText("");
                            e_device_id.setText("");
                            e_remarks.setText("");
                            fault_vts_id.setText("");
                            remove_deviceid.setText("");
                            linearReplacement.setVisibility(View.GONE);
                            linearInstall.setVisibility(View.GONE);
                            linearReInstall.setVisibility(View.GONE);
                            linearRemoval.setVisibility(View.GONE);
                            linearDrs.setVisibility(View.GONE);
                            linearFault.setVisibility(View.VISIBLE);
                            fault_vts_id.setVisibility(View.VISIBLE);
                            check_tel_supprt.setVisibility(View.VISIBLE);
                            getFaultList();
                            ShowProgressBar(false);
                        }
                    }, DELAY);
//                        fault_vts_id.addTextChangedListener(new TextWatcher() {
//                            @Override
//                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                                ivIdExistFault.setVisibility(View.GONE);
//                                ivIdnotExistFault.setVisibility(View.GONE);
//                            }
//                            @Override
//                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                                vts_id= fault_vts_id.getText().toString();
//                                if(vts_id.equals("")){
//                                    ivIdExistFault.setVisibility(View.GONE);
//                                    ivIdnotExistFault.setVisibility(View.GONE);
//                                }else{
//                                    progressfault.setVisibility(View.VISIBLE);
//                                    getVTSDetail();
//                                }
//                            }
//                            @Override
//                            public void afterTextChanged(Editable editable) {
//                                progressfault.setVisibility(View.GONE);
//                            }
//                        });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        reason_replace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                } else {
                    position = position - 1;
                }s_reason_repla = String.valueOf(arr_replaceReasons.get(reason_replace.getSelectedItemPosition()).getReplace_Id());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        update_dataa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s_clientname.equalsIgnoreCase("SELECT Client")||(s_clientname.equals(null))) {
                    Toast.makeText(getContext(), "Please Select Client", Toast.LENGTH_LONG).show();
                }else if(location.getSelectedItem().toString().equalsIgnoreCase("Select Location")) {
                    Toast.makeText(getContext(), "Please Select Location", Toast.LENGTH_LONG).show();
                } else if(workType.getSelectedItem().toString().equalsIgnoreCase("Select Work Type")) {
                    Toast.makeText(getContext(), "Please Select Work Type", Toast.LENGTH_LONG).show();
                } else if (s_work_type.equalsIgnoreCase("New Installation")) {
                    s_drs_id = e_drs_id.getText().toString();
                    s_reg_no = e_reg_no.getText().toString();
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    s_remarks = e_remarks.getText().toString();
                    int selectedType = radiodeviceType.getCheckedRadioButtonId();
                    radiotype = (RadioButton) v.findViewById(selectedType);
                    String type = radiotype.getText().toString();
                    if (type.equalsIgnoreCase("Voice")) {
                        device_type = "1";
                    } else {device_type = "2";}
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    radionormal = (RadioButton) v.findViewById(selectedId);
                    String drs = radionormal.getText().toString();
                    if (drs.equalsIgnoreCase("Normal") && (linearDrs.getVisibility() == View.VISIBLE)) {
                        drs_dirction = "1";
                    } else if (drs.equalsIgnoreCase("Reverse") && (linearDrs.getVisibility() == View.VISIBLE)) {
                        drs_dirction = "2";
                    } s_new_device_id = e_device_id.getText().toString();
                    if(s_new_device_id.equals("")) {
                        e_device_id.setError("fill field");
                    }else if (s_reg_no.equals("") || s_reg_no.equals(null)) {
                        e_reg_no.setError("fill field");
                    }else if(s_work_type.equalsIgnoreCase("Select Work Type")) {
                        Toast.makeText(getContext(), "Please Select Work Type", Toast.LENGTH_LONG).show();
                    }else if((vehicleType.getSelectedItem().toString().equalsIgnoreCase("Select Vehicle Type"))) {
                        Toast.makeText(getContext(), "Please Select Vehicle type", Toast.LENGTH_LONG).show();
                    }else if ((linearDrs.getVisibility() == View.VISIBLE) && (s_drs_id.equals(""))) {
                        e_drs_id.setError("fill Field");
                    } else if ((linearDrs.getVisibility() == View.VISIBLE)) {
                        s_drs_id = e_drs_id.getText().toString();
                        s_new_drs_id="0";
                        updateInstallationData();
                    } else{
                        drs_dirction = "0";
                        s_drs_id="0";
                        s_new_drs_id="0";
                        updateInstallationData();
                    }
                } else if(s_work_type.equalsIgnoreCase("ReInstallation")) {
                    s_reg_no = new_vehicleRegNo.getText().toString();
                    s_remarks = e_remarks.getText().toString();
                    s_drs_id = e_drs_id.getText().toString();
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    if(new_deviceidReinstall.getVisibility()== View.VISIBLE) {
                        s_new_device_id = new_deviceidReinstall.getText().toString();
                    }else {s_new_device_id="0";}
                    int selectedType = radioGroupReinstall.getCheckedRadioButtonId();
                    radiotype = (RadioButton) v.findViewById(selectedType);
                    String type = radiotype.getText().toString();
                    if (type.equalsIgnoreCase("Old")) {
                        device_type = "1";
                    } else {device_type = "2";}
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    radionormal = (RadioButton) v.findViewById(selectedId);
                    String drs = radionormal.getText().toString();
                    if (drs.equalsIgnoreCase("Normal") && (linearDrs.getVisibility() == View.VISIBLE)) {
                        drs_dirction = "1";
                    } else if (drs.equalsIgnoreCase("Reverse") && (linearDrs.getVisibility() == View.VISIBLE)) {
                        drs_dirction = "2";
                    } else {drs_dirction = "0";}
                    s_VehicleTypeInst = new_in_vehicleTypeReins.getSelectedItem().toString();
                    s_e_device_id = old_deviceid.getText().toString();
                    if (s_e_device_id.equals("")) {
                        old_deviceid.setError("fill field");
                    } else if (s_new_device_id.equals("")&&(new_deviceidReinstall.getVisibility()==View.VISIBLE)) {
                        new_deviceidReinstall.setError("fill field");
                    }else if(s_reg_no.equals("")){
                        new_vehicleRegNo.setError("fill field");
                    } else if((s_VehicleTypeInst.equalsIgnoreCase("Select Vehicle Type"))) {
                        Toast.makeText(getContext(), "please select vehicle type", Toast.LENGTH_LONG).show();
                    }else if(new_deviceidReinstall.getVisibility()==View.VISIBLE) {
                        s_new_device_id = new_deviceidReinstall.getText().toString();
                        s_drs_id="0";
                        s_new_drs_id="0";
                        if(new_deviceidReinstall.getText().toString().equals(old_deviceid.getText().toString()))
                        {
                            new_deviceidReinstall.setError("Id can't be same");
                            Toast.makeText(getContext(), "Id can't be same", Toast.LENGTH_SHORT).show();
                        }else{
                            s_new_device_id = new_deviceidReinstall.getText().toString();
                            s_drs_id="0";
                            s_new_drs_id="0";
                            updateInstallationData();}
                    }else if ((linearDrs.getVisibility() == View.VISIBLE) && (s_drs_id.equals(""))) {
                        e_drs_id.setError("fill Field");
                    }else if ((linearDrs.getVisibility() == View.VISIBLE)) {
                        s_drs_id = e_drs_id.getText().toString();
                        s_new_device_id = "0";
                        updateInstallationData();
                    }else{
                        s_new_drs_id="0";
                        s_new_device_id = "0";
                        s_drs_id="0";
                        updateInstallationData();
                    }
                } else if(s_work_type.equalsIgnoreCase("Replacement")) {
                    s_new_device_id = new_deviceid.getText().toString();
                    int selectedId = radiodireplace.getCheckedRadioButtonId();
                    radionormalrep = (RadioButton) v.findViewById(selectedId);
                    String drs = radionormalrep.getText().toString();
                    if (drs.equalsIgnoreCase("Normal") && (relaydrsTypeReplace.getVisibility() == View.VISIBLE)) {
                        drs_dirction = "1";
                    } else if (drs.equalsIgnoreCase("Reverse") && (relaydrsTypeReplace.getVisibility() == View.VISIBLE)) {
                        drs_dirction = "2";
                    } else {
                        drs_dirction = "0";
                    }
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    int selectedType = drsReplace.getCheckedRadioButtonId();
                    radiotype = (RadioButton) v.findViewById(selectedType);
                    String type = radiotype.getText().toString();
                    if (type.equalsIgnoreCase("YES")) {
                        is_drs = "1";
                    } else {
                        is_drs = "0";
                    }
                    int selectedTypeDRS = radiodeviceType.getCheckedRadioButtonId();
                    radiotype = (RadioButton) v.findViewById(selectedTypeDRS);
                    String types = radiotype.getText().toString();
                    if (types.equalsIgnoreCase("Voice")) {
                        device_type = "1";
                    } else {
                        device_type = "2";
                    }
                    s_e_device_id = old_deviceidreplace.getText().toString();
                    if (s_e_device_id.equals("")) {
                        old_deviceidreplace.setError("fill field");
                    } else if (s_new_device_id.equals("")) {
                        new_deviceid.setError("fill field");
                    }
                    if ((old_drsid.getVisibility() == View.VISIBLE) && (new_drsid.getVisibility() == View.VISIBLE)) {
                        if (old_drsid.getText().toString().equals(new_drsid.getText().toString())) {
                            new_drsid.setError("Id Could not be same");
                        } else {
                            s_new_drs_id = old_drsid.getText().toString();
                            s_drs_id = new_drsid.getText().toString();
                            updateInstallationData();
                        }
                    } else {
                        s_drs_id = "0";
                        s_new_drs_id = "0";
                        s_remarks = e_remarks.getText().toString();
                        updateInstallationData();
                    }
                } else if(s_work_type.equalsIgnoreCase("Removal")) {
                    s_e_device_id = remove_deviceid.getText().toString();
                    s_reg_no = remove_reg_no.getText().toString();
                    s_remove_reason = reason_remove.getSelectedItem().toString();
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    if (s_e_device_id.equals("")) {
                        remove_deviceid.setError("fill field");
                    } else if (s_reg_no.equals("")) {
                        remove_reg_no.setError("fill field");
                    }else if (s_remove_reason.equalsIgnoreCase("Select Removal Reason")) {
                        Toast.makeText(getContext(), "please select Reason", Toast.LENGTH_LONG).show();
                    }else if(ivnotExistRemove.getVisibility()==View.VISIBLE){
                    } else{
                        s_new_drs_id="0";
                        s_new_device_id = "0";
                        s_drs_id="0";
                        s_device_id = "0";
                        device_type = "0";
                        s_remarks = e_remarks.getText().toString();
                        SparseBooleanArray checked = lvItem.getCheckedItemPositions();
                        itemsCollected = "";
                        Log.i("***values", String.valueOf(checked) + "count" + lv.getCheckedItemCount());
                        for (int i = 0; i < checked.size(); i++) {
                            int key = checked.keyAt(i);
                            itemsCollected = itemsCollected + (collected_items.get(key).getId()) + ":";
                        }Log.i("***values", itemsCollected);
                        updateInstallationData();}

                } else if(s_work_type.equalsIgnoreCase("Fault")) {
                    s_e_device_id=fault_vts_id.getText().toString();
                    if(check_tel_supprt.isChecked())
                    {   tel_support="1";
                    }else {tel_support="";}
                    s_device_id="0";s_drs_id="0";s_new_drs_id="0";status="0";s_VehicleTypeInst="0";
                    s_reason_repla="0";removalReason="0";itemsCollected="";s_new_device_id="0";is_drs="1";drs_dirction="0";device_type="0";
                    s_date = t_install_date.getText().toString();
                    s_Time = t_install_Time.getText().toString();
                    SparseBooleanArray checked = lv.getCheckedItemPositions();
                    others = "";
                    Log.i("***values", String.valueOf(checked) + "count" + lv.getCheckedItemCount());
                    for (int i = 0; i < checked.size(); i++) {
                        int key = checked.keyAt(i);
                        others = others + (list_change_values.get(key).getId()) + ":";
                    }Log.i("***values", others);
                    s_remarks = e_remarks.getText().toString();
                    if (s_e_device_id.equals("")) {
                        fault_vts_id.setError("fill field");
                    }else if (others.equals("")) {
                        Toast.makeText(getActivity(), "select changed values", Toast.LENGTH_SHORT).show();
                    } else if(ivIdnotExistFault.getVisibility()==View.VISIBLE){}
                    else {
                        s_remarks = e_remarks.getText().toString();
                        updateInstallationData();
                    }
                }
            }
        });
        return v;
    }
    private void getDRSDetailNew() {
        progressReplaceDrsNew.setVisibility(View.VISIBLE);
        ApiHolder loc_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<DRSResponse> locCall = loc_att.reqeuestDrsDetail(drs_id);
        locCall.enqueue(new Callback<DRSResponse>() {
            public void onResponse(Call<DRSResponse> call, Response<DRSResponse> response) {
                DRSResponse workTypeResponse = response.body();
                drsList = response.body().getDrsDetails();
                Log.i("**work respnse", " " + response.body());
                if ((drsList.size() == 0)) {
                    progressReplaceDrsNew.setVisibility(View.GONE);
                    idExistReplaceDRSNew.setVisibility(View.VISIBLE);
                } else {
                    progressReplaceDrsNew.setVisibility(View.GONE);
                    idnotExistReplaceDRSNew.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "DRS ID already Exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<DRSResponse> call, Throwable t) {
                try {
                } catch (Exception e) {
                }
            }
        });
    }
    private void getDRSDetail() {
        progressdrs.setVisibility(View.VISIBLE);
        ApiHolder loc_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<DRSResponse> locCall = loc_att.reqeuestDrsDetail(drs_id);
        locCall.enqueue(new Callback<DRSResponse>() {
            public void onResponse(Call<DRSResponse> call, Response<DRSResponse> response) {
                DRSResponse workTypeResponse = response.body();
                drsList = response.body().getDrsDetails();
                Log.i("**work respnse", " " + response.body());
                if (s_work_type.equalsIgnoreCase("Installation")) {
                    if ((drsList.size() == 0)) {
                        progressdrs.setVisibility(View.GONE);
                        ivnotExistDRS.setVisibility(View.VISIBLE);
                    } else {
                        progressdrs.setVisibility(View.GONE);
                        ivExistDRS.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "DRS ID already Exist", Toast.LENGTH_SHORT).show();
                    }
                } else if (s_work_type.equalsIgnoreCase("Replacement")) {
                    if ((drsList.size() == 0) && (drs_id != null)) {
                        progressReplaceDrs.setVisibility(View.GONE);
                        idnotExistReplaceDRS.setVisibility(View.VISIBLE);
                    } else {
                        progressReplaceDrs.setVisibility(View.GONE);
                        idExistReplaceDRS.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onFailure(Call<DRSResponse> call, Throwable t) {
                try {
                } catch (Exception e) {
                }
            }
        });
    }
    private void getVTSDetailNew() {
        ApiHolder loc_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<VTSResponse> locCall = loc_att.reqeuestVtsDetail(vts_id);
        locCall.enqueue(new Callback<VTSResponse>() {
            public void onResponse(Call<VTSResponse> call, Response<VTSResponse> response) {
                VTSResponse workTypeResponse = response.body();
                vtsList = response.body().getVtsDetails();
                Log.i("**work respnse", " " + response.body());
                if(s_work_type.equalsIgnoreCase("Installation")){
                    if ((vtsList.size() == 0)) {
//                    pDialogText.setVisibility(View.GONE);
//                    ivIdnotExist.setVisibility(View.VISIBLE);
                    }
                    //else {
//                    pDialogText.setVisibility(View.GONE);
//                    ivIdExist.setVisibility(View.VISIBLE);
//                    Toast.makeText(getContext(), "Device Id already exist", Toast.LENGTH_SHORT).show();
                    //  }
//            } else if(s_work_type.equalsIgnoreCase("ReInstallation"))
//                {
//                    if (vtsList.size() == 0) {
//                        progressReinstall.setVisibility(View.GONE);
//                        idnotExist.setVisibility(View.VISIBLE);
//                        Toast.makeText(getContext(), "ID doesn't Exist", Toast.LENGTH_SHORT).show();
//                    } else {
//                        progressReinstall.setVisibility(View.GONE);
//                        idExist.setVisibility(View.VISIBLE);
//                    }
//                } else if(s_work_type.equalsIgnoreCase("Replacement"))
//                {if (vtsList.size() == 0) {
//                    progressReplace.setVisibility(View.GONE);
//                    idExistReplace.setVisibility(View.VISIBLE);
//                } else {
//                    progressReplace.setVisibility(View.GONE);
//                    idnotExistReplace.setVisibility(View.VISIBLE);
//                    Toast.makeText(getContext(), "Id Dosen't Exist", Toast.LENGTH_SHORT).show();}
                }
            }
            @Override
            public void onFailure(Call<VTSResponse> call, Throwable t) {
                try {
                } catch (Exception e) {
                }}
        });
    }
    private void getVTSDetailReins() {
        if(s_work_type.equalsIgnoreCase("Reinstallation"))
            progressReinstallNew.setVisibility(View.VISIBLE);
        ApiHolder loc_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<VTSResponse> locCall = loc_att.reqeuestVtsDetail(vts_id);
        locCall.enqueue(new Callback<VTSResponse>() {
            public void onResponse(Call<VTSResponse> call, Response<VTSResponse> response) {
                VTSResponse workTypeResponse = response.body();
                vtsList = response.body().getVtsDetails();
                Log.i("**work respnse", " " + response.body());
                if (vtsList.size() == 0) {
                    progressReinstallNew.setVisibility(View.GONE);
                    idExistNew.setVisibility(View.VISIBLE);
                } else {
                    progressReinstallNew.setVisibility(View.GONE);
                    idnotExistnew.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Id already exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<VTSResponse> call, Throwable t) {
                try {
                } catch (Exception e) {
                }
            }
        });
    }
    private void getVTSDetail() {
        ApiHolder loc_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<VTSResponse> locCall = loc_att.reqeuestVtsDetail(vts_id);
        locCall.enqueue(new Callback<VTSResponse>() {
            public void onResponse(Call<VTSResponse> call, Response<VTSResponse> response) {
                VTSResponse workTypeResponse = response.body();
                vtsList = response.body().getVtsDetails();
                Log.i("**work respnse", " " + response.body());
                if(vtsList.size()==0) {
                } else {
                    for (int i = 0; i < vtsList.size(); i++) {
                        clientId = String.valueOf(vtsList.get(i).getClient_id());
                        s_reg_no = String.valueOf(vtsList.get(i).getReg_no());
                        s_clientname = vtsList.get(i).getClient_name();
                        s_vehicletype = vtsList.get(i).getVeh_type_id();
                        regNo.setText(s_reg_no);
                        plantName.setText(s_clientname);
                    }
//                }else if(s_work_type.equalsIgnoreCase("Fault")) {
//                    if (vtsList.size() == 0) {
//                    progressfault.setVisibility(View.GONE);
//                    ivIdnotExistFault.setVisibility(View.VISIBLE);
//                } else {
//                    progressfault.setVisibility(View.GONE);
//                    ivIdExistFault.setVisibility(View.VISIBLE);
//                    for (int i = 0; i < vtsList.size(); i++) {
//                        clientId = String.valueOf(vtsList.get(i).getClient_id());
//                        s_reg_no = String.valueOf(vtsList.get(i).getReg_no());
//                        s_clientname = vtsList.get(i).getClient_name();
//                        s_vehicletype = vtsList.get(i).getVeh_type_id();
//                    }
//                }
//                }else if(s_work_type.equalsIgnoreCase("Removal"))
//                {if (vtsList.size() == 0) {
//                    progressremove.setVisibility(View.GONE);
//                    ivnotExistRemove.setVisibility(View.VISIBLE);
//                    Toast.makeText(getContext(), "Id Dosen't Exist", Toast.LENGTH_SHORT).show();
//                } else {
//                    progressremove.setVisibility(View.GONE);
//                    ivExistRemove.setVisibility(View.VISIBLE);
//                    for (int i = 0; i < vtsList.size(); i++) {
//                        clientId = String.valueOf(vtsList.get(i).getClient_id());
//                        s_clientname = vtsList.get(i).getClient_name();
//                        s_vehicletype = vtsList.get(i).getVeh_type_id();
//                    }
//                }
                }
            }
            @Override
            public void onFailure(Call<VTSResponse> call, Throwable t) {
                try {
                } catch (Exception e) {
                }
            }
        });
    }
    private void getFaultList() {
//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Updating list...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        ApiHolder loc_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
//        //Call<FaultResponse> locCall = loc_att.reqeuestWorkList();
//        locCall.enqueue(new Callback<FaultResponse>() {
//            public void onResponse(Call<FaultResponse> call, Response<FaultResponse> response) {
//                FaultResponse workTypeResponse = response.body();
//                list_change_values = response.body().getFaultLists();
//                Log.i("**work respnse", " " + response.body());
//                pDialog.hide();
//                try {
//                    try {
//                        value_name.clear();
//                    } catch (Exception e) {
//                        e.printStackTrace();}
//                    if (list_change_values.size() > 0) {
//                        for (int i = 0; i < list_change_values.size(); i++) {
//                            value_name.add(list_change_values.get(i).getDescp());}
//                        if (list_change_values.size() > 5) {
//                            lv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * list_change_values.size() + 1));}
//                        else {
//                            lv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * list_change_values.size()));}
//                        adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_list_item, value_name);
//                        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//                        lv.setAdapter(adapter);
//                    } else {}
//                }catch (NullPointerException npe) {
//                    npe.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(Call<FaultResponse> call, Throwable t) {
//                try {
//                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
//                    View snackbarView = snackbar.getView();
//                    snackbarView.setBackgroundColor(Color.RED);
//                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
//                    textView.setTextColor(Color.WHITE);
//                    snackbar.show();
//                } catch (Exception e) {
//                    //Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }
    private void getItemCollectList() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Updating list...");
        pDialog.setCancelable(false);
        pDialog.show();
        ApiHolder loc_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<CollectedItemsResponse> locCall = loc_att.reqeuestCollectedItemList();
        locCall.enqueue(new Callback<CollectedItemsResponse>() {
            public void onResponse(Call<CollectedItemsResponse> call, Response<CollectedItemsResponse> response) {
                CollectedItemsResponse workTypeResponse = response.body();
                collected_items = response.body().getItemLists();
                Log.i("**work respnse", " " + response.body());
                pDialog.hide();
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
                            lvItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * collected_items.size() + 1));}
                        else {
                            lvItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * collected_items.size() + 1));}
                        adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_list_item, item_name);
                        lvItem.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                        lvItem.setAdapter(adapter);
                    } else {}
                }catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<CollectedItemsResponse> call, Throwable t) {
                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                    //Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void updateInstallationData() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Updating DATA...");
        pDialog.show();
        ApiHolder loc_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
//        locCall = loc_att.postInstallationData(s_work_id,uusername,clientId,clientLocId,
//                s_reg_no,s_vehicletype,device_type,s_e_device_id,s_new_device_id,is_drs,s_drs_id,s_new_drs_id,drs_dirction,
//                s_reason_repla,removalReason,itemsCollected,others,tel_support,s_date,s_Time,s_remarks,disconnection_reason,ignition_sensor,fuel_sensor,door_sensor,veh_condition,mgt_set,sim_provider,old_sim_no,new_sim_no,sim_reason);
        locCall.enqueue(new Callback<InstallResponse>() {
            public void onResponse(Call<InstallResponse> call, Response<InstallResponse> response) {
                InstallResponse workTypeResponse = response.body();
                Log.i("**work respnse", " " + response.body());
                Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                if (response.body().getType().equals("0")) {

                } else {
                    pDialog.hide();
                    e_device_id.setText("");
                    e_reg_no.setText("");
                    e_remarks.setText("");
                    e_drs_id.setText("");
                    fault_vts_id.setText("");
                    old_deviceidreplace.setText("");
                    new_deviceid.setText("");
                    old_drsid.setText("");
                    new_drsid.setText("");
                    old_deviceid.setText("");
                    new_deviceidReinstall.setText("");
                    new_vehicleRegNo.setText("");
                    remove_deviceid.setText("");
                    remove_reg_no.setText("");
                    progressdrs.setVisibility(View.GONE);
                    ivnotExistDRS.setVisibility(View.GONE);
                    ivExistDRS.setVisibility(View.GONE);
                    pDialogText.setVisibility(View.GONE);
                    ivIdnotExist.setVisibility(View.GONE);
                    ivIdExist.setVisibility(View.GONE);
                    progressReinstall.setVisibility(View.GONE);
                    progressReinstallNew.setVisibility(View.GONE);
                    idExist.setVisibility(View.GONE);
                    idnotExist.setVisibility(View.GONE);
                    idExistNew.setVisibility(View.GONE);
                    idnotExistnew.setVisibility(View.GONE);
                    regNo.setText("");
                    plantName.setText("");
                    progressReplace.setVisibility(View.GONE);
                    idExistReplace.setVisibility(View.GONE);
                    idnotExistReplace.setVisibility(View.GONE);
                    progressReplaceDrs.setVisibility(View.GONE);
                    idExistReplaceDRS.setVisibility(View.GONE);
                    idnotExistReplaceDRS.setVisibility(View.GONE);
                    progressReplaceDrsNew.setVisibility(View.GONE);
                    idExistReplaceDRSNew.setVisibility(View.GONE);
                    idnotExistReplaceDRSNew.setVisibility(View.GONE);
                    progressremove.setVisibility(View.GONE);
                    ivExistRemove.setVisibility(View.GONE);
                    ivnotExistRemove.setVisibility(View.GONE);
                    progressfault.setVisibility(View.GONE);
                    ivIdExistFault.setVisibility(View.GONE);
                    ivIdnotExistFault.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<InstallResponse> call, Throwable t) {
                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                    //Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void addReasonRemove() {
        ApiHolder work_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<RemovalResponse> workCall = work_att.reqeuestRemovalReason();
        workCall.enqueue(new Callback<RemovalResponse>() {
            public void onResponse(Call<RemovalResponse> call, Response<RemovalResponse> response) {
                RemovalResponse workTypeResponse = response.body();
                removalList = response.body().getRemovalList();
                Log.i("**work respnse", " " + workTypeResponse);
                // pDialog.dismiss();
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
                    adapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_custom_spinner_item, removalDetail);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    reason_remove.setAdapter(adapter);
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<RemovalResponse> call, Throwable t) {
                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                    //Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void fetchReasons() {
        ApiHolder get_list = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<ReplaceReason> call = get_list.reqeuestReplaceReason();
        call.enqueue(new Callback<ReplaceReason>() {
            @Override
            public void onResponse(Call<ReplaceReason> call, Response<ReplaceReason> response) {
                try {
                    arr_replaceReasons = response.body().getReplaceList();
                    try {
                        try {
                            arr_reasons_s.clear();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }arr_reasons_s.add("SELECT REASON");
                        for (int i = 0; i < arr_replaceReasons.size(); i++) {
                            arr_reasons_s.add(arr_replaceReasons.get(i).getReplace_Name());
                        }
                        adapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_custom_spinner_item, arr_reasons_s);
                        reason_replace.setAdapter(adapter);
                    } catch (NullPointerException npe) {
                        npe.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "No data", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ReplaceReason> call, Throwable t) {
                t.printStackTrace();
                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                    //Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void addLocation() {
        ApiHolder loc_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<ClientLocationResponse> locCall = loc_att.reqeuestClientLocation(clientId);
        locCall.enqueue(new Callback<ClientLocationResponse>() {
            public void onResponse(Call<ClientLocationResponse> call, Response<ClientLocationResponse> response) {
                ClientLocationResponse workTypeResponse = response.body();
                locationList = response.body().getClientLoc();
                Log.i("**work respnse", " " + response.body());
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
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item,locationDetail);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    location.setAdapter(adapter);
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ClientLocationResponse> call, Throwable t) {
                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                    //Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void addWorkType() {
        ApiHolder work_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<WorkTypeResponse> workCall = work_att.reqeuestworkType();
        workCall.enqueue(new Callback<WorkTypeResponse>() {
            public void onResponse(Call<WorkTypeResponse> call, Response<WorkTypeResponse> response) {
                WorkTypeResponse workTypeResponse = response.body();
                workTypeList = response.body().getWorktypeList();
                Log.i("**work respnse", " " + workTypeResponse);
                // pDialog.dismiss();
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
                    adapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_custom_spinner_item, workDetail);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    workType.setAdapter(adapter);

                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<WorkTypeResponse> call, Throwable t) {
                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                    //Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void setDateAndTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        s_time = dateFormat.format(calen.getTime());
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        if (month + 1 < 10) {
            current_date = day + "-0" + +month + "-" + year;
        }else {
            current_date = day + "-" + month + "-" + year;
        }
        t_install_date.setText(current_date);
        t_install_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });
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
        },hour,minutes,true);
        tpd.show();
    }
    private void getDate() {
        Log.i("***string**", t_install_date.getText().toString());
        DatePickerDialog dpdd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
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
        //calen.add(Calendar.DATE, -6);
        dpdd.getDatePicker().setMaxDate(calen.getTimeInMillis());
        calen.add(Calendar.DATE, -5);
        dpdd.getDatePicker().setMinDate(calen.getTimeInMillis());
        dpdd.show();
    }
    private void addVehType() {
        ApiHolder veh_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<VehicleTypeResponse> vehCall = veh_att.reqeuestvehicleType();
        vehCall.enqueue(new Callback<VehicleTypeResponse>() {
            public void onResponse(Call<VehicleTypeResponse> call, Response<VehicleTypeResponse> response) {
                VehicleTypeResponse workTypeResponse = response.body();
                vehicleList = response.body().getVehicletypeList();
                Log.i("**workclientrespnse", " " + response.body());
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

                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<VehicleTypeResponse> call, Throwable t) {
                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                    //Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void addclients() {
        ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<ClientResponse> clientCall = client_att.reqeuestClientList();
        clientCall.enqueue(new Callback<ClientResponse>() {
            public void onResponse(Call<ClientResponse> call, Response<ClientResponse> response) {
                ClientResponse workTypeResponse = response.body();
                clientList = response.body().getClientList();
                Log.i("**workclientrespnse", " " + clientList);
                try {
                    try {
                        clientDetail.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < clientList.size(); i++) {
                        clientDetail.add(clientList.get(i).getClient_Name());
                    }
                    List<String> name = new ArrayList<String>();
                    Collection c = clientDetail;
                    Iterator itr = c.iterator();
                    name.add(" SELECT CLIENT");
                    while (itr.hasNext()) {
                        String temp = itr.next().toString();
                        Log.i("TEMP", "" + temp);
                        name.add(temp);
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, name);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    client.setAdapter(adapter);

                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ClientResponse> call, Throwable t) {
                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                    //Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void ShowProgressBar(boolean show) {
        try {
            if (show) {
                progressBar.setVisibility(View.VISIBLE);
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                progressBar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }}
        catch (Exception e) {
            e.printStackTrace();
        }}
}

