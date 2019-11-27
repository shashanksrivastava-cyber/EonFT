package in.eoninfotech.eontechnician.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.LiveFaultDataAdapter;
import in.eoninfotech.eontechnician.PvtClientAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.MyPojo;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 27/11/18.
 */

public class PvtClientFragment extends Fragment {

    View v;
    public RecyclerView recyclerView;
    private TextView txtContentUnavailable;
    public SwipeRefreshLayout refreshLayout;
    public LinearLayoutManager layoutManager;
    SharedPreferences sharedprefs;
    String username, dist_id, version;
    ArrayList<MyPojo> liveFault = new ArrayList<>();
    private PvtClientAdapter pvtClientAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_live_status, container, false);
        //  sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        username = getArguments().getString("usernme");
        dist_id = getArguments().getString("disttid");
        version = getArguments().getString("version");
        recyclerView = v.findViewById(R.id.recyclerView);
        txtContentUnavailable = v.findViewById(R.id.txt_content_unavailable);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        refreshLayout = v.findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE);
        refreshLayout.setOnRefreshListener(this::refresh);
        refreshLayout.setRefreshing(true);
        loadData();
        return v;
    }

    private void loadData() {
        ApiHolder loc_att = ServiceConnectionNewURL.getClient().create(ApiHolder.class);
        Call<MyPojo> restCall = loc_att.readUserData(username);
        restCall.enqueue(new Callback<MyPojo>(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {

                if(response.body().getType().equals("1")){

                    MyPojo activityResponse = response.body();
                    txtContentUnavailable.setVisibility(View.GONE);
                    liveFault = activityResponse.getList();
                    //pvtClientAdapter = new PvtClientAdapter(getContext(),liveFault);
                    recyclerView.setAdapter(pvtClientAdapter);
                    refreshLayout.setRefreshing(false);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                else{
                    txtContentUnavailable.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                }
            }
            @Override
            public void onFailure(Call<MyPojo> call, Throwable t) {
                Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void refresh() {
        clear();
        loadData();
    }
    private void clear() {
        liveFault.clear();
    }

}
