package in.eoninfotech.eontechnician.service;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.os.PowerManager;
import android.util.Log;

import com.google.android.gms.location.*;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import in.eoninfotech.eontechnician.GetLocation;
import in.eoninfotech.eontechnician.storage.LocationPrefs;
import in.eoninfotech.eontechnician.helper.EONUtil;
import in.eoninfotech.eontechnician.helper.Location_prop;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.LocationsResponse;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import in.eoninfotech.eontechnician.webservice.TrackingDetail;
import in.eoninfotech.eontechnician.webservice.TrackingResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Math.round;

/**
 * Created by root on 23/5/19.
 */

@SuppressLint("SpecifyJobSchedulerIdRange")
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

public class ForegroundService extends JobService {

    public static final long NOTIFY_INTERVAL = 1 * 1000; // 10 seconds
    private final static long INTERVAL = 1000 * 60; //3 minutes
    private Handler mHandler = new Handler();
    private FusedLocationProviderClient locationProviderClient;
    private int period;
    Handler handler = new Handler();
    private Timer mTimer;
    private LocationPrefs locationPrefs;
    String address;
    String username, version, imei, track_interval, track_status, mac_address;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    double latitude, longitude;
    String lati, lngi;

    Calendar calen = Calendar.getInstance();
    int track;
    PowerManager.WakeLock wakelock;
    Timer timer;
    TimerTask timerTask;
    final Location location;
    ArrayList<TrackingDetail> trackingDetails = new ArrayList<>();
    private Location_prop loc;

    public ForegroundService() {
        location = null;
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    @Override
    public void onCreate() {
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);editor = sharedprefs.edit();
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
//        mTimer.scheduleAtFixedRate(new ForegroundService.TimeDisplayTimerTask(), 0, (INTERVAL * track));
//        startService(new Intent(getBaseContext(), ForegroundService.class));
//        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        locationPrefs = new LocationPrefs(this);
//        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        wakelock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getCanonicalName());
//        wakelock.acquire();
        super.onCreate();
//        Intent notificationIntent = new Intent(this, ForegroundService.class);
//        notificationIntent.setAction(Content.ACTION.MAIN_ACTION);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                notificationIntent, PendingIntent.FLAG_IMMUTABLE);
//        Bitmap icon = BitmapFactory.decodeResource(getResources(),
//                R.drawable.app_icon);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
//            String channelName = "My Background Service";
//            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
//            chan.setLightColor(Color.BLUE);
//            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            assert manager != null;
//            manager.createNotificationChannel(chan);
//
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//            Notification notification = notificationBuilder.setOngoing(true)
//                    .setSmallIcon(R.drawable.app_icon)
//                    .setContentTitle("Getting Location")
//                    .setPriority(NotificationManager.IMPORTANCE_MIN)
//                    .setCategory(Notification.CATEGORY_SERVICE)
//                    .build();
//            startForeground(2, notification);
//        } else {
//            Notification notification = new NotificationCompat.Builder(this)
//                    .setContentTitle("Eon FT")
//                    .setTicker("Location Service")
//                    .setContentText("Getting Location")
//                    .setSmallIcon(R.drawable.app_icon)
//                    .setLargeIcon(
//                            Bitmap.createScaledBitmap(icon, 60, 60, false))
//                    .setContentIntent(pendingIntent)
//                    .setOngoing(true)
//                    .build();
//            startForeground(Content.NOTIFICATION_ID.FOREGROUND_SERVICE,
//                    notification);
//
//        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try{
            String parameter = intent.getStringExtra("param_name");
            if(parameter.equals("end")){
                stopForeground(true);
                stopSelf();
            }else{

            }
        }catch(Exception ex){
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
            super.onDestroy();
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
                final boolean isRunning = EONUtil.isServiceRunning(this.getBaseContext(), ForegroundService.class);
                String running = String.valueOf(isRunning);
                if (isRunning) {
                    loadContent();
                } else {

                }
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
            class TimeDisplayTimerTask extends TimerTask {
                @Override
                public void run() {
                   // getStatus();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
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
                            }else{
                                //getDeviceLocation();
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
        };
