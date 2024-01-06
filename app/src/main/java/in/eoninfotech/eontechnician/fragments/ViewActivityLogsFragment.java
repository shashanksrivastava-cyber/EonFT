package in.eoninfotech.eontechnician.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;


import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import in.eoninfotech.eontechnician.DayEnableDecorator;
import in.eoninfotech.eontechnician.LogViewActivity;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.AttendanceDetail;
import in.eoninfotech.eontechnician.Responses.AttendanceResponse;
import in.eoninfotech.eontechnician.activity.CustomCalender;
import in.eoninfotech.eontechnician.activity.EventsDecorator;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.view.CalendarView;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.AttResponse;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewActivityLogsFragment extends Fragment {

    View v;
    LinearLayout avgWrkngHrs;
    String usrnm, distid;
    int year, month, day;
    MaterialCalendarView mcv;
    static int count = 0;
    Calendar calen = Calendar.getInstance();
    String uusername, version;
    String monthtobeSend = "0", yeartobeSend = "0";
    ArrayList<AttendanceDetail> attendanceDetails = new ArrayList<AttendanceDetail>();
    TextView present,absent,leave,comp,avg,travel,holi,txtPoor,txtGood,txtVeryGood;
    String leaves;
    private int color;
    private HashSet<CalendarDay> dates;
    CalendarView cv;
    ProgressBar progressBar;
    String current_date, selected_date;
    ArrayList<Date> events = new ArrayList<>();
    ArrayList<String> kms_arr = new ArrayList<>();
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    ImageView poor,good,veryGood;
    String daates,monntth;
    BottomSheetBehavior mBottomSheetBehavior;
    String image,months;
    String fullName;
    String currentDate;
    String newDate,datetoBeShown,ddat;
    String jsonCars;
    int datesss,monthh,neewDatee;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_entries_calender, container, false);
        sharedprefs = this.getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        uusername = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        image = sharedprefs.getString("image","");
        fullName = sharedprefs.getString("dis_user","");
        String imageUri = K.Url.IMAGE_URL +"uploads/"+image;
        //setDateAndTime();
        mcv = v.findViewById(R.id.calendarView);
        mcv.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        monthtobeSend = String.valueOf(calen.get(Calendar.MONTH)+1 );
        yeartobeSend = String.valueOf(calen.get(Calendar.YEAR));
        present = v.findViewById(R.id.present);
        absent = v.findViewById(R.id.absent);
        leave = v.findViewById(R.id.leave);
        comp = v.findViewById(R.id.comp);
        avg = v.findViewById(R.id.avg);
        travel = v.findViewById(R.id.travel);
        progressBar = v.findViewById(R.id.progressBar);
        avgWrkngHrs = v.findViewById(R.id.avgWrkngHrs);
        holi = v.findViewById(R.id.holi);
        poor = v.findViewById(R.id.poor);
        good = v.findViewById(R.id.good);
        veryGood = v.findViewById(R.id.veryGood);
        txtPoor = v.findViewById(R.id.txtPoor);
        txtGood = v.findViewById(R.id.txtGood);
        txtVeryGood = v.findViewById(R.id.txtVeryGood);
        ShowProgressBar(false);

        mcv.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        mcv.addDecorator(new EventsDecorator());

        mcv.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                mcv.addDecorator(new EventsDecorator());
                monthtobeSend = String.valueOf(date.getMonth()+1);
                yeartobeSend = String.valueOf(date.getYear());
                getAttendanceData();
            }
        });
        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                try{
                    String abc = String.valueOf(date);
                    String[] separate = abc.split("\\{");
                    String abc0 = separate[0];
                    String abc1 = separate[1];
                    String[] separate1 = abc1.split("\\}");
                    String abc2 = separate1[0];
                    String[] separate3 = abc2.split("-");
                    String year = separate3[0];
                    String month = String.valueOf(date.getMonth() + 1);
                    String datess = separate3[2];
                    newDate = month + "/" + datess + "/" + year;
                    ddat = datess+"-"+month+"-"+year;
                    datesss = Integer.parseInt(datess);
                    monthh = Integer.parseInt(month);
                     if(datesss<10){
                      daates = ""+"0"+datesss;
                }else{
                         daates = datess;
                }if(monthh<10){
                     monntth = ""+"0"+monthh;
                }else{
                      monntth = month;
                }if(monntth.equals("01")){
                    months = "Jan";
                }else if(monntth.equals("02")){
                    months = "Feb";
                }else if(monntth.equals("03")){
                    months = "Mar";
                }else if(monntth.equals("04")){
                    months = "Apr";
                }else if(monntth.equals("05")){
                    months = "May";
                }else if(monntth.equals("06")){
                    months = "Jun";
                }else if(monntth.equals("07")){
                    months = "Jul";
                }else if(monntth.equals("08")){
                    months = "Aug";
                }else if(monntth.equals("09")){
                    months = "Sep";
                }else if(monntth.equals("10")){
                    months = "Oct";
                }else if(monntth.equals("11")){
                    months = "Nov";
                }else if(monntth.equals("12")){
                    months = "Dec";
                }
                datetoBeShown = daates + "-" + months + "-" + year;
                String neewDatee = daates + "/" + monntth + "/" + year;
                    Gson gson = new Gson();
                    jsonCars = gson.toJson(attendanceDetails);
                    String carListAsString = jsonCars;
                    Gson gson1 = new Gson();
                    Type type = new TypeToken<List<AttendanceDetail>>(){}.getType();
                    List<AttendanceDetail> carsList = gson1.fromJson(carListAsString, type);
                for (AttendanceDetail cars : carsList){
                    if(neewDatee.equals(cars.getDate())){
                        String displayValue = cars.getDisplay().toString();
                        if(displayValue.contains("0")||displayValue.contains("1")){
                            Intent intent = new Intent(getActivity(), LogViewActivity.class);
                            intent.putExtra("date", newDate);
                            intent.putExtra("leave", leaves);
                            intent.putExtra("AttendanceDetails", jsonCars);
                            startActivity(intent);
                        } else if(displayValue.contains("Sunday")||displayValue.contains("Holiday")){

                        } else {
                                MyBottomSheetDialog myBottomSheetDialog = MyBottomSheetDialog.getInstance(getActivity());
                                myBottomSheetDialog.setTvTitle(fullName);
                                myBottomSheetDialog.setTvSubTitle(datetoBeShown);
                                myBottomSheetDialog.setIvAvatar(K.Url.IMAGE_URL +"uploads/"+image);
                                myBottomSheetDialog.setLeave(cars.getDisplay().toString());
                                if(cars.getDisplay().toString().equalsIgnoreCase("Travel")){
                                    myBottomSheetDialog.setLeaveType("You were Travelling ");
                                }else if(cars.getDisplay().toString().equalsIgnoreCase("Absent")){
                                    myBottomSheetDialog.setLeaveType("You were "+cars.getDisplay().toString());
                                }else if(cars.getDisplay().toString().equalsIgnoreCase("Leave")){
                                    myBottomSheetDialog.setLeaveType("You were on "+cars.getDisplay().toString());
                                }else if(cars.getDisplay().toString().equalsIgnoreCase("Comp OFFs")){
                                    myBottomSheetDialog.setLeaveType("You were on Compensatory Leave");
                                }
                                myBottomSheetDialog.setCanceledOnTouchOutside(true);
                                myBottomSheetDialog.show();
                        }
                    }else{
                    }
                }
            }catch (Exception e ){
                }
                year = calen.get(Calendar.YEAR);
                month = calen.get(Calendar.MONTH);
                day = calen.get(Calendar.DAY_OF_MONTH);
                month = month + 1;
                String def  = day+"-"+month+"-"+year;
                if(ddat.equals(def)){
                    Intent intent = new Intent(getActivity(), LogViewActivity.class);
                    intent.putExtra("date", newDate);
                    intent.putExtra("leave", leaves);
                    intent.putExtra("AttendanceDetails", jsonCars);
                    startActivity(intent);
                }
            }
        });
        return v;
    }
    private void getAttendanceData() {
        ShowProgressBar(true);
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<AttendanceResponse> call = log_att.attendanceResponse(uusername, monthtobeSend, yeartobeSend);
        call.enqueue(new Callback<AttendanceResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<AttendanceResponse> call, Response<AttendanceResponse> response) {
                AttendanceResponse updateDataResponse = response.body();
                String type  = String.valueOf(response.body().getType());
                try{
                if(type.equalsIgnoreCase("0")){
                    poor.setImageResource(R.drawable.poor_grey);
                    txtPoor.setTextColor(getResources().getColor(R.color.grey500));
                    good.setImageResource(R.drawable.good_grey);
                    txtGood.setTextColor(getResources().getColor(R.color.grey500));
                    veryGood.setImageResource(R.drawable.very_good_grey);
                    txtVeryGood.setTextColor(getResources().getColor(R.color.grey500));
                    avgWrkngHrs.setBackgroundColor(getResources().getColor(R.color.grey500));
                    present.setText("");
                    absent.setText("");
                    leave.setText("");
                    comp.setText("");
                    avg.setText("");
                    travel.setText("");
                    holi.setText("");
                    ShowProgressBar(false);
                }else {
                    try {
                        mcv.addDecorator(new EventsDecorator());
                        AttendanceResponse attendanceResponse = response.body();
                        attendanceDetails = attendanceResponse.getAttendanceDetails();
                        present.setText(response.body().getPresent());
                        absent.setText(response.body().getAbsent());
                        leave.setText(response.body().getLeave());
                        comp.setText(response.body().getComp_off());
                        avg.setText(response.body().getAvg_hours());
                        travel.setText(response.body().getTravel());
                        holi.setText(response.body().getHoliday());
                        String color = response.body().getColorCode();
                        if (color.equalsIgnoreCase("orangered")) {
                            avgWrkngHrs.setBackgroundColor(getResources().getColor(R.color.c));
                        } else if (color.equalsIgnoreCase("green")) {
                            avgWrkngHrs.setBackgroundColor(getResources().getColor(R.color.material_green_600));
                        } else if (color.equalsIgnoreCase("red")) {
                            avgWrkngHrs.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        } else {
                            int myColor = Color.parseColor(color);
                            avgWrkngHrs.setBackgroundColor(getResources().getColor(R.color.grey500));
                        }String a = String.valueOf(response.body().getAvg_hours());
                        if (a.equalsIgnoreCase("null")) {
                            a = "0:00";
                            poor.setImageResource(R.drawable.poor_grey);
                            txtPoor.setTextColor(getResources().getColor(R.color.grey500));
                            good.setImageResource(R.drawable.good_grey);
                            txtGood.setTextColor(getResources().getColor(R.color.grey500));
                            veryGood.setImageResource(R.drawable.very_good_grey);
                            txtVeryGood.setTextColor(getResources().getColor(R.color.grey500));
                            avgWrkngHrs.setBackgroundColor(getResources().getColor(R.color.grey500));
                        }String[] separate = a.split(":");
                        int a1 = Integer.parseInt(separate[0]);
                        String a2 = separate[1];
                        if (a1 >= 8) {
                            veryGood.setImageResource(R.drawable.very_good_color);
                            txtVeryGood.setTextColor(getResources().getColor(R.color.material_green_600));
                            good.setImageResource(R.drawable.good_grey);
                            txtGood.setTextColor(getResources().getColor(R.color.grey500));
                            poor.setImageResource(R.drawable.poor_grey);
                            txtPoor.setTextColor(getResources().getColor(R.color.grey500));
                        } else if ((a1 >= 6 && a1 < 8)) {
                            good.setImageResource(R.drawable.good_color);
                            txtGood.setTextColor(getResources().getColor(R.color.c));
                            veryGood.setImageResource(R.drawable.very_good_grey);
                            txtVeryGood.setTextColor(getResources().getColor(R.color.grey500));
                            poor.setImageResource(R.drawable.poor_grey);
                        } else if (a1 < 6 && a1 > 0) {
                            poor.setImageResource(R.drawable.poor_color);
                            txtPoor.setTextColor(getResources().getColor(R.color.colorAccent));
                            good.setImageResource(R.drawable.good_grey);
                            txtGood.setTextColor(getResources().getColor(R.color.grey500));
                            veryGood.setImageResource(R.drawable.very_good_grey);
                            txtVeryGood.setTextColor(getResources().getColor(R.color.grey500));
                        }
                    ShowProgressBar(false);
                        final ArrayList<CalendarDay> dates = new ArrayList<>();
                        final ArrayList<CalendarDay> absent = new ArrayList<>();
                        final ArrayList<CalendarDay> leave = new ArrayList<>();
                        final ArrayList<CalendarDay> holiday = new ArrayList<>();
                        final ArrayList<CalendarDay> travels = new ArrayList<>();
                        final ArrayList<CalendarDay> comps = new ArrayList<>();
                        final ArrayList<CalendarDay> enabledDates = new ArrayList<>();
                        for (int j = 0; j <= attendanceDetails.size(); j++) {
                            leaves = String.valueOf(attendanceDetails.get(j).getDisplay());
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            CalendarDay d = CalendarDay.from(sdf.parse(attendanceDetails.get(j).getDate()));
                            if (leaves.contains("0") || leaves.contains("1")) {
                                dates.add(d);
                                mcv.addDecorator(new BookingDecorator(dates));
                            } else if (leaves.equalsIgnoreCase("Absent")) {
                                absent.add(d);
                                mcv.addDecorator(new LeaveDecorator(absent));
                            } else if (leaves.equalsIgnoreCase("Leave")) {
                                leave.add(d);
                                mcv.addDecorator(new EnableOneToTenDecorator(leave));
                            } else if (leaves.equalsIgnoreCase("Holiday") || (leaves.equalsIgnoreCase("Sunday"))) {
                                holiday.add(d);
                                String holy = String.valueOf(holiday.size());
                                holi.setText(holy);
                                mcv.addDecorator(new HolidayDecorator(holiday));
                            } else if (leaves.equalsIgnoreCase("Travel")) {
                                travels.add(d);
                                String abc = String.valueOf(travels.size());
                                travel.setText(abc);
                                mcv.addDecorator(new TravelDecorator(travels));
                            } else if ((leaves.equalsIgnoreCase("Comp Offs"))) {
                                comps.add(d);
                                String abc = String.valueOf(comps.size());
                                comp.setText(abc);
                                mcv.addDecorator(new CompDecorator(comps));
                            } else {
                                mcv.addDecorator(new DayEnableDecorator(enabledDates));
                            }
                        }

                    } catch (Exception e) {
                        ShowProgressBar(false);
                        e.printStackTrace();
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
        }
        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.WHITE));
            view.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.brown_circle));
        }
    }
    public class HolidayDecorator implements DayViewDecorator {
        private ArrayList<CalendarDay> holiday;
        public HolidayDecorator(ArrayList<CalendarDay> holiday) {
            this.holiday = holiday;
        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {
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
        }
        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.WHITE));
            view.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.blue_circle));
        }
    }
    private class CompDecorator implements DayViewDecorator {
        private ArrayList<CalendarDay> comps;
        public CompDecorator(ArrayList<CalendarDay> comps) {
            this.comps = comps;
        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return comps.contains(day);
        }
        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.WHITE));
            view.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.orange_circle));
        }
    }

    public class EventsDecorator implements DayViewDecorator {

        public EventsDecorator() {
        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {

            return day.getCalendar().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
        }
        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.WHITE));
            view.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.grey_circle));
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

    @Override
    public void onResume() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getAttendanceData();
            }
        }, 50);

        super.onResume();
    }
}
