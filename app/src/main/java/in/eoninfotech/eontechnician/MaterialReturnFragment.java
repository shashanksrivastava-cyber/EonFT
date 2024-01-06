package in.eoninfotech.eontechnician;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.robinhood.ticker.TickerView;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import in.eoninfotech.eontechnician.Responses.ClientDetails;
import in.eoninfotech.eontechnician.Responses.ClientLocationResponse;
import in.eoninfotech.eontechnician.Responses.ClientResponse;
import in.eoninfotech.eontechnician.Responses.CollectedItemsResponse;
import in.eoninfotech.eontechnician.Responses.DeviceList;
import in.eoninfotech.eontechnician.Responses.DisconnectionResponse;
import in.eoninfotech.eontechnician.Responses.FaultList;
import in.eoninfotech.eontechnician.Responses.FaultResponse;
import in.eoninfotech.eontechnician.Responses.ItemList;
import in.eoninfotech.eontechnician.Responses.MainResponse;
import in.eoninfotech.eontechnician.Responses.NotAvailActivityResponse;
import in.eoninfotech.eontechnician.Responses.PaymentMethodResponse;
import in.eoninfotech.eontechnician.Responses.RemovalActivityResponse;
import in.eoninfotech.eontechnician.Responses.RemovalResponse;
import in.eoninfotech.eontechnician.Responses.ReplaceReason;
import in.eoninfotech.eontechnician.Responses.SimOperatorResponse;
import in.eoninfotech.eontechnician.Responses.SimReplaceResponse;
import in.eoninfotech.eontechnician.Responses.TransitList;
import in.eoninfotech.eontechnician.Responses.VTSResponse;
import in.eoninfotech.eontechnician.Responses.VehNotAvailReasonResponse;
import in.eoninfotech.eontechnician.Responses.VehicleTypeResponse;
import in.eoninfotech.eontechnician.Responses.WorkTypeResponse;
import in.eoninfotech.eontechnician.Service.ForegroundService;
import in.eoninfotech.eontechnician.activity.LoginActivityNew;
import in.eoninfotech.eontechnician.callbacks.ClientListener;
import in.eoninfotech.eontechnician.callbacks.ReceiveDeviceListener;
import in.eoninfotech.eontechnician.controllers.NewInstallmentController;
import in.eoninfotech.eontechnician.controllers.ReceiveDeviceController;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;

import static android.content.Context.MODE_PRIVATE;

public class MaterialReturnFragment extends Fragment implements ReceiveDeviceListener {

