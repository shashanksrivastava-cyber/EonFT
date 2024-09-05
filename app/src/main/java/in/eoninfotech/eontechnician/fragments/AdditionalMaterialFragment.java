package in.eoninfotech.eontechnician.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.robinhood.ticker.TickerView;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.MaterialReturnFragment;
import in.eoninfotech.eontechnician.NonScrollListView;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.callbacks.ClientListener;
import in.eoninfotech.eontechnician.callbacks.ReceiveDeviceListener;
import in.eoninfotech.eontechnician.controllers.NewInstallmentController;
import in.eoninfotech.eontechnician.controllers.ReceiveDeviceController;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.responses.ClientLocationResponse;
import in.eoninfotech.eontechnician.responses.ClientResponse;
import in.eoninfotech.eontechnician.responses.CollectedItemsResponse;
import in.eoninfotech.eontechnician.responses.DeviceList;
import in.eoninfotech.eontechnician.responses.DeviceTypeOtherAis;
import in.eoninfotech.eontechnician.responses.DisconnectionResponse;
import in.eoninfotech.eontechnician.responses.FaultResponse;
import in.eoninfotech.eontechnician.responses.ItemList;
import in.eoninfotech.eontechnician.responses.MainClientList;
import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.responses.NotAvailActivityResponse;
import in.eoninfotech.eontechnician.responses.PaymentMethodResponse;
import in.eoninfotech.eontechnician.responses.RemovalActivityResponse;
import in.eoninfotech.eontechnician.responses.RemovalResponse;
import in.eoninfotech.eontechnician.responses.ReplaceReason;
import in.eoninfotech.eontechnician.responses.SimOperatorResponse;
import in.eoninfotech.eontechnician.responses.SimReplaceResponse;
import in.eoninfotech.eontechnician.responses.TransitList;
import in.eoninfotech.eontechnician.responses.VTSResponse;
import in.eoninfotech.eontechnician.responses.VehNotAvailReasonResponse;
import in.eoninfotech.eontechnician.responses.VehicleTypeResponse;
import in.eoninfotech.eontechnician.responses.WorkTypeResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.AttResponse;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdditionalMaterialFragment extends Fragment implements ReceiveDeviceListener, ClientListener {
    View v,rowView;
    Spinner new_main_clients,device_type,accessory_spinner;
    ArrayAdapter<String> adapter;
    private ProgressDialog pDialog;
    NonScrollListView lv;
    Button update_data,final_submit, final_cancel,addMaterial,addAccessory;
    ImageButton delete_button,acc_delete_button;
    SharedPreferences sharedprefs;
    LinearLayout parentLinearLayout,accessory_linear_layout, transit_linear, materialLL,linearField;
    Button btCancel,btSubmit,add_manual;
    TextView response_comment,items_values,preview_tags,items_tags,preview_values;
    TickerView tickerView;
    EditText details,et_remarks,search,etQuantity,acc_etQuantity;
    SharedPreferences.Editor editor;
    String version,device_type_id, username,mainClientId="Select Client",transit_id="",type_id,type_name="",tech_id,others="",items_value="",other_key="",courier_id="",transit_through="",remarks="",item_qty="",other_tech_id="";
    ReceiveDeviceController receiveDeviceController;
    ArrayList<ItemList> itemList = new ArrayList<>();
    ArrayList<TransitList> transitList = new ArrayList<>();
    ArrayList<String> transitDetails = new ArrayList<>();
    ArrayList<String> courierDetails = new ArrayList<>();
    ArrayList<String> itemDetails = new ArrayList<>();
    ArrayList<DeviceList> list_change_values = new ArrayList<>();
    ArrayList<String> value_name = new ArrayList<>();
    ArrayList<String> vehicletype = new ArrayList<>();
    ArrayList<String> clientDeviceType = new ArrayList<>();
    ArrayList<String> clientDeviceDetails = new ArrayList<>();
    ArrayList<String> items_list = new ArrayList<>();
    String searchingText;
    Dialog myDialog;
    EditText etMasterPass;
    ImageView txtclose;
    StringBuilder sb,sbFieldIds;
    private Dialog confirmDialog;
    int count=0;
    MaterialReturnFragment materialReturnFragment;
    NewInstallmentController newInstallmentController;
    ArrayList<MainClientList> mainclientList = new ArrayList<>();
    ArrayList<DeviceTypeOtherAis> deviceTypeOtherAis_arr = new ArrayList<>();
    ArrayList<String> arr_device_types = new ArrayList<>();
    ArrayList<String> mainClientDetail = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.additional_material_requirement, container, false);
        sharedprefs = getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        tech_id = sharedprefs.getString("s_user_id", "");
        initView();
        addclients();
        addDevice();
        getItemList();

        return v;
    }

    private void getItemList() {
        receiveDeviceController.deviceList(this);
    }

    private void addDevice() {
        try {
            pDialog = K.createProgressDialog(getActivity());
            pDialog.setMessage("Getting Data...");
            pDialog.show();
            pDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                            device_type.setAdapter(adapter);
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
            }
        });
    }

    public void initView(){
        newInstallmentController = new NewInstallmentController();
        receiveDeviceController = new ReceiveDeviceController();
        new_main_clients= v.findViewById(R.id.new_main_clients);
        device_type= v.findViewById(R.id.device_type);
        accessory_spinner= v.findViewById(R.id.accessory_spinner);
        parentLinearLayout = v.findViewById(R.id.parent_linear_layout);
        accessory_linear_layout = v.findViewById(R.id.accessory_linear_layout);
        addMaterial = v.findViewById(R.id.addMaterial);
        addAccessory = v.findViewById(R.id.addAccessory);
        etQuantity = v.findViewById(R.id.etQuantity);
        acc_etQuantity = v.findViewById(R.id.acc_etQuantity);
        delete_button = v.findViewById(R.id.delete_button);
        acc_delete_button = v.findViewById(R.id.acc_delete_button);
        et_remarks = v.findViewById(R.id.et_remarks);
        final_submit = v.findViewById(R.id.submit);
        confirmDialog = new Dialog(getActivity());

        accessory_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                type_id = itemList.get(i).getId();
                type_name  = itemList.get(i).getName();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        new_main_clients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                mainClientId = String.valueOf(mainclientList.get(i).getClient_Id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        device_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                device_type_id = deviceTypeOtherAis_arr.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentLinearLayout.removeView((View) v.getParent());
            }
        });

        acc_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accessory_linear_layout.removeView((View) view.getParent());
            }
        });

        addMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.fields_additional_vts, null);
                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
                new_main_clients = rowView.findViewById(R.id.new_main_clients);
                device_type = rowView.findViewById(R.id.device_type);
                delete_button = rowView.findViewById(R.id.delete_button);
                etQuantity = rowView.findViewById(R.id.etQuantity);

                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, mainClientDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                new_main_clients.setAdapter(adapter);

                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, arr_device_types);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                device_type.setAdapter(adapter);

                new_main_clients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                        if (i == 0) {
                            return;
                        } else {
                            i = i - 1;
                        }
                        mainClientId = String.valueOf(mainclientList.get(i).getClient_Id());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                device_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                        if (i == 0) {
                            return;
                        } else {
                            i = i - 1;
                        }
                        device_type_id = deviceTypeOtherAis_arr.get(i).getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        parentLinearLayout.removeView((View) view.getParent());
                    }
                });
            }
        });

        addAccessory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.field_additional_accessory, null);
                accessory_linear_layout.addView(rowView, accessory_linear_layout.getChildCount() - 1);
                accessory_spinner = rowView.findViewById(R.id.accessory_spinner);
                acc_delete_button = rowView.findViewById(R.id.acc_delete_button);
                acc_etQuantity = rowView.findViewById(R.id.acc_etQuantity);

                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, itemDetails);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                accessory_spinner.setAdapter(adapter);

                accessory_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if (i == 0) {
                            return;
                        } else {
                            i = i - 1;
                        }
                        type_id = itemList.get(i).getId();
                        type_name  = itemList.get(i).getName();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                acc_delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        accessory_linear_layout.removeView((View) view.getParent());
                    }
                });
            }
        });

        final_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                remarks = et_remarks.getText().toString();
