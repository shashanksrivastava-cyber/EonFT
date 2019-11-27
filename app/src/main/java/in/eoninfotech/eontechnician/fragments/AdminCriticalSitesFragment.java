package in.eoninfotech.eontechnician.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.helper.ClientList;
import in.eoninfotech.eontechnician.helper.CriticalSitesData;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 13/10/17.
 */
public class AdminCriticalSitesFragment extends Fragment {
    ListView lv;
    View v;
    String disttid;
    String username, version;
    ArrayList<CriticalSitesData> site_data = new ArrayList<>();
    MySearchableSpinner technicians;
    RelativeLayout relay;
    // ProgressDialog pDialog;
    String status, tech_name;
    ArrayList<ClientList> technicianlist = new ArrayList<ClientList>();
    ArrayAdapter adp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_critical_sites, container, false);
        lv = (ListView) v.findViewById(R.id.view_listview);
        relay =(RelativeLayout) v.findViewById(R.id.critical_relay);
        technicians =(MySearchableSpinner) v.findViewById(R.id.spin_admin_critical_sites);
        disttid = getArguments().getString("disttid");
        username = getArguments().getString("usernme");
        version = getArguments().getString("version");

        new UpdateTechnicianList().execute("def");
        relay.setVisibility(View.VISIBLE);
        technicians.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    tech_name = technicianlist.get(i).getClientname();
                    Log.i("***tech_name",tech_name);
                    getCriticaldata(tech_name.trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return v;
    }

    void getCriticaldata(String user_name) {
        ApiHolder get_list = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<UpdateDataResponse> call = get_list.critical_sites(user_name.toLowerCase(), K.Url.urlkey);
        call.enqueue(new Callback<UpdateDataResponse>() {
            @Override
            public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                site_data = response.body().getSite_data();
                for (int i = 0; i < site_data.size(); i++) {
                    Log.i("****", site_data.get(i).getCust_name());
                }
                try {
                    lv.setAdapter(new DetailAdapter(v.getContext(), site_data));
                }catch (Exception ae)
                {
                    ae.printStackTrace();
                }
                    //  pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                t.printStackTrace();
                //pDialog.dismiss();
                TSnackbar snackbar = TSnackbar.make(v, "Try Again Connection Timeout", TSnackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(Color.RED);
                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();
            }
        });

    }

    class DetailAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater layinfa;
        int j;
        ArrayList<CriticalSitesData> planDetails = new ArrayList<>();

        DetailAdapter(Context c, ArrayList<CriticalSitesData> planDetails) {
            ctx = c;
            this.planDetails = planDetails;
            layinfa = LayoutInflater.from(ctx);

        }

        @Override
        public int getCount() {
            //  Log.i("*******", new StringBuilder().append(list_vehidetai.size()).toString());
            return site_data.size();
        }

        @Override
        public Object getItem(int i) {
            return site_data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View vv = view;
            j = i;
            if (vv == null) {
                vv = layinfa.inflate(R.layout.custom_sites_item_view, null);
            }
            TextView locatn, cust_name, faulty_devices, faulty_drs;
            locatn = (TextView) vv.findViewById(R.id.site_loc);
            cust_name = (TextView) vv.findViewById(R.id.site_cust_name);
            faulty_devices = (TextView) vv.findViewById(R.id.faulty_devices);
            faulty_drs = (TextView) vv.findViewById(R.id.faulty_drs);


            locatn.setText(site_data.get(i).getLocation());
            cust_name.setText(site_data.get(i).getCust_name());
            faulty_devices.setText("Number of Faulty VTS - " + site_data.get(i).getFaulty_dev());
            faulty_drs.setText("Number of Faulty DRS - " + site_data.get(i).getFaulty_drs());

            Log.i("*****outer i****", new StringBuilder().append(i).toString());
            return vv;
        }
    }

    class UpdateTechnicianList extends AsyncTask<String, Void, Void> {
        ProgressDialog pDialog;
        URL url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                technicianlist.clear();
                String data = K.Url.urlkey;
                byte[] encodedd = Base64.encode(data.getBytes("UTF-8"), Base64.DEFAULT);
                String str1 = new String(encodedd, "UTF-8");
                Log.i("***urlen****", K.Url.gettechnician + str1);
                url = new URL(K.Url.gettechnician + str1);
                DocumentBuilderFactory builder = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = builder.newDocumentBuilder();
                Document doc = documentBuilder.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                NodeList nl = doc.getElementsByTagName("master");
                Element ea = (Element) nl.item(0);
                nl = ea.getElementsByTagName("tech");
                for (int i = 0; i < nl.getLength(); i++) {
                    ea = (Element) nl.item(i);
                    ClientList s = new ClientList();
                    s.setClientid(K.getNode("techid", ea));
                    s.setClientname(K.getNode("techname", ea).trim());
                    technicianlist.add(s);
                }
                Log.i("***stop size***", String.valueOf(technicianlist.size()));
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
            ArrayList<String> arr = new ArrayList<String>();
            for (int i = 0; i < technicianlist.size(); i++) {
                arr.add(technicianlist.get(i).getClientname());
            }
            try {
                adp = new ArrayAdapter<String>(getActivity(), R.layout.spinner_items, arr);
                technicians.setAdapter(adp);
            }catch (NullPointerException npe)
            {
                npe.printStackTrace();
            }
            }
    }

}