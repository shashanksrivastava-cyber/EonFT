package in.eoninfotech.eontechnician.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import in.eoninfotech.eontechnician.LiveFaultData;
import in.eoninfotech.eontechnician.LiveFaultDataAdapter;
import in.eoninfotech.eontechnician.MainActivity;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.LoginResponse;
import in.eoninfotech.eontechnician.Responses.MyPojo;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by root on 22/11/18.
 */

public class LiveStatusFragment extends Fragment {

    View v;
    public RecyclerView recyclerView;
    private TextView txtContentUnavailable;
    public SwipeRefreshLayout refreshLayout;
    public LinearLayoutManager layoutManager;
    String username, dist_id, version;
    //  String[][] liveFault;
    ArrayList<MyPojo> liveFault = new ArrayList<>();
    private LiveFaultDataAdapter liveFaultDataAdapter;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_live_status, container, false);

        sharedprefs = this.getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version","");

        recyclerView = v.findViewById(R.id.recyclerView);
        txtContentUnavailable = v.findViewById(R.id.txt_content_unavailable);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        refreshLayout = v.findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE,Color.GREEN);
        refreshLayout.setOnRefreshListener(this::refresh);
        refreshLayout.setRefreshing(true);
        PulsatorLayout pulsator = v.findViewById(R.id.pulsator);
        pulsator.start();
        loadData();
        return v;
    }

    private void loadData() {
        ApiHolder loc_att = ServiceConnectionNewURL.getClient().create(ApiHolder.class);
        Call<MyPojo> restCall = loc_att.readUserData(username);
        restCall.enqueue(new Callback<MyPojo>(){
            @Override
            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {
                if(response.body().getType().equals("1")){
                    MyPojo activityResponse = response.body();
                    txtContentUnavailable.setVisibility(View.GONE);
                    liveFault = activityResponse.getList();
                    liveFaultDataAdapter = new LiveFaultDataAdapter(getContext(),liveFault);
                    recyclerView.setAdapter(liveFaultDataAdapter);
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

