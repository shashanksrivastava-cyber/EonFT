package in.eoninfotech.eontechnician.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;
import in.eoninfotech.eontechnician.BuildConfig;
import in.eoninfotech.eontechnician.CardViewActivity;
import in.eoninfotech.eontechnician.ImageUtils;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.ClientDetails;
import in.eoninfotech.eontechnician.Responses.ClientLocationDetail;
import in.eoninfotech.eontechnician.Responses.ClientLocationResponse;
import in.eoninfotech.eontechnician.Responses.ClientResponse;
import in.eoninfotech.eontechnician.Responses.CollectedItemsResponse;
import in.eoninfotech.eontechnician.Responses.DisconnectionResponse;
import in.eoninfotech.eontechnician.Responses.FaultResponse;
import in.eoninfotech.eontechnician.Responses.LoginDetail;
import in.eoninfotech.eontechnician.Responses.MainResponse;
import in.eoninfotech.eontechnician.Responses.NotAvailActivityResponse;
import in.eoninfotech.eontechnician.Responses.PaymentMethodResponse;
import in.eoninfotech.eontechnician.Responses.RemovalActivityResponse;
import in.eoninfotech.eontechnician.Responses.RemovalResponse;
import in.eoninfotech.eontechnician.Responses.ReplaceReason;
import in.eoninfotech.eontechnician.Responses.SimOperatorResponse;
import in.eoninfotech.eontechnician.Responses.SimReplaceResponse;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.Responses.VTSResponse;
import in.eoninfotech.eontechnician.Responses.VehNotAvailReasonResponse;
import in.eoninfotech.eontechnician.Responses.VehicleTypeResponse;
import in.eoninfotech.eontechnician.Responses.WorkTypeResponse;
import in.eoninfotech.eontechnician.Service.JobScheduleService;
import in.eoninfotech.eontechnician.Service.LocationService;
import in.eoninfotech.eontechnician.SplashActivity;
import in.eoninfotech.eontechnician.Storage.LocationPrefs;
import in.eoninfotech.eontechnician.activity.SendDatatoServer;
import in.eoninfotech.eontechnician.callbacks.ClientListener;
import in.eoninfotech.eontechnician.controllers.NewInstallmentController;
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.helper.GetLocations;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.helper.Location_prop;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.AttResponse;
import in.eoninfotech.eontechnician.webservice.LocationsResponse;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


