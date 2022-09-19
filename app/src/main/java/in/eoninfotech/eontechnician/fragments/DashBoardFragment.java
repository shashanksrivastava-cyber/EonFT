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

public class DashBoardFragment extends Fragment {

    View v;
    String usrname, current_date, s_time, zone;
    int year, day, month, hour, minutes;
    String version, months;
    Calendar calen = Calendar.getInstance();
    TextView t_curntday, addTime, add_value, total_vts, total_drs, faulty_vts, faulty_drs, faulty_um,tot_sos,tot_lid,tot_fuel,
            tot_temp,um_working,sos_faulty,lid_faulty,fuel_faulty,temp_faulty;
    private PieChart mChart;
    ArrayList<Float> yData = new ArrayList<>();
    ArrayList<TechDashboardDetail> dashboardList = new ArrayList<>();
    Float achivd, total;
    private AlertDialog progressDialog;
    ColorfulRingProgressView vtsSpv, drsSpv, umSpv,um_workigSpv,sos_Spv,lid_Spv,fuel_Spv,temp_Spv;
    ProgressBar progressBar;
    CardView cv_one_login, cv_two_login, cv_three_login,cv_four_login,cv_five_login,cv_six_login,cv_seven_login,cv_eight_login;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    private String[] xData;
    public static final int[] BRIGHT_COLORS = {
            Color.parseColor("#D32F2F"), Color.parseColor("#F44336"), Color.parseColor("#FFC03C")};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.dashboard_new, container, false);

        mChart = v.findViewById(R.id.piechart);
        t_curntday = v.findViewById(R.id.curnt_date);
        addTime = v.findViewById(R.id.addTime);
        add_value = v.findViewById(R.id.add_value);
        total_vts = v.findViewById(R.id.total_vts);
        total_drs = v.findViewById(R.id.total_drs);
        tot_sos = v.findViewById(R.id.tot_sos);
        tot_lid = v.findViewById(R.id.tot_lid);
        tot_fuel = v.findViewById(R.id.tot_fuel);
        tot_temp = v.findViewById(R.id.tot_temp);
        faulty_vts = v.findViewById(R.id.faulty_vts);
        faulty_drs = v.findViewById(R.id.faulty_drs);
        faulty_um = v.findViewById(R.id.faulty_um);
        vtsSpv = v.findViewById(R.id.vtsSpv);
        drsSpv = v.findViewById(R.id.drsSpv);
        umSpv = v.findViewById(R.id.umSpv);
        um_working = v.findViewById(R.id.um_working);
        um_workigSpv = v.findViewById(R.id.um_workigSpv);
        sos_Spv = v.findViewById(R.id.sos_Spv);
        lid_Spv= v.findViewById(R.id.lid_Spv);
        fuel_Spv = v.findViewById(R.id.fuel_Spv);
        temp_Spv = v.findViewById(R.id.temp_Spv);
        sos_faulty = v.findViewById(R.id.sos_faulty);
        lid_faulty = v.findViewById(R.id.lid_faulty);
        fuel_faulty = v.findViewById(R.id.fuel_faulty);
        temp_faulty = v.findViewById(R.id.temp_faulty);
        cv_one_login = v.findViewById(R.id.cv_one_login);
        cv_two_login = v.findViewById(R.id.cv_two_login);
        cv_three_login = v.findViewById(R.id.cv_three_login);
        cv_four_login = v.findViewById(R.id.cv_four_login);
        cv_five_login = v.findViewById(R.id.cv_five_login);
        cv_six_login = v.findViewById(R.id.cv_six_login);
        cv_seven_login = v.findViewById(R.id.cv_seven_login);
        cv_eight_login = v.findViewById(R.id.cv_eight_login);
        progressDialog = new SpotsDialog(getActivity(), R.style.CustomIncentive);
        sharedprefs = this.getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        usrname = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        zone = sharedprefs.getString("zone", "");

        //ShowProgressBar(true);
        setDateAndTime();
        getDashBoardDetail();

        cv_one_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
                intent.putExtra("device_value", "2");
                intent.putExtra("tab", "1");
                intent.putExtra("other", "2");
                startActivity(intent);
            }
        });
        cv_two_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
                intent.putExtra("device_value", "1");
                intent.putExtra("tab", "1");
                intent.putExtra("other", "2");
                startActivity(intent);
            }
        });

        cv_three_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
                intent.putExtra("device_value", "3");
                intent.putExtra("tab", "1");
                intent.putExtra("other", "2");
                startActivity(intent);
            }
        });

        cv_four_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
                intent.putExtra("device_value", "3");
                intent.putExtra("tab", "1");
                intent.putExtra("other", "2");
                startActivity(intent);
            }
        });

        cv_five_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
                intent.putExtra("device_value", "3");
                intent.putExtra("tab", "1");
                intent.putExtra("other", "2");
                startActivity(intent);
            }
        });

        cv_six_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
                intent.putExtra("device_value", "3");
                intent.putExtra("tab", "1");
                intent.putExtra("other", "2");
                startActivity(intent);
            }
        });

        cv_seven_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
                intent.putExtra("device_value", "3");
                intent.putExtra("tab", "1");
                intent.putExtra("other", "2");
                startActivity(intent);
            }
        });

        cv_eight_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
                intent.putExtra("device_value", "3");
                intent.putExtra("tab", "1");
                intent.putExtra("other", "2");
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
                                addTime.setText("" + dashboardList.get(i).getCur_add());
                                add_value.setText("" + dashboardList.get(i).getAdd_21());
                                String color1 = dashboardList.get(i).getColor();
                                String[] separated = color1.split(";");
                                String color = separated[0];
                                int myColor = Color.parseColor(color);
                                addTime.setTextColor(myColor);

                                String color2 = dashboardList.get(i).getColor21();
                                String[] separated1 = color2.split(";");
                                String color3 = separated1[0];
                                int color21 = Color.parseColor(color3);
                                add_value.setTextColor(color21);

                                total_vts.setText("" + dashboardList.get(0).getTot_dev());
                                total_drs.setText("" + dashboardList.get(0).getTot_drs());