    View v;
    Spinner type_spinner,transit_spinner,courier_spinner;
    ArrayAdapter<String> adapter;
    private ProgressDialog pDialog;
    NonScrollListView lv;
    Button delete_button,update_data;
    SharedPreferences sharedprefs;
    LinearLayout parentLinearLayout, transit_linear;
    TextView addMaterial;
    TickerView tickerView;
    EditText details,et_remarks;
    SharedPreferences.Editor editor;
    String version, username,transit_id="",type_id,tech_id,others="",courier_id="",transit_through="",remarks="",item_qty="",other_tech_id="";
    ReceiveDeviceController receiveDeviceController;
    ArrayList<ItemList> itemList = new ArrayList<>();
    ArrayList<TransitList> transitList = new ArrayList<>();
    ArrayList<ItemList> detailList = new ArrayList<>();
    ArrayList<String> transitDetails = new ArrayList<>();
    ArrayList<String> courierDetails = new ArrayList<>();
    ArrayList<String> itemDetails = new ArrayList<>();
    ArrayList<DeviceList> list_change_values = new ArrayList<>();
    ArrayList<String> value_name = new ArrayList<>();
    ArrayList<String> vehicletype = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.activity_return_device, container, false);
        sharedprefs = getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        tech_id = sharedprefs.getString("s_user_id", "");
        initView();
        return v;
    }

    private void getDeviceList() {
        try {
            pDialog = K.createProgressDialog(getActivity());
            pDialog.setMessage("Getting Data...");
            pDialog.show();
            pDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
     receiveDeviceController.returnDeviceList(tech_id,this);
    }

    private void getTransitList() {
        receiveDeviceController.transitList(this);
    }

    private void getItemsList() {
      receiveDeviceController.deviceList(this);
    }

    private void initView() {
        parentLinearLayout = v.findViewById(R.id.parent_linear_layout);
        addMaterial = v.findViewById(R.id.addMaterial);
        delete_button = v.findViewById(R.id.delete_button);
        update_data = v.findViewById(R.id.update_data);
        type_spinner = v.findViewById(R.id.type_spinner);
        transit_spinner = v.findViewById(R.id.transit_spinner);
        transit_linear = v.findViewById(R.id.transit_linear);
        courier_spinner = v.findViewById(R.id.courier_spinner);
        details = v.findViewById(R.id.details);
        et_remarks = v.findViewById(R.id.et_remarks);
        lv = v.findViewById(R.id.return_device_list);
        tickerView = v.findViewById(R.id.device_left);
        receiveDeviceController = new ReceiveDeviceController();

        transit_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                transit_id = transitList.get(i).getTransit_id();

                if(transit_id.equalsIgnoreCase("1")){
                    transit_linear.setVisibility(View.VISIBLE);
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, courierDetails);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    courier_spinner.setAdapter(adapter);
                    details.setHint("Enter Docket No.");
                }else if(transit_id.equalsIgnoreCase("2")) {
                    transit_linear.setVisibility(View.GONE);
                    details.setHint("Enter Bus No.");
                }else {
                    transit_linear.setVisibility(View.GONE);
                    details.setHint("Enter Employee ID");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        courier_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                courier_id = detailList.get(i).getId();
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

        addMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.field, null);
                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
                delete_button = rowView.findViewById(R.id.delete_button);
                type_spinner = rowView.findViewById(R.id.type_spinner);

                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, itemDetails);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                type_spinner.setAdapter(adapter);

                type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if (i == 0) {
                            return;
                        } else {
                            i = i - 1;
                        }
                        type_id = itemList.get(i).getId();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
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

        update_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                transit_through = details.getText().toString();
                remarks = et_remarks.getText().toString();

                for (int i = 0; i < parentLinearLayout.getChildCount() - 1; i++) {
                    View view1 = parentLinearLayout.getChildAt(i);

                    Spinner spinner = view1.findViewById(R.id.type_spinner);
                    EditText ed_value = view1.findViewById(R.id.etQuantity);

                    vehicletype.add(spinner.getSelectedItemId() + ":" + ed_value.getText().toString());
                    StringBuffer sb = new StringBuffer();
                    for (int k = 0; k < vehicletype.size(); k++) {
                        sb.append(vehicletype.get(i));
                    }
                    item_qty = sb.toString();
                }

                    SparseBooleanArray checked = lv.getCheckedItemPositions();
                    others = "";
                    for (int j = 0; j < checked.size(); j++) {
                        int key = checked.keyAt(j);
                        others = others + (list_change_values.get(key).getId()) + ":";
                    }
                    submitData();
            }
        });
    }

    private void submitData() {

        confirmationDialog();
    }

    private void confirmationDialog() {
        new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm Submission")
                .setMessage("Are you sure you want to submit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finalSubmitData();
                    }
                })
                .setNegativeButton("No", null)
                .show();
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
        receiveDeviceController.return_material(tech_id,others,vehicletype.toString(),transit_id,courier_id,transit_through,remarks,other_tech_id,this);
    }

    @Override
    public void receiveDeviceResponse(MainResponse response) {
        if(response.getType()==1) {
            pDialog.dismiss();
            try {
                transitList = response.getTransit_list();
                detailList = response.getTransit_list().get(0).getDetail_array();
                try {
                    try {
                        transitDetails.clear();
                        courierDetails.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    transitDetails.add(" SELECT ITEMS");
                    for (int i = 0; i < transitList.size(); i++) {
                        transitDetails.add(transitList.get(i).getTransit_name());
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, transitDetails);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    transit_spinner.setAdapter(adapter);

                    courierDetails.add(" SELECT ITEMS");
                    for (int i = 0; i < detailList.size(); i++) {
                        courierDetails.add(detailList.get(i).getName());
                    }
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
                    type_spinner.setAdapter(adapter);
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
        if(response.getType()==1){
            pDialog.dismiss();
        try {
            list_change_values = response.getDevice_list();
            try {
                try {
                    value_name.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (list_change_values.size() > 0) {
                    for (int i = 0; i < list_change_values.size(); i++) {
                        value_name.add(list_change_values.get(i).getPcb_sr_no());
                    }
                    if (list_change_values.size() > 5) {
                        lv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * list_change_values.size() + 1));
                    } else {
                        lv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * list_change_values.size()));
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_list_item, value_name);
                    lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    lv.setAdapter(adapter);
                } else {
                }
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
    public void dispatchFromTechResponse(MainResponse response) {
        if(response.getType()==1) {
            pDialog.dismiss();
          tickerView.setText(response.getTotal_received_count());
        }else{
            pDialog.dismiss();
            Toast.makeText(getActivity(), ""+response.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {

        getDeviceList();
        getItemsList();
        getTransitList();

        super.onResume();
    }
}
