package in.eoninfotech.eontechnician;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.robinhood.ticker.TickerView;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
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

import static android.content.Context.MODE_PRIVATE;

public class MaterialReturnFragment extends Fragment implements ClientListener {

    View v;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.activity_return_device, container, false);
        sharedprefs = getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");

        initView();

        return v;
    }

    private void initView() {
        total_device = v.findViewById(R.id.total_device);
        spn_transit = v.findViewById(R.id.spn_transit);
        spn_items= v.findViewById(R.id.spn_items);
        device_detail_list = v.findViewById(R.id.device_detail_list);
        other_material = v.findViewById(R.id.other_material);
        btn_accept = v.findViewById(R.id.btn_accept);
        btn_reject = v.findViewById(R.id.btn_reject);
        remarks= v.findViewById(R.id.remarks);
        newInstallmentController = new NewInstallmentController();
        getFaultList();
    }

    private void getFaultList() {
        newInstallmentController.reqeuestFaultList(this);
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
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_list_item, value_name);
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
