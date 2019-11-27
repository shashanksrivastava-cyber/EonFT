package in.eoninfotech.eontechnician.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thefinestartist.Base;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.CallSheetResponse;
import in.eoninfotech.eontechnician.Responses.ClientDetails;
import in.eoninfotech.eontechnician.Responses.MonthDetail;
import in.eoninfotech.eontechnician.Responses.MonthListResponse;
import in.eoninfotech.eontechnician.Responses.YearDetail;
import in.eoninfotech.eontechnician.Responses.YearListResponse;
import in.eoninfotech.eontechnician.activity.CallSheetAdapter;
import in.eoninfotech.eontechnician.helper.CallSheetDetail;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.thefinestartist.utils.content.ContextUtil.getSharedPreferences;

/**
 * Created by root on 1/3/19.
 */

public class ViewCallSheetFragment extends Fragment {

    View v;
    int id = 1;
    EditText selectedMonth,selectedYear;
    private Handler handler = new Handler();
    RelativeLayout circularRelative;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String username, dist_id, version;
    int year, month, day;
    String current_date, selected_todate, s_date = "0";
    Calendar calen = Calendar.getInstance();
    TextView preview, callDate, tv;
    ImageView ivProfile;
    TextView datee;
    Button update_dataa;
    ImageView go;
    ProgressDialog pDialog;
    Button upload_img_view;
    String monthtobeSend="0",yeartobeSend="0";
    public RecyclerView recyclerView;
    public SwipeRefreshLayout refreshLayout;
    public LinearLayoutManager layoutManager;
    public LinearLayout linearActivity, update;
    private TextView txtContentUnavailable;
    private CallSheetAdapter callSheetAdapter;
    MySearchableSpinner monthSpinner,yearSpinner;
    ArrayList<CallSheetDetail> callSheetDetails = new ArrayList<>();
    ArrayList<MonthDetail> monthList = new ArrayList<>();
    ArrayList<YearDetail> yearList = new ArrayList<>();
    ArrayList<String> monthDetail = new ArrayList<>();
    ArrayList<String> yearDetail = new ArrayList<>();
    ArrayAdapter<String> adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_call_sheet_list, container, false);
        Base.initialize(getActivity());
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("usname", "");
        dist_id = sharedprefs.getString("s_distt", "");
        version = sharedprefs.getString("version", "");

        datee = v.findViewById(R.id.date);
        upload_img_view = v.findViewById(R.id.upload_img);
        update_dataa = v.findViewById(R.id.update_data);
        ivProfile = v.findViewById(R.id.ivProfile);
        preview = v.findViewById(R.id.preview);
        update = v.findViewById(R.id.update);
        go = v.findViewById(R.id.go);
//      clearFilter = (ImageView) v.findViewById(R.id.reset);
//      submit  = (ImageView)v.findViewById(R.id.submit);
//      selectedMonth = (EditText)v.findViewById(R.id.month);
//      selectedYear = (EditText)v.findViewById(R.id.year);
        monthSpinner = v.findViewById(R.id.monthSpinner);
        yearSpinner = v.findViewById(R.id.yearSpinner);
        datee.setInputType(InputType.TYPE_NULL);
        //callDate = v.findViewById(R.id.callDate);
        circularRelative = v.findViewById(R.id.circularRelative);
        refreshLayout = v.findViewById(R.id.refresh);
        recyclerView = v.findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(getActivity(),2);
        txtContentUnavailable = v.findViewById(R.id.txt_content_unavailable);
        recyclerView.setLayoutManager(layoutManager);
        linearActivity = v.findViewById(R.id.llContent);
        refreshLayout.setRefreshing(true);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE);
        refreshLayout.setOnRefreshListener(this::refresh);

        getMonth();
        getYear();
        getCallSheetData();
        setDateAndTime();

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              monthtobeSend = String.valueOf(monthList.get(i).getId());
               getCallSheetData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                yeartobeSend = String.valueOf(yearList.get(i).getYear());
                getCallSheetData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

//        clearFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                monthtobeSend="0";
//                yeartobeSend="0";
//                getCallSheetData();
//            }
//        });
//
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getCallSheetData();
//            }
//        });

         return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getCallSheetData();
        }
    }

    private void getYear() {
        ApiHolder uploadImage = ServiceConnectionNewURL.getClient().create(ApiHolder.class);
        Call<YearListResponse> call = uploadImage.yearResponse();
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
                            }adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, yearDetail);
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
        Call<MonthListResponse> call = uploadImage.monthResponse();
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

    private void getCallSheetData() {
        refreshLayout.setRefreshing(true);
        ApiHolder uploadImage = ServiceConnectionNewURL.getClient().create(ApiHolder.class);
        RequestBody tech_name = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody month = RequestBody.create(MediaType.parse("text/plain"), monthtobeSend);
        RequestBody year = RequestBody.create(MediaType.parse("text/plain"), yeartobeSend);
        Call<CallSheetResponse> call = uploadImage.callsheet_list(tech_name, month,year);
        call.enqueue(new Callback<CallSheetResponse>() {
            @Override
            public void onResponse(Call<CallSheetResponse> call, Response<CallSheetResponse> response) {
                if (response.body().getType() == 1) {
                    CallSheetResponse callSheetResponse = response.body();
                    txtContentUnavailable.setVisibility(View.GONE);
                    callSheetDetails = callSheetResponse.getCallsheet_list();
                    callSheetAdapter = new CallSheetAdapter(getContext(), callSheetDetails);
                    recyclerView.setAdapter(callSheetAdapter);
                    refreshLayout.setRefreshing(false);
                    runLayoutAnimation(recyclerView);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    txtContentUnavailable.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                }
            }
            @Override
            public void onFailure(Call<CallSheetResponse> call, Throwable t) {
                t.printStackTrace();
//                pDialog.dismiss();
                Toast.makeText(getActivity(), "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
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
        // TODO Auto-generated method stub
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
            getCallSheetData();
        }, year, month - 1, day);
        dpdd.getDatePicker().setMaxDate(calen.getTimeInMillis());
        dpdd.show();
    }
    private void refresh() {
        s_date = "0";
        getCallSheetData();
        setDateAndTime();
    }
}
