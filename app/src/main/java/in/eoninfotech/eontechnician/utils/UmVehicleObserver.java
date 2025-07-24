package in.eoninfotech.eontechnician.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.UnderMaintenanceVehicles;
import in.eoninfotech.eontechnician.viewModel.ViewModelUM;

public class UmVehicleObserver {

    public static void observeUmVehicles(
            Fragment fragment,
            ViewModelUM viewModel,
            String mainClientId,
            String clientId,
            String clientLocId,
            ListView listView,
            TextView txtContentUnavailable,
            List<String> valueNameList,
            List<String> valueNameSr,
            Context context) {

        viewModel.getUmRepository(mainClientId, clientId, clientLocId, "1")
                .observe(fragment.getViewLifecycleOwner(), response -> {

                    if (response.getType() == 1) {
                        List<UnderMaintenanceVehicles> vehicleList = response.getUm_vehicles();
                        valueNameList.clear();

                        if (vehicleList != null && !vehicleList.isEmpty()) {
                            for (int i = 0; i < vehicleList.size(); i++) {
                                String regNo = vehicleList.get(i).reg_no;
                                valueNameList.add((i + 1) + ". " + regNo);
                                valueNameSr.add(vehicleList.get(i).serial_no);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    context,
                                    R.layout.simple_custom_list_item, // Use your item layout
                                    valueNameList
                            );

                            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                            listView.setAdapter(adapter);
                            listView.setVisibility(View.VISIBLE);
                            txtContentUnavailable.setVisibility(View.GONE);
                        } else {
                            listView.setVisibility(View.GONE);
                            txtContentUnavailable.setVisibility(View.VISIBLE);
                        }

                    } else {
                        listView.setVisibility(View.GONE);
                        txtContentUnavailable.setVisibility(View.VISIBLE);
                    }
                });
    }
}
