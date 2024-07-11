package in.eoninfotech.eontechnician.service;


import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * Created by Android on 02-01-2018.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

public class LocationService {

//    public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds
//    private final static long INTERVAL = 1000*60; //3 minutes
//    private Handler mHandler = new Handler();
//    private FusedLocationProviderClient locationProviderClient;
//    private Timer mTimer = null;
//    private LocationPrefs locationPrefs;
//    String address;
//    String username,imei,track_interval,track_status,mac_address;
//    SharedPreferences sharedprefs;
//    SharedPreferences.Editor editor;
//    private AlarmManager alarmMgr;
//    private PendingIntent alarmIntent;
//    String lati,lngi;
//
//    int hour, min;
//    Calendar calen = Calendar.getInstance();
//    int track;
//    Intent intent;
//    int startTime = 8 ,endTime = 20;
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return START_STICKY;
//    }
//
//    @Override
//    public void onCreate() {
//        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
//        editor = sharedprefs.edit();
//        username = sharedprefs.getString("s_uuser", "");
//        imei = sharedprefs.getString("imei1", "");
//        track_status = sharedprefs.getString("track_status", "");
//        track_interval = sharedprefs.getString("track_interval", "");
//        mac_address = sharedprefs.getString("macAddress","");
//        if (track_interval.equalsIgnoreCase("false")) {
//            track = 20;
//            if (mTimer != null) {
//                mTimer.cancel();
//            } else {
//                mTimer = new Timer();
//            }
//            mTimer.scheduleAtFixedRate(new LocationService.TimeDisplayTimerTask(), 0, (INTERVAL * track));
//            startService(new Intent(getBaseContext(), LocationService.class));
//            locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//            locationPrefs = new LocationPrefs(this);
//            super.onCreate();
//            HandlerThread serviceThread = new HandlerThread("service_thread",
//                    android.os.Process.THREAD_PRIORITY_BACKGROUND);
//            serviceThread.start();
//        } else {
//            track = Integer.parseInt(track_interval);
//            if (mTimer != null) {
//                mTimer.cancel();
//            } else {
//                mTimer = new Timer();
//            }
//            mTimer.scheduleAtFixedRate(new LocationService.TimeDisplayTimerTask(), 0, (INTERVAL * track));
//        }
//        startService(new Intent(getBaseContext(), LocationService.class));
//        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        locationPrefs = new LocationPrefs(this);
//        super.onCreate();
//        HandlerThread serviceThread = new HandlerThread("service_thread",
//                android.os.Process.THREAD_PRIORITY_BACKGROUND);
//        serviceThread.start();
//    }
//
//    @Override
//    public boolean onStartJob(JobParameters jobParameters) {
//
//        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//        boolean gps_enabled = false;
//        boolean network_enabled = false;
//        try {
//            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        } catch (Exception ex) {
//        }try {
//            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        } catch (Exception ex) {
//        }if (!gps_enabled && !network_enabled) {
//        } else{
//        }
//        return false;
//    }
//    @Override
//    public boolean onStopJob(JobParameters jobParameters) {
//        return false;
//    }
//    @Override
//    public void onDestroy() {
//            // TODO Auto-generated method stub
//            if(track_status.equalsIgnoreCase("false") ||track_status.equalsIgnoreCase("n")){
//                mTimer.cancel();
//                super.onDestroy();
//            }else{
//                super.onDestroy();
//                startService(new Intent(getBaseContext(), LocationService.class));
//            }
//        }
//
//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//
//        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
//        editor = sharedprefs.edit();
//        username = sharedprefs.getString("s_uuser", "");
//        imei = sharedprefs.getString("imei1", "");
//        track_status = sharedprefs.getString("track_status", "");
//        track_interval = sharedprefs.getString("track_interval", "");
//        mac_address = sharedprefs.getString("macAddress","");
//        alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(getApplicationContext(), WakeupReceiver.class);
//        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
//        sendBroadcast(intent);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 11);
//        calendar.set(Calendar.MINUTE, 05);
//        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                (INTERVAL*track), alarmIntent);
//        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//
//        boolean gps_enabled = false;
//        boolean network_enabled = false;
//        try {
//            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        } catch (Exception ex) {
//        }
//        try {
//            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        } catch (Exception ex) {
//        }if (!gps_enabled && !network_enabled) {
//        } else {
//            refreshLocation();
//        }
//    }
//
//    private void getDeviceLocation() {
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Task locationTask = locationProviderClient.getLastLocation();
//        locationTask.addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                try {
//                    final Location location = (Location) task.getResult();
//                    lati = String.valueOf(locationPrefs.setLastKnownLat(location.getLatitude()));
//                     lngi = String.valueOf(locationPrefs.setLastKnownLng(location.getLongitude()));
//
//                  //  Toast.makeText(this, ""+lat+lng, Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    // Log.d(TAG, "getDeviceLocation: " + e.getMessage());
//                }
//            }
//        });
//    }
//
//    class TimeDisplayTimerTask extends TimerTask {
//        @Override
//        public void run() {
//            refreshLocation();
//
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (track_status.equalsIgnoreCase("false") || track_status.equalsIgnoreCase("n")) {
//
//                    } else {
//                        try{
//                            LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//                            boolean gps_enabled = false;
//                            boolean network_enabled = false;
//                            try {
//                                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//                            } catch (Exception ex) {
//                            }try {
//                                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//                            } catch (Exception ex) {
//                            }if (!gps_enabled && !network_enabled) {
//                            } else {
//
//                                lati = String.valueOf((locationPrefs.getLastKnownLat()));
//                                lngi = String.valueOf((locationPrefs.getlastKownLng()));
//
//                                Double lat = (locationPrefs.getLastKnownLat());
//                                Double lng = (locationPrefs.getlastKownLng());
//
//                                Geocoder geocoder;
//                                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//
//                                try {
//                                    String addresses = String.valueOf(geocoder.getFromLocation(lat, lng, 1));
//                                    String[] separated = addresses.split("=");
//                                    String abc = separated[1];
//                                    String[] separate = abc.split(":");
//                                    String def = separate[1];
//                                    String[] separat = def.split("]");
//                                    String addresss = separat[0];
//                                    String addressss = addresss.replaceAll("^\"|\"$", "");
//                                    Log.e("", "address" + address);
//                                    address = addressss;
//                                    locationPrefs.setLastLoc(address);
//
//                                    Calendar c = Calendar.getInstance();
//                                    int hour = c.get(Calendar.HOUR_OF_DAY);
//                                    if((hour>=startTime)&&(hour<endTime)){
//                                       // loadContent();
//                                    }else{
//
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
//        }
//    }
//
//    private void refreshLocation() {
//
//    }

}

