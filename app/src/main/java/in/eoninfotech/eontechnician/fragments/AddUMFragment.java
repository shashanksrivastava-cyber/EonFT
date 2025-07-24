package in.eoninfotech.eontechnician.fragments;

import static android.content.Context.MODE_PRIVATE;


import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.UnderMaintenanceVehicles;
import in.eoninfotech.eontechnician.adapters.UnderMaintenanceVehicleAdapter;
import in.eoninfotech.eontechnician.databinding.ActivityAddUmBinding;
import in.eoninfotech.eontechnician.databinding.FragmentLiveStatusNewEonBinding;
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.responses.ClientDetails;
import in.eoninfotech.eontechnician.responses.ClientLocationDetail;
import in.eoninfotech.eontechnician.responses.DeviceLiveStatus;
import in.eoninfotech.eontechnician.responses.MainClientList;
import in.eoninfotech.eontechnician.viewModel.ViewModelClientLocation;
import in.eoninfotech.eontechnician.viewModel.ViewModelLiveStatus;
import in.eoninfotech.eontechnician.viewModel.ViewModelMainClient;
import in.eoninfotech.eontechnician.viewModel.ViewModelSerialNo;
import in.eoninfotech.eontechnician.viewModel.ViewModelSubClient;
import in.eoninfotech.eontechnician.viewModel.ViewModelUM;

public class AddUMFragment extends Fragment {

    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String version, username, tech_id, mainClientId = "", clientId, s_clientname, id_dist, server_name, db_name, location_id, status = "0", reg_no;
    ActivityAddUmBinding binding;
    private ViewModelMainClient viewModelMainClient;
    private ViewModelSubClient viewModelSubClient;
    private ViewModelClientLocation viewModelClientLocation;
    private ViewModelUM viewModelVehicleUM;
    private CheckConnection checkConnection;
    private AlertDialog progressDialog;
    private final ArrayList<MainClientList> mainclientList = new ArrayList<>();
    private final ArrayList<String> mainClientDetail = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private boolean hasLoadedClients = false;
    private final ArrayList<ClientDetails> clientList = new ArrayList<>();
    private final ArrayList<String> clientDetail = new ArrayList<>();
    private final ArrayList<ClientLocationDetail> locationList = new ArrayList<>();
    private final ArrayList<String> locationDetail = new ArrayList<>();
    private boolean isFirstTimeLoaded = false;
    private ArrayList<UnderMaintenanceVehicles> list_change_values = new ArrayList<>();
    ArrayList<String> value_name = new ArrayList<>();
    private ArrayAdapter<String> mainClientAdapter;
    private ArrayAdapter<String> subClientAdapter;
    private ArrayAdapter<String> locationAdapter;
    private ArrayAdapter<String> umVehicleAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = ActivityAddUmBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        initUI();

        initViewModels();

