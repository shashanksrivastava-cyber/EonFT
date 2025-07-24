package in.eoninfotech.eontechnician.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;

import in.eoninfotech.eontechnician.responses.MainClientList;
import in.eoninfotech.eontechnician.viewModel.ViewModelMainClient;

public class MainClientObserver {

    public static void observeMainClients(
            Fragment fragment,
            ViewModelMainClient viewModel,
            AlertDialog progressDialog,
            Spinner spinner,
            List<String> clientNameList,
            Context context
    ) {
        viewModel.getMainClientRepository().observe(fragment.getViewLifecycleOwner(), response -> {

            progressDialog.hide();

            if (response.getType() == 1) {
                List<MainClientList> clientList = response.getMain_client_list();
                clientNameList.clear();

                if (clientList != null && !clientList.isEmpty()) {
                    for (MainClientList client : clientList) {
                        clientNameList.add(client.getClient_Name());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            context,
                            android.R.layout.simple_spinner_item,
                            clientNameList
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setVisibility(View.VISIBLE);
                } else {
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(context, "No Main Clients found", Toast.LENGTH_SHORT).show();
                }

            } else {
                spinner.setVisibility(View.GONE);
                Toast.makeText(context, "Failed to load Main Clients", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
