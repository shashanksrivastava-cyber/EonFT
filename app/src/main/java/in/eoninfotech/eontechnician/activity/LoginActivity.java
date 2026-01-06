package in.eoninfotech.eontechnician.activity;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import in.eoninfotech.eontechnician.AdminMainActivity;
import in.eoninfotech.eontechnician.AppPreferences;
import in.eoninfotech.eontechnician.MainActivity;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.helper.TelephonyInfo;
/***************************************************************************/
// Copyright EON Infotech Ltd., published work, created 2018.          //
// This computer program includes Confidential, Proprietary information  //
// and is a trade secret of EON Infotech Ltd. All use, disclosure and/or //
// reproduction is prohibited unless authorised in writing by an         //
// authorised officer of EON Infotech Ltd. All rights reserved.          //
//

/**************************************************************************/

public class LoginActivity extends Activity {
    Button loginn;
    TextView phn, email;
    EditText e_usrnme, e_paswrd;
    String p_usr = "", p_pass = "", pp_usrnmae = "", pp_passwrd = "";
    int REQUEST_CODE_PERMISSION = 10;
    String key = "eon180$135rddyttd";
    CheckUser chckusr;
    String disttid = "0";
    // CheckBox rembrme;
    String imsiSIM1 = "0";
    int count = 0;
    String versname, acti_vate, dis_username, user_id;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String uusername, alert, asyn_versn;
    ProgressDialog pDialog;
    CheckConnection chk = new CheckConnection(LoginActivity.this);
    AppPreferences appPrefs;
    TextView t_version;
    int PERMISSION_ALL = 1;
    FirebaseRemoteConfig remoteConfig;
    String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginn = findViewById(R.id.login);
        phn = findViewById(R.id.phnnum);
        email = findViewById(R.id.emadd);
        e_usrnme = findViewById(R.id.username);
        e_paswrd = findViewById(R.id.passwordd);
        t_version = findViewById(R.id.app_version);
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        pp_usrnmae = sharedprefs.getString("usname", "");
        pp_passwrd = sharedprefs.getString("pass", "");
        e_usrnme.setText(pp_usrnmae);
        e_paswrd.setText(pp_passwrd);


