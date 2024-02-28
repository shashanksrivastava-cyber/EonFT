package in.eoninfotech.eontechnician;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.eoninfotech.eontechnician.Responses.ActivityDetailResponse;
import in.eoninfotech.eontechnician.Responses.ActivityResponse;
import in.eoninfotech.eontechnician.Responses.DispatchDeviceList;
import in.eoninfotech.eontechnician.Responses.MainResponse;
import in.eoninfotech.eontechnician.Responses.TechReturnDevice;
import in.eoninfotech.eontechnician.callbacks.ReceiveDeviceListener;
import in.eoninfotech.eontechnician.controllers.ReceiveDeviceController;

public class MaterialReturnViews extends Fragment implements ReceiveDeviceListener {

    View v;
    Calendar calen = Calendar.getInstance();
    int year, month, day;
    EditText fromDate, toDate;
    public RecyclerView recyclerView;
    public SwipeRefreshLayout refreshLayout;
    public LinearLayoutManager layoutManager;
    String current_date, selected_todate,username, dist_id, version,user_id,status="A";
    ReceiveDeviceController receiveDeviceController;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    SendDeviceAdapter sendDeviceAdapter;
    Spinner statusSpinner;
    SwipeRefreshLayout swipe_refresh;

    private TextView txtContentUnavailable;
    ArrayList<TechReturnDevice> dispatchDeviceListArrayList = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_return_device_view, container, false);
        sharedprefs = this.getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        user_id = sharedprefs.getString("s_user_id","");

        initView();
        setDate();

        return v;
    }

    private void initView() {

        fromDate = v.findViewById(R.id.fromDate);
        toDate = v.findViewById(R.id.toDate);
        statusSpinner = v.findViewById(R.id.statusSpinner);
        txtContentUnavailable = v.findViewById(R.id.txt_content_unavailable);
        refreshLayout = v.findViewById(R.id.swipe_refresh);
        recyclerView = v.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        receiveDeviceController = new ReceiveDeviceController();
        swipe_refresh = v.findViewById(R.id.swipe_refresh);
        swipe_refresh.setColorSchemeColors(Color.RED, Color.BLUE,Color.GREEN);
        swipe_refresh.setOnRefreshListener(this::refresh);
        swipe_refresh.setRefreshing(false);

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

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String abc = adapterView.getItemAtPosition(i).toString();
                if (abc.equalsIgnoreCase("All")) {
                    status = "A";
                    loadContent();
                } else if (abc.equalsIgnoreCase("Pending")) {
                    status = "P";
                    loadContent();
                } else {
                    status = "R";
                    loadContent();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void refresh() {
        loadContent();
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
                loadContent();
            }
        }, year, month - 1, day);
        dpdd.getDatePicker().setMaxDate(calen.getTimeInMillis());
        dpdd.show();
    }

    private void loadContent() {
        receiveDeviceController.requestDispatchDevice(fromDate.getText().toString(),toDate.getText().toString(),status,user_id,this);
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
                loadContent();
            }
        }, year, month - 1, day);
        dpdd.getDatePicker().setMaxDate(calen.getTimeInMillis());
        dpdd.show();
    }


    @Override
    public void receiveDeviceResponse(MainResponse response) {
        if (response.getType() == 1) {
            dispatchDeviceListArrayList = response.getTech_return_device_list();
            txtContentUnavailable.setVisibility(View.GONE);
            sendDeviceAdapter = new SendDeviceAdapter(getContext(), dispatchDeviceListArrayList);
            recyclerView.setAdapter(sendDeviceAdapter);
            refreshLayout.setRefreshing(false);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            txtContentUnavailable.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void receiveDispatchMaterial(MainResponse response) {

    }

    @Override
    public void returnDeviceresponse(MainResponse response) {

    }

    @Override
    public void dispatchFromTechResponse(MainResponse response) {

    }
}
