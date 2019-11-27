package in.eoninfotech.eontechnician.salesteam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.OrderListDetail;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.Responses.ViewEntryResponse;
import in.eoninfotech.eontechnician.activity.LoginActivity;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewBillActivity extends AppCompatActivity { // activity bcz user will send order number as intentStringExtra

    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String usrname, uusername, s_order_no, version;
    TextView no_record;
    ListView lv;
    MySearchableSpinner spin_order_no;
    ProgressDialog pDialog;
    ArrayList<ViewEntryResponse> sales_entries = new ArrayList<>();
    ArrayList<String> arr_order_no = new ArrayList<>();
    ArrayList<OrderListDetail> orderListDetails = new ArrayList<>();
    ArrayAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill);
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
        version = sharedprefs.getString("version", "");
        //btn_srch = (Button) findViewById(R.id.btn_srch);
        lv = (ListView) findViewById(R.id.list_new_entry);
        no_record = (TextView) findViewById(R.id.text_no_record);
        spin_order_no = (MySearchableSpinner) findViewById(R.id.e_order_no);
        try {
            s_order_no = getIntent().getStringExtra("order");
        } catch (Exception e) {

        }
        addorder_no();

        spin_order_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getBillDetail(spin_order_no.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void getBillDetail(String order_no) {
        pDialog = new ProgressDialog(ViewBillActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        ApiHolder log_att = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<UpdateDataResponse> call = log_att.view_bill(order_no.trim());
        Log.i("****call", String.valueOf(call));
        call.enqueue(new Callback<UpdateDataResponse>() {
            @Override
            public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                UpdateDataResponse updateDataResponse = response.body();
                Log.i("**respnse", " " + response.body());
                if (updateDataResponse != null) {
                    if (updateDataResponse.getType() == 1) {
                        try {

                            sales_entries = updateDataResponse.getView_bill();
                            lv.setVisibility(View.VISIBLE);
                            no_record.setVisibility(View.GONE);
                            lv.setAdapter(new CustomAdp(ViewBillActivity.this, sales_entries));

                        } catch (Exception e) {
                            lv.setVisibility(View.GONE);
                            no_record.setVisibility(View.VISIBLE);
                        }
                    }else{
                        lv.setVisibility(View.GONE);
                        no_record.setVisibility(View.VISIBLE);
                    }
                } else {
                    try {
                        assert updateDataResponse != null;
                        Log.v("Response", updateDataResponse.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                t.printStackTrace();
                lv.setVisibility(View.GONE);
                no_record.setVisibility(View.VISIBLE);
                pDialog.dismiss();
                Toast.makeText(ViewBillActivity.this, "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addorder_no() {
        pDialog = new ProgressDialog(ViewBillActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        ApiHolder log_att = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<UpdateDataResponse> call = log_att.order_list(uusername);
        Log.i("****call", String.valueOf(call));
        call.enqueue(new Callback<UpdateDataResponse>() {
            @Override
            public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                UpdateDataResponse updateDataResponse = response.body();
                Log.i("**respnse", " " + response.body());
                if (updateDataResponse != null) {
                    if (updateDataResponse.getType() == 1) {
                        orderListDetails = updateDataResponse.getOrder_list();
                        for (int i = 0; i < orderListDetails.size(); i++) {
                            arr_order_no.add(orderListDetails.get(i).getOrder_no());
                        }
                        adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_items, arr_order_no);
                        adp.setDropDownViewResource(R.layout.spinner_items);
                        spin_order_no.setAdapter(adp);
                        try {
                            if (!s_order_no.equals("")) {
                                spin_order_no.setSelection(getIndex(spin_order_no, s_order_no));
                            }
                        } catch (Exception e) {
                        }

                    }
                }   else {
                    try {
                        assert updateDataResponse != null;
                        Log.v("Response", updateDataResponse.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                t.printStackTrace();
                pDialog.dismiss();
                Toast.makeText(ViewBillActivity.this, "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
            }
        });

    }

    class CustomAdp extends BaseAdapter {
        LayoutInflater lay_infa;
        Context ctx;
        ArrayList<ViewEntryResponse> entries;

        CustomAdp(Context c, ArrayList<ViewEntryResponse> preview_val) {
            ctx = c;
            entries = preview_val;
            lay_infa = LayoutInflater.from(ctx);
        }

        @Override
        public int getCount() {
            return entries.size();
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
                v = lay_infa.inflate(R.layout.custom_view_bill_item, null);
            }
            TextView client_nm, address, /*order_no_n_type,*/ person_detail, remarks,
                    price_hardware,
                    tax_hardware,
                    price_hardware_pump,
                    tax_hardware_pump,
                    price_hardware_without,
                    tax_hardware_without,
                    price_month_recu,
                    tax_month_recu,
                    price_setup,
                    tax_setup,
                    price_instal,
                    tax_instal,
                    price_accessory,
                    tax_accessory,
                    price_other,
                    tax_other,
                    amc_charges;
            client_nm = (TextView) v.findViewById(R.id.b_client_name);
            address = (TextView) v.findViewById(R.id.b_address);
            //  order_no_n_type = (TextView) v.findViewById(R.id.b_order_no_type);
            person_detail = (TextView) v.findViewById(R.id.b_contact_phn);
            remarks = (TextView) v.findViewById(R.id.b_remarks);
            price_hardware = (TextView) v.findViewById(R.id.p_hardware_with);
            tax_hardware = (TextView) v.findViewById(R.id.t_hardware_with);
            price_hardware_pump = (TextView) v.findViewById(R.id.p_hardware_pump);
            tax_hardware_pump = (TextView) v.findViewById(R.id.t_hardware_pump);
            price_hardware_without = (TextView) v.findViewById(R.id.p_hardware_without);
            tax_hardware_without = (TextView) v.findViewById(R.id.t_hardware_without);
            price_month_recu = (TextView) v.findViewById(R.id.p_recurring);
            tax_month_recu = (TextView) v.findViewById(R.id.t_recurring);
            price_setup = (TextView) v.findViewById(R.id.p_setup);
            tax_setup = (TextView) v.findViewById(R.id.t_setup);
            price_instal = (TextView) v.findViewById(R.id.p_installation);
            tax_instal = (TextView) v.findViewById(R.id.t_installation);
            price_accessory = (TextView) v.findViewById(R.id.p_accessories);
            tax_accessory = (TextView) v.findViewById(R.id.t_accessories);
            price_other = (TextView) v.findViewById(R.id.p_other);
            tax_other = (TextView) v.findViewById(R.id.t_other);
            amc_charges = (TextView) v.findViewById(R.id.p_amc);

            client_nm.setText(entries.get(position).getCust_name());
            address.setText(entries.get(position).getCust_street_name() + ", " + entries.get(position).getCust_city() + ", " + entries.get(position).getCust_district() + ", " + entries.get(position).getCust_state() + ", " + entries.get(position).getCust_pincode() + "\n M: " + entries.get(position).getCust_office_number());
            //  order_no_n_type.setText("Order no.- "+entries.get(position).getOrder_no() + ", Type - " +entries.get(position).getInstall_type());
            person_detail.setText(entries.get(position).getCust_p_name() + " M: " + entries.get(position).getCust_p_number());
            try {
                remarks.setText("Remarks : " + entries.get(position).getRemarks());
            } catch (Exception e) {
                remarks.setText("--");
            }
            try {
                price_hardware.setText(entries.get(position).getHardware_price());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                tax_hardware.setText(entries.get(position).getHardware_tax());
            } finally {

            }
            price_month_recu.setText(entries.get(position).getMonthly_recurring_price());
            tax_month_recu.setText(entries.get(position).getMonthly_recurring_tax());
            price_setup.setText(entries.get(position).getSetup_charges());
            tax_setup.setText(entries.get(position).getSetup_charges_tax());
            price_instal.setText(entries.get(position).getInstallation_price());
            tax_instal.setText(entries.get(position).getInstallation_tax());
            price_accessory.setText(entries.get(position).getAccessories_price());
            tax_accessory.setText(entries.get(position).getAccessories_tax());
            price_other.setText(entries.get(position).getOther_price());
            tax_other.setText(entries.get(position).getOther_tax());
            amc_charges.setText(entries.get(position).getAmc_charges());
            try {
                price_hardware_pump.setText(entries.get(position).getHardware_pump_price());
                tax_hardware_pump.setText(entries.get(position).getHardware_pump_tax());
                price_hardware_without.setText(entries.get(position).getHardware_without_voice_price());
                tax_hardware_without.setText(entries.get(position).getHardware_without_voice_tax());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return v;
        }
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
            Intent inteer = new Intent(ViewBillActivity.this, SalesMainActivity.class);
            inteer.putExtra("view", "view");
            startActivity(inteer);
            finish();
        } else if (id == R.id.menu_logout) {
            Intent inteer = new Intent(ViewBillActivity.this, LoginActivity.class);
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
        Intent inteer = new Intent(ViewBillActivity.this, SalesMainActivity.class);
        inteer.putExtra("view", "view");
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


}
