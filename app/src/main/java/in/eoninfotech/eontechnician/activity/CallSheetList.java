package in.eoninfotech.eontechnician.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Bundle;

import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.responses.CallSheetListDetail;
import in.eoninfotech.eontechnician.responses.CallSheetListResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CallSheetList extends AppCompatActivity  {

    int id = 1;
    private Handler handler = new Handler();
    RelativeLayout circularRelative;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String username, dist_id, version;
    String s_date = "0";
    Calendar calen = Calendar.getInstance();
    TextView preview, callDate, tv;
    ImageView ivProfile;
    TextView datee;
    Button update_dataa;
    ProgressDialog pDialog;
    String intentYear;
    Button upload_img_view;
    public RecyclerView recyclerView;
    public SwipeRefreshLayout refreshLayout;
    public LinearLayoutManager layoutManager;
    public LinearLayout linearActivity, update;
    private TextView txtContentUnavailable;
    private CallSheetListAdapter callSheetAdapter;
    ArrayList<CallSheetListDetail> callSheetDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_sheet_list);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Call Sheet List");
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("usname", "");
        dist_id = sharedprefs.getString("s_distt", "");
        version = sharedprefs.getString("version", "");

        datee = findViewById(R.id.date);
        upload_img_view = findViewById(R.id.upload_img);
        update_dataa = findViewById(R.id.update_data);
        ivProfile = findViewById(R.id.ivProfile);
        circularRelative = findViewById(R.id.circularRelative);
        refreshLayout = findViewById(R.id.refresh);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        txtContentUnavailable = findViewById(R.id.txt_content_unavailable);
        recyclerView.setLayoutManager(layoutManager);
        linearActivity = findViewById(R.id.llContent);
        refreshLayout.setRefreshing(true);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE);
        refreshLayout.setOnRefreshListener(this::refresh);

        Intent intent = getIntent();
        String month = intent.getStringExtra("month");
        intentYear = intent.getStringExtra("year");
        s_date = month;

        getCallSheetList();
    }

    private void getCallSheetList() {
        refreshLayout.setRefreshing(true);
        ApiHolder uploadImage = ServiceConnectionNewURL.getClient().create(ApiHolder.class);
        RequestBody tech_name = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody month = RequestBody.create(MediaType.parse("text/plain"), s_date);
        RequestBody year = RequestBody.create(MediaType.parse("text/plain"),intentYear);
        Call<CallSheetListResponse> call = uploadImage.callsheetlist(tech_name, month,year);
        call.enqueue(new Callback<CallSheetListResponse>() {
            @Override
            public void onResponse(Call<CallSheetListResponse> call, Response<CallSheetListResponse> response) {

                refreshLayout.setRefreshing(false);

                if (response.body() != null && response.body().getType() == 1) {

                    txtContentUnavailable.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    List<CallSheetListDetail> newList = response.body().getCallsheetlist();

                    if (callSheetAdapter == null) {
                        // create adapter first time
                        callSheetAdapter = new CallSheetListAdapter(CallSheetList.this, newList);
                        recyclerView.setAdapter(callSheetAdapter);
                    } else {
                        // update using DiffUtil
                        callSheetAdapter.updateList(newList);
                    }

                    runLayoutAnimation(recyclerView);

                } else {
                    txtContentUnavailable.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<CallSheetListResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(CallSheetList.this, "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
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

    private void refresh() {
        getCallSheetList();
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
