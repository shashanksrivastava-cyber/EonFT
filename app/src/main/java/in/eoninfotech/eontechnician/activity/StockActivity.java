package in.eoninfotech.eontechnician.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import in.eoninfotech.eontechnician.HttpRestClient;
import in.eoninfotech.eontechnician.MainActivity;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.ClientDetails;
import in.eoninfotech.eontechnician.helper.EONUtil;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.helper.StockDetail;
/***************************************************************************/
// Copyright EON Infotech Ltd., unpublished work, created 2016.          //
// This computer program includes Confidential, Proprietary information  //
// and is a trade secret of EON Infotech Ltd. All use, disclosure and/or //
// reproduction is prohibited unless authorised in writing by an         //
// authorised officer of EON Infotech Ltd. All rights reserved.          //

/**************************************************************************/

public class StockActivity extends AppCompatActivity {
    View v;
    EditText e_wrking_vts_qty, e_wrking_vts_srno, e_faulty_vts_qty, e_faulty_vts_srno, e_cable_7_meter, e_cable_2_meter, e_drum_sensor_qty, e_drum_sensor_ids, e_magnet_set, e_y_cable, e_remarks, e_power_cable, e_vts_remarks;
    String s_wrking_vts_qty, s_wrking_vts_srno, s_faulty_vts_qty, s_faulty_vts_srno, s_cable_7_meter, s_cable_2_meter, s_drum_sensor_qty, s_drum_sensor_ids, s_magnet_set, s_y_cable, s_remarks, s_clientid = "", s_datee, s_power_cable, s_vts_remarks;
    Button update_dataa;
    MySearchableSpinner client;
    TextView datee;
    String selected_todate, s_drs_status, version;
    int year, month, day;
    Calendar calen;
    ArrayList<StockDetail> stock_dtl = new ArrayList<StockDetail>();
    String username, dist_id;
    ProgressDialog pDialog;
    String status;
    LinearLayout drs_one, drs_two, drs_three, vts_one;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    ArrayList<ClientDetails> clientList = new ArrayList<>();
    ArrayList<String> clientDetail = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("usname", "");
        dist_id = sharedprefs.getString("s_distt", "");
        version = sharedprefs.getString("version", "");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(username.toUpperCase());

        drs_one = findViewById(R.id.drs_lay_on);
        drs_two = findViewById(R.id.drs_lay_two);
        drs_three = findViewById(R.id.drs_lay_th);
        vts_one = findViewById(R.id.vts_lay);
        e_cable_2_meter = findViewById(R.id.cable_2_meter);
        e_cable_7_meter = findViewById(R.id.cable_7_meter);
        e_drum_sensor_ids = findViewById(R.id.drum_sensor_id);
        e_drum_sensor_qty = findViewById(R.id.drum_sensor_qty);
        e_faulty_vts_qty = findViewById(R.id.faulty_vts_qty);
        e_faulty_vts_srno = findViewById(R.id.faulty_vts_srno);
        e_magnet_set = findViewById(R.id.magnet_set);
        e_remarks = findViewById(R.id.remarks);
        e_vts_remarks = findViewById(R.id.power_cable);
        e_power_cable = findViewById(R.id.vts_remarks);
        e_wrking_vts_qty = findViewById(R.id.working_vts_qty);
        e_wrking_vts_srno = findViewById(R.id.working_vts_srno);
        e_y_cable = findViewById(R.id.y_cable);
        client = findViewById(R.id.m_region);
        datee = findViewById(R.id.datee);
        update_dataa = findViewById(R.id.update_data);
        hashMap = EONUtil.gettingData(StockActivity.this);

        addclients();

