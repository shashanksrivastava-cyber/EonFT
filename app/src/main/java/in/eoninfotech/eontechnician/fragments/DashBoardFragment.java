package in.eoninfotech.eontechnician.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.thefinestartist.Base;
import com.timqi.sectorprogressview.ColorfulRingProgressView;
import com.timqi.sectorprogressview.SectorProgressView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.activity.FaultyDevicesActivity;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.DashBoardResponse;
import in.eoninfotech.eontechnician.Responses.TechDashboardDetail;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.thefinestartist.utils.content.ContextUtil.getSharedPreferences;

public class DashBoardFragment extends Fragment {

    String usrname, current_date, s_time,zone;
    int year, day,month, hour, minutes;
    String version,months;
    Calendar calen = Calendar.getInstance();
    TextView t_curntday,addTime,add_value,total_vts,total_drs,faulty_vts,faulty_drs,faulty_um;
    private PieChart mChart;
    ArrayList<Float> yData = new ArrayList<>();
    ArrayList<TechDashboardDetail> dashboardList = new ArrayList<>();
    Float achivd, total;
    private AlertDialog progressDialog;
    ColorfulRingProgressView vtsSpv,drsSpv,umSpv;
    ProgressBar progressBar;
    CardView cv_one_login,cv_two_login,cv_three_login;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    private String[] xData;
    public static final int[] BRIGHT_COLORS = {
            Color.parseColor("#D32F2F"), Color.parseColor("#F44336"),Color.parseColor("#FFC03C")};
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.dashboard_new, container, false);

        mChart = v.findViewById(R.id.piechart);
        t_curntday = v.findViewById(R.id.curnt_date);
        addTime = v.findViewById(R.id.addTime);
        progressBar = v.findViewById(R.id.progressBar);
        add_value = v.findViewById(R.id.add_value);
        total_vts = v.findViewById(R.id.total_vts);
        total_drs = v.findViewById(R.id.total_drs);
        faulty_vts = v.findViewById(R.id.faulty_vts);
        faulty_drs = v.findViewById(R.id.faulty_drs);
        faulty_um = v.findViewById(R.id.faulty_um);
        vtsSpv = v.findViewById(R.id.vtsSpv);
        drsSpv = v.findViewById(R.id.drsSpv);
        umSpv = v.findViewById(R.id.umSpv);
        cv_one_login = v.findViewById(R.id.cv_one_login);
        cv_two_login = v.findViewById(R.id.cv_two_login);
        cv_three_login = v.findViewById(R.id.cv_three_login);
        progressDialog = new SpotsDialog(getActivity(), R.style.CustomIncentive);
        Base.initialize(getActivity());
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        usrname = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        zone = sharedprefs.getString("zone","");

        ShowProgressBar(true);
        setDateAndTime();
        getDashBoardDetail();

