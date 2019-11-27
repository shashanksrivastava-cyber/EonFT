package in.eoninfotech.eontechnician.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.ActivityDetailAdapter;
import in.eoninfotech.eontechnician.FaultyDevicesAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.ActivityDetailResponse;
import in.eoninfotech.eontechnician.Responses.FaultyDevices;

/**
 * Created by root on 30/10/18.
 */

public class FaultyFragment extends Fragment {

    View v;
    public RecyclerView recyclerView;
    private FaultyDevicesAdapter faultyDevicesAdapter;
    public LinearLayoutManager layoutManager;
    private TextView txtContentUnavailable;
    public SwipeRefreshLayout refreshLayout;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.faulty_fragment, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);
        txtContentUnavailable = v.findViewById(R.id.txt_content_unavailable);
        refreshLayout = v.findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE);
        refreshLayout.setOnRefreshListener(this::refresh);
        refreshLayout.setRefreshing(true);

//        ArrayList<FaultyDevices>faultyDevices = new ArrayList<>();
//        faultyDevices.add(new FaultyDevices("Moga","Nestle","Number of Faulty VTS: 45","Number of Faulty DRS: 30"));
//        faultyDevices.add(new FaultyDevices("Ludhiana","ACC","Number of Faulty VTS: 40","Number of Faulty DRS: 35"));
//        faultyDevices.add(new FaultyDevices("Delhi","RMC","Number of Faulty VTS: 4","Number of Faulty DRS: 0"));
//        faultyDevices.add(new FaultyDevices("Mumbai","ACC","Number of Faulty VTS: 5","Number of Faulty DRS: 1"));
//        faultyDevices.add(new FaultyDevices("Kanpur","RDC","Number of Faulty VTS: 21","Number of Faulty DRS: 2"));

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //faultyDevicesAdapter = new FaultyDevicesAdapter(faultyDevices,this);
        recyclerView.setAdapter(faultyDevicesAdapter);
        runLayoutAnimation(recyclerView);
        refreshLayout.setRefreshing(false);

        return v;
    }

    private void refresh() {
        refreshLayout.setRefreshing(false);
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

}
