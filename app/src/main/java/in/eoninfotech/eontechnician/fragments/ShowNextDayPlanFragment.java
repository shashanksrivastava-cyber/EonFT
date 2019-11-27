package in.eoninfotech.eontechnician.fragments;


import android.app.ProgressDialog;
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
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.helper.ViewPlanDetail;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowNextDayPlanFragment extends Fragment {

    ListView lv;
    TextView text;
    View v;
    String disttid;
    String username, version;
    ArrayList<ViewPlanDetail> plan_list = new ArrayList<>();
    ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_show_next_day_plan, container, false);
        lv = v.findViewById(R.id.view_listview);
        text = v.findViewById(R.id.text_no_record);
        disttid = getArguments().getString("disttid");
        username = getArguments().getString("usernme");
        version = getArguments().getString("version");
        pDialog = new ProgressDialog(v.getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        ApiHolder get_list = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<UpdateDataResponse> call = get_list.view_next_plan(username, K.Url.urlkey);
        call.enqueue(new Callback<UpdateDataResponse>() {
            @Override
            public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                plan_list = response.body().getView_plan();
                if(plan_list.size()>0) {
                    text.setVisibility(View.GONE);
                } else {
                    text.setVisibility(View.VISIBLE);
                }for (int i = 0; i < plan_list.size(); i++) {
                    Log.i("****", plan_list.get(i).getCust_name());
                }
                lv.setAdapter(new DetailAdapter(getContext(), plan_list));
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                t.printStackTrace();
                pDialog.dismiss();
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
        ArrayList<ViewPlanDetail> planDetails = new ArrayList<>();
        DetailAdapter(Context c, ArrayList<ViewPlanDetail> planDetails) {
            ctx = c;
            this.planDetails = planDetails;
            layinfa = LayoutInflater.from(ctx);
        }

        @Override
        public int getCount() {
            return plan_list.size();
        }

        @Override
        public Object getItem(int i) {
            return plan_list.get(i);
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
                vv = layinfa.inflate(R.layout.custom_showlist_plan, null);
            }
            TextView date, cust_name, client_name, client_number, spoken_to_client, veh_chk, drs_chk, remarks;
            date = vv.findViewById(R.id.view_date);
            cust_name = vv.findViewById(R.id.view_cust_name);
            client_name = vv.findViewById(R.id.view_client_name);
            client_number = vv.findViewById(R.id.view_client_number);
            spoken_to_client = vv.findViewById(R.id.view_spoken_to_client);
            veh_chk = vv.findViewById(R.id.view_veh_chk);
            drs_chk = vv.findViewById(R.id.view_drs_chk);
            remarks = vv.findViewById(R.id.view_remarks);

            date.setText(plan_list.get(i).getDate());
            cust_name.setText(plan_list.get(i).getCust_name());
            client_name.setText(plan_list.get(i).getClient_name());
            client_number.setText(plan_list.get(i).getClient_number());
            spoken_to_client.setText(plan_list.get(i).getSpoken_to_client());
            veh_chk.setText(plan_list.get(i).getVeh_chk());
            drs_chk.setText(plan_list.get(i).getDrs_chk());
            remarks.setText(plan_list.get(i).getRemarks());
            Log.i("*****outer i****", new StringBuilder().append(i).toString());
            return vv;
        }
    }
}
