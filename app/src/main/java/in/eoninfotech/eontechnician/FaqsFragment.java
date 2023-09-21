package in.eoninfotech.eontechnician;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import in.eoninfotech.eontechnician.Responses.CollectionReportDetail;
import in.eoninfotech.eontechnician.Responses.DashBoardResponse;
import in.eoninfotech.eontechnician.Responses.IncentiveResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.FaqResponse;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class FaqsFragment extends Fragment {

    View v;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String username, dist_id, version;
    public RecyclerView recyclerView;
    public SwipeRefreshLayout refreshLayout;
    public GridLayoutManager layoutManager;
    CardView cv_powerdisconnect;
    LinearLayout ll1, ll2;
    FAQFragmentAdapter faqFragmentAdapter;
    ArrayList<FaqResponse> faqResponses = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fault_list_for_technician, container, false);
        sharedprefs = getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("usname", "");
        dist_id = sharedprefs.getString("s_distt", "");
        version = sharedprefs.getString("version", "");

        final RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        cv_powerdisconnect = v.findViewById(R.id.cv_powerdisconnect);
        ll1 = v.findViewById(R.id.ll1);
        ll2 = v.findViewById(R.id.ll2);

        //loadContent();

        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FaqDetailActivity.class);
                startActivity(intent);
            }
        });

        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FaqDetailActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    private void loadContent() {

        try {
            ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
            Call<FaqResponse> call = log_att.getFaqData();
            call.enqueue(new Callback<FaqResponse>() {
                @Override
                public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {
                    if (response.body().getType().equalsIgnoreCase("1")) {

                        layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        faqFragmentAdapter = new FAQFragmentAdapter(faqResponses, getContext());
                        recyclerView.setAdapter(faqFragmentAdapter);
                    }
                }

                @Override
                public void onFailure(Call<FaqResponse> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getActivity(), "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }
}


