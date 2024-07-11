package in.eoninfotech.eontechnician.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import in.eoninfotech.eontechnician.FaultyDeviceDetailAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.responses.FaultyDevices;
import in.eoninfotech.eontechnician.responses.FaultyDevicesDetails;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 9/11/18.
 */

public class FaultyDevicesDetailActivity extends AppCompatActivity {

    String usrname, zone, version;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    private FaultyDeviceDetailAdapter faultyDeviceDetailAdapter;
    public LinearLayoutManager layoutManager;
    ArrayList<FaultyDevicesDetails> faultyDevicesDetails = new ArrayList<>();
    SharedPreferences sharedprefs;
    private TextView txtContentUnavailable;
    String device_value;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.faulty_fragment_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Faulty Devices Details");
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        usrname = sharedprefs.getString("dis_user", "");
        version = sharedprefs.getString("version", "");
        zone = sharedprefs.getString("zone", "");
        txtContentUnavailable = findViewById(R.id.txt_content_unavailable);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(FaultyDevicesDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE);
        refreshLayout.setOnRefreshListener(this::refresh);
        refreshLayout.setRefreshing(true);
        Intent intent = getIntent();
        device_value = intent.getStringExtra("device_value");

        getFaultyDeviceData();
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
                    faultyDevicesDetails = activityResponse.getFaultyDevices();
                    faultyDeviceDetailAdapter = new FaultyDeviceDetailAdapter(faultyDevicesDetails, FaultyDevicesDetailActivity.this);
                    recyclerView.setAdapter(faultyDeviceDetailAdapter);
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

    private void refresh() {
        clear();
        getFaultyDeviceData();
    }

    private void clear() {
        faultyDevicesDetails.clear();
    }

    @Override
    public void onBackPressed() {

        Intent inteer = new Intent(FaultyDevicesDetailActivity.this, FaultyDevicesActivity.class);
        inteer.putExtra("device_value", device_value);
        startActivity(inteer);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent inteer = new Intent(FaultyDevicesDetailActivity.this, FaultyDevicesActivity.class);
            inteer.putExtra("device_value", device_value);
            startActivity(inteer);
            finish();
        } else {
        }
        return super.onOptionsItemSelected(item);
    }
}