//                                tot_sos.setText(""+dashboardList.get(0).getTot_sos());
//                                tot_lid.setText(""+dashboardList.get(0).getTot_lid());
//                                tot_fuel.setText(""+dashboardList.get(0).getTot_fuel());
//                                tot_temp.setText(""+dashboardList.get(0).getTot_temp());

                                faulty_vts.setText("" + dashboardList.get(0).getFaulty_dev());
                                vtsSpv.setPercent(Float.parseFloat(dashboardList.get(0).getFaulty_dev()));

                                faulty_drs.setText("" + dashboardList.get(0).getFaulty_drs());
                                drsSpv.setPercent(dashboardList.get(0).getFaulty_drs());

                                faulty_um.setText("" + dashboardList.get(0).getUmain());
                                umSpv.setPercent(Float.parseFloat(dashboardList.get(0).getUmain()));

                                um_working.setText(""+dashboardList.get(0).getUmain_work());
                                um_workigSpv.setPercent(Float.parseFloat(dashboardList.get(0).getUmain_work()));

//                                sos_faulty.setText(""+dashboardList.get(0).getFaulty_sos());
//                                sos_Spv.setPercent(Float.parseFloat(dashboardList.get(0).getFaulty_sos()));
//
//                                lid_faulty.setText(""+dashboardList.get(0).getFaulty_lid());
//                                lid_Spv.setPercent(Float.parseFloat(dashboardList.get(0).getFaulty_lid()));
//
//                                fuel_faulty.setText(""+dashboardList.get(0).getFaulty_fuel());
//                                fuel_Spv.setPercent(Float.parseFloat(""+dashboardList.get(0).getFaulty_fuel()));
//
//                                temp_faulty.setText(""+dashboardList.get(0).getFaulty_temp());
//                                temp_Spv.setPercent(Float.parseFloat(dashboardList.get(0).getFaulty_temp()));

                            }
                        }
                    } else {
                        assert updateDataResponse != null;
                        Log.v("Response", updateDataResponse.toString());
                        achivd = 0.0f;
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
        if (month == 1) {
            months = "Jan";
        } else if (month == 2) {
            months = "Feb";
        } else if (month == 3) {
            months = "Mar";
        } else if (month == 4) {
            months = "Apr";
        } else if (month == 5) {
            months = "May";
        } else if (month == 6) {
            months = "Jun";
        } else if (month == 7) {
            months = "Jul";
        } else if (month == 8) {
            months = "Aug";
        } else if (month == 9) {
            months = "Sep";
        } else if (month == 10) {
            months = "Oct";
        } else if (month == 11) {
            months = "Nov";
        } else if (month == 12) {
            months = "Dec";
        }
        current_date = months + " " + day + "," + year;
        t_curntday.setText(current_date);
        SimpleDateFormat dateFormatt = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        s_time = dateFormatt.format(calen.getTime());
    }

}
