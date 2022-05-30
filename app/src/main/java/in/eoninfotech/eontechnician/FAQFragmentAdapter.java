package in.eoninfotech.eontechnician;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.webservice.FaqResponse;

public class FAQFragmentAdapter extends RecyclerView.Adapter<FAQFragmentAdapter.ActivityHolder> {

    ArrayList<FaqResponse> faqResponses;
    Context context;

    public FAQFragmentAdapter(ArrayList<FaqResponse> faqResponses, Context context) {

        this.context = context;
        this.faqResponses = faqResponses;
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.faq_fragment_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return faqResponses.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {
        public ActivityHolder(View itemView) {
            super(itemView);
        }
    }
}
