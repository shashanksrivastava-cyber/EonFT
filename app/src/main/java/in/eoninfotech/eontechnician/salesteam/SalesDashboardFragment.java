package in.eoninfotech.eontechnician.salesteam;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesDashboardFragment extends Fragment {
    private PieChart mChart;
    String usrname, current_date, s_time, uusername, versionname, disgnid = "0";
    Toolbar toolbar;
    ProgressDialog pDialog;
    int year, day;
    String month, version;
    Calendar calen = Calendar.getInstance();
    TextView t_curntday, t_target;
    Float achivd, total;
    ArrayList<Float> yData = new ArrayList<>();
    private String[] xData = {"Target Completed", "Remaining"};
    public static final int[] BRIGHT_COLORS = {
            Color.parseColor("#43A047"), Color.parseColor("#F44336")};
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.dashboard_sales_main, container, false);

        mChart = (PieChart) v.findViewById(R.id.piechart);
        t_curntday = (TextView) v.findViewById(R.id.curnt_date);
        t_target = (TextView) v.findViewById(R.id.target_month_text);
        usrname = getArguments().getString("usernme");
        version = getArguments().getString("version");
        setDateAndTime();
        getTargetDetail();
     /*   fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), SalesInstallationActivity.class);
                startActivity(intent);
                //finish();
            }
        });*/
        // configure pie chart
        mChart.setUsePercentValues(false);
        mChart.getDescription().setEnabled(false);
        // enable hole and configure
        mChart.setDrawHoleEnabled(true);
        // mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(40);
        mChart.setTransparentCircleRadius(10);
        mChart.setHoleColor(Color.parseColor("#ffffff"));
        mChart.setCenterTextColor(Color.parseColor("#000000"));
        mChart.setDrawCenterText(true);

        // enable rotation of the chart by touch
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // set a chart value selected listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;
                /*Toast.makeText(getActivity(), xData + "Value: " + e.getY() + ", index: " + h.getX() + ", DataSet index: " + h.getDataSetIndex() + "%", Toast.LENGTH_SHORT).show();   */
            }
            @Override
            public void onNothingSelected() {

            }
        });

        return v;
    }

    void getTargetDetail() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        ApiHolder log_att = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<UpdateDataResponse> call = log_att.sales_target(usrname);
        Log.i("****call", String.valueOf(call));
        call.enqueue(new Callback<UpdateDataResponse>() {
            @Override
            public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                UpdateDataResponse updateDataResponse = response.body();
                Log.i("**respnse", " " + response.body());
                if (updateDataResponse != null) {
                    if (updateDataResponse.getType() == 1) {
                        mChart.setCenterText("Total");
                        t_target.setText(updateDataResponse.getTarget() + " vts");
                        try {
                            total = Float.valueOf(updateDataResponse.getTarget());
                            achivd = Float.valueOf(updateDataResponse.getAchieved());
                            mChart.setCenterText("Total \n"+ achivd);
                        } catch (Exception e) {
                            achivd = 0.0f;
                        }

                    }
                } else {
                    assert updateDataResponse != null;
                    Log.v("Response", updateDataResponse.toString());
                    achivd=0.0f;
                }
                int per_achievd = Math.round(achivd * 100 / total);
                Log.i("*** percntage", "" + per_achievd);
                if (per_achievd < 100) {
                    yData.add(Float.valueOf(per_achievd));
                    yData.add(Float.valueOf(100 - per_achievd));
                } else {
                    yData.add(Float.valueOf(100));
                    yData.add(Float.valueOf(0));
                }
                addData(yData);
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                t.printStackTrace();

                pDialog.dismiss();
                Toast.makeText(getActivity(), "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
            }
        });
    }

    void setDateAndTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
        month = dateFormat.format(calen.getTime());
        day = calen.get(Calendar.DAY_OF_MONTH);
        Log.i("*** day + month", day + month);
        if (day == 1 || day == 21 || day == 31) {
            current_date = day + "st, " + month;
        } else if (day == 2 || day == 22) {
            current_date = day + "nd, " + month;
        } else if (day == 3 || day == 23) {
            current_date = day + "rd, " + month;
        } else {
            current_date = day + "th, " + month;
        }
        t_curntday.setText(current_date);
        SimpleDateFormat dateFormatt = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        s_time = dateFormatt.format(calen.getTime());
    }

    private void addData(ArrayList<Float> yData) {
        float mult = 100;
        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();

        for (int i = 0; i < yData.size(); i++) {
            yVals1.add(new PieEntry(yData.get(i), xData[i]));
        }
        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "Performance Detail");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : BRIGHT_COLORS) colors.add(c);

        dataSet.setColors(colors);
        // instantiate pie data object now
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // update pie chart
        mChart.invalidate();
    }
}
