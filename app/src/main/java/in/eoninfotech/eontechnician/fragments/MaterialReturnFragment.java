package in.eoninfotech.eontechnician;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.robinhood.ticker.TickerView;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

import in.eoninfotech.eontechnician.responses.DeviceList;
import in.eoninfotech.eontechnician.responses.ItemList;
import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.responses.TransitList;
import in.eoninfotech.eontechnician.callbacks.ReceiveDeviceListener;
import in.eoninfotech.eontechnician.controllers.ReceiveDeviceController;
import in.eoninfotech.eontechnician.helper.K;

import static android.content.Context.MODE_PRIVATE;

public class MaterialReturnFragment extends Fragment implements ReceiveDeviceListener, TextWatcher {

    View v,rowView;
    Spinner type_spinner,transit_spinner,courier_spinner;
    ArrayAdapter<String> adapter;
    private ProgressDialog pDialog;
    NonScrollListView lv;
    Button delete_button,update_data,final_submit, final_cancel;
    SharedPreferences sharedprefs;
    LinearLayout parentLinearLayout, transit_linear, materialLL,linearField;
    Button btCancel,btSubmit,add_manual;
    TextView addMaterial,response_comment,items_values,preview_tags,items_tags,addMaterialText;
    TickerView tickerView;
    EditText details,et_remarks,search,etQuantity;
    SharedPreferences.Editor editor;
    String version, username,transit_id="",type_id,type_name="",tech_id,others="",items_value="",other_key="",courier_id="",transit_through="",remarks="",item_qty="",other_tech_id="";
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
    ArrayList<String> items_list = new ArrayList<>();
    String searchingText;
    Dialog myDialog;
    EditText etMasterPass;
    ImageView txtclose;
    StringBuilder sb,sbFieldIds;
    private Dialog confirmDialog;
    int count=0;
    MaterialReturnFragment materialReturnFragment;

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

        getDeviceList();
        getItemsList();
        getTransitList();

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
        materialReturnFragment = new MaterialReturnFragment();
        parentLinearLayout = v.findViewById(R.id.parent_linear_layout);
        materialLL = v.findViewById(R.id.materialLL);
        addMaterial = v.findViewById(R.id.addMaterial);
        linearField = v.findViewById(R.id.linearField);
        delete_button = v.findViewById(R.id.delete_button);
        update_data = v.findViewById(R.id.update_data);
        type_spinner = v.findViewById(R.id.type_spinner);
        search = v.findViewById(R.id.search);
        transit_spinner = v.findViewById(R.id.transit_spinner);
        transit_linear = v.findViewById(R.id.transit_linear);
        courier_spinner = v.findViewById(R.id.courier_spinner);
        details = v.findViewById(R.id.details);
        et_remarks = v.findViewById(R.id.et_remarks);
        etQuantity = v.findViewById(R.id.etQuantity);
        lv = v.findViewById(R.id.return_device_list);
        tickerView = v.findViewById(R.id.device_left);
        myDialog = new Dialog(getActivity());
        receiveDeviceController = new ReceiveDeviceController();
        confirmDialog = new Dialog(getActivity());

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

        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                type_id = itemList.get(i).getId();
                type_name  = itemList.get(i).getName();
                //add_accessory_dialog(type_name);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        addMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.field, null);
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
                        type_name  = itemList.get(i).getName();
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

//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                SparseBooleanArray checked = lv.getCheckedItemPositions();
//                others = list_change_values.get(position).getSr_no();
//                myDialog.setContentView(R.layout.add_serial_no);
//                txtclose = myDialog.findViewById(R.id.error);
//                btCancel = myDialog.findViewById(R.id.btCancel);
//                btSubmit = myDialog.findViewById(R.id.btSubmit);
//                etMasterPass = myDialog.findViewById(R.id.etMasterPass);
//                txtclose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        myDialog.dismiss();
//                    }
//                });
//                btCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        myDialog.dismiss();
//                    }
//                });
//
//                btSubmit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        myDialog.dismiss();
//                    }
//                });
//                myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                myDialog.show();
//            }
//        });

        update_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                transit_through = details.getText().toString();
                remarks = et_remarks.getText().toString();

                if(transit_id.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Select Transit Type", Toast.LENGTH_SHORT).show();
                }else if(transit_id.equalsIgnoreCase("1")&&(courier_id.equalsIgnoreCase(""))){
                        Toast.makeText(getActivity(), "Select Courier Name ", Toast.LENGTH_SHORT).show();
                }else if(transit_id.equalsIgnoreCase("1")&&(details.getText().toString().equalsIgnoreCase(""))){
                        Toast.makeText(getActivity(), "Enter Docket Number", Toast.LENGTH_SHORT).show();
                }else if(details.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Enter bus no/Employee id/Consignment No", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < parentLinearLayout.getChildCount() - 1; i++) {
                        View view1 = parentLinearLayout.getChildAt(i);

                        Spinner spinner = view1.findViewById(R.id.type_spinner);
                        EditText ed_value = view1.findViewById(R.id.etQuantity);

                            for (ItemList entry : itemList) {
                                if(entry.getName().equalsIgnoreCase(spinner.getSelectedItem().toString())) {
//                                    if(!(entry.getName().equalsIgnoreCase("SELECT ITEMS")&&(ed_value.getText().toString().equalsIgnoreCase("")))){
//                                        Toast.makeText(getActivity(), "Select Quantity", Toast.LENGTH_SHORT).show();
//                                    }else {
                                        vehicletype.add(entry.getId()+ ":" + ed_value.getText().toString());
                                        items_list.add(entry.getName()+ ":" + ed_value.getText().toString());
                                   // }
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
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            lv.smoothScrollToPosition(0);
            adapter.getFilter().filter(search.getText().toString());
            if(adapter.getCount()==0){
                String errorString = "No match found";
                Toast.makeText(getActivity(), errorString, Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

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
                        int val1 = 1;
                        String k = Integer.toString(i + val1);
                        value_name.add(k +". "+ list_change_values.get(i).getPcb_sr_no());
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
            getDeviceList();
            getItemsList();
            getTransitList();
        }else{
            confirmDialog.hide();
            pDialog.dismiss();
            Toast.makeText(getActivity(), ""+response.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    public void add_accessory_dialog(String type_name){

        myDialog.setContentView(R.layout.add_accessory_dialog);
        txtclose = myDialog.findViewById(R.id.error);
        btCancel = myDialog.findViewById(R.id.btCancel);
        btSubmit = myDialog.findViewById(R.id.btSubmit);
        etMasterPass = myDialog.findViewById(R.id.etMasterPass);

        etMasterPass.setText(type_name);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
