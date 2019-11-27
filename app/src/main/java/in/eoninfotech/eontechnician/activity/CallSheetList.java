package in.eoninfotech.eontechnician.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thefinestartist.Base;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.CallSheetListDetail;
import in.eoninfotech.eontechnician.Responses.CallSheetListResponse;
import in.eoninfotech.eontechnician.Responses.CallSheetResponse;
import in.eoninfotech.eontechnician.helper.CallSheetDetail;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thefinestartist.utils.content.ContextUtil.getSharedPreferences;

public class CallSheetList extends AppCompatActivity {

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
        preview = findViewById(R.id.preview);
        //callDate = v.findViewById(R.id.callDate);
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
                if (response.body().getType() == 1) {
                    CallSheetListResponse callSheetResponse = response.body();
                    txtContentUnavailable.setVisibility(View.GONE);
                    callSheetDetails = callSheetResponse.getCallsheetlist();
                    callSheetAdapter = new CallSheetListAdapter(getApplicationContext(), callSheetDetails);
                    recyclerView.setAdapter(callSheetAdapter);
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
//                pDialog.dismiss();
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
        onBackPressed();
        return true;
    }
}
