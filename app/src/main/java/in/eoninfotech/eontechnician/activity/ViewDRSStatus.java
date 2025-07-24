package in.eoninfotech.eontechnician.activity;

import static androidx.test.InstrumentationRegistry.getContext;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.ActivityDetailAdapter;
import in.eoninfotech.eontechnician.BillViewAdapter;
import in.eoninfotech.eontechnician.BusDataAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.controllers.NewInstallmentController;
import in.eoninfotech.eontechnician.databinding.DeviceDashboardDetailBinding;
import in.eoninfotech.eontechnician.databinding.ViewDrsStatusBinding;
import in.eoninfotech.eontechnician.fragments.LiveStatusAdapterNew;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.responses.DeviceLiveStatus;
import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.viewModel.ViewModelActivityDetails;
import in.eoninfotech.eontechnician.viewModel.ViewModelLiveStatus;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.BillResponse;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import in.eoninfotech.eontechnicianactivity.DeviceCountDetailAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewDRSStatus extends AppCompatActivity {

    ViewDrsStatusBinding binding;
    String usrname, zone, version, reg_no, server, dbName;

    private AlertDialog progressDialog;

    SharedPreferences sharedprefs;

    SharedPreferences.Editor editor;
    public LinearLayoutManager layoutManager;
    ViewModelLiveStatus viewModelLiveStatus;
    private final ArrayList<DeviceLiveStatus> deviceLiveStatuses = new ArrayList<>();

    BusDataAdapter busDataAdapter;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ViewDrsStatusBinding.inflate(getLayoutInflater());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        View view = binding.getRoot();
        // below line is to set
        // Content view for our layout.
        setContentView(view);
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        usrname = sharedprefs.getString("s_uuser", "");

        Intent intent = getIntent();
        reg_no = intent.getStringExtra("veh_no");
        server = intent.getStringExtra("server");
        dbName = intent.getStringExtra("db_Name");

        actionBar.setTitle("DRS Status - " + reg_no);

        layoutManager = new LinearLayoutManager(ViewDRSStatus.this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);

        progressDialog = new SpotsDialog(ViewDRSStatus.this, R.style.CustomIncentive);

        getData();

        binding.swipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        binding.swipeRefresh.setOnRefreshListener(this::refresh);
        binding.swipeRefresh.setRefreshing(true);

    }

    private void refresh() {
        getData();
    }

    private void getData() {

        viewModelLiveStatus = ViewModelProviders.of(this).get(ViewModelLiveStatus.class);
        viewModelLiveStatus.getLiveStatusRepository(reg_no, server, dbName).observe(this, response -> {

            if (response == null) {
                Toast.makeText(this, "Null response from server", Toast.LENGTH_SHORT).show();
                return;
            }
            if (response.getType() == 1) {
                deviceLiveStatuses.clear();
                deviceLiveStatuses.addAll(response.getData());
                busDataAdapter = new BusDataAdapter(this, deviceLiveStatuses);
                binding.recyclerView.setAdapter(busDataAdapter);
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.txtContentUnavailable.setVisibility(View.GONE);
                progressDialog.hide();
                binding.swipeRefresh.setRefreshing(false);
            } else {
                progressDialog.hide();
                binding.swipeRefresh.setRefreshing(false);
                binding.recyclerView.setVisibility(View.GONE);
                binding.txtContentUnavailable.setVisibility(View.VISIBLE);
                binding.txtContentUnavailable.setText(response.getMsg());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
