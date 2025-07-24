package in.eoninfotech.eontechnician.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.UnderMaintenanceVehicles;
import in.eoninfotech.eontechnician.fragments.LiveStatusAdapterNew;
import in.eoninfotech.eontechnician.responses.DeviceLiveStatus;

public class UnderMaintenanceVehicleAdapter extends RecyclerView.Adapter<UnderMaintenanceVehicleAdapter.ActivityHolder> {

    Context context;
    private final ArrayList<UnderMaintenanceVehicles> underMaintenanceVehicles;
    public UnderMaintenanceVehicleAdapter(Context context, ArrayList<UnderMaintenanceVehicles> umVehicles) {

        this.context = context;
        this.underMaintenanceVehicles  = umVehicles;

    }

    @NonNull
    @Override
    public UnderMaintenanceVehicleAdapter.ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull UnderMaintenanceVehicleAdapter.ActivityHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        public ActivityHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
