package in.eoninfotech.eontechnician;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.eoninfotech.eontechnician.responses.DeviceLiveStatus;

public class BusDataAdapter extends RecyclerView.Adapter<BusDataAdapter.BusDataViewHolder>{
    private Context context;
    ArrayList<DeviceLiveStatus> deviceLiveStatuses;

    public BusDataAdapter(Context context, ArrayList<DeviceLiveStatus> deviceLiveStatuses) {
        this.context = context;
        this.deviceLiveStatuses = deviceLiveStatuses;
    }

    @NonNull
    @Override
    public BusDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drum_rotation_status_dialog, parent, false);
        return new BusDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusDataViewHolder holder, int position) {

        //holder.tv_reg_no.setText(deviceLiveStatuses.get(position).bus_name);

        holder.tv_reg_no.setText("Trip No : "+(String.valueOf(position + 1)));

        holder.tvStatus.setText(deviceLiveStatuses.get(position).drum_rotaion_status);
        if (deviceLiveStatuses.get(position).drum_rotaion_status.equals("Drum Rotated")) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_rotated);
        } else {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_not_rotated);
        }

        holder.tvDrumHrs.setText("Trip Drum Hrs: " + (deviceLiveStatuses.get(position).drum_hrs.isEmpty() ? "N/A" : deviceLiveStatuses.get(position).drum_hrs));
        holder.tvKm.setText("Trip KM: " + deviceLiveStatuses.get(position).km_per);

        String siteText = "Site In: " + (deviceLiveStatuses.get(position).site_in.isEmpty() ? "N/A" : deviceLiveStatuses.get(position).site_in) +
                " \nSite Out " + (deviceLiveStatuses.get(position).site_out.isEmpty() ? "N/A" : deviceLiveStatuses.get(position).site_out);
        String plantText = "Plant Out: " + deviceLiveStatuses.get(position).plant_in + " \nPlant In " + deviceLiveStatuses.get(position).plant_out;

        holder.tvSiteInOut.setText(siteText);
        holder.tvPlantInOut.setText(plantText);
    }

    @Override
    public int getItemCount() {
        return deviceLiveStatuses.size();
    }

    public static class BusDataViewHolder extends RecyclerView.ViewHolder {
        TextView tv_reg_no, tvStatus, tvSiteInOut, tvPlantInOut, tvDrumHrs, tvKm, tvDate;

        public BusDataViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_reg_no = itemView.findViewById(R.id.tv_reg_no);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvSiteInOut = itemView.findViewById(R.id.tv_site_in_out);
            tvPlantInOut = itemView.findViewById(R.id.tv_plant_in_out);
            tvDrumHrs = itemView.findViewById(R.id.tv_drum_hrs);
            tvKm = itemView.findViewById(R.id.tv_km);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}
