package in.eoninfotech.eontechnician.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.reactivestreams.Subscription;

import java.util.ArrayList;

import in.eoninfotech.eontechnician.ImageUtils;
import in.eoninfotech.eontechnician.OtherMaterialAdapter;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.adapters.AdditionalAccessoryDetailsAdapter;
import in.eoninfotech.eontechnician.adapters.AdditionalMaterialDeviceAdapter;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.responses.AdditionalAccessoryDetails;
import in.eoninfotech.eontechnician.responses.AdditionalDeviceDetails;
import in.eoninfotech.eontechnician.responses.DeviceItems;
import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdditionalMaterialDetails extends AppCompatActivity {

    SharedPreferences sharedprefs;
    String usrname, version, id_no;
    ProgressDialog pDialog;
    AdditionalMaterialDeviceAdapter additionalMaterialDeviceAdapter;
    AdditionalAccessoryDetailsAdapter additionalAccessoryDetailsAdapter;
    RecyclerView vts_recyclerView,recyclerView;
    public LinearLayoutManager layoutManager,acc_layoutManager;
    TextView content_unavailable,txt_content_unavailable;
    ArrayList<AdditionalDeviceDetails> additionalDeviceDetails = new ArrayList<>();
    ArrayList<AdditionalAccessoryDetails> additionalAccessoryDetails = new ArrayList<>();
    private Subscription subscription;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.additional_material_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Additional Material Details");

        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        usrname = sharedprefs.getString("usname", "");

        Intent intent = getIntent();
        id_no = intent.getStringExtra("id_no");

        initView();
        getContent();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void initView(){

        vts_recyclerView = findViewById(R.id.vts_recyclerView);
        recyclerView = findViewById(R.id.recyclerView);
        content_unavailable = findViewById(R.id.content_unavailable);
        txt_content_unavailable = findViewById(R.id.txt_content_unavailable);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        vts_recyclerView.setLayoutManager(layoutManager);

        acc_layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(acc_layoutManager);
    }

    public void getContent(){
        try {
            pDialog = K.createProgressDialog(this);
            pDialog.setMessage("Loading");
            pDialog.show();
            pDialog.setCancelable(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<MainResponse> call = log_att.get_tech_requirement_detailed_data(id_no,usrname);
        Log.i("****call", String.valueOf(call));
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                if (response.body().getType() == 1) {
                    pDialog.hide();
                    additionalDeviceDetails = response.body().getTech_req_dtls().get(0).getDevice_dtls();
                    additionalMaterialDeviceAdapter = new AdditionalMaterialDeviceAdapter(AdditionalMaterialDetails.this,additionalDeviceDetails);
                    vts_recyclerView.setAdapter(additionalMaterialDeviceAdapter);
                    vts_recyclerView.setVisibility(View.VISIBLE);

                    additionalAccessoryDetails = response.body().getTech_req_dtls().get(0).getAccs_dtls();
                    additionalAccessoryDetailsAdapter = new AdditionalAccessoryDetailsAdapter(AdditionalMaterialDetails.this,additionalAccessoryDetails);
                    recyclerView.setAdapter(additionalAccessoryDetailsAdapter);
                    recyclerView.setVisibility(View.VISIBLE);

                } else {
                    pDialog.hide();
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
