package in.eoninfotech.eontechnician.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import java.util.List;

public class WorkTypeObserver {

//    public static void observeWorkTypes(
//            Fragment fragment,
//            ViewModelWorkType viewModel,
//            ProgressDialog progressDialog,
//            Spinner spinner,
//            List<String> workTypeNameList,
//            Context context
//    ) {
//        viewModel.getWorkTypeList().observe(fragment.getViewLifecycleOwner(), response -> {
//
//            progressDialog.hide();
//
//            if (response.getType() == 1) {
//                List<WorkType> workTypes = response.getWorkTypes();
//                workTypeNameList.clear();
//
//                // ✅ Add "Select Work Type" as the first option
//                workTypeNameList.add("Select Work Type");
//
//                if (workTypes != null && !workTypes.isEmpty()) {
//                    for (WorkType item : workTypes) {
//                        workTypeNameList.add(item.getName());
//                    }
//
//                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                            context,
//                            android.R.layout.simple_spinner_item,
//                            workTypeNameList
//                    );
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spinner.setAdapter(adapter);
//                    spinner.setVisibility(View.VISIBLE);
//                } else {
//                    spinner.setVisibility(View.GONE);
//                    Toast.makeText(context, "No Work Types found", Toast.LENGTH_SHORT).show();
//                }
//
//            } else {
//                spinner.setVisibility(View.GONE);
//                Toast.makeText(context, "Failed to load Work Types", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
