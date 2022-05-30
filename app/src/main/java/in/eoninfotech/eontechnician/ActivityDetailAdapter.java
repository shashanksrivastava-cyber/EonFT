package in.eoninfotech.eontechnician;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.eoninfotech.eontechnician.Responses.ActivityDetailResponse;

import in.eoninfotech.eontechnician.activity.ImageDetailActivity;
import in.eoninfotech.eontechnician.fragments.ActivityDetailFragment;

import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;


/**
 * Created by root on 16/10/18.
 */

public class ActivityDetailAdapter extends RecyclerView.Adapter<ActivityDetailAdapter.ActivityHolder> {

    ActivityDetailFragment fragment;
    Context context;
    String date;
    private final ArrayList<ActivityDetailResponse> activityDetail;

    public ActivityDetailAdapter(Context context, ArrayList<ActivityDetailResponse> activityDetail, String s_date) {
        this.activityDetail = activityDetail;
        this.context = context;
        this.date=s_date;
    }
    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_activity_detail, parent, false));
    }
    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {

        String newDate = null;
        final ActivityDetailResponse activityDetailResponse = activityDetail.get(position);
        holder.activity.setText(activityDetailResponse.getActivity());
        holder.workType.setText(activityDetailResponse.getActivity());
        holder.clientName.setText(activityDetailResponse.getClient_Name());
        String image = activityDetailResponse.getCollected_img();
        String imageUri = ServiceConnectionNewURL.BASE_URL + image;
        String abc = activityDetailResponse.getCollected_date();
        if (!abc.equals("")&&(!abc.equals("0")&&(!abc.equals(null))) ) {
            String[] separated = abc.split("-");
            String date =  separated[0];
            String month = separated[1];
            String years = separated[2];
            newDate = date + "-" + month + "-" + years;
        } else {
        }
        String activity_id = activityDetailResponse.getActivity_id();
        String reason = removeBr(activityDetailResponse.getReasons());
        if (activity_id.equals("2")) {
            holder.textViewAttached.setVisibility(View.GONE);
            if (activityDetailResponse.getNew_drs().equals("0")&&(activityDetailResponse.getNew_vts().equals("0")&&(activityDetailResponse.getOld_vts().equals("0")))) {
                holder.data.setText("VTS with Sr.no. " + "" + activityDetailResponse.getNew_serial_no() + " " + "is installed on" + " " + activityDetailResponse.getReg_no()
                        + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc());
            } else if((activityDetailResponse.getNew_drs().equals("0"))) {
                holder.data.setText("VTS ID " + "" + activityDetailResponse.getNew_vts() + " " + "is installed on" + " " + activityDetailResponse.getReg_no()
                        + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc());
            }else{
                holder.data.setText("VTS ID " + "" + activityDetailResponse.getNew_vts() + " " + "is installed on" + " " + activityDetailResponse.getReg_no()
                        + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc() + "," + "New DRS ID" + "  " + activityDetailResponse.getNew_drs());
            }holder.image.setImageResource(R.drawable.settings);
        }
        if (activity_id.equals("6")) {
            holder.textViewAttached.setVisibility(View.GONE);
            holder.data.setText("Phone Support is given to VTS ID " + "" + activityDetailResponse.getOld_vts() + " " + " installed on" + " " + activityDetailResponse.getReg_no()
                    + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc() + " " + "due to" + " " + reason);
            holder.image.setImageResource(R.drawable.call_center);
        }
        if (activity_id.equals("5")) {
            holder.textViewAttached.setVisibility(View.GONE);
            holder.data.setText("VTS ID " + "" + activityDetailResponse.getOld_vts() + " " + "is removed from" + " " + activityDetailResponse.getReg_no()
                    + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc() + " " + "due to" + " " + reason);
            holder.image.setImageResource(R.drawable.remove);
        }
        if (activity_id.equals("1")) {
            if (!imageUri.contains("jpg")&&(!activityDetailResponse.getOld_vts().equals("0"))) {
                holder.textViewAttached.setVisibility(View.GONE);
                holder.data.setText("Fault Repair is done for VTS ID " + "" + activityDetailResponse.getOld_vts() + " " + "installed on" + " " + activityDetailResponse.getReg_no()
                        + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc() + " " + "due to" + " " + reason);
                holder.image.setImageResource(R.drawable.ic_disc_full_black_24dp);
            }else if(!imageUri.contains("jpg")&&(!activityDetailResponse.getOld_serial_no().equals(""))){
                holder.textViewAttached.setVisibility(View.GONE);
                holder.data.setText("Fault Repair is done for VTS Sr No. " + "" + activityDetailResponse.getOld_serial_no() + " " + "installed on" + " " + activityDetailResponse.getReg_no()
                        + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc() + " " + "due to" + " " + reason);
                holder.image.setImageResource(R.drawable.ic_disc_full_black_24dp);
            }else if(imageUri.contains("jpg")&&(!activityDetailResponse.getOld_vts().equals("0"))){
                holder.textViewAttached.setVisibility(View.VISIBLE);
                holder.data.setText("Fault Repair is done for VTS ID " + "" + activityDetailResponse.getOld_vts() + " " + "installed on" + " " + activityDetailResponse.getReg_no()
                        + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc() + " " + "due to" + " " + reason);
                holder.image.setImageResource(R.drawable.ic_disc_full_black_24dp);
            }else if(imageUri.contains("jpg")&&(!activityDetailResponse.getOld_serial_no().equals(""))){
                holder.textViewAttached.setVisibility(View.VISIBLE);
                holder.data.setText("Fault Repair is done for VTS Sr No. " + "" + activityDetailResponse.getOld_serial_no() + " " + "installed on" + " " + activityDetailResponse.getReg_no()
                        + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc() + " " + "due to" + " " + reason);
                holder.image.setImageResource(R.drawable.ic_disc_full_black_24dp);
            }
        }if (activity_id.equals("3")) {
            holder.textViewAttached.setVisibility(View.GONE);
             if(activityDetailResponse.getNew_drs().equals("0")&&(activityDetailResponse.getNew_vts().equals("0")&&(activityDetailResponse.getOld_vts().equals("0")))){
                 holder.data.setText("VTS with Sr.no. " + "" + activityDetailResponse.getOld_serial_no() + " " + "is replaced with" + " " + activityDetailResponse.getNew_serial_no() + " " + "on" + " " + activityDetailResponse.getReg_no()
                         + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc() + " " + "due to" + " " + reason);
            } else if (activityDetailResponse.getOld_drs().equals("0") && (activityDetailResponse.getNew_drs().equals("0"))) {
                holder.data.setText("VTS ID " + "" + activityDetailResponse.getOld_vts() + " " + "is replaced with" + " " + activityDetailResponse.getNew_vts() + " " + "on" + " " + activityDetailResponse.getReg_no()
                        + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc() + " " + "due to" + " " + reason);
            } else if ((activityDetailResponse.getNew_vts().equals("0"))) {
                holder.data.setText("VTS ID " + "" + activityDetailResponse.getOld_vts() + " " + "Vehicle No. " + activityDetailResponse.getReg_no()
                        + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc() + " " + "& Old DRS ID " + activityDetailResponse.getOld_drs() + "," + "New DRS ID" + "  " + activityDetailResponse.getNew_drs());
            }else if(activityDetailResponse.getNew_vts().equals("0")&&(activityDetailResponse.getOld_vts().equals("0"))){
                holder.data.setText("VTS with Serial No. " + "" + activityDetailResponse.getOld_serial_no() + " " + "is replaced with" + " " + activityDetailResponse.getNew_serial_no() + " " + "on" + " " + activityDetailResponse.getReg_no()
                        + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc() + " " + "due to" + " " + reason);
            } else {
                holder.data.setText("VTS ID " + "" + activityDetailResponse.getOld_vts() + " " + "is replaced with" + " " + activityDetailResponse.getNew_vts() + " " + "on" + " " + activityDetailResponse.getReg_no()
                        + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc() + " " + "due to" + " " + reason + "  " + "& Old DRS ID " + activityDetailResponse.getOld_drs() + "," + "New DRS ID" + "  " + activityDetailResponse.getNew_drs());
            }
            holder.image.setImageResource(R.drawable.responsive);
        }
        if (activity_id.equals("4")) {
            holder.textViewAttached.setVisibility(View.GONE);
            if(activityDetailResponse.getNew_drs().equals("0")&&(activityDetailResponse.getNew_vts().equals("0")&&(activityDetailResponse.getNew_serial_no().equals("0")))){
                holder.data.setText("VTS with Sr.no.  " + "" + activityDetailResponse.getOld_serial_no() + " " + "is reinstall on" + " " + activityDetailResponse.getReg_no()
                        + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc());
            } else if (activityDetailResponse.getNew_vts().equals("0") && (activityDetailResponse.getNew_drs().equals("0"))) {
                holder.data.setText("VTS ID " + "" + activityDetailResponse.getOld_vts() + " " + "is reinstall on" + " " + activityDetailResponse.getReg_no()
                        + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc());
            } else if (!activityDetailResponse.getNew_drs().equals("0") && (activityDetailResponse.getNew_vts().equals("0"))) {
                holder.data.setText("VTS ID " + "" + activityDetailResponse.getOld_vts() + " " + "is reinstall on" + " " + activityDetailResponse.getReg_no()
                        + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc() + " , " + "DRS ID" + " " + activityDetailResponse.getNew_drs());
            } else if (activityDetailResponse.getNew_vts().equals("0") && (activityDetailResponse.getNew_drs().equals("0"))) {
                holder.data.setText("New VTS ID " + "" + activityDetailResponse.getNew_vts() + " " + "Vehicle No." + " " + activityDetailResponse.getReg_no() + " " + "Old VTS ID" + " " + activityDetailResponse.getOld_vts()
                        + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc());
            } else if (!activityDetailResponse.getNew_vts().equals("0") && (activityDetailResponse.getNew_drs().equals("0"))) {
                holder.data.setText("New VTS ID " + " " + activityDetailResponse.getNew_vts() + " " + "is reinstall on" + " " + activityDetailResponse.getReg_no() + " " + "Old VTS" + " " + activityDetailResponse.getOld_vts()
                        + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc());
            } else {
                holder.data.setText("New VTS ID " + "" + activityDetailResponse.getNew_vts() + " " + "Vehicle No." + " " + activityDetailResponse.getReg_no() + " " + "Old VTS ID" + " " + activityDetailResponse.getOld_vts()
                        + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc() + " , " + "DRS ID" + " " + activityDetailResponse.getNew_drs());
            }
            holder.image.setImageResource(R.drawable.ic_crop_rotate_black_24dp);
        }
        if (activity_id.equals("7")) {
            holder.textViewAttached.setVisibility(View.GONE);
            if (activityDetailResponse.getOld_sim().equals("")) {
                holder.data.setText("New SIM " + "" + activityDetailResponse.getNew_sim() + " " + "of " + " " + activityDetailResponse.getSim_opr() + " is replaced on VTS " + activityDetailResponse.getOld_vts()
                        + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc() + " " + "due to" + " " + reason);
            } else {
                holder.data.setText("New SIM " + "" + activityDetailResponse.getNew_sim() + " " + "of " + " " + activityDetailResponse.getSim_opr() + " is replaced on VTS " + activityDetailResponse.getOld_vts()
                        + " " + "at" + " " + activityDetailResponse.getClient_Name() + " " + "at" + " " + activityDetailResponse.getClient_loc() + " " + "due to" + " " + reason + " " + "having Old Sim No. " + " " + activityDetailResponse.getOld_sim());
            }
            holder.image.setImageResource(R.drawable.dual_sim);
        }
        if (activity_id.equals("8")) {
            if (!imageUri.contains("jpg")&&(!activityDetailResponse.getOld_vts().equals("0"))) {
                holder.textViewAttached.setVisibility(View.GONE);
                holder.data.setText("VTS ID " + "" + activityDetailResponse.getOld_vts() + " " + "is missing from " + " " + activityDetailResponse.getReg_no() + " " + "at" +
                        " " + activityDetailResponse.getClient_Name() + " " + "at " + activityDetailResponse.getClient_loc());
                holder.image.setImageResource(R.drawable.ic_phonelink_erase_black_24dp);
            }else if(!imageUri.contains("jpg")&&(!activityDetailResponse.getOld_serial_no().equals(""))){
                holder.textViewAttached.setVisibility(View.GONE);
                holder.data.setText("VTS ID with Sr no " + "" + activityDetailResponse.getOld_serial_no() + " " + "is missing from " + " " + activityDetailResponse.getReg_no() + " " + "at" +
                        " " + activityDetailResponse.getClient_Name() + " " + "at " + activityDetailResponse.getClient_loc());
                holder.image.setImageResource(R.drawable.ic_phonelink_erase_black_24dp);
            }else if(imageUri.contains("jpg")&&(!activityDetailResponse.getOld_vts().equals("0"))){
                holder.textViewAttached.setVisibility(View.VISIBLE);
                holder.data.setText("VTS ID " + "" + activityDetailResponse.getOld_vts() + " " + "is missing from " + " " + activityDetailResponse.getReg_no() + " " + "at" +
                        " " + activityDetailResponse.getClient_Name() + " " + "at " + activityDetailResponse.getClient_loc());
                holder.image.setImageResource(R.drawable.ic_phonelink_erase_black_24dp);
            }else if(imageUri.contains("jpg")&&(!activityDetailResponse.getOld_serial_no().equals(""))) {
                holder.textViewAttached.setVisibility(View.VISIBLE);
                holder.data.setText("VTS ID with Sr no " + "" + activityDetailResponse.getOld_serial_no() + " " + "is missing from " + " " + activityDetailResponse.getReg_no() + " " + "at" +
                        " " + activityDetailResponse.getClient_Name() + " " + "at " + activityDetailResponse.getClient_loc());
                holder.image.setImageResource(R.drawable.ic_phonelink_erase_black_24dp);
            }
            else{
                holder.textViewAttached.setVisibility(View.VISIBLE);
                holder.data.setText("VTS ID " + "" + activityDetailResponse.getOld_vts() + " " + "is missing from " + " " + activityDetailResponse.getReg_no() + " " + "at" +
                        " " + activityDetailResponse.getClient_Name() + " " + "at " + activityDetailResponse.getClient_loc());
                holder.image.setImageResource(R.drawable.ic_phonelink_erase_black_24dp);
            }
        }
        if (activity_id.equals("9")) {
            holder.textViewAttached.setVisibility(View.GONE);
            holder.data.setText("Vehicle " + "" + activityDetailResponse.getReg_no() + " " + "is not available" + "" + "due to " + " " + activityDetailResponse.getReasons());
        }if (activity_id.equals("10")) {
            if (!imageUri.contains("jpg")) {
                holder.textViewAttached.setVisibility(View.GONE);
                holder.data.setText("Payment of " + "₹" + " " + "" + activityDetailResponse.getCollected_amt() + " " + "is collected from " + "" + activityDetailResponse.getClient_Name() + " " + "on" + " " + newDate
                        + " " + "through " + "" + activityDetailResponse.getCollected_mtd());
                holder.image.setImageResource(R.drawable.get_money);
            } else {
                holder.textViewAttached.setVisibility(View.VISIBLE);
                holder.data.setText("Payment of " + "₹" + " " + "" + activityDetailResponse.getCollected_amt() + " " + "is collected from " + "" + activityDetailResponse.getClient_Name() + " " + "on" + " " + newDate
                        + " " + "through " + "" + activityDetailResponse.getCollected_mtd());
                holder.image.setImageResource(R.drawable.get_money);
            }
        }if(activity_id.equals("11")){
            holder.data.setText(activityDetailResponse.getRemarks());
        }
        holder.textViewAttached.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ImageDetailActivity.class);
                        intent.putExtra("Image",imageUri);
                        intent.putExtra("Remarks",activityDetailResponse.getRemarks());
                        intent.putExtra("Dates",date);
                        context.startActivity(intent);
            }
        });
    }
    private String removeBr(String str1){
        return str1.replaceAll("<br/>", ",");
    }

    @Override
    public int getItemCount() {
        return activityDetail.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        TextView activity,data,workType,clientName;
        ImageView image;
        CircleImageView image1;
        RelativeLayout relImage;
        TextView textViewAttached;
        public ActivityHolder(View inflate) {
            super(inflate);
            activity = inflate.findViewById(R.id.activity);
            data = inflate.findViewById(R.id.data);
            image = inflate.findViewById(R.id.image);
            workType = inflate.findViewById(R.id.workType);
            clientName = inflate.findViewById(R.id.clientName);
           // image1  = (CircleImageView) inflate.findViewById(R.id.image1);
           // relImage = (RelativeLayout)inflate.findViewById(R.id.relImage);
            textViewAttached = inflate.findViewById(R.id.textViewAttached);
        }
    }
}
