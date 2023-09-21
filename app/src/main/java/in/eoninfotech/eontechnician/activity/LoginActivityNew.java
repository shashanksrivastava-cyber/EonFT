package in.eoninfotech.eontechnician.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.AppPreferences;
import in.eoninfotech.eontechnician.BuildConfig;
import in.eoninfotech.eontechnician.GetLocation;
import in.eoninfotech.eontechnician.MainActivity;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.LoginDetail;
import in.eoninfotech.eontechnician.Responses.LoginResponse;
import in.eoninfotech.eontechnician.Service.ForegroundService;

import in.eoninfotech.eontechnician.Storage.LocationPrefs;
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.helper.EONUtil;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.helper.TelephonyInfo;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by root on 2/11/18.
 */

public class LoginActivityNew extends AppCompatActivity {

    Button loginn;
    TextView phn, email;
    EditText e_usrnme, e_paswrd;
    TextInputLayout user,pass;
    String p_usr = "", p_pass = "", pp_usrnmae = "", pp_passwrd = "",user_id;
    int REQUEST_CODE_PERMISSION = 10;
    private FusedLocationProviderClient locationProviderClient;
    String key = "eon180$135rddyttd";
    String disttid = "0";
    String imsiSIM1 = "";
    private AlertDialog progressDialog;
    TelephonyManager telephonyManager;
    int count = 0;
    StringBuilder res1;
    String versname, acti_vate, dis_username, location, contact, zone, image, usrtype,logout,isRunning;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String uusername, alert, asyn_versn, versionName, track_status, track_interval,bill_amt_limit;
    ProgressDialog pDialog;
    CheckConnection chk = new CheckConnection(LoginActivityNew.this);
    AppPreferences appPrefs;
    TextView t_version;
    RelativeLayout progressBar;
   // ProgressBar progressBar;
    private LocationPrefs locationPrefs;
    String macAddress;
    int PERMISSION_ALL = 1;
    ArrayList<LoginDetail> loginList = new ArrayList<>();
    String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);

        loginn = findViewById(R.id.login);
        phn = findViewById(R.id.phnnum);
        email = findViewById(R.id.emadd);
        e_usrnme = findViewById(R.id.username);
        e_paswrd = findViewById(R.id.passwordd);
        t_version = findViewById(R.id.app_version);
        user = findViewById(R.id.user);
        pass = findViewById(R.id.pass);

        progressBar = findViewById(R.id.llayoutProgress);
        progressDialog = new SpotsDialog(this, R.style.Custom);

        ShowProgressBar(false);
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        pp_usrnmae = sharedprefs.getString("usname", "");
        pp_passwrd = sharedprefs.getString("pass", "");
        logout = sharedprefs.getString("logout","");
        isRunning = sharedprefs.getString("isRunning","");
        e_usrnme.setText(pp_usrnmae);
        e_paswrd.setText(pp_passwrd);
        //chckusr = new CheckUser();
        versionName = BuildConfig.VERSION_NAME;
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationPrefs = new LocationPrefs(this);
        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.eonBlue));
        try {
            String int_val = getIntent().getStringExtra("username");
            editor.putString("pass", "");
            editor.commit();
        } catch (Exception e) {
        }try {
            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (sharedprefs.getString("pass", "").equals("") || sharedprefs.getString("pass", "").equals(null)) {
                Log.i("***staylogin", "stay=" + pp_passwrd);
            }
        } catch (Exception npe) {
            npe.printStackTrace();
        }
        appPrefs = new AppPreferences(getApplicationContext());

        if (appPrefs.getproviderInfo().equals("true")) {
        } else {
            addRecord();
        }
        versname = getVersion();
        Log.i("***v***", versname);
        t_version.setText("Version " + versname);

        loginn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
