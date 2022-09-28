package in.eoninfotech.eontechnician;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import in.eoninfotech.eontechnician.Responses.UnderMaintenanceDetail;
import in.eoninfotech.eontechnician.activity.FaultyDevicesActivity;

/**
 * Created by root on 5/11/18.
 */

public class UmainAdapter extends RecyclerView.Adapter<UmainAdapter.ActivityHolder> {

    Context context;
    String sendData;
    TextView site_veh_num;
    private final ArrayList<UnderMaintenanceDetail> uMainDetail;

    public UmainAdapter(ArrayList<UnderMaintenanceDetail> uMainDetail, FaultyDevicesActivity faultyDevicesActivity, String sendData) {

        this.uMainDetail = uMainDetail;
        this.sendData = sendData;
        context = faultyDevicesActivity;
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UmainAdapter.ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_faulty_devices, parent, false));
    }

    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {

        final UnderMaintenanceDetail underMaintenanceDetail = uMainDetail.get(position);

        holder.site_loc.setText(underMaintenanceDetail.getLocation());
        holder.site_cust_name.setText(underMaintenanceDetail.getCustomer());
        holder.faulty_vts.setText("Total Devices : "+underMaintenanceDetail.getTotal_device());
        holder.faulty_drs.setVisibility(View.GONE);
        holder.slide_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.mainvehicledetail);

                site_veh_num = dialog.findViewById(R.id.site_veh_num);
                if((underMaintenanceDetail.getDev_detail()!= null)&&(underMaintenanceDetail.getDev_detail()!="")) {

                    String vehiclenumber = removeBr(underMaintenanceDetail.getDev_detail());
                    site_veh_num.setText(vehiclenumber);
                } else {
                    site_veh_num.setText("No Data Available");
                }
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }
    private String removeBr(String str1){
        return str1.replaceAll(":", " , ");
    }

    @Override
    public int getItemCount() {
        return uMainDetail.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        CardView slide_card;
        TextView site_loc, site_cust_name,faulty_vts,faulty_drs,quantity,tvBought;
        public ActivityHolder(View inflate) {
            super(inflate);

            site_loc = inflate.findViewById(R.id.site_loc);
            site_cust_name = inflate.findViewById(R.id.site_cust_name);
            faulty_vts = inflate.findViewById(R.id.faulty_vts);
            faulty_drs = inflate.findViewById(R.id.faulty_drs);
            slide_card = inflate.findViewById(R.id.slide_card);
            quantity = inflate.findViewById(R.id.quantity);
            tvBought = inflate.findViewById(R.id.tvBought);
        }
    }
}