        if (checkConnection.isConnected()) {
            fetchMainClients();
        } else {
            checkConnection.showConnectionErrorDialog();
        }
        return view;
    }

    private void fetchMainClients() {
        observeViewModels();
    }

    private void fetchSubClients() {
        observeViewModelsSubClient();
    }

    private void fetchLocations() {
        observeViewModelsLocation();
    }

    private void loadUMData() {
        observeViewModelUmVehicles();
    }

    private void submitData() {
        observeViewModelAddVehicle();
    }

    private void initUI() {
        sharedprefs = getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        tech_id = sharedprefs.getString("s_user_id", "");
        progressDialog = new SpotsDialog(requireContext(), R.style.CustomIncentive);
        checkConnection = new CheckConnection(requireContext());

        binding.mainClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                mainClientId = String.valueOf(mainclientList.get(i).getClient_Id());
                fetchSubClients();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.newInClients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                clientId = String.valueOf(clientList.get(i).getId_dist());
                s_clientname = clientList.get(i).getClient_Name();
                id_dist = clientList.get(i).getId_dist();
                server_name = clientList.get(i).getServer_name();
                db_name = clientList.get(i).getDb_name();
                fetchLocations();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.newInLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                location_id = String.valueOf(locationList.get(i).getLoc_Id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(server_name) || TextUtils.isEmpty(db_name) || TextUtils.isEmpty(id_dist)) {
                    Toast.makeText(getContext(), "Please Select Client", Toast.LENGTH_SHORT).show();
                } else {
                    loadUMData();
                }
            }
        });

        binding.addVehUm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checked = binding.deviceDetailListReceive.getCheckedItemPositions();
                StringBuilder selectedRegNos = new StringBuilder();

                for (int i = 0; i < list_change_values.size(); i++) {
                    if (checked.get(i)) {
                        if (selectedRegNos.length() > 0) {
                            selectedRegNos.append(",");
                        }
                        selectedRegNos.append(list_change_values.get(i).reg_no);
                    }
                }
                if (selectedRegNos.length() == 0) {
                    Toast.makeText(getContext(), "Please select at least one vehicle", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    reg_no = selectedRegNos.toString();
                    Log.e("SelectedVehicles", reg_no);
                    showAddVehicleConfirmationDialog();
                }
            }
        });
    }

    private void showAddVehicleConfirmationDialog() {
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                .setTitle("Confirm Addition")
                .setMessage("Are you sure you want to add the selected vehicle(s) to Under Maintenance?")
                .setIcon(R.drawable.ic_warning_black_24dp) // Optional, use a suitable icon
                .setPositiveButton("Yes", (dialog, which) -> {
                    dialog.dismiss();
                    // Proceed with your add vehicle logic here
                    submitData();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    private void initViewModels() {
        viewModelMainClient = new ViewModelProvider(this).get(ViewModelMainClient.class);
        viewModelSubClient = new ViewModelProvider(this).get(ViewModelSubClient.class);
        viewModelClientLocation = new ViewModelProvider(this).get(ViewModelClientLocation.class);
        viewModelVehicleUM = new ViewModelProvider(this).get(ViewModelUM.class);
    }

    private void observeViewModels() {
        progressDialog.show();
        viewModelMainClient.getMainClientRepository().observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                Toast.makeText(getActivity(), "Null response from server", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            if (response.getType() == 1) {
                Log.e("Main Client", "Api Call");
                hasLoadedClients = true;
                mainclientList.clear();
                mainclientList.addAll(response.getMain_client_list());
                mainClientDetail.clear();
                mainClientDetail.add("SELECT CLIENT");
                for (MainClientList client : mainclientList) {
                    mainClientDetail.add(client.getClient_Name());
                }
                mainClientAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_custom_spinner_item, mainClientDetail);
                mainClientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.mainClient.setAdapter(mainClientAdapter);
            } else {
                mainClientDetail.clear();
                mainClientDetail.add("NO DATA AVAILABLE");
            }
            progressDialog.dismiss();
        });
    }

    private void observeViewModelsSubClient() {
        progressDialog.show();
        viewModelSubClient.getSubClientRepository(mainClientId)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        Toast.makeText(getActivity(), "Null response from server", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }
                    if (response.getType() == 1) {
                        hasLoadedClients = true; // ✅ move this here after success
                        clientList.clear();
                        clientList.addAll(response.getClientList());

                        clientDetail.clear();
                        clientDetail.add("SELECT CLIENT");
                        for (ClientDetails client : clientList) {
                            clientDetail.add(client.getClient_Name());
                        }
                        subClientAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_custom_spinner_item, clientDetail);
                        subClientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.newInClients.setAdapter(subClientAdapter);
                    } else {
                        clientDetail.clear();
                        clientDetail.add("NO DATA AVAILABLE");
                    }
                    progressDialog.dismiss();
                });
    }

    private void observeViewModelsLocation() {
        progressDialog.show();
        viewModelClientLocation.getClientLocationRepository(id_dist, server_name, db_name).observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                Toast.makeText(getActivity(), "Null response from server", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            if (response.getType() == 1) {
                hasLoadedClients = true;
                locationList.clear();
                locationList.addAll(response.getClientLoc());
                locationDetail.clear();
                locationDetail.add("SELECT LOCATION");
                for (ClientLocationDetail loc : locationList) {
                    locationDetail.add(loc.getLoc_Name());
                }
                locationAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_custom_spinner_item, locationDetail);
                locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.newInLocations.setAdapter(locationAdapter);
            } else {
                locationDetail.clear();
                locationDetail.add("NO DATA AVAILABLE");
            }
            progressDialog.dismiss();
        });
    }

    private void observeViewModelUmVehicles() {
        progressDialog.show();
        viewModelVehicleUM.getUmRepository(mainClientId, clientId, location_id, status)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response.getType() == 1) {
                        try {
                            list_change_values = response.getUm_vehicles();
                            try {
                                try {
                                    value_name.clear();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (list_change_values.size() > 0) {
                                    for (int i = 0; i < list_change_values.size(); i++) {
                                        String regNo = list_change_values.get(i).reg_no;
                                        value_name.add((i + 1) + ". " + regNo);
                                    }
                                    if (list_change_values.size() > 5) {
                                        binding.deviceDetailListReceive.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * list_change_values.size() + 1));
                                    } else {
                                        binding.deviceDetailListReceive.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * list_change_values.size()));
                                    }
                                    umVehicleAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_list_item, value_name);
                                    binding.deviceDetailListReceive.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                                    binding.deviceDetailListReceive.setAdapter(umVehicleAdapter);
                                    binding.txtContentUnavailable.setVisibility(View.GONE);
                                    binding.deviceDetailListReceive.setVisibility(View.VISIBLE);
                                } else {
                                }
                            } catch (NullPointerException npe) {
                                npe.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        binding.txtContentUnavailable.setVisibility(View.VISIBLE);
                        binding.deviceDetailListReceive.setVisibility(View.GONE);
                    }
                    progressDialog.dismiss();
                });
    }

    private void observeViewModelAddVehicle() {
//        progressDialog.show();
//        viewModelVehicleUM.addInUM(mainClientId, clientId, location_id, "1", reg_no)
//                .observe(getViewLifecycleOwner(), response -> {
//                    if (response.getType() == 1) {
//                        Toast.makeText(getActivity(), "" + response.getMsg(), Toast.LENGTH_SHORT).show();
//                        //loadUMData();
//                        binding.deviceDetailListReceive.clearChoices();
//                        value_name.clear();
//                        list_change_values.clear();
//
//                        // ✅ Notify adapter to reset UI immediately
//                        if (umVehicleAdapter != null) {
//                            umVehicleAdapter.notifyDataSetChanged();
//                        }
//
//                        // ✅ Reload the fresh list
//                        loadUMData();
//                    } else {
//                        Toast.makeText(getActivity(), "" + response.getMsg(), Toast.LENGTH_SHORT).show();
//                    }
//                    progressDialog.dismiss();
//                });
    }
}
