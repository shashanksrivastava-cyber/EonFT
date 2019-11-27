package in.eoninfotech.eontechnician.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.IncentiveResponse;
import in.eoninfotech.eontechnician.helper.ListIncentiveDetail;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminIncentiveFragment extends Fragment {

    View v;
    ListView lv;
    ArrayList<ListIncentiveDetail> incentive_list = new ArrayList<>();
    String version;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_listview, container, false);
        lv = v.findViewById(R.id.admin_listview);
        version = getArguments().getString("version");
        ApiHolder get_list = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<IncentiveResponse> call = get_list.incentive_list(K.Url.urlkey);
        call.enqueue(new Callback<IncentiveResponse>() {
            @Override
            public void onResponse(Call<IncentiveResponse> call, Response<IncentiveResponse> response) {
                incentive_list = response.body().getIncentv_list();
                for (int i = 0; i < incentive_list.size(); i++) {
                  //  Log.i("****", incentive_list.get(i).getTech_name());
                }
                lv.setAdapter(new DetailAdapter(getContext(), incentive_list));
                //  pDialog.dismiss();
            }
            @Override
            public void onFailure(Call<IncentiveResponse> call, Throwable t) {
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
        return v;
    }
    class DetailAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater layinfa;
        int j;
        ArrayList<ListIncentiveDetail> planDetails = new ArrayList<>();

        DetailAdapter(Context c, ArrayList<ListIncentiveDetail> planDetails) {
            ctx = c;
            this.planDetails = planDetails;
            layinfa = LayoutInflater.from(ctx);
        }
        @Override
        public int getCount() {
            //  Log.i("*******", new StringBuilder().append(list_vehidetai.size()).toString());
            return incentive_list.size();
        }
        @Override
        public Object getItem(int i) {
            return incentive_list.get(i);
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
                vv = layinfa.inflate(R.layout.custom_incentive_item, null);
            }
            TextView locatn, cust_name, faulty_devices, faulty_drs;
            locatn = vv.findViewById(R.id.tech_name);
            cust_name = vv.findViewById(R.id.total_incentive);

//            locatn.setText(incentive_list.get(i).getTech_name());
//            cust_name.setText(incentive_list.get(i).getTotal_inc());

            Log.i("*****outer i****", new StringBuilder().append(i).toString());
            return vv;
        }
    }

}
