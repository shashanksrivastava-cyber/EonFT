package in.eoninfotech.eontechnician;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.Responses.FaultyDevicesDetails;
import in.eoninfotech.eontechnician.activity.FaultyDeviceDetails;
import in.eoninfotech.eontechnician.activity.FaultyDevicesActivity;

/**
 * Created by androidpc on 29/5/19.
 */

public class FaultyDrsAdapter extends RecyclerView.Adapter<FaultyDrsAdapter.ActivityHolder> {

    View v;
    Dialog myDialog;
    Context context;
    String sendData;
    TextView site_veh_num, site_drs_num;
    private final ArrayList<FaultyDevicesDetails> faultyDevices;

    public FaultyDrsAdapter(ArrayList<FaultyDevicesDetails> faultyDevices, FaultyDevicesActivity faultyDevicesActivity, String sendData) {
        this.faultyDevices = faultyDevices;
        this.sendData = sendData;
        context = faultyDevicesActivity;
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_faulty_devices, parent, false));
    }

    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {

        final FaultyDevicesDetails faultyDevicesResponse = faultyDevices.get(position);

        holder.site_loc.setText(faultyDevicesResponse.getLocation());
        holder.site_cust_name.setText(faultyDevicesResponse.getCustomer());
        holder.faulty_drs.setText("Faulty DRS : " + faultyDevicesResponse.getFaulty_drs_cnt());
        holder.faulty_vts.setText("Faulty VTS : " + faultyDevicesResponse.getFaulty_dev_cnt());
        String abc = faultyDevicesResponse.getFaulty_devices();
        String def = faultyDevicesResponse.getFaulty_drs();
        String server = faultyDevicesResponse.getServer();
        String loc_id = faultyDevicesResponse.getLoc_id();
        String cust_id = faultyDevicesResponse.getCust_id();
        String database = faultyDevicesResponse.getDatabase();
        String customerName = faultyDevicesResponse.getCustomer();
        String location = faultyDevicesResponse.getLocation();

        holder.slide_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, FaultyDeviceDetails.class);
                intent.putExtra("Faulty VTS", abc);
                intent.putExtra("Faulty DRS", def);
                intent.putExtra("Server", server);
                intent.putExtra("LocId", loc_id);
                intent.putExtra("Cust_id", cust_id);
                intent.putExtra("Database", database);
                intent.putExtra("CustomerName", customerName);
                intent.putExtra("Location", location);
                context.startActivity(intent);
            }
        });
    }

    private String removeBr(String str1) {
        return str1.replaceAll("\\|", " ");
    }

    @Override
    public int getItemCount() {
        return faultyDevices.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        CardView slide_card;
        TextView site_loc, site_cust_name, faulty_vts, faulty_drs, quantity, tvBought;

        public ActivityHolder(View inflate) {
            super(inflate);
            site_loc = inflate.findViewById(R.id.site_loc);
            site_cust_name = inflate.findViewById(R.id.site_cust_name);
            faulty_vts = inflate.findViewById(R.id.faulty_vts);
            faulty_drs = inflate.findViewById(R.id.faulty_drs);
            slide_card = inflate.findViewById(R.id.slide_card);
            quantity = inflate.findViewById(R.id.quantity);
            tvBought = inflate.findViewById(R.id.tvBought);
        }
    }
}
