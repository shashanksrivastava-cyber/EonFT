package in.eoninfotech.eontechnician.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import in.eoninfotech.eontechnician.HttpRestClient;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.ClientDataResponse;
import in.eoninfotech.eontechnician.Responses.ClientDetails;
import in.eoninfotech.eontechnician.Responses.ClientResponse;
import in.eoninfotech.eontechnician.Responses.MainResponse;
import in.eoninfotech.eontechnician.Responses.StockClientDataResponse;
import in.eoninfotech.eontechnician.helper.EONUtil;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.helper.StockDetail;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by root on 28/11/18.
 */

public class StockFragment extends Fragment {

    View v;
    EditText e_wrking_vts_qty, e_wrking_vts_srno, e_faulty_vts_qty, e_faulty_vts_srno, e_cable_7_meter, e_cable_2_meter, e_drum_sensor_qty, e_drum_sensor_ids, e_magnet_set, e_y_cable, e_remarks, e_power_cable, e_vts_remarks;
    String s_wrking_vts_qty, s_wrking_vts_srno, s_faulty_vts_qty, s_clientname, s_faulty_vts_srno, s_cable_7_meter, s_cable_2_meter, s_drum_sensor_qty, s_drum_sensor_ids, s_magnet_set, s_y_cable, s_remarks, s_clientid = "", s_datee, s_power_cable, s_vts_remarks;
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
    private Call<MainResponse> locCall;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_stock, container, false);
        sharedprefs = getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("usname", "");
        dist_id = sharedprefs.getString("s_distt", "");
        version = sharedprefs.getString("version", "");
        drs_one = v.findViewById(R.id.drs_lay_on);
        drs_two = v.findViewById(R.id.drs_lay_two);
        drs_three = v.findViewById(R.id.drs_lay_th);
        vts_one = v.findViewById(R.id.vts_lay);
        e_cable_2_meter = v.findViewById(R.id.cable_2_meter);
        e_cable_7_meter = v.findViewById(R.id.cable_7_meter);
        e_drum_sensor_ids = v.findViewById(R.id.drum_sensor_id);
        e_drum_sensor_qty = v.findViewById(R.id.drum_sensor_qty);
        e_faulty_vts_qty = v.findViewById(R.id.faulty_vts_qty);
        e_faulty_vts_srno = v.findViewById(R.id.faulty_vts_srno);
        e_magnet_set = v.findViewById(R.id.magnet_set);
        e_remarks = v.findViewById(R.id.remarks);
        e_vts_remarks = v.findViewById(R.id.vts_remarks);
        e_power_cable = v.findViewById(R.id.power_cable);
        e_wrking_vts_qty = v.findViewById(R.id.working_vts_qty);
        e_wrking_vts_srno = v.findViewById(R.id.working_vts_srno);
        e_y_cable = v.findViewById(R.id.y_cable);
        client = v.findViewById(R.id.m_region);
        datee = v.findViewById(R.id.datee);
        update_dataa = v.findViewById(R.id.update_data);
        progressBar = v.findViewById(R.id.progressBar);
        hashMap = EONUtil.gettingData(getActivity());

        ShowProgressBar(false);
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

                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                s_clientid = String.valueOf(clientList.get(i).getClient_Id());
                s_clientname = String.valueOf(clientList.get(i).getClient_Name());
                s_drs_status = String.valueOf(clientList.get(i).getDrs_status());
                Log.i("idd", "" + s_clientid + s_drs_status);

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

                try {
                    getClient_data();
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
               if (!e_wrking_vts_qty.getText().toString().equals("") && (e_wrking_vts_srno.getText().toString().equals(""))) {
                    e_wrking_vts_srno.setError("Fill Field");
                } else if (!e_faulty_vts_qty.getText().toString().equals("") && (e_faulty_vts_srno.getText().toString().equals(""))) {
                    e_faulty_vts_srno.setError("Fill Field");
                } else if(!e_wrking_vts_srno.getText().toString().equals("")&&(e_wrking_vts_qty.getText().toString().equals(""))){
                    e_wrking_vts_qty.setError("Fill Field");
                }else if(!e_faulty_vts_srno.getText().toString().equals("")&&(e_faulty_vts_qty.getText().toString().equals(""))){
                    e_faulty_vts_qty.setError("Fill Field");
                }else if (!e_wrking_vts_qty.getText().toString().equals("") && (!e_wrking_vts_srno.getText().toString().equals(""))) {
                   String string = String.valueOf(e_wrking_vts_srno.getText().toString());
                   try{
                   int qty = Integer.parseInt(String.valueOf(e_wrking_vts_qty.getText().toString()));
                   String[] parts = string.split(",");
                   int counts = Integer.parseInt(String.valueOf(parts.length));
                   if (qty == 0) {
                       updatedata();
                   } else if (counts < qty) {
                       e_wrking_vts_srno.setError("Serial no. contain value equal to Quantity");
                   } else if (counts > qty) {
                       e_wrking_vts_srno.setError("Serial no. contain value equal to Quantity");
                   } else if (!e_faulty_vts_qty.getText().toString().equals("") && (!e_faulty_vts_srno.getText().toString().equals(""))) {
                       String strings = String.valueOf(e_faulty_vts_srno.getText().toString());
                       int faultqty = Integer.parseInt(String.valueOf(e_faulty_vts_qty.getText().toString()));
                       String[] partss = strings.split(",");
                       int count = Integer.parseInt(String.valueOf(partss.length));
                       if (faultqty == 0) {
                           updatedata();
                       } else if (count < faultqty) {
                           e_faulty_vts_srno.setError("Serial no. contain value equal to Quantity");
                       } else if (count > faultqty) {
                           e_faulty_vts_srno.setError("Serial no. contain value equal to Quantity");
                       } else if (drs_two.getVisibility() == View.VISIBLE) {
                           if (!e_drum_sensor_qty.getText().toString().equals("") && (e_drum_sensor_ids.getText().toString().equals(""))) {
                               e_drum_sensor_ids.setError("Fill Field");
                           } else if (!e_drum_sensor_ids.getText().toString().equals("") && (e_drum_sensor_qty.getText().toString().equals(""))) {
                               e_drum_sensor_qty.setError("Fill Field");
                           } else if (!e_drum_sensor_qty.getText().toString().equals("") && (!e_drum_sensor_ids.getText().toString().equals(""))) {
                               String strin = String.valueOf(e_drum_sensor_ids.getText().toString());
                               int qtyy = Integer.parseInt(String.valueOf(e_drum_sensor_qty.getText().toString()));
                               String[] part = strin.split(",");
                               int countss = Integer.parseInt(String.valueOf(part.length));
                               if (qtyy == 0) {
                                   updatedata();
                               } else if (countss < qtyy) {
                                   e_drum_sensor_ids.setError("Serial no. contain value equal to Quantity");
                               } else if (countss > qtyy) {
                                   e_drum_sensor_ids.setError("Serial no. contain value equal to Quantity");
                               } else {
                                   updatedata();
                               }
                           } else if ((drs_two.getVisibility() == View.VISIBLE) && (e_drum_sensor_qty.getText().toString().equals(""))) {
                               updatedata();
                           }
                       } else {
                           updatedata();
                       }
               }else {
                        updatedata();
                    }
                }
                catch (Exception e) {
                }
               }else {
                    updatedata();
                }
            }
        });
        return v;
    }

    public void updatedata() {

        if (e_cable_7_meter.getText().toString().equals("")) {
            s_cable_7_meter = "0";
        } else {
            s_cable_7_meter = e_cable_7_meter.getText().toString();
        }
        if (e_drum_sensor_ids.getText().toString().equals("")) {
            s_drum_sensor_ids = "0";
        } else {
            s_drum_sensor_ids = e_drum_sensor_ids.getText().toString();
        }
        if (e_drum_sensor_qty.getText().toString().equals("")) {
            s_drum_sensor_qty = "0";
        } else {
            s_drum_sensor_qty = e_drum_sensor_qty.getText().toString();
        }
        if (e_faulty_vts_qty.getText().toString().equals("")) {
            s_faulty_vts_qty = "0";
        } else {
            s_faulty_vts_qty = e_faulty_vts_qty.getText().toString();
        }
        if (e_faulty_vts_srno.getText().toString().equals("")) {
            s_faulty_vts_srno = "0";
        } else {
            s_faulty_vts_srno = e_faulty_vts_srno.getText().toString();
        }
        if (e_magnet_set.getText().toString().equals("")) {
            s_magnet_set = "0";
        } else {
            s_magnet_set = e_magnet_set.getText().toString();
        }
        if (e_wrking_vts_qty.getText().toString().equals("")) {
            s_wrking_vts_qty = "0";
        } else {
            s_wrking_vts_qty = e_wrking_vts_qty.getText().toString();
        }
        if (e_wrking_vts_srno.getText().toString().equals("")) {
            s_wrking_vts_srno = "0";
        } else {
            s_wrking_vts_srno = e_wrking_vts_srno.getText().toString();
        }
        if (e_y_cable.getText().toString().equals("")) {
            s_y_cable = "0";
        } else {
            s_y_cable = e_y_cable.getText().toString();
        }
        if (vts_one.getVisibility() == View.GONE) {
            if (e_cable_2_meter.getText().toString().equals("")) {
                s_cable_2_meter = "0";
            } else {
                s_cable_2_meter = e_cable_2_meter.getText().toString();
            }
            s_remarks = e_remarks.getText().toString();
        } else {
            if (e_power_cable.getText().toString().equals("")) {
                s_cable_2_meter = "0";
            } else {
                s_cable_2_meter = e_power_cable.getText().toString();
            }
            s_remarks = e_vts_remarks.getText().toString();
        }
        if (s_clientid.equals("") || s_clientid.equals(null)) {
            Toast.makeText(getActivity(), "Please Select Client", Toast.LENGTH_LONG).show();
        } else {
            updateData();
        }
    }

    private void updateData() {

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Updating DATA...");
        pDialog.show();
        ApiHolder loc_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        locCall = loc_att.stockResponse(s_clientid, s_wrking_vts_qty, s_faulty_vts_qty, s_cable_7_meter, s_cable_2_meter, s_drum_sensor_qty,
                s_magnet_set, username, s_y_cable, s_remarks, s_wrking_vts_srno, s_faulty_vts_srno, s_drum_sensor_ids);
        locCall.enqueue(new Callback<MainResponse>() {
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                MainResponse workTypeResponse = response.body();
                Log.i("**work respnse", "" + response.body());
                Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.hide();
                int DELAY = 1000;
                ShowProgressBar(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ShowProgressBar(false);
                    }
                }, DELAY);
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }

    private void addclients() {
        ShowProgressBar(true);
        ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<ClientResponse> clientCall = client_att.reqeuestStockClientList();
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
                    ShowProgressBar(false);
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
                    TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void ShowProgressBar(boolean show) {
        try {
            if (show) {
                progressBar.setVisibility(View.VISIBLE);
                // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                progressBar.setVisibility(View.GONE);
                // getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getClient_data() {
        ShowProgressBar(true);
        ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<ClientDataResponse> clientCall = client_att.reqeuestClientDataResponse(username,s_clientid);
        clientCall.enqueue(new Callback<ClientDataResponse>() {
            public void onResponse(Call<ClientDataResponse> call, Response<ClientDataResponse> response) {
                ClientDataResponse workTypeResponse = response.body();
                if (response.body().getType().equals("1")) {
                Log.i("**workclientrespnse", " " + clientList);

                    e_cable_2_meter.setText(response.body().getData().getMtrs_2());
                    e_cable_7_meter.setText(response.body().getData().getMtrs_7());
                    e_drum_sensor_ids.setText(response.body().getData().getDrs_id());
                    e_drum_sensor_qty.setText(response.body().getData().getDrs());
                    e_faulty_vts_qty.setText(response.body().getData().getVts_falt());
                    e_faulty_vts_srno.setText(response.body().getData().getVts_f_id());
                    e_magnet_set.setText(response.body().getData().getMgt_set());
                    e_remarks.setText(response.body().getData().getRemarks());
                    e_wrking_vts_qty.setText(response.body().getData().getVts_wrk());
                    e_wrking_vts_srno.setText(response.body().getData().getVts_w_id());
                    e_y_cable.setText(response.body().getData().getY_cable());
                    e_remarks.setText(response.body().getData().getRemarks());
                    e_vts_remarks.setText(response.body().getData().getRemarks());
                    e_power_cable.setText(response.body().getData().getMtrs_2());
                    ShowProgressBar(false);
            }else {
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
                    e_remarks.setText("");
                    e_vts_remarks.setText("");
                    e_power_cable.setText("");
                    ShowProgressBar(false);
                }
            }

            @Override
            public void onFailure(Call<ClientDataResponse> call, Throwable t) {
                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();
                }
            }

            class UpdateData extends AsyncTask<String, Void, Void> {
                URL url;
                private String response = null;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog = new ProgressDialog(getActivity());
                    pDialog.setMessage("Updating Data...");
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
                        e_remarks.setText("");
                        e_vts_remarks.setText("");
                        e_power_cable.setText("");
                        showDialogg(status, 0);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                        Toast.makeText(getActivity(), "Unable To Update Data", Toast.LENGTH_LONG).show();
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
                            if (stock_dtl.get(0).getCable_2_meter().equals("0")) {
                                e_cable_2_meter.setText("");
                            } else {
                                e_power_cable.setText(stock_dtl.get(0).getCable_2_meter());
                            }
                            if (stock_dtl.get(0).getCable_7_meter().equals("0")) {
                                e_cable_7_meter.setText("");
                            } else {
                                e_cable_7_meter.setText(stock_dtl.get(0).getCable_7_meter());
                            }
                            if (stock_dtl.get(0).getDrum_sensor_qty().equals("0")) {
                                e_drum_sensor_qty.setText("");
                            } else {
                                e_drum_sensor_qty.setText(stock_dtl.get(0).getDrum_sensor_qty());
                            }
                            if (stock_dtl.get(0).getDrum_sensor_id().equals("0")) {
                                e_drum_sensor_ids.setText("");
                            } else {
                                e_drum_sensor_ids.setText(stock_dtl.get(0).getDrum_sensor_id());
                            }
                            if (stock_dtl.get(0).getFaulty_vts_qty().equals("0")) {
                                e_faulty_vts_qty.setText("");
                            } else {
                                e_faulty_vts_qty.setText(stock_dtl.get(0).getFaulty_vts_qty());
                            }
                            if (stock_dtl.get(0).getFaulty_vts_srno().equals("0")) {
                                e_faulty_vts_srno.setText("");
                            } else {
                                e_faulty_vts_srno.setText(stock_dtl.get(0).getFaulty_vts_srno());
                            }
                            if (stock_dtl.get(0).getMagnet_set().equals("0")) {
                                e_magnet_set.setText("");
                            } else {
                                e_magnet_set.setText(stock_dtl.get(0).getMagnet_set());
                            }
                            e_remarks.setText(stock_dtl.get(0).getRemarks());
                            if (stock_dtl.get(0).getWorking_vts_qty().equals("0")) {
                                e_wrking_vts_qty.setText("");
                            } else {
                                e_wrking_vts_qty.setText(stock_dtl.get(0).getWorking_vts_qty());
                            }
                            if (stock_dtl.get(0).getFaulty_vts_srno().equals("0")) {
                                e_wrking_vts_srno.setText("");
                            } else {
                                e_wrking_vts_srno.setText(stock_dtl.get(0).getWorking_vts_srno());
                            }
                            e_y_cable.setText(stock_dtl.get(0).getLocation());

                            ShowProgressBar(false);

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
                            e_power_cable.setText("");
                            e_vts_remarks.setText("");
                            ShowProgressBar(false);
                        }
                    } catch (Exception ae) {
                        ae.printStackTrace();
                    }
                }
            }
            void showDialogg(String msg, int labl) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
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
                          //  new StockDetailByDate().execute(s_clientid);
                            break;
                        default:
                            break;
                    }
                }
            };
        });
    }
}
