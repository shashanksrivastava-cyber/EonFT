package in.eoninfotech.eontechnician;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.Responses.MyPojo;
import in.eoninfotech.eontechnician.activity.LiveDataWebView;

/**
 * Created by root on 27/11/18.
 */

public class PvtClientAdapter extends RecyclerView.Adapter<PvtClientAdapter.ActivityHolder> {

    private final ArrayList<MyPojo> liveFault;
    Context context;
    String name;

    public PvtClientAdapter(Context context, ArrayList<MyPojo> liveFault) {

        this.liveFault = liveFault;
        this.context = context;
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_live_fault, parent, false));
    }

    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {

        final String liveFaults = String.valueOf(liveFault.get(position));

        String currentString = liveFaults;
        String[] separated = currentString.split(",");
        String abc = separated[0];
        String def = separated[1];
        String[] seperate = abc.split("\\[");
        String abc1 = seperate[0];
        String def1 = seperate[1];

        String[] sep = def.split("\\]");
        String xyz = sep[0];
        String abcd = xyz.replaceAll(" ","");

        if(position %2 == 1){

            holder.site_loc.setText(def1);
            holder.site_loc.setBackgroundColor(Color.parseColor("#0082c7"));

        }else {
            holder.site_loc.setText(def1);
            holder.site_loc.setBackgroundColor(Color.parseColor("#00ca87"));
        }

        holder.site_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(context, LiveDataWebView.class);
                    intent.putExtra("name", abcd);
                    context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return liveFault.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        TextView site_loc, site_cust_name;

        public ActivityHolder(View inflate) {
            super(inflate);

            site_loc = (TextView) inflate.findViewById(R.id.acc);

        }
    }
}
