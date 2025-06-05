package in.eoninfotech.eontechnician.fragments;

import static in.eoninfotech.eontechnician.R.drawable.connected;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.activity.ViewDRSStatus;
import in.eoninfotech.eontechnician.responses.DeviceLiveStatus;

public class LiveStatusAdapterNew extends RecyclerView.Adapter<LiveStatusAdapterNew.ActivityHolder> {

    Context context;
    private final ArrayList<DeviceLiveStatus> deviceLiveStatuses;

    String server_name,db_name;

    public interface OnDRSStatusClickListener {
        void onDRSStatusClick(DeviceLiveStatus device);
    }
    private OnDRSStatusClickListener listener;

    public LiveStatusAdapterNew(Context context, ArrayList<DeviceLiveStatus> deviceLiveStatuses, String server_name, String db_name) {

        this.deviceLiveStatuses = deviceLiveStatuses;
        this.context = context;
        this.listener = listener;
        this.server_name = server_name;
        this.db_name = db_name;
    }

    @NonNull
    @Override
    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LiveStatusAdapterNew.ActivityHolder holder, int position) {

        holder.sr_no.setText(deviceLiveStatuses.get(position).sr_no);
        holder.reg_no.setText(deviceLiveStatuses.get(position).reg_no);
        holder.device_id.setText(deviceLiveStatuses.get(position).vts_id);
        holder.serial_no.setText(deviceLiveStatuses.get(position).serial_no);
        holder.gps.setText(deviceLiveStatuses.get(position).gps);
        holder.gsm.setText(deviceLiveStatuses.get(position).gsm);
        if(deviceLiveStatuses.get(position).power.equalsIgnoreCase("Dis Connected")){
            holder.power.setBackgroundResource(R.drawable.power_dis);
        }
        holder.battery.setText(deviceLiveStatuses.get(position).battery);
        holder.drum_sensor.setText(deviceLiveStatuses.get(position).drum_sensor);
        holder.lid_sensor.setText(deviceLiveStatuses.get(position).lid_sensor);
        holder.speed.setText(deviceLiveStatuses.get(position).speed);
        holder.fuel.setText(deviceLiveStatuses.get(position).fuel);
        holder.silo.setText(deviceLiveStatuses.get(position).silo);

        if(deviceLiveStatuses.get(position).status_type.equalsIgnoreCase("C")){
            holder.connection_status.setText("Connected");
            holder.connected.setBackgroundResource(connected);
        }else if(deviceLiveStatuses.get(position).status_type.equalsIgnoreCase("D")){
            holder.connection_status.setText(deviceLiveStatuses.get(position).status);
            holder.connected.setBackgroundResource(R.drawable.disconnected);
        }else {
            holder.connection_status.setText(deviceLiveStatuses.get(position).status);
            holder.connected.setBackgroundResource(R.drawable.notrack);
        }

        if (position % 2 == 0) {
            holder.linear_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.back_color));
        } else {
            holder.linear_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.eonWhite));
        }

        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strUri = "http://maps.google.com/maps?q=loc:" + deviceLiveStatuses.get(position).lat + "," + deviceLiveStatuses.get(position).lng +deviceLiveStatuses.get(position).reg_no;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                context.startActivity(intent);
            }
        });

        holder.drs_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inteer = new Intent(context, ViewDRSStatus.class);
                inteer.putExtra("veh_no",deviceLiveStatuses.get(position).reg_no);
                inteer.putExtra("server",server_name);
                inteer.putExtra("db_Name",db_name);
                context.startActivity(inteer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceLiveStatuses.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        ImageView connected,disconnected,under_main;
        TextView reg_no,device_id,depo,serial_no,veh_type,gps,gsm,power,battery,drum_sensor,lid_sensor,
                speed,connection_status,sr_no,fuel,silo,location,drs_status;
        LinearLayout linear_layout;
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
            under_main = itemView.findViewById(R.id.under_main);
            linear_layout = itemView.findViewById(R.id.linear_layout);
            fuel = itemView.findViewById(R.id.fuel);
            silo = itemView.findViewById(R.id.silo);
            location = itemView.findViewById(R.id.location);
            drs_status = itemView.findViewById(R.id.drs_status);
        }
    }
}
