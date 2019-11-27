package in.eoninfotech.eontechnician;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import in.eoninfotech.eontechnician.Responses.AttendanceDetail;
import in.eoninfotech.eontechnician.Responses.AttendanceResponse;
import in.eoninfotech.eontechnician.Responses.LogDetail;
import in.eoninfotech.eontechnician.Responses.LogResponse;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.Responses.LogStatusResponse;
import in.eoninfotech.eontechnician.activity.EventsDecorator;
import in.eoninfotech.eontechnician.fragments.ViewActivityLogsFragment;
import in.eoninfotech.eontechnician.helper.Location_prop;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 2/2/18.
 */
public class LogViewActivity extends AppCompatActivity{

    String disttid;
    String uusername, date, version;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    ListView lv;
    String months,leaves;
    TextView tvInfo , tvInfoo,avg,timer,txt_content_unavailable;
    RelativeLayout infoRelative;
    String month,years;
    LinearLayout totalWork ;
    ArrayList<LogDetail> log_status_time = new ArrayList<>();
    ArrayList<AttendanceDetail> attendanceDetails = new ArrayList<>();
    ArrayList<String>intentData = new ArrayList<>();
    private Location_prop loc;
    String datesss , monntth ,mnth;
    ImageView car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_activity_logs);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        uusername = sharedprefs.getString("s_uuser", "");
        disttid = sharedprefs.getString("s_distt", "");
        version = sharedprefs.getString("version", "");
        Log.i("****tab dis n usr***", disttid + " " + uusername);
        date = getIntent().getStringExtra("date");
        leaves = getIntent().getStringExtra("leave");
        lv = findViewById(R.id.list_logs);
       // tvInfo = findViewById(R.id.tvInfo);
       // tvInfoo = findViewById(R.id.tvInfoo);
       // infoRelative = findViewById(R.id.infoRelative);
        txt_content_unavailable = findViewById(R.id.txt_content_unavailable);
        avg = findViewById(R.id.avg);
        totalWork = findViewById(R.id.totalWork);
        timer = findViewById(R.id.timer);

        String def = date;
        String[] separated1 = def.split("/");
        String monnth = separated1[0];
        String datess1 = separated1[1];
        String yeears = separated1[2];
        int datte = Integer.parseInt(datess1);
        if(datte<10){
            datesss = ""+"0"+datte;
        }else{
             datesss = datess1;
        }
        int montth = Integer.parseInt(monnth);
        if(montth<10){
            monntth = ""+"0"+montth;
        }else{
            monntth = monnth;
        }

        String newDate = datesss+"/"+monntth+"/"+yeears;
        String carListAsString = getIntent().getStringExtra("AttendanceDetails");
        Gson gson = new Gson();
        Type type = new TypeToken<List<AttendanceDetail>>(){}.getType();
        List<AttendanceDetail> carsList = gson.fromJson(carListAsString, type);
        for (AttendanceDetail cars : carsList){
            if(newDate.equals(cars.getDate())){
                avg.setText(cars.getDisplay().toString());
            }
        }
        String abc = date;
        String[] separated = abc.split("/");
         month = separated[0];
        String datess = separated[1];
         years = separated[2];
        if(month.equals("1")){
            months = "Jan";
        }else if(month.equals("2")){
            months = "Feb";
        }else if(month.equals("3")){
            months = "Mar";
        }else if(month.equals("4")){
            months = "Apr";
        }else if(month.equals("5")){
            months = "May";
        }else if(month.equals("6")){
            months = "Jun";
        }else if(month.equals("7")){
            months = "Jul";
        }else if(month.equals("8")){
            months = "Aug";
        }else if(month.equals("9")){
            months = "Sep";
        }else if(month.equals("10")){
            months = "Oct";
        }else if(month.equals("11")){
            months = "Nov";
        }else if(month.equals("12")){
            months = "Dec";
        }
        String dates  = months+" "+datess+","+years;
        String s_date = dates;

        actionBar.setTitle("Attendance Log");
        timer.setText(s_date);
        ApiHolder get_list = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<LogResponse> call = get_list.logResponse(uusername,date);
        call.enqueue(new Callback<LogResponse>() {
            @Override
            public void onResponse(Call<LogResponse> call, Response<LogResponse> response) {
                if(response.body().getType()==0){

                    totalWork.setVisibility(View.GONE);
                    lv.setVisibility(View.GONE);
                    txt_content_unavailable.setVisibility(View.VISIBLE);
                   // infoRelative.setVisibility(View.VISIBLE);
//                    String def = date;
//                    String[] separated1 = def.split("/");
//                    String monnth = separated1[0];
//                    String datess1 = separated1[1];
//                    String yeears = separated1[2];
//                    int datte = Integer.parseInt(datess1);
//                    if(datte<10){
//                        datesss = ""+"0"+datte;
//                    }else{
//                        datesss = datess1;
//                    }
//                    int montth = Integer.parseInt(monnth);
//                    if(montth<10){
//                        monntth = ""+"0"+montth;
//                    }else{
//                        monntth = monnth;
//                    }
//                    String newDate = datesss+"/"+monntth+"/"+yeears;
//
//                    if(monnth.equals("1")){
//                        mnth = "Jan";
//                    }else if(monnth.equals("2")){
//                        mnth = "Feb";
//                    }else if(monnth.equals("3")){
//                        mnth = "Mar";
//                    }else if(monnth.equals("4")){
//                        mnth = "Apr";
//                    }else if(monnth.equals("5")){
//                        mnth = "May";
//                    }else if(monnth.equals("6")){
//                        mnth = "Jun";
//                    }else if(monnth.equals("7")){
//                        mnth = "Jul";
//                    }else if(monnth.equals("8")){
//                        mnth = "Aug";
//                    }else if(monnth.equals("9")){
//                        mnth = "Sep";
//                    }else if(monnth.equals("10")){
//                        mnth = "Oct";
//                    }else if(monnth.equals("11")){
//                        mnth = "Nov";
//                    }else if(monnth.equals("12")){
//                        mnth = "Dec";
//                    }
//                    String neewDate = datesss+"/"+mnth+"/"+yeears;
//                   // tvInfoo.setText("On "+" "+neewDate);
//                    String carListAsString = getIntent().getStringExtra("AttendanceDetails");
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<List<AttendanceDetail>>(){}.getType();
//                    List<AttendanceDetail> carsList = gson.fromJson(carListAsString, type);
//                    for (AttendanceDetail cars : carsList){
//                        if(newDate.equals(cars.getDate())){
//                            String abc  = cars.getDisplay().toString();
//                            if(abc.equalsIgnoreCase("Travel")){
//                                tvInfo.setText("You were Travelling ");
//                            }else if(abc.equalsIgnoreCase("Absent")){
//                                tvInfo.setText("You were "+abc);
//                            }else if(abc.equalsIgnoreCase("Leave")){
//                                tvInfo.setText("You were on "+cars.getDisplay().toString());
//                            }else if(abc.equalsIgnoreCase("Sunday")){
//                                tvInfo.setText(""+cars.getDisplay().toString());
//                            }else if(abc.equalsIgnoreCase("Holiday")){
//                                tvInfo.setText(""+cars.getDisplay().toString());
//                            }
//                            //tvInfo.setText("You were "+cars.getDisplay().toString());
//                        }
//                    }
                }else {
                    txt_content_unavailable.setVisibility(View.GONE);
                    log_status_time = response.body().getLogDetails();
                    for (int i = 0; i < log_status_time.size(); i++) {
                        Log.i("****", log_status_time.get(i).getStatus());
                    }
                    lv.setAdapter(new DetailAdapter(LogViewActivity.this, log_status_time));
                }
                }

            @Override
            public void onFailure(Call<LogResponse> call, Throwable t) {
                t.printStackTrace();
                //pDialog.dismiss();
                try {
                    TSnackbar snackbar = TSnackbar.make(LogViewActivity.this.getCurrentFocus(), "Try Again Connection Timeout", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getCalendarData() {
       // ShowProgressBar(true);
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<AttendanceResponse> call = log_att.attendanceResponse(uusername, month,years);
        call.enqueue(new Callback<AttendanceResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<AttendanceResponse> call, Response<AttendanceResponse> response) {

            }
            @Override
            public void onFailure(Call<AttendanceResponse> call, Throwable t) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_atd, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.menu_logout) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    class DetailAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater layinfa;
        int j;
        ArrayList<LogDetail> log_status_detail = new ArrayList<LogDetail>();

        DetailAdapter(Context c, ArrayList<LogDetail> log_status_detail) {
            ctx = c;
            this.log_status_detail = log_status_detail;
            layinfa = LayoutInflater.from(ctx);
        }
        @Override
        public int getCount() {
            //  Log.i("*******", new StringBuilder().append(list_vehidetai.size()).toString());
            return log_status_time.size();
        }
        @Override
        public Object getItem(int i) {
            return log_status_time.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View vv = view;
            j = i;
            if (vv == null) {
                vv = layinfa.inflate(R.layout.custom_incentive_item, null);
            }
            TextView status, time, km_count,address;
            status = vv.findViewById(R.id.tech_name);
            time = vv.findViewById(R.id.total_incentive);
            km_count = vv.findViewById(R.id.km_count);
            address = vv.findViewById(R.id.address);

            status.setText(log_status_time.get(i).getStatus());
            time.setText(log_status_time.get(i).getTime());
            address.setText(log_status_time.get(i).getAddress());
            if(i==0) {
                km_count.setText("0 km");
            }else{
            }
            if(i==0 || i==log_status_detail.size()-1) {
              status.setTextColor(Color.parseColor("#4588da"));
              time.setTextColor(Color.parseColor("#4588da"));
            }else {
                status.setTextColor(Color.parseColor("#000000"));
                time.setTextColor(Color.parseColor("#000000"));
            }
            Log.i("*****outer i****", new StringBuilder().append(i).toString());
            return vv;
        }
    }

}
