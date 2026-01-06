package in.eoninfotech.eontechnician.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.ActivityDetailAdapter;
import in.eoninfotech.eontechnician.LiveFaultDataAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.databinding.FragmentLiveStatusNewBinding;
import in.eoninfotech.eontechnician.di.SharedPreferenceManager;
import in.eoninfotech.eontechnician.responses.ClientDetails;
import in.eoninfotech.eontechnician.responses.ClientLocationDetail;
import in.eoninfotech.eontechnician.responses.ClientLocationResponse;
import in.eoninfotech.eontechnician.responses.ClientResponse;
import in.eoninfotech.eontechnician.responses.CollectedItemsResponse;
import in.eoninfotech.eontechnician.responses.DeviceLiveStatus;
import in.eoninfotech.eontechnician.responses.DisconnectionResponse;
import in.eoninfotech.eontechnician.responses.FaultResponse;
import in.eoninfotech.eontechnician.responses.MainClientList;
import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.responses.MyPojo;
import in.eoninfotech.eontechnician.responses.NotAvailActivityResponse;
import in.eoninfotech.eontechnician.responses.PaymentMethodResponse;
import in.eoninfotech.eontechnician.responses.RemovalActivityResponse;
import in.eoninfotech.eontechnician.responses.RemovalResponse;
import in.eoninfotech.eontechnician.responses.ReplaceReason;
import in.eoninfotech.eontechnician.responses.SimOperatorResponse;
import in.eoninfotech.eontechnician.responses.SimReplaceResponse;
import in.eoninfotech.eontechnician.responses.VTSResponse;
import in.eoninfotech.eontechnician.responses.VehNotAvailReasonResponse;
import in.eoninfotech.eontechnician.responses.VehicleTypeDetail;
import in.eoninfotech.eontechnician.responses.VehicleTypeResponse;
import in.eoninfotech.eontechnician.responses.WorkTypeResponse;
import in.eoninfotech.eontechnician.callbacks.ClientListener;
import in.eoninfotech.eontechnician.controllers.NewInstallmentController;
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.viewModel.ViewModelActivityDetails;
import in.eoninfotech.eontechnician.viewModel.ViewModelClientLocation;
import in.eoninfotech.eontechnician.viewModel.ViewModelLiveStatus;
import in.eoninfotech.eontechnician.viewModel.ViewModelMainClient;
import in.eoninfotech.eontechnician.viewModel.ViewModelSubClient;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import jakarta.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by root on 22/11/18.
 */

public class LiveStatusFragment extends Fragment{

    @Inject
    SharedPreferenceManager sharedPref;
    @Inject
    CheckConnection checkConnection;
    private FragmentLiveStatusNewBinding binding;
    private AlertDialog progressDialog;
    private ProgressDialog pDialog;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;
    private LiveStatusAdapterNew liveStatusAdapterNew;
    private ArrayAdapter<String> adapter;
    private ViewModelMainClient viewModelMainClient;
    private ViewModelSubClient viewModelSubClient;
    private ViewModelClientLocation viewModelClientLocation;
    private ViewModelLiveStatus viewModelLiveStatus;
    private final ArrayList<MainClientList> mainclientList = new ArrayList<>();
    private final ArrayList<ClientDetails> clientList = new ArrayList<>();
    private final ArrayList<ClientLocationDetail> locationList = new ArrayList<>();
    private final ArrayList<DeviceLiveStatus> deviceLiveStatuses = new ArrayList<>();
    private final ArrayList<VehicleTypeDetail> vehicleList = new ArrayList<>();
    private final ArrayList<String> mainClientDetail = new ArrayList<>();
    private final ArrayList<String> clientDetail = new ArrayList<>();
    private final ArrayList<String> locationDetail = new ArrayList<>();
    private final ArrayList<String> vehicleDetail = new ArrayList<>();
    private String server_name = "", db_name = "", id_dist = "", depo_id = "", clientId = "", mainClientId = "", connection_status = "D", veh_type = "";

    private boolean hasLoadedClients = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLiveStatusNewBinding.inflate(inflater, container, false);
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

