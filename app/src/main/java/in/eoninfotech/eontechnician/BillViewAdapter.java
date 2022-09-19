package in.eoninfotech.eontechnician;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class BillViewAdapter extends RecyclerView.Adapter<BillViewAdapter.ActivityHolder> {
    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_view_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {
        holder.bill_no.setText("B123456789");
        holder.date.setText("19 Sept");
        holder.amount.setText("5000");
        holder.status.setText("Approved");
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        TextView bill_no,amount,status,date;
        TextView textViewAttached;

        public ActivityHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            bill_no = itemView.findViewById(R.id.bill_no);
            amount = itemView.findViewById(R.id.amount);
            status = itemView.findViewById(R.id.status);
        }
    }
}
