package in.eoninfotech.eontechnician.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import dagger.hilt.android.AndroidEntryPoint;
import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.activity.DevicedashboardDetail;
import in.eoninfotech.eontechnician.activity.FaultyDevicesActivity;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.activity.LoginActivityNew;
import in.eoninfotech.eontechnician.di.SharedPreferenceManager;
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.responses.DashBoardResponse;
import in.eoninfotech.eontechnician.responses.TechDashboardDetail;
import in.eoninfotech.eontechnician.databinding.DashboardNewBinding;
import in.eoninfotech.eontechnician.viewModel.ViewModelAddDashboard;

import jakarta.inject.Inject;


@AndroidEntryPoint
public class DashBoardFragment extends Fragment {
    @Inject
    SharedPreferenceManager sharedPref;
    @Inject
    CheckConnection checkConnection;
    View v;
    DashboardNewBinding binding;
    String usrname, current_date, s_time, zone, version, months;
    int year, day, month, hour, minutes;
    Calendar calen = Calendar.getInstance();
    ArrayList<Float> yData = new ArrayList<>();
    ArrayList<TechDashboardDetail> dashboardList = new ArrayList<>();
    private AlertDialog progressDialog;
    private boolean hasLoadedOnce = false;
    public static final int[] BRIGHT_COLORS = {
            Color.parseColor("#D32F2F"), Color.parseColor("#F44336"), Color.parseColor("#FFC03C")};
    ViewModelAddDashboard viewModelAddDashboard;
    boolean isDashboardLoaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DashboardNewBinding.inflate(getLayoutInflater(), container, false);
        usrname = sharedPref.getUsername();
        version = sharedPref.getVersionName();
        zone = sharedPref.getZone();

        progressDialog = new SpotsDialog(getActivity(), R.style.CustomIncentive);

        setDateAndTime();

        viewModelAddDashboard = new ViewModelProvider(this).get(ViewModelAddDashboard.class);
        binding.setViewModelAddDashboard(viewModelAddDashboard);

        binding.swipeRefresh.setOnRefreshListener(() -> {
            isDashboardLoaded = false;
            if (checkConnection.isConnected()) {
                getDashBoardDetail();
                isDashboardLoaded = true;
            } else {
                checkConnection.showConnectionErrorDialog();
                binding.swipeRefresh.setRefreshing(false);
            }
        });

        setupClickListeners();

        return binding.getRoot();
    }

    private void setupClickListeners() {
        View.OnClickListener launchIntent = view -> {
            Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
            int id = view.getId();

            if (id == binding.cvOneLogin.getId()) {
                intent.putExtra("device_value", "2");
            } else if (id == binding.cvTwoLogin.getId()) {
                intent.putExtra("device_value", "1");
            } else if (id == binding.cvThreeLogin.getId()) {
                intent.putExtra("device_value", "3");
            } else if (id == binding.cvFourLogin.getId()) {
                intent.putExtra("device_value", "4");
            }

            intent.putExtra("tab", "1");
            intent.putExtra("other", "2");
            startActivity(intent);
        };

        binding.cvOneLogin.setOnClickListener(launchIntent);
        binding.cvTwoLogin.setOnClickListener(launchIntent);
        binding.cvThreeLogin.setOnClickListener(launchIntent);
        binding.cvFourLogin.setOnClickListener(launchIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        isDashboardLoaded = false;
        if (checkConnection.isConnected()) {
            getDashBoardDetail();
            isDashboardLoaded = true;
        } else {
            checkConnection.showConnectionErrorDialog();
            binding.swipeRefresh.setRefreshing(false);
        }
        //getDashBoardDetail();
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (!hasLoadedOnce) {               // ← only load on first resume
//            hasLoadedOnce = true;
//            if (checkConnection.isConnected()) {
//                getDashBoardDetail();
//            } else {
//                checkConnection.showConnectionErrorDialog();
//                binding.swipeRefresh.setRefreshing(false);
//            }
//        }
//    }

    private void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    void getDashBoardDetail() {
        progressDialog.show();
        try {
            viewModelAddDashboard.getAddCountsRepository(zone).observe(getViewLifecycleOwner(), movieResponse -> {
                if (movieResponse == null) {
                    Toast.makeText(getActivity(), "Null response from server", Toast.LENGTH_SHORT).show();
                    hideProgress();
                    return;
                }
                try {
                    if (movieResponse.getType() == 1) {
                        dashboardList = movieResponse.getTechDashboardDetails();
                        if (dashboardList != null && !dashboardList.isEmpty()) {
                            TechDashboardDetail dashboardDetail = dashboardList.get(0);
                            binding.setDashboardDetail(dashboardDetail);

                            String color1 = dashboardDetail.getColor().split(";")[0];
                            binding.addTime.setTextColor(Color.parseColor(color1));

                            String color2 = dashboardDetail.getColor21().split(";")[0];
                            binding.addValue.setTextColor(Color.parseColor(color2));

                            String drsColor = dashboardDetail.getDrs_color().split(";")[0];
                            binding.drsAdd.setTextColor(Color.parseColor(drsColor));

                            String drsColor21 = dashboardDetail.getDrs_color21().split(";")[0];
                            binding.addTime.setTextColor(Color.parseColor(drsColor21));
                        }
                    } else {
                        Toast.makeText(getActivity(), "Try Again - Connection timeout", Toast.LENGTH_LONG).show();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } finally {
                    hideProgress();
                    binding.swipeRefresh.setRefreshing(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            hideProgress();
            binding.swipeRefresh.setRefreshing(false);
        }
    }

    void setDateAndTime() {
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);
        hour = calen.get(Calendar.HOUR_OF_DAY);
        minutes = calen.get(Calendar.MINUTE);
        month += 1;

        months = new SimpleDateFormat("MMM", Locale.ENGLISH).format(calen.getTime());
        current_date = months + " " + day + "," + year;
        binding.curntDate.setText(current_date);

        SimpleDateFormat dateFormatt = new SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.ENGLISH);
        s_time = dateFormatt.format(calen.getTime());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgress();
    }
}

