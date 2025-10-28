package in.eoninfotech.eontechnician;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import in.eoninfotech.eontechnician.responses.FaultyDevicesDetails;
import in.eoninfotech.eontechnician.activity.FaultyDeviceDetails;
import in.eoninfotech.eontechnician.activity.FaultyDevicesActivity;

/**
 * Created by root on 30/10/18.
 */

public class FaultyDevicesAdapter extends RecyclerView.Adapter<FaultyDevicesAdapter.ActivityHolder> {

//    View v;
//    Dialog myDialog;
//    Context context;
//    String sendData;
//    TextView site_veh_num,site_drs_num;
//    private final ArrayList<FaultyDevicesDetails> faultyDevices;
//
//    public FaultyDevicesAdapter(ArrayList<FaultyDevicesDetails> faultyDevices, FaultyDevicesActivity faultyDevicesActivity, String sendData) {
//        this.faultyDevices = faultyDevices;
//        this.sendData = sendData;
//        context = faultyDevicesActivity;
//    }
//    @Override
//    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new ActivityHolder(LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.adapter_faulty_devices, parent, false));
//    }
//    @Override
//    public void onBindViewHolder(ActivityHolder holder, int position) {
//
//        final FaultyDevicesDetails faultyDevicesResponse = faultyDevices.get(position);
//
//        holder.site_loc.setText(faultyDevicesResponse.getLocation());
//        holder.site_cust_name.setText(faultyDevicesResponse.getCustomer());
//        holder.faulty_drs.setText("Faulty DRS/SOS : "+"0");
//        holder.faulty_vts.setText("Faulty VTS : "+faultyDevicesResponse.getFaulty_dev_cnt());
//        holder.faulty_fuel.setText("Faulty Fuel : "+faultyDevicesResponse.getFaulty_fuel_count());
//
//        holder.slide_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(context,FaultyDeviceDetails.class);
//                intent.putExtra("Faulty VTS",faultyDevicesResponse.getFaulty_devices());
//                intent.putExtra("Faulty DRS",faultyDevicesResponse.getFaulty_drs());
//                intent.putExtra("Faulty fuel",faultyDevicesResponse.getFaulty_fuel());
//                intent.putExtra("Server",faultyDevicesResponse.getServer());
//                intent.putExtra("LocId",faultyDevicesResponse.getLoc_id());
//                intent.putExtra("Cust_id",faultyDevicesResponse.getCust_id());
//                intent.putExtra("Database",faultyDevicesResponse.getDatabase());
//                intent.putExtra("CustomerName",faultyDevicesResponse.getCustomer());
//                intent.putExtra("Location",faultyDevicesResponse.getLocation());
//                context.startActivity(intent);
//            }
//        });
//    }
//    private String removeBr(String str1){
//        return str1.replaceAll("\\|", " ");
//    }
//
//    @Override
//    public int getItemCount() {
//        return faultyDevices.size();
//    }
//
//    public class ActivityHolder extends RecyclerView.ViewHolder {
//
//        CardView slide_card;
//        TextView site_loc, site_cust_name, faulty_vts, faulty_drs, quantity, tvBought,faulty_fuel;
//
//        public ActivityHolder(View inflate) {
//            super(inflate);
//            site_loc = inflate.findViewById(R.id.site_loc);
//            site_cust_name = inflate.findViewById(R.id.site_cust_name);
//            faulty_vts = inflate.findViewById(R.id.faulty_vts);
//            faulty_drs = inflate.findViewById(R.id.faulty_drs);
//            slide_card = inflate.findViewById(R.id.slide_card);
//            quantity = inflate.findViewById(R.id.quantity);
//            tvBought = inflate.findViewById(R.id.tvBought);
//            faulty_fuel = inflate.findViewById(R.id.faulty_fuel);
//        }
//    }

    private final Context context;
    private final String sendData;
    private ArrayList<FaultyDevicesDetails> faultyDevices;

    public FaultyDevicesAdapter(ArrayList<FaultyDevicesDetails> faultyDevices, Context context, String sendData) {
        this.faultyDevices = faultyDevices;
        this.context = context;
        this.sendData = sendData;
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_faulty_devices, parent, false);
        return new ActivityHolder(view);
    }

    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {
        final FaultyDevicesDetails faultyDevicesResponse = faultyDevices.get(position);

        holder.site_loc.setText(faultyDevicesResponse.getLocation());
        holder.site_cust_name.setText(faultyDevicesResponse.getCustomer());
        holder.faulty_drs.setText("Faulty DRS/SOS : 0");
        holder.faulty_vts.setText("Faulty VTS : " + faultyDevicesResponse.getFaulty_dev_cnt());
        holder.faulty_fuel.setText("Faulty Fuel : " + faultyDevicesResponse.getFaulty_fuel_count());

        holder.slide_card.setOnClickListener(v -> {
            Intent intent = new Intent(context, FaultyDeviceDetails.class);
            intent.putExtra("Faulty VTS", faultyDevicesResponse.getFaulty_devices());
            intent.putExtra("Faulty DRS", faultyDevicesResponse.getFaulty_drs());
            intent.putExtra("Faulty fuel", faultyDevicesResponse.getFaulty_fuel());
            intent.putExtra("Server", faultyDevicesResponse.getServer());
            intent.putExtra("LocId", faultyDevicesResponse.getLoc_id());
            intent.putExtra("Cust_id", faultyDevicesResponse.getCust_id());
            intent.putExtra("Database", faultyDevicesResponse.getDatabase());
            intent.putExtra("CustomerName", faultyDevicesResponse.getCustomer());
            intent.putExtra("Location", faultyDevicesResponse.getLocation());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return faultyDevices != null ? faultyDevices.size() : 0;
    }

    public static class ActivityHolder extends RecyclerView.ViewHolder {
        CardView slide_card;
        TextView site_loc, site_cust_name, faulty_vts, faulty_drs, faulty_fuel, quantity, tvBought;

        public ActivityHolder(View itemView) {
            super(itemView);
            slide_card = itemView.findViewById(R.id.slide_card);
            site_loc = itemView.findViewById(R.id.site_loc);
            site_cust_name = itemView.findViewById(R.id.site_cust_name);
            faulty_vts = itemView.findViewById(R.id.faulty_vts);
            faulty_drs = itemView.findViewById(R.id.faulty_drs);
            faulty_fuel = itemView.findViewById(R.id.faulty_fuel);
            quantity = itemView.findViewById(R.id.quantity);
            tvBought = itemView.findViewById(R.id.tvBought);
        }
    }

    /**
     * ✅ Update list efficiently using DiffUtil
     */
    public void updateList(List<FaultyDevicesDetails> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return faultyDevices == null ? 0 : faultyDevices.size();
            }

            @Override
            public int getNewListSize() {
                return newList == null ? 0 : newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                // Use a unique field (like LocId) to check identity
                return Objects.equals(
                        faultyDevices.get(oldItemPosition).getLoc_id(),
                        newList.get(newItemPosition).getLoc_id()
                );
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                // Compare full objects (make sure FaultyDevicesDetails implements equals)
                return faultyDevices.get(oldItemPosition).equals(newList.get(newItemPosition));
            }
        });

        faultyDevices.clear();
        faultyDevices.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
}
