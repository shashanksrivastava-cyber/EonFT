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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import in.eoninfotech.eontechnician.responses.FaultyDevicesDetails;
import in.eoninfotech.eontechnician.activity.FaultyDeviceDetails;
import in.eoninfotech.eontechnician.activity.FaultyDevicesActivity;

/**
 * Created by root on 30/10/18.
 */

public class FaultyDevicesAdapter extends RecyclerView.Adapter<FaultyDevicesAdapter.ActivityHolder> {

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

//public class FaultyDevicesAdapter extends ListAdapter<FaultyDevicesDetails, FaultyDevicesAdapter.ActivityHolder> {
//
//    private final Context context;
//    private final String sendData;
//
//    public FaultyDevicesAdapter(Context context, String sendData) {
//        super(DIFF_CALLBACK);
//        this.context = context;
//        this.sendData = sendData;
//    }
//
//    /**
//     * ✅ DiffUtil to efficiently handle updates
//     */
//    private static final DiffUtil.ItemCallback<FaultyDevicesDetails> DIFF_CALLBACK =
//            new DiffUtil.ItemCallback<FaultyDevicesDetails>() {
//                @Override
//                public boolean areItemsTheSame(@NonNull FaultyDevicesDetails oldItem,
//                                               @NonNull FaultyDevicesDetails newItem) {
//                    // Use a unique field (like loc_id) to identify item
//                    return Objects.equals(oldItem.getLoc_id(), newItem.getLoc_id());
//                }
//
//                @Override
//                public boolean areContentsTheSame(@NonNull FaultyDevicesDetails oldItem,
//                                                  @NonNull FaultyDevicesDetails newItem) {
//                    // Compare all relevant fields
//                    return oldItem.equals(newItem);
//                }
//            };
//
//    @NonNull
//    @Override
//    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.adapter_faulty_devices, parent, false);
//        return new ActivityHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {
//        FaultyDevicesDetails item = getItem(position);
//
//        holder.site_loc.setText(item.getLocation());
//        holder.site_cust_name.setText(item.getCustomer());
//        holder.faulty_drs.setText("Faulty DRS/SOS : 0");
//        holder.faulty_vts.setText("Faulty VTS : " + item.getFaulty_dev_cnt());
//        holder.faulty_fuel.setText("Faulty Fuel : " + item.getFaulty_fuel_count());
//
//        holder.slide_card.setOnClickListener(v -> {
//            Intent intent = new Intent(context, FaultyDeviceDetails.class);
//            intent.putExtra("Faulty VTS", item.getFaulty_devices());
//            intent.putExtra("Faulty DRS", item.getFaulty_drs());
//            intent.putExtra("Faulty fuel", item.getFaulty_fuel());
//            intent.putExtra("Server", item.getServer());
//            intent.putExtra("LocId", item.getLoc_id());
//            intent.putExtra("Cust_id", item.getCust_id());
//            intent.putExtra("Database", item.getDatabase());
//            intent.putExtra("CustomerName", item.getCustomer());
//            intent.putExtra("Location", item.getLocation());
//            context.startActivity(intent);
//        });
//    }
//
//    /**
//     * ✅ ViewHolder
//     */
//    public static class ActivityHolder extends RecyclerView.ViewHolder {
//        CardView slide_card;
//        TextView site_loc, site_cust_name, faulty_vts, faulty_drs, faulty_fuel;
//
//        public ActivityHolder(@NonNull View itemView) {
//            super(itemView);
//            slide_card = itemView.findViewById(R.id.slide_card);
//            site_loc = itemView.findViewById(R.id.site_loc);
//            site_cust_name = itemView.findViewById(R.id.site_cust_name);
//            faulty_vts = itemView.findViewById(R.id.faulty_vts);
//            faulty_drs = itemView.findViewById(R.id.faulty_drs);
//            faulty_fuel = itemView.findViewById(R.id.faulty_fuel);
//        }
//    }
//
//    /**
//     * ✅ Replace your old updateList() with this clean method
//     */
//    public void submitFaultyDevices(java.util.List<FaultyDevicesDetails> newList) {
//        submitList(newList);
//    }
//}
