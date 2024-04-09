package in.eoninfotech.eontechnician.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.BillViewAdapter;
import in.eoninfotech.eontechnician.NonScrollListView;
import in.eoninfotech.eontechnician.OtherMaterialAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.DeviceItems;
import in.eoninfotech.eontechnician.Responses.DeviceList;
import in.eoninfotech.eontechnician.Responses.DispatchDeviceDetails;
import in.eoninfotech.eontechnician.Responses.FaultList;
import in.eoninfotech.eontechnician.Responses.MainResponse;
import in.eoninfotech.eontechnician.Responses.TechReturnDetails;
import in.eoninfotech.eontechnician.databinding.ActivitySendDeviceDetailsBinding;
import in.eoninfotech.eontechnician.fragments.LiveStatusAdapterNew;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendDeviceDetails extends AppCompatActivity {

    ActivitySendDeviceDetailsBinding binding;
    SharedPreferences sharedprefs;
    String usrname, version, id_no;
    NonScrollListView device_detail_list_receive;
    ProgressDialog pDialog;
    //ArrayList<DispatchDeviceDetails> list_change_values = new ArrayList<>();
    ArrayList<DeviceList> list_change_values = new ArrayList<>();
    //ArrayList<DeviceList> list_values = new ArrayList<>();
    ArrayList<DeviceItems> lr = new ArrayList<>();
    ArrayList<String> value_name = new ArrayList<>();
    ArrayAdapter<String> adapter;
    OtherMaterialAdapter othermaterialAdapter;
    public LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendDeviceDetailsBinding.inflate(getLayoutInflater());

        // getting our root layout in our view.
        View view = binding.getRoot();
        // below line is to set
        // Content view for our layout.
        setContentView(view);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Dispatched Devices Details");
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        usrname = sharedprefs.getString("dis_user", "");

        Intent intent = getIntent();
        id_no = intent.getStringExtra("id_no");

        initView();
        getContent();

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initView(){

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);
    }

    private void getContent() {
        try {
            pDialog = K.createProgressDialog(this);
            pDialog.setMessage("Loading");
            pDialog.show();
            pDialog.setCancelable(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<MainResponse> call = log_att.get_tech_returned_device_details(id_no);
        Log.i("****call", String.valueOf(call));
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                if (response.body().getType() == 1) {
                    pDialog.hide();
                    try {
                        lr = response.body().getTech_return_details().get(0).getDevice_items();
                        list_change_values = response.body().getTech_return_details().get(0).getDevice_list();
                        try {
                            try {
                                value_name.clear();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (list_change_values.size() > 0) {
                                for (int i = 0; i < list_change_values.size(); i++) {
                                    Log.e("List Change Value",""+list_change_values.get(i));
                                   value_name.add(list_change_values.get(i).getCust_name()+" "+list_change_values.get(i).getPcb_sr_no());
                                }
                                if (list_change_values.size() > 5) {
                                    binding.deviceDetailListReceive.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * list_change_values.size() + 1));
                                } else {
                                    binding.deviceDetailListReceive.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * list_change_values.size()));
                                }
                                adapter = new ArrayAdapter<String>(SendDeviceDetails.this, R.layout.custom_list_item_disable, value_name);
                                binding.deviceDetailListReceive.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                                binding.deviceDetailListReceive.setAdapter(adapter);

                                othermaterialAdapter = new OtherMaterialAdapter(SendDeviceDetails.this,lr,"Send");
                                binding.recyclerView.setAdapter(othermaterialAdapter);
                                binding.recyclerView.setVisibility(View.VISIBLE);

                            } else {
                            }
                        } catch (NullPointerException npe) {
                            npe.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    pDialog.hide();
                    Toast.makeText(SendDeviceDetails.this, ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(SendDeviceDetails.this, "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
