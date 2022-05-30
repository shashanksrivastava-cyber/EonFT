package in.eoninfotech.eontechnician.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.helper.ProgressRequestBody;
import in.eoninfotech.eontechnician.view.MyEditText;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.view.MyTextView;
import in.eoninfotech.eontechnician.helper.PlantDetails;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAddNextDayPlan extends Fragment {
    View v;
    String disttid;
    String uusername, version;
    CheckConnection chk;
    ArrayAdapter adapterplant, vehadapter, drsadapter;
    String date;
    MySearchableSpinner plants;
    String selected_todate;
    int year, month, day;
    GregorianCalendar calen;
    TextView datee;
    Button update_dataa;
    ListView veh_list, drs_list;
    ArrayList<String> plant_name = new ArrayList<>();
    public ArrayList<String> veh_name = new ArrayList<>();
    public ArrayList<String> drs_name = new ArrayList<>();
    ArrayList<PlantDetails> plantdetail = new ArrayList<PlantDetails>();
    ProgressDialog pDialog;
    MyTextView drs_txt;
    MyEditText client_nme, clientnumber, remarks;
    public int vehsize, drssize;
    CheckBox spokentoclient;
    String s_date, s_cust_name, s_cust_id, s_veh_chk, s_drs_chk, s_spoken_to_client, s_client_name,
            s_client_number, s_remarks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.activity_next_day_plan, container, false);
        plants = v.findViewById(R.id.nxt_plant);
        disttid = getArguments().getString("disttid");
        uusername = getArguments().getString("usernme");
        version = getArguments().getString("version");

        getPlantDeviceDrsData();

        datee = v.findViewById(R.id.datee);
        update_dataa = v.findViewById(R.id.nxt_update_data);
        veh_list = v.findViewById(R.id.vehicle_list);
        drs_list = v.findViewById(R.id.drs_list);
        drs_txt = v.findViewById(R.id.drs_txt);
        client_nme = v.findViewById(R.id.nxt_client_name);
        clientnumber = v.findViewById(R.id.nxt_client_number);
        remarks = v.findViewById(R.id.nxt_remarks);
        spokentoclient = v.findViewById(R.id.nxt_spoken_client);
        calen = new GregorianCalendar();
        calen.add(Calendar.DATE, 1);
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        if (month + 1 < 10) {
            selected_todate = day + "-0" + month + "-" + year;
        } else {
            selected_todate = day + "-" + month + "-" + year;
        }
        datee.setText(selected_todate);
        plants.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                veh_name.clear();
                vehsize = plantdetail.get(position).getRegno().size();
                if (vehsize > 1) {
                    vehsize = vehsize - 1;
                    for (int i = 0; i < vehsize; i++) {
                        veh_name.add(plantdetail.get(position).getRegno().get(i));
                    }
                }
                if (vehsize > 4) {
                    veh_list.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * plantdetail.get(position).getRegno().size() + 1));
                } else {
                    veh_list.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * plantdetail.get(position).getRegno().size()));
                }
                vehadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, veh_name);
                veh_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                vehadapter.notifyDataSetChanged();
                veh_list.setAdapter(vehadapter);
                drs_name.clear();
                if (plantdetail.get(position).getDrs_details().size() == 0) {
                    drs_txt.setVisibility(View.INVISIBLE);
                } else {
                    drs_txt.setVisibility(View.VISIBLE);
                }
                drssize = plantdetail.get(position).getDrs_details().size();
                if (drssize > 1) {
                    drssize = drssize - 1;
                    for (int i = 0; i < drssize; i++) {
                        drs_name.add(plantdetail.get(position).getDrs_details().get(i));
                    }
                }
                if (drssize > 4) {
                    drs_list.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * plantdetail.get(position).getDrs_details().size() + 1));
                } else {
                    drs_list.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80 * plantdetail.get(position).getDrs_details().size()));
                }
                drsadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, drs_name);
                drs_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                drsadapter.notifyDataSetChanged();
                drs_list.setAdapter(drsadapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        update_dataa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*pDialog = new ProgressDialog(getContext());
                pDialog.setMessage("Loading...");
                pDialog.show();*/
                s_date = datee.getText().toString();
                int i = plants.getSelectedItemPosition();
                s_cust_name = plantdetail.get(i).getPlant_name();
                s_cust_id = plantdetail.get(i).getPlant_id();

                SparseBooleanArray checked = veh_list.getCheckedItemPositions();
                s_veh_chk = "";
                Log.i("***values", String.valueOf(checked) + "count" + veh_list.getCheckedItemCount());
                try {
                    for (int j = 0; j < checked.size(); j++) {
                        int key = checked.keyAt(i);
                        s_veh_chk = s_veh_chk + (veh_name.get(key)) + ":";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SparseBooleanArray checkeddrs = drs_list.getCheckedItemPositions();
                s_drs_chk = "";
                Log.i("***values", String.valueOf(checkeddrs) + "count" + drs_list.getCheckedItemCount());
                try {
                    for (int j = 0; j < checkeddrs.size(); j++) {
                        int key = checkeddrs.keyAt(i);
                        s_drs_chk = s_drs_chk + (drs_name.get(key)) + ":";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (spokentoclient.isChecked()) {
                    s_spoken_to_client = "1";
                } else {
                    s_spoken_to_client = "0";
                }
                s_client_name = client_nme.getText().toString();
                s_client_number = clientnumber.getText().toString();
                s_remarks = remarks.getText().toString();
                if (s_client_number.length() < 10 && s_client_number.length() > 1) {
                    Toast.makeText(getActivity(), "Number is less then 10 characters", Toast.LENGTH_SHORT).show();
                } else if (s_cust_name.equals("") || s_cust_id.equals("") || s_spoken_to_client.equals("")) {
                    Toast.makeText(getActivity(), "Mandantory Fields are not filled", Toast.LENGTH_SHORT).show();
                } else {
                    ApiHolder uploadImage = ServiceConnection.getClient(version).create(ApiHolder.class);
                    Call<UpdateDataResponse> call = uploadImage.update_next_day_plan(uusername, s_date, s_cust_name, s_cust_id, s_veh_chk, s_drs_chk, s_spoken_to_client, s_client_name, s_client_number, s_remarks);
                    Log.i("****data", uusername + s_date + s_cust_name + s_cust_id + s_veh_chk + s_drs_chk + s_spoken_to_client + s_client_name + s_client_number + s_remarks);
                    call.enqueue(new Callback<UpdateDataResponse>() {
                        @Override
                        public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                            UpdateDataResponse updateDataResponse = response.body();
                            if (updateDataResponse != null) {
                                Toast.makeText(getActivity(), updateDataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                if (updateDataResponse.getType() == 1) {
                                }
                            } else {
                                assert updateDataResponse != null;
                                Log.v("Response", updateDataResponse.toString());
                            }
                            //pDialog.dismiss();
                        }
                        @Override
                        public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                            t.printStackTrace();
                            //pDialog.dismiss();
                            TSnackbar snackbar = TSnackbar.make(getView(), "Try Again  Connection timeout", TSnackbar.LENGTH_LONG);
                            View snackbarView = snackbar.getView();
                            snackbarView.setBackgroundColor(Color.RED);
                            TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            snackbar.show();
                            //Toast.makeText(CallSheetActivity.this,"Try Again-Connection timeout",Toast.LENGTH_LONG).show();
                        }
                    });
                    // new UpdateInstall().execute("abc");
                }
            }
        });
        return v;
    }
    void getPlantDeviceDrsData() {
        pDialog = new ProgressDialog(v.getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        ApiHolder get_list = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<UpdateDataResponse> call = get_list.plan_detail(uusername, K.Url.urlkey);
        call.enqueue(new Callback<UpdateDataResponse>() {
            @Override
            public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                plantdetail = response.body().getPlantdetail();
                for (int i = 0; i < plantdetail.size(); i++) {
                    Log.i("****", plantdetail.get(i).getPlant_name());
                    plant_name.add(plantdetail.get(i).getPlant_name());
                }
                adapterplant = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, plant_name);
                plants.setAdapter(adapterplant);
                pDialog.dismiss();



            }
            @Override
            public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                t.printStackTrace();
                pDialog.dismiss();
                TSnackbar snackbar = TSnackbar.make(v, "Try Again Connection Timeout", TSnackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(Color.RED);
                TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();

            }
        });
    }
}
