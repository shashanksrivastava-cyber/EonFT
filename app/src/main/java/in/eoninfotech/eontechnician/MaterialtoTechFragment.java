package in.eoninfotech.eontechnician;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.robinhood.ticker.TickerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
import in.eoninfotech.eontechnician.Responses.TechDetails;
import in.eoninfotech.eontechnician.Responses.TechResponse;
import in.eoninfotech.eontechnician.Responses.VTSResponse;
import in.eoninfotech.eontechnician.Responses.VehNotAvailReasonResponse;
import in.eoninfotech.eontechnician.Responses.VehicleTypeResponse;
import in.eoninfotech.eontechnician.Responses.WorkTypeResponse;
import in.eoninfotech.eontechnician.callbacks.ClientListener;
import in.eoninfotech.eontechnician.controllers.NewInstallmentController;
import in.eoninfotech.eontechnician.fragments.ViewStockFragment;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MaterialtoTechFragment extends Fragment implements ClientListener {

    View v;
    ArrayList<TechDetails> techList = new ArrayList<>();
    ArrayList<String> techDetail = new ArrayList<>();
    NonScrollListView device_detail_list;
    MySearchableSpinner spn_technicians;
    private TickerView total_device;
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
        v = inflater.inflate(R.layout.fragment_material_to_tech, container, false);
        sharedprefs = getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");

        initView();
        addTechnicians();
        getFaultList();

        return v;
    }

    private void getFaultList() {
        newInstallmentController.reqeuestFaultList(this);
    }

    private void addTechnicians() {
        ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<TechResponse> clientCall = client_att.requestTechList();
        clientCall.enqueue(new Callback<TechResponse>() {
            public void onResponse(Call<TechResponse> call, Response<TechResponse> response) {
                try {

                    TechResponse workTypeResponse = response.body();
                    techList = response.body().gettechList();
                    Log.i("**workclientrespnse", " " + techList);

                    try {
                        techDetail.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < techList.size(); i++) {
                        techDetail.add(techList.get(i).getName());
                    }
                    List<String> name = new ArrayList<String>();
                    Collection c = techDetail;
                    Iterator itr = c.iterator();
                    name.add(" Select Technician");
                    while (itr.hasNext()) {
                        String temp = itr.next().toString();
                        Log.i("TEMP", "" + temp);
                        name.add(temp);
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, name);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_technicians.setAdapter(adapter);

                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TechResponse> call, Throwable t) {
                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }

    private void initView() {

        spn_technicians = v.findViewById(R.id.spn_technicians);
        device_detail_list = v.findViewById(R.id.device_detail_list);
        final TickerView tickerView = v.findViewById(R.id.ticker1);
        //tickerView.setCharacterLists(TickerUtils.provideNumberList());
        newInstallmentController = new NewInstallmentController();
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
