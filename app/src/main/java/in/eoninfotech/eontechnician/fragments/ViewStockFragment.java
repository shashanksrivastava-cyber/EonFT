package in.eoninfotech.eontechnician.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import in.eoninfotech.eontechnician.ActivityDetailAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.ActivityDetailResponse;
import in.eoninfotech.eontechnician.Responses.ClientDataResponse;
import in.eoninfotech.eontechnician.Responses.ClientDetails;
import in.eoninfotech.eontechnician.Responses.ClientResponse;
import in.eoninfotech.eontechnician.Responses.StockClientDataResponse;
import in.eoninfotech.eontechnician.Responses.StockResponse;
import in.eoninfotech.eontechnician.ViewStockAdapter;
import in.eoninfotech.eontechnician.helper.EONUtil;
import in.eoninfotech.eontechnician.helper.StockDetail;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class ViewStockFragment extends Fragment {

    View v;
    TextView e_wrking_vts_qty, e_wrking_vts_srno,e_faulty_vts_qty, e_faulty_vts_srno, e_cable_7_meter, e_cable_2_meter,
            e_drum_sensor_qty, e_magnet_set, e_y_cable, e_remarks, e_power_cable, e_vts_remarks;
    Button update_dataa;
    MySearchableSpinner client;
    TextView datee;
    String  s_drs_status, version;
    ArrayList<StockDetail> stock_dtl = new ArrayList<StockDetail>();
    String username, dist_id;
    ProgressDialog pDialog;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    ArrayList<ClientDetails> clientList = new ArrayList<>();
    ArrayList<StockClientDataResponse> stockClientList = new ArrayList<>();
    ArrayList<String> clientDetail = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private ViewStockAdapter viewStockAdapter;
    ProgressBar progressBar;
    TextView t_install_date,installDates;
    int year, month, day, hour, minutes;
    Calendar calen = Calendar.getInstance();
    private TextView txtContentUnavailable;
    String current_date, selected_todate, s_date;
    public RecyclerView recyclerView;
    public SwipeRefreshLayout refreshLayout;
    public LinearLayoutManager layoutManager;
    public LinearLayout linearActivity;
    ArrayList<StockDetail> activityDetail = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_view_stock, container, false);
        sharedprefs = getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("usname", "");
        dist_id = sharedprefs.getString("s_distt", "");
        version = sharedprefs.getString("version", "");
        e_cable_2_meter = v.findViewById(R.id.cable_2_meter);
        e_cable_7_meter = v.findViewById(R.id.cable_7_meter);
        e_drum_sensor_qty = v.findViewById(R.id.drum_sensor_qty);
        e_faulty_vts_qty = v.findViewById(R.id.faulty_vts_qty);
        e_faulty_vts_srno = v.findViewById(R.id.faulty_vts_srno);
        e_magnet_set = v.findViewById(R.id.magnet_set);
        e_remarks = v.findViewById(R.id.remarks);
        e_vts_remarks = (EditText) v.findViewById(R.id.vts_remarks);
        e_power_cable = v.findViewById(R.id.power_cable);
        e_wrking_vts_qty = v.findViewById(R.id.working_vts_qty);
        e_wrking_vts_srno = v.findViewById(R.id.working_vts_srno);
        e_y_cable = v.findViewById(R.id.y_cable);
        client = v.findViewById(R.id.m_region);
        datee = v.findViewById(R.id.datee);
        update_dataa = v.findViewById(R.id.update_data);
        progressBar = v.findViewById(R.id.progressBar);
        refreshLayout = v.findViewById(R.id.refresh);
        recyclerView = v.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        t_install_date = v.findViewById(R.id.installDate);
        txtContentUnavailable = v.findViewById(R.id.txt_content_unavailable);
        recyclerView.setLayoutManager(layoutManager);
        linearActivity = v.findViewById(R.id.llContent);
        setDateAndTime();
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE);
        refreshLayout.setOnRefreshListener(this::refresh);
        refreshLayout.setRefreshing(true);
        t_install_date.setInputType(InputType.TYPE_NULL);

        ShowProgressBar(false);
        getClient_data();
        setDateAndTime();

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getClient_data();
        }
    }

    private void refresh() {
        getClient_data();
    }

    private void setDateAndTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        hour = calen.get(Calendar.HOUR_OF_DAY);
        minutes = calen.get(Calendar.MINUTE);
        if (month + 1 < 10) {
            current_date = day + "-0" + month + "-" + year;
        } else {
            current_date = day + "-" + month + "-" + year;
        }
        t_install_date.setText(current_date);
        String abc = t_install_date.getText().toString();
        String[] separated = abc.split("-");
        String date =  separated[0];
        String month = separated[1];
        String years = separated[2];
        String dates  = years+"-"+month+"-"+date;
        s_date = dates;
        t_install_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });
    }
    private void getDate() {
        final DatePickerDialog dpdd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            // TODO Auto-generated method stub
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (monthOfYear + 1 < 10) {
                    selected_todate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
                } else {
                    selected_todate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                }
                t_install_date.setText(selected_todate);
                String abc = t_install_date.getText().toString();
                String[] separated = abc.split("-");
                String date =  separated[0];
                String month = separated[1];
                String years = separated[2];
                String dates  = years+"-"+month+"-"+date;
                s_date = dates;
                refreshLayout.setRefreshing(true);
                getClient_data();
            }
        }, year, month - 1, day);
        dpdd.getDatePicker().setMaxDate(calen.getTimeInMillis());
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        dpdd.show();
    }

    private void getClient_data() {
        refreshLayout.setRefreshing(true);
        ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<StockResponse> clientCall = client_att.stock_report(username, s_date);
        clientCall.enqueue(new Callback<StockResponse>() {
            public void onResponse(Call<StockResponse> call, Response<StockResponse> response) {
                StockResponse workTypeResponse = response.body();
                if (response.body().getType()==1) {
                    txtContentUnavailable.setVisibility(View.GONE);
                    activityDetail = workTypeResponse.getStock_list();
                    viewStockAdapter = new ViewStockAdapter(activityDetail, this);
                    recyclerView.setAdapter(viewStockAdapter);
                    runLayoutAnimation(recyclerView);
                    refreshLayout.setRefreshing(false);
                    recyclerView.setVisibility(View.VISIBLE);

                } else {
                    txtContentUnavailable.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                }
            }
            @Override
            public void onFailure(Call<StockResponse> call, Throwable t) {
            refreshLayout.setRefreshing(false);
            }
        });
    }
    private void runLayoutAnimation(RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
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

}

