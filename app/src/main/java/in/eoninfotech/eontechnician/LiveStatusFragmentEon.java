package in.eoninfotech.eontechnician;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.databinding.FragmentLiveStatusNewEonBinding;
import in.eoninfotech.eontechnician.fragments.LiveStatusAdapterNew;
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.responses.ClientLocationDetail;
import in.eoninfotech.eontechnician.responses.DeviceLiveStatus;
import in.eoninfotech.eontechnician.responses.MainClientList;
import in.eoninfotech.eontechnician.responses.VehicleTypeDetail;
import in.eoninfotech.eontechnician.viewModel.ViewModelClientLocation;
import in.eoninfotech.eontechnician.viewModel.ViewModelLiveStatus;
import in.eoninfotech.eontechnician.viewModel.ViewModelMainClient;
import in.eoninfotech.eontechnician.viewModel.ViewModelSerialNo;
import in.eoninfotech.eontechnician.viewModel.ViewModelSubClient;

public class LiveStatusFragmentEon extends Fragment {

    private FragmentLiveStatusNewEonBinding binding;
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
    private ViewModelSerialNo viewModelSerialNo;
    private final ArrayList<MainClientList> mainclientList = new ArrayList<>();
    private final ArrayList<LiveStatusSerialNo> clientList = new ArrayList<>();
    private final ArrayList<ClientLocationDetail> locationList = new ArrayList<>();
    private final ArrayList<DeviceLiveStatus> deviceLiveStatuses = new ArrayList<>();
    private final ArrayList<VehicleTypeDetail> vehicleList = new ArrayList<>();
    private final ArrayList<String> mainClientDetail = new ArrayList<>();
    private final ArrayList<String> clientDetail = new ArrayList<>();
    private final ArrayList<String> locationDetail = new ArrayList<>();
    private final ArrayList<String> vehicleDetail = new ArrayList<>();

    private String formattedSerials = " ", tech_id="",server_name = "", db_name = "", id_dist = "", depo_id = "", clientId = "", mainClientId = "",db = "",server="", connection_status = "", veh_type = "";

    private CheckConnection checkConnection;
    private boolean hasLoadedClients = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLiveStatusNewEonBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        initUI();
        initViewModels();
        observeViewModels();
        observeViewModelsSerialNo();
        observeViewModelsLiveStatus();

        if (checkConnection.isConnected()) {
            fetchMainClients();
        } else {
            checkConnection.showConnectionErrorDialog();
        }
        return view;
    }

    private void initUI() {
        sharedPrefs = requireActivity().getSharedPreferences("login_user_pass", Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();

        tech_id = sharedPrefs.getString("s_user_id", "");

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
                if (i == 0) return;
                String selectedId = String.valueOf(mainclientList.get(i - 1).getClient_Id());
                if (!selectedId.equals(mainClientId)) {
                    mainClientId = selectedId;
                    db_name = mainclientList.get(i-1).getDb();
                    server_name = mainclientList.get(i-1).getServer();
                    hasLoadedClients = false;
                    fetchSerialNo();
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.newInClients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                if (i == 0) return; // Skip if "SELECT SERIAL NO." is selected

                if (i == 1) { // "ALL" is selected (index 1 because it's the second item)
                    formattedSerials = getProperlyFormattedSerialNumbers();
                    Log.d("FormattedSerials", "Formatted: " + formattedSerials);
                    // Output will be: '11285','3903','7896'

                    // Use this formatted string in your API call
                    //fetchDataWithFormattedSerials(formattedSerials);
                } else {
                    // Single serial number selected
                    LiveStatusSerialNo client = clientList.get(i - 2); // -2 because we have "SELECT SERIAL NO." and "ALL" at the beginning
                    formattedSerials = "'" + client.serial_no + "'";
                    Log.d("LiveStatus", "Selected Serial: " + formattedSerials);

                    // Proceed with single serial number logic
                    //fetchLocations();
                }
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
            if (checkConnection.isConnected()) {
                loadLiveStatusData();
            } else {
                checkConnection.showConnectionErrorDialog();
            }
        });
    }

    private String getProperlyFormattedSerialNumbers() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < clientList.size(); i++) {
            // Add single quote + serial number + single quote
            sb.append("'").append(clientList.get(i).serial_no).append("'");
            // Add comma only if it's not the last item
            if (i < clientList.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private void initViewModels() {
        viewModelMainClient = new ViewModelProvider(this).get(ViewModelMainClient.class);
        viewModelSubClient = new ViewModelProvider(this).get(ViewModelSubClient.class);
        viewModelClientLocation = new ViewModelProvider(this).get(ViewModelClientLocation.class);
        viewModelLiveStatus = new ViewModelProvider(this).get(ViewModelLiveStatus.class);
        viewModelSerialNo = new ViewModelProvider(this).get(ViewModelSerialNo.class);
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
                //Toast.makeText(getContext(), "Failed to load main clients", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        });
    }

    private void observeViewModelsSerialNo() {
        viewModelSerialNo.getSerialNoDetails(tech_id,mainClientId).observe(getViewLifecycleOwner(), response -> {
            if (!hasLoadedClients) {
                hasLoadedClients = true;
                if (response == null) {
                    Toast.makeText(getActivity(), "Null response from server", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                    return;
                }
                if (response.getType() == 1) {
                    clientList.clear();
                    clientList.addAll(response.getLive_status_serial_no());
                    clientDetail.clear();
                    clientDetail.add("SELECT SERIAL NO.");
                    clientDetail.add("ALL");
                    for (LiveStatusSerialNo client : clientList) {
                        clientDetail.add(client.serial_no);
                    }
                    adapter = new ArrayAdapter<>(getContext(), R.layout.simple_custom_spinner_item, clientDetail);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.newInClients.setAdapter(adapter);
                } else {
                    clientList.clear();
                    clientDetail.clear();
                    clientDetail.add("NO DATA AVAILABLE");
                }
            }
            progressDialog.dismiss();
        });
    }

    private void observeViewModelsLiveStatus() {
        viewModelLiveStatus.getLiveStatusRepository(server_name, db_name, id_dist, depo_id, connection_status, veh_type,formattedSerials)
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
                        liveStatusAdapterNew = new LiveStatusAdapterNew(getContext(), deviceLiveStatuses, server_name, db_name);
                        binding.recyclerView.setAdapter(liveStatusAdapterNew);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        binding.txtContentUnavailable.setVisibility(View.GONE);
                        progressDialog.hide();
                    } else {
                        progressDialog.hide();
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.txtContentUnavailable.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void fetchMainClients() {
        progressDialog.show();
        viewModelMainClient.getMainClientRepository(); // assumed method call
    }

    private void fetchSerialNo() {
        progressDialog.show();
        viewModelSerialNo.getSerialNoDetails(tech_id,mainClientId);
        //observeViewModelsSerialNo();// assumed method call
    }
    private void loadLiveStatusData() {
       progressDialog.show();
        viewModelLiveStatus.getLiveStatusRepository(server_name, db_name, id_dist, depo_id, connection_status, veh_type,formattedSerials);// assumed method call
    }

    private void refresh() {
        loadLiveStatusData();
    }

}
