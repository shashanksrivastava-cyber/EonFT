package in.eoninfotech.eontechnician.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import in.eoninfotech.eontechnician.ImageUtils;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.ActivityDetailResponse;
import in.eoninfotech.eontechnician.fragments.CallSheetFragment;
import in.eoninfotech.eontechnician.fragments.ViewCallSheetFragment;
import in.eoninfotech.eontechnician.helper.CallSheetDetail;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;

/**
 * Created by root on 26/2/19.
 */

public class CallSheetAdapter extends RecyclerView.Adapter<CallSheetAdapter.ActivityHolder> {

    ViewCallSheetFragment fragment;
   // Activity activity;
    Context context;
    private final ArrayList<CallSheetDetail> callSheetDetails;

    public CallSheetAdapter(Context context, ArrayList<CallSheetDetail> callSheetDetails) {
        this.context = context;
        this.callSheetDetails= callSheetDetails;
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new CallSheetAdapter.ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.call_sheet_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(CallSheetAdapter.ActivityHolder holder, int position) {

        final CallSheetDetail callSheetDetail = callSheetDetails.get(position);

        String test = callSheetDetail.getMonth();
        String s=test.substring(0,3);
        holder.date.setText(s+" "+"("+callSheetDetail.getSheets_count()+")");
        String ImageUri = ServiceConnectionNewURL.BASE_URL+callSheetDetail.getImage();
        ImageUtils.glideImage(holder.image, ImageUri,R.drawable.noimage);

        holder.image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(context, CallSheetList.class);
                intent.putExtra("Image",ImageUri);
                intent.putExtra("month",callSheetDetail.getMonth_id());
                intent.putExtra("year",callSheetDetail.getYear());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return callSheetDetails.size();
    }

    public static class ActivityHolder extends RecyclerView.ViewHolder {

        TextView date;
        ImageView image;
        public ActivityHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.monthName);
            image = itemView.findViewById(R.id.image);
        }
    }
}
