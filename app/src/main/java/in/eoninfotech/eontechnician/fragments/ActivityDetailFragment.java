package in.eoninfotech.eontechnician.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import in.eoninfotech.eontechnician.ActivityDetailAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.responses.ActivityDetailResponse;
import in.eoninfotech.eontechnician.responses.ActivityResponse;
import in.eoninfotech.eontechnician.viewModel.ViewModelActivityDetails;
import in.eoninfotech.eontechnician.viewModel.ViewModelClientLocation;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by root on 16/10/18.
 */

public class ActivityDetailFragment extends Fragment {

    View v;
    String current_date, selected_todate, s_date;
    String username, dist_id, version,user_id;
    public RecyclerView recyclerView;
    public SwipeRefreshLayout refreshLayout;
    public LinearLayoutManager layoutManager;
    public LinearLayout linearActivity;
    ImageView forward;
    //EditText t_install_date;
    TextView t_install_date;
    ShimmerFrameLayout mShimmerViewContainer;
    int year, month, day, hour, minutes;
    Calendar calen = Calendar.getInstance();
    private TextView txtContentUnavailable;
    private ActivityDetailAdapter activityDetailAdapter;
    ProgressBar progressBars;
    ArrayList<ActivityDetailResponse> activityDetail = new ArrayList<>();
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    ViewModelActivityDetails viewModelActivityDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_detail, container, false);
        sharedprefs = this.getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        user_id = sharedprefs.getString("s_user_id","");
        refreshLayout = v.findViewById(R.id.refresh);
        recyclerView = v.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        t_install_date = v.findViewById(R.id.installDate);
        txtContentUnavailable = v.findViewById(R.id.txt_content_unavailable);

        linearActivity = v.findViewById(R.id.llContent);
        setDateAndTime();
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE,Color.GREEN);
        refreshLayout.setOnRefreshListener(this::refresh);
        refreshLayout.setRefreshing(true);
        t_install_date.setInputType(InputType.TYPE_NULL);
        progressBars = v.findViewById(R.id.progressBars);
        mShimmerViewContainer = v.findViewById(R.id.shimmer_view_container);

        loadContent();

        return v;
    }

    private void setDateAndTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        hour = calen.get(Calendar.HOUR_OF_DAY);
        minutes = calen.get(Calendar.MINUTE);
        if (month + 1 < 10) {
            current_date = day + "-0" + month + "-" + year;
        } else {
            current_date = day + "-" + month + "-" + year;
        }
        t_install_date.setText(current_date);
        String abc = t_install_date.getText().toString();
        String[] separated = abc.split("-");
        String date =  separated[0];
        String month = separated[1];
        String years = separated[2];
        String dates  = years+"-"+month+"-"+date;
        s_date = dates;
        t_install_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });
    }

    private void getDate() {
        Log.i("***string**", t_install_date.getText().toString());
        // TODO Auto-generated method stub
        final DatePickerDialog dpdd = new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> {
            if (monthOfYear + 1 < 10) {
                selected_todate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
            } else {
                selected_todate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
            }
            t_install_date.setText(selected_todate);
            String abc = t_install_date.getText().toString();
            String[] separated = abc.split("-");
            String date =  separated[0];
            String month = separated[1];
            String years = separated[2];
            String dates  = years+"-"+month+"-"+date;
            s_date = dates;
            refreshLayout.setRefreshing(true);
            loadContent();
        }, year, month - 1, day);
        dpdd.getDatePicker().setMaxDate(calen.getTimeInMillis());
        dpdd.show();
    }

    private void refresh() {
        clear();
        loadContent();
    }

    private void clear() {
        activityDetail.clear();
    }

    private void loadContent() {

        viewModelActivityDetails= ViewModelProviders.of(this).get(ViewModelActivityDetails.class);
        viewModelActivityDetails.getActivityDetailRepository(s_date, user_id).observe(this, movieResponse -> {

            if (movieResponse == null) {
                Toast.makeText(getActivity(), "Null response from server", Toast.LENGTH_SHORT).show();
                return;
            }

            activityDetail = movieResponse.getActivityList();
            if(movieResponse.getType()==1){
                txtContentUnavailable.setVisibility(View.GONE);
                activityDetailAdapter = new ActivityDetailAdapter(getContext(), activityDetail,s_date);
                recyclerView.setAdapter(activityDetailAdapter);
                runLayoutAnimation(recyclerView);
                refreshLayout.setRefreshing(false);
                recyclerView.setVisibility(View.VISIBLE);
                //mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
            }else {
                txtContentUnavailable.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                //mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
            }
        });
    }
    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void ShowProgressBar(boolean show) {
        try {
            if (show) {
                progressBars.setVisibility(View.VISIBLE);
            } else {
                progressBars.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
