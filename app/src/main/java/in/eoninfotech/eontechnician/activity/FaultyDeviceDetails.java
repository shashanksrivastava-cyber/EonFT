package in.eoninfotech.eontechnician.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;

import java.util.ArrayList;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.eoninfotech.eontechnician.InstInstructionAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.responses.InstInstructionResponse;
import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.responses.InstructionDetail;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 30/11/18.
 */

public class FaultyDeviceDetails extends AppCompatActivity implements InstInstructionAdapter.InstInstructionAdapterListener {

    TextView site_veh_num, site_drs_num, vts_Id;
    SharedPreferences sharedprefs;
    String usrname, zone, version;
    String locId, server, database, cust_id, custName, location;
    public RecyclerView recyclerView;
    public LinearLayoutManager layoutManager;
    ArrayList<InstructionDetail> instructionDetails = new ArrayList<>();
    InstInstructionAdapter instAdapter;
    private TextView txtContentUnavailable;
    InstInstructionAdapter.InstInstructionAdapterListener listener;
    String message_id = "";

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

        site_veh_num = findViewById(R.id.site_veh_num);
        site_drs_num = findViewById(R.id.site_drs_num);
        vts_Id = findViewById(R.id.vts_Id);
        recyclerView = findViewById(R.id.recyclerView);
        txtContentUnavailable = findViewById(R.id.txt_content_unavailable);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        String faultyVTS = intent.getStringExtra("Faulty VTS");
        String faultyDRS = intent.getStringExtra("Faulty DRS");
        locId = intent.getStringExtra("LocId");
        server = intent.getStringExtra("Server");
        database = intent.getStringExtra("Database");
        cust_id = intent.getStringExtra("Cust_id");
        custName = intent.getStringExtra("CustomerName");
        location = intent.getStringExtra("Location");
        if (!faultyVTS.equals("")) {
            String abc = String.valueOf(faultyVTS);
            String veh = removeSlash(abc);
            String def = removeBr(veh);
            site_veh_num.setText(def);
        } else {
            site_veh_num.setText("No Faulty VTS");
        }
        if (!faultyDRS.equals("")) {
            String DrsVehicleNumber = removeColon(faultyDRS);
            String veh = removeSlashDRS(DrsVehicleNumber);
            String def = removeBr(veh);
            site_drs_num.setText(def);
        } else {
            site_drs_num.setText("No Faulty DRS");
        }
        listener = new InstInstructionAdapter.InstInstructionAdapterListener() {
            @Override
            public void onMessageRowClicked(int position) {
                InstructionDetail message = instructionDetails.get(position);
                message_id = instructionDetails.get(position).getMsg_id();
                updateData();
            }
        };
        getContent();
    }

    private void updateData() {
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<MainResponse> call = log_att.updateResponse(message_id);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                if (response.body().getType() == 1) {
                    getContent();
                } else {
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
            }
        });
    }

    private void getContent() {

        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<InstInstructionResponse> call = log_att.instructionResponse(server, database, cust_id, locId);
        call.enqueue(new Callback<InstInstructionResponse>() {
            @Override
            public void onResponse(Call<InstInstructionResponse> call, Response<InstInstructionResponse> response) {
                if (response.body().getType() == 1) {
                    InstInstructionResponse instInstructionResponse = response.body();
                    txtContentUnavailable.setVisibility(View.GONE);
                    instructionDetails = instInstructionResponse.getInstDetail();
                    instAdapter = new InstInstructionAdapter(getApplicationContext(), instructionDetails, listener, custName, location);
                    recyclerView.setAdapter(instAdapter);
                } else {
                    txtContentUnavailable.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<InstInstructionResponse> call, Throwable t) {
            }
        });
    }

    private String removeBr(String str1) {
        return str1.replaceAll("\\|", "\n");
    }

    private String removeColon(String str1) {
        return str1.replaceAll(":", "\n");
    }

    private String removeSlash(String str1) {
        return str1.replaceAll("\\/", "      ");
    }

    private String removeSlashDRS(String str1) {
        return str1.replaceAll("\\/", "        ");
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else {
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMessageRowClicked(int position) {

    }
}
