package in.eoninfotech.eontechnician.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        final DeviceLiveStatus deviceLiveStatus = deviceLiveStatuses.get(position);
        //holder.reg_no.setText(deviceLiveStatus.reg_no);

    }


    @Override
    public int getItemCount() {
        return deviceLiveStatuses.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        TextView reg_no,device_id,status,date,app_amount,app_date,remarks,rej_date,title,viewBill,action_by;
        public ActivityHolder(@NonNull View itemView) {
            super(itemView);

            reg_no = itemView.findViewById(R.id.reg_no);
            device_id = itemView.findViewById(R.id.device_id);
            status = itemView.findViewById(R.id.status);
            app_amount = itemView.findViewById(R.id.app_amount);
            app_date = itemView.findViewById(R.id.app_date);
            remarks = itemView.findViewById(R.id.remarks);
            rej_date = itemView.findViewById(R.id.rej_date);
            title = itemView.findViewById(R.id.app_title);
            viewBill = itemView.findViewById(R.id.viewBill);
            action_by = itemView.findViewById(R.id.action_by);
        }
    }
}
