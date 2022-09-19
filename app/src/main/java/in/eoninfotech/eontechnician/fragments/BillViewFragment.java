package in.eoninfotech.eontechnician.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.eoninfotech.eontechnician.BillViewAdapter;
import in.eoninfotech.eontechnician.R;


public class BillViewFragment extends Fragment {

    RecyclerView recyclerView;
    BillViewAdapter billViewAdapter;
    public LinearLayoutManager layoutManager;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_bill_view, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        billViewAdapter = new BillViewAdapter();
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(billViewAdapter);

        return v;
    }
}