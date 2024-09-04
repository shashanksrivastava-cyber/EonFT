package in.eoninfotech.eontechnician.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.activity.CallSheetList;
import in.eoninfotech.eontechnician.activity.CallSheetListAdapter;
import in.eoninfotech.eontechnician.adapters.AdditionalMaterialViewAdapter;
import in.eoninfotech.eontechnician.responses.CallSheetListDetail;
import in.eoninfotech.eontechnician.responses.CallSheetListResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdditionalMaterialViewFragment extends Fragment {

    View v;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String username, dist_id, version,month,year;
    TextView txtContentUnavailable;
    RecyclerView recyclerView;
    AdditionalMaterialViewAdapter additionalMaterialViewAdapter;
    public LinearLayoutManager layoutManager;
    public SwipeRefreshLayout refreshLayout;
    ArrayList<CallSheetListDetail> callSheetDetails = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       v =  inflater.inflate(R.layout.fragment_additional_material_view, container, false);

        sharedprefs = getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("usname", "");
        dist_id = sharedprefs.getString("s_distt", "");
        version = sharedprefs.getString("version", "");

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView = v.findViewById(R.id.recyclerView);
        refreshLayout = v.findViewById(R.id.refresh);
        txtContentUnavailable = v.findViewById(R.id.txt_content_unavailable);
        recyclerView.setLayoutManager(layoutManager);
        refreshLayout.setRefreshing(true);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE);
        refreshLayout.setOnRefreshListener(this::refresh);

        getCurrentMonth();
        getCurrentYear();
        getData();

       return v;
    }

    private void getCurrentYear() {
        year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    }

    private void getCurrentMonth() {
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        Log.d("Month",dateFormat.format(date));
        month = dateFormat.format(date);
    }

    private void refresh() {
     getData();
    }

    public void getData(){
        ApiHolder uploadImage = ServiceConnectionNewURL.getClient().create(ApiHolder.class);
        Call<CallSheetListResponse> call = uploadImage.get_tech_requirement_details(month,year,username);
        call.enqueue(new Callback<CallSheetListResponse>() {
            @Override
            public void onResponse(Call<CallSheetListResponse> call, Response<CallSheetListResponse> response) {
                if (response.body().getType() == 1) {
                    CallSheetListResponse callSheetResponse = response.body();
                    txtContentUnavailable.setVisibility(View.GONE);
                    callSheetDetails = callSheetResponse.getAdd_material_list();
                    additionalMaterialViewAdapter = new AdditionalMaterialViewAdapter(getContext(), callSheetDetails);
                    recyclerView.setAdapter(additionalMaterialViewAdapter);
                    refreshLayout.setRefreshing(false);
                    runLayoutAnimation(recyclerView);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    txtContentUnavailable.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                }
            }
            @Override
            public void onFailure(Call<CallSheetListResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void runLayoutAnimation(RecyclerView recyclerView) {

        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}