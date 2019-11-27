package in.eoninfotech.eontechnician.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.thefinestartist.Base;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.AttendanceDetail;
import in.eoninfotech.eontechnician.Responses.AttendanceResponse;
import in.eoninfotech.eontechnician.view.CalendarView;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thefinestartist.Base.getContext;

public class CustomCalender extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener {

    MaterialCalendarView mcv;
    static int count = 0;
    Calendar calen = Calendar.getInstance();
    String uusername, version;
    SharedPreferences sharedprefs;
    String monthtobeSend = "0", yeartobeSend = "0";
    ArrayList<AttendanceDetail> attendanceDetails = new ArrayList<AttendanceDetail>();
    TextView present, absent, leave, comp, avg;
    String leaves;
    private int color;
    private HashSet<CalendarDay> dates;
    CalendarView cv;
    ProgressBar progressBar;
    String current_date, selected_date;
    ArrayList<Date> events = new ArrayList<>();
    ArrayList<String> kms_arr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_calender);
        Base.initialize(this);

        sharedprefs = this.getSharedPreferences("login_user_pass", MODE_PRIVATE);
        uusername = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        cv = findViewById(R.id.calendar_view);
        mcv = findViewById(R.id.calendarView);
        mcv.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        monthtobeSend = String.valueOf(calen.get(Calendar.MONTH)+1 );
        yeartobeSend = String.valueOf(calen.get(Calendar.YEAR));
        present = findViewById(R.id.present);
        absent = findViewById(R.id.absent);
        leave = findViewById(R.id.leave);
        comp = findViewById(R.id.comp);
        avg = findViewById(R.id.avg);
        progressBar = findViewById(R.id.progressBar);

        ShowProgressBar(false);
        getAttendanceData();

        mcv.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        mcv.addDecorator(new EventsDecorator((Activity) this));

        mcv.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                mcv.addDecorator(new EventsDecorator(this));
                monthtobeSend = String.valueOf(date.getMonth()+1);
                yeartobeSend = String.valueOf(date.getYear());
                getAttendanceData();
            }
        });
//         mcv.setOnDateChangedListener(new OnDateSelectedListener() {
//             @Override
//             public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//                 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                 selected_date=dateFormat.format(date);
//                 Intent intent_view_entries = new Intent(CustomCalender.this, LogViewActivity.class);
//                 intent_view_entries.putExtra("date",selected_date);
//                 startActivity(intent_view_entries);
//             }
//         });
    }
    private void getAttendanceData() {

        ShowProgressBar(true);
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<AttendanceResponse> call = log_att.attendanceResponse(uusername, monthtobeSend, yeartobeSend);
        call.enqueue(new Callback<AttendanceResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<AttendanceResponse> call, Response<AttendanceResponse> response) {
                mcv.addDecorator(new EventsDecorator(this));
                AttendanceResponse attendanceResponse = response.body();
                attendanceDetails = attendanceResponse.getAttendanceDetails();
                present.setText(response.body().getPresent());
                absent.setText(response.body().getAbsent());
                leave.setText(response.body().getLeave());
                comp.setText(response.body().getComp_off());
                avg.setText(response.body().getAvg_hours());
                ShowProgressBar(false);
                try {
                    final ArrayList<CalendarDay> dates = new ArrayList<>();
                    final ArrayList<CalendarDay> absent = new ArrayList<>();
                    final ArrayList<CalendarDay> leave = new ArrayList<>();
                    final ArrayList<CalendarDay> holiday = new ArrayList<>();
                    final ArrayList<CalendarDay> travel = new ArrayList<>();
                    for (int j = 0; j <= attendanceDetails.size(); j++) {
                        leaves = String.valueOf(attendanceDetails.get(j).getDisplay());
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        CalendarDay d = CalendarDay.from(sdf.parse(attendanceDetails.get(j).getDate()));
                        if(leaves.contains("0")||leaves.contains("1")) {
                            dates.add(d);
                            mcv.addDecorator(new BookingDecorator(dates));
                        }  else if(leaves.equalsIgnoreCase("Absent")){
                            absent.add(d);
                            mcv.addDecorator(new LeaveDecorator(absent));
                        }  else if(leaves.equalsIgnoreCase("Leave")){
                            leave.add(d);
                            mcv.addDecorator(new EnableOneToTenDecorator(leave));
                        } else if(leaves.equalsIgnoreCase("Holiday")){
                            holiday.add(d);
                            mcv.addDecorator(new HolidayDecorator(holiday));
                        }else if(leaves.equalsIgnoreCase("Travel")){
                            travel.add(d);
                            mcv.addDecorator(new TravelDecorator(travel));
                        }
                        else if(leaves.equalsIgnoreCase("Sunday")){
                            mcv.addDecorator(new EventsDecorator(this));
                        }
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

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }

    private class EnableOneToTenDecorator implements DayViewDecorator {
        private ArrayList<CalendarDay> leave;
        public EnableOneToTenDecorator(Activity context) {
        }
        public EnableOneToTenDecorator(ArrayList<CalendarDay> leave) {
            this.leave = leave;
        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return leave.contains(day);
           // return day.getDay() <= (calen.get(Calendar.DATE)-1);
        }
        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.WHITE));
            view.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.brown_circle));
        }
    }
    public class HolidayDecorator implements DayViewDecorator {
        private ArrayList<CalendarDay> holiday;
        public HolidayDecorator(Activity context) {
        }
        public HolidayDecorator(ArrayList<CalendarDay> holiday) {
            this.holiday = holiday;
        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            //return true;
            return holiday.contains(day);
        }
        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.WHITE));
            view.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.grey_circle));
        }
    }
    private class LeaveDecorator implements DayViewDecorator {
        private ArrayList<CalendarDay> absent;
        public LeaveDecorator(ArrayList<CalendarDay> absent) {
            this.absent = absent;
        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return absent.contains(day);
        }
        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.WHITE));
            view.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.maroon_circle));
        }
    }
    private class BookingDecorator implements DayViewDecorator {
        private ArrayList<CalendarDay> dates;
        public BookingDecorator(ArrayList<CalendarDay> dates) {
            this.dates = dates;
        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
            //  return PRIME_TABLE[day.getDay()];
        }
        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.WHITE));
            view.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.green_circle));
        }
    }

    private class TravelDecorator implements DayViewDecorator {
        private ArrayList<CalendarDay> travel;
        public TravelDecorator(ArrayList<CalendarDay> travel) {
            this.travel = travel;
        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return travel.contains(day);
            //  return PRIME_TABLE[day.getDay()];
        }
        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.WHITE));
            view.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.blue_circle));
        }
    }

    private void ShowProgressBar(boolean show) {
        try {
            if (show) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }}
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
