package in.eoninfotech.eontechnician.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.BillViewAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.DeviceLiveStatus;
import in.eoninfotech.eontechnician.webservice.BillDetails;

public class LiveStatusAdapterNew extends RecyclerView.Adapter<LiveStatusAdapterNew.ActivityHolder> {

    Context context;
    private final ArrayList<DeviceLiveStatus> deviceLiveStatuses;

    public LiveStatusAdapterNew(Context context, ArrayList<DeviceLiveStatus> deviceLiveStatuses) {

        this.deviceLiveStatuses = deviceLiveStatuses;
        this.context = context;
    }

    @NonNull
    @Override
    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.live_status_adapter_new, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LiveStatusAdapterNew.ActivityHolder holder, int position) {

        //final DeviceLiveStatus deviceLiveStatus = deviceLiveStatuses.get(position);
        holder.sr_no.setText(deviceLiveStatuses.get(position).sr_no);
        holder.reg_no.setText(deviceLiveStatuses.get(position).reg_no);
        holder.connection_status.setText(deviceLiveStatuses.get(position).status);
        holder.device_id.setText("Device Id : " + deviceLiveStatuses.get(position).vts_id);
        holder.depo.setText("Depot : "+ deviceLiveStatuses.get(position).depot);
        holder.serial_no.setText("Serial No : "+ deviceLiveStatuses.get(position).serial_no);
        holder.veh_type.setText("Veh Type : "+ deviceLiveStatuses.get(position).veh_type_name);
        holder.gps.setText("GPS : "+ deviceLiveStatuses.get(position).gps);
        holder.gsm.setText("GSM : "+ deviceLiveStatuses.get(position).gsm);
        holder.power.setText("Power : "+ deviceLiveStatuses.get(position).power);
        holder.battery.setText("Battery : "+ deviceLiveStatuses.get(position).battery);
        holder.drum_sensor.setText("Drum Sensor : "+ deviceLiveStatuses.get(position).drum_sensor);
        holder.lid_sensor.setText("LID Sensor : "+ deviceLiveStatuses.get(position).lid_sensor);
        holder.speed.setText("Speed : "+ deviceLiveStatuses.get(position).speed);
        if(deviceLiveStatuses.get(position).status_type.equalsIgnoreCase("C")){
            holder.connected.setVisibility(View.VISIBLE);
        }else {
            holder.disconnected.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return deviceLiveStatuses.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        ImageView connected,disconnected;
        TextView reg_no,device_id,depo,serial_no,veh_type,gps,gsm,power,battery,drum_sensor,lid_sensor,
                speed,connection_status,sr_no;
        public ActivityHolder(@NonNull View itemView) {
            super(itemView);

            reg_no = itemView.findViewById(R.id.reg_no);
            device_id = itemView.findViewById(R.id.device_id);
            depo = itemView.findViewById(R.id.depo);
            serial_no = itemView.findViewById(R.id.serial_no);
            veh_type = itemView.findViewById(R.id.veh_type);
            gps = itemView.findViewById(R.id.gps);
            gsm = itemView.findViewById(R.id.gsm);
            power = itemView.findViewById(R.id.power);
            battery = itemView.findViewById(R.id.battery);
            drum_sensor = itemView.findViewById(R.id.drum_sensor);
            lid_sensor = itemView.findViewById(R.id.lid_sensor);
            speed = itemView.findViewById(R.id.speed);
            connection_status = itemView.findViewById(R.id.connection_status);
            connected = itemView.findViewById(R.id.connected);
            disconnected = itemView.findViewById(R.id.disconnected);
            sr_no = itemView.findViewById(R.id.sr_no);
        }
    }
}
