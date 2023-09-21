package in.eoninfotech.eontechnician.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import in.eoninfotech.eontechnician.FaultyDevicesAdapter;
import in.eoninfotech.eontechnician.FaultyDrsAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.FaultyDevices;
import in.eoninfotech.eontechnician.Responses.FaultyDevicesDetails;
import in.eoninfotech.eontechnician.Responses.UnderMaintenanceDetail;
import in.eoninfotech.eontechnician.Responses.UnderMaintenanceResponse;
import in.eoninfotech.eontechnician.UmainAdapter;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 2/11/18.
 */

public class FaultyDevicesActivity extends AppCompatActivity {

    String usrname, zone, version;
    public RecyclerView recyclerView;
    private FaultyDevicesAdapter faultyDevicesAdapter;
    private FaultyDrsAdapter faultyDrsAdapter;
    private UmainAdapter umainAdapter;
    public LinearLayoutManager layoutManager;
    private TextView txtContentUnavailable;
    public SwipeRefreshLayout refreshLayout;
    String sendData;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    ArrayList<FaultyDevicesDetails> faultyDevices = new ArrayList<>();
    ArrayList<UnderMaintenanceDetail> uMainDetail = new ArrayList<>();
    String tab,device_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.faulty_fragment);
        Intent intent = getIntent();
        device_value = intent.getStringExtra("device_value");
        tab = intent.getStringExtra("tab");
        String zonee = intent.getStringExtra("zone");
        String other = intent.getStringExtra("other");
        if (device_value.equals("3")) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Under Maintenance");
        } else if (device_value.equals("1")) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Faulty DRS/SOS");
        } else {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Faulty Devices");
        }
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        usrname = sharedprefs.getString("dis_user", "");
        version = sharedprefs.getString("version", "");
        if (other.equals("1")) {
            zone = zonee;
        } else {
            zone = sharedprefs.getString("zone", "");
        }
        recyclerView = findViewById(R.id.recyclerView);
        txtContentUnavailable = findViewById(R.id.txt_content_unavailable);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE);
        refreshLayout.setOnRefreshListener(this::refresh);
        refreshLayout.setRefreshing(true);

        if (device_value.equals("1")) {
            sendData = device_value;
            getFaultyDrsData();
        } else if (device_value.equals("2")) {
            sendData = device_value;
            getFaultyDeviceData();
        } else if (device_value.equals("3")) {
            sendData = device_value;
            getUmainValue();
        } else if (device_value.equals("4")) {
            sendData = device_value;
            getFaultyDeviceData();
        }
    }

    private void getUmainValue() {

        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<UnderMaintenanceResponse> call = log_att.underMainResponse(zone);
        call.enqueue(new Callback<UnderMaintenanceResponse>() {
            @Override
            public void onResponse(Call<UnderMaintenanceResponse> call, Response<UnderMaintenanceResponse> response) {
                if (response.body().getType() == 1) {
                    UnderMaintenanceResponse activityResponse = response.body();
                    txtContentUnavailable.setVisibility(View.GONE);
                    uMainDetail = activityResponse.getuMainDetail();
                    umainAdapter = new UmainAdapter(uMainDetail, FaultyDevicesActivity.this, sendData);
                    recyclerView.setAdapter(umainAdapter);
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
            public void onFailure(Call<UnderMaintenanceResponse> call, Throwable t) {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void getFaultyDeviceData() {

        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<FaultyDevices> call = log_att.faultyDevicesResponse(zone);
        call.enqueue(new Callback<FaultyDevices>() {
            @Override
            public void onResponse(Call<FaultyDevices> call, Response<FaultyDevices> response) {
                if (response.body().getType() == 1) {
                    FaultyDevices activityResponse = response.body();
                    txtContentUnavailable.setVisibility(View.GONE);
                    faultyDevices = activityResponse.getFaultyDevices();
                    faultyDevicesAdapter = new FaultyDevicesAdapter(faultyDevices, FaultyDevicesActivity.this, sendData);
                    recyclerView.setAdapter(faultyDevicesAdapter);
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
            public void onFailure(Call<FaultyDevices> call, Throwable t) {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void getFaultyDrsData() {

        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<FaultyDevices> call = log_att.faultyDRSResponse(zone);
        call.enqueue(new Callback<FaultyDevices>() {
            @Override
            public void onResponse(Call<FaultyDevices> call, Response<FaultyDevices> response) {
                if (response.body().getType() == 1) {
                    FaultyDevices activityResponse = response.body();
                    txtContentUnavailable.setVisibility(View.GONE);
                    faultyDevices = activityResponse.getFaultyDevices();
                    faultyDrsAdapter = new FaultyDrsAdapter(faultyDevices, FaultyDevicesActivity.this, sendData);
                    recyclerView.setAdapter(faultyDrsAdapter);
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
            public void onFailure(Call<FaultyDevices> call, Throwable t) {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_right);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void refresh() {
        clear();
        if (device_value.equals("1")) {
            sendData = device_value;
            getFaultyDrsData();
        } else if (device_value.equals("2")) {
            sendData = device_value;
            getFaultyDeviceData();
        } else if (device_value.equals("3")) {
            sendData = device_value;
            getUmainValue();
        } else if (device_value.equals("4")) {
            sendData = device_value;
            getFaultyDeviceData();
        }
    }

    private void clear() {
        faultyDevices.clear();
    }

    @Override
    public void onBackPressed() {

        if (tab.equals("1")) {
            finish();
        } else if (tab.equals("2")) {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (tab.equals("1")) {
                finish();
            } else if (tab.equals("2")) {
                finish();
            }
        } else {
        }
        return super.onOptionsItemSelected(item);
    }

}
