package in.eoninfotech.eontechnician.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.activity.AdditionalMaterialDetails;
import in.eoninfotech.eontechnician.activity.CallSheetListAdapter;
import in.eoninfotech.eontechnician.activity.ImageDetailCallSheet;
import in.eoninfotech.eontechnician.fragments.AdditionalMaterialViewFragment;
import in.eoninfotech.eontechnician.responses.CallSheetListDetail;

public class AdditionalMaterialViewAdapter extends RecyclerView.Adapter<AdditionalMaterialViewAdapter.ActivityHolder>{

    Context context;
    private final ArrayList<CallSheetListDetail> callSheetDetails;


    public AdditionalMaterialViewAdapter(android.content.Context context, ArrayList<CallSheetListDetail> callSheetDetails) {

        this.context = context;
        this.callSheetDetails = callSheetDetails;
    }

    @NonNull
    @Override
    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdditionalMaterialViewAdapter.ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.call_sheet_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {

        final CallSheetListDetail callSheetDetail = callSheetDetails.get(position);

        holder.date.setText(callSheetDetail.getDate());
        holder.image.setText("View Detail");

        holder.image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(context, AdditionalMaterialDetails.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id_no",callSheetDetail.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return callSheetDetails.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        TextView date,image;
        public ActivityHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.workType);
            image = itemView.findViewById(R.id.textViewAttached);
        }
    }
}
