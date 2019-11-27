package in.eoninfotech.eontechnician;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.Responses.ActivityDetailResponse;
import in.eoninfotech.eontechnician.Responses.InstInstructionResponse;
import in.eoninfotech.eontechnician.Responses.InstructionDetail;
import in.eoninfotech.eontechnician.activity.MessageAdapter;
import in.eoninfotech.eontechnician.fragments.ActivityDetailFragment;
import in.eoninfotech.eontechnician.webservice.MessageDetail;
import retrofit2.Callback;

/**
 * Created by root on 3/12/18.
 */

public class InstInstructionAdapter extends RecyclerView.Adapter<InstInstructionAdapter.ActivityHolder> {

    private final ArrayList<InstructionDetail> instDetails;
    private final ArrayList<InstructionDetail> instDetail;
    String months;
    Context context;
    InstInstructionAdapterListener listener;
    String custName,location;

//    public InstInstructionAdapter(Context context,ArrayList<InstructionDetail> instructionDetails,InstInstructionAdapterListener listener ) {
//        this.instDetails = instructionDetails;
//        this.context = context;
//        this.listener = listener;
//        this.instDetail = instructionDetails;
//    }

    public InstInstructionAdapter(Context applicationContext, ArrayList<InstructionDetail> instructionDetails, InstInstructionAdapterListener listener, String custName, String location) {
        this.instDetails = instructionDetails;
        this.context = context;
        this.listener = listener;
        this.instDetail = instructionDetails;
        this.custName = custName;
        this.location = location;
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InstInstructionAdapter.ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_message, parent, false));
    }

    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {

        InstructionDetail instructionDetail = instDetails.get(position);
        String date  = instructionDetail.getDate();
        String[] splited = date.split("\\s+");
        String datee = splited[0];
        String time = splited[1];
        String[] newDate = datee.split("/");
        String dates = newDate[0];
        String month = newDate[1];
        String year = newDate[2];

        if(month.equals("01")){
            months = "Jan";
        }else if(month.equals("02")){
            months = "Feb";
        }else if(month.equals("03")){
            months = "Mar";
        }else if(month.equals("04")){
            months = "Apr";
        }else if(month.equals("05")){
            months = "May";
        }else if(month.equals("06")){
            months = "Jun";
        }else if(month.equals("07")){
            months = "Jul";
        }else if(month.equals("08")){
            months = "Aug";
        }else if(month.equals("09")){
            months = "Sep";
        }else if(month.equals("10")){
            months = "Oct";
        }else if(month.equals("11")){
            months = "Nov";
        }else if(month.equals("12")){
            months = "Dec";
        }
        String dateTobeShown = dates+"-"+months+"-"+year+" " + time;
        holder.date.setText(dateTobeShown);

       // holder.date.setText(instructionDetail.getDate());
        holder.from.setText(instructionDetail.getSender());
        //holder.message.setText(instructionDetail.getMessage());
        holder.clientName.setText(instructionDetail.getCust_name());
        holder.cLocation.setText(instructionDetail.getLoc_name());
        holder.title.setText(instructionDetail.getTitle());
        holder.message.setText(instructionDetail.getMessage());

        applyReadStatus(holder, instructionDetail);
        holder.messageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMessageRowClicked(position);
            }
        });
    }

    private void applyReadStatus(InstInstructionAdapter.ActivityHolder holder, InstructionDetail
            instructionDetail) {
        if(instructionDetail.getStatus().equals("1")) {
//            holder.title.setTypeface(null, Typeface.NORMAL);
//            holder.message.setTypeface(null, Typeface.NORMAL);
//            holder.title.setTextColor(ContextCompat.getColor(context, R.color.dark_greys));
//            holder.message.setTextColor(ContextCompat.getColor(context, R.color.dark_greys));
        }else{
            holder.title.setTypeface(null, Typeface.BOLD);
            holder.message.setTypeface(null, Typeface.BOLD);
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.message.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
    }

    public interface InstInstructionAdapterListener {

        void onMessageRowClicked(int position);
    }

    @Override
    public int getItemCount() {
        return instDetails.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        TextView date,from,clientName,cLocation,title;
        ExpandableTextView message;
        LinearLayout messageContainer;
        public ActivityHolder(View inflate) {
            super(inflate);
            date = inflate.findViewById(R.id.date);
            from = inflate.findViewById(R.id.from);
            message = inflate.findViewById(R.id.message);
            clientName = inflate.findViewById(R.id.clientName);
            cLocation = inflate.findViewById(R.id.cLocation);
            messageContainer = inflate.findViewById(R.id.messageContainer);
            title  = inflate.findViewById(R.id.title);
        }
    }
}