//                    ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);
//                    ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (ActivityCompat.checkSelfPermission(LoginActivityNew.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//                            imsiSIM1 = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
//                        }
//                    } else {
//                        imsiSIM1 = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
//                    }
                    getMacAddr();
                p_usr = e_usrnme.getText().toString().trim();
                p_pass = e_paswrd.getText().toString().trim();
                if (p_usr.equals("")) {
                    user.setError("Please Enter Username");
                } else if (p_pass.equals("")) {
                    pass.setError("Please Enter Password");
                } else {
                    editor.putString("usname", p_usr);
                    editor.putString("pass", p_pass);
                    editor.commit();

                    View vie = LoginActivityNew.this.getCurrentFocus();
                    if (vie != null) {
                    }if (chk.isConnected()) {
                        getLogin();
                    } else {
                        chk.showConnectionErrorDialog();
                    }
                }
                    e_usrnme.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            user.setError("");
                            return false;
                        }
                    });
                    e_paswrd.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            pass.setError("");
                            return false;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        phn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + "01725044700"));
                try {
                    startActivity(callIntent);
                } catch (Exception e) {
                    Toast.makeText(LoginActivityNew.this.getApplicationContext(), "enable calling permissions", Toast.LENGTH_SHORT).show();
                }
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={"support@eoninfotech.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });
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
                    res1.append(String.format("%02X:", b));
                    macAddress = String.valueOf(res1);
                    editor.putString("MacAddress", macAddress);
                    editor.commit();
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "";
    }

    private void getLogin() {
        if(asyn_versn!=versname)
            progressDialog.show();
        ApiHolder loc_att = ServiceConnectionNewURL.getClient().create(ApiHolder.class);
        Call<LoginResponse> locCall = loc_att.loginResponse(p_usr,p_pass,imsiSIM1);
        locCall.enqueue(new Callback<LoginResponse>() {
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try{
                LoginResponse workTypeResponse = response.body();
                loginList = response.body().getLoginDetails();
                if (loginList.size() == 0) {
                    Toast.makeText(LoginActivityNew.this, "Username/Password Incorrect", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                } else{
                    int i=0;
                    user_id = loginList.get(i).getUsr_id();
                    versname = loginList.get(i).getVerno();
                    usrtype = loginList.get(i).getUsrtype();
                    uusername = loginList.get(i).getUsrname();
                    dis_username = loginList.get(i).getDisplayname();
                    contact = loginList.get(i).getContact();
                    zone = loginList.get(i).getZone();
                    location = loginList.get(i).getLocation();
                    image = loginList.get(i).getImage();
                    track_status = loginList.get(i).getTrack_status();
                    track_interval = loginList.get(i).getTrack_interval();
                    bill_amt_limit = loginList.get(i).getBill_amt_limit();
                    editor.putString("s_user_id",user_id);
                    editor.putString("s_uuser", uusername);
                    editor.putString("location", location);
                    editor.putString("usrtype",usrtype);
                    editor.putString("zone", zone);
                    editor.putString("version", versname);
                    editor.putString("contact", contact);
                    editor.putString("dis_user", dis_username);
                    editor.putString("image", image);
                    editor.putString("imei1",imsiSIM1);
                    editor.putString("track_status",track_status);
                    editor.putString("track_interval",track_interval);
                    editor.putString("bill_amt_limit",bill_amt_limit);
                    editor.commit();
                    progressDialog.hide();

                    if(logout.equalsIgnoreCase("logout")&&(isRunning.equalsIgnoreCase("true"))) {
                        startService(new Intent(LoginActivityNew.this, ForegroundService.class));
                    }else if(logout.equalsIgnoreCase("")&&(isRunning.equalsIgnoreCase(""))){
                        startService(new Intent(LoginActivityNew.this, ForegroundService.class));
                    }else if(logout.equalsIgnoreCase("logout")&&(isRunning.equalsIgnoreCase(""))){
                        startService(new Intent(LoginActivityNew.this, ForegroundService.class));
                    } else{
                    }
                        Intent intee = new Intent(LoginActivityNew.this, MainActivity.class);
                        startActivity(intee);
                        finish();
                        appPrefs.setLoggedIn(true);
                }
            }catch(Exception e){
                e.printStackTrace();}
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivityNew.this, "Username/Password Incorrect", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
                try {
                } catch (Exception e) {
                }
            }
        });
    }
    public void addRecord() {
        appPrefs.saveProviderInfo("true");
    }

    @Override
    public void onBackPressed() {
        this.count++;
        if (this.count <= 1) {
            Toast.makeText(getApplicationContext(), "Press back to exit", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }
    void logMeIn() {
        try {
            if (uusername.equals(p_usr)) {
                Log.i("***user vs user***", uusername + " " + p_usr);
                    Intent intee = new Intent(LoginActivityNew.this, MainActivity.class);
                    startActivity(intee);
                    finish();
                    appPrefs.setLoggedIn(true);
            }
        } catch (NullPointerException ne) {
            editor.putString("pass", "");
            editor.commit();
            Toast.makeText(getApplicationContext(), "Username/Password Incorrect", Toast.LENGTH_LONG).show();
        }
    }
    public String getVersion() {
        String v = "0.0";
        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionNumber = pinfo.versionCode;
            v = pinfo.versionName;
            Log.d("***version name***", v);
        } catch (PackageManager.NameNotFoundException e) {
            // Haaa? Really?
            Log.i("***lol***", e.toString());
        }
        return v;
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
            } else {
                // permission wasn't granted
            }
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    new LoginActivityNew.UpdateApp().execute("asdf");
                    break;
                case 5:
                    logMeIn();
                    break;
                default:
                    break;
            }
        }
    };

    class UpdateApp extends AsyncTask<String, String, Void> {

        int FileSize;
        int percentage;
        int count;
        ProgressDialog pDialog;
        long totalSize = 0;
        URLConnection urlconnection;

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivityNew.this);
            pDialog.setTitle("Updating...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... voids) {
            Log.i("****", voids[0]);
            try {
                String str = K.Url.getapk;
                HttpURLConnection c = (HttpURLConnection) new URL(str).openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/eontech.apk"));
                FileSize = c.getContentLength();
                InputStream is = c.getInputStream();
                byte[] buffer = new byte[1024];
                while ((count = is.read(buffer)) != -1) {
                    totalSize += count;
                    publishProgress("" + ((this.totalSize * 100) / FileSize));
                    fos.write(buffer, 0, count);
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException ignored) {
                ignored.printStackTrace();
            } catch (Exception ae) {
                ae.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            pDialog.setProgress(Integer.parseInt(values[0]));
            percentage = Integer.parseInt(values[0]);
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (percentage == 100) {
                try {
                    Intent promptInstall = new Intent("android.intent.action.VIEW");
                    promptInstall.setDataAndType(Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/eontech.apk")), "application/vnd.android.package-archive");
                    promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(promptInstall);
                    //openFolder();
                } catch (Exception ae) {
                    ae.printStackTrace();
                }
            } else
                Toast.makeText(getApplicationContext(), "Permission Required", Toast.LENGTH_LONG).show();
            pDialog.cancel();
        }
    }

    private void ShowProgressBar(boolean show) {
        try {
            if (show) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
