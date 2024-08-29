package in.eoninfotech.eontechnician;

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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;
import com.robinhood.ticker.TickerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import androidx.fragment.app.Fragment;

import in.eoninfotech.eontechnician.responses.DeviceList;
import in.eoninfotech.eontechnician.responses.ItemList;
import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.responses.TechDetails;
import in.eoninfotech.eontechnician.responses.TechResponse;
import in.eoninfotech.eontechnician.responses.TransitList;
import in.eoninfotech.eontechnician.callbacks.ReceiveDeviceListener;
import in.eoninfotech.eontechnician.controllers.ReceiveDeviceController;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MaterialtoTechFragment extends Fragment implements ReceiveDeviceListener {

    View v;
    NonScrollListView lv;
    private ProgressDialog pDialog;
    Button update_data,final_submit, final_cancel,addMaterial;
    EditText details,et_remarks,etQuantity;
    TickerView tickerView;

    ImageButton delete_button;
    Spinner type_spinner,transit_spinner,courier_spinner;
    ArrayList<TechDetails> techList = new ArrayList<>();
    ArrayList<String> techDetail = new ArrayList<>();
    ArrayList<TransitList> transitList = new ArrayList<>();
    MySearchableSpinner spn_technicians;
    LinearLayout parentLinearLayout, transit_linear,materialLL;
    ArrayList<ItemList> itemList = new ArrayList<>();
    TextView response_comment,items_values,preview_tags,items_tags;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String version, username,transit_id="",type_id,tech_id,others="",courier_id="",transit_through="",remarks="",other_key="",item_qty="",other_tech_id="";
    ReceiveDeviceController receiveDeviceController;
    ArrayAdapter<String> adapter;
    ArrayList<ItemList> detailList = new ArrayList<>();
    ArrayList<String> transitDetails = new ArrayList<>();
    ArrayList<String> courierDetails = new ArrayList<>();
    ArrayList<String> itemDetails = new ArrayList<>();
    ArrayList<DeviceList> list_change_values = new ArrayList<>();
    ArrayList<String> value_name = new ArrayList<>();
    ArrayList<String> vehicletype = new ArrayList<>();
    int count=0;
    Dialog myDialog;
    EditText etMasterPass;
    ImageView txtclose;
    StringBuilder sb,sbFieldIds;
    private Dialog confirmDialog;
    ArrayList<String> items_list = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_material_to_tech, container, false);
        sharedprefs = getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        tech_id = sharedprefs.getString("s_user_id", "");
        initView();

        addTechnicians();
        getDeviceList();
        getItemsList();
        getTransitList();

        return v;
    }

    private void getDeviceList() {
        receiveDeviceController.returnDeviceList(tech_id,this);
    }

    private void getTransitList() {
        receiveDeviceController.transitList(this);
    }

    private void getItemsList() {
        receiveDeviceController.deviceList(this);
    }


    private void addTechnicians() {
        ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<TechResponse> clientCall = client_att.requestTechList();
        clientCall.enqueue(new Callback<TechResponse>() {
            public void onResponse(Call<TechResponse> call, Response<TechResponse> response) {
                try {
                    TechResponse workTypeResponse = response.body();
                    techList = response.body().gettechList();
                    Log.i("**workclientrespnse", " " + techList);

                    try {
                        techDetail.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < techList.size(); i++) {
                        techDetail.add(techList.get(i).getName());
                    }
                    List<String> name = new ArrayList<String>();
                    Collection c = techDetail;
                    Iterator itr = c.iterator();
                    name.add(" Select Technician");
                    while (itr.hasNext()) {
                        String temp = itr.next().toString();
                        Log.i("TEMP", "" + temp);
                        name.add(temp);
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, name);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_technicians.setAdapter(adapter);

                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TechResponse> call, Throwable t) {
                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }

    private void initView() {

        parentLinearLayout = v.findViewById(R.id.parent_linear_layout);
        materialLL = v.findViewById(R.id.materialLL);
        addMaterial = v.findViewById(R.id.addMaterial);
        delete_button = v.findViewById(R.id.delete_button);
        update_data = v.findViewById(R.id.update_data);
        type_spinner = v.findViewById(R.id.type_spinner);
        transit_spinner = v.findViewById(R.id.transit_spinner);
        transit_linear = v.findViewById(R.id.transit_linear);
        courier_spinner = v.findViewById(R.id.courier_spinner);
        etQuantity = v.findViewById(R.id.etQuantity);
        details = v.findViewById(R.id.details);
        et_remarks = v.findViewById(R.id.et_remarks);
        lv = v.findViewById(R.id.return_device_list);
        tickerView = v.findViewById(R.id.device_left);
        spn_technicians = v.findViewById(R.id.spn_technicians);
        receiveDeviceController = new ReceiveDeviceController();
        confirmDialog = new Dialog(getActivity());

        spn_technicians.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                other_tech_id = techList.get(i).getId_no();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, courierDetails);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    courier_spinner.setAdapter(adapter);
                    courier_id="";
                }else {
                    transit_linear.setVisibility(View.GONE);
                    details.setHint("Enter Employee ID");
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, courierDetails);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    courier_spinner.setAdapter(adapter);
                    courier_id="";
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
                if (other_tech_id.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Select Technician Id", Toast.LENGTH_SHORT).show();
                } else if (transit_id.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Select Transit Type", Toast.LENGTH_SHORT).show();
                } else if (transit_id.equalsIgnoreCase("1") && (courier_id.equalsIgnoreCase(""))) {
                    Toast.makeText(getActivity(), "Select Courier Name ", Toast.LENGTH_SHORT).show();
                } else if (transit_id.equalsIgnoreCase("1") && (details.getText().toString().equalsIgnoreCase(""))) {
                    Toast.makeText(getActivity(), "Enter Docket Number", Toast.LENGTH_SHORT).show();
                } else if (details.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Enter bus no/Employee id/Consignment No", Toast.LENGTH_SHORT).show();
                } else {
                    transit_through = details.getText().toString();
                    remarks = et_remarks.getText().toString();

                    for (int i = 0; i < parentLinearLayout.getChildCount() - 1; i++) {
                        View view1 = parentLinearLayout.getChildAt(i);

                        Spinner spinner = view1.findViewById(R.id.type_spinner);
                        EditText ed_value = view1.findViewById(R.id.etQuantity);

                        for (ItemList entry : itemList) {
                            if(entry.getName().equalsIgnoreCase(spinner.getSelectedItem().toString()))
                            {
                                vehicletype.add(entry.getId()+ ":" + ed_value.getText().toString());
                                items_list.add(entry.getName()+ ":" + ed_value.getText().toString());
                            }
                        }
                    }
                    SparseBooleanArray checked = lv.getCheckedItemPositions();
                    int abc = lv.getCheckedItemCount();
                    count=0;
                    others = "";
                    other_key="";
                    boolean containsBoolean = false;
                    for (int j = 0; j < checked.size(); j++) {
                        int key = checked.keyAt(j);
                        if(checked.valueAt(j)==true){
                            count++;
                            others = others + (list_change_values.get(key).getId()) + ":";
                            other_key = other_key + (list_change_values.get(key).getPcb_sr_no()) + "\n";
                        }else {

                        }
                    }
                    submitData();
                }
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
        TextView preview_values = confirmDialog.findViewById(R.id.preview_values);

        String errorList=other_key.toString();
        errorList=errorList.replaceAll(",", "\n");
        preview_values.setText((errorList));

        preview_tags.setText("Total Device Count : " + count);

        items_tags.setText("Total Items Count : "+ items_list.size());
        items_values.setText(items_list.toString());
        response_comment.setText(remarks);
        final_submit.setOnClickListener(view -> finalSubmitData());

        final_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items_list.clear();
                vehicletype.clear();
                count=0;
                confirmDialog.hide();
            }
        });

        confirmDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        confirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmDialog.setCanceledOnTouchOutside(false);
        Window window = confirmDialog.getWindow();
        confirmDialog.setCancelable(false);
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
        receiveDeviceController.return_material(tech_id,others,vehicletype.toString(),transit_id,courier_id,transit_through,remarks,other_tech_id,this);
    }

    @Override
    public void receiveDeviceResponse(MainResponse response) {
        if(response.getType()==1) {
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
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, courierDetails);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    courier_spinner.setAdapter(adapter);
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
            Toast.makeText(getActivity(), ""+response.getMsg(), Toast.LENGTH_SHORT).show();
            details.setText("");
            et_remarks.setText("");
            confirmDialog.hide();
            etQuantity.setText("");
            parentLinearLayout.removeViews(1, parentLinearLayout.getChildCount()-1);
            parentLinearLayout.addView(addMaterial, parentLinearLayout.getChildCount());
            for (int i = 0; i < parentLinearLayout.getChildCount() - 1; i++) {
                View view1 = parentLinearLayout.getChildAt(i);

                Spinner spinner = view1.findViewById(R.id.type_spinner);
                spinner.setSelection(0);
            }

            items_list.clear();
            vehicletype.clear();
            count=0;
            addTechnicians();
            getDeviceList();
            getItemsList();
            getTransitList();
        }else{
            pDialog.dismiss();
            Toast.makeText(getActivity(), ""+response.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {

        super.onResume();
    }
}
