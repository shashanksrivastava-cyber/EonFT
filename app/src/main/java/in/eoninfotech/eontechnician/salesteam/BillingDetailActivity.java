package in.eoninfotech.eontechnician.salesteam;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.OrderListDetail;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.activity.LoginActivity;
import in.eoninfotech.eontechnician.helper.FileUtils;
import in.eoninfotech.eontechnician.helper.MyTextWatcher;
import in.eoninfotech.eontechnician.helper.ProgressRequestBody;
import in.eoninfotech.eontechnician.helper.SalesCustDetail;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillingDetailActivity extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks {
    ProgressBar progressBar;

    EditText et_cust_street_name, et_cust_city, et_cust_office_number, et_cust_district,
            et_cust_state, et_cust_pincode, et_cust_p_name, et_cust_p_number, et_cust_p_id, et_cust_s_name,
            et_cust_s_number, et_cust_s_id, et_units_quantity, et_gst_no, et_pan_no,
            et_price_hardware, et_tax_hardware, et_price_month_recu, et_tax_month_recu,
            et_price_hardware_pump, et_tax_hardware_pump, et_price_without_alerts, et_tax_without_alerts,
            et_price_setup, et_tax_setup, et_price_instal, et_tax_instal,
            et_price_accessory, et_tax_accessory, et_price_other, et_tax_other,
            et_amc_charges, et_client_nm, et_remarks, et_tax_percent;
    String order_no_s = "0", install_type_s, cust_type_s,
            client_name_s = "0", cust_street_name_s, cust_city_s, cust_office_number_s,
            cust_district_s, cust_state_s, cust_pincode_s = "0", cust_p_name_s,
            cust_p_number_s, cust_p_id_s, cust_s_name_s, cust_s_number_s, gst_no_s, pan_no_s,
            cust_s_id_s, units_quantity, price_hardware_s, tax_hardware_s, price_month_recu_s,
            tax_month_recu_s, price_setup_s, tax_setup_s, price_instal_s, tax_instal_s,
            price_accessory_s, tax_accessory_s, price_other_s, tax_other_s, amc_charges_s,
            price_hardware_pump_s, tax_hardware_pump_s, price_without_alerts_s,
            tax_without_alerts_s, remarks_s, amc_date_s, sale_id_s;
    String bill_date, amc_date, path;
    int READ_REQUEST_CODE = 0;
    TextView tx_amc_date, tx_filename;
    RadioGroup rg_instal_type, rg_client_type;
    RadioButton r_outrigt, r_rental, r_new, r_existing;
    Button preview, b_select_file;
    RelativeLayout relay;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String usrname, uusername, disgnid = "0", version;
    String current_date, s_update_date;
    int year, month, day;
    Calendar calen;
    ProgressDialog pDialog;
    ScrollView sv;
    MySearchableSpinner client, spin_order_no;
    ArrayList<SalesCustDetail> arr_sales_cust_detail = new ArrayList<>();
    ArrayList<String> cust_names = new ArrayList<>();
    ArrayAdapter<String> adp;
    double tax_var;
    File file;
    Uri uri;
    ArrayList<String> preview_value = new ArrayList<>();
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    int REQUEST_CODE_PERMISSION = 10;
    ArrayList<String> arr_order_no = new ArrayList<>();
    ArrayList<OrderListDetail> orderListDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Billing Information");
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
        sv = (ScrollView) findViewById(R.id.scroll_bill_view);
        et_client_nm = (EditText) findViewById(R.id.bi_client_name);
        et_cust_street_name = (EditText) findViewById(R.id.bi_cust_street_name);
        et_cust_city = (EditText) findViewById(R.id.bi_cust_city);
        et_cust_office_number = (EditText) findViewById(R.id.bi_cust_office_number);
        et_cust_district = (EditText) findViewById(R.id.bi_cust_district);
        et_cust_state = (EditText) findViewById(R.id.bi_cust_state);
        et_cust_pincode = (EditText) findViewById(R.id.bi_cust_pincode);
        et_cust_p_name = (EditText) findViewById(R.id.bi_cust_p_name);
        et_cust_p_number = (EditText) findViewById(R.id.bi_cust_p_number);
        et_cust_p_id = (EditText) findViewById(R.id.bi_cust_p_id);
        et_cust_s_name = (EditText) findViewById(R.id.bi_cust_s_name);
        et_cust_s_number = (EditText) findViewById(R.id.bi_cust_s_number);
        et_cust_s_id = (EditText) findViewById(R.id.bi_cust_s_id);
        et_price_hardware = (EditText) findViewById(R.id.bi_price_hardware);
        et_tax_hardware = (EditText) findViewById(R.id.bi_tax_hardware);
        et_price_month_recu = (EditText) findViewById(R.id.bi_price_month_recu);
        et_tax_month_recu = (EditText) findViewById(R.id.bi_tax_month_recu);
        et_price_setup = (EditText) findViewById(R.id.bi_price_setup);
        et_tax_setup = (EditText) findViewById(R.id.bi_tax_setup);
        et_price_instal = (EditText) findViewById(R.id.bi_price_instal);
        et_tax_instal = (EditText) findViewById(R.id.bi_tax_instal);
        et_price_accessory = (EditText) findViewById(R.id.bi_price_accessory);
        et_tax_accessory = (EditText) findViewById(R.id.bi_tax_accessory);
        et_price_other = (EditText) findViewById(R.id.bi_price_other);
        et_tax_other = (EditText) findViewById(R.id.bi_tax_other);
        et_amc_charges = (EditText) findViewById(R.id.bi_amc_charges);
        et_remarks = (EditText) findViewById(R.id.bi_remarks);
        et_units_quantity = (EditText) findViewById(R.id.bi_units_quantity);
        et_tax_percent = (EditText) findViewById(R.id.bi_tax_percentage);
        et_gst_no = (EditText) findViewById(R.id.bi_cust_gst_no);
        et_pan_no = (EditText) findViewById(R.id.bi_cust_pan_no);
        et_price_hardware_pump = (EditText) findViewById(R.id.bi_price_pump);
        et_tax_hardware_pump = (EditText) findViewById(R.id.bi_tax_pump);
        et_price_without_alerts = (EditText) findViewById(R.id.bi_price_without_alerts);
        et_tax_without_alerts = (EditText) findViewById(R.id.bi_tax_without_alerts);

        spin_order_no = (MySearchableSpinner) findViewById(R.id.bi_order_no);
        client = (MySearchableSpinner) findViewById(R.id.bi_clients_list);

        tx_amc_date = (TextView) findViewById(R.id.amc_start_date);
        tx_filename = (TextView) findViewById(R.id.file_name);
        //tx_bill_date = (TextView) findViewById(R.id.billing_date);

        rg_instal_type = (RadioGroup) findViewById(R.id.bi_installation_type);
        rg_client_type = (RadioGroup) findViewById(R.id.radiogroup_cust);
        r_outrigt = (RadioButton) findViewById(R.id.r_outright);
        r_rental = (RadioButton) findViewById(R.id.r_rental);
        r_new = (RadioButton) findViewById(R.id.r_new);
        r_existing = (RadioButton) findViewById(R.id.r_existing);
        b_select_file = (Button) findViewById(R.id.select_file);

        relay = (RelativeLayout) findViewById(R.id.relay);
        preview = (Button) findViewById(R.id.preview_bill);
        calen = Calendar.getInstance();
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        if (month + 1 < 10) {
            current_date = year + "-0" + +month + "-" + day;
        } else {
            current_date = year + "-" + month + "-" + day;
        }
        tx_amc_date.setText(current_date);
        tx_amc_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("***string**", tx_amc_date.getText().toString());
                DatePickerDialog dpdd = new DatePickerDialog(BillingDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    // TODO Auto-generated method stub
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear + 1 < 10) {
                            amc_date = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                        } else {
                            amc_date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        }
                        tx_amc_date.setText(amc_date);
                    }
                }, year, month - 1, day);
                dpdd.show();
            }
        });
        b_select_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");

                //startActivityForResult(intent, READ_REQUEST_CODE);
                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a File to Upload"),
                            READ_REQUEST_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(BillingDetailActivity.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                    try {
                        //checkWritingPermission();
                        if (!hasPermissions(BillingDetailActivity.this, PERMISSIONS)) {
                            ActivityCompat.requestPermissions(BillingDetailActivity.this, PERMISSIONS, PERMISSION_ALL);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        addclients();
        addorder_no();
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

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        try {
            order_no_s = getIntent().getStringExtra("order_no_s");
            cust_type_s = getIntent().getStringExtra("cust_type_s");
            client_name_s = getIntent().getStringExtra("client_name_s");
            cust_street_name_s = getIntent().getStringExtra("cust_street_name_s");
            cust_city_s = getIntent().getStringExtra("cust_city_s");
            cust_office_number_s = getIntent().getStringExtra("cust_office_number_s");
            cust_district_s = getIntent().getStringExtra("cust_district_s");
            cust_state_s = getIntent().getStringExtra("cust_state_s");
            cust_pincode_s = getIntent().getStringExtra("cust_pincode_s");

            et_cust_street_name.setText(cust_street_name_s);
            et_cust_city.setText(cust_city_s);
            et_cust_office_number.setText(cust_office_number_s);
            et_cust_district.setText(cust_district_s);
            et_cust_state.setText(cust_state_s);
            et_cust_pincode.setText(cust_pincode_s);

            if (cust_type_s.equalsIgnoreCase("new")) {
                r_new.setChecked(true);
                et_client_nm.setVisibility(View.VISIBLE);
                relay.setVisibility(View.GONE);
                et_client_nm.setText(client_name_s);

            } else {
                r_existing.setChecked(true);
                et_client_nm.setVisibility(View.GONE);
                relay.setVisibility(View.VISIBLE);
                client.setSelection(getIndex(client, client_name_s));
            }

        } catch (Exception e) {
            relay.setVisibility(View.GONE);
            et_client_nm.setVisibility(View.GONE);
            e.printStackTrace();
        }
        rg_instal_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.r_outright:
                        install_type_s = "outright";
                        break;
                    case R.id.r_rental:
                        install_type_s = "rental";
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
                }
            }
        });
        et_tax_percent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_price_hardware.removeTextChangedListener(new MyTextWatcher(et_tax_hardware, tax_var));
                et_price_month_recu.removeTextChangedListener(new MyTextWatcher(et_tax_month_recu, tax_var));
                et_price_accessory.removeTextChangedListener(new MyTextWatcher(et_tax_accessory, tax_var));
                et_price_instal.removeTextChangedListener(new MyTextWatcher(et_tax_instal, tax_var));
                et_price_setup.removeTextChangedListener(new MyTextWatcher(et_tax_setup, tax_var));
                et_price_other.removeTextChangedListener(new MyTextWatcher(et_tax_other, tax_var));
                et_price_hardware_pump.removeTextChangedListener(new MyTextWatcher(et_tax_hardware_pump, tax_var));
                et_price_without_alerts.removeTextChangedListener(new MyTextWatcher(et_tax_without_alerts, tax_var));

                et_price_hardware.setText("");
                et_price_month_recu.setText("");
                et_price_accessory.setText("");
                et_price_instal.setText("");
                et_price_setup.setText("");
                et_price_other.setText("");
                et_price_hardware_pump.setText("");
                et_price_without_alerts.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {

                    double gst = Double.parseDouble(s.toString());
                    tax_var = gst / 100;
                    Log.i("*** tax perc", "" + gst + tax_var);
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    tax_var = 0;
                }
                et_price_hardware.addTextChangedListener(new MyTextWatcher(et_tax_hardware, tax_var));
                et_price_month_recu.addTextChangedListener(new MyTextWatcher(et_tax_month_recu, tax_var));
                et_price_accessory.addTextChangedListener(new MyTextWatcher(et_tax_accessory, tax_var));
                et_price_instal.addTextChangedListener(new MyTextWatcher(et_tax_instal, tax_var));
                et_price_setup.addTextChangedListener(new MyTextWatcher(et_tax_setup, tax_var));
                et_price_other.addTextChangedListener(new MyTextWatcher(et_tax_other, tax_var));
                et_price_hardware_pump.addTextChangedListener(new MyTextWatcher(et_tax_hardware_pump, tax_var));
                et_price_without_alerts.addTextChangedListener(new MyTextWatcher(et_tax_without_alerts, tax_var));
            }
        });
        double gst = Double.parseDouble(et_tax_percent.getText().toString());
        tax_var = gst / 100;
        et_price_hardware.addTextChangedListener(new MyTextWatcher(et_tax_hardware, tax_var));
        et_price_month_recu.addTextChangedListener(new MyTextWatcher(et_tax_month_recu, tax_var));
        et_price_accessory.addTextChangedListener(new MyTextWatcher(et_tax_accessory, tax_var));
        et_price_instal.addTextChangedListener(new MyTextWatcher(et_tax_instal, tax_var));
        et_price_setup.addTextChangedListener(new MyTextWatcher(et_tax_setup, tax_var));
        et_price_other.addTextChangedListener(new MyTextWatcher(et_tax_other, tax_var));
        et_price_hardware_pump.addTextChangedListener(new MyTextWatcher(et_tax_hardware_pump, tax_var));
        et_price_without_alerts.addTextChangedListener(new MyTextWatcher(et_tax_without_alerts, tax_var));

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    order_no_s = spin_order_no.getSelectedItem().toString();
                    sale_id_s = orderListDetails.get(spin_order_no.getSelectedItemPosition()).getSale_id();
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
                if (!client_name_s.equals("")) {
                    if (!et_cust_street_name.getText().toString().equals("")) {
                        cust_street_name_s = et_cust_street_name.getText().toString();
                        cust_city_s = et_cust_city.getText().toString();
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
                                                if (!et_units_quantity.getText().toString().equals("")) {
                                                    if (!et_gst_no.getText().toString().equals("")) {
                                                        if (!et_pan_no.getText().toString().equals("")) {
                                                            if(!install_type_s.equalsIgnoreCase("")) {
                                                                if (!et_price_hardware.getText().toString().equals("")) {
                                                                    price_hardware_s = et_price_hardware.getText().toString();
                                                                } else {
                                                                    price_hardware_s = "0";
                                                                }
                                                                if (!et_tax_hardware.getText().toString().equals("")) {
                                                                    tax_hardware_s = et_tax_hardware.getText().toString();
                                                                } else {
                                                                    tax_hardware_s = "0";
                                                                }
                                                                if (!et_price_hardware_pump.getText().toString().equals("")) {
                                                                    price_hardware_pump_s = et_price_hardware_pump.getText().toString();
                                                                } else {
                                                                    price_hardware_pump_s = "0";
                                                                }
                                                                if (!et_tax_hardware_pump.getText().toString().equals("")) {
                                                                    tax_hardware_pump_s = et_tax_hardware_pump.getText().toString();
                                                                } else {
                                                                    tax_hardware_pump_s = "0";
                                                                }
                                                                if (!et_price_without_alerts.getText().toString().equals("")) {
                                                                    price_without_alerts_s = et_price_without_alerts.getText().toString();
                                                                } else {
                                                                    price_without_alerts_s = "0";
                                                                }
                                                                if (!et_tax_without_alerts.getText().toString().equals("")) {
                                                                    tax_without_alerts_s = et_tax_without_alerts.getText().toString();
                                                                } else {
                                                                    tax_without_alerts_s = "0";
                                                                }
                                                                if (!et_price_month_recu.getText().toString().equals("")) {
                                                                    price_month_recu_s = et_price_month_recu.getText().toString();
                                                                } else {
                                                                    price_month_recu_s = "0";
                                                                }
                                                                if (!et_tax_month_recu.getText().toString().equals("")) {
                                                                    tax_month_recu_s = et_tax_month_recu.getText().toString();
                                                                } else {
                                                                    tax_month_recu_s = "0";
                                                                }
                                                                if (!et_price_setup.getText().toString().equals("")) {
                                                                    price_setup_s = et_price_setup.getText().toString();
                                                                } else {
                                                                    price_setup_s = "0";
                                                                }
                                                                if (!et_tax_setup.getText().toString().equals("")) {
                                                                    tax_setup_s = et_tax_setup.getText().toString();
                                                                } else {
                                                                    tax_setup_s = "0";
                                                                }
                                                                if (!et_price_instal.getText().toString().equals("")) {
                                                                    price_instal_s = et_price_instal.getText().toString();
                                                                } else {
                                                                    price_instal_s = "0";
                                                                }
                                                                if (!et_tax_instal.getText().toString().equals("")) {
                                                                    tax_instal_s = et_tax_instal.getText().toString();
                                                                } else {
                                                                    tax_instal_s = "0";
                                                                }
                                                                if (!et_price_accessory.getText().toString().equals("")) {
                                                                    price_accessory_s = et_price_accessory.getText().toString();
                                                                } else {
                                                                    price_accessory_s = "0";
                                                                }
                                                                if (!et_tax_accessory.getText().toString().equals("")) {
                                                                    tax_accessory_s = et_tax_accessory.getText().toString();
                                                                } else {
                                                                    tax_accessory_s = "0";
                                                                }
                                                                if (!et_price_other.getText().toString().equals("")) {
                                                                    price_other_s = et_price_other.getText().toString();
                                                                } else {
                                                                    price_other_s = "0";
                                                                }
                                                                if (!et_tax_other.getText().toString().equals("")) {
                                                                    tax_other_s = et_tax_other.getText().toString();
                                                                } else {
                                                                    tax_other_s = "0";
                                                                }
                                                                if (!et_remarks.getText().toString().equals("")) {
                                                                    remarks_s = et_remarks.getText().toString();
                                                                } else {
                                                                    remarks_s = "0";
                                                                }
                                                                if (!et_amc_charges.getText().toString().equals("")) {
                                                                    amc_charges_s = et_amc_charges.getText().toString();
                                                                } else {
                                                                    amc_charges_s = "0";
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
                                                                if (!et_cust_p_id.getText().toString().equals("")) {
                                                                    cust_p_id_s = et_cust_p_id.getText().toString();
                                                                } else {
                                                                    cust_p_id_s = "0";
                                                                }
                                                                units_quantity = et_units_quantity.getText().toString();
                                                                amc_date_s = tx_amc_date.getText().toString();
                                                                gst_no_s = et_gst_no.getText().toString();
                                                                pan_no_s = et_pan_no.getText().toString();

                                                                try {
                                                                    preview_value.clear();
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                                preview_value.add(usrname);
                                                                preview_value.add(order_no_s);
                                                                preview_value.add(install_type_s);
                                                                preview_value.add(client_name_s);
                                                                preview_value.add(cust_street_name_s);
                                                                preview_value.add(cust_city_s);
                                                                preview_value.add(gst_no_s);
                                                                preview_value.add(pan_no_s);
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
                                                                preview_value.add(price_hardware_s);
                                                                preview_value.add(tax_hardware_s);
                                                                preview_value.add(price_without_alerts_s);
                                                                preview_value.add(tax_without_alerts_s);
                                                                preview_value.add(price_hardware_pump_s);
                                                                preview_value.add(tax_hardware_pump_s);
                                                                preview_value.add(price_month_recu_s);
                                                                preview_value.add(tax_month_recu_s);
                                                                preview_value.add(price_setup_s);
                                                                preview_value.add(tax_setup_s);
                                                                preview_value.add(price_instal_s);
                                                                preview_value.add(tax_instal_s);
                                                                preview_value.add(price_accessory_s);
                                                                preview_value.add(tax_accessory_s);
                                                                preview_value.add(price_other_s);
                                                                preview_value.add(tax_other_s);
                                                                preview_value.add(amc_charges_s);
                                                                preview_value.add(amc_date_s);
                                                                preview_value.add(remarks_s);

                                                                alertBox("Review Data", preview_value, R.style.MyAlertDialogStyleConnected);
                                                            }else {
                                                                scrollToView(sv, spin_order_no);
                                                                Toast.makeText(BillingDetailActivity.this, "select installation type!", Toast.LENGTH_LONG).show();
                                                            }
                                                            } else {
                                                            et_pan_no.setError("please enter");
                                                            scrollToView(sv, et_pan_no);
                                                        }
                                                    } else {
                                                        et_gst_no.setError("please fill");
                                                        scrollToView(sv, et_gst_no);
                                                    }
                                                } else {
                                                    et_units_quantity.setError("fill");
                                                    scrollToView(sv, et_units_quantity);
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
                    Toast.makeText(BillingDetailActivity.this, "select client!", Toast.LENGTH_LONG).show();
                    scrollToView(sv, spin_order_no);
                }
            }
        });

        try {
            //checkWritingPermission();
            if (!hasPermissions(BillingDetailActivity.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(BillingDetailActivity.this, PERMISSIONS, PERMISSION_ALL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == READ_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Get the Uri of the selected file
                uri = data.getData();
                Log.d("*** file uri", "File Uri: " + uri);
                // Get the path
                path = FileUtils.getPath(this, uri);
                Log.d("*** file path", "File Path: " + path);
                // Get the file instance

                File file = new File(path);
                // Initiate the upload
                tx_filename.setText(file.getName());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void alertBox(String title, ArrayList<String> pre, int style) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(BillingDetailActivity.this, style);
        builder.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(BillingDetailActivity.this);
        View dialogLayout = inflater.inflate(R.layout.preview_list, null);
        builder.setView(dialogLayout);
        ListView lv = (ListView) dialogLayout.findViewById(R.id.prevw_lst);

        lv.setAdapter(new CustomAdp(BillingDetailActivity.this, pre));
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
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();
    }

    private void sendDataToServer() {
        try {
            pDialog = new ProgressDialog(BillingDetailActivity.this);
            pDialog.setMessage("Uploading...");
            pDialog.setCancelable(false);
            pDialog.show();
            MultipartBody.Part filePart = null;
            try {
                file = FileUtils.getFile(BillingDetailActivity.this, uri);
                ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
                filePart = MultipartBody.Part.createFormData("image", file.getName(),fileBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
            RequestBody r_uusername = RequestBody.create(MediaType.parse("text/plain"), uusername);
            RequestBody r_current_date = RequestBody.create(MediaType.parse("text/plain"), current_date);
            RequestBody r_order_no_s = RequestBody.create(MediaType.parse("text/plain"), order_no_s);
            RequestBody r_install_type_s = RequestBody.create(MediaType.parse("text/plain"), install_type_s);
            RequestBody r_cust_type_s = RequestBody.create(MediaType.parse("text/plain"), cust_type_s);
            RequestBody r_client_name_s = RequestBody.create(MediaType.parse("text/plain"), client_name_s);
            RequestBody r_cust_street_name_s = RequestBody.create(MediaType.parse("text/plain"), cust_street_name_s);
            RequestBody r_cust_city_s = RequestBody.create(MediaType.parse("text/plain"), cust_city_s);
            RequestBody r_cust_office_number_s = RequestBody.create(MediaType.parse("text/plain"), cust_office_number_s);
            RequestBody r_cust_district_s = RequestBody.create(MediaType.parse("text/plain"), cust_district_s);
            RequestBody r_cust_state_s = RequestBody.create(MediaType.parse("text/plain"), cust_state_s);
            RequestBody r_cust_pincode_s = RequestBody.create(MediaType.parse("text/plain"), cust_pincode_s);
            RequestBody r_cust_p_name_s = RequestBody.create(MediaType.parse("text/plain"), cust_p_name_s);
            RequestBody r_cust_p_number_s = RequestBody.create(MediaType.parse("text/plain"), cust_p_number_s);
            RequestBody r_cust_p_id_s = RequestBody.create(MediaType.parse("text/plain"), cust_p_id_s);
            RequestBody r_cust_s_name_s = RequestBody.create(MediaType.parse("text/plain"), cust_s_name_s);
            RequestBody r_cust_s_number_s = RequestBody.create(MediaType.parse("text/plain"), cust_s_number_s);
            RequestBody r_cust_s_id_s = RequestBody.create(MediaType.parse("text/plain"), cust_s_id_s);
            RequestBody r_units_quantity = RequestBody.create(MediaType.parse("text/plain"), units_quantity);
            RequestBody r_price_hardware_s = RequestBody.create(MediaType.parse("text/plain"), price_hardware_s);
            RequestBody r_price_month_recu_s = RequestBody.create(MediaType.parse("text/plain"), price_month_recu_s);
            RequestBody r_price_setup_s = RequestBody.create(MediaType.parse("text/plain"), price_setup_s);
            RequestBody r_price_instal_s = RequestBody.create(MediaType.parse("text/plain"), price_instal_s);
            RequestBody r_price_accessory_s = RequestBody.create(MediaType.parse("text/plain"), price_accessory_s);
            RequestBody r_price_other_s = RequestBody.create(MediaType.parse("text/plain"), price_other_s);
            RequestBody r_tax_hardware_s = RequestBody.create(MediaType.parse("text/plain"), tax_hardware_s);
            RequestBody r_tax_month_recu_s = RequestBody.create(MediaType.parse("text/plain"), tax_month_recu_s);
            RequestBody r_tax_setup_s = RequestBody.create(MediaType.parse("text/plain"), tax_setup_s);
            RequestBody r_tax_instal_s = RequestBody.create(MediaType.parse("text/plain"), tax_instal_s);
            RequestBody r_tax_accessory_s = RequestBody.create(MediaType.parse("text/plain"), tax_accessory_s);
            RequestBody r_tax_other_s = RequestBody.create(MediaType.parse("text/plain"), tax_other_s);
            RequestBody r_amc_charges_s = RequestBody.create(MediaType.parse("text/plain"), amc_charges_s);
            RequestBody r_amc_date_s = RequestBody.create(MediaType.parse("text/plain"), amc_date_s);
            RequestBody r_remarks_s = RequestBody.create(MediaType.parse("text/plain"), remarks_s);
            RequestBody r_gst_no_s = RequestBody.create(MediaType.parse("text/plain"), gst_no_s);
            RequestBody r_pan_no_s = RequestBody.create(MediaType.parse("text/plain"), pan_no_s);
            RequestBody r_price_without_alerts_s = RequestBody.create(MediaType.parse("text/plain"), price_without_alerts_s);
            RequestBody r_tax_without_alerts_s = RequestBody.create(MediaType.parse("text/plain"), tax_without_alerts_s);
            RequestBody r_price_hardware_pump_s = RequestBody.create(MediaType.parse("text/plain"), price_hardware_pump_s);
            RequestBody r_tax_hardware_pump_s = RequestBody.create(MediaType.parse("text/plain"), tax_hardware_pump_s);
            RequestBody r_sale_id = RequestBody.create(MediaType.parse("text/plain"), sale_id_s);

            ApiHolder log_att = ServiceConnection.getClient(version).create(ApiHolder.class);
            Call<UpdateDataResponse> call = log_att.billing_intimation(filePart,
                    r_uusername,
                    r_current_date,
                    r_order_no_s,
                    r_install_type_s,
                    r_cust_type_s,
                    r_client_name_s,
                    r_cust_street_name_s,
                    r_cust_city_s,
                    r_cust_office_number_s,
                    r_cust_district_s,
                    r_cust_state_s,
                    r_cust_pincode_s,
                    r_cust_p_name_s,
                    r_cust_p_number_s,
                    r_cust_p_id_s,
                    r_cust_s_name_s,
                    r_cust_s_number_s,
                    r_cust_s_id_s,
                    r_units_quantity,
                    r_price_hardware_s,
                    r_price_month_recu_s,
                    r_price_setup_s,
                    r_price_instal_s,
                    r_price_accessory_s,
                    r_price_other_s,
                    r_tax_hardware_s,
                    r_tax_month_recu_s,
                    r_tax_setup_s,
                    r_tax_instal_s,
                    r_tax_accessory_s,
                    r_tax_other_s,
                    r_amc_charges_s,
                    r_amc_date_s,
                    r_remarks_s,
                    r_gst_no_s,
                    r_pan_no_s,
                    r_price_without_alerts_s,
                    r_tax_without_alerts_s,
                    r_price_hardware_pump_s,
                    r_tax_hardware_pump_s, r_sale_id);
            Log.i("****call", String.valueOf(call));
            call.enqueue(new Callback<UpdateDataResponse>() {
                @Override
                public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                    UpdateDataResponse updateDataResponse = response.body();
                    Log.i("**respnse", " " + response.body());
                    if (updateDataResponse != null) {
                        Toast.makeText(BillingDetailActivity.this, updateDataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if (updateDataResponse.getType() == 1) {
                            new android.app.AlertDialog.Builder(BillingDetailActivity.this)
                                    .setTitle("Submitted")
                                    .setMessage("Billing intimation has been submitted.")
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {
                                                    clearalldata();
                                                }
                                            })
                                    .setIcon(R.drawable.ic_check_circle_black_24dp).show()
                                    .setCancelable(false);
                        }
                    } else {
                        assert updateDataResponse != null;
                        Log.v("Response", updateDataResponse.toString());
                    }
                    preview.setClickable(true);
                    pDialog.dismiss();
                }
                @Override
                public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                    t.printStackTrace();
                    preview.setClickable(true);
                    pDialog.dismiss();
                    Toast.makeText(BillingDetailActivity.this, "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            pDialog.dismiss();
            Toast.makeText(BillingDetailActivity.this, "Failed to update, try again!", Toast.LENGTH_LONG).show();
        }
    }
    public void addclients() {
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
              //  pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                t.printStackTrace();
              //  pDialog.dismiss();
                Toast.makeText(BillingDetailActivity.this, "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
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
            Intent inteer = new Intent(BillingDetailActivity.this, SalesMainActivity.class);
            startActivity(inteer);
            finish();
        } else if (id == R.id.menu_logout) {
            Intent inteer = new Intent(BillingDetailActivity.this, LoginActivity.class);
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
        Intent inteer = new Intent(BillingDetailActivity.this, SalesMainActivity.class);
        startActivity(inteer);
        finish();
        //super.onBackPressed();
    }

    private int getIndex(Spinner spinner, String myString) {

        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
            }
        }
        return index;
    }

    void clearalldata() {

        et_client_nm.setText("");
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
        et_price_hardware.setText("");
        et_tax_hardware.setText("");
        et_price_month_recu.setText("");
        et_tax_month_recu.setText("");
        et_price_setup.setText("");
        et_tax_setup.setText("");
        et_price_instal.setText("");
        et_tax_instal.setText("");
        et_price_accessory.setText("");
        et_tax_accessory.setText("");
        et_price_other.setText("");
        et_tax_other.setText("");
        et_amc_charges.setText("");
        et_remarks.setText("");
        et_units_quantity.setText("");
        et_tax_percent.setText("");
        et_gst_no.setText("");
    }

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

    @Override
    public void onProgressUpdate(int percentage) {
        try {
            progressBar.setProgress(percentage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
        progressBar.setProgress(100);
    }

    class CustomAdp extends BaseAdapter {
        LayoutInflater lay_infa;
        Context ctx;
        ArrayList<String> preview_valu;
        String arr[] = {"Username : ",
                "Order no. : ",
                "Installation type : ",
                "Client name : ",
                "Street name : ",
                "City : ",
                "GST Number : ",
                "PAN Number : ",
                "Office number : ",
                "District : ",
                "State : ",
                "Pincode : ",
                "Contact name : ",
                "Contact number : ",
                "Contact id : ",
                "(alternate)contact name : ",
                "(alternate)contact number : ",
                "(alternate)contact id : ",
                "Hardware with voice alerts price : ",
                "Hardware with voice alerts tax : ",
                "Hardware without voice alerts price : ",
                "Hardware without voice alerts tax : ",
                "Hardware pump price : ",
                "Hardware pump tax : ",
                "Monthly recurring price : ",
                "Monthly recurring tax : ",
                "Setup price : ",
                "Setup tax : ",
                "Installation price  : ",
                "Installation tax  : ",
                "Accessory price : ",
                "Accessory tax : ",
                "Other price : ",
                "Other tax : ",
                "Amc Charges : ",
                "Amc start date : ",
                "Remarks : "};

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

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
            } else {

                // permission wasn't granted
            }
        }
    }
    public void addorder_no() {
        pDialog = new ProgressDialog(BillingDetailActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        ApiHolder log_att = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<UpdateDataResponse> call = log_att.pending_bill(uusername);
        Log.i("****call", String.valueOf(call));
        call.enqueue(new Callback<UpdateDataResponse>() {
            @Override
            public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                UpdateDataResponse updateDataResponse = response.body();
                Log.i("**respnse", " " + response.body());
                if (updateDataResponse != null) {
                    if (updateDataResponse.getType() == 1) {
                        spin_order_no.setClickable(true);
                        preview.setClickable(true);
                        orderListDetails = updateDataResponse.getOrder_list();
                        for (int i = 0; i < orderListDetails.size(); i++) {
                            arr_order_no.add(orderListDetails.get(i).getOrder_no());
                        }
                        adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_items, arr_order_no);
                        adp.setDropDownViewResource(R.layout.spinner_items);
                        spin_order_no.setAdapter(adp);
                        try {
                            if (!order_no_s.equals("")) {
                                spin_order_no.setSelection(getIndex(spin_order_no, order_no_s));
                            }
                        } catch (Exception e) {
                        }
                    }
                    if (updateDataResponse.getType() == 0) {
                        arr_order_no.add("No pending bill");
                        adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_items, arr_order_no);
                        adp.setDropDownViewResource(R.layout.spinner_items);
                        spin_order_no.setAdapter(adp);
                        spin_order_no.setClickable(false);
                        preview.setClickable(false);
                    }
                } else {
                    try {
                        assert updateDataResponse != null;
                        Log.v("Response", updateDataResponse.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    pDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                t.printStackTrace();
                pDialog.dismiss();
                Toast.makeText(BillingDetailActivity.this, "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
            }
        });

    }
}
