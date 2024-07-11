package in.eoninfotech.eontechnician.fragments;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import in.eoninfotech.eontechnician.PaymentCollectionAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.responses.CollectionReportDetail;
import in.eoninfotech.eontechnician.responses.CollectionReportResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by root on 12/3/19.
 */

public class PaymentCollectionReportFragment extends Fragment {

    View v;
    String current_date, selected_todate, todatetoSend,fromdatetoSend;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String username, dist_id,version;
    public RecyclerView recyclerView;
    public SwipeRefreshLayout refreshLayout;
    public LinearLayoutManager layoutManager;
    public LinearLayout linearActivity;
    EditText fromDate,toDate;
    TextView t_install_date,totalAmount,fromDateShow,toDateShow;
    int year, month, day, hour, minutes;
    Calendar calen = Calendar.getInstance();
    private TextView txtContentUnavailable;
    ImageView filter;
    ImageView go;
    LinearLayout lay_filter;
    private PaymentCollectionAdapter paymentCollectionAdapter;
    ArrayList<CollectionReportDetail> collectionReportDetails = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.payment_collection_report, container, false);
        sharedprefs = getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("usname", "");
        dist_id = sharedprefs.getString("s_distt", "");
        version = sharedprefs.getString("version", "");

        refreshLayout = v.findViewById(R.id.refresh);
        recyclerView = v.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//      t_install_date = (EditText) v.findViewById(R.id.installDate);
        t_install_date = v.findViewById(R.id.installDate);
        totalAmount = v.findViewById(R.id.totalAmount);
        fromDate = v.findViewById(R.id.fromDate);
        toDate = v.findViewById(R.id.toDate);
        txtContentUnavailable = v.findViewById(R.id.txt_content_unavailable);
        recyclerView.setLayoutManager(layoutManager);
        linearActivity = v.findViewById(R.id.llContent);
        filter = v.findViewById(R.id.action_filter);
        go = v.findViewById(R.id.go);
        lay_filter = v.findViewById(R.id.linearView);
        fromDateShow = v.findViewById(R.id.fromDateShow);
        toDateShow = v.findViewById(R.id.toDateShow);
        lay_filter.setVisibility(View.GONE);
        setDateAndTime();
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE,Color.GREEN);
        refreshLayout.setOnRefreshListener(this::refresh);
        refreshLayout.setRefreshing(true);
        fromDate.setInputType(InputType.TYPE_NULL);
        toDate.setInputType(InputType.TYPE_NULL);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lay_filter.getVisibility()== View.VISIBLE){
                    filter.setImageResource(R.drawable.ic_filter);
                    lay_filter.setVisibility(View.GONE);
                }else {
                    filter.setImageResource(R.drawable.up_arrow);
                    lay_filter.setVisibility(View.VISIBLE);
                }
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter.setImageResource(R.drawable.ic_filter);
                lay_filter.setVisibility(View.GONE);
                loadContent();
            }
        });

        loadContent();
        return v;
    }

    private void loadContent() {

        refreshLayout.setRefreshing(true);
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<CollectionReportResponse> call = log_att.requestCollectionReportResponse(username,fromdatetoSend,todatetoSend);
        call.enqueue(new Callback<CollectionReportResponse>() {
            @Override
            public void onResponse(Call<CollectionReportResponse> call, Response<CollectionReportResponse> response) {
                totalAmount.setText(response.body().getTotal_amount());
                if (response.body().getType() == 1) {
                    CollectionReportResponse collectionReportResponse = response.body();
                    txtContentUnavailable.setVisibility(View.GONE);
                    collectionReportDetails = collectionReportResponse.getCollectionReportDetails();
                    paymentCollectionAdapter = new PaymentCollectionAdapter(getContext(), collectionReportDetails);
                    recyclerView.setAdapter(paymentCollectionAdapter);
                    refreshLayout.setRefreshing(false);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    txtContentUnavailable.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                }
            }
            @Override
            public void onFailure(Call<CollectionReportResponse> call, Throwable t) {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void refresh() {
        loadContent();
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
        fromDate.setText(current_date);
        toDate.setText(current_date);
        String abc = fromDate.getText().toString();
        String[] separated = abc.split("-");
        String date =  separated[0];
        String month = separated[1];
        String years = separated[2];
        String dates  = years+"-"+month+"-"+date;
        todatetoSend = dates;
        fromdatetoSend = dates;
        fromDateShow.setText(current_date);
        toDateShow.setText(current_date);
        loadContent();
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettoDate();
            }
        });
    }

    private void gettoDate() {
        final DatePickerDialog dpdd = new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> {
            if (monthOfYear + 1 < 10) {
                selected_todate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
            } else {
                selected_todate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
            }
            toDate.setText(selected_todate);
            toDateShow.setText(selected_todate);
            String abc = toDate.getText().toString();
            String[] separated = abc.split("-");
            String date =  separated[0];
            String month = separated[1];
            String years = separated[2];
            String dates  = years+"-"+month+"-"+date;
            todatetoSend = dates;
        }, year, month - 1, day);
        dpdd.getDatePicker().setMaxDate(calen.getTimeInMillis());
        dpdd.show();
    }

    private void getDate() {
        // TODO Auto-generated method stub
        final DatePickerDialog dpdd = new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> {
            if (monthOfYear + 1 < 10) {
                selected_todate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
            } else {
                selected_todate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
            }
            fromDate.setText(selected_todate);
            fromDateShow.setText(selected_todate);
            String abc = fromDate.getText().toString();
            String[] separated = abc.split("-");
            String date =  separated[0];
            String month = separated[1];
            String years = separated[2];
            String dates  = years+"-"+month+"-"+date;
            fromdatetoSend = dates;
        }, year, month - 1, day);
        dpdd.getDatePicker().setMaxDate(calen.getTimeInMillis());
        dpdd.show();
    }

}
