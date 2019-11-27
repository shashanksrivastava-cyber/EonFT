package in.eoninfotech.eontechnician.salesteam;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.Responses.ViewEntryResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SalesViewEntryFragment extends Fragment implements View.OnClickListener {

    View v;
    TextView t_from, t_to, no_record;
    Button btn_srch;
    int year, month, day;
    Calendar calen = Calendar.getInstance();
    String username, current_date, s_time, s_to_date, s_from_date, version;
    ProgressDialog pDialog;
    ArrayList<ViewEntryResponse> sales_entries = new ArrayList<>();
    RecyclerView rv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_sales_view_entry, container, false);
        t_from = (TextView) v.findViewById(R.id.from_date);
        t_to = (TextView) v.findViewById(R.id.to_date);
        btn_srch = (Button) v.findViewById(R.id.btn_srch);
        rv = (RecyclerView) v.findViewById(R.id.rv_install_list);
        no_record = (TextView) v.findViewById(R.id.text_no_record);
        setDateAndTime();
        username = getArguments().getString("usernme");
        version = getArguments().getString("version");
        t_from.setOnClickListener(this);
        t_to.setOnClickListener(this);
        btn_srch.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(linearLayoutManager);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.from_date:
                DatePickerDialog dpdd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    // TODO Auto-generated method stub
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear + 1 < 10) {
                            s_from_date = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                        } else {
                            s_from_date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        }
                        t_from.setText(s_from_date);
                    }
                }, year, month - 1, day);

                dpdd.show();


                break;
            case R.id.to_date:
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    // TODO Auto-generated method stub
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear + 1 < 10) {
                            s_to_date = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                        } else {
                            s_to_date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        }
                        t_to.setText(s_to_date);
                    }
                }, year, month - 1, day);

                dpd.show();
                break;
            case R.id.btn_srch:
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Loading...");
                pDialog.show();
                ApiHolder log_att = ServiceConnection.getClient(version).create(ApiHolder.class);
                Call<UpdateDataResponse> call = log_att.view_entry(username, t_from.getText().toString(), t_to.getText().toString());
                Log.i("****call", String.valueOf(call));
                call.enqueue(new Callback<UpdateDataResponse>() {
                    @Override
                    public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                        UpdateDataResponse updateDataResponse = response.body();
                        Log.i("**respnse", " " + response.body());
                        if (updateDataResponse != null) {
                            if (updateDataResponse.getType() == 1) {
                                try {
                                    sales_entries = updateDataResponse.getView_sales_data();
                                    rv.setVisibility(View.VISIBLE);
                                    no_record.setVisibility(View.GONE);
                                    rv.setAdapter(new CustomRecyclerAdapter(getActivity(), sales_entries));

                                } catch (Exception e) {
                                    rv.setVisibility(View.GONE);
                                    no_record.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            assert updateDataResponse != null;
                            Log.v("Response", updateDataResponse.toString());
                        }
                        pDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                        t.printStackTrace();
                        pDialog.dismiss();
                        Toast.makeText(getActivity(), "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }

    void setDateAndTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        s_time = dateFormat.format(calen.getTime());
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        if (month + 1 < 10) {
            current_date = year + "-0" + +month + "-" + day;
        } else {
            current_date = year + "-" + month + "-" + day;
        }
        t_from.setText(current_date);
        t_to.setText(current_date);
    }

    class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder> {

        ArrayList<ViewEntryResponse> entries;
        Context context;
        int pos;
        public CustomRecyclerAdapter(Context context, ArrayList<ViewEntryResponse> inst_entries) {
            this.context = context;
            this.entries = inst_entries;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// infalte the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_view_new_entries_item, parent, false);
// set the view's size, margins, paddings and layout parameters
            MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
            return vh;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            pos=position;
            // set the data in items
            holder.client_nm.setText(entries.get(position).getCust_name());
            try {
                holder.sales_person.setText("Filled by " + entries.get(position).getSales_person().toUpperCase());
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.address.setText(entries.get(position).getCust_street_name() + ", " + entries.get(position).getCust_city() + ", " + entries.get(position).getCust_district() + ", " + entries.get(position).getCust_state() + ", " + entries.get(position).getCust_pincode() + "\n M: " + entries.get(position).getCust_office_number());
            try {
                holder.vts_qty.setText(entries.get(position).getVts_quantity());
            } catch (Exception e) {
                holder.vts_qty.setText("0");
            }
            //drs_qty.setText(entries.get(position).getDrs_quantity());
            holder.vehicle_type.setText(entries.get(position).getVehicle_type());
            holder.entry_date.setText(entries.get(position).getDate());
            if (entries.get(position).getInstall_type().equalsIgnoreCase("demo")) {
                holder.bill.setText("No Order number");
                holder.btn_view_bill.setClickable(false);
            } else {
                holder.btn_view_bill.setClickable(true);
                holder.bill.setText("Order no - " + entries.get(position).getOrder_no().trim());
            }
            holder.person_detail.setText(entries.get(position).getCust_p_name() + "\n" + entries.get(position).getCust_p_number() + "\n");
            holder.priority.setText("Low: " + entries.get(position).getLow_count() + "\nNormal: " + entries.get(position).getNormal_count() + "\nHigh: " + entries.get(position).getHigh_count());
            try {
                holder.remarks.setText(entries.get(position).getRemarks());
            } catch (Exception e) {
                holder.remarks.setText("--");
            }
            // implement setOnClickListener event on item view.
            holder.btn_view_bill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ViewBillActivity.class);
                    intent.putExtra("order", entries.get(pos).getOrder_no());
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
            });
            holder.btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EditInstallationDetailActivity.class);
                    intent.putExtra("order", entries.get(pos).getOrder_no());
                    intent.putExtra("sale_id",entries.get(pos).getSale_id());
                    Log.i("***sale_id",entries.get(pos).getSale_id());
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
            });

        }
        @Override
        public int getItemCount() {
            Log.i("***entries size",""+ entries.size());
            return entries.size();
        }
        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView sales_person, client_nm, address, vts_qty, vehicle_type, entry_date, bill, person_detail, priority, remarks;
            Button btn_view_bill, btn_edit;
            public MyViewHolder(View v) {
                super(v);

// get the reference of item view's
                client_nm = (TextView) v.findViewById(R.id.sv_client_name);
                address = (TextView) v.findViewById(R.id.sv_address);
                vts_qty = (TextView) v.findViewById(R.id.sv_vts_qty);
                vehicle_type = (TextView) v.findViewById(R.id.sv_vehicle_type);
                entry_date = (TextView) v.findViewById(R.id.sv_entry_date);
                bill = (TextView) v.findViewById(R.id.sv_bill);
                person_detail = (TextView) v.findViewById(R.id.sv_person_detail);
                priority = (TextView) v.findViewById(R.id.sv_priority);
                remarks = (TextView) v.findViewById(R.id.sv_remarks);
                sales_person = (TextView) v.findViewById(R.id.sv_sales_person);
                btn_view_bill = (Button) v.findViewById(R.id.btn_view_bill);
                btn_edit = (Button) v.findViewById(R.id.btn_edit);
            }
        }
    }
}
