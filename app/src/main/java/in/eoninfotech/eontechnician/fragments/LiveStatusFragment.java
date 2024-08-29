package in.eoninfotech.eontechnician.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import in.eoninfotech.eontechnician.ActivityDetailAdapter;
import in.eoninfotech.eontechnician.LiveFaultDataAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.responses.ClientDetails;
import in.eoninfotech.eontechnician.responses.ClientLocationDetail;
import in.eoninfotech.eontechnician.responses.ClientLocationResponse;
import in.eoninfotech.eontechnician.responses.ClientResponse;
import in.eoninfotech.eontechnician.responses.CollectedItemsResponse;
import in.eoninfotech.eontechnician.responses.DeviceLiveStatus;
import in.eoninfotech.eontechnician.responses.DisconnectionResponse;
import in.eoninfotech.eontechnician.responses.FaultResponse;
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
import in.eoninfotech.eontechnician.viewModel.ViewModelSubClient;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by root on 22/11/18.
 */

public class LiveStatusFragment extends Fragment implements ClientListener {

    View v;
    public RecyclerView recyclerView;
    private TextView txt_content_unavailable;
    public SwipeRefreshLayout refreshLayout;
    public LinearLayoutManager layoutManager;
    private ProgressDialog pDialog;
    Button submit;
    ArrayList<String> vehicleDetail = new ArrayList<>();
    ArrayList<VehicleTypeDetail> vehicleList = new ArrayList<>();
    ArrayList<ClientDetails> clientList = new ArrayList<>();
    ArrayList<String> clientDetail = new ArrayList<>();
    String username, version,id_dist="",server_name="",db_name="",clientId,s_clientname = "SELECT CLIENT",depo_id="",connection_status="D",veh_type="";
    ArrayList<MyPojo> liveFault = new ArrayList<>();
    private LiveFaultDataAdapter liveFaultDataAdapter;
    NewInstallmentController newInstallmentController;
    SharedPreferences sharedprefs;
    ArrayList<ClientLocationDetail> locationList = new ArrayList<>();
    ArrayList<String> locationDetail = new ArrayList<>();
    SharedPreferences.Editor editor;
    MySearchableSpinner client, location,vehicleType,device_status;
    CheckConnection chk;
    ProgressBar progressBar;
    ArrayAdapter<String> adapter;
    ArrayList<DeviceLiveStatus> deviceLiveStatuses = new ArrayList<>();
    LiveStatusAdapterNew liveStatusAdapterNew;
    ViewModelSubClient viewModelSubClient;
    ViewModelClientLocation viewModelClientLocation;
    ViewModelLiveStatus viewModelLiveStatus;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_live_status_new, container, false);

        sharedprefs = this.getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version","");
        chk = new CheckConnection(v.getContext());
        recyclerView = v.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        client = v.findViewById(R.id.new_in_clients);
        vehicleType = v.findViewById(R.id.new_in_vehicleType);
        location = v.findViewById(R.id.new_in_locations);
        recyclerView.setLayoutManager(layoutManager);
        refreshLayout = v.findViewById(R.id.refresh);
        progressBar = v.findViewById(R.id.progressBar);
        submit = v.findViewById(R.id.submit);
        device_status = v.findViewById(R.id.device_status);
        vehicleType = v.findViewById(R.id.vehicleType);
        txt_content_unavailable = v.findViewById(R.id.txt_content_unavailable);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE,Color.GREEN);
        refreshLayout.setOnRefreshListener(this::refresh);
        refreshLayout.setRefreshing(false);
        ShowProgressBar(false);
        newInstallmentController = new NewInstallmentController();
        if (chk.isConnected()) {
            addclients();
        } else {
            chk.showConnectionErrorDialog();
        }
        device_status.setSelection(2);

        device_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(device_status.getSelectedItemPosition()==0){
                    connection_status = "A";
                }else if(device_status.getSelectedItemPosition()==1){
                    connection_status = "C";
                }else {
                    connection_status="D";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        vehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                veh_type = String.valueOf(vehicleList.get(i).getVehicle_Id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        client.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                clientId = String.valueOf(clientList.get(i).getClient_Id());
                s_clientname = clientList.get(i).getClient_Name();
                id_dist = clientList.get(i).getId_dist();
                server_name = clientList.get(i).getServer_name();
                db_name = clientList.get(i).getDb_name();
                addLocation();
                location.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                depo_id = String.valueOf((locationList.get(i).getLoc_Id()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(server_name.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please Select Client", Toast.LENGTH_SHORT).show();
                }else if(db_name.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please Select Client", Toast.LENGTH_SHORT).show();
                }else if(id_dist.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please Select Client", Toast.LENGTH_SHORT).show();
                }else {
                    loadData();
                }
            }
        });

        return v;
    }

    private void addLocation() {

        viewModelClientLocation= ViewModelProviders.of(this).get(ViewModelClientLocation.class);
        viewModelClientLocation.getClientLocationRepository(id_dist,server_name,db_name).observe(this, movieResponse -> {
            locationList = movieResponse.getClientLoc();
            if(movieResponse.getType()==1){
                try {
                    locationDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                locationDetail.add("SELECT LOCATION");
                for (int i = 0; i < locationList.size(); i++) {
                    locationDetail.add(locationList.get(i).getLoc_Name());
                }
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, locationDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                location.setAdapter(adapter);
            }else {
                Toast.makeText(getActivity(), "Something Went Wrong!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addclients() {
        viewModelSubClient= ViewModelProviders.of(this).get(ViewModelSubClient.class);
        viewModelSubClient.getSubClientRepository("").observe(this, movieResponse -> {
            clientList = movieResponse.getClientList();
            if(movieResponse.getType()==1){
                try {
                    clientDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                clientDetail.add(" SELECT CLIENT");
                for (int i = 0; i < clientList.size(); i++) {
                    clientDetail.add(clientList.get(i).getClient_Name());
                }
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, clientDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                client.setAdapter(adapter);
            }else {
                Toast.makeText(getActivity(), "Something Went Wrong!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addVehType() {
        newInstallmentController.reqeuestvehicleType(this);
    }

    private void loadData() {
        try {
            pDialog = K.createProgressDialog(getActivity());
            pDialog.setMessage("Loading");
            pDialog.show();
            pDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewModelLiveStatus= ViewModelProviders.of(this).get(ViewModelLiveStatus.class);
        viewModelLiveStatus.getLiveStatusRepository(server_name,db_name,id_dist,depo_id,connection_status,veh_type).observe(this, movieResponse -> {
            deviceLiveStatuses = movieResponse.getData();
            if(movieResponse.getType()==1){
                liveStatusAdapterNew = new LiveStatusAdapterNew(getContext(),deviceLiveStatuses);
                recyclerView.setAdapter(liveStatusAdapterNew);
                recyclerView.setVisibility(View.VISIBLE);
                txt_content_unavailable.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                pDialog.dismiss();
            }else {
                refreshLayout.setRefreshing(false);
                pDialog.dismiss();
                txt_content_unavailable.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
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
        try {
            vehicleList = response.getVehicletypeList();
            try {
                try {
                    vehicleDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                vehicleDetail.add("SELECT VEHICLE TYPE");
                for (int i = 0; i < vehicleList.size(); i++) {
                    vehicleDetail.add(vehicleList.get(i).getVehicle_Name());
                }
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, vehicleDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                vehicleType.setAdapter(adapter);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    }

    @Override
    public void vtsAccResponses(MainResponse response) {

    }

    private void ShowProgressBar(boolean show) {
        try {
            if (show) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