        calen = Calendar.getInstance();
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        if (month + 1 < 10) {
            selected_todate = day + "/0" + month + "/" + year;
        } else {
            selected_todate = day + "/" + month + "/" + year;
        }
        datee.setText(selected_todate);
        client.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String st_name = String.valueOf(client.getSelectedItem());
                    String st_id = "";
                    for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                        if (entry.getValue().equals(st_name.trim())) {
                            st_id = entry.getKey();
                            Log.e("", "KEY VALUE :::" + entry.getKey());
                        }
                    }
                    String CurrentString = st_id;
                    String[] separated = CurrentString.split(":");
                    s_clientid = separated[0].trim();
                    s_drs_status = separated[1].trim();
                    if (s_drs_status.equals("0")) {
                        drs_one.setVisibility(View.GONE);
                        drs_two.setVisibility(View.GONE);
                        drs_three.setVisibility(View.GONE);
                        vts_one.setVisibility(View.VISIBLE);
                    } else {
                        vts_one.setVisibility(View.GONE);
                        drs_one.setVisibility(View.VISIBLE);
                        drs_two.setVisibility(View.VISIBLE);
                        drs_three.setVisibility(View.VISIBLE);
                    }
                } catch (Exception ae) {
                    ae.printStackTrace();
                }
                try {
                    new StockDetailByDate().execute(s_clientid);
                } catch (Exception ae) {
                    ae.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        update_dataa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String st_name = String.valueOf(client.getSelectedItem());
                    String st_id = "";
                    for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                        if (entry.getValue().equals(st_name.trim())) {
                            st_id = entry.getKey();
                            Log.e("", "KEY VALUE :::" + entry.getKey());
                        }
                    }
                    String CurrentString = st_id;
                    String[] separated = CurrentString.split(":");
                    s_clientid = separated[0].trim();
                    s_drs_status = separated[1].trim();
                    if (e_wrking_vts_qty.getText().toString().equals("") || e_wrking_vts_qty.getText().toString().equals(null)) {
                        e_wrking_vts_qty.setError("fill field");
                    } else {
                        s_cable_7_meter = e_cable_7_meter.getText().toString();
                        s_drum_sensor_ids = e_drum_sensor_ids.getText().toString();
                        s_drum_sensor_qty = e_drum_sensor_qty.getText().toString();
                        s_faulty_vts_qty = e_faulty_vts_qty.getText().toString();
                        s_faulty_vts_srno = e_faulty_vts_srno.getText().toString();
                        s_magnet_set = e_magnet_set.getText().toString();
                        s_wrking_vts_qty = e_wrking_vts_qty.getText().toString();
                        s_wrking_vts_srno = e_wrking_vts_srno.getText().toString();
                        s_y_cable = e_y_cable.getText().toString();
                        s_datee = datee.getText().toString();
                        if (vts_one.getVisibility() == View.GONE) {
                            s_cable_2_meter = e_cable_2_meter.getText().toString();
                            s_remarks = e_remarks.getText().toString();
                        } else {
                            s_cable_2_meter = e_power_cable.getText().toString();
                            s_remarks = e_vts_remarks.getText().toString();
                        }
                        if (s_clientid.equals("") || s_clientid.equals(null)) {
                            Toast.makeText(getApplicationContext(), "Please select client", Toast.LENGTH_LONG).show();
                        } else {
                            new UpdateData().execute(s_wrking_vts_qty, s_wrking_vts_srno, s_faulty_vts_qty);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
            Intent inteer = new Intent(StockActivity.this, MainActivity.class);
            startActivity(inteer);
            finish();
        } else if (id == R.id.menu_logout) {
            Intent inteer = new Intent(StockActivity.this, LoginActivity.class);
            inteer.putExtra("username", "us");
            editor.putString("pass", "");
            editor.commit();
            startActivity(inteer);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addclients() {

        List<String> name = new ArrayList<String>();
        Collection c = hashMap.values();
        Iterator itr = c.iterator();
        // iterate through HashMap values iterator
        name.add("Select");
        while (itr.hasNext()) {
            String temp = itr.next().toString();
            Log.i("TEMP", "" + temp);
            name.add(temp);
        }
        Collections.sort(name);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(StockActivity.this,
                R.layout.spinner_items, name);
        dataAdapter.setDropDownViewResource(R.layout.spinner_items);
        client.setAdapter(dataAdapter);
    }
    class UpdateData extends AsyncTask<String, Void, Void> {
        URL url;
        private String response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StockActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            Log.e("Response", " Load data ");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("vts_wrk", strings[0]));
            nameValuePairs.add(new BasicNameValuePair("vts_w_id", strings[1]));
            nameValuePairs.add(new BasicNameValuePair("vts_falt", s_faulty_vts_qty));
            nameValuePairs.add(new BasicNameValuePair("vts_f_id", s_faulty_vts_srno));
            nameValuePairs.add(new BasicNameValuePair("mtrs_7", s_cable_7_meter));
            nameValuePairs.add(new BasicNameValuePair("mtrs_2", s_cable_2_meter));
            nameValuePairs.add(new BasicNameValuePair("drs", s_drum_sensor_qty));
            nameValuePairs.add(new BasicNameValuePair("drs_id", s_drum_sensor_ids));
            nameValuePairs.add(new BasicNameValuePair("mgt_set", s_magnet_set));
            nameValuePairs.add(new BasicNameValuePair("y_cable", s_y_cable));
            nameValuePairs.add(new BasicNameValuePair("remarks", s_remarks));
            nameValuePairs.add(new BasicNameValuePair("client_id", s_clientid));
            nameValuePairs.add(new BasicNameValuePair("frm_date", s_datee));
            nameValuePairs.add(new BasicNameValuePair("user_name", username));
            Log.e("**", "RESPONSE" + nameValuePairs);
            try {
                response = HttpRestClient.connectPost(K.Url.set_stock_detail, nameValuePairs);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("", "RESPONSE" + response);
            /* response = HttpRestClient.connectGet(url);*/
            try {
                DocumentBuilderFactory builder = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = builder.newDocumentBuilder();
                Reader reader = new StringReader(response);
                InputSource source = new InputSource(reader);
                Document doc = documentBuilder.parse(source);
                doc.getDocumentElement().normalize();
                NodeList nl = doc.getElementsByTagName("master");
                for (int i = 0; i < nl.getLength(); i++) {
                    Element e = (Element) nl.item(i);
                    try {
                        status = K.getNode("status", e);
                    } catch (NullPointerException ne) {
                        status = "";
                    }
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ParserConfigurationException pe) {
                pe.printStackTrace();
            } catch (Exception ae) {
                ae.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.cancel();
            try {
                e_cable_2_meter.setText("");
                e_cable_7_meter.setText("");
                e_drum_sensor_ids.setText("");
                e_drum_sensor_qty.setText("");
                e_faulty_vts_qty.setText("");
                e_faulty_vts_srno.setText("");
                e_magnet_set.setText("");
                e_remarks.setText("");
                e_wrking_vts_qty.setText("");
                e_wrking_vts_srno.setText("");
                e_y_cable.setText("");
                showDialogg(status, 0);
            } catch (NullPointerException ne) {
                ne.printStackTrace();
           Toast.makeText(getApplicationContext(),"Unable To Update Data", Toast.LENGTH_LONG).show();

            }
        }
    }

    class StockDetailByDate extends AsyncTask<String, Void, Void> {
        URL url;

        @Override
        protected Void doInBackground(String... strings) {
            try {
                stock_dtl.clear();
                String data = username + "&" + strings[0] + "&" + K.Url.urlkey;
                byte[] encodedd = Base64.encode(data.getBytes("UTF-8"), Base64.DEFAULT);
                String str1 = new String(encodedd, "UTF-8");
                Log.i("***urlen****", data);
                url = new URL(K.Url.get_stock_detail + str1);
                DocumentBuilderFactory builder = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = builder.newDocumentBuilder();
                Document doc = documentBuilder.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                NodeList nl = doc.getElementsByTagName("master");
                Element e = (Element) nl.item(0);
                nl = e.getElementsByTagName("data");
                Log.i("****Svehsize**", new StringBuilder().append(nl.getLength()).toString());
                for (int i = 0; i < nl.getLength(); i++) {
                    StockDetail stock_detail = new StockDetail();
                    e = (Element) nl.item(i);
                    stock_detail.setWorking_vts_qty(K.getNode("vts_wrk", e));
                    try {
                        stock_detail.setWorking_vts_srno(K.getNode("vts_w_id", e));
                    } catch (NullPointerException ne) {
                    }
                    try {
                        stock_detail.setFaulty_vts_qty(K.getNode("vts_falt", e));
                    } catch (NullPointerException ne) {
                    }
                    try {
                        stock_detail.setFaulty_vts_srno(K.getNode("vts_f_id", e));
                    } catch (NullPointerException ne) {
                    }
                    try {
                        stock_detail.setCable_7_meter(K.getNode("mtrs_7", e));
                    } catch (NullPointerException ne) {
                    }
                    try {
                        stock_detail.setCable_2_meter(K.getNode("mtrs_2", e));
                    } catch (NullPointerException ne) {
                    }
                    try {
                        stock_detail.setDrum_sensor_qty(K.getNode("drs", e));
                    } catch (NullPointerException ne) {
                    }
                    try {
                        stock_detail.setDrum_sensor_id(K.getNode("drs_id", e));
                    } catch (NullPointerException ne) {
                    }
                    try {
                        stock_detail.setMagnet_set(K.getNode("mgt_set", e));
                    } catch (NullPointerException ne) {
                    }
                    try {
                        stock_detail.setLocation(K.getNode("y_cable", e));
                    } catch (NullPointerException ne) {
                    }
                    try {
                        stock_detail.setRemarks(K.getNode("remarks", e));
                    } catch (NullPointerException ne) {
                    }
                    Log.i("****stock_detail**", stock_detail.toString());
                    stock_dtl.add(stock_detail);
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ParserConfigurationException pe) {
                pe.printStackTrace();
            } catch (Exception ae) {
                ae.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                Log.i("***vehsize***", String.valueOf(stock_dtl.size()));
                if (stock_dtl.size() >= 1) {
                    e_cable_2_meter.setText(stock_dtl.get(0).getCable_2_meter());
                    e_cable_7_meter.setText(stock_dtl.get(0).getCable_7_meter());
                    e_drum_sensor_ids.setText(stock_dtl.get(0).getDrum_sensor_id());
                    e_drum_sensor_qty.setText(stock_dtl.get(0).getDrum_sensor_qty());
                    e_faulty_vts_qty.setText(stock_dtl.get(0).getFaulty_vts_qty());
                    e_faulty_vts_srno.setText(stock_dtl.get(0).getFaulty_vts_srno());
                    e_magnet_set.setText(stock_dtl.get(0).getMagnet_set());
                    e_remarks.setText(stock_dtl.get(0).getRemarks());
                    e_wrking_vts_qty.setText(stock_dtl.get(0).getWorking_vts_qty());
                    e_wrking_vts_srno.setText(stock_dtl.get(0).getWorking_vts_srno());
                    e_y_cable.setText(stock_dtl.get(0).getLocation());
                } else {
                    e_cable_2_meter.setText("");
                    e_cable_7_meter.setText("");
                    e_drum_sensor_ids.setText("");
                    e_drum_sensor_qty.setText("");
                    e_faulty_vts_qty.setText("");
                    e_faulty_vts_srno.setText("");
                    e_magnet_set.setText("");
                    e_remarks.setText("");
                    e_wrking_vts_qty.setText("");
                    e_wrking_vts_srno.setText("");
                    e_y_cable.setText("");
                }
            } catch (Exception ae) {
                ae.printStackTrace();
            }
        }
    }
    void showDialogg(String msg, int labl) {
        AlertDialog alertDialog = new AlertDialog.Builder(StockActivity.this).create();
        alertDialog.setTitle("STATUS");
        alertDialog.setMessage(msg);
        if (labl == 0) {
            alertDialog.setButton("OK", Message.obtain(handler, labl));
        }
        alertDialog.show();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String st_name = String.valueOf(client.getSelectedItem());
                    String st_id = "";
                    for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                        if (entry.getValue().equals(st_name.trim())) {
                            st_id = entry.getKey();
                            Log.e("", "KEY VALUE :::" + entry.getKey());
                        }
                    }
                    String CurrentString = st_id;
                    String[] separated = CurrentString.split(":");
                    s_clientid = separated[0].trim();
                    s_drs_status = separated[1].trim();
                    new StockDetailByDate().execute(s_clientid);
                    break;
                default:
                    break;
            }
        }
    };
}