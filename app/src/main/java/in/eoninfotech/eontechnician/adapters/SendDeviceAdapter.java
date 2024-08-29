package in.eoninfotech.eontechnician;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.responses.TechReturnDevice;
import in.eoninfotech.eontechnician.activity.SendDeviceDetails;

public class SendDeviceAdapter extends RecyclerView.Adapter<SendDeviceAdapter.ActivityHolder>{

    in.eoninfotech.eontechnician.MaterialReturnViews fragment;
    Context context;
    String date;
    private final ArrayList<TechReturnDevice> dispatchDeviceListArrayList;

    public SendDeviceAdapter(Context context, ArrayList<TechReturnDevice> dispatchDeviceListArrayList) {

        this.dispatchDeviceListArrayList = dispatchDeviceListArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.return_material_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {

        holder.ref_no.setText(dispatchDeviceListArrayList.get(position).dispatched_date);

        holder.sr_no.setText(dispatchDeviceListArrayList.get(position).device_count);


        if(dispatchDeviceListArrayList.get(position).transit_name.equalsIgnoreCase("")){
            holder.name.setVisibility(View.GONE);
        }else {
            holder.name.setVisibility(View.VISIBLE);
            holder.name.setText(dispatchDeviceListArrayList.get(position).transit_name);
        }
        holder.type.setText(dispatchDeviceListArrayList.get(position).transit_type);
        holder.docket.setText(dispatchDeviceListArrayList.get(position).transit_through);

        holder.status.setText(dispatchDeviceListArrayList.get(position).status);

        if(dispatchDeviceListArrayList.get(position).status.equalsIgnoreCase("All Received")){
            holder.status.setBackgroundResource(R.color.green);
            holder.received_at.setVisibility(View.VISIBLE);
            holder.received_at.setText("Received at: " + dispatchDeviceListArrayList.get(position).received_date);
        }else if(dispatchDeviceListArrayList.get(position).status.equalsIgnoreCase("In-Transit")) {
            holder.status.setBackgroundResource(R.color.c);
            holder.received_at.setVisibility(View.GONE);
        }else {
            holder.status.setBackgroundResource(R.color.dash_red);
            holder.received_at.setVisibility(View.GONE);
        }
        holder.dispatch_to.setText("Dispatched To : "+ dispatchDeviceListArrayList.get(position).dispatched_to);

        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SendDeviceDetails.class);
                intent.putExtra("id_no",dispatchDeviceListArrayList.get(position).id_no);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dispatchDeviceListArrayList.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        TextView ref_no,sr_no,name,type,docket,status,details,dispatch_to,received_at;
        public ActivityHolder(View inflate) {
            super(inflate);

            ref_no = inflate.findViewById(R.id.ref_no);
            sr_no = inflate.findViewById(R.id.sr_no);
            name = inflate.findViewById(R.id.name);
            type = inflate.findViewById(R.id.type);
            docket = inflate.findViewById(R.id.docket);
            status = inflate.findViewById(R.id.status);
            details = inflate.findViewById(R.id.details);
            dispatch_to = inflate.findViewById(R.id.dispatch_to);
            received_at = inflate.findViewById(R.id.received_at);
        }
    }
}
