package in.eoninfotech.eontechnician.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.BillViewAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.callbacks.ClientListener;
import in.eoninfotech.eontechnician.controllers.NewInstallmentController;
import in.eoninfotech.eontechnician.databinding.DeviceDashboardDetailBinding;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.responses.ClientLocationResponse;
import in.eoninfotech.eontechnician.responses.ClientResponse;
import in.eoninfotech.eontechnician.responses.CollectedItemsResponse;
import in.eoninfotech.eontechnician.responses.DeviceCountDetail;
import in.eoninfotech.eontechnician.responses.DisconnectionResponse;
import in.eoninfotech.eontechnician.responses.FaultResponse;
import in.eoninfotech.eontechnician.responses.MainClientList;
import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.responses.NotAvailActivityResponse;
import in.eoninfotech.eontechnician.responses.PaymentMethodResponse;
import in.eoninfotech.eontechnician.responses.RemovalActivityResponse;
import in.eoninfotech.eontechnician.responses.RemovalResponse;
import in.eoninfotech.eontechnician.responses.ReplaceReason;
import in.eoninfotech.eontechnician.responses.SimOperatorResponse;
import in.eoninfotech.eontechnician.responses.SimReplaceResponse;
import in.eoninfotech.eontechnician.responses.VTSResponse;
import in.eoninfotech.eontechnician.responses.VehNotAvailReasonResponse;
import in.eoninfotech.eontechnician.responses.VehicleTypeResponse;
import in.eoninfotech.eontechnician.responses.WorkTypeResponse;
import in.eoninfotech.eontechnician.viewModel.ViewModelCountDetails;
import in.eoninfotech.eontechnician.viewModel.ViewModelDeviceDashboard;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.BillResponse;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import in.eoninfotech.eontechnicianactivity.DeviceCountDetailAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DevicedashboardDetail extends AppCompatActivity implements ClientListener, DeviceCountDetailAdapter.MessageAdapterListener {

    DeviceDashboardDetailBinding binding;

    String usrname,zone,version;

    private AlertDialog progressDialog;

    SharedPreferences sharedprefs;

    SharedPreferences.Editor editor;

    String mainClientId="",status="";

    NewInstallmentController newInstallmentController;

    ArrayAdapter<String> adapter;

    ArrayList<MainClientList> mainclientList = new ArrayList<>();

    ArrayList<String> mainClientDetail = new ArrayList<>();

    ArrayList<DeviceCountDetail> deviceCountDetails = new ArrayList<>();

    DeviceCountDetailAdapter deviceCountDetailAdapter;

    public LinearLayoutManager layoutManager;

    DeviceCountDetailAdapter.MessageAdapterListener listener;

    ViewModelCountDetails viewModelCountDetails;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = DeviceDashboardDetailBinding.inflate(getLayoutInflater());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Device Count Detail");

        View view = binding.getRoot();
        // below line is to set
        // Content view for our layout.
        setContentView(view);
        newInstallmentController = new NewInstallmentController();
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        usrname = sharedprefs.getString("s_uuser", "");

        layoutManager = new LinearLayoutManager(DevicedashboardDetail.this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);

        progressDialog = new SpotsDialog(DevicedashboardDetail.this, R.style.CustomIncentive);

       listener = new DeviceCountDetailAdapter.MessageAdapterListener() {
           @Override
           public void onButtonClick(@Nullable String pcb_sr_no, @Nullable String change_status) {

               try {
                   progressDialog.show();
               } catch (Exception e) {
                   e.printStackTrace();
               }
               ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
               Call<MainResponse> call = log_att.get_change_device_status(usrname,change_status,pcb_sr_no);
               Log.i("****call", String.valueOf(call));
               call.enqueue(new Callback<MainResponse>() {
                   @Override
                   public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                       progressDialog.hide();
                       if(response.body().getType()==1){
                           getData();
                       }
                       else {
                           Toast.makeText(DevicedashboardDetail.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                       }
                   }
                   @Override
                   public void onFailure(Call<MainResponse> call, Throwable t) {
                       t.printStackTrace();
                       Toast.makeText(DevicedashboardDetail.this, "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
                   }
               });
           }
       };

        Intent intent = getIntent();
        status = intent.getStringExtra("Status");

        if(status.equalsIgnoreCase("T")){
            binding.total.setChecked(true);
            status="T";
            getData();
        } else if(status.equalsIgnoreCase("W")){
            binding.working.setChecked(true);
            status="W";
            getData();
        }else if(status.equalsIgnoreCase("F")) {
            binding.faulty.setChecked(true);
            status = "F";
            getData();
        } else if (status.equalsIgnoreCase("ITT")){
            binding.inTransitTech.setChecked(true);
            status="ITT";
            getData();
        }else{
            binding.inTransitStore.setChecked(true);
            status="ITS";
            getData();
        }

        binding.swipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE,Color.GREEN);
        binding.swipeRefresh.setOnRefreshListener(this::refresh);
        binding.swipeRefresh.setRefreshing(true);

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               if(checkedId == R.id.total){
                   status="T";
                   getData();
               }else if(checkedId == R.id.working){
                   status="W";
                   getData();
               }else if(checkedId == R.id.faulty) {
                   status="F";
                   getData();
               }else if(checkedId == R.id.in_transit_tech) {
                   status="ITT";
                   getData();
               }else {
                   status="ITS";
                   getData();
               }
            }
        });

        binding.newMainClients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                mainClientId = String.valueOf(mainclientList.get(i).getClient_Id());
                getData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void refresh() {
        getData();
    }

    private void addclients() {
        newInstallmentController.reqeuestMainClientList(this);
    }

    private void getData() {
        progressDialog.show();
        viewModelCountDetails= ViewModelProviders.of(this).get(ViewModelCountDetails.class);
        viewModelCountDetails.getCountDetailsRepository(usrname,status,mainClientId).observe(this, movieResponse -> {
            deviceCountDetails = movieResponse.getDevice_count_detail();
            if(movieResponse.getType()==1){
                binding.txtContentUnavailable.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);
                deviceCountDetails = movieResponse.getDevice_count_detail();
                deviceCountDetailAdapter = new DeviceCountDetailAdapter(DevicedashboardDetail.this, deviceCountDetails,status,"",listener);
                binding.recyclerView.setAdapter(deviceCountDetailAdapter);
                deviceCountDetailAdapter.notifyDataSetChanged();
                binding.recyclerView.setVisibility(View.VISIBLE);
                progressDialog.hide();
            }else {
                binding.txtContentUnavailable.setVisibility(View.VISIBLE);
                binding.swipeRefresh.setRefreshing(false);
                binding.recyclerView.setVisibility(View.GONE);
                progressDialog.hide();
            }
        });

    }

    @Override
    public void clientResponse(ClientResponse response) {

    }

    @Override
    public void locationResponse(ClientLocationResponse response) {

    }

    @Override
    public void workTypeResponse(WorkTypeResponse response) {

    }

    @Override
    public void vehicleTypeResponse(VehicleTypeResponse response) {

    }

    @Override
    public void faultListResponse(FaultResponse response) {

    }

    @Override
    public void replaceResponse(ReplaceReason response) {

    }

    @Override
    public void disconnectionResponse(DisconnectionResponse response) {

    }

    @Override
    public void removalActivityResponse(RemovalActivityResponse response) {

    }

    @Override
    public void removalResponse(RemovalResponse response) {

    }

    @Override
    public void damageResponse(RemovalResponse response) {

    }

    @Override
    public void collectItemResponse(CollectedItemsResponse response) {

    }

    @Override
    public void simOperatorResponse(SimOperatorResponse response) {

    }

    @Override
    public void simReplaceReason(SimReplaceResponse response) {

    }

    @Override
    public void notAvailActivity(NotAvailActivityResponse response) {

    }

    @Override
    public void vehicleNotAvailReason(VehNotAvailReasonResponse response) {

    }

    @Override
    public void vtsResponses(VTSResponse response) {

    }

    @Override
    public void vtsResponse(VTSResponse response) {

    }

    @Override
    public void pMethod(PaymentMethodResponse response) {

    }

    @Override
    public void updateDataResponse(MainResponse response) {

    }

    @Override
    public void mainClientResponse(MainResponse response) {
        try {
            mainclientList = response.getMain_client_list();
            try {
                try {
                    mainClientDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mainClientDetail.add("SELECT CLIENT (Optional)");
                for (int i = 0; i < mainclientList.size(); i++) {
                    mainClientDetail.add(mainclientList.get(i).getClient_Name());
                }
                adapter = new ArrayAdapter<String>(this, R.layout.simple_custom_spinner_item, mainClientDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.newMainClients.setAdapter(adapter);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void vtsAccResponses(MainResponse response) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        addclients();
        super.onResume();
    }

    @Override
    public void onButtonClick(String pcb_sr_no,String change_status) {

    }

}