//        mChart.setUsePercentValues(false);
//        mChart.getDescription().setEnabled(false);
//        // enable hole and configure
//        mChart.setDrawHoleEnabled(true);
//        mChart.setHoleRadius(30);
//        mChart.setTransparentCircleRadius(10);
//        mChart.setHoleColor(Color.parseColor("#FFFFFF"));
//        mChart.setCenterTextColor(Color.parseColor("#000000"));
//        mChart.setDrawCenterText(true);
//        mChart.setRotationEnabled(true);
//        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//
//            @Override
//            public void onValueSelected(Entry e, Highlight h) {
//                if (e == null)
//                    return;
//                if(h.getX()==1.0) {
//                    Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
//                    intent.putExtra("device_value","1");
//                    intent.putExtra("tab","1");
//                    intent.putExtra("other","2");
//                    startActivity(intent);
//                }else if(h.getX()==0.0) {
//                    Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
//                    intent.putExtra("device_value","2");
//                    intent.putExtra("tab","1");
//                    intent.putExtra("other","2");
//                    startActivity(intent);
//                }else if(h.getX()==2.0) {
//                    Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
//                    intent.putExtra("device_value","3");
//                    intent.putExtra("tab","1");
//                    intent.putExtra("other","2");
//                    startActivity(intent);
//                }
//            }
//            @Override
//            public void onNothingSelected() {
//            }
//        });

        cv_one_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
                    intent.putExtra("device_value","2");
                    intent.putExtra("tab","1");
                    intent.putExtra("other","2");
                    startActivity(intent);
            }
        });
        cv_two_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
                intent.putExtra("device_value","1");
                intent.putExtra("tab","1");
                intent.putExtra("other","2");
                startActivity(intent);
            }
        });

        cv_three_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
                intent.putExtra("device_value","3");
                intent.putExtra("tab","1");
                intent.putExtra("other","2");
                startActivity(intent);
            }
        });
        return v;
    }
     void getDashBoardDetail() {

         try {
             ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
             Call<DashBoardResponse> call = log_att.dashBoardResponse(zone);
             Log.i("****call", String.valueOf(call));
             call.enqueue(new Callback<DashBoardResponse>() {
                 @Override
                 public void onResponse(Call<DashBoardResponse> call, Response<DashBoardResponse> response) {
                     DashBoardResponse updateDataResponse = response.body();
                     dashboardList = response.body().getTechDashboardDetails();
                     if (updateDataResponse != null) {
                         if (updateDataResponse.getType() == 1) {
                             for (int i = 0; i < dashboardList.size(); i++) {
                                 addTime.setText(""+dashboardList.get(i).getCur_add());
                                 add_value.setText(""+dashboardList.get(i).getAdd_21());
                                 String color1  = dashboardList.get(i).getColor();
                                 String[] separated = color1.split(";");
                                 String color = separated[0];
                                 int myColor = Color.parseColor(color);
                                 addTime.setTextColor(myColor);
                                 //addTime.setBackgroundColor(myColor);

                                 String color2  = dashboardList.get(i).getColor21();
                                 String[] separated1 = color2.split(";");
                                 String color3 = separated1[0];
                                 int color21 = Color.parseColor(color3);
                                 add_value.setTextColor(color21);

                                 total_vts.setText(""+dashboardList.get(0).getTot_dev());
                                 total_drs.setText(""+dashboardList.get(0).getTot_drs());

                                 faulty_vts.setText(""+dashboardList.get(0).getFaulty_dev());
                                 vtsSpv.setPercent(Float.parseFloat(dashboardList.get(0).getFaulty_dev()));

                                 faulty_drs.setText(""+dashboardList.get(0).getFaulty_drs());
                                 drsSpv.setPercent(dashboardList.get(0).getFaulty_drs());

                                 faulty_um.setText(""+dashboardList.get(0).getUmain());
                                 umSpv.setPercent(Float.parseFloat(dashboardList.get(0).getUmain()));

                                // add_value.setBackgroundColor(color21);
     //                            mChart.setCenterText("VTS : "+dashboardList.get(i).getTot_dev()+"\n"+"\n"+"DRS :"+dashboardList.get(i).getTot_drs());
     //                            mChart.setCenterTextSize(14f);
     //                            xData = new String[]{"Faulty VTS : " +(dashboardList.get(i).getFaulty_dev()),
     //                                    "Faulty DRS : " +(dashboardList.get(i).getFaulty_drs()),
     //                                    "Under Maint. : " +(dashboardList.get(i).getUmain())};
                             }
                         }
                     } else {
                         assert updateDataResponse != null;
                         Log.v("Response", updateDataResponse.toString());
                         achivd=0.0f;
                     }
                         yData.add(Float.valueOf(33));
                         yData.add(Float.valueOf(33));
                         yData.add(Float.valueOf(33));
                    // addData(yData);
                     //ShowProgressBar(false);
                 }

                 @Override
                 public void onFailure(Call<DashBoardResponse> call, Throwable t) {
                     t.printStackTrace();
                     Toast.makeText(getActivity(), "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
                    // progressDialog.hide();
                 }
             });
         } catch (Exception e) {
             e.printStackTrace();
             Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
         }
     }

    void setDateAndTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
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
        if(month==1){
            months = "Jan";
        }else if(month==2){
            months = "Feb";
        }else if(month==3){
            months = "Mar";
        }else if(month==4){
            months = "Apr";
        }else if(month==5){
            months = "May";
        }else if(month==6){
            months = "Jun";
        }else if(month==7){
            months = "Jul";
        }else if(month==8){
            months = "Aug";
        }else if(month==9){
            months = "Sep";
        }else if(month==10){
            months = "Oct";
        }else if(month==11){
            months = "Nov";
        }else if(month==12){
            months = "Dec";
        }
        current_date = months+ " " +day + "," + year;
        t_curntday.setText(current_date);
        SimpleDateFormat dateFormatt = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        s_time = dateFormatt.format(calen.getTime());
    }

//    private void addData(ArrayList<Float> yData) {
//        float mult = 100;
//        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();
//
//        for (int i = 0; i < yData.size(); i++) {
//            try {
//                yVals1.add(new PieEntry(yData.get(i), xData[i]));
//            }catch (Exception e){
//
//            }
//        }
//        // create pie data set
//        PieDataSet dataSet = new PieDataSet(yVals1, "");
//        dataSet.setSliceSpace(3);
//        dataSet.setSelectionShift(5);
//
//        // add many colors
//        ArrayList<Integer> colors = new ArrayList<Integer>();
//        for (int c : BRIGHT_COLORS) colors.add(c);
//
//        dataSet.setColors(colors);
//        // instantiate pie data object now
//        PieData data = new PieData(dataSet);
//       // data.setValueFormatter(new PercentFormatter());
//        data.setValueTextSize(0f);
//        data.setValueTextColor(Color.WHITE);
//        mChart.setData(data);
//        // undo all highlights
//        mChart.highlightValues(null);
//        // update pie chart
//        mChart.invalidate();
//    }
     private void ShowProgressBar(boolean show) {
         try {
             if (show) {
                 progressBar.setVisibility(View.VISIBLE);
             } else {
                 progressBar.setVisibility(View.GONE);
             }}
         catch (Exception e) {
             e.printStackTrace();
         }}

}
