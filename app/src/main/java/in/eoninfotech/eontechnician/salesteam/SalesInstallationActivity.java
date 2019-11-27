package in.eoninfotech.eontechnician.salesteam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.activity.LoginActivity;
import in.eoninfotech.eontechnician.helper.SalesCustDetail;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesInstallationActivity extends AppCompatActivity {

    EditText et_duration, et_cust_street_name, et_cust_city, et_cust_office_number, et_cust_district,
            et_cust_state, et_cust_pincode, et_cust_p_name, et_cust_p_number, et_cust_p_id, et_cust_s_name,
            et_cust_s_number, et_cust_s_id, et_install_street_name, et_install_city, et_install_office_number,
            et_install_district, et_install_state, et_install_pincode, et_vts_quantity,
            et_vehicle_type_other, et_voltage_other, et_priority_rsn, et_order_no, et_client_nm,
            et_drs_quantity, et_fuel_sensor_quantity, et_door_sensor_quantity, et_remarks,
            et_high_count, et_normal_count, et_low_count;
    RadioGroup rg_instal_type, rg_client_type, rg_battry_voltage, rg_accessory, rg_ignition;
    CheckBox rb_truck, rb_bus, rb_tm, rb_tipper, rb_lcv, rb_car, rb_others;
    AppCompatCheckBox copy_address;
    Button preview_btn;
    ProgressDialog pDialog;
    ScrollView sv;
    LinearLayout lay_acc_quan, lay_duration;
    RelativeLayout relay;
    String sales_person_name_s,
            order_no_s = "0",
            install_type_s,
            duration_s,
            cust_type_s,
            client_name_s = "0",
            cust_street_name_s,
            cust_city_s,
            cust_office_number_s,
            cust_district_s,
            cust_state_s,
            cust_pincode_s = "0",
            cust_p_name_s,
            cust_p_number_s,
            cust_p_id_s,
            cust_s_name_s,
            cust_s_number_s,
            cust_s_id_s,
            install_street_name_s,
            install_city_s,
            install_office_number_s,
            install_district_s,
            install_state_s,
            install_pincode_s = "0",
            vehicle_type_s,
            battery_voltage_s = "12 Volt",
            vts_quantity_s = "0",
            accessory_s = "No",
            drs_quantity_s = "0",
            fuel_sensor_quantity_s = "0",
            door_sensor_quantity_s = "0",
            priority_reason = "0",
            remarks_s, version,
            ignition_s = "No", high_count_s, normal_count_s, low_count_s;

    ;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String usrname, current_date, s_time, uusername, disgnid = "0";
    int year, month, day;
    Calendar calen = Calendar.getInstance();
    MySearchableSpinner client;
    ArrayList<SalesCustDetail> arr_sales_cust_detail = new ArrayList<>();
    ArrayList<String> cust_names = new ArrayList<>();
    ArrayAdapter<String> adp;
    ArrayList<String> preview_value = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_installation);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("New Requirement");
        try {
            //if( Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.parseColor("#453d87"));
            }
            //}
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        uusername = sharedprefs.getString("s_uuser", "");
        Log.i("******shared******", uusername);
        usrname = sharedprefs.getString("dis_user", "");
        disgnid = sharedprefs.getString("s_distt", "");
        version = sharedprefs.getString("version", "");
        sv = (ScrollView) findViewById(R.id.scrl_view);
        et_duration = (EditText) findViewById(R.id.sales_duration);
        et_cust_street_name = (EditText) findViewById(R.id.sales_cust_street_name);
        et_cust_city = (EditText) findViewById(R.id.sales_cust_city);
        et_cust_office_number = (EditText) findViewById(R.id.sales_cust_office_number);
        et_cust_district = (EditText) findViewById(R.id.sales_cust_district);
        et_cust_state = (EditText) findViewById(R.id.sales_cust_state);
        et_cust_pincode = (EditText) findViewById(R.id.sales_cust_pincode);
        et_cust_p_name = (EditText) findViewById(R.id.sales_cust_p_name);
        et_cust_p_number = (EditText) findViewById(R.id.sales_cust_p_number);
        et_cust_p_id = (EditText) findViewById(R.id.sales_cust_p_id);
        et_cust_s_name = (EditText) findViewById(R.id.sales_cust_s_name);
        et_cust_s_number = (EditText) findViewById(R.id.sales_cust_s_number);
        et_cust_s_id = (EditText) findViewById(R.id.sales_cust_s_id);
        et_install_street_name = (EditText) findViewById(R.id.sales_install_street_name);
        et_install_city = (EditText) findViewById(R.id.sales_install_city);
        et_install_office_number = (EditText) findViewById(R.id.sales_install_office_number);
        et_install_district = (EditText) findViewById(R.id.sales_install_district);
        et_install_state = (EditText) findViewById(R.id.sales_install_state);
        et_install_pincode = (EditText) findViewById(R.id.sales_install_pincode);
        et_vehicle_type_other = (EditText) findViewById(R.id.sales_other_veh_type);
        et_voltage_other = (EditText) findViewById(R.id.sales_other_voltage);
        et_vts_quantity = (EditText) findViewById(R.id.sales_vts_quantity);
        et_drs_quantity = (EditText) findViewById(R.id.sales_drs_quantity);
        et_fuel_sensor_quantity = (EditText) findViewById(R.id.sales_fuel_sensor_quantity);
        et_door_sensor_quantity = (EditText) findViewById(R.id.sales_door_sensor_quantity);
        et_remarks = (EditText) findViewById(R.id.sales_remarks);
        et_client_nm = (EditText) findViewById(R.id.sales_client_name);
        et_priority_rsn = (EditText) findViewById(R.id.sales_priority_reason);
        et_order_no = (EditText) findViewById(R.id.sales_order_no);
        et_high_count = (EditText) findViewById(R.id.high_count);
        et_normal_count = (EditText) findViewById(R.id.normal_count);
        et_low_count = (EditText) findViewById(R.id.low_count);

        rg_instal_type = (RadioGroup) findViewById(R.id.installation_type);
        rg_client_type = (RadioGroup) findViewById(R.id.radiogroup_cust);
        rg_battry_voltage = (RadioGroup) findViewById(R.id.radiogroup_battry_voltage);
        //   rg_vehicle_type = (RadioGroup) findViewById(R.id.radiogroup_vehicle_type);
        // rg_vehicle_type_2 = (RadioGroup) findViewById(R.id.radiogroup_vehicle_type2);
        rg_accessory = (RadioGroup) findViewById(R.id.radiogroup_accessory);
        //rg_priority = (RadioGroup) findViewById(R.id.radiogroup_priority);
        rg_ignition = (RadioGroup) findViewById(R.id.radiogroup_ignition);

        rb_truck = (CheckBox) findViewById(R.id.r_truck);
        rb_bus = (CheckBox) findViewById(R.id.r_bus);
        rb_tm = (CheckBox) findViewById(R.id.r_tm);
        rb_tipper = (CheckBox) findViewById(R.id.r_tipper);
        rb_lcv = (CheckBox) findViewById(R.id.r_lcv);
        rb_car = (CheckBox) findViewById(R.id.r_car);
        rb_others = (CheckBox) findViewById(R.id.r_others);

        lay_acc_quan = (LinearLayout) findViewById(R.id.lay_quantity_accessory);
        lay_duration = (LinearLayout) findViewById(R.id.duration_lay);
        relay = (RelativeLayout) findViewById(R.id.relay);
        client = (MySearchableSpinner) findViewById(R.id.new_in_clients);
        et_vehicle_type_other.setVisibility(View.GONE);
        addclients();
        et_order_no.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode==KeyEvent.KEYCODE_ENTER)
                {
                    // Just ignore the [Enter] key
                    return true;
                }
                return  false;
            }
        });
        rb_others.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rb_others.isChecked()) {
                    et_vehicle_type_other.setVisibility(View.VISIBLE);
                }
                else {
                    et_vehicle_type_other.setVisibility(View.GONE);
                }
                }
        });
        client.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    client_name_s = client.getSelectedItem().toString();
                    try {
                        et_cust_street_name.setText(arr_sales_cust_detail.get(i).getCust_street_name());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        et_cust_city.setText(arr_sales_cust_detail.get(i).getCust_city());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        et_cust_office_number.setText(arr_sales_cust_detail.get(i).getCust_office_number());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        et_cust_district.setText(arr_sales_cust_detail.get(i).getCust_district());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        et_cust_state.setText(arr_sales_cust_detail.get(i).getCust_state());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        et_cust_pincode.setText(arr_sales_cust_detail.get(i).getCust_pincode());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        et_cust_p_name.setText(arr_sales_cust_detail.get(i).getCust_p_name());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        et_cust_p_number.setText(arr_sales_cust_detail.get(i).getCust_p_number());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        et_cust_p_id.setText(arr_sales_cust_detail.get(i).getCust_p_id());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        rg_ignition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ig_yes:
                        ignition_s = "Yes";
                        break;
                    case R.id.ig_no:
                        ignition_s = "No";
                        break;
                }
            }
        });
        rg_instal_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.r_permanent:
                        install_type_s = "permanent";
                        lay_duration.setVisibility(View.GONE);
                        break;
                    case R.id.r_demo:
                        install_type_s = "demo";
                        lay_duration.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        rg_client_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.r_new:
                        cust_type_s = "new";
                        et_client_nm.setVisibility(View.VISIBLE);
                        relay.setVisibility(View.GONE);
                        break;
                    case R.id.r_existing:
                        cust_type_s = "existing";
                        et_client_nm.setVisibility(View.GONE);
                        relay.setVisibility(View.VISIBLE);
                        break;
                    case R.id.r_old:
                        cust_type_s = "old";
                        et_client_nm.setVisibility(View.VISIBLE);
                        relay.setVisibility(View.GONE);
                        break;
                }
            }
        });
        rg_battry_voltage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.r_12_volt:
                        battery_voltage_s = "12 Volt";
                        et_voltage_other.setVisibility(View.GONE);
                        break;
                    case R.id.r_24_volt:
                        battery_voltage_s = "24 Volt";
                        et_voltage_other.setVisibility(View.GONE);
                        break;
                    case R.id.r_48_volt:
                        battery_voltage_s = "48 Volt";
                        et_voltage_other.setVisibility(View.GONE);
                        break;
                    case R.id.r_volt_others:
                        battery_voltage_s = "other"; //et_voltage_other.getText().toString();
                        et_voltage_other.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        rg_accessory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.r_yes:
                        lay_acc_quan.setVisibility(View.VISIBLE);
                        accessory_s = "Yes";
                        break;
                    case R.id.r_no:
                        lay_acc_quan.setVisibility(View.GONE);
                        accessory_s = "No";

                        break;
                }
            }
        });

        setDateAndTime();
        copy_address = (AppCompatCheckBox) findViewById(R.id.copy_address);
        preview_btn = (Button) findViewById(R.id.submit_requirement);
        copy_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (copy_address.isChecked()) {
                    try {
                        cust_street_name_s = et_cust_street_name.getText().toString();
                        et_install_street_name.setText(cust_street_name_s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        cust_city_s = et_cust_city.getText().toString();
                        et_install_city.setText(cust_city_s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        cust_office_number_s = et_cust_office_number.getText().toString();
                        et_install_office_number.setText(cust_office_number_s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        cust_district_s = et_cust_district.getText().toString();
                        et_install_district.setText(cust_district_s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        cust_state_s = et_cust_state.getText().toString();
                        et_install_state.setText(cust_state_s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        cust_pincode_s = et_cust_pincode.getText().toString();
                        et_install_pincode.setText(cust_pincode_s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    et_install_street_name.setText("");
                    et_install_city.setText("");
                    et_install_office_number.setText("");
                    et_install_district.setText("");
                    et_install_state.setText("");
                    et_install_pincode.setText("");
                }
            }
        });

        preview_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //preview_btn.setClickable(false);
                    StringBuffer result = new StringBuffer();
                    if (rb_car.isChecked()) {
                        result.append("car : ");
                    }
                    if (rb_bus.isChecked()) {
                        result.append("bus : ");
                    }
                    if (rb_lcv.isChecked()) {
                        result.append("lcv :");
                    }
                    if (rb_truck.isChecked()) {
                        result.append("truck :");
                    }
                    if (rb_tipper.isChecked()) {
                        result.append("tipper :");
                    }
                    if (rb_tm.isChecked()) {
                        result.append("tm :");
                    }
                    if (rb_others.isChecked()) {
                        result.append(et_vehicle_type_other.getText().toString());
                    }
                    vehicle_type_s = result.toString();
                } catch (NullPointerException npe) {
                    Toast.makeText(SalesInstallationActivity.this, "Select Vehicle Type", Toast.LENGTH_LONG).show();
                    npe.printStackTrace();
                }
                try {
                    if (battery_voltage_s.equalsIgnoreCase("other")) {
                        if (et_voltage_other.getText().toString().equals("")) {
                            et_voltage_other.setError("specify");
                        } else {
                            battery_voltage_s = et_voltage_other.getText().toString();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (cust_type_s.equalsIgnoreCase("new")) {
                        client_name_s = et_client_nm.getText().toString();
                    } else {
                        client_name_s = client.getSelectedItem().toString();
                        if (client_name_s.equalsIgnoreCase("SELECT")) {
                            client_name_s = "";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    client_name_s = et_client_nm.getText().toString();
                }
                order_no_s = et_order_no.getText().toString();
                if (!et_priority_rsn.getText().toString().equals("")) {
                    priority_reason = et_priority_rsn.getText().toString();
                } else {
                    priority_reason = "0";
                }
                sales_person_name_s = uusername;
                if (!client_name_s.equals("")) {
                    if (!et_cust_street_name.getText().toString().equals("")) {
                        cust_street_name_s = et_cust_street_name.getText().toString();
                        if (!et_cust_office_number.getText().toString().equals("")) {
                            cust_office_number_s = et_cust_office_number.getText().toString();
                            if (!et_cust_district.getText().toString().equals("")) {
                                cust_district_s = et_cust_district.getText().toString();
                                if (!et_cust_state.getText().toString().equals("")) {
                                    cust_state_s = et_cust_state.getText().toString();
                                    if (!et_cust_pincode.getText().toString().equals("")) {
                                        cust_pincode_s = et_cust_pincode.getText().toString();
                                        if (!et_cust_p_name.getText().toString().equals("")) {
                                            cust_p_name_s = et_cust_p_name.getText().toString();
                                            if (!et_cust_p_number.getText().toString().equals("")) {
                                                cust_p_number_s = et_cust_p_number.getText().toString();
                                                if (!et_vts_quantity.getText().toString().equals("")) {
                                                    vts_quantity_s = et_vts_quantity.getText().toString();
                                                    if(!et_high_count.getText().toString().equals("") && et_priority_rsn.getText().toString().equals("")) {
                                                        et_priority_rsn.setError("Specify reason of high priority");

                                                    }else {

                                                        if (!et_cust_p_id.getText().toString().equals("")) {
                                                            cust_p_id_s = et_cust_p_id.getText().toString();

                                                        } else {
                                                            cust_p_id_s = "0";
                                                        }
                                                        if (!et_cust_s_name.getText().toString().equals("")) {
                                                            cust_s_name_s = et_cust_s_name.getText().toString();
                                                        } else {
                                                            cust_s_name_s = "0";

                                                        }
                                                        if (!et_cust_s_number.getText().toString().equals("")) {
                                                            cust_s_number_s = et_cust_s_number.getText().toString();
                                                        } else {
                                                            cust_s_number_s = "0";
                                                        }
                                                        if (!et_cust_s_id.getText().toString().equals("")) {
                                                            cust_s_id_s = et_cust_s_id.getText().toString();
                                                        } else {
                                                            cust_s_id_s = "0";
                                                        }
                                                        cust_city_s= et_cust_city.getText().toString();

                                                        if (!et_drs_quantity.getText().toString().equals("")) {
                                                            drs_quantity_s = et_drs_quantity.getText().toString();
                                                        } else {
                                                            drs_quantity_s = "0";
                                                        }
                                                        if (!et_fuel_sensor_quantity.getText().toString().equals("")) {
                                                            fuel_sensor_quantity_s = et_fuel_sensor_quantity.getText().toString();
                                                        } else {
                                                            fuel_sensor_quantity_s = "0";
                                                        }
                                                        if (!et_door_sensor_quantity.getText().toString().equals("")) {
                                                            door_sensor_quantity_s = et_door_sensor_quantity.getText().toString();
                                                        } else {
                                                            door_sensor_quantity_s = "0";
                                                        }

                                                        if (!et_remarks.getText().toString().equals("")) {
                                                            remarks_s = et_remarks.getText().toString();
                                                        } else {
                                                            remarks_s = "0";
                                                        }
                                                        if (!et_install_street_name.getText().toString().equals("")) {
                                                            install_street_name_s = et_install_street_name.getText().toString();
                                                        } else {
                                                            install_street_name_s = "0";
                                                        }
                                                        if (!et_install_city.getText().toString().equals("")) {
                                                            install_city_s = et_install_city.getText().toString();
                                                        } else {
                                                            install_city_s = "0";
                                                        }
                                                        if (!et_install_office_number.getText().toString().equals("")) {
                                                            install_office_number_s = et_install_office_number.getText().toString();
                                                        } else {
                                                            install_office_number_s = "0";
                                                        }
                                                        if (!et_install_district.getText().toString().equals("")) {
                                                            install_district_s = et_install_district.getText().toString();
                                                        } else {
                                                            install_district_s = "0";
                                                        }
                                                        if (!et_install_state.getText().toString().equals("")) {
                                                            install_state_s = et_install_state.getText().toString();
                                                        } else {
                                                            install_state_s = "0";
                                                        }
                                                        if (!et_install_pincode.getText().toString().equals("")) {
                                                            install_pincode_s = et_install_pincode.getText().toString();
                                                        } else {
                                                            install_pincode_s = "0";
                                                        }
                                                        if (et_high_count.getText().toString().equals("")) {
                                                            high_count_s = "0";
                                                        } else {
                                                            high_count_s = et_high_count.getText().toString();
                                                        }
                                                        if (et_normal_count.getText().toString().equals("")) {
                                                            normal_count_s = "0";
                                                        } else {
                                                            normal_count_s = et_normal_count.getText().toString();
                                                        }
                                                        if (et_low_count.getText().toString().equals("")) {
                                                            low_count_s = "0";
                                                        } else {
                                                            low_count_s = et_low_count.getText().toString();
                                                        }
                                                        if (et_duration.getText().toString().equals("")) {
                                                            duration_s = "0";
                                                        } else {
                                                            duration_s = et_duration.getText().toString();
                                                        }
                                                        if (accessory_s.equalsIgnoreCase("No")) {
                                                            ignition_s = "No";
                                                            drs_quantity_s = "0";
                                                            fuel_sensor_quantity_s = "0";
                                                            door_sensor_quantity_s = "0";
                                                        }
                                                        try {
                                                            preview_value.clear();
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        preview_value.add(sales_person_name_s.toUpperCase());
                                                        preview_value.add(order_no_s);
                                                        preview_value.add(install_type_s);
                                                        preview_value.add(duration_s);
                                                        preview_value.add(cust_type_s);
                                                        preview_value.add(client_name_s);
                                                        preview_value.add(cust_street_name_s);
                                                        preview_value.add(cust_city_s);
                                                        preview_value.add(cust_office_number_s);
                                                        preview_value.add(cust_district_s);
                                                        preview_value.add(cust_state_s);
                                                        preview_value.add(cust_pincode_s);
                                                        preview_value.add(cust_p_name_s);
                                                        preview_value.add(cust_p_number_s);
                                                        preview_value.add(cust_p_id_s);
                                                        preview_value.add(cust_s_name_s);
                                                        preview_value.add(cust_s_number_s);
                                                        preview_value.add(cust_s_id_s);
                                                        preview_value.add(":");
                                                        preview_value.add(install_street_name_s);
                                                        preview_value.add(install_city_s);
                                                        preview_value.add(install_office_number_s);
                                                        preview_value.add(install_district_s);
                                                        preview_value.add(install_state_s);
                                                        preview_value.add(install_pincode_s);
                                                        preview_value.add(vehicle_type_s);
                                                        preview_value.add(battery_voltage_s);
                                                        preview_value.add(vts_quantity_s);
                                                        preview_value.add(ignition_s);
                                                        preview_value.add(accessory_s);
                                                        preview_value.add(drs_quantity_s);
                                                        preview_value.add(fuel_sensor_quantity_s);
                                                        preview_value.add(door_sensor_quantity_s);
                                                        preview_value.add(high_count_s);
                                                        preview_value.add(normal_count_s);
                                                        preview_value.add(low_count_s);
                                                        preview_value.add(priority_reason);
                                                        preview_value.add(remarks_s);

                                                        previewBox("Review Data", preview_value, R.style.MyAlertDialogStyleConnected, 0);

                                                    }
                                                } else {
                                                    et_vts_quantity.setError("");
                                                    scrollToView(sv, et_vts_quantity);
                                                }
                                            } else {
                                                et_cust_p_number.setError("fill");
                                                scrollToView(sv, et_cust_p_number);
                                            }
                                        } else {
                                            et_cust_p_name.setError("fill");
                                            scrollToView(sv, et_cust_p_name);
                                        }
                                    } else {
                                        et_cust_pincode.setError("fill");
                                        scrollToView(sv, et_cust_pincode);
                                    }
                                } else {
                                    et_cust_state.setError("fill");
                                    scrollToView(sv, et_cust_state);
                                }
                            } else {
                                et_cust_district.setError("fill");
                                scrollToView(sv, et_cust_district);
                            }
                        } else {
                            et_cust_office_number.setError("fill");
                            scrollToView(sv, et_cust_office_number);
                        }
                    } else {
                        et_cust_street_name.setError("fill");
                        scrollToView(sv, et_cust_street_name);
                    }
                } else {
                    Toast.makeText(SalesInstallationActivity.this, "select client", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    void setDateAndTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        s_time = dateFormat.format(calen.getTime());
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        if (month + 1 < 10) {
            current_date = year + "-0" + +month + "-" + day;
        } else {
            current_date = year + "-" + month + "-" + day;
        }
    }

    public void addclients() {
        pDialog = new ProgressDialog(SalesInstallationActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        ApiHolder log_att = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<UpdateDataResponse> call = log_att.sales_client_detail(usrname);
        Log.i("****call", String.valueOf(call));
        call.enqueue(new Callback<UpdateDataResponse>() {
            @Override
            public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                UpdateDataResponse updateDataResponse = response.body();
                Log.i("**respnse", " " + response.body());
                if (updateDataResponse != null) {
                    if (updateDataResponse.getType() == 1) {
                        arr_sales_cust_detail = updateDataResponse.getSales_cust_detail();
                        for (int i = 0; i < arr_sales_cust_detail.size(); i++) {
                            cust_names.add(arr_sales_cust_detail.get(i).getCust_name());
                        }
                        adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_items, cust_names);
                        adp.setDropDownViewResource(R.layout.spinner_items);
                        client.setAdapter(adp);
                    }
                } else {
                    assert updateDataResponse != null;
                    Log.v("Response", updateDataResponse.toString());
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                t.printStackTrace();
                pDialog.dismiss();
                Toast.makeText(SalesInstallationActivity.this, "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent inteer = new Intent(SalesInstallationActivity.this, SalesMainActivity.class);
            startActivity(inteer);
            finish();
        } else if (id == R.id.menu_logout) {
            Intent inteer = new Intent(SalesInstallationActivity.this, LoginActivity.class);
            inteer.putExtra("username", "us");
            editor.putString("pass", "");
            editor.commit();
            startActivity(inteer);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent inteer = new Intent(SalesInstallationActivity.this, SalesMainActivity.class);
        startActivity(inteer);
        finish();
        //super.onBackPressed();
    }

    void alertBox(String title, CharSequence msg, int style, int type) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SalesInstallationActivity.this, style);
        builder.setCancelable(false);
        if (type == 1) {
            builder.setMessage(msg);
            builder.setPositiveButton("Billing Information",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            Intent intent = new Intent(SalesInstallationActivity.this, BillingDetailActivity.class);
                            intent.putExtra("order_no_s", order_no_s);
                            intent.putExtra("install_type_s", install_type_s);
                            intent.putExtra("cust_type_s", cust_type_s);
                            intent.putExtra("client_name_s", client_name_s);
                            intent.putExtra("cust_street_name_s", cust_street_name_s);
                            intent.putExtra("cust_city_s", cust_city_s);
                            intent.putExtra("cust_office_number_s", cust_office_number_s);
                            intent.putExtra("cust_district_s", cust_district_s);
                            intent.putExtra("cust_state_s", cust_state_s);
                            intent.putExtra("cust_pincode_s", cust_pincode_s);
                            intent.putExtra("cust_p_name_s", cust_p_name_s);
                            intent.putExtra("cust_p_number_s", cust_p_number_s);
                            intent.putExtra("cust_p_id_s", cust_p_id_s);
                            intent.putExtra("cust_s_name_s", cust_s_name_s);
                            intent.putExtra("cust_s_number_s", cust_s_number_s);
                            intent.putExtra("cust_s_id_s", cust_s_id_s);
                            startActivity(intent);
                            finish();
                            // Fill Billing Intimation for this customer
                        }
                    });
            builder.setIcon(R.drawable.ic_check_circle_black_24dp);
        } else if (type == 2) {
            builder.setMessage(msg);
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();
                        }
                    });
            builder.setIcon(R.drawable.ic_check_circle_black_24dp);
        } else if (type == 3) {
            builder.setMessage(msg);
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();

                        }
                    });
            builder.setIcon(R.drawable.ic_not_submit_24dp);
        }
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();
    }

    void previewBox(String title, ArrayList<String> pre, int style, int type) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SalesInstallationActivity.this, style);
        builder.setCancelable(false);
        if (type == 0) {
            LayoutInflater inflater = LayoutInflater.from(SalesInstallationActivity.this);
            View dialogLayout = inflater.inflate(R.layout.preview_list, null);
            builder.setView(dialogLayout);
            ListView lv = (ListView) dialogLayout.findViewById(R.id.prevw_lst);

            lv.setAdapter(new CustomAdp(SalesInstallationActivity.this, pre));
            builder.setPositiveButton("Save Data", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    sendDataToServer();

                }
            });
            builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();
    }

    private void sendDataToServer() {
        pDialog = new ProgressDialog(SalesInstallationActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        ApiHolder log_att = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<UpdateDataResponse> call = log_att.vts_requirement(sales_person_name_s,
                current_date,
                order_no_s,
                install_type_s,
                duration_s,
                cust_type_s,
                client_name_s,
                cust_street_name_s,
                cust_city_s,
                cust_office_number_s,
                cust_district_s,
                cust_state_s,
                cust_pincode_s,
                cust_p_name_s,
                cust_p_number_s,
                cust_p_id_s,
                cust_s_name_s,
                cust_s_number_s,
                cust_s_id_s,
                install_street_name_s,
                install_city_s,
                install_office_number_s,
                install_district_s,
                install_state_s,
                install_pincode_s,
                vehicle_type_s,
                battery_voltage_s,
                vts_quantity_s,
                accessory_s,
                drs_quantity_s,
                fuel_sensor_quantity_s,
                door_sensor_quantity_s,
                priority_reason,
                remarks_s, ignition_s, high_count_s, normal_count_s, low_count_s);
        Log.i("****call", String.valueOf(call));
        call.enqueue(new Callback<UpdateDataResponse>() {
            @Override
            public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                UpdateDataResponse updateDataResponse = response.body();
                Log.i("**respnse", " " + response.body());
                if (updateDataResponse != null) {
                    Toast.makeText(SalesInstallationActivity.this, updateDataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    if (updateDataResponse.getType() == 1) {
                        if (install_type_s.equalsIgnoreCase("demo")) {
                            alertBox("Submitted", "New Requirement has been submitted.", R.style.MyAlertDialogStyleConnected, 2);
                        } else {
                            alertBox("Submitted", "New Requirement has been submitted. Fill Billing Intimation for customer.", R.style.MyAlertDialogStyleConnected, 1);
                        }
                    } else if (updateDataResponse.getType() == 2) {
                        alertBox("Order number", updateDataResponse.getMessage(), R.style.MyAlertDialogStyleDisConnected, 3);
                    }
                } else {
                    assert updateDataResponse != null;
                    Log.v("Response", updateDataResponse.toString());
                }
                try {
                    if (pDialog != null)
                        pDialog.dismiss();
                } catch (IllegalArgumentException iae) {
                    iae.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                t.printStackTrace();
                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(SalesInstallationActivity.this, "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
            }
        });
    }

    void clearalldata() {
        et_duration.setText("");
        et_cust_street_name.setText("");
        et_cust_city.setText("");
        et_cust_office_number.setText("");
        et_cust_district.setText("");
        et_cust_state.setText("");
        et_cust_pincode.setText("");
        et_cust_p_name.setText("");
        et_cust_p_number.setText("");
        et_cust_p_id.setText("");
        et_cust_s_name.setText("");
        et_cust_s_number.setText("");
        et_cust_s_id.setText("");
        et_install_street_name.setText("");
        et_install_city.setText("");
        et_install_office_number.setText("");
        et_install_district.setText("");
        et_install_state.setText("");
        et_install_pincode.setText("");
        et_vehicle_type_other.setText("");
        et_voltage_other.setText("");
        et_vts_quantity.setText("");
        et_drs_quantity.setText("");
        et_fuel_sensor_quantity.setText("");
        et_door_sensor_quantity.setText("");
        et_remarks.setText("");
        et_client_nm.setText("");
        et_priority_rsn.setText("");
        et_order_no.setText("");
        et_high_count.setText("");
        et_normal_count.setText("");
        et_low_count.setText("");

    }


    /**
     * Used to scroll to the given view.
     *
     * @param scrollViewParent Parent ScrollView
     * @param view             View to which we need to scroll.
     */
    private void scrollToView(final ScrollView scrollViewParent, final View view) {
        // Get deepChild Offset
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, childOffset.y);
    }

    /**
     * Used to get deep child offset.
     * <p/>
     * 1. We need to scroll to child in scrollview, but the child may not the direct child to scrollview.
     * 2. So to get correct child position to scroll, we need to iterate through all of its parent views till the main parent.
     *
     * @param mainParent        Main Top parent.
     * @param parent            Parent.
     * @param child             Child.
     * @param accumulatedOffset Accumulated Offset.
     */
    private void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }


    class CustomAdp extends BaseAdapter {
        LayoutInflater lay_infa;
        Context ctx;
        ArrayList<String> preview_valu;
        String arr[] = {
                getString(R.string.filled_by),
                getString(R.string.order_no),
                getString(R.string.install_type),
                getString(R.string.duration),
                getString(R.string.cust_type),
                getString(R.string.client_name),
                getString(R.string.client_street_name),
                getString(R.string.client_city),
                getString(R.string.client_office_number),
                getString(R.string.client_district),
                getString(R.string.client_state),
                getString(R.string.client_pincode),
                getString(R.string.contact_name),
                getString(R.string.contact_number),
                getString(R.string.contact_id),
                getString(R.string.contact_name),
                getString(R.string.contact_number),
                getString(R.string.contact_id),
                getString(R.string.INSTALLATION_ADDRESS),
                getString(R.string.street_name),
                getString(R.string.city),
                getString(R.string.office_number),
                getString(R.string.district),
                getString(R.string.state),
                getString(R.string.pincode),
                getString(R.string.vehicle_type),
                getString(R.string.battery_voltage),
                getString(R.string.vts_quantity_pre),
                getString(R.string.ignition),
                getString(R.string.accessory),
                getString(R.string.drs_quantity),
                getString(R.string.fuel_sensor_quanti),
                getString(R.string.door_sensor_quanti),
                getString(R.string.high_count),
                getString(R.string.normal_count),
                getString(R.string.low_count),
                getString(R.string.priority_reason),
                getString(R.string.remarks)};


        CustomAdp(Context c, ArrayList<String> preview_val) {
            ctx = c;
            preview_valu = preview_val;
            lay_infa = LayoutInflater.from(ctx);
        }

        @Override
        public int getCount() {
            return arr.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                v = lay_infa.inflate(R.layout.preview_layout, null);
            }
            TextView tags = (TextView) v.findViewById(R.id.preview_tags);
            TextView values = (TextView) v.findViewById(R.id.preview_values);
            tags.setText(arr[position]);
            values.setText(preview_valu.get(position));

            return v;
        }
    }
}