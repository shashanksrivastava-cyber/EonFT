package in.eoninfotech.eontechnician.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.helper.CriticalSitesData;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class CriticalSitesFragment extends Fragment {
    ListView lv;
    // TextView text;
    View v;
    String disttid;
    String username, version;
    ArrayList<CriticalSitesData> site_data = new ArrayList<>();
    RelativeLayout relay;
    private TextView txtContentUnavailable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_critical_sites, container, false);
        lv = v.findViewById(R.id.view_listview);
        relay = v.findViewById(R.id.critical_relay);
        txtContentUnavailable = v.findViewById(R.id.txt_content_unavailable);
        disttid = getArguments().getString("disttid");
        username = getArguments().getString("usernme");
        version = getArguments().getString("version");
        relay.setVisibility(View.GONE);
        getCriticaldata(username);
        return v;
    }

    void getCriticaldata(String user_name) {

        ApiHolder get_list = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<UpdateDataResponse> call = get_list.critical_sites(user_name, K.Url.urlkey);
        call.enqueue(new Callback<UpdateDataResponse>() {
            @Override
            public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                site_data = response.body().getSite_data();
                if (response.body().getType() == 1) {
                    for (int i = 0; i < site_data.size(); i++) {
                        Log.i("****", site_data.get(i).getCust_name());
                    }
                } else {
                    txtContentUnavailable.setVisibility(View.VISIBLE);
                }
                try {
                    lv.setAdapter(new DetailAdapter(getContext(), site_data));
                } catch (Exception e) {
                    e.printStackTrace();
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
            locatn = vv.findViewById(R.id.site_loc);
            cust_name = vv.findViewById(R.id.site_cust_name);
            faulty_devices = vv.findViewById(R.id.faulty_devices);
            faulty_drs = vv.findViewById(R.id.faulty_drs);

            locatn.setText(site_data.get(i).getLocation());
            cust_name.setText(site_data.get(i).getCust_name());
            faulty_devices.setText("Number of Faulty VTS - " + site_data.get(i).getFaulty_dev());
            faulty_drs.setText("Number of Faulty DRS - " + site_data.get(i).getFaulty_drs());



            return vv;
        }
    }
}
