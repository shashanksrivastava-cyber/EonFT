package in.eoninfotech.eontechnician;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.responses.ActivityResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDetailActivity extends AppCompatActivity {

    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String user_id,s_date,version,position;
    int i;
    private AlertDialog progressDialog;
    TextView cust_name,loc_name,vts_type,new_device_id,old_device_id,new_sr_no,old_serial_no,old_drs,new_drs,old_sim,new_sim,reason
            ,remarks,workType,collected_amt,collected_date,lid_status,trans_receiver,temp_sensor,fuel_status,panic_status,missing_type;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Activity Detail");

        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        version = sharedprefs.getString("version", "");
        user_id = sharedprefs.getString("s_user_id","");

        Intent intent = getIntent();
        s_date = intent.getStringExtra("Dates");
        position = intent.getStringExtra("Item Position");
        i = Integer.parseInt(position);

        cust_name =findViewById(R.id.customer_name);
        loc_name =findViewById(R.id.loc_name);
        vts_type =findViewById(R.id.vts_type);
        new_device_id =findViewById(R.id.new_device_id);
        old_device_id =findViewById(R.id.old_device_id);
        new_sr_no =findViewById(R.id.new_sr_no);
        old_serial_no =findViewById(R.id.old_serial_no);
        old_drs =findViewById(R.id.old_drs);
        new_drs =findViewById(R.id.new_drs);
        old_sim =findViewById(R.id.old_sim);
        new_sim =findViewById(R.id.new_sim);
        reason =findViewById(R.id.reason);
        remarks =findViewById(R.id.remarks);
        workType =findViewById(R.id.workType);
        collected_amt =findViewById(R.id.collected_amt);
        collected_date =findViewById(R.id.collected_date);
        lid_status =findViewById(R.id.lid_status);
        trans_receiver =findViewById(R.id.trans_receiver);
        temp_sensor =findViewById(R.id.temp_sensor);
        fuel_status =findViewById(R.id.fuel_status);
        panic_status =findViewById(R.id.panic_status);
        missing_type =findViewById(R.id.missing_type);
        progressDialog = new SpotsDialog(this, R.style.CustomIncentive);
        loadContent();
    }

    private void loadContent() {
        progressDialog.show();
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<ActivityResponse> call = log_att.view_activities(s_date, user_id);
        call.enqueue(new Callback<ActivityResponse>() {
            @Override
            public void onResponse(Call<ActivityResponse> call, Response<ActivityResponse> response) {
                if (response.body().getType() == 1) {
                    ActivityResponse activityResponse = response.body();
                    workType.setText(activityResponse.getActivityList().get(i).getActivity());
                    cust_name.setText(activityResponse.getActivityList().get(i).getClient_Name());
                    loc_name.setText(activityResponse.getActivityList().get(i).getClient_loc());
                    vts_type.setText(activityResponse.getActivityList().get(i).getVts_type());
                    new_device_id.setText(activityResponse.getActivityList().get(i).getNew_vts());
                    old_device_id.setText(activityResponse.getActivityList().get(i).getOld_vts());
                    new_sr_no.setText(activityResponse.getActivityList().get(i).getNew_serial_no());
                    old_serial_no.setText(activityResponse.getActivityList().get(i).getOld_serial_no());
                    old_drs.setText(activityResponse.getActivityList().get(i).getOld_drs());
                    new_drs.setText(activityResponse.getActivityList().get(i).getNew_drs());
                    old_sim.setText(activityResponse.getActivityList().get(i).getOld_sim());
                    new_sim.setText(activityResponse.getActivityList().get(i).getNew_sim());
                    reason.setText(activityResponse.getActivityList().get(i).getReasons());
                    remarks.setText(activityResponse.getActivityList().get(i).getRemarks());
                    collected_amt.setText(activityResponse.getActivityList().get(i).getCollected_amt());
                    collected_date.setText(activityResponse.getActivityList().get(i).getCollected_date());
                    lid_status.setText(activityResponse.getActivityList().get(i).getLid_status());
                    trans_receiver.setText(activityResponse.getActivityList().get(i).getTrans_receiver());
                    temp_sensor.setText(activityResponse.getActivityList().get(i).getTemp_sensor());
                    fuel_status.setText(activityResponse.getActivityList().get(i).getFuel_status());
                    panic_status.setText(activityResponse.getActivityList().get(i).getPanic_status());
                    missing_type.setText(activityResponse.getActivityList().get(i).getMissing_type());
                    progressDialog.hide();
                } else {
                    progressDialog.hide();
                }
            }
            @Override
            public void onFailure(Call<ActivityResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
