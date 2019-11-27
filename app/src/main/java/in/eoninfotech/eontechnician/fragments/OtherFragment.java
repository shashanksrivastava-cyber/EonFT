package in.eoninfotech.eontechnician.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import in.eoninfotech.eontechnician.HttpRestClient;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.helper.ClientList;
import in.eoninfotech.eontechnician.helper.EONUtil;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.salesteam.SalesInstallationActivity;
import in.eoninfotech.eontechnician.view.MyEditText;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtherFragment extends Fragment {

    View v;
    String disttid;
    String uusername, version;
    CheckConnection chk;
    MyEditText e_device_id, e_remarks;
    Button update_dataa;
    MySearchableSpinner client;
    HashMap<String, String> hashMap = new HashMap<String, String>();    //hashmap used to prevent duplicate values for clientlist.
    String s_device_id, s_changed_value_ids = "", s_remarks, s_clientid, status, s_tel_supprt;
    ArrayList<ClientList> list_change_values = new ArrayList<ClientList>();
    ArrayList<String> value_name = new ArrayList<String>();
    ListView lv;
    CheckBox chk_tel_supprt;
    ArrayAdapter<String> adapter;
    ArrayList<String> pos = new ArrayList<String>();
    ProgressDialog proDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_others, container, false);
        disttid = getArguments().getString("disttid");
        uusername = getArguments().getString("usernme");
        version = getArguments().getString("version");
        e_device_id = v.findViewById(R.id.other_device_id);
        e_remarks = v.findViewById(R.id.other_remarks);
        chk = new CheckConnection(v.getContext());
        client = v.findViewById(R.id.others_clients);
        chk_tel_supprt = v.findViewById(R.id.check_tel_supprt);
        update_dataa = v.findViewById(R.id.other_update);
        lv = v.findViewById(R.id.other_list);
        hashMap = EONUtil.gettingData(getContext());
        addclients();
        if (chk.isConnected()) {
            new GetValueList().execute(uusername);
        } else {
            chk.showConnectionErrorDialog();
        }
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        update_dataa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SparseBooleanArray checked = lv.getCheckedItemPositions();
                // ArrayList<String> selectedItems = new ArrayList<String>();
                s_changed_value_ids = "";
                Log.i("***values", String.valueOf(checked) + "count" + lv.getCheckedItemCount());
                for (int i = 0; i < checked.size(); i++) {
                    int key = checked.keyAt(i);
                    s_changed_value_ids = s_changed_value_ids + (list_change_values.get(key).getClientid()) + ":";
                }
                Log.i("***values", s_changed_value_ids);
                s_device_id = e_device_id.getText().toString();
                s_remarks = e_remarks.getText().toString();
                if (s_device_id.equals("") || s_device_id.equals(null)) {
                    e_device_id.setError("fill field");
                } else if (s_changed_value_ids.equals("")) {
                    Toast.makeText(getActivity(), "select changed values", Toast.LENGTH_SHORT).show();
                } else {
                    sendDataToServer();
                   // new UpdateInstall().execute("abc");
                }
            }
        });
        return v;
    }

    public void addclients() {
        List<String> name = new ArrayList<String>();
        Collection c = hashMap.values();
        Iterator itr = c.iterator();
        // iterate through HashMap values iterator
        name.add(" SELECT");
        while (itr.hasNext()) {
            String temp = itr.next().toString();
            Log.i("TEMP", "" + temp);
            name.add(temp);
        }
        Collections.sort(name);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, name);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        client.setAdapter(dataAdapter);
    }

    class GetValueList extends AsyncTask<String, Void, Void> {
        ProgressDialog pDialog;
        URL url;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            list_change_values.clear();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Updating list...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                String data = K.Url.urlkey;
                byte[] encodedd = Base64.encode(data.getBytes("UTF-8"), Base64.DEFAULT);
                String str1 = new String(encodedd, "UTF-8");
                Log.i("***urlen****", K.Url.get_check_list + str1);
                url = new URL(K.Url.get_check_list + str1);
                DocumentBuilderFactory builder = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = builder.newDocumentBuilder();
                Document doc = documentBuilder.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                NodeList nl = doc.getElementsByTagName("master");
                Element ea = (Element) nl.item(0);
                nl = ea.getElementsByTagName("item");
                for (int i = 0; i < nl.getLength(); i++) {
                    ea = (Element) nl.item(i);
                    ClientList s = new ClientList();
                    s.setClientid(K.getNode("id", ea));
                    s.setClientname(K.getNode("descp", ea));
                    list_change_values.add(s);
                }
                Log.i("***stop size***", String.valueOf(list_change_values.size()));
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
            if (list_change_values.size() > 0) {
                for (int i = 0; i < list_change_values.size(); i++) {
                    value_name.add(list_change_values.get(i).getClientname());
                }
                if (list_change_values.size() > 5) {
                    lv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * list_change_values.size() + 1));
                } else {
                    lv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * list_change_values.size()));
                }
                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, value_name);
                lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                lv.setAdapter(adapter);
            } else {
                //Toast.makeText(getActivity(),"try again please",Toast.LENGTH_SHORT).show();
            }
        }
    }


    /*class UpdateInstall extends AsyncTask<String, Void, Void> {
        ProgressDialog pDialog;
        URL url;
        private String response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Log.e("Response", " Load data ");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("tech_name", uusername));
                nameValuePairs.add(new BasicNameValuePair("vts_old", s_device_id));
                nameValuePairs.add(new BasicNameValuePair("change_value", s_changed_value_ids));
                nameValuePairs.add(new BasicNameValuePair("remarks", s_remarks));
                nameValuePairs.add(new BasicNameValuePair("client_id", s_clientid));
                Log.e("**", "RESPONSE" + nameValuePairs);
                try {
                    response = HttpRestClient.connectPost(K.Url.set_other_detail, nameValuePairs);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("", "RESPONSE" + response);
            *//* response = HttpRestClient.connectGet(url);*//*
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
                showDialogg(status, 0);
            } catch (NullPointerException ne) {
                ne.printStackTrace();
                TSnackbar snackbar = TSnackbar.make(v, "Unable To Update Data", TSnackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(Color.BLACK);
                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();
            }
        }
    }*/

    void showDialogg(String msg, int labl) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("DATA UPDATED");
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
                    e_device_id.setText("");
                    e_remarks.setText("");
                    break;
                default:
                    break;
            }
        }
    };

    private void sendDataToServer() {
        if (chk_tel_supprt.isChecked()) {
            s_tel_supprt = "yes";
        } else {
            s_tel_supprt = "no";
        }
        proDialog = new ProgressDialog(getActivity());
        proDialog.setMessage("Loading...");
        proDialog.show();
        ApiHolder log_att = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<UpdateDataResponse> call = log_att.device_other(uusername,
                s_device_id,
                s_changed_value_ids,
                s_remarks,
                s_clientid,
                s_tel_supprt);
        Log.i("****call", String.valueOf(call));
        call.enqueue(new Callback<UpdateDataResponse>() {
            @Override
            public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                UpdateDataResponse updateDataResponse = response.body();
                Log.i("**respnse", " " + response.body());
                if (updateDataResponse != null) {
                    Toast.makeText(getActivity(), updateDataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    if (updateDataResponse.getType() == 1) {
                        //    alertBox("Submitted", "New Requirement has been submitted. Fill Billing Intimation for customer.", R.style.MyAlertDialogStyleConnected, 1);
                        showDialogg(status, 0);
                    }
                } else {
                    assert updateDataResponse != null;
                    Log.v("Response", updateDataResponse.toString());
                }
                proDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                t.printStackTrace();
                proDialog.dismiss();
                TSnackbar snackbar = TSnackbar.make(v, "Unable To Update Data", TSnackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(Color.BLACK);
                TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();
            }
        });
    }
}