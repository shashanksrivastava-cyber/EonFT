package in.eoninfotech.eontechnician;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.Responses.ActivityResponse;
import in.eoninfotech.eontechnician.activity.ImageDetailActivity;
import in.eoninfotech.eontechnician.activity.PhotoFullPopup;
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
            ,remarks,workType;


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
