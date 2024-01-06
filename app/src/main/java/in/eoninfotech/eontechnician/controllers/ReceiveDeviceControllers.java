package in.eoninfotech.eontechnician.controllers;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;

import in.eoninfotech.eontechnician.Responses.MainResponse;
import in.eoninfotech.eontechnician.callbacks.ReceiveDeviceListener;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiveDeviceControllers extends Controller {

    String version;
    View v;
    ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
    Call<MainResponse> clientCall;

    public void requestReceiveDevice(String from_date,String to_date,String status,String tech_id,ReceiveDeviceListener listener) {

        clientCall = client_att.get_dispatch_material_status(from_date,to_date,status,tech_id);
        clientCall.enqueue(new Callback<MainResponse>() {
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                listener.receiveDeviceResponse(response.body());
            }
            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
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

    public void get_serial_no(String tech_id,String customer, String sub_cust,String activity_type,String dbname,String server,ReceiveDeviceListener listener) {

        clientCall = client_att.get_serial_no(tech_id,customer,sub_cust,activity_type,dbname,server);
        clientCall.enqueue(new Callback<MainResponse>() {
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                listener.receiveDeviceResponse(response.body());
            }
            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
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
}