        try {
            String int_val = getIntent().getStringExtra("username");
            editor.putString("pass", "");
            editor.commit();
        } catch (Exception e) {

        }
        try {
            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (sharedprefs.getString("pass", "").equals("") || sharedprefs.getString("pass", "").equals(null)) {
                Log.i("***staylogin", "stay=" + pp_passwrd);
            } else {
                Log.i("***directlogin", "direct=" + pp_passwrd);
                if (sharedprefs.getString("s_distt", "").equals("0")) {
                    Intent intee = new Intent(LoginActivity.this,  AdminMainActivity.class);
                    startActivity(intee);
                    finish();
                } else if (sharedprefs.getString("s_distt", "").equals("2")) {
                    Intent intee = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intee);
                    finish();
                    appPrefs.setLoggedIn(true);
                }
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

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(LoginActivity.this);
                    imsiSIM1 = telephonyInfo.getImsiSIM1();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        loginn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    if (!hasPermissions(LoginActivity.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS, PERMISSION_ALL);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(LoginActivity.this);
                    imsiSIM1 = telephonyInfo.getImsiSIM1();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                p_usr = e_usrnme.getText().toString().trim();
                p_pass = e_paswrd.getText().toString().trim();
                Log.i("****clicklistnr****", p_usr + p_pass + " imei =" + imsiSIM1);
                if (p_usr.equals("")) {
                    e_usrnme.setError("fill field");
                } else if (p_pass.equals("")) {
                    e_paswrd.setError("fill field");
                } else {
                    editor.putString("usname", p_usr);
                    editor.putString("pass", p_pass);
                    editor.commit();
                    View vie = LoginActivity.this.getCurrentFocus();
                    if (vie != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(vie.getWindowToken(), 0);
                    }
                    if (chk.isConnected() && imsiSIM1 != null) {
                        chckusr = new CheckUser();
                        chckusr.execute(p_usr, p_pass, key);
                        checkAyscTask chke = new checkAyscTask(chckusr);
                        (new Thread(chke)).start();
                    } else {
                        chk.showConnectionErrorDialog();
                    }
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
                    Toast.makeText(LoginActivity.this.getApplicationContext(), "enable calling permissions", Toast.LENGTH_SHORT).show();
                }
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {"support@eoninfotech.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));

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

    void showDialogg(String msg, int labl, int style) {
       AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, style);
        builder.setMessage(msg)
                .setCancelable(false);
        if (labl == 1) {
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    //checkWritingPermission();
                    new UpdateApp().execute("asdf");
                }
            });
            builder.setNegativeButton("Remind me later", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    logMeIn();
                }
            });
        }
        if (labl == 0) {
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
        }
        AlertDialog alert = builder.create();
        alert.setTitle("Alert");
        alert.show();
    }

    void logMeIn() {
        try {
            if (uusername.equals(p_usr)) {
                Log.i("***user vs user***", uusername + " " + p_usr);
                if (sharedprefs.getString("s_distt", "").equals("0")) {
                    Intent intee = new Intent(LoginActivity.this, AdminMainActivity.class);
                    startActivity(intee);
                    finish();
                } else if (sharedprefs.getString("s_distt", "").equals("2")) {
                    Intent intee = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intee);
                    finish();
                    appPrefs.setLoggedIn(true);
                }
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
                    new UpdateApp().execute("asdf");       // update app
                    break;
                case 5:
                    logMeIn();
                    break;
                default:
                    break;
            }
        }
    };

    class CheckUser extends AsyncTask<String, Void, Void> {
        URL url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Log.i("****user & pass****", strings[0] + strings[1]);
                String data = strings[0] + "&" + strings[1] + "&" + K.Url.urlkey + "&" + imsiSIM1;
                byte[] encodedd = Base64.encode(data.getBytes("UTF-8"), Base64.DEFAULT);
                String str1 = new String(encodedd, "UTF-8");
                Log.i("***urlen****", K.Url.login_user + str1);
                url = new URL(K.Url.login_user + str1);
                DocumentBuilderFactory builder = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = builder.newDocumentBuilder();
                Document doc = documentBuilder.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                NodeList nl = doc.getElementsByTagName("login");
                for (int i = 0; i < nl.getLength(); i++) {
                    Element e = (Element) nl.item(i);
                    try {
                        acti_vate = K.getNode("activate", e);
                    } catch (NullPointerException ne) {
                        acti_vate = "";
                    }
                    asyn_versn = K.getNode("verno", e);
                    dis_username = K.getNode("displayname", e);
                    try {
                        alert = K.getNode("alert", e);
                    } catch (NullPointerException ne) {
                        alert = "nothing";
                    }
                    try {
                        user_id = K.getNode("user_id", e);
                    } catch (NullPointerException ne) {
                        user_id = "";
                    }
                    uusername = K.getNode("usrname", e);
                    disttid = K.getNode("usrtype", e);

                    editor.putString("s_uuser", uusername);
                    editor.putString("user_id", user_id);
                    editor.putString("s_distt", disttid);
                    editor.putString("version", versname);
                    editor.putString("alert", alert);
                    editor.putString("dis_user", dis_username);
                    editor.commit();
                    Log.i("****disttid****", disttid);
                    Log.i("****user****", uusername);
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ParserConfigurationException pe) {
                pe.printStackTrace();
            } catch (Exception ae) {
                ae.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.cancel();
            try {
                if (acti_vate.equals("1")) {
                    showDialogg("Your account is deactivated", 0, R.style.MyAlertDialogStyleDisConnected);
                } else {
                    if (!versname.equals(asyn_versn)) {
                        Log.i("***version***", versname + "  " + asyn_versn);
                        showDialogg("New Release.Please Update.", 1, R.style.MyAlertDialogStyleConnected);
                    } else {
                        editor.putString("pass", e_paswrd.getText().toString());
                        logMeIn();
                    }
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
                editor.putString("pass", "");
                editor.commit();

                Toast.makeText(getApplicationContext(), "username/password incorrect", Toast.LENGTH_LONG).show();
            }
        }
    }

    class UpdateApp extends AsyncTask<String, String, Void> {

        int FileSize;
        int percentage;
        int count;
        ProgressDialog pDialog;
        long totalSize = 0;
        URLConnection urlconnection;

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setTitle("Updating...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... voids) {
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
                Log.i("***************", ignored.toString());
            } catch (Exception ae) {
                ae.printStackTrace();
                Log.i("***************", ae.toString());
            }
            Log.i("****", voids[0]);
            return null;
        }

        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            pDialog.setProgress(Integer.parseInt(values[0]));
            percentage = Integer.parseInt(values[0]);
            Log.i("********", values[0]);
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("**********", "post Execute");
            if (percentage == 100) {
                try {
                    Intent promptInstall = new Intent("android.intent.action.VIEW");
                    promptInstall.setDataAndType(Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/eontech.apk")), "application/vnd.android.package-archive");
                    promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(promptInstall);
                    //    openFolder();
                } catch (Exception ae) {
                    ae.printStackTrace();
                }
            } else
                Toast.makeText(getApplicationContext(), "Permission Required", Toast.LENGTH_LONG).show();
            pDialog.cancel();
        }
    }

    class checkAyscTask implements Runnable {
        AsyncTask<String, Void, Void> mAT;
        Context context;

        public checkAyscTask(AsyncTask<String, Void, Void> at) {
            mAT = at;
        }

        @Override
        public void run() {
            mHandler.postDelayed(runnable, 30000);
            // After 30sec the task in run() of runnable will be done
        }

        Handler mHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mAT.getStatus() == AsyncTask.Status.RUNNING || mAT.getStatus() == AsyncTask.Status.PENDING) {
                    mAT.cancel(true); //Cancel Async task or do the operation you want after 30 sec
                    editor.putString("pass", "");
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Connection timeout", Toast.LENGTH_SHORT).show();
                    pDialog.cancel();
                }
            }
        };
    }
}
