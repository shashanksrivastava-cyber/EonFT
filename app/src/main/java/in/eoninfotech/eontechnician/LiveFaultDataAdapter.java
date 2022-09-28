package in.eoninfotech.eontechnician;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import in.eoninfotech.eontechnician.Responses.MyPojo;
import in.eoninfotech.eontechnician.activity.LiveDataWebView;

/**
 * Created by root on 26/11/18.
 */

public class LiveFaultDataAdapter extends RecyclerView.Adapter<LiveFaultDataAdapter.ActivityHolder> {

   private final ArrayList<MyPojo> liveFault;
    Context context;
    String name;

    public LiveFaultDataAdapter(Context context, ArrayList<MyPojo> liveFault) {

        this.liveFault = liveFault;
        this.context = context;
    }
    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LiveFaultDataAdapter.ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_live_fault, parent, false));
    }

    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {

        final String liveFaults = String.valueOf(liveFault.get(position));
        String currentString = liveFaults;
        String[] separated = currentString.split(",");
        String clientName = separated[0];
        String link = separated[1];
        String[] seperate = clientName.split("\\[");
        String character = seperate[0];
        String Clientstring = seperate[1];

        String[] LinkSep = link.split("\\]");
        String linkString = LinkSep[0];
        String abc = linkString.replaceAll(" ","");

        if(position %2 == 1){

            holder.site_loc.setText(Clientstring);
            holder.site_loc.setBackgroundColor(Color.parseColor("#0082c7"));

        }else {
            holder.site_loc.setText(Clientstring);
            holder.site_loc.setBackgroundColor(Color.parseColor("#00ca87"));
        }

        holder.site_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Clientstring.equalsIgnoreCase("PVT Clients")){

                    Intent intent = new Intent(context, LiveFaultData.class);
                    intent.putExtra("name", abc);
                    context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(context, LiveDataWebView.class);
                    intent.putExtra("name", abc);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return liveFault.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        TextView site_loc;

        public ActivityHolder(View inflate) {
            super(inflate);
            site_loc = (TextView) inflate.findViewById(R.id.acc);

        }
    }
}
