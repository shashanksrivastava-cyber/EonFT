package in.eoninfotech.eontechnician.Service;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
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
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
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

import in.eoninfotech.eontechnician.BuildConfig;
import in.eoninfotech.eontechnician.Storage.LocationPrefs;
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

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

public class LocationService extends JobService {

    public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds
    private final static long INTERVAL = 1000*60; //3 minutes
    private Handler mHandler = new Handler();
    private FusedLocationProviderClient locationProviderClient;
    private Timer mTimer = null;
    private LocationPrefs locationPrefs;
    String address;
    String username,fullUserName,version,imei,track_interval,track_status,mac_address;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    String lati,lngi;
    int hour, min;
    Calendar calen = Calendar.getInstance();
    int track;
    Intent intent;
    int startTime = 8 ,endTime = 20;
    ArrayList<TrackingDetail> trackingDetails = new ArrayList<>();
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
        mac_address = sharedprefs.getString("macAddress","");
        if (track_interval.equalsIgnoreCase("false")) {
            track = 20;
            if (mTimer != null) {
                mTimer.cancel();
            } else {
                mTimer = new Timer();
            }
            mTimer.scheduleAtFixedRate(new LocationService.TimeDisplayTimerTask(), 0, (INTERVAL * track));
            startService(new Intent(getBaseContext(), LocationService.class));
            locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            locationPrefs = new LocationPrefs(this);
            super.onCreate();
            HandlerThread serviceThread = new HandlerThread("service_thread",
                    android.os.Process.THREAD_PRIORITY_BACKGROUND);
            serviceThread.start();
        } else {
            track = Integer.parseInt(track_interval);
            if (mTimer != null) {
                mTimer.cancel();
            } else {
                mTimer = new Timer();
            }
            mTimer.scheduleAtFixedRate(new LocationService.TimeDisplayTimerTask(), 0, (INTERVAL * track));
        }
        startService(new Intent(getBaseContext(), LocationService.class));
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationPrefs = new LocationPrefs(this);
        super.onCreate();
        HandlerThread serviceThread = new HandlerThread("service_thread",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        serviceThread.start();
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

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
        } else{
        }
        return false;
    }
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
    @Override
    public void onDestroy() {
            // TODO Auto-generated method stub
            if(track_status.equalsIgnoreCase("false") ||track_status.equalsIgnoreCase("n")){
                mTimer.cancel();
                super.onDestroy();
            }else{
                super.onDestroy();
//            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//            alarmManager.set(alarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000*60*60),
//                    PendingIntent.getService(this,0,new Intent(this,JobScheduleService.class),0));
                startService(new Intent(getBaseContext(), LocationService.class));
            }
        }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        imei = sharedprefs.getString("imei1", "");
        track_status = sharedprefs.getString("track_status", "");
        track_interval = sharedprefs.getString("track_interval", "");
        mac_address = sharedprefs.getString("macAddress","");
        alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), WakeupReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        sendBroadcast(intent);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 05);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                (INTERVAL*track), alarmIntent);
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
        }if (!gps_enabled && !network_enabled) {
        } else {
            refreshLocation();
        }
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
                    final Location location = (Location) task.getResult();
                    lati = String.valueOf(locationPrefs.setLastKnownLat(location.getLatitude()));
                     lngi = String.valueOf(locationPrefs.setLastKnownLng(location.getLongitude()));

                  //  Toast.makeText(this, ""+lat+lng, Toast.LENGTH_SHORT).show();
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

                                lati = String.valueOf((locationPrefs.getLastKnownLat()));
                                lngi = String.valueOf((locationPrefs.getlastKownLng()));

                                Double lat = (locationPrefs.getLastKnownLat());
                                Double lng = (locationPrefs.getlastKownLng());

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

                                    Calendar c = Calendar.getInstance();
                                    int hour = c.get(Calendar.HOUR_OF_DAY);
                                    if((hour>=startTime)&&(hour<endTime)){
                                       // loadContent();
                                    }else{

                                    }
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

    private void refreshLocation() {

    }

}

