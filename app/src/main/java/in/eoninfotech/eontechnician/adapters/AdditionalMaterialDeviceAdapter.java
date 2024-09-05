package in.eoninfotech.eontechnician.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.responses.AdditionalDeviceDetails;
import in.eoninfotech.eontechnician.responses.CallSheetListDetail;

public class AdditionalMaterialDeviceAdapter extends RecyclerView.Adapter<AdditionalMaterialDeviceAdapter.ActivityHolder> {


    Context context;
    private final ArrayList<AdditionalDeviceDetails> additionalDeviceDetails;

    public AdditionalMaterialDeviceAdapter(Context context,ArrayList<AdditionalDeviceDetails> additionalDeviceDetails) {
        this.context = context;
        this.additionalDeviceDetails = additionalDeviceDetails;
    }

    @NonNull
    @Override
    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdditionalMaterialDeviceAdapter.ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.additional_material_device_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {
        final AdditionalDeviceDetails additionalDeviceDetail = additionalDeviceDetails.get(position);

        holder.client_name.setText(additionalDeviceDetail.getCust_name());
        holder.device_type.setText(additionalDeviceDetail.getDevice_type());
        holder.quantity.setText(additionalDeviceDetail.getQuantity());

    }

    @Override
    public int getItemCount() {
        return additionalDeviceDetails.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        TextView client_name,device_type,quantity;
        public ActivityHolder(View itemView) {
            super(itemView);

            client_name = itemView.findViewById(R.id.client_name);
            device_type = itemView.findViewById(R.id.device_type);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }
}
