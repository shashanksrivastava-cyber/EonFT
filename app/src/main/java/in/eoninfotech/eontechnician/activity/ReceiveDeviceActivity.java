package in.eoninfotech.eontechnician.activity;

import androidx.appcompat.app.AppCompatActivity;
import in.eoninfotech.eontechnician.NonScrollListView;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.ClientDetails;
import in.eoninfotech.eontechnician.Responses.ClientLocationResponse;
import in.eoninfotech.eontechnician.Responses.ClientResponse;
import in.eoninfotech.eontechnician.Responses.CollectedItemsResponse;
import in.eoninfotech.eontechnician.Responses.DisconnectionResponse;
import in.eoninfotech.eontechnician.Responses.FaultList;
import in.eoninfotech.eontechnician.Responses.FaultResponse;
import in.eoninfotech.eontechnician.Responses.MainResponse;
import in.eoninfotech.eontechnician.Responses.NotAvailActivityResponse;
import in.eoninfotech.eontechnician.Responses.PaymentMethodResponse;
import in.eoninfotech.eontechnician.Responses.RemovalActivityResponse;
import in.eoninfotech.eontechnician.Responses.RemovalResponse;
import in.eoninfotech.eontechnician.Responses.ReplaceReason;
import in.eoninfotech.eontechnician.Responses.SimOperatorResponse;
import in.eoninfotech.eontechnician.Responses.SimReplaceResponse;
import in.eoninfotech.eontechnician.Responses.VTSResponse;
import in.eoninfotech.eontechnician.Responses.VehNotAvailReasonResponse;
import in.eoninfotech.eontechnician.Responses.VehicleTypeResponse;
import in.eoninfotech.eontechnician.Responses.WorkTypeResponse;
import in.eoninfotech.eontechnician.callbacks.ClientListener;
import in.eoninfotech.eontechnician.controllers.NewInstallmentController;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.robinhood.ticker.TickerView;

import java.util.ArrayList;

public class ReceiveDeviceActivity extends AppCompatActivity implements ClientListener {

    private TickerView total_device;
    MySearchableSpinner spn_transit, spn_items;
    NonScrollListView device_detail_list;
    EditText other_material, remarks;
    Button btn_accept, btn_reject;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String version, username;
    NewInstallmentController newInstallmentController;
    ArrayList<ClientDetails> clientList = new ArrayList<>();
    ArrayList<String> clientDetail = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayList<FaultList> list_change_values = new ArrayList<>();
    ArrayList<String> value_name = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_device);
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");

        initView();
    }
    private void initView() {

        total_device = findViewById(R.id.total_device);
        spn_transit = findViewById(R.id.spn_transit);
        spn_items= findViewById(R.id.spn_items);
        device_detail_list = findViewById(R.id.device_detail_list);
        other_material = findViewById(R.id.other_material);
        btn_accept = findViewById(R.id.btn_accept);
        btn_reject = findViewById(R.id.btn_reject);
        remarks= findViewById(R.id.remarks);
        newInstallmentController = new NewInstallmentController();

        addclients();
        addLocation();
        getFaultList();
        total_device.setPreferredScrollingDirection(TickerView.ScrollingDirection.DOWN);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void getFaultList() {
        newInstallmentController.reqeuestFaultList(this);
    }

    private void addLocation() {

    }

    private void addclients() {
        newInstallmentController.reqeuestClientList(this);
    }
    @Override
    public void clientResponse(ClientResponse response) {
        try{
            clientList = response.getClientList();
            try {
                try {
                    clientDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }   clientDetail.add(" SELECT CLIENT");
                for (int i = 0; i < clientList.size(); i++) {
                    clientDetail.add(clientList.get(i).getClient_Name());
                }
                adapter = new ArrayAdapter<String>(this, R.layout.simple_custom_spinner_item, clientDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_transit.setAdapter(adapter);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
            list_change_values = response.getFaultLists();
            try {
                try {
                    value_name.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (list_change_values.size() > 0) {
                    for (int i = 0; i < list_change_values.size(); i++) {
                        value_name.add(list_change_values.get(i).getName());
                    }
                    if (list_change_values.size() > 5) {
                        device_detail_list.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * list_change_values.size() + 1));
                    } else {
                        device_detail_list.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * list_change_values.size()));
                    }
                    adapter = new ArrayAdapter<String>(this, R.layout.simple_custom_list_item, value_name);
                    device_detail_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    device_detail_list.setAdapter(adapter);
                } else {
                }
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}