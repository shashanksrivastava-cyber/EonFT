package in.eoninfotech.eontechnician;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.eoninfotech.eontechnician.Responses.ActivityDetailResponse;
import in.eoninfotech.eontechnician.Responses.ClientDataResponse;
import in.eoninfotech.eontechnician.Responses.StockClientDataResponse;
import in.eoninfotech.eontechnician.Responses.StockResponse;
import in.eoninfotech.eontechnician.helper.StockDetail;
import retrofit2.Callback;

/**
 * Created by root on 9/1/19.
 */

public class ViewStockAdapter extends RecyclerView.Adapter<ViewStockAdapter.ActivityHolder> {

    private final ArrayList<StockDetail> activityDetail;

    public ViewStockAdapter(ArrayList<StockDetail> activityDetail, Callback<StockResponse> callback) {

        this.activityDetail = activityDetail;
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewStockAdapter.ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_stock_adapter, parent, false));
    }
    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {

        final StockDetail stockDetail = activityDetail.get(position);

        holder.clientName.setText(stockDetail.getClientname());
        holder.e_cable_2_meter.setText(stockDetail.getCable_2_meter());
        holder.e_cable_7_meter.setText(stockDetail.getCable_7_meter());
        holder.e_drum_sensor_qty.setText(stockDetail.getDrum_sensor_qty());
        holder.e_faulty_vts_qty.setText(stockDetail.getFaulty_vts_qty());
        holder.e_faulty_vts_srno.setText(stockDetail.getFaulty_vts_srno());
        holder.e_magnet_set.setText(stockDetail.getMagnet_set());
        holder.e_remarks.setText(stockDetail.getRemarks());
        holder.e_wrking_vts_qty.setText(stockDetail.getWorking_vts_qty());
        holder.e_wrking_vts_srno.setText(stockDetail.getWorking_vts_srno());
    }

    @Override
    public int getItemCount() {
        return activityDetail.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        TextView e_wrking_vts_qty, e_wrking_vts_srno, e_faulty_vts_qty, e_faulty_vts_srno,
                e_cable_7_meter, e_cable_2_meter, e_drum_sensor_qty, e_drum_sensor_ids,
                e_magnet_set, e_y_cable, e_remarks, e_power_cable, e_vts_remarks,clientName;
        public ActivityHolder(View inflate) {
            super(inflate);

            clientName = inflate.findViewById(R.id.workType);
            e_cable_2_meter = inflate.findViewById(R.id.cable_2_meter);
            e_cable_7_meter = inflate.findViewById(R.id.cable_7_meter);
            e_drum_sensor_qty = inflate.findViewById(R.id.drum_sensor_qty);
            e_faulty_vts_qty = inflate.findViewById(R.id.faulty_vts_qty);
            e_faulty_vts_srno = inflate.findViewById(R.id.faulty_vts_srno);
            e_magnet_set = inflate.findViewById(R.id.magnet_set);
            e_remarks = inflate.findViewById(R.id.remarks);
            e_vts_remarks = (EditText) inflate.findViewById(R.id.vts_remarks);
            e_power_cable = inflate.findViewById(R.id.power_cable);
            e_wrking_vts_qty = inflate.findViewById(R.id.working_vts_qty);
            e_wrking_vts_srno = inflate.findViewById(R.id.working_vts_srno);
            e_y_cable = inflate.findViewById(R.id.y_cable);

        }
    }
}
