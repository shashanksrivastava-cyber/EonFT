package in.eoninfotech.eontechnician.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.responses.CallSheetListDetail;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;

/**
 * Created by root on 7/3/19.
 */


public class CallSheetListAdapter extends RecyclerView.Adapter<CallSheetListAdapter.ActivityHolder> {

    private final Context context;
    private final List<CallSheetListDetail> callSheetDetails = new ArrayList<>();

    public CallSheetListAdapter(Context context, List<CallSheetListDetail> callSheetDetails) {
        this.context = context;
        if (callSheetDetails != null) {
            this.callSheetDetails.addAll(callSheetDetails);
        }
    }

    @NonNull
    @Override
    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivityHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.call_sheet_adapter, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {

        CallSheetListDetail item = callSheetDetails.get(position);

        holder.date.setText(item.getDate());
        holder.image.setText("View Uploaded Image");

        String imageUrl = ServiceConnectionNewURL.BASE_URL + item.getImage();

        holder.image.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImageDetailCallSheet.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("Image", imageUrl);
            intent.putExtra("Date", item.getDate());
            intent.putExtra("Remarks", item.getRemarks());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return callSheetDetails.size();
    }

    public static class ActivityHolder extends RecyclerView.ViewHolder {

        TextView date, image;

        public ActivityHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.workType);
            image = itemView.findViewById(R.id.textViewAttached);
        }
    }

    // -----------------------------------------------------
    // 🚀 DiffUtil Update Method
    // -----------------------------------------------------
    public void updateList(List<CallSheetListDetail> newList) {

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {

            @Override
            public int getOldListSize() {
                return callSheetDetails.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldPos, int newPos) {
                // Unique key → Date + Image URL
                CallSheetListDetail oldItem = callSheetDetails.get(oldPos);
                CallSheetListDetail newItem = newList.get(newPos);

                return oldItem.getDate().equals(newItem.getDate())
                        && oldItem.getImage().equals(newItem.getImage());
            }

            @Override
            public boolean areContentsTheSame(int oldPos, int newPos) {
                return callSheetDetails.get(oldPos).equals(newList.get(newPos));
            }
        });

        callSheetDetails.clear();
        callSheetDetails.addAll(newList);

        diffResult.dispatchUpdatesTo(this);
    }
}