public class ActivityLogFragment extends Fragment implements ClientListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback{
    View v;
    String username,fullUserName, dist_id, version,imei,imei1,e_remarks,clientId,s_clientname="SELECT CLIENT";
    String current_date, s_update_date, s_time, address, s_log_status = "IN",months,clientLocId;
    int year,month, day;
    Dialog myDialog;
    ImageView txtclose;
    EditText etMasterPass;
    Button btCancel,btSubmit;
    private final static int REQUEST_CHECK_SETTINGS = 2000;
    Calendar calen = Calendar.getInstance();
    int REQUEST_CODE_PERMISSION = 10;
    //boolean isPermissionGranted = true;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_REQUEST = 1000;
    EditText remarks;
    int locTry = 0;
    TextView t_techname,t_address,timer,date,time;
    Button btn,refresh;
    ProgressDialog pDialog;
    double latitude, longitude;
    String lati,lngi;
    private Location_prop loc;
    private RadioGroup radioGroup;
    private RadioButton r_in, r_out;
    String versionName;
    CircleImageView ivProfile;
    String customAddress;
    File f;
    ProgressBar progressBar;
    MaterialCalendarView mcv;
    String image;
    Calendar c ;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    private LocationPrefs locationPrefs;
    MySearchableSpinner client,location;
    NewInstallmentController newInstallmentController;
    ArrayList<ClientDetails> clientList = new ArrayList<>();
    ArrayList<String> clientDetail = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayList<ClientLocationDetail> locationList = new ArrayList<>();
    ArrayList<String> locationDetail = new ArrayList<>();
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,};
    // integer for permissions results request

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_attendence, container, false);

        sharedprefs = this.getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        fullUserName = sharedprefs.getString("dis_user","");
        image = sharedprefs.getString("image","");
        imei = sharedprefs.getString("imei1","");
        imei1 = sharedprefs.getString("imei2","");
        versionName  = BuildConfig.VERSION_NAME;

        ivProfile = v.findViewById(R.id.ivProfile);
        t_techname = v.findViewById(R.id.attd_username);
        t_address = v.findViewById(R.id.att_address);
        t_techname.setText(fullUserName.toUpperCase());
        btn = v.findViewById(R.id.update_data);
        refresh = v.findViewById(R.id.refresh);
        radioGroup = v.findViewById(R.id.radiogroup);
        progressBar = v.findViewById(R.id.progressBar);
        timer = v.findViewById(R.id.timer);
        locationPrefs = new LocationPrefs(getActivity());
        remarks =v.findViewById(R.id.remarks);
        date  = v.findViewById(R.id.date);
        time = v.findViewById(R.id.time);
        client =  v.findViewById(R.id.new_in_clients);
        location =   v.findViewById(R.id.new_in_locations);
        myDialog = new Dialog(getActivity());
        newInstallmentController = new NewInstallmentController();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        c= Calendar.getInstance();
        addclients();
        addLocation();
        location.setEnabled(false);
        String imageUri = K.Url.IMAGE_URL +"uploads/"+image;
        ImageUtils.glideImage(ivProfile, imageUri, R.drawable.user);
        btn.setEnabled(false);
        btn.setBackgroundColor(getResources().getColor(R.color.grey500));

        CountDownTimer newtimer = new CountDownTimer(1000000000, 1000) {

            public void onTick(long millisUntilFinished) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                s_time = dateFormat.format(calen.getTime());
                year = calen.get(Calendar.YEAR);
                month = calen.get(Calendar.MONTH);
                day = calen.get(Calendar.DAY_OF_MONTH);
                month = month + 1;
                if(month==1){
                    months = "Jan";
                }else if(month==2){
                    months = "Feb";
                }else if(month==3){
                    months = "Mar";
                }else if(month==4){
                    months = "Apr";
                }else if(month==5){
                    months = "May";
                }else if(month==6){
                    months = "Jun";
                }else if(month==7){
                    months = "Jul";
                }else if(month==8){
                    months = "Aug";
                }else if(month==9){
                    months = "Sep";
                }else if(month==10){
                    months = "Oct";
                }else if(month==11){
                    months = "Nov";
                }else if(month==12){
                    months = "Dec";
                }
                current_date = months + " "  +day + "," + year;
                date.setText(current_date);
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                int seconds = c.get(Calendar.SECOND);
                int minutes = 0;
                if (minute < 10) {
                    minutes  = Integer.parseInt(""+"0"+minute);
                } else {
                    minutes = minute;
                }
                time.setText(hour+":"+minutes+":"+seconds);
            }
            public void onFinish() {
            }
        };
        newtimer.start();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        s_time = dateFormat.format(calen.getTime());
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);
        month = month + 1;

        if (month + 1 < 10) {
            current_date = day + "-0" + +month + "-" + year;
        } else {
            current_date = day + "-" + month + "-" + year;
        }
         s_update_date = current_date;
        setDateAndTime();
        ShowProgressBar(false);
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        } catch (NoSuchMethodError nsme) {
            nsme.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }
                clientId = String.valueOf(clientList.get(i).getClient_Id());
                s_clientname = clientList.get(i).getClient_Name();
                if(clientId.equals("500")){
                    location.setEnabled(false);
                }else {
                    location.setEnabled(true);
                }
                addLocation();
            }catch(Exception e){
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    i = i - 1;
                }clientLocId = String.valueOf((locationList.get(i).getLoc_Id()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        onGps();
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if((locTry>=2)&&(gps_enabled)) {
                    myDialog.setContentView(R.layout.add_address);
                    txtclose = myDialog.findViewById(R.id.error);
                    etMasterPass = myDialog.findViewById(R.id.etMasterPass);
                    btCancel = myDialog.findViewById(R.id.btCancel);
                    btSubmit = myDialog.findViewById(R.id.btSubmit);
                    txtclose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myDialog.dismiss();
                        }
                    });
                    btCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           etMasterPass.setText("");
                        }
                    });
                    btSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            customAddress = etMasterPass.getText().toString();
                            t_address.setText(customAddress);
                            address = customAddress;
                            latitude= Double.parseDouble("0");
                            longitude= Double.parseDouble("0");
                            btn.setEnabled(true);
                            btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            myDialog.dismiss();
                        }
                    });
                    myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.show();
                    }else {
                        try {
                        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    } catch (Exception ex) {
                    }
                    try {
                        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    } catch (Exception ex) {
                    }
                    if (!gps_enabled && !network_enabled) {
                        // notify user
                        new AlertDialog.Builder(getActivity())
                                .setMessage("There is problem fetching your location. Please turn on your GPS.")
                                .setPositiveButton("Enable Location", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                        getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    }
                                }).setNegativeButton("Cancel", null)
                                .show();
                    } else {
                        getAddress();
                    }
                    }
                    try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
                    }else{
                    }
                   } catch (Exception e) {
                    e.printStackTrace();
                  }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (s_clientname.equalsIgnoreCase("SELECT Client") || (s_clientname.equals(null))) {
                    Toast.makeText(getContext(), "Please Select Client", Toast.LENGTH_LONG).show();
                }else if(s_clientname.equalsIgnoreCase("Others") && (location.getSelectedItem().toString().equalsIgnoreCase("Select Location"))){
                    clientLocId = "0";
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    if (selectedId == R.id.l_in) {
                        s_log_status = "IN";
                    } else if (selectedId == R.id.l_out) {
                        s_log_status = "OUT";
                    }
                    e_remarks = remarks.getText().toString();
                    sendData();

                }else if (location.getSelectedItem().toString().equalsIgnoreCase("Select Location")) {
                    Toast.makeText(getContext(), "Please Select Client Location", Toast.LENGTH_LONG).show();
                } else {
                    //   s_update_date = t_date.getText().toString();
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    if (selectedId == R.id.l_in) {
                        s_log_status = "IN";
                    } else if (selectedId == R.id.l_out) {
                        s_log_status = "OUT";
                    }
                    e_remarks = remarks.getText().toString();
                    sendData();
                }
            }
        });
        return v;
    }
    private void getLocation() {
            try {
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
    }
    private boolean checkPlayServices() {
            GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
            int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(getActivity());
            if (resultCode != ConnectionResult.SUCCESS) {
                if (googleApiAvailability.isUserResolvableError(resultCode)) {
                    googleApiAvailability.getErrorDialog(getActivity(),resultCode,
                            PLAY_SERVICES_REQUEST).show();
                } else {
                    Toast.makeText(getActivity(),
                            "This device is not supported.", Toast.LENGTH_LONG)
                            .show();
                }
                return false;
            }
            return true;
        }
    private void sendData() {
        ShowProgressBar(true);
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<AttResponse> call = log_att.log_attendance(username, s_time, s_update_date, String.valueOf(latitude), String.valueOf(longitude), address, s_log_status, versionName, e_remarks,clientId,clientLocId);
        Log.i("****call", String.valueOf(call));
        call.enqueue(new Callback<AttResponse>() {
            @Override
            public void onResponse(Call<AttResponse> call, Response<AttResponse> response) {
                AttResponse updateDataResponse = response.body();
                Log.i("**respnse", " " + response.body());
                Log.i("**respnse", " " + response.body().getType());

                    if (updateDataResponse.getType() == 1) {
                        Toast.makeText(getActivity(), ""+updateDataResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        addclients();
                        addLocation();
                        remarks.setText("");
                        t_address.setText("No address");
                        locTry=0;
                        ShowProgressBar(false);
                    }
                btn.setClickable(true);
                ShowProgressBar(false);
            }
            @Override
            public void onFailure(Call<AttResponse> call, Throwable t) {
                t.printStackTrace();
                btn.setClickable(true);
                ShowProgressBar(false);
                Toast.makeText(getActivity(), "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addLocation() {

        newInstallmentController.reqeuestClientLocation(clientId,this);
    }

    private void addclients() {
        newInstallmentController.reqeuestClientList(this);
    }

    public Address getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void getAddress() {

        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }if (!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(getActivity())
                    .setMessage("There is problem fetching your location. Please turn on your GPS.")
                    .setPositiveButton("Enable Location", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("Cancel", null)
                    .show();
        } else {
            int DELAY = 1000;
            ShowProgressBar(true);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        loc = GetLocations.getCurrentLocation(getActivity());
                        latitude = loc.latitude;
                        longitude = loc.longitude;
                        lati = String.valueOf(latitude);
                        lngi = String.valueOf(longitude);

                        if (lati.equals("0.0") && (lngi.equals("0.0"))) {
                                locTry = locTry+1;
                                ShowProgressBar(false);
                        }
                        else {
                            locationPrefs.setLastKnownLat(latitude);
                            locationPrefs.setLastKnownLng(longitude);
                            Double lat = (locationPrefs.getLastKnownLat());
                            Double lng = (locationPrefs.getlastKownLng());
                            Geocoder geocoder;
                            geocoder = new Geocoder(getActivity(), Locale.getDefault());
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
                                t_address.setText(address);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (t_address.getText().toString().equalsIgnoreCase("No Address")) {
                                btn.setEnabled(false);
                            } else {
                                btn.setEnabled(true);
                                btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // This method will be executed once the timer is over
                                    if (isAdded()) {
                                        btn.setEnabled(false);
                                        btn.setBackgroundColor(getResources().getColor(R.color.grey500));
                                        t_address.setText("No Address");
                                    }
                                }
                            }, 60000);
                            ShowProgressBar(false);
                        }
                    }catch(Exception e){
                            e.printStackTrace();
                        }
                }
            }, DELAY);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    default:
                        break;
                }
                break;
        }
    }
    void setDateAndTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        s_time = dateFormat.format(calen.getTime());
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        if (month + 1 < 10) {
            current_date = day + "-0" + +month + "-" + year;
        } else {
            current_date = day + "-" + month + "-" + year;
        }
    }
    void onGps() {
        if (Build.VERSION.SDK_INT >= K.Url.chkversion)
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // permission wasn't granted
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
                }
            }
    }
    public static boolean isMockSettingsON(Context context) {
        return !Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
    }

    public static boolean areThereMockPermissionApps(Context context) {
        int count = 0;
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : packages) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName,
                        PackageManager.GET_PERMISSIONS);

                // Get Permissions
                String[] requestedPermissions = packageInfo.requestedPermissions;

                if (requestedPermissions != null) {
                    for (int i = 0; i < requestedPermissions.length; i++) {
                        if (requestedPermissions[i]
                                .equals("android.permission.ACCESS_MOCK_LOCATION")
                                && !applicationInfo.packageName.equals(context.getPackageName())) {
                            count++;
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("Got exception ", e.getMessage());
            }
        }
        return count > 0;
    }

    private void ShowProgressBar(boolean show) {
        try {
            if (show) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }}
        catch (Exception e) {
            e.printStackTrace();
        }}

    @Override
    public void clientResponse(ClientResponse response) {
        try{
            clientList = response.getClientList();
            try {
                try {
                    clientDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }   clientDetail.add(" SELECT CLIENT");
                for (int i = 0; i < clientList.size(); i++) {
                    clientDetail.add(clientList.get(i).getClient_Name());
                }
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item, clientDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                client.setAdapter(adapter);
                ShowProgressBar(false);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void locationResponse(ClientLocationResponse response) {
        try{
            locationList = response.getClientLoc();
            try {
                try {
                    locationDetail.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }locationDetail.add("SELECT LOCATION");
                for (int i = 0; i < locationList.size(); i++) {
                    locationDetail.add(locationList.get(i).getLoc_Name());
                }adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_custom_spinner_item,locationDetail);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                location.setAdapter(adapter);
                ShowProgressBar(false);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void workTypeResponse(WorkTypeResponse response) {

    }

    @Override
    public void vehicleTypeResponse(VehicleTypeResponse response) {

    }

    @Override
    public void faultListResponse(FaultResponse response) {

    }

    @Override
    public void replaceResponse(ReplaceReason response) {

    }

    @Override
    public void disconnectionResponse(DisconnectionResponse response) {

    }

    @Override
    public void removalActivityResponse(RemovalActivityResponse response) {

    }

    @Override
    public void removalResponse(RemovalResponse response) {

    }

    @Override
    public void damageResponse(RemovalResponse response) {

    }

    @Override
    public void collectItemResponse(CollectedItemsResponse response) {

    }

    @Override
    public void simOperatorResponse(SimOperatorResponse response) {

    }

    @Override
    public void simReplaceReason(SimReplaceResponse response) {

    }

    @Override
    public void notAvailActivity(NotAvailActivityResponse response) {

    }

    @Override
    public void vehicleNotAvailReason(VehNotAvailReasonResponse response) {

    }

    @Override
    public void vtsResponses(VTSResponse response) {

    }

    @Override
    public void vtsResponse(VTSResponse response) {

    }

    @Override
    public void pMethod(PaymentMethodResponse response) {

    }

    @Override
    public void updateDataResponse(MainResponse response) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
            // notify user
            Toast.makeText(getActivity(), "There is problem fetching your location.Please turn on your GPS.", Toast.LENGTH_SHORT).show();
        }else{

            }
    }
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