//                if(transit_id.equalsIgnoreCase("")){
//                    Toast.makeText(getActivity(), "Select Transit Type", Toast.LENGTH_SHORT).show();
//                }else if(transit_id.equalsIgnoreCase("1")&&(courier_id.equalsIgnoreCase(""))){
//                    Toast.makeText(getActivity(), "Select Courier Name ", Toast.LENGTH_SHORT).show();
//                }else if(transit_id.equalsIgnoreCase("1")&&(details.getText().toString().equalsIgnoreCase(""))){
//                    Toast.makeText(getActivity(), "Enter Docket Number", Toast.LENGTH_SHORT).show();
//                }else if(details.getText().toString().equalsIgnoreCase("")){
//                    Toast.makeText(getActivity(), "Enter bus no/Employee id/Consignment No", Toast.LENGTH_SHORT).show();
//                } else {
                    for (int j = 0; j < parentLinearLayout.getChildCount() - 1; j++) {
                        View view2 = parentLinearLayout.getChildAt(j);

                        Spinner clientSpinner = view2.findViewById(R.id.new_main_clients);
                        Spinner device_spinner = view2.findViewById(R.id.device_type);
                        EditText etQuantity = view2.findViewById(R.id.etQuantity);

                        for (MainClientList clientEntry : mainclientList) {
                            if(clientEntry.getClient_Name().equalsIgnoreCase(clientSpinner.getSelectedItem().toString())) {
                                    for(DeviceTypeOtherAis entrys : deviceTypeOtherAis_arr){
                                        if(entrys.getName().equalsIgnoreCase(device_spinner.getSelectedItem().toString())){
                                                clientDeviceType.add(clientEntry.getClient_Id()+ ":" +entrys.getId()+ ":" +etQuantity.getText().toString());
                                                clientDeviceDetails.add(clientEntry.getClient_Name()+ ":" +entrys.getName()+ ":" +etQuantity.getText().toString());
                                        }
                                }
                            }
                        }
                    }

                for (int i = 0; i < accessory_linear_layout.getChildCount() - 1; i++) {
                    View view1 = accessory_linear_layout.getChildAt(i);

                    Spinner spinner = view1.findViewById(R.id.accessory_spinner);
                    EditText ed_value = view1.findViewById(R.id.acc_etQuantity);

                    for (ItemList entry : itemList) {
                        if(entry.getName().equalsIgnoreCase(spinner.getSelectedItem().toString())) {
                                vehicletype.add(entry.getId()+ ":" + ed_value.getText().toString());
                                items_list.add(entry.getName()+ ":" + ed_value.getText().toString());
                        }
                    }
                }
                submitData();
            }
        });

    }

    private void submitData() {

        confirmationDialog();
    }

    private void confirmationDialog() {

        confirmDialog.setContentView(R.layout.confirmation_dialog);
        final_submit = confirmDialog.findViewById(R.id.final_submit);
        final_cancel = confirmDialog.findViewById(R.id.final_cancel);
        response_comment = confirmDialog.findViewById(R.id.comment);
        items_values = confirmDialog.findViewById(R.id.items_values);
        preview_tags = confirmDialog.findViewById(R.id.preview_tags);
        items_tags = confirmDialog.findViewById(R.id.items_tags);
        preview_values = confirmDialog.findViewById(R.id.preview_values);

        items_values.setText(items_list.toString());
        preview_values.setText(clientDeviceDetails.toString());
        response_comment.setText(remarks);
        final_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalSubmitData();
                confirmDialog.hide();
            }
        });

        final_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicletype.clear();
                clientDeviceType.clear();
                clientDeviceDetails.clear();
                items_list.clear();
                confirmDialog.hide();
            }
        });

        confirmDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        confirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmDialog.setCanceledOnTouchOutside(false);
        confirmDialog.setCancelable(false);
        Window window = confirmDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        confirmDialog.show();
    }

    private void finalSubmitData() {
        try {
            pDialog = K.createProgressDialog(getActivity());
            pDialog.setMessage("Submitting Data...");
            pDialog.show();
            pDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<MainResponse> call = log_att.submit_additional_devices(username, clientDeviceType.toString(),vehicletype.toString(),remarks);
        Log.i("****call", String.valueOf(call));
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                MainResponse updateDataResponse = response.body();
                Log.i("**respnse", " " + response.body());
                Log.i("**respnse", " " + response.body().getType());

                if (updateDataResponse.getType() == 1) {
                    Toast.makeText(getActivity(), ""+updateDataResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    pDialog.hide();
                }else {
                    Toast.makeText(getActivity(), ""+updateDataResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    pDialog.hide();
                }
            }
            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addclients() {
        newInstallmentController.reqeuestMainClientList(this);
    }
    @Override
    public void receiveDeviceResponse(MainResponse response) {

    }

    @Override
    public void receiveDispatchMaterial(MainResponse response) {
        if(response.getType()==1) {
            pDialog.dismiss();
            try {
                itemList = response.getItems_list();
                try {
                    try {
                        itemDetails.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    itemDetails.add(" SELECT ITEMS");
                    for (int i = 0; i < itemList.size(); i++) {
                        itemDetails.add(itemList.get(i).getName());
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, itemDetails);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    accessory_spinner.setAdapter(adapter);
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(getActivity(), ""+response.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void returnDeviceresponse(MainResponse response) {

    }

    @Override
    public void dispatchFromTechResponse(MainResponse response) {

    }

    @Override
    public void clientResponse(ClientResponse response) {

    }

    @Override
    public void locationResponse(ClientLocationResponse response) {

    }

    @Override
    public void workTypeResponse(WorkTypeResponse response) {

    }

    @Override
    public void vehicleTypeResponse(VehicleTypeResponse response) {

    }

    @Override
    public void faultListResponse(FaultResponse response) {

    }

    @Override
    public void replaceResponse(ReplaceReason response) {

    }

    @Override
    public void disconnectionResponse(DisconnectionResponse response) {

    }

    @Override
    public void removalActivityResponse(RemovalActivityResponse response) {

    }

    @Override
    public void removalResponse(RemovalResponse response) {

    }

    @Override
    public void damageResponse(RemovalResponse response) {

    }

    @Override
    public void collectItemResponse(CollectedItemsResponse response) {

    }

    @Override
    public void simOperatorResponse(SimOperatorResponse response) {

    }

    @Override
    public void simReplaceReason(SimReplaceResponse response) {

    }

    @Override
    public void notAvailActivity(NotAvailActivityResponse response) {

    }

    @Override
    public void vehicleNotAvailReason(VehNotAvailReasonResponse response) {

    }

    @Override
    public void vtsResponses(VTSResponse response) {

    }

    @Override
    public void vtsResponse(VTSResponse response) {

    }

    @Override
    public void pMethod(PaymentMethodResponse response) {

    }

    @Override
    public void updateDataResponse(MainResponse response) {

    }

    @Override
    public void mainClientResponse(MainResponse response) {
        try {
            mainclientList = response.getMain_client_list();
            try {
                try {
                    mainClientDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mainClientDetail.add("SELECT CLIENT");
                for (int i = 0; i < mainclientList.size(); i++) {
                    mainClientDetail.add(mainclientList.get(i).getClient_Name());
                }
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, mainClientDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                new_main_clients.setAdapter(adapter);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void vtsAccResponses(MainResponse response) {

    }
}
