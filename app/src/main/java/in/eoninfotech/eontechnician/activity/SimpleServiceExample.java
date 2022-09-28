package in.eoninfotech.eontechnician.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

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
import in.eoninfotech.eontechnician.GetLocation;
import in.eoninfotech.eontechnician.Service.JobScheduleService;
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
 * Created by androidpc on 31/5/19.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

public class SimpleServiceExample extends JobService {

    public static final long NOTIFY_INTERVAL = 10 * 1000;
    private final static long INTERVAL = 1000 * 60 * 3;
    private Handler mHandler = new Handler();
    private FusedLocationProviderClient locationProviderClient;
    private Timer mTimer = null;
    double latitude, longitude;
    private LocationPrefs locationPrefs;
    int track;
    String address;
    String lati, lngi;
    ArrayList<TrackingDetail> trackingDetails = new ArrayList<>();
    private Location_prop loc;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String username, version, imei, track_interval, track_status, mac_address;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            String parameter = intent.getStringExtra("param_name");
            if (parameter.equals("end")) {
                stopSelf();
            }
        } catch (Exception ex) {
        }
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
        mac_address = sharedprefs.getString("MacAddress", "");
        track = Integer.parseInt(track_interval);
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            mTimer = new Timer();
        }
        mTimer.scheduleAtFixedRate(new SimpleServiceExample.TimeDisplayTimerTask(), 0, (INTERVAL));
        startService(new Intent(getBaseContext(), SimpleServiceExample.class));
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
        super.onDestroy();
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        PendingIntent restartServicePendingIntent = PendingIntent
                .getService(getApplicationContext(), 1, restartServiceIntent,
                        PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()
                + 100, restartServicePendingIntent);
        super.onTaskRemoved(rootIntent);
    }

    public class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    getDeviceLocation();
                    getStatus();
                }
            });
        }
    }

    private void getStatus() {
        ApiHolder loc_att = ServiceConnectionNewURL.getClient().create(ApiHolder.class);
        Call<TrackingResponse> locCall = loc_att.trackingResponse(username, imei);
        locCall.enqueue(new Callback<TrackingResponse>() {
            public void onResponse(Call<TrackingResponse> call, Response<TrackingResponse> response) {
                try {
                    if (response.body().getType() == 1) {
                        TrackingResponse trackingResponse = response.body();
                        trackingDetails = response.body().getTrackingDetails();
                        track_status = trackingDetails.get(0).getTrack_status();
                        track_interval = trackingDetails.get(0).getTrack_interval();
                        editor.putString("track_status", track_status);
                        editor.putString("track_interval", track_interval);
                        editor.commit();
                        track = Integer.parseInt(track_interval);
                        if (track_status.equalsIgnoreCase("y")) {
                            getDeviceLocation();
                        }
                    }
                } catch (Exception e) {
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
                    getAddress();
                } catch (Exception e) {
                }
            }
        });
    }

    private void getAddress() {

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
            latitude = loc.latitude;
            longitude = loc.longitude;

            lati = String.valueOf(latitude);
            lngi = String.valueOf(longitude);

            Double lat = Double.valueOf((latitude));
            Double lng = Double.valueOf((longitude));
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
    }

    private void loadContent() {
        String curAdd = locationPrefs.getlastLoc().toString();
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<LocationsResponse> call = log_att.locationResponse(username, curAdd, imei, lati, lngi, mac_address);
        call.enqueue(new Callback<LocationsResponse>() {
            @Override
            public void onResponse(Call<LocationsResponse> call, Response<LocationsResponse> response) {
                try {
                    if (response.body().getType() == 1) {
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LocationsResponse> call, Throwable t) {
            }
        });
    }
}
