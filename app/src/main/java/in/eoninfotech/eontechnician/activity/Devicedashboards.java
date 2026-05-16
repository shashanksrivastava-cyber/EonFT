package in.eoninfotech.eontechnician.activity;

import static android.app.ProgressDialog.show;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.MainActivity;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.responses.DeviceCount;
import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.databinding.DeviceDashboardActivityBinding;
import in.eoninfotech.eontechnician.responses.TechDashboardDetail;
import in.eoninfotech.eontechnician.viewModel.ViewModelAddDashboard;
import in.eoninfotech.eontechnician.viewModel.ViewModelDeviceDashboard;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Devicedashboards extends AppCompatActivity  {

    DeviceDashboardActivityBinding binding;

    String usrname, zone, version;

    private AlertDialog progressDialog;

    SharedPreferences sharedprefs;

    SharedPreferences.Editor editor;

    ArrayList<DeviceCount> countDetails = new ArrayList<>();

    ArrayList<PieEntry> yValues = new ArrayList<>();

    String f_faulty = "0", f_working = "0", f_total = "";

    public static final int REQUEST_CODE = 1;

    ViewModelDeviceDashboard viewModelDeviceDashboard;
    private CheckConnection checkConnection;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = DeviceDashboardActivityBinding.inflate(getLayoutInflater());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Live Inventory Dashboard");

        // getting our root layout in our view.
        View view = binding.getRoot();
        // below line is to set
        // Content view for our layout.
        setContentView(view);

        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        usrname = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        zone = sharedprefs.getString("zone", "");

        progressDialog = new SpotsDialog(Devicedashboards.this, R.style.CustomIncentive);
        checkConnection = new CheckConnection(this);

        viewModelDeviceDashboard = new ViewModelProvider(this).get(ViewModelDeviceDashboard.class);
        binding.setViewModelDeviceDashboard(viewModelDeviceDashboard);

        if (checkConnection.isConnected()) {
            getDeviceCountDetails();
        } else {
            checkConnection.showConnectionErrorDialog();
        }

        binding.linearTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Devicedashboards.this, DevicedashboardDetail.class);
                intent.putExtra("Status", "T");
                startActivity(intent);
            }
        });

        binding.cvWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Devicedashboards.this, DevicedashboardDetail.class);
                intent.putExtra("Status", "W");
                startActivity(intent);
            }
        });

        binding.cvFaulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Devicedashboards.this, DevicedashboardDetail.class);
                intent.putExtra("Status", "F");
                startActivity(intent);
            }
        });

        binding.inTransitTech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Devicedashboards.this, DevicedashboardDetail.class);
                intent.putExtra("Status", "ITT");
                startActivity(intent);
            }
        });

        binding.inTransitStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Devicedashboards.this, DevicedashboardDetail.class);
                intent.putExtra("Status", "ITS");
                startActivity(intent);
            }
        });

        binding.receiveDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Devicedashboards.this, ReceiveDeviceActivity.class);
                startActivity(intent);
            }
        });

        binding.sendToEon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Devicedashboards.this, MainActivity.class);
                intent.putExtra("intent", "toEon");
                startActivity(intent);
                finish();
            }
        });

        binding.sendToFt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Devicedashboards.this, MainActivity.class);
                intent.putExtra("intent", "toFT");
                startActivity(intent);
                finish();
            }
        });

    }

    private void getDeviceCountDetails() {
        progressDialog.show();
        viewModelDeviceDashboard = ViewModelProviders.of(this).get(ViewModelDeviceDashboard.class);
        viewModelDeviceDashboard.getDashboardCountRepository(usrname).observe(this, movieResponse -> {

            if (movieResponse == null) {
                Toast.makeText(this, ""+movieResponse.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.hide();
                return;
            }
            if (movieResponse.getType() == 1) {
                countDetails = movieResponse.getDevice_count();
                if (countDetails != null &&!countDetails.isEmpty()) {
                    DeviceCount countList = countDetails.get(0);

                    // Update UI through data binding
                    binding.setDeviceCount(countList);

                    binding.totalDevice.setText("Total Devices - " + countDetails.get(0).total);
                    binding.totalDrs.setText("Total DRS - " + countDetails.get(0).total_drs);
                    binding.inTransitStore.setText("Outgoing Material - " + countDetails.get(0).in_transit_store);
                    binding.inTransitTech.setText("Incoming Material - " + countDetails.get(0).in_transit_tech);

                }
                progressDialog.hide();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Prevent memory leaks
    }
}
