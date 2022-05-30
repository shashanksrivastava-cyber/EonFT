package in.eoninfotech.eontechnician.fragments;


import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.ActivityDetailAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.ActivityResponse;
import in.eoninfotech.eontechnician.Responses.IncentiveResponse;
import in.eoninfotech.eontechnician.Responses.MonthDetail;
import in.eoninfotech.eontechnician.Responses.MonthListResponse;
import in.eoninfotech.eontechnician.Responses.TechDashboardDetail;
import in.eoninfotech.eontechnician.Responses.YearDetail;
import in.eoninfotech.eontechnician.Responses.YearListResponse;
import in.eoninfotech.eontechnician.activity.IncentiveAdapter;
import in.eoninfotech.eontechnician.helper.IncentiveDetail;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.helper.ListIncentiveDetail;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCurrentMonth extends Fragment {

    View v;
    String username, version,zone,displayName;
    int year, month, day,hour,minute,seconds;
    LinearLayout mLayout;
    Dialog myDialog;
    TextView datee,tech_name,dates,textView;
    TextView txt_add,txt_attendance,txt_activity,txt_stock,txt_callSheet,txt_paymentCollection,txt_incentive;
    String current_date, selected_todate, s_date = "0",s_time,months,date,user_id;
    Calendar calen = Calendar.getInstance();
    private AlertDialog progressDialog;
    public RecyclerView recyclerView;
    public SwipeRefreshLayout refreshLayout;
    public LinearLayoutManager layoutManager;
    private TextView txtContentUnavailable;
    MySearchableSpinner monthSpinner,yearSpinner;
    ArrayList<MonthDetail> monthList = new ArrayList<>();
    ArrayList<YearDetail> yearList = new ArrayList<>();
    ArrayList<String> monthDetail = new ArrayList<>();
    ArrayList<String> yearDetail = new ArrayList<>();
    ArrayAdapter<String> adapter;
    RelativeLayout relay;
    String monthtobeSend="0",yeartobeSend="0";
    private IncentiveAdapter incentiveAdapter;
    ArrayList<TechDashboardDetail> dashboardList = new ArrayList<>();
    Float achivd, total;
    ProgressBar progressBar;
    Timer timer;
    ImageView filter,go;
    LinearLayout lay_filter;
    SharedPreferences sharedprefs;
    TextView incentiveDate,incentive ;
    //ScratchTextView scratch_view;
    private TextView mScratchTitleView;
    ArrayList<ListIncentiveDetail> incentiveDetails = new ArrayList<ListIncentiveDetail>();
    private Random random = new Random();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
        sharedprefs = getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        zone = sharedprefs.getString("zone","");
        user_id = sharedprefs.getString("s_user_id","");
        displayName = sharedprefs.getString("dis_user","");
        myDialog = new Dialog(getActivity());
       // arc_add = v.findViewById(R.id.arc_add);
        monthSpinner = v.findViewById(R.id.monthSpinner);
        yearSpinner = v.findViewById(R.id.yearSpinner);
        tech_name = v.findViewById(R.id.tech_name);
        dates = v.findViewById(R.id.dates);
        filter = v.findViewById(R.id.action_filter);
        lay_filter = v.findViewById(R.id.linearView);
        lay_filter.setVisibility(View.GONE);
        progressDialog = new SpotsDialog(getActivity(), R.style.CustomIncentive);
       // refreshLayout = v.findViewById(R.id.refresh);
        mLayout = v.findViewById(R.id.mLayout);
        datee = v.findViewById(R.id.date);
        txtContentUnavailable = v.findViewById(R.id.txt_content_unavailable);
        go = v.findViewById(R.id.go);
        incentiveDate = v.findViewById(R.id.incentiveDate);
        txt_add = v.findViewById(R.id.txt_add);
        txt_attendance = v.findViewById(R.id.txt_attendance);
        txt_activity = v.findViewById(R.id.txt_activity);
        txt_stock = v.findViewById(R.id.txt_stock);
        txt_callSheet = v.findViewById(R.id.txt_callSheet);
        txt_paymentCollection = v.findViewById(R.id.txt_paymentCollection);
        txt_incentive = v.findViewById(R.id.txt_incentive);
        tech_name.setText(displayName);
        incentive  = v.findViewById(R.id.incentive);

        CountDownTimer newtimer = new CountDownTimer(1000000000, 1000) {

            public void onTick(long millisUntilFinished) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                s_time = dateFormat.format(calen.getTime());
                year = calen.get(Calendar.YEAR);
                month = calen.get(Calendar.MONTH);
                day = calen.get(Calendar.DAY_OF_MONTH);
                month = month + 1;
                if(month==1){
                    months = "Jan";
                }else if(month==2){
                    months = "Feb";
                }else if(month==3){
                    months = "Mar";
                }else if(month==4){
                    months = "Apr";
                }else if(month==5){
                    months = "May";
                }else if(month==6){
                    months = "Jun";
                }else if(month==7){
                    months = "Jul";
                }else if(month==8){
                    months = "Aug";
                }else if(month==9){
                    months = "Sep";
                }else if(month==10){
                    months = "Oct";
                }else if(month==11){
                    months = "Nov";
                }else if(month==12){
                    months = "Dec";
                }
                current_date = day + "-"  +months + "-" + year;
               // date.setText(current_date);
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
                seconds = c.get(Calendar.SECOND);

                incentiveDate.setText("* Till Date"+" "+current_date+" "+hour+":"+minute+":"+seconds);
            }
            public void onFinish() {
            }
        };
        newtimer.start();
        getMonth();
        getYear();
        setDateAndTime();
        monthtobeSend = String.valueOf(calen.get(Calendar.MONTH)+1);
        yeartobeSend = String.valueOf(calen.get(Calendar.YEAR));

        if(monthtobeSend.equals("1")){
            dates.setText("JAN"+" "+","+" "+yeartobeSend);
        }else if(monthtobeSend.equals("2")){
            dates.setText("FEB"+" "+","+" "+yeartobeSend);
        }else if(monthtobeSend.equals("3")){
            dates.setText("MAR"+" "+","+" "+yeartobeSend);
        } else if(monthtobeSend.equals("4")){
            dates.setText("APR"+" "+","+" "+yeartobeSend);
        } else if(monthtobeSend.equals("5")){
            dates.setText("MAY"+" "+","+" "+yeartobeSend);
        } else if(monthtobeSend.equals("6")){
            dates.setText("JUN"+" "+","+" "+yeartobeSend);
        } else if(monthtobeSend.equals("7")){
            dates.setText("JUL"+" "+","+" "+yeartobeSend);
        }else if(monthtobeSend.equals("8")){
            dates.setText("AUG"+" "+","+" "+yeartobeSend);
        }else if(monthtobeSend.equals("9")){
            dates.setText("SEP"+" "+","+" "+yeartobeSend);
        }else if(monthtobeSend.equals("10")){
            dates.setText("OCT"+" "+","+" "+yeartobeSend);
        }else if(monthtobeSend.equals("11")){
            dates.setText("NOV"+" "+","+" "+yeartobeSend);
        }else if(monthtobeSend.equals("12")){
            dates.setText("DEC"+" "+","+" "+yeartobeSend);
        }
       // dates.setText(monthtobeSend+","+yeartobeSend);
        loadContent();
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                monthtobeSend = String.valueOf(monthList.get(i).getId());
                if(monthtobeSend.equals("1")){
                    dates.setText("JAN"+" "+","+" "+yeartobeSend);
                }else if(monthtobeSend.equals("2")){
                    dates.setText("FEB"+" "+","+" "+yeartobeSend);
                }else if(monthtobeSend.equals("3")){
                    dates.setText("MAR"+" "+","+" "+yeartobeSend);
                } else if(monthtobeSend.equals("4")){
                    dates.setText("APR"+" "+","+" "+yeartobeSend);
                } else if(monthtobeSend.equals("5")){
                    dates.setText("MAY"+" "+","+" "+yeartobeSend);
                } else if(monthtobeSend.equals("6")){
                    dates.setText("JUN"+" "+","+" "+yeartobeSend);
                } else if(monthtobeSend.equals("7")){
                    dates.setText("JUL"+" "+","+" "+yeartobeSend);
                }else if(monthtobeSend.equals("8")){
                    dates.setText("AUG"+" "+","+" "+yeartobeSend);
                }else if(monthtobeSend.equals("9")){
                    dates.setText("SEP"+" "+","+" "+yeartobeSend);
                }else if(monthtobeSend.equals("10")){
                    dates.setText("OCT"+" "+","+" "+yeartobeSend);
                }else if(monthtobeSend.equals("11")){
                    dates.setText("NOV"+" "+","+" "+yeartobeSend);
                }else if(monthtobeSend.equals("12")){
                    dates.setText("DEC"+" "+","+" "+yeartobeSend);
                }
              //  loadContent();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadContent();
            }
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                yeartobeSend = String.valueOf(yearList.get(i).getYear());
               // loadContent();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

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

        dates.setOnClickListener(new View.OnClickListener() {
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

        return v;
    }
    private void runOnUiThread(Runnable progress) {
    }
    private void setDateAndTime() {
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
        datee.setText(current_date);
        String abc = datee.getText().toString();
        String[] separated = abc.split("-");
        String date =  separated[0];
        String month = separated[1];
        String years = separated[2];
        String dates = years + "-" + month + "-" + date;
        s_date = dates;
        datee.setOnClickListener(v -> getDate());
    }
    private void getDate() {
        final DatePickerDialog dpdd = new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> {
            if (monthOfYear + 1 < 10) {
                selected_todate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
            } else {
                selected_todate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
            }
            datee.setText(selected_todate);
            String abc = datee.getText().toString();
            String[] separated = abc.split("-");
            String date =  separated[0];
            String month = separated[1];
            String years = separated[2];
            String dates = years + "-" + month + "-" + date;
            s_date = dates;
           // loadContent();
        }, year, month - 1, day);
        dpdd.getDatePicker().setMaxDate(calen.getTimeInMillis());
        dpdd.show();
    }

    private void refresh() {
        s_date = "0";
        loadContent();
        setDateAndTime();
    }

    private void loadContent() {
        progressDialog.show();
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<IncentiveResponse> call = log_att.incentiveResponse(user_id,username,zone,monthtobeSend,yeartobeSend);
        call.enqueue(new Callback<IncentiveResponse>() {
            @Override
            public void onResponse(Call<IncentiveResponse> call, Response<IncentiveResponse> response) {
                IncentiveResponse incentiveResponse = response.body();
                incentiveDetails = incentiveResponse.getIncentv_list();
                if (response.body().getType() == 1) {
                    filter.setImageResource(R.drawable.ic_filter);
                    lay_filter.setVisibility(View.GONE);
                    txt_add.setText(incentiveDetails.get(0).getAdd_cnt());
                    String color1  = incentiveDetails.get(0).getAdd_color();
                    String[] separated = color1.split(";");
                    String color = separated[0];
                    int myColor = Color.parseColor(color);
                    txt_add.setTextColor(myColor);
                    txt_attendance.setText(incentiveDetails.get(0).getAttendance());
                    txt_activity.setText(incentiveDetails.get(0).getActivity());
                    txt_stock.setText(incentiveDetails.get(0).getStock());
                    txt_callSheet.setText(incentiveDetails.get(0).getCall_sheets());
                    txt_paymentCollection.setText(incentiveDetails.get(0).getPayment_collection());
                    if(incentiveDetails.get(0).getIncentive_amt().equalsIgnoreCase("false")){
                       txt_incentive.setText("---");
                       String monnth = String.valueOf(month);
                       int montt = Integer.parseInt(monthtobeSend);
                       int monn = month-1;
                       String mm = String.valueOf(monn);
                       if(monnth.equals(monthtobeSend)){
                           incentiveDate.setVisibility(View.VISIBLE);
                           incentive.setVisibility(View.VISIBLE);
                           incentive.setText("** Incentive to be calculated in 1st week of Next Month ");
                        }else if(monthtobeSend.equals(mm)){
                           incentiveDate.setVisibility(View.GONE);
                           incentive.setVisibility(View.VISIBLE);
                          incentive.setText("** Incentive not yet Calculated!!");
                       }else{
                           incentiveDate.setVisibility(View.GONE);
                           incentive.setVisibility(View.GONE);
                       }
                    }else {
                        incentiveDate.setVisibility(View.GONE);
                        incentive.setVisibility(View.GONE);
                        txt_incentive.setText(incentiveDetails.get(0).getIncentive_amt());
                        txt_incentive.setTextSize(25);
                    }
                    progressDialog.hide();
                } else {
                    txtContentUnavailable.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    progressDialog.hide();
                }
            }
            @Override
            public void onFailure(Call<IncentiveResponse> call, Throwable t) {
               // refreshLayout.setRefreshing(false);
                progressDialog.hide();
            }
        });
    }

    private void getYear() {
        ApiHolder uploadImage = ServiceConnectionNewURL.getClient().create(ApiHolder.class);
        Call<YearListResponse>call = uploadImage.yearResponse();
        call.enqueue(new Callback<YearListResponse>() {
            @Override
            public void onResponse(Call<YearListResponse> call, Response<YearListResponse> response) {
                if (response.body().getType() == 1) {
                    try{
                        yearList = response.body().getYearDetail();
                        try {
                            try {
                                yearDetail.clear();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }for (int i = 0; i < yearList.size(); i++) {
                                yearDetail.add(yearList.get(i).getYear());
                            }adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item,yearDetail);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            yearSpinner.setAdapter(adapter);
                        } catch (NullPointerException npe) {
                            npe.printStackTrace();
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<YearListResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getMonth() {
        ApiHolder uploadImage = ServiceConnectionNewURL.getClient().create(ApiHolder.class);
        Call<MonthListResponse>call=uploadImage.monthResponse();
        call.enqueue(new Callback<MonthListResponse>() {
            @Override
            public void onResponse(Call<MonthListResponse> call, Response<MonthListResponse> response) {
                if (response.body().getType() == 1) {
                    try{
                        monthList = response.body().getMonthDetail();
                        try {
                            try {
                                monthDetail.clear();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }for (int i = 0; i < monthList.size(); i++) {
                                monthDetail.add(monthList.get(i).getName());
                            }adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, monthDetail);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            monthSpinner.setAdapter(adapter);
                            monthSpinner.setSelection(month-1);
                        } catch (NullPointerException npe) {
                            npe.printStackTrace();
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<MonthListResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
            }
        });
    }
}
