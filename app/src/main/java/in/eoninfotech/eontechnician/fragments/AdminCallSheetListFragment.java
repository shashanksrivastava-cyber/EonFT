package in.eoninfotech.eontechnician.fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;

import java.util.ArrayList;
import java.util.Calendar;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.CallSheetResponse;
import in.eoninfotech.eontechnician.helper.CallSheetDetail;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 13/10/17.
 */
public class AdminCallSheetListFragment extends Fragment {

    View v;
    ListView lv;
    ArrayList<CallSheetDetail> call_sheet_list = new ArrayList<CallSheetDetail>();
    TextView datee;
    //  Button update_dataa;
    String selected_todate, s_update_date, version;
    int year, month, day;
    Calendar calen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_call_sheet_list, container, false);
       // lv = (ListView) v.findViewById(R.id.call_list);
        version = getArguments().getString("version");

       // datee = (TextView) v.findViewById(R.id.datepicker);
        //  update_dataa = (Button) v.findViewById(R.id.update_data);
        calen = Calendar.getInstance();
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        if (month + 1 < 10) {
            selected_todate = year + "-0" + +month + "-" + day;
        } else {
            selected_todate = year + "-" + month + "-" + day;
        }
        datee.setText(selected_todate);
        datee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("***string**", datee.getText().toString());
                DatePickerDialog dpdd = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    // TODO Auto-generated method stub
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear + 1 < 10) {
                            selected_todate = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                        } else {
                            selected_todate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        }
                        datee.setText(selected_todate);
                        getIncentiveList(selected_todate);
                    }
                }, year, month - 1, day);

                dpdd.show();
            }
        });

        return v;
    }

    void getIncentiveList(String date) {
//        ApiHolder get_list = ServiceConnection.getClient(version).create(ApiHolder.class);
//        Call<CallSheetResponse> call = get_list.callsheet_list(date,tech_name);
//        call.enqueue(new Callback<CallSheetResponse>() {
//            @Override
//            public void onResponse(Call<CallSheetResponse> call, Response<CallSheetResponse> response) {
//                call_sheet_list = response.body().getCallsheet_list();
//                for (int i = 0; i < call_sheet_list.size(); i++) {
//                    Log.i("****", call_sheet_list.get(i).getTech_name());
//                }
//                lv.setAdapter(new DetailAdapter(getContext(), call_sheet_list));
//                //  pDialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(Call<CallSheetResponse> call, Throwable t) {
//                t.printStackTrace();
//                //pDialog.dismiss();
//                TSnackbar snackbar = TSnackbar.make(v, "Try Again Connection Timeout", TSnackbar.LENGTH_LONG);
//                View snackbarView = snackbar.getView();
//                snackbarView.setBackgroundColor(Color.RED);
//                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
//                textView.setTextColor(Color.WHITE);
//                snackbar.show();
//            }
//        });
    }

    class DetailAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater layinfa;
        int j;
        String link;
        ArrayList<CallSheetDetail> planDetails = new ArrayList<>();

        DetailAdapter(Context c, ArrayList<CallSheetDetail> planDetails) {
            ctx = c;
            this.planDetails = planDetails;
            layinfa = LayoutInflater.from(ctx);
        }

        @Override
        public int getCount() {
            //  Log.i("*******", new StringBuilder().append(list_vehidetai.size()).toString());
            return call_sheet_list.size();
        }

        @Override
        public Object getItem(int i) {
            return call_sheet_list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View vv = view;
            j = i;
            if (vv == null) {
                vv = layinfa.inflate(R.layout.custom_call_sheet_list_item, null);
            }
            TextView techname;
            ImageView img_view;
            techname = (TextView) vv.findViewById(R.id.tech_name);
            img_view = (ImageView) vv.findViewById(R.id.call_date);

//            techname.setText(call_sheet_list.get(i).getTech_name());
//            Picasso.with(vv.getContext()).load(call_sheet_list.get(i).getImage_link()).fit().into(img_view);
//            Log.i("*****outer i****", new StringBuilder().append(i).toString());
            return vv;
        }
    }

}
