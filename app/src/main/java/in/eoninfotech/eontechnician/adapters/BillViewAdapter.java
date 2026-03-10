package in.eoninfotech.eontechnician;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.BillDetails;
import in.eoninfotech.eontechnician.webservice.BillResponse;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class BillViewAdapter extends RecyclerView.Adapter<BillViewAdapter.ActivityHolder> {

    Context context;
    private final ArrayList<BillDetails> billDetails;
    Dialog myDialog;
    ProgressDialog pDialog;
    String version;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    TextView dialog_bill_no,dialog_amt,dialog_remark,cancel,ok;
    MessageAdapterListener listener;

    public BillViewAdapter(Context context, ArrayList<BillDetails> billDetails,MessageAdapterListener listener) {

        this.billDetails = billDetails;
        this.context=context;
        myDialog = new Dialog(context);
        this.listener = listener;
        sharedprefs = this.context.getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        version = sharedprefs.getString("version", "");
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ActivityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_view_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {

        final BillDetails billDetailsResponse = billDetails.get(position);

        holder.bill_no.setText(billDetailsResponse.bill_no);
        holder.date.setText(billDetailsResponse.bill_date);
        holder.amount.setText("₹" + billDetailsResponse.bill_amt);
        holder.status.setText(billDetailsResponse.status);
        if(billDetailsResponse.status.equalsIgnoreCase("Approved")){
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
        }else if(billDetailsResponse.status.equalsIgnoreCase("Rejected")){
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
        }else if(billDetailsResponse.status.equalsIgnoreCase("Received")){
            holder.status.setTextColor(context.getResources().getColor(R.color.c));
        }else if(billDetailsResponse.status.equalsIgnoreCase("Cancelled")){
            holder.status.setTextColor(context.getResources().getColor(R.color.dash_red));
        }else {
        }

        if(billDetailsResponse.status.equalsIgnoreCase("Approved")){
            holder.rej_date.setVisibility(View.GONE);
            holder.title.setVisibility(View.VISIBLE);
            holder.app_amount.setVisibility(View.VISIBLE);
            holder.app_date.setVisibility(View.VISIBLE);
            holder.app_amount.setText("₹" + billDetailsResponse.app_amt);
            holder.app_date.setText(billDetailsResponse.app_date);
            holder.ll_form.setVisibility(View.GONE);
            holder.view1.setVisibility(View.GONE);
        }else if(billDetailsResponse.status.equalsIgnoreCase("Rejected")) {
            holder.title.setVisibility(View.GONE);
            holder.app_date.setVisibility(View.VISIBLE);
            holder.app_amount.setVisibility(View.GONE);
            holder.app_date.setText("Bill Rejected");
            holder.rej_date.setText(billDetailsResponse.rej_date);
            holder.ll_form.setVisibility(View.GONE);
            holder.view1.setVisibility(View.GONE);
        }else if(billDetailsResponse.status.equalsIgnoreCase("Cancelled")) {
            holder.title.setVisibility(View.GONE);
            holder.app_date.setVisibility(View.VISIBLE);
            holder.app_amount.setVisibility(View.GONE);
            holder.app_date.setText("Bill cancelled");
            holder.rej_date.setText(billDetailsResponse.canc_date);
            holder.ll_form.setVisibility(View.GONE);
            holder.view1.setVisibility(View.GONE);
        }else {
            holder.title.setVisibility(View.GONE);
            holder.app_date.setVisibility(View.VISIBLE);
            holder.app_date.setText("Bill not processed yet");
            holder.app_amount.setVisibility(View.GONE);
            holder.rej_date.setText(billDetailsResponse.rec_date);
            holder.view1.setVisibility(View.VISIBLE);
            holder.ll_form.setVisibility(View.VISIBLE);
        }
        if(billDetailsResponse.remarks.equalsIgnoreCase("-")){
            holder.remarks.setText("Remarks : ");
        }else {
            holder.remarks.setText("Remarks : " + billDetailsResponse.remarks);
        }

        if(billDetailsResponse.remarks.equalsIgnoreCase("-")){
            holder.action_by.setText("Action By :" );
        }else {
            holder.action_by.setText("Action By : " + billDetailsResponse.action_by);
        }

        holder.viewBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s_bill_no  = billDetails.get(position).bill_no;
                String s_amount = billDetails.get(position).bill_amt;
                showDialog(s_bill_no,s_amount,position);
            }
        });
    }

    private void showDialog(String s_bill_no, String s_amount, int position) {
        myDialog.setContentView(R.layout.bill_confirmation_dialog);
        cancel = myDialog.findViewById(R.id.cancel);
        ok = myDialog.findViewById(R.id.ok);
        dialog_bill_no = myDialog.findViewById(R.id.dialog_bill_no);
        dialog_amt = myDialog.findViewById(R.id.dialog_amt);
        dialog_remark = myDialog.findViewById(R.id.dialog_remark);

        dialog_bill_no.setText(s_bill_no);
        dialog_amt.setText("₹" + s_amount);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.hide();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog_remark.getText().toString().equalsIgnoreCase("")){
                    dialog_remark.setError("Fill Remarks");
                }else {
                   String s_remarks = dialog_remark.getText().toString();
                    cancelBill(position,s_bill_no,s_remarks);
                }
            }
        });

        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void cancelBill(int position,String s_bill_no,String s_remarks) {
        try {
            pDialog = K.createProgressDialog(context);
            pDialog.setMessage("Loading");
            pDialog.show();
            pDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<BillResponse> call = log_att.cancel_bill(s_bill_no,s_remarks);
        Log.i("****call", String.valueOf(call));
        call.enqueue(new Callback<BillResponse>() {
            @Override
            public void onResponse(Call<BillResponse> call, Response<BillResponse> response) {
                pDialog.dismiss();
                if(response.body().getType().equalsIgnoreCase("1")){
                    myDialog.hide();
                    Toast.makeText(context, "Bill Cancelled Successfully", Toast.LENGTH_SHORT).show();
                    listener.onCancelButtonClick(position,s_bill_no,s_remarks);
                }
                else {
                }
            }
            @Override
            public void onFailure(Call<BillResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {

        return billDetails.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        TextView bill_no,amount,status,date,app_amount,app_date,remarks,rej_date,title,viewBill,action_by;
        LinearLayout ll_form;
        View view1;

        public ActivityHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            bill_no = itemView.findViewById(R.id.bill_no);
            amount = itemView.findViewById(R.id.amount);
            status = itemView.findViewById(R.id.status);
            app_amount = itemView.findViewById(R.id.app_amount);
            app_date = itemView.findViewById(R.id.app_date);
            remarks = itemView.findViewById(R.id.remarks);
            rej_date = itemView.findViewById(R.id.rej_date);
            title = itemView.findViewById(R.id.app_title);
            ll_form = itemView.findViewById(R.id.ll_form);
            view1 = itemView.findViewById(R.id.view1);
            viewBill = itemView.findViewById(R.id.viewBill);
            action_by = itemView.findViewById(R.id.action_by);
        }
    }

    public interface MessageAdapterListener {

        void onCancelButtonClick(int position, String s_bill_no, String s_remarks);
    }
}
