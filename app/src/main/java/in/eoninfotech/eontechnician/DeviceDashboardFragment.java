package in.eoninfotech.eontechnician;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;
import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.activity.DevicedashboardDetail;
import in.eoninfotech.eontechnician.activity.ReceiveDeviceActivity;
import in.eoninfotech.eontechnician.databinding.DeviceDashboardActivityBinding;
import in.eoninfotech.eontechnician.di.SharedPreferenceManager;
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.responses.DeviceCount;
import in.eoninfotech.eontechnician.viewModel.ViewModelDeviceDashboard;
import jakarta.inject.Inject;

@AndroidEntryPoint
public class DeviceDashboardFragment extends Fragment {

    @Inject
    SharedPreferenceManager sharedPref;
    @Inject
    CheckConnection checkConnection;
    private DeviceDashboardActivityBinding binding;
    private String usrname, zone, version;
    private AlertDialog progressDialog;

    private ArrayList<DeviceCount> countDetails = new ArrayList<>();
    private ViewModelDeviceDashboard viewModelDeviceDashboard;

    public DeviceDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout using ViewBinding
        binding = DeviceDashboardActivityBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize SharedPreferences
        usrname = sharedPref.getUsername();
        version = sharedPref.getVersionName();
        zone = sharedPref.getZone();

        // Initialize progress dialog
        progressDialog = new SpotsDialog(getActivity(), R.style.CustomCallSheet);

        // Initialize ViewModel
        viewModelDeviceDashboard = new ViewModelProvider(this).get(ViewModelDeviceDashboard.class);
        binding.setViewModelDeviceDashboard(viewModelDeviceDashboard);

        // Fetch data if connected
        if (checkConnection.isConnected()) {
            getDeviceCountDetails();
        } else {
            checkConnection.showConnectionErrorDialog();
        }
        // Set up click listeners
        setupClickListeners();

        return view;
    }

    private void setupClickListeners() {

        binding.linearTotal.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), DevicedashboardDetail.class);
            intent.putExtra("Status", "T");
            startActivity(intent);
        });

        binding.cvWorking.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), DevicedashboardDetail.class);
            intent.putExtra("Status", "W");
            startActivity(intent);
        });

        binding.cvFaulty.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), DevicedashboardDetail.class);
            intent.putExtra("Status", "F");
            startActivity(intent);
        });

        binding.inTransitTech.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), DevicedashboardDetail.class);
            intent.putExtra("Status", "ITT");
            startActivity(intent);
        });

        binding.inTransitStore.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), DevicedashboardDetail.class);
            intent.putExtra("Status", "ITS");
            startActivity(intent);
        });

        binding.receiveDevice.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ReceiveDeviceActivity.class);
            startActivity(intent);
        });

        binding.sendToEon.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MainActivityNew.class);
            intent.putExtra("intent", "toEon");
            startActivity(intent);
            requireActivity().finish();
        });

        binding.sendToFt.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MainActivityNew.class);
            intent.putExtra("intent", "toFT");
            startActivity(intent);
            requireActivity().finish();
        });
    }

    private void getDeviceCountDetails() {
        progressDialog.show();

        viewModelDeviceDashboard.getDashboardCountRepository(usrname)
                .observe(getViewLifecycleOwner(), movieResponse -> {

                    if (movieResponse == null) {
                        Toast.makeText(requireContext(), "Null response from server", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }

                    if (movieResponse.getType() == 1) {
                        countDetails = movieResponse.getDevice_count();

                        if (countDetails != null && !countDetails.isEmpty()) {
                            DeviceCount countList = countDetails.get(0);

                            // Update UI using ViewBinding
                            binding.setDeviceCount(countList);

                            binding.totalDevice.setText("Total Devices - " + countList.total);
                            binding.totalDrs.setText("Total DRS - " + countList.total_drs);
                            binding.inTransitStore.setText("Outgoing Material - " + countList.in_transit_store);
                            binding.inTransitTech.setText("Incoming Material - " + countList.in_transit_tech);
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}
