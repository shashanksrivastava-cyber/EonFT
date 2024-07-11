package in.eoninfotech.eontechnician.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.fragment.app.Fragment;
import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.activity.FaultyDevicesActivity;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.responses.DashBoardResponse;
import in.eoninfotech.eontechnician.responses.TechDashboardDetail;
import in.eoninfotech.eontechnician.databinding.DashboardNewBinding;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class DashBoardFragment extends Fragment {

    View v;
    DashboardNewBinding binding;
    String usrname, current_date, s_time, zone,version, months;
    int year, day, month, hour, minutes;
    Calendar calen = Calendar.getInstance();
    ArrayList<Float> yData = new ArrayList<>();
    ArrayList<TechDashboardDetail> dashboardList = new ArrayList<>();
    Float achivd;
    private AlertDialog progressDialog;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    private String[] xData;

    ReviewManager reviewManager;
    ReviewInfo reviewInfo = null;
    public static final int[] BRIGHT_COLORS = {
            Color.parseColor("#D32F2F"), Color.parseColor("#F44336"), Color.parseColor("#FFC03C")};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.dashboard_new, container, false);
        binding = DashboardNewBinding.inflate(getLayoutInflater(), container, false);

        sharedprefs = this.getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        usrname = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        zone = sharedprefs.getString("zone", "");

        progressDialog = new SpotsDialog(getActivity(), R.style.CustomIncentive);
        setDateAndTime();

        getReviewInfo();

        //AskForRating(0);

        binding.cvOneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
                intent.putExtra("device_value", "2");
                intent.putExtra("tab", "1");
                intent.putExtra("other", "2");
                startActivity(intent);
            }
        });
        binding.cvTwoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
                intent.putExtra("device_value", "1");
                intent.putExtra("tab", "1");
                intent.putExtra("other", "2");
                startActivity(intent);
            }
        });

        binding.cvThreeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaultyDevicesActivity.class);
                intent.putExtra("device_value", "3");
                intent.putExtra("tab", "1");
                intent.putExtra("other", "2");
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
//                intent.putExtra("device_value", "7");
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

    private void AskForRating(int _appUsedCount){

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Please Rate Us");
        alert.setIcon(R.drawable.app_icon);
        alert.setMessage("Thanks for using the application. If you like YOUR APP NAME please rate us! Your feedback is important for us!");
        alert.setPositiveButton("Rate it",new Dialog.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                String url = "https://play.google.com/store/apps/details?id=YOUR PACKAGE NAME";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        alert.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }

    private void getReviewInfo() {
        reviewManager = ReviewManagerFactory.create(getActivity());
        //reviewManager = new FakeReviewManager(getActivity());
        Task<ReviewInfo> manager = reviewManager.requestReviewFlow();
        manager.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                reviewInfo = task.getResult();
                startReviewFlow();
            } else {
                //Toast.makeText(getActivity(), "In App ReviewFlow failed to start", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startReviewFlow() {

        if (reviewInfo != null) {
            Task<Void> flow = reviewManager.launchReviewFlow(getActivity(), reviewInfo);
            flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                   // Toast.makeText(getActivity(), "In App Rating complete", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
           // Toast.makeText(getActivity(), "In App Rating failed", Toast.LENGTH_LONG).show();
        }
    }

    void getDashBoardDetail() {
        progressDialog.show();
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
                                binding.totSos.setText("" + dashboardList.get(0).getTot_sos());
                                binding.totLid.setText("" + dashboardList.get(0).getTot_lid());
                                binding.totFuel.setText("" + dashboardList.get(0).getTot_fuel());
                                binding.totTemp.setText("" + dashboardList.get(0).getTot_temp());

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
                        }
                    } else {
                        assert updateDataResponse != null;
                        Log.v("Response", updateDataResponse.toString());
                        achivd = 0.0f;
                        progressDialog.hide();
                    }
                    yData.add(Float.valueOf(33));
                    yData.add(Float.valueOf(33));
                    yData.add(Float.valueOf(33));
                    progressDialog.hide();
                }

                @Override
                public void onFailure(Call<DashBoardResponse> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getActivity(), "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
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
        binding.curntDate.setText(current_date);
        SimpleDateFormat dateFormatt = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        s_time = dateFormatt.format(calen.getTime());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        progressDialog.hide();
        Runtime.getRuntime().gc();
    }

    @Override
    public void onResume() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getDashBoardDetail();
            }
        }, 100);
        super.onResume();
    }

}
