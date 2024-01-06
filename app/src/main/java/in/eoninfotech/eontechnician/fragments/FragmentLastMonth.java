package in.eoninfotech.eontechnician.fragments;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.helper.IncentiveDetail;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 10/10/17.
 */
public class FragmentLastMonth extends Fragment {

    View v;
    String disttid;
    String username,version;
    private ImageView ivCircle;
    private TextView txtIdle;
    private TextView txtInactive;
    private TextView txtNoData;
    private TextView txtRunning;
    private TextView txtStop;
    private TextView txtTotal;
    MySearchableSpinner technicians;
    RelativeLayout relay;
    ProgressDialog pDialog;
    ArrayList<IncentiveDetail> last_month = new ArrayList<IncentiveDetail>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
        username = getArguments().getString("usernme");
        version = getArguments().getString("version");
//        relay = (RelativeLayout) v.findViewById(R.id.dash_relay);
//        relay.setVisibility(View.GONE);
//        ivCircle = (ImageView) v.findViewById(R.id.imgcircle);
//        txtTotal = (TextView) v.findViewById(R.id.centerView);
//        txtRunning = (TextView) v.findViewById(R.id.txtRunning);
//        txtIdle = (TextView) v.findViewById(R.id.txtIdle);
//        txtInactive = (TextView) v.findViewById(R.id.txtInactive);
//        txtNoData = (TextView) v.findViewById(R.id.txtNodata);
//        txtStop = (TextView) v.findViewById(R.id.txtStop);
        getPlantDeviceDrsData();

        return v;
    }
    void getPlantDeviceDrsData() {
        pDialog = new ProgressDialog(v.getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        ApiHolder get_list = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<UpdateDataResponse> call = get_list.incentive(username.toUpperCase(), K.Url.urlkey);
        call.enqueue(new Callback<UpdateDataResponse>() {
            @Override
            public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                try {
                    last_month = response.body().getLast_month();
                    last_month.add(last_month.get(0));
                    txtIdle.setText("Stock Update\n" + last_month.get(0).getStock_upd());
                    txtInactive.setText("Up Time\n" + last_month.get(0).getUp_time());
                    txtNoData.setText("Activity\n" + last_month.get(0).getActivity_upd());
                    txtRunning.setText("Next Day Plan\n" + last_month.get(0).getNext_day_plan());
                    txtStop.setText("Call Sheet\n" + last_month.get(0).getDc_sheet());
                    txtTotal.setText("Total\n" + last_month.get(0).getTotal_inc());
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"No data",Toast.LENGTH_LONG);
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
//                t.printStackTrace();
//                pDialog.dismiss();
//                TSnackbar snackbar = TSnackbar.make(v, "Try Again Connection Timeout", TSnackbar.LENGTH_LONG);
//                View snackbarView = snackbar.getView();
//                snackbarView.setBackgroundColor(Color.RED);
//                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
//                textView.setTextColor(Color.WHITE);
//                snackbar.show();
            }
        });
    }
}
