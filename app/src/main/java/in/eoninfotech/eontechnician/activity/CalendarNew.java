package in.eoninfotech.eontechnician.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import in.eoninfotech.eontechnician.view.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.AttendanceDetail;
import in.eoninfotech.eontechnician.Responses.AttendanceResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 10/4/19.
 */

public class CalendarNew extends AppCompatActivity {

    CalendarView cv;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String uusername, version;
    String leaves;
    String disttid;
    String username, status, domain, versn;
    ProgressDialog pDialog;
    int year, month, day;
    Calendar calen = Calendar.getInstance();
    String current_date, selected_date;
    String reg_from_intent;
    TextView from_date, to_date, total_distance;
    ArrayList<Date> events = new ArrayList<>();
    ArrayList<String> kms_arr = new ArrayList<>();
    String monthtobeSend = "0", yeartobeSend = "0";
    ArrayList<AttendanceDetail> attendanceDetails = new ArrayList<AttendanceDetail>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_calender);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        sharedprefs = this.getSharedPreferences("login_user_pass", MODE_PRIVATE);
        uusername = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        cv = findViewById(R.id.calendar_view);
        monthtobeSend = String.valueOf(calen.get(Calendar.MONTH)+1 );
        yeartobeSend = String.valueOf(calen.get(Calendar.YEAR));

        cv = ((CalendarView) findViewById(R.id.calendar_view));

        try {
            getDistance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cv.setEventHandler(new CalendarView.EventHandler() {
            @Override
            public void onDayLongPress(Date date) {

                //Toast.makeText(getActivity(),String.valueOf(date).toString(),Toast.LENGTH_LONG).show();
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    selected_date = dateFormat.format(date);

                    Date strDate = dateFormat.parse(selected_date);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void getDistance() {
       // ShowProgressBar(true);
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<AttendanceResponse> call = log_att.attendanceResponse(uusername, monthtobeSend, yeartobeSend);
        call.enqueue(new Callback<AttendanceResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<AttendanceResponse> call, Response<AttendanceResponse> response) {
                AttendanceResponse attendanceResponse = response.body();
//                attendanceDetails = attendanceResponse.getAttendanceDetails();
//                present.setText(response.body().getPresent());
//                absent.setText(response.body().getAbsent());
//                leave.setText(response.body().getLeave());
//                comp.setText(response.body().getComp_off());
//                avg.setText(response.body().getAvg_hours());
               // ShowProgressBar(false);
                try {
                    final ArrayList<CalendarDay> dates = new ArrayList<>();
                    ArrayList<String> display = new ArrayList<>();
                    for (int j = 0; j <= attendanceDetails.size(); j++) {
                        leaves = String.valueOf(attendanceDetails.get(j).getDisplay());
                        kms_arr.add(String.valueOf(attendanceDetails.get(j).getDisplay()));

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            //Date d = sdf.parse(attendanceDetails.get(j).getDdate());
                            Date d = sdf.parse(attendanceDetails.get(j).getDate());
                            events.add(d);
                        } catch (ParseException ex) {
                            Log.v("Exception", ex.getLocalizedMessage());
                        }
                        cv.updateCalendar(events, kms_arr);
                        DecimalFormat df = new DecimalFormat("###.##");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<AttendanceResponse> call, Throwable t) {
            }
        });
    }

//    private void ShowProgressBar(boolean b) {
//        try {
//            if (b) {
//                progressBar.setVisibility(View.VISIBLE);
//            } else {
//                progressBar.setVisibility(View.GONE);
//            }}
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    }

