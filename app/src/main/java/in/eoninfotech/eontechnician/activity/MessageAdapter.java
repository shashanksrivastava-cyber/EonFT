package in.eoninfotech.eontechnician.activity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.eoninfotech.eontechnician.ActivityDetailAdapter;
import in.eoninfotech.eontechnician.AppPreferences;
import in.eoninfotech.eontechnician.ExpandableTextView;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.ActivityDetailResponse;
import in.eoninfotech.eontechnician.Responses.ActivityResponse;
import in.eoninfotech.eontechnician.fragments.ActivityDetailFragment;
import in.eoninfotech.eontechnician.webservice.MessageDetail;
import retrofit2.Callback;

import static com.thefinestartist.utils.content.ContextUtil.getApplicationContext;
import static com.thefinestartist.utils.content.ContextUtil.getResources;

/**
 * Created by root on 7/5/19.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ActivityHolder>implements Filterable {
    ActivityDetailFragment fragment;
    Context context;
    private ArrayList<MessageDetail> activityDetail;
    private final ArrayList<MessageDetail> activityDetails;
    MessageAdapterListener listener;
    AppPreferences appPreferences;
    String months;

    public MessageAdapter(Context context, ArrayList<MessageDetail> activityDetail, MessageAdapterListener listener) {
        this.activityDetail = activityDetail;
        this.activityDetails  =activityDetail;
        this.context = context;
        this.listener = listener;
        appPreferences = new AppPreferences(getApplicationContext());
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {
        appPreferences = new AppPreferences(getApplicationContext());
        final MessageDetail activityDetailResponse = activityDetail.get(position);
        holder.title.setText(activityDetailResponse.getTitle());
        holder.message.setText(activityDetailResponse.getMessage());

        if(activityDetailResponse.getCust_name().equalsIgnoreCase("false")&&(activityDetailResponse.getLoc_name().equalsIgnoreCase("false"))){
          holder.lClient.setVisibility(View.GONE);
        }else{
            holder.lClient.setVisibility(View.VISIBLE);
            holder.clientName.setText(activityDetailResponse.getCust_name());
            holder.cLocation.setText(activityDetailResponse.getLoc_name());
        }
            String date = activityDetailResponse.getDatetime();
            String[] newDate = date.split("/");
            String dates = newDate[0];
            String month = newDate[1];
            String year = newDate[2];

            if (month.equals("01")) {
                months = "Jan";
            } else if (month.equals("02")) {
                months = "Feb";
            } else if (month.equals("03")) {
                months = "Mar";
            } else if (month.equals("04")) {
                months = "Apr";
            } else if (month.equals("05")) {
                months = "May";
            } else if (month.equals("06")) {
                months = "Jun";
            } else if (month.equals("07")) {
                months = "Jul";
            } else if (month.equals("08")) {
                months = "Aug";
            } else if (month.equals("09")) {
                months = "Sep";
            } else if (month.equals("10")) {
                months = "Oct";
            } else if (month.equals("11")) {
                months = "Nov";
            } else if (month.equals("12")) {
                months = "Dec";
            }
            String dateTobeShown = dates + "-" + months + "-" + year;
            holder.date.setText(dateTobeShown);
            holder.sender.setText(activityDetailResponse.getSender());
        if(activityDetailResponse.getMsg_type().equalsIgnoreCase("G")){
            holder.panic_fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_green_600)));
            holder.textViewCounter.setText("G");
        }else{
            holder.panic_fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorAccent)));
            holder.textViewCounter.setText("F");
        }
            applyReadStatus(holder, activityDetailResponse);
            holder.messageContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onMessageRowClicked(position);
                }
            });
        }

    private void applyReadStatus(ActivityHolder holder, MessageDetail activityDetailResponse) {
        if(activityDetailResponse.getStatus().equals("1")) {
            holder.title.setTypeface(null, Typeface.NORMAL);
            holder.message.setTypeface(null, Typeface.NORMAL);
            holder.clientName.setTypeface(null, Typeface.NORMAL);
            holder.cLocation.setTypeface(null, Typeface.NORMAL);
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.dark_greys));
            holder.message.setTextColor(ContextCompat.getColor(context, R.color.dark_greys));
            holder.clientName.setTextColor(ContextCompat.getColor(context, R.color.dark_greys));
            holder.cLocation.setTextColor(ContextCompat.getColor(context, R.color.dark_greys));

        }else{
            holder.title.setTypeface(null, Typeface.BOLD);
            holder.message.setTypeface(null, Typeface.BOLD);
            holder.clientName.setTypeface(null, Typeface.BOLD);
            holder.cLocation.setTypeface(null, Typeface.BOLD);
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.message.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.clientName.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.cLocation.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
    }
    @Override
    public int getItemCount() {
        return activityDetail.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    activityDetail = activityDetails;
                } else {
                    ArrayList<MessageDetail> filteredList = new ArrayList<>();
                    for (MessageDetail row : activityDetails) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getMessage().toLowerCase().contains(charString.toLowerCase())||row.getSender().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    activityDetail = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = activityDetail;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                activityDetail = (ArrayList<MessageDetail>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface MessageAdapterListener {

        void onMessageRowClicked(int position);
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        TextView title,date,sender,textViewCounter,clientName,cLocation,message;
        LinearLayout messageContainer,lClient;
        CircleImageView ivProfile;
        FloatingActionButton panic_fab;
       // ExpandableTextView message;
        public ActivityHolder(View inflate) {
            super(inflate);
            title = inflate.findViewById(R.id.title);
            message = inflate.findViewById(R.id.message);
            date = inflate.findViewById(R.id.date);
            sender = inflate.findViewById(R.id.sender);
            textViewCounter = inflate.findViewById(R.id.textViewCounter);
            messageContainer = inflate.findViewById(R.id.messageContainer);
            panic_fab= inflate.findViewById(R.id.panic_fab);
            clientName = inflate.findViewById(R.id.clientName);
            cLocation = inflate.findViewById(R.id.cLocation);
            lClient = inflate.findViewById(R.id.lClient);
        }
    }
}
