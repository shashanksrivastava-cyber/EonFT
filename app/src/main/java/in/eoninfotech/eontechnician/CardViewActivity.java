package in.eoninfotech.eontechnician;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;


import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import in.eoninfotech.eontechnician.Responses.ActivityResponse;
import in.eoninfotech.eontechnician.Responses.TechnicianMonthDetail;
import in.eoninfotech.eontechnician.Responses.TechnicianMonthResponse;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardViewActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private Button mButton;
    private ViewPager mViewPager;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;
    String  uusername, versionname, disgnid = "0",activityName="Activities";
    private boolean mShowingFragments = false;
    ArrayList<TechnicianMonthDetail> techList = new ArrayList<>();
    RelativeLayout progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Technician of the Month");

        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        uusername = sharedprefs.getString("s_uuser", "");
        versionname = sharedprefs.getString("version", "");
        progressBar = findViewById(R.id.llayoutProgress);
        ShowProgressBar(true);
        getDetail();

        mViewPager = findViewById(R.id.viewPager);
    }

    private void getDetail() {
        ApiHolder loc_att = ServiceConnectionNewURL.getClient(versionname).create(ApiHolder.class);
        Call<TechnicianMonthResponse> locCall = loc_att.requestTechnicianoftheMonth();
        locCall.enqueue(new Callback<TechnicianMonthResponse>() {
            public void onResponse(Call<TechnicianMonthResponse> call, Response<TechnicianMonthResponse> response) {
                TechnicianMonthResponse workTypeResponse = response.body();
                techList = response.body().getTechList();
                Log.i("**work respnse", " " + response.body());
                if (techList.size() == 0) {
                } else {
                        mCardAdapter = new CardPagerAdapter(techList,this);
                        mFragmentCardAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(),
                                dpToPixels(2, this));

                        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
                        mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter);

                        mViewPager.setAdapter(mCardAdapter);
                        mViewPager.setPageTransformer(false, mCardShadowTransformer);
                        mViewPager.setOffscreenPageLimit(3);
                        ShowProgressBar(false);
                }
            }
            @Override
            public void onFailure(Call<TechnicianMonthResponse> call, Throwable t) {
                try {
                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent inteer = new Intent(CardViewActivity.this, MainActivity.class);
        startActivity(inteer);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent inteer = new Intent(CardViewActivity.this, MainActivity.class);
            startActivity(inteer);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (!mShowingFragments) {
            mButton.setText("Views");
            mViewPager.setAdapter(mFragmentCardAdapter);
            mViewPager.setPageTransformer(false, mFragmentCardShadowTransformer);
        } else {
            mButton.setText("Fragments");
            mViewPager.setAdapter(mCardAdapter);
            mViewPager.setPageTransformer(false, mCardShadowTransformer);
        }
        mShowingFragments = !mShowingFragments;
    }

    public float dpToPixels(int dp, Callback<TechnicianMonthResponse> callback) {
        return dp * (getResources().getDisplayMetrics().density);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        mCardShadowTransformer.enableScaling(b);
        mFragmentCardShadowTransformer.enableScaling(b);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    private void ShowProgressBar(boolean show) {
        try {
            if (show) {
                progressBar.setVisibility(View.VISIBLE);
                this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                progressBar.setVisibility(View.GONE);
                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
