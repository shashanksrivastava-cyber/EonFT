package in.eoninfotech.eontechnician;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import in.eoninfotech.eontechnician.helper.ClientList;
import in.eoninfotech.eontechnician.helper.DashboardDetail;
/***************************************************************************/
// Copyright EON Infotech Ltd., unpublished work, created 2016.          //
// This computer program includes Confidential, Proprietary information  //
// and is a trade secret of EON Infotech Ltd. All use, disclosure and/or //
// reproduction is prohibited unless authorised in writing by an         //
// authorised officer of EON Infotech Ltd. All rights reserved.          //

/**************************************************************************/

public class ScheduleActivity extends AppCompatActivity  {
    ListView lv;
    ArrayList<ClientList> list_vehidetai = new ArrayList<ClientList>();
    private PieChart mChart;
    // we're going to display pie chart for smartphones martket shares
    private float[] yData = {33, 33, 34};
    private String[] xData = {"Faulty VTS", "Working VTS", "working DRS"};
    public static final int[] BRIGHT_COLORS = {
            Color.parseColor("#0091EA"), Color.parseColor("#F44336"), Color.parseColor("#FFC107")
            , Color.parseColor("#009688"), Color.parseColor("#ffd600")};
    DashboardDetail d = new DashboardDetail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fragment_dashboard);
        mChart = (PieChart) findViewById(R.id.piechart);

        // configure pie chart
        mChart.setUsePercentValues(false);
        mChart.getDescription().setEnabled(false);
        // enable hole and configure
        mChart.setDrawHoleEnabled(true);
        // mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(40);
        mChart.setTransparentCircleRadius(10);
        mChart.setHoleColor(Color.parseColor("#616161"));
        mChart.setCenterText("Total %age\n95%");
        mChart.setCenterTextColor(Color.parseColor("#ffffff"));
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
            }

            @Override
            public void onNothingSelected() {

            }
        });
        // add data
        addData();

        // customize legends
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        //l.setExtra(BRIGHT_COLORS,xData);

    }
    private void addData() {
        float mult = 100;
        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();

        for (int i = 0; i < yData.length; i++) {
            yVals1.add(new PieEntry(yData[i], xData[i]));
        }
        /*ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xData.length; i++) {
            xVals.add(xData[i]);
        }*/
        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "Performance Detail");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        //for (int c : ColorTemplate.VORDIPLOM_COLORS) colors.add(c);

        //  for (int c : ColorTemplate.JOYFUL_COLORS) colors.add(c);

        //  for (int c : ColorTemplate.COLORFUL_COLORS) colors.add(c);

        for (int c : BRIGHT_COLORS) colors.add(c);

        // for (int c : ColorTemplate.PASTEL_COLORS) colors.add(c);

        //colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        // instantiate pie data object now
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        // pieChart.invalidate();

        // undo all highlights
        mChart.highlightValues(null);

        // update pie chart
        mChart.invalidate();

    }

}
