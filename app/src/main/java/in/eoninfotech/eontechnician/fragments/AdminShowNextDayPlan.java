package in.eoninfotech.eontechnician.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.Responses.AdminShowNextDayPlanDetail;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 31/10/17.
 */
public class AdminShowNextDayPlan extends Fragment{
    ExpandableListView exandlistt;
    int lastExpandedGroupPosition = -1;
    View v;
    String disttid;
    String username, version;
    ArrayList<AdminShowNextDayPlanDetail> plan_list = new ArrayList<>();
    ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_plan_detail, container, false);
        exandlistt = v.findViewById(R.id.report_expand_list);
        disttid = getArguments().getString("disttid");
        username = getArguments().getString("usernme");
        version = getArguments().getString("version");
        pDialog = new ProgressDialog(v.getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        ApiHolder get_list = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<UpdateDataResponse> call = get_list.admin_show_plan(K.Url.urlkey);
        call.enqueue(new Callback<UpdateDataResponse>() {
            @Override
            public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                plan_list = response.body().getShow_plan();

                for (int i = 0; i < plan_list.size(); i++) {
                    Log.i("****", plan_list.get(i).getTech_name());
                }
                exandlistt.setAdapter(new ExpandAdapter(getContext()));
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
        exandlistt.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != lastExpandedGroupPosition) {
                    exandlistt.collapseGroup(lastExpandedGroupPosition);
                }
                lastExpandedGroupPosition = groupPosition;
            }
        });
        exandlistt.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                int count = expandableListView.getExpandableListAdapter().getChildrenCount(i);
                if (count <= 0) {
                    TSnackbar snackbar = TSnackbar.make(v, "No Data Found", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.BLACK);
                    TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                    exandlistt.collapseGroup(i);
                }
                return false;
            }
        });

        return v;
    }
    class ExpandAdapter extends BaseExpandableListAdapter {

        Context ctx;
        int j;
        ExpandAdapter(Context c) {
            ctx = c;
        }

        @Override
        public int getGroupCount() {

            Log.i("********", new StringBuilder(plan_list.size()).toString());
            return plan_list.size();
        }

        @Override
        public int getChildrenCount(int i) {
            Log.i("********", new StringBuilder(plan_list.get(i).getView_plan().size()).toString());

            return plan_list.get(i).getView_plan().size();
        }

        @Override
        public Object getGroup(int i) {
            return null;
        }

        @Override
        public Object getChild(int i, int i1) {
            return null;
        }

        @Override
        public long getGroupId(int i) {
            return 0;
        }

        @Override
        public long getChildId(int i, int i1) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.ctx.
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.reprt_list_group, null);
            }
            TextView listTitleTextView = view
                    .findViewById(R.id.listTitle);
            listTitleTextView.setText(Html.fromHtml("<b>" + plan_list.get(i).getTech_name().toUpperCase() + "</b>"));
            return view;
        }

        @Override
        public View getChildView(int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
            j=i;
            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.custom_showlist_plan, null);
            }

            TableRow tech_block;
            TextView technician, date, cust_name, client_name, client_number, spoken_to_client, veh_chk, drs_chk, remarks;
            tech_block= view.findViewById(R.id.tech_name_block);
            tech_block.setVisibility(View.VISIBLE);
            technician = view.findViewById(R.id.tech_name);
            date = view.findViewById(R.id.view_date);
            cust_name = view.findViewById(R.id.view_cust_name);
            client_name = view.findViewById(R.id.view_client_name);
            client_number = view.findViewById(R.id.view_client_number);
            spoken_to_client = view.findViewById(R.id.view_spoken_to_client);
            veh_chk = view.findViewById(R.id.view_veh_chk);
            drs_chk = view.findViewById(R.id.view_drs_chk);
            remarks = view.findViewById(R.id.view_remarks);
            plan_list.get(i).getView_plan().size();
            date.setText(plan_list.get(i).getView_plan().get(i1).getDate());
            cust_name.setText(plan_list.get(i).getView_plan().get(i1).getCust_name());
            client_name.setText(plan_list.get(i).getView_plan().get(i1).getClient_name());
            client_number.setText(plan_list.get(i).getView_plan().get(i1).getClient_number());
            spoken_to_client.setText(plan_list.get(i).getView_plan().get(i1).getSpoken_to_client());
            veh_chk.setText(plan_list.get(i).getView_plan().get(i1).getVeh_chk());
            drs_chk.setText(plan_list.get(i).getView_plan().get(i1).getDrs_chk());
            remarks.setText(plan_list.get(i).getView_plan().get(i1).getRemarks());
            Log.i("*****outer i****", new StringBuilder().append(i).toString());
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }
    }

    }
