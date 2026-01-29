package in.eoninfotech.eontechnician;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import in.eoninfotech.eontechnician.storage.LocationPrefs;
import in.eoninfotech.eontechnician.activity.LoginActivityNew;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import in.eoninfotech.eontechnician.webservice.TrackingDetail;
import in.eoninfotech.eontechnician.webservice.TrackingResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 17/10/18.
 */

public class SplashActivity extends AppCompatActivity  {

    private static final long SPLASH_DELAY = 786;
    private CountDownTimer timer;
    LinearLayout linearLayout;
    AppPreferences appPrefs;
    String uusername, versionname, disgnid = "0", activityName = "Activities", userType, track_status, track_interval;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String imsiSIM1 = "0";
    String imsiSIM2 = "0";
    TelephonyManager telephonyManager;
    private LocationPrefs locationPrefs;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    ArrayList<TrackingDetail> trackingDetails = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        redirect();
        linearLayout = (LinearLayout) findViewById(R.id.linearsplash);
        appPrefs = new AppPreferences(getApplicationContext());
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        uusername = sharedprefs.getString("s_uuser", "");
        versionname = sharedprefs.getString("version", "");
        userType = sharedprefs.getString("usrtype", "");
        track_status = sharedprefs.getString("track_status", "");
        track_interval = sharedprefs.getString("track_interval", "");
        getMacAddr();
        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
//        try {
//            TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(SplashActivity.this);
//            imsiSIM1 = telephonyInfo.getImsiSIM1();
//            imsiSIM2 = telephonyInfo.getImsiSIM2();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        imsiSIM1 = telephonyManager.getDeviceId();
       // Toast.makeText(this, ""+imsiSIM1, Toast.LENGTH_SHORT).show();
        // refreshLocation();
        locationPrefs = new LocationPrefs(SplashActivity.this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }
        public String getMacAddr() {
            try {
                List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface nif : all) {
                    if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }
                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        //res1.append(Integer.toHexString(b & 0xFF) + ":");
                        res1.append(String.format("%02X:",b));
                    }
                   // Toast.makeText(this, ""+res1, Toast.LENGTH_SHORT).show();

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            } catch (Exception ex) {
            }
            return "";

    }
    private void refreshLocation() {
        ApiHolder loc_att = ServiceConnectionNewURL.getClient().create(ApiHolder.class);
        Call<TrackingResponse> locCall = loc_att.trackingResponse(uusername,imsiSIM1);
        locCall.enqueue(new Callback<TrackingResponse>() {
            public void onResponse(Call<TrackingResponse> call, Response<TrackingResponse> response) {
                TrackingResponse trackingResponse = response.body();
                trackingDetails = response.body().getTrackingDetails();
                try{
                    track_status = trackingDetails.get(0).getTrack_status();
                    track_interval = trackingDetails.get(0).getTrack_interval();
                    editor.putString("track_status", track_status);
                    editor.putString("track_interval", track_interval);
                    editor.commit();
            }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<TrackingResponse> call, Throwable t) {
                //Toast.makeText(JobScheduleService.this, "Username/Password Incorrect", Toast.LENGTH_SHORT).show();
                try {
                } catch (Exception e) {
                }
            }
        });
    }

    private void redirect() {

        if (timer != null) {
            timer.cancel();
        } else {
            timer = new CountDownTimer(SPLASH_DELAY, 200) {
                @Override
                public void onTick(long l) {
                }
                @Override
                public void onFinish() {
                    startActivity();
                }
            }.start();
        }
    }
    private void startActivity() {

                if (appPrefs.isLoggedIn()) {
                        Intent intee = new Intent(SplashActivity.this, MainActivity.class);
                        //Intent intee = new Intent(SplashActivity.this, MainActivityNew.class);
                        intee.putExtra("intent","");
                        startActivity(intee);
                        finish();
                        appPrefs.setLoggedIn(true);
                } else {
                    Intent intee = new Intent(SplashActivity.this, LoginActivityNew.class);
                    startActivity(intee);
                    finish();
                }
    }
    private boolean isNetworkAvailable(SplashActivity splashActivity) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
