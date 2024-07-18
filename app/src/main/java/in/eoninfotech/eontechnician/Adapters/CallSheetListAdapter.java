package in.eoninfotech.eontechnician.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.responses.CallSheetListDetail;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;

/**
 * Created by root on 7/3/19.
 */

public class CallSheetListAdapter extends RecyclerView.Adapter<CallSheetListAdapter.ActivityHolder>{

    Context context;
    private final ArrayList<CallSheetListDetail> callSheetDetails;

    public CallSheetListAdapter(Context context, ArrayList<CallSheetListDetail> callSheetDetails) {

        this.context = context;
        this.callSheetDetails = callSheetDetails;
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CallSheetListAdapter.ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.call_sheet_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(CallSheetListAdapter.ActivityHolder holder, int position) {

        final CallSheetListDetail callSheetDetail = callSheetDetails.get(position);

        holder.date.setText(callSheetDetail.getDate());
        holder.image.setText("View Uploaded Image");

        String ImageUri = ServiceConnectionNewURL.BASE_URL+callSheetDetail.getImage();

        holder.image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(context, ImageDetailCallSheet.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Image",ImageUri);
                intent.putExtra("Date",callSheetDetail.getDate());
                intent.putExtra("Remarks",callSheetDetail.getRemarks());
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