    private void initUI() {

        checkConnection = new CheckConnection(requireContext());

        progressDialog = new SpotsDialog(requireContext(), R.style.CustomIncentive);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        binding.refresh.setOnRefreshListener(this::refresh);
        binding.refresh.setRefreshing(false);
        binding.progressBar.setVisibility(View.GONE);

        binding.deviceStatus.setSelection(2);
        binding.deviceStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                connection_status = position == 0 ? "A" : position == 1 ? "C" : "D";
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });

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

            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.newInClients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) return;
                ClientDetails client = clientList.get(i - 1);
                clientId = String.valueOf(client.getClient_Id());
                id_dist = client.getId_dist();
                server_name = client.getServer_name();
                db_name = client.getDb_name();
                fetchLocations();
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.newInLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) return;
                depo_id = String.valueOf(locationList.get(i - 1).getLoc_Id());
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.submit.setOnClickListener(v -> {
            if (TextUtils.isEmpty(server_name) || TextUtils.isEmpty(db_name) || TextUtils.isEmpty(id_dist)) {
                Toast.makeText(getContext(), "Please Select Client", Toast.LENGTH_SHORT).show();
            } else {
                loadLiveStatusData();
            }
        });
    }

    private void initViewModels() {
        viewModelMainClient = new ViewModelProvider(this).get(ViewModelMainClient.class);
        viewModelSubClient = new ViewModelProvider(this).get(ViewModelSubClient.class);
        viewModelLiveStatus = new ViewModelProvider(this).get(ViewModelLiveStatus.class);
        viewModelClientLocation = new ViewModelProvider(this).get(ViewModelClientLocation.class);
    }

    private void observeViewModels() {
        viewModelMainClient.getMainClientRepository().observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                Toast.makeText(getActivity(), "Null response from server", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
                return;
            }
            if (response.getType() == 1) {
                mainclientList.clear();
                mainclientList.addAll(response.getMain_client_list());
                mainClientDetail.clear();
                mainClientDetail.add("SELECT CLIENT");
                for (MainClientList client : mainclientList) {
                    mainClientDetail.add(client.getClient_Name());
                }
                adapter = new ArrayAdapter<>(getContext(), R.layout.simple_custom_spinner_item, mainClientDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.mainClient.setAdapter(adapter);
            } else {
                mainClientDetail.clear();
                mainClientDetail.add("NO DATA AVAILABLE");
            }
            progressDialog.dismiss();
        });
    }

    private void observeViewModelsSubClient() {
        viewModelSubClient.getSubClientRepository(mainClientId)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        Toast.makeText(getActivity(), "Null response from server", Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
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
                        adapter = new ArrayAdapter<>(getContext(), R.layout.simple_custom_spinner_item, clientDetail);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.newInClients.setAdapter(adapter);
                        fetchLocations();
                    } else {
                        clientDetail.clear();
                        clientDetail.add("NO DATA AVAILABLE");
                        fetchLocations();
                    }
                    progressDialog.hide();
                });
    }

    private void observeViewModelsLocation() {
        viewModelClientLocation.getClientLocationRepository(id_dist, server_name, db_name).observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                Toast.makeText(getActivity(), "Null response from server", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
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
                adapter = new ArrayAdapter<>(getContext(), R.layout.simple_custom_spinner_item, locationDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.newInLocations.setAdapter(adapter);
            } else {
                locationDetail.clear();
                locationDetail.add("NO DATA AVAILABLE");
            }
            progressDialog.dismiss();
        });
    }

    private void observeViewModelsLiveStatus() {

        viewModelLiveStatus.getLiveStatusRepository(server_name, db_name, id_dist, depo_id, connection_status, veh_type)
                .observe(getViewLifecycleOwner(), response -> {
                    binding.refresh.setRefreshing(false);
                    if (pDialog != null) pDialog.dismiss();
                    if (response == null) {
                        Toast.makeText(getActivity(), "Null response from server", Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                        return;
                    }
                    if (response.getType() == 1) {
                        deviceLiveStatuses.clear();
                        deviceLiveStatuses.addAll(response.getData());
                        liveStatusAdapterNew = new LiveStatusAdapterNew(getContext(), deviceLiveStatuses,server_name,db_name);
                        binding.recyclerView.setAdapter(liveStatusAdapterNew);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        binding.txtContentUnavailable.setVisibility(View.GONE);
                        progressDialog.hide();
                    } else {
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.txtContentUnavailable.setVisibility(View.VISIBLE);
                        binding.txtContentUnavailable.setText(response.getMsg());
                        progressDialog.hide();
                    }
                });
    }

    private void fetchMainClients() {
        progressDialog.show();
        observeViewModels();
    }

    private void fetchSubClients() {
        progressDialog.show();
        observeViewModelsSubClient();
    }

    private void fetchLocations() {
        progressDialog.show();
        observeViewModelsLocation();
    }

    private void loadLiveStatusData() {
        progressDialog.show();
        observeViewModelsLiveStatus();
    }

    private void refresh() {
        loadLiveStatusData();
    }

}

