package in.eoninfotech.eontechnician;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

import in.eoninfotech.eontechnician.responses.CollectionReportDetail;

/**
 * Created by root on 13/3/19.
 */

public class PaymentCollectionAdapter extends RecyclerView.Adapter<PaymentCollectionAdapter.ActivityHolder> {

    ArrayList<CollectionReportDetail> collectionReportDetails = new ArrayList<>();
    Context context;
    View view;

    public PaymentCollectionAdapter(Context context, ArrayList<CollectionReportDetail> collectionReportDetails) {
        this.collectionReportDetails = collectionReportDetails;
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PaymentCollectionAdapter.ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_payment_collection_report, parent, false));
    }

    @Override
    public void onBindViewHolder(PaymentCollectionAdapter.ActivityHolder holder, int position) {

        final CollectionReportDetail collectionReportDetail = collectionReportDetails.get(position);
        holder.date.setText(collectionReportDetail.getCollection_date());
        holder.clientName.setText(collectionReportDetail.getCustomer_name());
        holder.location.setText(collectionReportDetail.getCustomer_location());
        holder.amount.setText(collectionReportDetail.getCollection_amount());
        holder.method.setText(collectionReportDetail.getCollection_method());

        if(collectionReportDetail.getVerified_status().equalsIgnoreCase("2")){
            holder.verified.setImageResource(R.drawable.ic_check_circle_black_24dp);

        }else {
            holder.verified.setImageResource(R.drawable.ic_error_black_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return collectionReportDetails.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        TextView date,clientName,location,amount,method;
        ImageView verified ;
        public ActivityHolder(View inflate) {
            super(inflate);
            date = inflate.findViewById(R.id.date);
            location = inflate.findViewById(R.id.location);
            method = inflate.findViewById(R.id.method);
            clientName = inflate.findViewById(R.id.clientName);
            amount = inflate.findViewById(R.id.amount);
            verified = inflate.findViewById(R.id.verified);
        }
    }
}
