package in.eoninfotech.eontechnician;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.Responses.FaultyDevicesDetails;
import in.eoninfotech.eontechnician.activity.FaultyDevicesDetailActivity;

/**
 * Created by root on 1/11/18.
 */

public class FaultyDeviceDetailAdapter extends RecyclerView.Adapter<FaultyDeviceDetailAdapter.ActivityHolder> {

    Context context;
    String sendData;
    private final ArrayList<FaultyDevicesDetails> faultyDevices;

    public FaultyDeviceDetailAdapter(ArrayList<FaultyDevicesDetails> faultyDevicesDetails, FaultyDevicesDetailActivity faultyDevicesDetailActivity) {
      this.faultyDevices = faultyDevicesDetails;
      this.context = faultyDevicesDetailActivity;
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FaultyDeviceDetailAdapter.ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vehicledetails, parent, false));
    }

    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {

        final FaultyDevicesDetails faultyDevicesResponse = faultyDevices.get(position);
        holder.site_veh_num.setText(faultyDevicesResponse.getFaulty_devices());
    }

    @Override
    public int getItemCount() {
        return faultyDevices.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        TextView site_veh_num, site_drs_num,faulty_vts,faulty_drs;
        public ActivityHolder(View inflate) {
            super(inflate);
            site_veh_num = (TextView)inflate.findViewById(R.id.site_veh_num);
            site_drs_num = (TextView)inflate.findViewById(R.id.site_drs_num);
        }
    }
}
