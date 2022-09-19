package in.eoninfotech.eontechnician.fragments;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.MainResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.AttResponse;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class BillIntimationFragment extends Fragment {

    EditText fromDate,toDate,amount,remarks,billNo;
    Calendar calen = Calendar.getInstance();
    int year, month, day;
    String current_date,selected_todate,s_remarks="",s_amount,s_from_date,s_to_date,version,username;
    View v;
    LinearLayout submit,linearBill;
    ProgressBar progressBar;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_bill_intimation, container, false);

        sharedprefs = this.getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");

        fromDate = v.findViewById(R.id.fromDate);
        toDate = v.findViewById(R.id.toDate);
        amount = v.findViewById(R.id.amount);
        remarks = v.findViewById(R.id.remarks);
        billNo = v.findViewById(R.id.billNo);
        submit = v.findViewById(R.id.submit);
        linearBill = v.findViewById(R.id.linearBill);
        linearBill.setVisibility(View.GONE);
        fromDate.setInputType(InputType.TYPE_NULL);
        toDate.setInputType(InputType.TYPE_NULL);
        billNo.setInputType(InputType.TYPE_NULL);
        progressBar = v.findViewById(R.id.progressBar);

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

         setDate();
         
         submit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 s_from_date = fromDate.getText().toString();
                 s_to_date = toDate.getText().toString();
                 s_amount = amount.getText().toString();
                 s_remarks = remarks.getText().toString();
                 
                 if(s_amount.equalsIgnoreCase("")){
                     amount.setError("Enter Amount");
                 }else {
                     submitData();
                 }
             }
         });

        return v;

    }

    private void submitData() {

        linearBill.setVisibility(View.VISIBLE);
        billNo.setText("B123854468");

//        ShowProgressBar(true);
//        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
//        Call<MainResponse> call = log_att.submit_bill(username,s_from_date,s_to_date,s_amount,s_remarks);
//        Log.i("****call", String.valueOf(call));
//        call.enqueue(new Callback<MainResponse>() {
//            @Override
//            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
//                MainResponse updateDataResponse = response.body();
//                Log.i("**respnse", " " + response.body().getType());
//
//                if (updateDataResponse.getType() == 1) {
//                    linearBill.setVisibility(View.VISIBLE);
//                    billNo.setText("B123854468");
//                    ShowProgressBar(false);
//                }
//                ShowProgressBar(false);
//            }
//            @Override
//            public void onFailure(Call<MainResponse> call, Throwable t) {
//                t.printStackTrace();
//                ShowProgressBar(false);
//                Toast.makeText(getActivity(), "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
//            }
//        });

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

    private void ShowProgressBar(boolean show) {
        try {
            if (show) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }}
        catch (Exception e) {
            e.printStackTrace();
        }}

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
                    selected_todate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
                } else {
                    selected_todate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                }
                fromDate.setText(selected_todate);
            }
        }, year, month - 1, day);
        dpdd.getDatePicker().setMaxDate(calen.getTimeInMillis());
        dpdd.show();
    }
}