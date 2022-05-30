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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.activity.FaultyDevicesActivity;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.DashBoardResponse;
import in.eoninfotech.eontechnician.Responses.TechDashboardDetail;
import in.eoninfotech.eontechnician.Responses.TechDetails;
import in.eoninfotech.eontechnician.Responses.TechResponse;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by root on 10/11/18.
 */

public class OtherDashBoardFragment extends Fragment {

    View v;
    int year, day, month;
    Calendar calen = Calendar.getInstance();
    MySearchableSpinner client;
    SharedPreferences sharedprefs;
    String uusername, version, zone, current_date, months;
    ArrayList<TechDetails> techList = new ArrayList<>();
    ArrayList<String> techDetail = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private AlertDialog progressDialog;
    private PieChart mChart;
    ArrayList<TechDashboardDetail> dashboardList = new ArrayList<>();
    TextView t_curntday, t_target, addName, addTime, add_value, total_vts, total_drs, faulty_vts, faulty_drs, faulty_um,
            tot_sos,tot_lid,tot_fuel,tot_temp,um_working,sos_faulty,lid_faulty,fuel_faulty,temp_faulty;
    ArrayList<Float> yData = new ArrayList<>();
    ColorfulRingProgressView vtsSpv, drsSpv, umSpv,um_workigSpv,sos_Spv,lid_Spv,fuel_Spv,temp_Spv;
    CardView cv_one_login, cv_two_login, cv_three_login,cv_four_login,cv_five_login,cv_six_login,cv_seven_login,cv_eight_login;
    Float achivd, total;
    private String[] xData;
    TextView txt_content_unavailable;
    LinearLayout addDetail;
    public static final int[] BRIGHT_COLORS = {
            Color.parseColor("#D32F2F"), Color.parseColor("#F44336"), Color.parseColor("#FFC03C")};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_others_new, container, false);

        sharedprefs = getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        uusername = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        Log.i("****stat dist n usr***", version + " " + uusername);
        client = v.findViewById(R.id.new_in_clients);
        mChart = v.findViewById(R.id.piechart);
        addTime = v.findViewById(R.id.addTime);
        add_value = v.findViewById(R.id.add_value);
        addDetail = v.findViewById(R.id.addDetail);
        addName = v.findViewById(R.id.addName);
        add_value = v.findViewById(R.id.add_value);
        total_vts = v.findViewById(R.id.total_vts);
        total_drs = v.findViewById(R.id.total_drs);
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
        txt_content_unavailable = v.findViewById(R.id.txt_content_unavailable);
        t_curntday = v.findViewById(R.id.curnt_date);
        progressDialog = new SpotsDialog(getActivity(), R.style.CustomIncentive);
        setDateAndTime();
        addTechnicians();
        //ShowProgressBar(false);

        cv_one_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
                intent.putExtra("device_value", "2");
                intent.putExtra("zone", zone);
                intent.putExtra("tab", "2");
                intent.putExtra("other", "1");
                startActivity(intent);
            }
        });
        cv_two_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
                intent.putExtra("device_value", "1");
                intent.putExtra("zone", zone);
                intent.putExtra("tab", "2");
                intent.putExtra("other", "1");
                startActivity(intent);
            }
        });

        cv_three_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
                intent.putExtra("device_value", "3");
                intent.putExtra("zone", zone);
                intent.putExtra("tab", "2");
                intent.putExtra("other", "1");
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

    private void addTechnicians() {
        ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<TechResponse> clientCall = client_att.requestTechList();
        clientCall.enqueue(new Callback<TechResponse>() {
            public void onResponse(Call<TechResponse> call, Response<TechResponse> response) {
                try {

                    TechResponse workTypeResponse = response.body();
                    techList = response.body().gettechList();
                    Log.i("**workclientrespnse", " " + techList);

                    try {
                        techDetail.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < techList.size(); i++) {
                        techDetail.add(techList.get(i).getName());
                    }
                    List<String> name = new ArrayList<String>();
                    Collection c = techDetail;
                    Iterator itr = c.iterator();
                    name.add(" Select Technician");
                    while (itr.hasNext()) {
                        String temp = itr.next().toString();
                        Log.i("TEMP", "" + temp);
                        name.add(temp);
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, name);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    client.setAdapter(adapter);

                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TechResponse> call, Throwable t) {
                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });

        client.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                zone = (techList.get(i).getZone());
                addName.setText(techList.get(i).getName() + "'s ADD Performence");
                addDetail.setVisibility(View.VISIBLE);
                txt_content_unavailable.setVisibility(View.GONE);
                getDashBoardDetail();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getDashBoardDetail() {

        progressDialog.show();
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<DashBoardResponse> call = log_att.dashBoardResponse(zone);
        Log.i("****call", String.valueOf(call));
        call.enqueue(new Callback<DashBoardResponse>() {
            @Override
            public void onResponse(Call<DashBoardResponse> call, Response<DashBoardResponse> response) {
                DashBoardResponse updateDataResponse = response.body();
                dashboardList = response.body().getTechDashboardDetails();
                Log.i("**respnse", " " + response.body());
                // if (updateDataResponse != null) {
                if (updateDataResponse.getType() == 1) {
                    for (int i = 0; i < dashboardList.size(); i++) {
                        addTime.setText("" + dashboardList.get(i).getCur_add());
                        add_value.setText("" + dashboardList.get(i).getAdd_21());
                        String color1 = dashboardList.get(i).getColor();
                        String[] separated = color1.split(";");
                        String color = separated[0];
                        int myColor = Color.parseColor(color);
                        addTime.setTextColor(myColor);
                        // addTime.setBackgroundColor(myColor);

                        String color2 = dashboardList.get(i).getColor21();
                        String[] separated1 = color2.split(";");
                        String color3 = separated1[0];
                        int color21 = Color.parseColor(color3);
                        add_value.setTextColor(color21);
                        // add_value.setBackgroundColor(color21);

                        total_vts.setText("" + dashboardList.get(0).getTot_dev());
                        total_drs.setText("" + dashboardList.get(0).getTot_drs());
                        tot_sos.setText(""+dashboardList.get(0).getTot_sos());
                        tot_lid.setText(""+dashboardList.get(0).getTot_lid());
                        tot_fuel.setText(""+dashboardList.get(0).getTot_fuel());
                        tot_temp.setText(""+dashboardList.get(0).getTot_temp());

                        faulty_vts.setText("" + dashboardList.get(0).getFaulty_dev());
                        vtsSpv.setPercent(Float.parseFloat(dashboardList.get(0).getFaulty_dev()));

                        faulty_drs.setText("" + dashboardList.get(0).getFaulty_drs());
                        drsSpv.setPercent(dashboardList.get(0).getFaulty_drs());

                        faulty_um.setText("" + dashboardList.get(0).getUmain());
                        umSpv.setPercent(Float.parseFloat(dashboardList.get(0).getUmain()));

                        um_working.setText(""+dashboardList.get(0).getUmain_work());
                        um_workigSpv.setPercent(Float.parseFloat(dashboardList.get(0).getUmain_work()));

                        sos_faulty.setText(""+dashboardList.get(0).getFaulty_sos());
                        sos_Spv.setPercent(Float.parseFloat(dashboardList.get(0).getFaulty_sos()));

                        lid_faulty.setText(""+dashboardList.get(0).getFaulty_lid());
                        lid_Spv.setPercent(Float.parseFloat(dashboardList.get(0).getFaulty_lid()));

                        fuel_faulty.setText(""+dashboardList.get(0).getFaulty_fuel());
                        fuel_Spv.setPercent(Float.parseFloat(""+dashboardList.get(0).getFaulty_fuel()));

                        temp_faulty.setText(""+dashboardList.get(0).getFaulty_temp());
                        temp_Spv.setPercent(Float.parseFloat(dashboardList.get(0).getFaulty_temp()));

                        progressDialog.hide();
                    }
                } else {
                    progressDialog.hide();
                    assert updateDataResponse != null;
                    Log.v("Response", updateDataResponse.toString());
                    achivd = 0.0f;
                }
            }

            @Override
            public void onFailure(Call<DashBoardResponse> call, Throwable t) {
                progressDialog.hide();
                t.printStackTrace();
                Toast.makeText(getActivity(), "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
            }
        });
    }

    void setDateAndTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);

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
    }

}
