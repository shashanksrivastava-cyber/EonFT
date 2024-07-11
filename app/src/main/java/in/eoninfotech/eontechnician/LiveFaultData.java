package in.eoninfotech.eontechnician;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import in.eoninfotech.eontechnician.responses.MyPojo;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 24/11/18.
 */

public class LiveFaultData extends AppCompatActivity {

    WebView wv;
    String html,usrname,version,zone;
    public RecyclerView recyclerView;
    private TextView txtContentUnavailable;
    public SwipeRefreshLayout refreshLayout;
    public LinearLayoutManager layoutManager;
    SharedPreferences sharedprefs;
  //  String[][] liveFault;
    ArrayList<MyPojo> liveFault = new ArrayList<>();
    private LiveFaultDataAdapter liveFaultDataAdapter;
    private PvtClientAdapter pvtClientAdapter;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_fault_data);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Private Client Details");
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        usrname = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version","");
        zone = sharedprefs.getString("zone","");
        recyclerView = findViewById(R.id.recyclerView);
        txtContentUnavailable = findViewById(R.id.txt_content_unavailable);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE);
        refreshLayout.setOnRefreshListener(this::refresh);
        refreshLayout.setRefreshing(true);
        loadData();
    }

    private void loadData() {
        ApiHolder loc_att = ServiceConnectionNewURL.getClient().create(ApiHolder.class);
        Call<MyPojo> restCall = loc_att.readPvtData(usrname);
        restCall.enqueue(new Callback<MyPojo>(){
            @Override
            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {

                if(response.body().getType().equals("1")){
                    MyPojo activityResponse = response.body();
                    txtContentUnavailable.setVisibility(View.GONE);
                    liveFault = activityResponse.getList();
                    pvtClientAdapter = new PvtClientAdapter(LiveFaultData.this,liveFault);
                    recyclerView.setAdapter(pvtClientAdapter);
                    refreshLayout.setRefreshing(false);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                else{
                    txtContentUnavailable.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                }
            }
            @Override
            public void onFailure(Call<MyPojo> call, Throwable t) {
                Toast.makeText(LiveFaultData.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refresh() {
        clear();
        loadData();
    }
    private void clear() {
        liveFault.clear();
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
}
