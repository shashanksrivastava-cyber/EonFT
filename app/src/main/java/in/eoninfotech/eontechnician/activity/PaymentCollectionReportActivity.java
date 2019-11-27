package in.eoninfotech.eontechnician.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import in.eoninfotech.eontechnician.PaymentCollectionAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.CollectionReportDetail;

public class PaymentCollectionReportActivity extends AppCompatActivity {

    String current_date, selected_todate, todatetoSend,fromdatetoSend;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String username, dist_id,version;
    public RecyclerView recyclerView;
    public SwipeRefreshLayout refreshLayout;
    public LinearLayoutManager layoutManager;
    public LinearLayout linearActivity;
    EditText fromDate,toDate;
    TextView t_install_date;
    int year, month, day, hour, minutes;
    Calendar calen = Calendar.getInstance();
    private TextView txtContentUnavailable;
    private PaymentCollectionAdapter paymentCollectionAdapter;
    ArrayList<CollectionReportDetail> collectionReportDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_collection_report);

        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("usname", "");
        dist_id = sharedprefs.getString("s_distt", "");
        version = sharedprefs.getString("version", "");

        refreshLayout = findViewById(R.id.refresh);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//      t_install_date = (EditText) v.findViewById(R.id.installDate);
        t_install_date = findViewById(R.id.installDate);
        fromDate = findViewById(R.id.fromDate);
        toDate = findViewById(R.id.toDate);
        txtContentUnavailable = findViewById(R.id.txt_content_unavailable);
        recyclerView.setLayoutManager(layoutManager);
        linearActivity = findViewById(R.id.llContent);
        //setDateAndTime();
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE,Color.GREEN);
       // refreshLayout.setOnRefreshListener(this::refresh);
        refreshLayout.setRefreshing(false);

       // loadContent();
    }
}
