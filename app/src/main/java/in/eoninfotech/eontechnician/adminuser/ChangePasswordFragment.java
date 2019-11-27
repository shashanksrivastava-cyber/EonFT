package in.eoninfotech.eontechnician.adminuser;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.activity.LoginActivity;
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/***************************************************************************/
// Copyright EON Infotech Ltd., unpublished work, created 2016.          //
// This computer program includes Confidential, Proprietary information  //
// and is a trade secret of EON Infotech Ltd. All use, disclosure and/or //
// reproduction is prohibited unless authorised in writing by an         //
// authorised officer of EON Infotech Ltd. All rights reserved.          //

/**************************************************************************/
public class ChangePasswordFragment extends Fragment {
    EditText old_pass, new_pass, confrm_pass;
    Button btn_change_pass;
    String oldpwd, newpwd, cnfrmpwd, username,version, status, error;
    TextView txuser;
    View v;
    CoordinatorLayout coordinatorLayout;
    Thread thread;
    ProgressDialog pDialog;
    CheckConnection chk;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_change_password, container, false);
        // Inflate the layout for this fragment
        old_pass = v.findViewById(R.id.old_passwd);
        new_pass = v.findViewById(R.id.new_passwd);
        confrm_pass = v.findViewById(R.id.confirm_passwd);
        btn_change_pass = v.findViewById(R.id.btn_change);
        txuser = v.findViewById(R.id.change_user);

        username = getArguments().getString("usernme");
        version = getArguments().getString("version");
        txuser.setText(username.toUpperCase());

        chk= new CheckConnection(getActivity());

        btn_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chk.isConnected()) {
                    btn_change_pass.setClickable(false);
                    oldpwd = old_pass.getText().toString();
                    newpwd = new_pass.getText().toString();
                    cnfrmpwd = confrm_pass.getText().toString();
                    if (oldpwd.equals("")) {
                        old_pass.setError("Enter field");
                    } else if (newpwd.equals("")) {
                        new_pass.setError("Enter field");
                    } else if (cnfrmpwd.equals("")) {
                        confrm_pass.setError("Enter field");
                    } else if (!cnfrmpwd.equals(newpwd)) {
                        btn_change_pass.setClickable(true);
                        Toast.makeText(getContext(), "passwords do not match", Toast.LENGTH_SHORT).show();
                        new_pass.setText("");
                        confrm_pass.setText("");
                        new_pass.setFocusable(true);
                    } else {
                        View vie = getActivity().getCurrentFocus();
                        if (vie != null) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(vie.getWindowToken(), 0);
                        }
                        ChangePasswdApi(username, oldpwd, cnfrmpwd);
                    }
                }
                else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        return v;
    }

    private void ChangePasswdApi(String username, String oldpwd, String cnfrmpwd) {

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Confirming...");
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
                                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                                intent.putExtra("username", "us");
                                                startActivity(intent);
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
                btn_change_pass.setClickable(true);
                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                t.printStackTrace();
                btn_change_pass.setClickable(true);
                pDialog.dismiss();
                old_pass.setText("");
                new_pass.setText("");
                confrm_pass.setText("");
                Toast.makeText(getActivity(), "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
            }
        });

    }
}
