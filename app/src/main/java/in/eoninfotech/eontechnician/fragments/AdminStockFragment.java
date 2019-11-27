package in.eoninfotech.eontechnician.fragments;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ListView;
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
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.StockResponse;
import in.eoninfotech.eontechnician.helper.StockDetail;
import in.eoninfotech.eontechnician.helper.ClientList;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 16/10/17.
 */
public class AdminStockFragment extends Fragment {
    View v;
    ListView lv;
    MySearchableSpinner tech_names;
    TextView date;
    String selected_todate, tech_name, version;
    int year, month, day;
    Calendar calen;
    ArrayList<ClientList> technicianlist = new ArrayList<ClientList>();
    ArrayList<StockDetail> stockdetail_list = new ArrayList<>();
    ArrayAdapter<String> adp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_admin_stock, container, false);
        lv = v.findViewById(R.id.stock_list);
        tech_names = v.findViewById(R.id.sp_technician);
        date = v.findViewById(R.id.ad_datee);
        version = getArguments().getString("version");
        calen = Calendar.getInstance();
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        if (month + 1 < 10) {
            selected_todate = year+ "-0" + month + "-" + day ;
        } else {
            selected_todate =year  + "-" + month + "-" +day ;
        }
        new UpdateTechnicianList().execute("def");
        date.setText(selected_todate);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpdd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    // TODO Auto-generated method stub
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear + 1 < 10) {
                            selected_todate =  year+ "-0" + (monthOfYear + 1) + "-" +dayOfMonth ;
                        } else {
                            selected_todate = year + "-" + (monthOfYear + 1) + "-" +dayOfMonth ;
                        }
                        date.setText(selected_todate);
                        tech_name = tech_names.getSelectedItem().toString();
                        getIncentiveList(tech_name.toLowerCase(), selected_todate);
                    }
                }, year, month - 1, day);
                dpdd.show();
            }
        });
        tech_names.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tech_name = technicianlist.get(position).getClientname();
                getIncentiveList(tech_name, selected_todate);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    void getIncentiveList(String tech, String date) {
        ApiHolder get_list = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<StockResponse> call = get_list.stock_report(tech, date);
        call.enqueue(new Callback<StockResponse>() {
            @Override
            public void onResponse(Call<StockResponse> call, Response<StockResponse> response) {
                stockdetail_list = response.body().getStock_list();
                for (int i = 0; i < stockdetail_list.size(); i++) {
                    Log.i("****stock", stockdetail_list.get(i).getClientname());
                }
                lv.setAdapter(new DetailAdapter(getContext(), stockdetail_list));
                //  pDialog.dismiss();
            }
            @Override
            public void onFailure(Call<StockResponse> call, Throwable t) {
                t.printStackTrace();
                //pDialog.dismiss();
                TSnackbar snackbar = TSnackbar.make(v, "Try Again Connection Timeout", TSnackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(Color.RED);
                TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();
            }
        });

    }

    class DetailAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater layinfa;
        int j;
        ArrayList<StockDetail> planDetails = new ArrayList<>();

        DetailAdapter(Context c, ArrayList<StockDetail> planDetails) {
            ctx = c;
            this.planDetails = planDetails;
            layinfa = LayoutInflater.from(ctx);
        }

        @Override
        public int getCount() {
            //  Log.i("*******", new StringBuilder().append(list_vehidetai.size()).toString());
            return stockdetail_list.size();
        }

        @Override
        public Object getItem(int i) {
            return stockdetail_list.get(i);
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
                vv = layinfa.inflate(R.layout.custom_stock_list_item, null);
            }
            TextView vts_wrking, vts_faulty, client_name, drs, meter_7, meter_2, mgnt_set, locatn, remarks;
            vts_wrking = vv.findViewById(R.id.stock_vts_working);
            vts_faulty = vv.findViewById(R.id.stock_vts_faulty);
            client_name = vv.findViewById(R.id.stock_client_name);
            drs = vv.findViewById(R.id.stock_drs);
            meter_7 = vv.findViewById(R.id.stock_meter_7);
            meter_2 = vv.findViewById(R.id.stock_meter_2);
            mgnt_set = vv.findViewById(R.id.stock_mgnt_set);
            locatn = vv.findViewById(R.id.stock_location);
            remarks = vv.findViewById(R.id.stock_remarks);
            vts_faulty.setText(stockdetail_list.get(i).getFaulty_vts_qty());
            client_name.setText(stockdetail_list.get(i).getClientname());
            drs.setText(stockdetail_list.get(i).getDrum_sensor_qty());
            locatn.setText(stockdetail_list.get(i).getLocation());
            try {
                vts_wrking.setText(stockdetail_list.get(i).getWorking_vts_qty());
            }catch (Exception e)
            {
                e.printStackTrace();
                vts_wrking.setText("0");
            }
            try{
                meter_7.setText(stockdetail_list.get(i).getCable_7_meter());
            }catch (Exception e)
            {
                e.printStackTrace();
                meter_7.setText("0");
            }
            try{
                meter_2.setText(stockdetail_list.get(i).getCable_2_meter());
            }catch (Exception e)
            {
                e.printStackTrace();
                meter_2.setText("0");
            }
            try{
                mgnt_set.setText(stockdetail_list.get(i).getMagnet_set());
            }catch (Exception e)
            {
                e.printStackTrace();
                mgnt_set.setText("0");
            }
            try{
                remarks.setText(stockdetail_list.get(i).getRemarks());
            }catch (Exception e)
            {
                e.printStackTrace();

            }

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
            adp = new ArrayAdapter<String>(getContext(), R.layout.spinner_items, arr);
            tech_names.setAdapter(adp);
        }
    }
}
