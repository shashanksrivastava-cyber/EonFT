package in.eoninfotech.eontechnician.Service;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import in.eoninfotech.eontechnician.AlarmReceiver;
import in.eoninfotech.eontechnician.GetLocation;

import in.eoninfotech.eontechnician.Storage.LocationPrefs;

import in.eoninfotech.eontechnician.helper.Location_prop;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.LocationsResponse;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import in.eoninfotech.eontechnician.webservice.TrackingDetail;
import in.eoninfotech.eontechnician.webservice.TrackingResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Android on 02-01-2018.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

public class JobScheduleService extends JobService {

    public static final long NOTIFY_INTERVAL = 10 * 1000;
    private final static long INTERVAL = 1000 * 60;
    private Handler mHandler = new Handler();
    private FusedLocationProviderClient locationProviderClient;
    private Timer mTimer = null;
    private LocationPrefs locationPrefs;
    String address;
    String username, version, imei, track_interval, track_status,mac_address;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    double latitude, longitude;
    String lati, lngi;
    Calendar calen = Calendar.getInstance();
    int track=1;
    final Location location;
    ArrayList<TrackingDetail> trackingDetails = new ArrayList<>();
    private Location_prop loc;
    public JobScheduleService() {
        location = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
    @Override
    public void onCreate() {

            sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
            editor = sharedprefs.edit();
            username = sharedprefs.getString("s_uuser", "");
            imei = sharedprefs.getString("imei1", "");
            track_status = sharedprefs.getString("track_status", "");
            track_interval = sharedprefs.getString("track_interval", "");
            mac_address = sharedprefs.getString("MacAddress","");
            track_interval = "3";
            track = Integer.parseInt(track_interval);
            if (track_interval.equalsIgnoreCase("false")) {
                track = 1;
                if (mTimer != null) {
                    mTimer.cancel();
                } else {
                    mTimer = new Timer();
                }
                mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0,(INTERVAL * track));
                startService(new Intent(getBaseContext(), JobScheduleService.class));
                locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                locationPrefs = new LocationPrefs(this);
                super.onCreate();
            } else {
                track = Integer.parseInt(track_interval);
                if (mTimer != null) {
                    mTimer.cancel();
                } else {
                    mTimer = new Timer();
                }
                mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0,(INTERVAL * track));
            }
            startService(new Intent(getBaseContext(), JobScheduleService.class));
            locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            locationPrefs = new LocationPrefs(this);
            super.onCreate();
    }
    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        return false;
    }

    @Override
    public void onDestroy() {
       // super.onDestroy();
        // TODO Auto-generated method stub
        if (track_status.equalsIgnoreCase("false") || track_status.equalsIgnoreCase("n")) {
            mTimer.cancel();
            super.onDestroy();
        } else {
            mTimer.cancel();
            super.onDestroy();
        }
    }
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {

        super.onTaskRemoved(rootIntent);
    }
    private void getDeviceLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task locationTask = locationProviderClient.getLastLocation();
        locationTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                try {
                    loc = GetLocation.getCurrentLocation(this);
                    latitude = loc.latitude;
                    longitude = loc.longitude;

                    locationPrefs.setLastKnownLat(latitude);
                    locationPrefs.setLastKnownLng(longitude);
                     } catch (Exception e) {
                    // Log.d(TAG, "getDeviceLocation: " + e.getMessage());
                }
            }
        });
    }
    class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            refreshLocation();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (track_status.equalsIgnoreCase("false") || track_status.equalsIgnoreCase("n")) {
                    } else {
                        try{
                        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                        boolean gps_enabled = false;
                        boolean network_enabled = false;
                        try {
                            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        } catch (Exception ex) {
                        }try {
                            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                        } catch (Exception ex) {
                        }if (!gps_enabled && !network_enabled) {
                        } else {
                            getDeviceLocation();
                                lati = String.valueOf((locationPrefs.getLastKnownLat()));
                                lngi = String.valueOf((locationPrefs.getlastKownLng()));

                                Double lat = Double.valueOf((lati));
                                Double lng = Double.valueOf((lngi));

                                Geocoder geocoder;
                                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                            try {
                                String addresses = String.valueOf(geocoder.getFromLocation(lat, lng, 1));
                                String[] separated = addresses.split("=");
                                String abc = separated[1];
                                String[] separate = abc.split(":");
                                String def = separate[1];
                                String[] separat = def.split("]");
                                String addresss = separat[0];
                                String addressss = addresss.replaceAll("^\"|\"$", "");
                                Log.e("", "address" + address);
                                address = addressss;
                                locationPrefs.setLastLoc(address);
                                    loadContent();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
    private void loadContent() {
        String curAdd = locationPrefs.getlastLoc().toString();
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<LocationsResponse> call = log_att.locationResponse(username,curAdd,imei,lati,lngi,mac_address);
        call.enqueue(new Callback<LocationsResponse>() {
            @Override
            public void onResponse(Call<LocationsResponse> call, Response<LocationsResponse> response) {
                try{
                if (response.body().getType() == 1) {
                } else {
                }
            }catch (Exception e){
                  e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<LocationsResponse> call, Throwable t) {
            }
        });
    }
    private void refreshLocation(){

        ApiHolder loc_att = ServiceConnectionNewURL.getClient().create(ApiHolder.class);
        Call<TrackingResponse> locCall = loc_att.trackingResponse(username,imei);
        locCall.enqueue(new Callback<TrackingResponse>() {
            public void onResponse(Call<TrackingResponse> call, Response<TrackingResponse> response) {
                try {
                    if (response.body().getType().equals("0")) {
                        TrackingResponse trackingResponse = response.body();
                        track_status = sharedprefs.getString("track_status", "");
                        track_interval = sharedprefs.getString("track_interval", "");
                        track_interval="20";
                        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                        boolean gps_enabled = false;
                        boolean network_enabled = false;
                        try {
                            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        } catch (Exception ex) {
                        }
                        try {
                            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                        } catch (Exception ex) {
                        }
                        if (!gps_enabled && !network_enabled) {
                        } else {
                            if (track_status.equalsIgnoreCase("false") || track_status.equalsIgnoreCase("n")) {
                            } else {
                                getDeviceLocation();
                            }
                        }
                    } else{
                        TrackingResponse trackingResponse = response.body();
                        trackingDetails = response.body().getTrackingDetails();
                        track_status = trackingDetails.get(0).getTrack_status();
                        track_interval = trackingDetails.get(0).getTrack_interval();
                        editor.putString("track_status", track_status);
                        editor.putString("track_interval", track_interval);
                        editor.commit();
                        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
                        editor = sharedprefs.edit();
                        track_status = sharedprefs.getString("track_status", "");
                        track_interval = sharedprefs.getString("track_interval", "");
                        //track_interval="20";
                        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                        boolean gps_enabled = false;
                        boolean network_enabled = false;
                        try {
                            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        } catch (Exception ex) {
                        }
                        try {
                            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                        } catch (Exception ex) {
                        }
                        if (!gps_enabled && !network_enabled) {
                        } else {
                            if (track_status.equalsIgnoreCase("false") || track_status.equalsIgnoreCase("n")) {
                            } else {
                                getDeviceLocation();
                            }
                        }
                    }
                }catch (Exception e){
                }
            }
            @Override
            public void onFailure(Call<TrackingResponse> call, Throwable t) {
                try {
                } catch (Exception e) {
                }
            }
        });
    }
}

