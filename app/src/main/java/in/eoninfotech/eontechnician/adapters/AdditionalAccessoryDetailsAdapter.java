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
import in.eoninfotech.eontechnician.activity.AdditionalMaterialDetails;
import in.eoninfotech.eontechnician.responses.AdditionalAccessoryDetails;
import in.eoninfotech.eontechnician.responses.AdditionalDeviceDetails;

public class AdditionalAccessoryDetailsAdapter extends RecyclerView.Adapter<AdditionalAccessoryDetailsAdapter.ActivityHolder>{

    Context context;
    private final ArrayList<AdditionalAccessoryDetails> additionalAccessoryDetails;

    public AdditionalAccessoryDetailsAdapter(Context context , ArrayList<AdditionalAccessoryDetails> additionalAccessoryDetails) {
        this.context = context;
        this.additionalAccessoryDetails = additionalAccessoryDetails;
    }

    @NonNull
    @Override
    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdditionalAccessoryDetailsAdapter.ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.additional_accessory_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {

        final AdditionalAccessoryDetails additionalAccessoryDetail = additionalAccessoryDetails.get(position);

        holder.material_name.setText(additionalAccessoryDetail.getItem_name());
        holder.quantity.setText(additionalAccessoryDetail.getQuantity());
    }

    @Override
    public int getItemCount() {
        return additionalAccessoryDetails.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        TextView material_name,quantity;
        public ActivityHolder(View itemView) {
            super(itemView);

            material_name = itemView.findViewById(R.id.material_name);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }
}
