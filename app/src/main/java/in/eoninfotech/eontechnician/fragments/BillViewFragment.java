package in.eoninfotech.eontechnician.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import in.eoninfotech.eontechnician.BillViewAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.ActivityDetailResponse;
import in.eoninfotech.eontechnician.Responses.MainResponse;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.BillDetails;
import in.eoninfotech.eontechnician.webservice.BillResponse;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class BillViewFragment extends Fragment implements BillViewAdapter.MessageAdapterListener {

    RecyclerView recyclerView;
    BillViewAdapter billViewAdapter;
    public LinearLayoutManager layoutManager;
    EditText fromDate, toDate;
    private ProgressDialog pDialog;
    View v;
    public SwipeRefreshLayout refreshLayout;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String version, username,current_date,selected_todate,selected_fromdate,s_from_date,s_to_date,status,s_status;
    Calendar calen = Calendar.getInstance();
    int year, month, day;
    TextView txt_content_unavailable,done;
    MySearchableSpinner filter_status;
    ArrayList<BillDetails> billDetails = new ArrayList<>();
    BillViewAdapter.MessageAdapterListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_bill_view, container, false);
        sharedprefs = this.getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");

        recyclerView = v.findViewById(R.id.recyclerView);
        fromDate = v.findViewById(R.id.fromDate);
        toDate = v.findViewById(R.id.toDate);
        done = v.findViewById(R.id.done);
        filter_status = v.findViewById(R.id.status);
        txt_content_unavailable = v.findViewById(R.id.txt_content_unavailable);

        refreshLayout = v.findViewById(R.id.refresh);
        refreshLayout.setRefreshing(true);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE);
        refreshLayout.setOnRefreshListener(this::refresh);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        fromDate.setInputType(InputType.TYPE_NULL);
        toDate.setInputType(InputType.TYPE_NULL);

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFromDate();
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToDate();
            }
        });

        listener = new BillViewAdapter.MessageAdapterListener() {
            @Override
            public void onCancelButtonClick(int position, String s_bill_no, String s_remarks) {
                getBillDetails(s_from_date,s_to_date,s_status);
            }
        };

        setDate();

        filter_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                s_status = filter_status.getSelectedItem().toString();
                if(s_status.equalsIgnoreCase("Submitted")){
                    s_status = "NR";
                    s_from_date = fromDate.getText().toString();
                    s_to_date = toDate.getText().toString();
                    getBillDetails(s_from_date,s_to_date,s_status);
                }else if(s_status.equalsIgnoreCase("Received")){
                    s_status="R";
                    s_from_date = fromDate.getText().toString();
                    s_to_date = toDate.getText().toString();
                    getBillDetails(s_from_date,s_to_date,s_status);
                }else if(s_status.equalsIgnoreCase("Approved")){
                    s_status = "A";
                    s_from_date = fromDate.getText().toString();
                    s_to_date = toDate.getText().toString();
                    getBillDetails(s_from_date,s_to_date,s_status);
                }else if(s_status.equalsIgnoreCase("Rejected")){
                    s_status = "RJ";
                    s_from_date = fromDate.getText().toString();
                    s_to_date = toDate.getText().toString();
                    getBillDetails(s_from_date,s_to_date,s_status);
                }else if(s_status.equalsIgnoreCase("Cancelled")) {
                    s_status = "C";
                    s_from_date = fromDate.getText().toString();
                    s_to_date = toDate.getText().toString();
                    getBillDetails(s_from_date,s_to_date,s_status);
                }else {
                    s_status = "";
                    s_from_date = fromDate.getText().toString();
                    s_to_date = toDate.getText().toString();
                    getBillDetails(s_from_date,s_to_date,s_status);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return v;
    }

    private void getBillDetails(String s_from_date,String s_to_date,String s_status) {
        try {
            pDialog = K.createProgressDialog(getActivity());
            pDialog.setMessage("Loading");
            pDialog.show();
            pDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<BillResponse> call = log_att.get_bill_details(username,s_from_date,s_to_date,s_status);
        Log.i("****call", String.valueOf(call));
        call.enqueue(new Callback<BillResponse>() {
            @Override
            public void onResponse(Call<BillResponse> call, Response<BillResponse> response) {
                pDialog.dismiss();
                if(response.body().getType().equalsIgnoreCase("1")){
                    BillResponse billResponse = response.body();
                    billDetails = billResponse.getBillDetails();
                    billViewAdapter = new BillViewAdapter(getContext(),billDetails,listener);
                    recyclerView.setAdapter(billViewAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                    txt_content_unavailable.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                    pDialog.dismiss();
                }
                else {
                    refreshLayout.setRefreshing(false);
                    pDialog.dismiss();
                    txt_content_unavailable.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<BillResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getToDate() {
        final DatePickerDialog dpdd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            // TODO Auto-generated method stub
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (monthOfYear + 1 < 10) {
                    selected_todate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
                } else {
                    selected_todate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                }
                toDate.setText(selected_todate);
                s_to_date = selected_todate;
                getBillDetails(s_from_date,s_to_date,"NR");
            }
        }, year, month - 1, day);
        dpdd.getDatePicker().setMaxDate(calen.getTimeInMillis());
        dpdd.show();
    }

    private void getFromDate() {
        final DatePickerDialog dpdd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            // TODO Auto-generated method stub
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (monthOfYear + 1 < 10) {
                    selected_fromdate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
                } else {
                    selected_fromdate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                }
                fromDate.setText(selected_fromdate);
                s_from_date = selected_fromdate;
                getBillDetails(s_from_date,s_to_date,"NR");
            }
        }, year, month - 1, day);
        dpdd.getDatePicker().setMaxDate(calen.getTimeInMillis());
        dpdd.show();
    }

    private void setDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        if (month + 1 < 10) {
            current_date = day + "-0" + month + "-" + year;
        } else {
            current_date = day + "-" + month + "-" + year;
        }
        fromDate.setText(current_date);
        toDate.setText(current_date);
    }

    @Override
    public void onCancelButtonClick(int position, String s_bill_no, String s_remarks) {

    }

    private void refresh() {
        s_status = "";
        s_from_date = fromDate.getText().toString();
        s_to_date = toDate.getText().toString();
        getBillDetails(s_from_date,s_to_date,s_status);
    }
}