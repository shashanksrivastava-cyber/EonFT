package in.eoninfotech.eontechnician;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    String username, dist_id,version;
    public RecyclerView recyclerView;
    public SwipeRefreshLayout refreshLayout;
    public GridLayoutManager layoutManager;
    CardView cv_powerdisconnect;
    LinearLayout ll1,ll2;
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
        layoutManager = new GridLayoutManager(getContext(),2, GridLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(layoutManager);
        cv_powerdisconnect = v.findViewById(R.id.cv_powerdisconnect);
        ll1 = v.findViewById(R.id.ll1);
        ll2 = v.findViewById(R.id.ll2);

        //loadContent();

//        recyclerView.addItemDecoration(new DividerItemDecorator(getActivity()));
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        final List<ItemModel> data = new ArrayList<>();
//        data.add(new ItemModel(
//                "What are the common reasons of a Device Disconnection?",
//                R.color.colorPrimaryDark,
//                R.color.colorPrimaryDark,
//                Utils.createInterpolator(Utils.ACCELERATE_INTERPOLATOR)));
//        data.add(new ItemModel(
//                "What are the common reasons of Power Removed/No Power Supply?\n",
//                R.color.dark_greys,
//                R.color.dark_greys,
//                Utils.createInterpolator(Utils.ACCELERATE_INTERPOLATOR)));
//        data.add(new ItemModel(
//                "What are the common reasons of SOS Tamper?",
//                R.color.dash_red,
//                R.color.dash_red,
//                Utils.createInterpolator(Utils.ACCELERATE_INTERPOLATOR)));
//        data.add(new ItemModel(
//                "3 DECELERATE_INTERPOLATOR",
//                R.color.brown,
//                R.color.brown,
//                Utils.createInterpolator(Utils.DECELERATE_INTERPOLATOR)));
//        data.add(new ItemModel(
//                "4 FAST_OUT_LINEAR_IN_INTERPOLATOR",
//                R.color.yello,
//                R.color.yello,
//                Utils.createInterpolator(Utils.FAST_OUT_LINEAR_IN_INTERPOLATOR)));
//        data.add(new ItemModel(
//                "5 FAST_OUT_SLOW_IN_INTERPOLATOR",
//                R.color.red,
//                R.color.red,
//                Utils.createInterpolator(Utils.FAST_OUT_SLOW_IN_INTERPOLATOR)));
//        data.add(new ItemModel(
//                "6 LINEAR_INTERPOLATOR",
//                R.color.c,
//                R.color.c,
//                Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR)));
//        data.add(new ItemModel(
//                "7 LINEAR_OUT_SLOW_IN_INTERPOLATOR",
//                R.color.material_green_600,
//                R.color.material_green_600,
//                Utils.createInterpolator(Utils.LINEAR_OUT_SLOW_IN_INTERPOLATOR)));
//        recyclerView.setAdapter(new RecyclerViewRecyclerAdapter(data));
//
//        cv_powerdisconnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),FaqDetailActivity.class);
//                startActivity(intent);
//            }
//        });
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FaqDetailActivity.class);
                startActivity(intent);
            }
        });

        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FaqDetailActivity.class);
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
                    if(response.body().getType().equalsIgnoreCase("1")){

                        layoutManager = new GridLayoutManager(getContext(),2, GridLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        faqFragmentAdapter = new FAQFragmentAdapter(faqResponses,getContext());
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


