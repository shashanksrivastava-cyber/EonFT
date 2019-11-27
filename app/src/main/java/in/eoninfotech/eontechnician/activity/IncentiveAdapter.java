package in.eoninfotech.eontechnician.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.eoninfotech.eontechnician.ActivityDetailAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.ActivityDetailResponse;
import in.eoninfotech.eontechnician.helper.IncentiveDetail;
import in.eoninfotech.eontechnician.helper.ListIncentiveDetail;

/**
 * Created by root on 25/3/19.
 */

public class IncentiveAdapter extends RecyclerView.Adapter<IncentiveAdapter.ActivityHolder> {

    private final ArrayList<ListIncentiveDetail> incentiveDetail;

    public IncentiveAdapter(Context context, ArrayList<ListIncentiveDetail> incentiveDetails) {

        this.incentiveDetail = incentiveDetails;
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IncentiveAdapter.ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_incentive, parent, false));
    }

    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {

        final ListIncentiveDetail incentiveDetailResponse = incentiveDetail.get(position);
        holder.txt_add.setText(incentiveDetailResponse.getAdd_cnt());
        String color1  = incentiveDetailResponse.getAdd_color();
        String[] separated = color1.split(";");
        String color = separated[0];
        int myColor = Color.parseColor(color);
        holder.txt_add.setBackgroundColor(myColor);
        holder.txt_attendance.setText(incentiveDetailResponse.getAttendance());
        holder.txt_activity.setText(incentiveDetailResponse.getActivity());
        holder.txt_stock.setText(incentiveDetailResponse.getStock());
        holder.txt_callSheet.setText(incentiveDetailResponse.getCall_sheets());
        holder.txt_paymentCollection.setText(incentiveDetailResponse.getPayment_collection());

        if(incentiveDetailResponse.getIncentive_amt().equalsIgnoreCase("false")){
            holder.txt_incentive.setText("");
        }else {
            holder.txt_incentive.setText(incentiveDetailResponse.getIncentive_amt());
        }
    }

    @Override
    public int getItemCount() {
        return incentiveDetail.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        TextView txt_add,txt_attendance,txt_activity,txt_stock,txt_callSheet,txt_paymentCollection,txt_incentive;
        public ActivityHolder(View inflate) {
            super(inflate);
            txt_add = inflate.findViewById(R.id.txt_add);
            txt_attendance = inflate.findViewById(R.id.txt_attendance);
            txt_activity = inflate.findViewById(R.id.txt_activity);
            txt_stock = inflate.findViewById(R.id.txt_stock);
            txt_callSheet = inflate.findViewById(R.id.txt_callSheet);
            txt_paymentCollection = inflate.findViewById(R.id.txt_paymentCollection);
            txt_incentive = inflate.findViewById(R.id.txt_incentive);
        }
    }
}
