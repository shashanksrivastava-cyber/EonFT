package in.eoninfotech.eontechnician.adminuser;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.activity.LoginActivity;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    TextView t_quantity_entered, t_devices_install, t_pending_previous, t_total_pending;
    View v;
    ProgressDialog pDialog;
    String version;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_admin_main, container, false);
        t_quantity_entered = v.findViewById(R.id.quantity_entered);
        t_devices_install = v.findViewById(R.id.devices_install);
        t_pending_previous = v.findViewById(R.id.pending_previous_mon);
        t_total_pending = v.findViewById(R.id.total_pending);
        version = getArguments().getString("version");
        return v;
    }

    private void ChangePasswdApi(String username, String oldpwd, String cnfrmpwd) {

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Getting detail...");
        pDialog.show();
        ApiHolder log_att = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<UpdateDataResponse> call = log_att.change_pwd(username, oldpwd, cnfrmpwd);
        Log.i("****call", String.valueOf(call));
        call.enqueue(new Callback<UpdateDataResponse>() {
            @Override
            public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                UpdateDataResponse updateDataResponse = response.body();
                Log.i("**respnse", " " + response.body());
                if (updateDataResponse != null) {
                    Toast.makeText(getActivity(), updateDataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    if (updateDataResponse.getType() == 1) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Password Changed")
                                .setMessage(
                                        "Your new password has been set. Login with your new password")
                                .setPositiveButton("Login",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                                getActivity().finish();
                                            }
                                        })
                                .setIcon(R.drawable.ic_check_circle_black_24dp).show()
                                .setCancelable(false);
                    }
                } else {
                    assert updateDataResponse != null;
                    Log.v("Response", updateDataResponse.toString());
                    TSnackbar snackbar = TSnackbar.make(v, updateDataResponse.getMessage(), TSnackbar.LENGTH_LONG);
                    /*    snackbar.setActionTextColor(Color.RED);*/
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.GRAY);
                    TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.RED);
                    snackbar.show();

                }
                if (pDialog != null)
                    pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                t.printStackTrace();
                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(getActivity(), "Try Again-Connection timeout", Toast.LENGTH_LONG).show();

            }
        });

    }
}
