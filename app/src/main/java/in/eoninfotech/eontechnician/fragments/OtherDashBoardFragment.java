package in.eoninfotech.eontechnician.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.snackbar.Snackbar;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.activity.FaultyDevicesActivity;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.databinding.DashboardNewBinding;
import in.eoninfotech.eontechnician.databinding.FragmentOthersNewBinding;
import in.eoninfotech.eontechnician.responses.DashBoardResponse;
import in.eoninfotech.eontechnician.responses.TechDashboardDetail;
import in.eoninfotech.eontechnician.responses.TechDetails;
import in.eoninfotech.eontechnician.responses.TechResponse;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.viewModel.ViewModelAddDashboard;
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
    FragmentOthersNewBinding binding;
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
            tot_sos, tot_lid, tot_fuel, tot_temp, um_working, sos_faulty, lid_faulty, fuel_faulty, temp_faulty, drs_add, drs_add_21;
    ArrayList<Float> yData = new ArrayList<>();
    ColorfulRingProgressView vtsSpv, drsSpv, umSpv, um_workigSpv, sos_Spv, lid_Spv, fuel_Spv, temp_Spv;
    CardView cv_one_login, cv_two_login, cv_three_login, cv_four_login, cv_five_login, cv_six_login, cv_seven_login, cv_eight_login;
    Float achivd, total;
    private String[] xData;
    TextView txt_content_unavailable;
    LinearLayout addDetail;
    public static final int[] BRIGHT_COLORS = {
            Color.parseColor("#D32F2F"), Color.parseColor("#F44336"), Color.parseColor("#FFC03C")};

    ViewModelAddDashboard viewModelAddDashboard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_others_new, container, false);
        binding = FragmentOthersNewBinding.inflate(getLayoutInflater(), container, false);

        sharedprefs = getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        uusername = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        Log.i("****stat dist n usr***", version + " " + uusername);
        progressDialog = new SpotsDialog(getActivity(), R.style.CustomIncentive);
        setDateAndTime();


        binding.cvOneLogin.setOnClickListener(new View.OnClickListener() {
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
        binding.cvTwoLogin.setOnClickListener(new View.OnClickListener() {
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

        binding.cvThreeLogin.setOnClickListener(new View.OnClickListener() {
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
        binding.cvFourLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
                intent.putExtra("device_value", "4");
                intent.putExtra("tab", "1");
                intent.putExtra("other", "2");
                startActivity(intent);
            }
        });

        binding.cvFiveLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
//                intent.putExtra("device_value", "3");
//                intent.putExtra("tab", "1");
//                intent.putExtra("other", "2");
//                startActivity(intent);
            }
        });

        binding.cvSixLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
//                intent.putExtra("device_value", "3");
//                intent.putExtra("tab", "1");
//                intent.putExtra("other", "2");
//                startActivity(intent);
            }
        });

        binding.cvSevenLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
//                intent.putExtra("device_value", "3");
//                intent.putExtra("tab", "1");
//                intent.putExtra("other", "2");
//                startActivity(intent);
            }
        });

        binding.cvEightLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
//                intent.putExtra("device_value", "3");
//                intent.putExtra("tab", "1");
//                intent.putExtra("other", "2");
//                startActivity(intent);
            }
        });

        return binding.getRoot();
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
                    binding.newInClients.setAdapter(adapter);

                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TechResponse> call, Throwable t) {
                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });

        binding.newInClients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                zone = (techList.get(i).getZone());
                binding.addName.setText(techList.get(i).getName() + "'s ADD Performence");
                binding.addDetail.setVisibility(View.VISIBLE);
                binding.txtContentUnavailable.setVisibility(View.GONE);
                getDashBoardDetail();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getDashBoardDetail() {

        progressDialog.show();

        viewModelAddDashboard = ViewModelProviders.of(this).get(ViewModelAddDashboard.class);
        viewModelAddDashboard.getAddCountsRepository(zone).observe(this, movieResponse -> {
            dashboardList = movieResponse.getTechDashboardDetails();

            if (movieResponse.getType() == 1) {
                for (int i = 0; i < dashboardList.size(); i++) {
                    binding.addTime.setText("" + dashboardList.get(i).getCur_add());
                    binding.addValue.setText("" + dashboardList.get(i).getAdd_21());
                    String color1 = dashboardList.get(i).getColor();
                    String[] separated = color1.split(";");
                    String color = separated[0];
                    int myColor = Color.parseColor(color);
                    binding.addTime.setTextColor(myColor);

                    String color2 = dashboardList.get(i).getColor21();
                    String[] separated1 = color2.split(";");
                    String color3 = separated1[0];
                    int color21 = Color.parseColor(color3);
                    binding.addValue.setTextColor(color21);

                    binding.drsAdd.setText("" + dashboardList.get(i).getDrs_add());
                    String drs_color = dashboardList.get(i).getDrs_color();
                    String[] drs_separated = drs_color.split(";");
                    String colors = drs_separated[0];
                    int drs_colors = Color.parseColor(colors);
                    binding.drsAdd.setTextColor(drs_colors);

                    binding.drsAdd21.setText("" + dashboardList.get(i).getDrs_add_21());
                    String drs21_ = dashboardList.get(i).getDrs_color21();
                    String[] separate = drs21_.split(";");
                    String drs_21 = separate[0];
                    int drs21_color = Color.parseColor(drs_21);
                    binding.addTime.setTextColor(drs21_color);

                    binding.totalVts.setText("" + dashboardList.get(0).getTot_dev());
                    binding.totalDrs.setText("" + dashboardList.get(0).getTot_drs());

                    binding.faultyVts.setText("" + dashboardList.get(0).getFaulty_dev());
                    binding.vtsSpv.setPercent(Float.parseFloat(dashboardList.get(0).getFaulty_dev()));

                    binding.faultyDrs.setText("" + dashboardList.get(0).getFaulty_drs());
                    binding.drsSpv.setPercent(dashboardList.get(0).getFaulty_drs());

                    binding.faultyUm.setText("" + dashboardList.get(0).getUmain());
                    binding.umSpv.setPercent(Float.parseFloat(dashboardList.get(0).getUmain()));

                    binding.umWorking.setText("" + dashboardList.get(0).getUmain_work());
                    binding.umWorkigSpv.setPercent(Float.parseFloat(dashboardList.get(0).getUmain_work()));

                    binding.sosFaulty.setText("" + dashboardList.get(0).getFaulty_sos());
                    binding.sosSpv.setPercent(Float.parseFloat(dashboardList.get(0).getFaulty_sos()));

                    binding.lidFaulty.setText("" + dashboardList.get(0).getFaulty_lid());
                    binding.lidSpv.setPercent(Float.parseFloat(dashboardList.get(0).getFaulty_lid()));

                    binding.fuelFaulty.setText("" + dashboardList.get(0).getFaulty_fuel());
                    binding.fuelSpv.setPercent(Float.parseFloat("" + dashboardList.get(0).getFaulty_fuel()));

                    binding.tempFaulty.setText("" + dashboardList.get(0).getFaulty_temp());
                    binding.tempSpv.setPercent(Float.parseFloat(dashboardList.get(0).getFaulty_temp()));

                    progressDialog.hide();

                }
            } else {
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

    @Override
    public void onResume() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addTechnicians();
                getDashBoardDetail();
            }
        }, 100);

        super.onResume();
    }


}
