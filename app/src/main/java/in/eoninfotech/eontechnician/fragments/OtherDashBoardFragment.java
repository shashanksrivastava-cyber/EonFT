package in.eoninfotech.eontechnician.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import dagger.hilt.android.AndroidEntryPoint;
import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.activity.FaultyDevicesActivity;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.databinding.DashboardNewBinding;
import in.eoninfotech.eontechnician.databinding.FragmentOthersNewBinding;
import in.eoninfotech.eontechnician.di.SharedPreferenceManager;
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.responses.DashBoardResponse;
import in.eoninfotech.eontechnician.responses.TechDashboardDetail;
import in.eoninfotech.eontechnician.responses.TechDetails;
import in.eoninfotech.eontechnician.responses.TechResponse;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.viewModel.ViewModelAddDashboard;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import jakarta.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by root on 10/11/18.
 */
@AndroidEntryPoint
public class OtherDashBoardFragment extends Fragment {

    @Inject
    SharedPreferenceManager sharedPref;
    FragmentOthersNewBinding binding;
    int year, day, month;
    Calendar calen = Calendar.getInstance();
    SharedPreferences sharedprefs;
    String uusername, version, zone, current_date, months;
    ArrayList<TechDetails> techList = new ArrayList<>();
    ArrayList<String> techDetail = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private AlertDialog progressDialog;
    ViewModelAddDashboard viewModelAddDashboard;
    private CheckConnection checkConnection;
    int hour, minutes;

    public static final int[] BRIGHT_COLORS = {
            Color.parseColor("#D32F2F"), Color.parseColor("#F44336"), Color.parseColor("#FFC03C")
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOthersNewBinding.inflate(inflater, container, false);

        uusername = sharedPref.getUsername();
        version = sharedPref.getVersionName();
        zone = sharedPref.getZone();

        progressDialog = new SpotsDialog(requireContext(), R.style.CustomIncentive);
        checkConnection = new CheckConnection(requireContext());

        setDateAndTime();

        viewModelAddDashboard = new ViewModelProvider(this).get(ViewModelAddDashboard.class);
        binding.setViewModelAddDashboard(viewModelAddDashboard);

        setupClickListeners();

        return binding.getRoot();
    }

    private void setupClickListeners() {
        View.OnClickListener launchIntent = view -> {
            Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
            int id = view.getId();

            if (id == binding.cvOneLogin.getId()) {
                intent.putExtra("device_value", "2");
                intent.putExtra("tab", "2");
                intent.putExtra("other", "1");
                intent.putExtra("zone", zone);
            } else if (id == binding.cvTwoLogin.getId()) {
                intent.putExtra("device_value", "1");
                intent.putExtra("tab", "2");
                intent.putExtra("other", "1");
                intent.putExtra("zone", zone);
            } else if (id == binding.cvThreeLogin.getId()) {
                intent.putExtra("device_value", "3");
                intent.putExtra("tab", "2");
                intent.putExtra("other", "1");
                intent.putExtra("zone", zone);
            } else if (id == binding.cvFourLogin.getId()) {
                intent.putExtra("device_value", "4");
                intent.putExtra("tab", "1");
                intent.putExtra("other", "2");
            }

            startActivity(intent);
        };

        binding.cvOneLogin.setOnClickListener(launchIntent);
        binding.cvTwoLogin.setOnClickListener(launchIntent);
        binding.cvThreeLogin.setOnClickListener(launchIntent);
        binding.cvFourLogin.setOnClickListener(launchIntent);
    }

    private void addTechnicians() {
        ApiHolder clientAtt = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        clientAtt.requestTechList().enqueue(new Callback<TechResponse>() {
            @Override
            public void onResponse(Call<TechResponse> call, Response<TechResponse> response) {
                try {
                    TechResponse workTypeResponse = response.body();
                    techList = workTypeResponse.gettechList();

                    techDetail.clear();
                    for (TechDetails tech : techList) {
                        techDetail.add(tech.getName());
                    }

                    List<String> nameList = new ArrayList<>();
                    nameList.add(" Select Technician");
                    nameList.addAll(techDetail);

                    adapter = new ArrayAdapter<>(requireContext(), R.layout.simple_custom_spinner_item, nameList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.newInClients.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<TechResponse> call, Throwable t) {
                try {
                    Snackbar snackbar = Snackbar.make(binding.getRoot(), "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(Color.RED);
                    TextView textView = snackbar.getView().findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception ignored) {
                }
            }
        });

        binding.newInClients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) return;
                i--;

                zone = techList.get(i).getZone();
                binding.addName.setText(techList.get(i).getName() + "'s ADD Performance");
                binding.addDetail.setVisibility(View.VISIBLE);
                binding.txtContentUnavailable.setVisibility(View.GONE);

                progressDialog.show();
                getDashBoardDetail();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void getDashBoardDetail() {
        viewModelAddDashboard.getAddCountsRepository(zone).observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                Toast.makeText(requireContext(), "Null response from server", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }

            if (response.getType() == 1 && response.getTechDashboardDetails() != null && !response.getTechDashboardDetails().isEmpty()) {
                TechDashboardDetail detail = response.getTechDashboardDetails().get(0);
                binding.setDashboardDetail(detail);

                try {
                    binding.addTime.setTextColor(Color.parseColor(detail.getColor().split(";")[0]));
                    binding.addValue.setTextColor(Color.parseColor(detail.getColor21().split(";")[0]));
                    binding.drsAdd.setTextColor(Color.parseColor(detail.getDrs_color().split(";")[0]));
                    binding.addTime.setTextColor(Color.parseColor(detail.getDrs_color21().split(";")[0]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(requireContext(), "Try Again - Connection timeout", Toast.LENGTH_LONG).show();
            }

            progressDialog.dismiss();
        });
    }

    void setDateAndTime() {
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);

        months = new SimpleDateFormat("MMM", Locale.ENGLISH).format(calen.getTime());
        current_date = months + " " + day + "," + year;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (checkConnection.isConnected()) {
            addTechnicians();
        } else {
            checkConnection.showConnectionErrorDialog();
        }
    }
}
