package in.eoninfotech.eontechnician.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.eoninfotech.eontechnician.InstInstructionAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.responses.InstInstructionResponse;
import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.responses.InstructionDetail;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaultyDeviceDetails extends AppCompatActivity implements InstInstructionAdapter.InstInstructionAdapterListener {

    SharedPreferences sharedprefs;
    String usrname, zone, version;
    String locId, server, database, cust_id, custName, location;
    public RecyclerView recyclerView;
    public LinearLayoutManager layoutManager;
    ArrayList<InstructionDetail> instructionDetails = new ArrayList<>();
    InstInstructionAdapter instAdapter;
    private TextView txtContentUnavailable;
    InstInstructionAdapter.InstInstructionAdapterListener listener;
    String message_id = "";
    TableLayout tableDevices;
    TableLayout tableDRS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faulty_fragment_detail);

        // Initialize action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Faulty Devices Details");
        }

        // Get shared preferences
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        usrname = sharedprefs.getString("dis_user", "");
        version = sharedprefs.getString("version", "");
        zone = sharedprefs.getString("zone", "");

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        txtContentUnavailable = findViewById(R.id.txt_content_unavailable);
        tableDevices = findViewById(R.id.table_faulty_devices);
        tableDRS = findViewById(R.id.table_faulty_drs);

        // Setup recycler view
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Get intent data
        Intent intent = getIntent();
        String faultyVTS = intent.getStringExtra("Faulty VTS") != null ? intent.getStringExtra("Faulty VTS") : "";
        String faultyDRS = intent.getStringExtra("Faulty DRS") != null ? intent.getStringExtra("Faulty DRS") : "";
        locId = intent.getStringExtra("LocId");
        server = intent.getStringExtra("Server");
        database = intent.getStringExtra("Database");
        cust_id = intent.getStringExtra("Cust_id");
        custName = intent.getStringExtra("CustomerName");
        location = intent.getStringExtra("Location");

        // Setup tables
        setupFaultyDevicesTable(faultyVTS);
        setupFaultyDRSTable(faultyDRS);

        // Set up listener for instructions
        listener = new InstInstructionAdapter.InstInstructionAdapterListener() {
            @Override
            public void onMessageRowClicked(int position) {
                InstructionDetail message = instructionDetails.get(position);
                message_id = instructionDetails.get(position).getMsg_id();
                updateData();
            }
        };

        getContent();
    }

    private void setupFaultyDevicesTable(String faultyVTS) {
        // Clear existing views except header
        tableDevices.removeAllViews();

        // Header Row for Faulty Devices
        TableRow headerVTS = new TableRow(this);
        headerVTS.setBackgroundColor(Color.DKGRAY);
        headerVTS.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        addCell(headerVTS, "Vehicle No.", Color.WHITE, true);
        addCell(headerVTS, "ID", Color.WHITE, true);
        addCell(headerVTS, "SR.NO.", Color.WHITE, true);
        addCell(headerVTS, "Date", Color.WHITE, true);
        tableDevices.addView(headerVTS);

//        if (!faultyVTS.isEmpty()) {
//            if (faultyVTS.endsWith("|")) {
//                faultyVTS = faultyVTS.substring(0, faultyVTS.length() - 1);
//            }
//
//            String[] records = faultyVTS.split("\\|");
//            for (int i = 0; i < records.length; i++) {
//                String[] parts = records[i].split("/");
//                if (parts.length == 3) {
//                    String vehicleNo = parts[0];
//                    String id = parts[1];
//                    String[] dateTimeParts = parts[2].split(" ");
//                    String date = dateTimeParts.length > 0 ? dateTimeParts[0] : "";
//
//                    TableRow row = new TableRow(this);
//                    row.setBackgroundColor(i % 2 == 0 ? Color.parseColor("#F5F5F5") : Color.parseColor("#E0E0E0"));
//                    addCell(row, vehicleNo, Color.BLACK, false);
//                    addCell(row, id, Color.BLACK, false);
//                    addCell(row, date, Color.BLACK, false);
//                    tableDevices.addView(row);
//                }
//            }
//        } else {
//            addEmptyRow(tableDevices, 3, "No Faulty VTS Devices Found");
//        }

        if (!faultyVTS.isEmpty()) {
            // Determine the format and split accordingly
            if (faultyVTS.contains("|")) {
                // Format: PB65M6276/31/14-05-2025 17:36:52|PB65AK3664/74/14-05-2025 13:38:24|
                String[] records = faultyVTS.split("\\|");
                for (int i = 0; i < records.length; i++) {
                    if (!records[i].isEmpty()) {
                        String[] parts = records[i].split("/");
                        if (parts.length >= 4) {
                            TableRow row = new TableRow(this);
                            row.setBackgroundColor(i % 2 == 0 ? Color.parseColor("#F5F5F5") : Color.parseColor("#E0E0E0"));

                            addCell(row, parts[0], Color.BLACK, false); // Vehicle No
                            addCell(row, parts[1], Color.BLACK, false); // ID

                            String srNo = parts[2].length() > 0 ? parts[2] : "N/A";
                            addCell(row, srNo, Color.BLACK, false); // SR NO.

                            // Extract date from timestamp
//                            String[] dateTime = parts[2].split(" ");
                            String date = parts[3].length() > 0 ? parts[3] : "N/A";
                            addCell(row, date, Color.BLACK, false);

                            tableDevices.addView(row);
                        }else {
                            TableRow row = new TableRow(this);
                            row.setBackgroundColor(i % 2 == 0 ? Color.parseColor("#F5F5F5") : Color.parseColor("#E0E0E0"));

                            addCell(row, parts[0], Color.BLACK, false); // Vehicle No
                            addCell(row, parts[1], Color.BLACK, false); // ID

                            // Extract date from timestamp
//                            String[] dateTime = parts[2].split(" ");
                            String date = parts[2].length() > 0 ? parts[2] : "N/A";
                            addCell(row, date, Color.BLACK, false);

                            tableDevices.addView(row);
                        }
                    }
                }
            } else if (faultyVTS.contains(":")) {
                // Format: PB65BB7918:PB65BB8168:
                String[] records = faultyVTS.split(":");
                for (int i = 0; i < records.length; i++) {
                    if (!records[i].isEmpty()) {
                        TableRow row = new TableRow(this);
                        row.setBackgroundColor(i % 2 == 0 ? Color.parseColor("#F5F5F5") : Color.parseColor("#E0E0E0"));

                        addCell(row, records[i], Color.BLACK, false); // Vehicle No
                        addCell(row, "N/A", Color.BLACK, false);     // ID
                        addCell(row, "N/A", Color.BLACK, false);     // Date

                        tableDevices.addView(row);
                    }
                }
            }
        } else {
            addEmptyRow(tableDevices, 3, "No Faulty VTS Devices Found");
        }
    }

    private void setupFaultyDRSTable(String faultyDRS) {
        // Clear existing views except header
        tableDRS.removeAllViews();

        // Header Row for Faulty DRS
        TableRow headerDRS = new TableRow(this);
        headerDRS.setBackgroundColor(Color.DKGRAY);
        headerDRS.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        addCell(headerDRS, "Vehicle No.", Color.WHITE, true);
        addCell(headerDRS, "Trip Count", Color.WHITE, true);
        addCell(headerDRS, "Last Update", Color.WHITE, true);
        tableDRS.addView(headerDRS);

        if (!faultyDRS.isEmpty()) {

            if (faultyDRS.contains("|")) {

                String[] drsRecords = faultyDRS.split("\\|");
                for (int i = 0; i < drsRecords.length; i++) {
                    String[] parts = drsRecords[i].split("/");
                    if (parts.length >= 2) {
                        TableRow row = new TableRow(this);
                        row.setBackgroundColor(i % 2 == 0 ? Color.parseColor("#F5F5F5") : Color.parseColor("#E0E0E0"));
                        addCell(row, parts[0], Color.BLACK, false);
                        addCell(row, parts[1], Color.BLACK, false);

                        // Add date if available
                        if (parts.length >= 3) {
                            String[] dateTimeParts = parts[2].split(" ");
                            String date = dateTimeParts.length > 0 ? dateTimeParts[0] : "";
                            addCell(row, date, Color.BLACK, false);
                        } else {
                            addCell(row, "N/A", Color.BLACK, false);
                        }

                        tableDRS.addView(row);
                    }
                }
            } else if (faultyDRS.contains(":")) {
                // Format: PB65BB7918:PB65BB8168:
                String[] records = faultyDRS.split(":");
                for (int i = 0; i < records.length; i++) {
                    if (!records[i].isEmpty()) {
                        TableRow row = new TableRow(this);
                        row.setBackgroundColor(i % 2 == 0 ? Color.parseColor("#F5F5F5") : Color.parseColor("#E0E0E0"));

                        addCell(row, records[i], Color.BLACK, false); // Vehicle No
                        addCell(row, "N/A", Color.BLACK, false);     // ID
                        addCell(row, "N/A", Color.BLACK, false);     // Date

                        tableDRS.addView(row);
                    }
                }
            }
        }else {
            addEmptyRow(tableDRS, 3, "No Faulty DRS Devices Found");
        }
    }

    private void addCell(TableRow row, String text, int textColor, boolean isHeader) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(textColor);
        tv.setTextSize(isHeader ? 16 : 14);
        tv.setPadding(16, 16, 16, 16);
        // Set layout parameters with weight
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                0, // 0 width with weight
                TableRow.LayoutParams.WRAP_CONTENT,
                1f); // weight

        if (isHeader) {
            // Center alignment for headers
            tv.setGravity(Gravity.CENTER);
            // Optional: make header text bold
            tv.setTypeface(null, Typeface.BOLD);
        } else {
            // Left alignment for regular cells (or CENTER if you prefer)
            tv.setGravity(Gravity.START);
        }

        tv.setLayoutParams(params);
        row.addView(tv);
    }

    private void addEmptyRow(TableLayout table, int colSpan, String message) {
        TableRow row = new TableRow(this);
        TextView tv = new TextView(this);
        tv.setText(message);
        tv.setTextColor(Color.BLACK);
        tv.setPadding(16, 24, 16, 24);
        tv.setTextSize(16);
        tv.setBackgroundColor(Color.LTGRAY);
        tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tv.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                colSpan));
        row.addView(tv);
        table.addView(row);
    }

    private void updateData() {
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<MainResponse> call = log_att.updateResponse(message_id);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                if (response.body() != null && response.body().getType() == 1) {
                    getContent();
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void getContent() {
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<InstInstructionResponse> call = log_att.instructionResponse(server, database, cust_id, locId);
        call.enqueue(new Callback<InstInstructionResponse>() {
            @Override
            public void onResponse(Call<InstInstructionResponse> call, Response<InstInstructionResponse> response) {
                if (response.body() != null) {
                    if (response.body().getType() == 1) {
                        InstInstructionResponse instInstructionResponse = response.body();
                        txtContentUnavailable.setVisibility(View.GONE);
                        instructionDetails = instInstructionResponse.getInstDetail();
                        instAdapter = new InstInstructionAdapter(getApplicationContext(), instructionDetails, listener, custName, location);
                        recyclerView.setAdapter(instAdapter);
                    } else {
                        txtContentUnavailable.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<InstInstructionResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMessageRowClicked(int position) {
        // Handle message row click
    }
}