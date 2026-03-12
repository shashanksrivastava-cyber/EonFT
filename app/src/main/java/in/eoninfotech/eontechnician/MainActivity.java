package in.eoninfotech.eontechnician;

import static android.app.ProgressDialog.show;
import static android.content.ContentValues.TAG;
import static com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import dagger.hilt.android.AndroidEntryPoint;
import de.hdodenhof.circleimageview.CircleImageView;
import in.eoninfotech.eontechnician.activity.BaseActivity;
import in.eoninfotech.eontechnician.activity.ReceiveDeviceDetails;
import in.eoninfotech.eontechnician.di.SharedPreferenceManager;
import in.eoninfotech.eontechnician.fragments.AddUMFragment;
import in.eoninfotech.eontechnician.fragments.AdditionalMaterialFragment;
import in.eoninfotech.eontechnician.fragments.AdditionalMaterialViewFragment;
import in.eoninfotech.eontechnician.fragments.LiveStatusFragmentEon;
import in.eoninfotech.eontechnician.fragments.NewInstallmentFragmentUpdated;
import in.eoninfotech.eontechnician.fragments.RemoveUMFragment;
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.responses.TechnicianMonthDetail;
import in.eoninfotech.eontechnician.responses.TechnicianMonthResponse;
import in.eoninfotech.eontechnician.responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.service.ForegroundService;
import in.eoninfotech.eontechnician.activity.Devicedashboards;
import in.eoninfotech.eontechnician.activity.LoginActivityNew;
import in.eoninfotech.eontechnician.activity.MessageActivity;
import in.eoninfotech.eontechnician.activity.ReceiveDeviceActivity;
import in.eoninfotech.eontechnician.fragments.ActivityDetailFragment;
import in.eoninfotech.eontechnician.fragments.BillIntimationFragment;
import in.eoninfotech.eontechnician.fragments.BillViewFragment;
import in.eoninfotech.eontechnician.fragments.CallSheetFragment;
import in.eoninfotech.eontechnician.fragments.DashBoardFragment;
import in.eoninfotech.eontechnician.fragments.LiveStatusFragment;
import in.eoninfotech.eontechnician.fragments.NewInstallmentFragment;
import in.eoninfotech.eontechnician.fragments.OtherDashBoardFragment;
import in.eoninfotech.eontechnician.fragments.PaymentCollectionReportFragment;
import in.eoninfotech.eontechnician.fragments.StockFragment;
import in.eoninfotech.eontechnician.fragments.ViewActivityLogsFragment;
import in.eoninfotech.eontechnician.fragments.ViewCallSheetFragment;
import in.eoninfotech.eontechnician.fragments.ViewStockFragment;
import in.eoninfotech.eontechnician.helper.ClientList;
import in.eoninfotech.eontechnician.helper.EONUtil;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.fragments.ActivityLogFragment;
import in.eoninfotech.eontechnician.fragments.FragmentIncentiveTab;
import in.eoninfotech.eontechnician.helper.TelephonyInfo;
import in.eoninfotech.eontechnician.storage.LocationPrefs;
import in.eoninfotech.eontechnician.utils.ImageUtils;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.MessageResponse;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import in.eoninfotech.eontechnician.webservice.TrackingDetail;
import jakarta.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

@AndroidEntryPoint
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Inject
    LocationPrefs locationPrefs;
    @Inject
    CheckConnection chk;
    @Inject
    TelephonyManager telephonyManager;
    @Inject AppPreferences appPrefs;
    @Inject
    SharedPreferenceManager sharedPref;
    FragmentManager fm;
    FragmentTransaction ft;
    String username, version, dis_username, image;
    Toolbar toolbar;
    ArrayList<ClientList> ClientLists = new ArrayList<ClientList>();
    //AppPreferences appPrefs;
    FloatingActionButton fab, panic_fab;
    //SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    in.eoninfotech.eontechnician.FragmentCurrentMonth fragmentCurrentMonth;
    String usrname, alertt, uusername, versionname, disgnid = "0", activityName = "Activities", intent_req = "TT";
    NavigationView navigationView;
    Bundle bundle;
    in.eoninfotech.eontechnician.FaqsFragment faqsfragment;
    ActivityDetailFragment activityDetailFragment;
    ActivityLogFragment activityLogFragment;
    //NewInstallmentFragment installmentFragment;
    NewInstallmentFragmentUpdated installmentFragment;
    LiveStatusFragment liveStatusFragment;
    LiveStatusFragmentEon liveStatusFragmentEon;
    StockFragment stockFragment;
    TextView textCartItemCount;
    PaymentCollectionReportFragment paymentCollectionReportFragment;
    in.eoninfotech.eontechnician.MaterialReturnFragment materialReturnFragment;
    in.eoninfotech.eontechnician.MaterialtoTechFragment materialtoTechFragment;
    in.eoninfotech.eontechnician.MaterialReturnViews materialReturnView;
    BillIntimationFragment billIntimationFragment;
    AdditionalMaterialFragment additionalMaterialFragment;
    AdditionalMaterialViewFragment additionalMaterialViewFragment;
    ViewStockFragment viewStockFragment;
    CallSheetFragment callSheetFragment;
    ViewCallSheetFragment viewCallSheetFragment;
    ViewActivityLogsFragment viewActivityLogsFragment;
    DashBoardFragment dashBoardFragment;
    OtherDashBoardFragment otherDashBoardFragment;
    BillViewFragment billViewFragment;
    ViewPagerAdapter viewPagerAdapter;
    ViewPagerAdapterAtd viewPagerAdapterAtd;
    ViewPagerAdapterStock viewPagerAdapterStock;
    ViewPagerAdapterCallSheet viewPagerAdapterCallSheet;
    ViewPagerAdapterActivity viewPagerAdapterActivity;
    ViewPagerAdapterBills viewPagerAdapterBills;
    ViewPagerAdapterAddMaterial viewPagerAdapterAddMaterial;
    ViewPagerReturnMaterial viewPagerReturnMaterial;
    ViewPagerSendToTechnician viewPagerSendtoTechnician;
    AddUMFragment addUMFragment;
    RemoveUMFragment removeUMFragment;
    ViewPagerAddRemoveUM viewPagerAdapterUMAddRemove;
    ArrayList<TrackingDetail> trackingDetails = new ArrayList<>();
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    int REQUEST_CODE_PERMISSION = 1001;
    DownloadManager manager;
    MediaPlayer mp = new MediaPlayer();
    private Menu menu;
    String faulty_num, imei;
    Dialog myDialog;
    RelativeLayout progressBar;
    TextView txtclose, month, name, loc, viewMore;
    CircleImageView tech_img;
    ProgressBar progressBars;
    ArrayList<TechnicianMonthDetail> techList = new ArrayList<>();
    TabLayout tabLayout;
    public static int int_items = 2;
    ViewPager viewPager, viewpagerattendance, viewpageractivity, viewpagerstock, viewpagercallsheet, viewPagerBill, viewPagerMaterialReturn, viewPagertechnician, viewPagerAddMaterial,viewPagerAddRemoveUM;
    String tab = "1";
    int count = 0;
    private String currentVersion;
    LinearLayout linearLayout;
    int id = 0;
    String s_date = "0", status = "0";
    String msg_type = "";
    ArrayList<MessageResponse> messageResponses = new ArrayList<>();
    FrameLayout frame;
    private AppUpdateManager appUpdateManager;
    private final int UPDATE_REQUEST_CODE = 99;
    private static final int MY_REQUEST_CODE = 17326;
    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 11;
    private Trace mainTrace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainTrace = FirebasePerformance.getInstance().newTrace("MainActivity_Load");
        mainTrace.start();
        Log.d("PERF_TEST", "MainActivity onCreate started at " + System.currentTimeMillis());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle("ADD Dashboard");

        username = sharedPref.getUsername();
        versionname = sharedPref.getVersionName();
        image = sharedPref.getImage();
        alertt = sharedPref.getAlert();
        disgnid = sharedPref.getDistrictId();
        dis_username = sharedPref.getDisplayUsername();

        fm = getSupportFragmentManager();
        bundle = new Bundle();
        bundle.putString("disttid", disgnid);
        bundle.putString("usernme", usrname);
        bundle.putString("version", versionname);
        bundle.putString("image", image);

        setupAppUpdate();

        try {
            TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(MainActivity.this);
            imei = telephonyInfo.getImsiSIM1();
        } catch (Exception e) {
            e.printStackTrace();
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                new MaterialAlertDialogBuilder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirm Exit")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", (dialog, which) -> finish())
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        fab = findViewById(R.id.fab);
        panic_fab = findViewById(R.id.panic_fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MessageActivity.class);
            startActivity(intent);
        });
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        myDialog = new Dialog(this);
        if (mainTrace != null) {
            mainTrace.stop();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView headr_usrnam = view.findViewById(R.id.header_username);
        headr_usrnam.setText(dis_username.toUpperCase());
        getFaultyVts(0);
        try {
            //checkWritingPermission();
            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = getIntent();
        tab = intent.getStringExtra("tab");
        viewPager = findViewById(R.id.viewpager);
        viewpagerattendance = findViewById(R.id.viewpagerattendance);
        viewpageractivity = findViewById(R.id.viewpageractivity);
        viewpagerstock = findViewById(R.id.viewpagerstock);
        viewpagercallsheet = findViewById(R.id.viewpagercallsheet);
        viewPagerBill = findViewById(R.id.viewPagerBill);
        viewPagerMaterialReturn = findViewById(R.id.viewPagerMaterialReturn);
        viewPagertechnician = findViewById(R.id.viewPagerSendtoTechnician);
        viewPagerAddMaterial = findViewById(R.id.viewPagerAddMaterial);
        viewPagerAddRemoveUM = findViewById(R.id.viewPagerAddRemoveUM);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        handleIntentActions();
    }

    private void handleIntentActions() {
        Intent intent1 = getIntent();
        try {
            intent_req = intent1.getStringExtra("intent");
        } catch (Exception e) {
            intent_req = "";
            throw new RuntimeException(e);
        }

        try {
            if ("toEon".equalsIgnoreCase(intent_req)) {
                sendToEon();
            } else if ("toFT".equalsIgnoreCase(intent_req)) {
                sendToFT();
            }else {
                addDashboard();
            }
        } catch (Exception e) {
            addDashboard();
            throw new RuntimeException(e);
        }
    }

    private void setupAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateManager.registerListener(installStateUpdatedListener);

        inAppUpdate();
    }

    private void checkLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
            new AlertDialog.Builder(this)
                    .setMessage("Your Location is OFF.")
                    .setPositiveButton("Enable Location", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent dialogIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(dialogIntent);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    private void initializeFirebase() {

        FirebaseApp.initializeApp(this);

        FirebaseRemoteConfig remoteConfig =
                FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings =
                new FirebaseRemoteConfigSettings.Builder()
                        .setMinimumFetchIntervalInSeconds(60)
                        .build();

        remoteConfig.setConfigSettingsAsync(configSettings);

        remoteConfig.fetchAndActivate()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        long remoteVersion =
                                remoteConfig.getLong("logout_version");

                        long localVersion =
                                sharedPref.getLogoutVersion();

                        // Logout only if version increased
                        if (remoteVersion > localVersion
                                && appPrefs.isLoggedIn()) {

                            logoutUser(remoteVersion);
                        }
                    }
                });
    }

    private void logoutUser(long remoteVersion) {

        new AlertDialog.Builder(this)
                .setTitle("Session Expired")
                .setMessage("Your session has expired. Please login again.")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {

                    logoutMethod(remoteVersion);

                })
                .show();
    }

    private void logoutMethod(long remoteVersion) {

        // Save latest version so dialog doesn't repeat
        sharedPref.clearAll();
        appPrefs.setLoggedIn(false);
        sharedPref.setLogoutVersion(remoteVersion);

        Intent intent =
                new Intent(MainActivity.this, LoginActivityNew.class);

        intent.putExtra("username", "us");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish();
    }


    private void monitorConnectivity() {
        ConnectivityManager m = (ConnectivityManager) getSystemService(Service.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            m.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    Log.e(TAG, "onAvailable: ");
                    if (count == 0) {
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Internet connection restored", Snackbar.LENGTH_LONG).show();
                        count = 0;
                    }
                }

                @Override
                public void onLost(Network network) {
                    Log.e(TAG, "onLost: ");
                    count = 1;
                    Snackbar.make(findViewById(android.R.id.content), "It seems internet connection not available", Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    private void addDashboard() {
        setTitle("ADD Dashboard");
        tabLayout.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        viewpagerattendance.setVisibility(View.GONE);
        viewpageractivity.setVisibility(View.GONE);
        viewpagercallsheet.setVisibility(View.GONE);
        viewPagerBill.setVisibility(View.GONE);
        viewPagerMaterialReturn.setVisibility(View.GONE);
        viewPagerAddMaterial.setVisibility(View.GONE);
        viewPagerAddRemoveUM.setVisibility(View.GONE);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        hideKeyboard();
    }

    private void sendToFT() {

        materialReturnFragment = new in.eoninfotech.eontechnician.MaterialReturnFragment();
        materialReturnFragment.setArguments(bundle);
        ft = fm.beginTransaction().replace(R.id.framelay, materialReturnFragment);
        setTitle("Send to Technician");
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.removeAllTabs();
        viewpagerattendance.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        viewpageractivity.setVisibility(View.GONE);
        viewpagercallsheet.setVisibility(View.GONE);
        viewpagerstock.setVisibility(View.GONE);
        viewPagerBill.setVisibility(View.GONE);
        viewPagerMaterialReturn.setVisibility(View.GONE);
        viewPagerAddMaterial.setVisibility(View.GONE);
        viewPagertechnician.setVisibility(View.VISIBLE);
        viewPagerSendtoTechnician = new ViewPagerSendToTechnician(getSupportFragmentManager());
        viewPagertechnician.setAdapter(viewPagerSendtoTechnician);
        tabLayout.setupWithViewPager(viewPagertechnician);
        ft.commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        hideKeyboard();
    }

    private void inAppUpdate() {

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
//            @Override
//            public void onSuccess(AppUpdateInfo appUpdateInfo) {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.

                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
//                                // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
//                                activityResultLauncher,
//                                // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
//                                // flexible updates.
//                                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
//                                        .setAllowAssetPackDeletion(true)
//                                        .build()
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            MainActivity.this,
                            // Include a request code to later monitor this update request.
                            UPDATE_REQUEST_CODE);
                } catch (IntentSender.SendIntentException ignored) {

                }
            } else {
                // Toast.makeText(MainActivity.this, "No Update Available", Toast.LENGTH_SHORT).show();
            }
//            }
        });
    }

    //lambda operation used for below listener
    InstallStateUpdatedListener installStateUpdatedListener = installState -> {
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnackbarForCompleteUpdate();
        } else
            Log.e("UPDATE", "Not downloaded yet");
    };

    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(
                        findViewById(android.R.id.content),
                        "Update almost finished!",
                        Snackbar.LENGTH_INDEFINITE);
        //lambda operation used for below action
        snackbar.setAction(this.getString(R.string.restart), view ->
                appUpdateManager.completeUpdate());
        snackbar.setActionTextColor(getResources().getColor(R.color.red));
        snackbar.show();
    }

    private void displayAlert() {

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
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
            new android.app.AlertDialog.Builder(this)
                    .setMessage("Your Location is OFF.")
                    .setPositiveButton("Enable Location", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.faulty_status, menu);
        final MenuItem menuItem = menu.findItem(R.id.msg_alert);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.textViewCounter);
        panic_fab = actionView.findViewById(R.id.panic_fab);
        frame = actionView.findViewById(R.id.frame);
        //setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.msg_alert) {
            Intent intent = new Intent(MainActivity.this, MessageActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        // ✅ Intercept these BEFORE any layout changes
        if (id == R.id.menu_logout) {
            confirmLogout();
            return true;
        }
        if (id == R.id.nav_material_return) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            sendToEon();
            return true;
        }
        if (id == R.id.nav_material_send_to_tech) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            sendToFT();
            return true;
        }
        if (id == R.id.nav_material) {
            startActivity(new Intent(MainActivity.this, ReceiveDeviceActivity.class));
            return true;
        }
        if (id == R.id.material_dashboard) {
            startActivity(new Intent(MainActivity.this, Devicedashboards.class));
            return true;
        }
        if (id == R.id.nav_technician_of_the_month) {
            showTechnicianOfMonthPopup();
            return true;
        }
        if (id == R.id.tm_removal) { openVideo("tm"); return true; }
        if (id == R.id.pump_removal) { openVideo("pump"); return true; }
        if (id == R.id.device_maint) { openDeviceMaintenance("maint"); return true; }
        if (id == R.id.knowldge_base) { openDeviceMaintenance("knowldge"); return true; }
        if (id == R.id.faqs) { openDeviceMaintenance("faq"); return true; }
        if (id == R.id.nav_tech_dashboard) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            addDashboard();
            return true;
        }

        // ── Only real fragment navigation reaches here ────────────────────
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        hideKeyboard();

        Fragment fragment = null;
        String title = "";
        boolean useTabLayout = false;
        ViewPager targetViewPager = null;
        FragmentTransaction ft = fm.beginTransaction();

        resetAllViewPagerVisibility(); // ← now ONLY called for fragment navigation

        switch (id) {
            case R.id.nav_dashboard:
                if (!"0".equals(disgnid)) {
                    fragment = new FragmentIncentiveTab();
                    fragment.setArguments(bundle);
                }
                title = "Incentive";
                break;

            case R.id.nav_mark_site:
                title = "Attendance Activity";
                useTabLayout = true;
                targetViewPager = viewpagerattendance;
                targetViewPager.setAdapter(new ViewPagerAdapterAtd(getSupportFragmentManager()));
                break;

            case R.id.nav_incentive:
                fragment = new in.eoninfotech.eontechnician.FragmentCurrentMonth();
                fragment.setArguments(bundle);
                title = "Incentive Activity";
                break;

            case R.id.nav_stock:
                fragment = new StockFragment();
                fragment.setArguments(bundle);
                title = "Stock Activity";
                useTabLayout = true;
                targetViewPager = viewpagerstock;
                targetViewPager.setAdapter(new ViewPagerAdapterStock(getSupportFragmentManager()));
                break;

            case R.id.nav_live_status:
                fragment = new LiveStatusFragment();
                fragment.setArguments(bundle);
                title = "Live Status";
                break;

            case R.id.nav_live_status_eon:
                fragment = new LiveStatusFragmentEon();
                fragment.setArguments(bundle);
                title = "Live Status Eon";
                break;

            case R.id.nav_new_repair:
                title = "Activities";
                useTabLayout = true;
                targetViewPager = viewpageractivity;
                targetViewPager.setAdapter(new ViewPagerAdapterActivity(getSupportFragmentManager()));
                break;

            case R.id.nav_call_sheet:
                fragment = new CallSheetFragment();
                fragment.setArguments(bundle);
                title = "Call Sheet";
                useTabLayout = true;
                targetViewPager = viewpagercallsheet;
                targetViewPager.setAdapter(new ViewPagerAdapterCallSheet(getSupportFragmentManager()));
                break;

            case R.id.nav_pay_col_report:
                fragment = new PaymentCollectionReportFragment();
                fragment.setArguments(bundle);
                title = "Payment Collection Report";
                break;

            case R.id.nav_bill:
                fragment = new BillIntimationFragment();
                fragment.setArguments(bundle);
                title = "Bill Intimation";
                useTabLayout = true;
                targetViewPager = viewPagerBill;
                targetViewPager.setAdapter(new ViewPagerAdapterBills(getSupportFragmentManager()));
                break;

            case R.id.nav_additional_material:
                fragment = new AdditionalMaterialFragment();
                fragment.setArguments(bundle);
                title = "Service Stock Demand";
                useTabLayout = true;
                targetViewPager = viewPagerAddMaterial;
                targetViewPager.setAdapter(new ViewPagerAdapterAddMaterial(getSupportFragmentManager()));
                break;

            case R.id.service_request:
                fragment = new ServiceRequestFragment();
                fragment.setArguments(bundle);
                title = "Service Request";
                break;

            case R.id.nav_um:
                fragment = new AddUMFragment();
                fragment.setArguments(bundle);
                title = "Add/Remove UM";
                useTabLayout = true;
                targetViewPager = viewPagerAddRemoveUM;
                targetViewPager.setAdapter(new ViewPagerAddRemoveUM(getSupportFragmentManager()));
                break;
        }

        if (fragment != null) {
            ft.replace(R.id.framelay, fragment).commitAllowingStateLoss();
        }

        setTitle(title);
        tabLayout.setVisibility(useTabLayout ? View.VISIBLE : View.GONE);
        if (targetViewPager != null) {
            targetViewPager.setVisibility(View.VISIBLE);
            tabLayout.setupWithViewPager(targetViewPager);
        }

        return true;
    }

    private void openVideo(String type) {
        Intent intent = new Intent(MainActivity.this, VideoViewActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    private void openDeviceMaintenance(String type) {
        Intent intent = new Intent(MainActivity.this, DeviceMaintenance.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    private void showTechnicianOfMonthPopup() {
        myDialog.setContentView(R.layout.technician_monthpopup);
        txtclose = myDialog.findViewById(R.id.txtclose);
        month = myDialog.findViewById(R.id.technician_month);
        name = myDialog.findViewById(R.id.tech_name);
        loc = myDialog.findViewById(R.id.tech_loc);
        tech_img = myDialog.findViewById(R.id.ivProfile);
        progressBars = findViewById(R.id.progressBars);
        viewMore = myDialog.findViewById(R.id.viewMore);

        txtclose.setOnClickListener(v -> myDialog.dismiss());
        viewMore.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), in.eoninfotech.eontechnician.CardViewActivity.class));
            myDialog.hide();
        });

        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        getDetail();
    }

    private void confirmLogout() {
        boolean isRunning = EONUtil.isServiceRunning(getBaseContext(), ForegroundService.class);
        new MaterialAlertDialogBuilder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    logoutMethodManual();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void logoutMethodManual() {

        sharedPref.clearAllExceptLogoutVersion();
        appPrefs.setLoggedIn(false);

        Intent intent = new Intent(MainActivity.this, LoginActivityNew.class);
        intent.putExtra("username", "us");

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

        finishAffinity();

//        sharedPref.clearAllExceptLogoutVersion();
//        appPrefs.setLoggedIn(false);
//
//        Intent intent = new Intent(MainActivity.this, LoginActivityNew.class);
//        intent.putExtra("username", "us");
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//        startActivity(intent);
//
//        overridePendingTransition(0,0); // prevent white screen flash
//        finish();

        }

    private void resetAllViewPagerVisibility() {
        viewPager.setVisibility(View.GONE);
        viewpagerattendance.setVisibility(View.GONE);
        viewpageractivity.setVisibility(View.GONE);
        viewpagercallsheet.setVisibility(View.GONE);
        viewpagerstock.setVisibility(View.GONE);
        viewPagerBill.setVisibility(View.GONE);
        viewPagerMaterialReturn.setVisibility(View.GONE);
        viewPagerAddMaterial.setVisibility(View.GONE);
        viewPagertechnician.setVisibility(View.GONE);
        viewPagerAddRemoveUM.setVisibility(View.GONE);
    }

    private void sendToEon() {
        materialReturnFragment = new in.eoninfotech.eontechnician.MaterialReturnFragment();
        materialReturnFragment.setArguments(bundle);
        ft = fm.beginTransaction().replace(R.id.framelay, materialReturnFragment);
        setTitle("Send to Eon");
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.removeAllTabs();
        viewpagerattendance.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        viewpageractivity.setVisibility(View.GONE);
        viewpagercallsheet.setVisibility(View.GONE);
        viewpagerstock.setVisibility(View.GONE);
        viewPagerBill.setVisibility(View.GONE);
        viewPagerMaterialReturn.setVisibility(View.VISIBLE);
        viewPagerReturnMaterial = new ViewPagerReturnMaterial(getSupportFragmentManager());
        viewPagerMaterialReturn.setAdapter(viewPagerReturnMaterial);
        tabLayout.setupWithViewPager(viewPagerMaterialReturn);
        ft.commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        hideKeyboard();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Override this method in the activity that hosts the Fragment and call super
        // in order to receive the result inside onActivityResult from the fragment.
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == MY_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (resultCode != RESULT_OK) {
                        Toast.makeText(this, "RESULT_OK" + resultCode, Toast.LENGTH_LONG).show();
                        Log.d("RESULT_OK  :", "" + resultCode);
                    }
                    break;
                case Activity.RESULT_CANCELED:

                    if (resultCode != RESULT_CANCELED) {
                        Toast.makeText(this, "RESULT_CANCELED" + resultCode, Toast.LENGTH_LONG).show();
                        Log.d("RESULT_CANCELED  :", "" + resultCode);
                    }
                    break;
                case RESULT_IN_APP_UPDATE_FAILED:

                    if (resultCode != RESULT_IN_APP_UPDATE_FAILED) {

                        Toast.makeText(this, "RESULT_IN_APP_UPDATE_FAILED" + resultCode, Toast.LENGTH_LONG).show();
                        Log.d("RESULT_IN_APP_FAILED:", "" + resultCode);
                    }
            }
        }
    }

    private void hideKeyboard() {
        LinearLayout mainLayout;
        mainLayout = findViewById(R.id.framelay);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
    }

    private void getDetail() {

        ShowProgressBar(true);
        ApiHolder loc_att = ServiceConnectionNewURL.getClient(versionname).create(ApiHolder.class);
        Call<TechnicianMonthResponse> locCall = loc_att.requestTechnicianoftheMonth();
        locCall.enqueue(new Callback<TechnicianMonthResponse>() {
            public void onResponse(Call<TechnicianMonthResponse> call, Response<TechnicianMonthResponse> response) {
                TechnicianMonthResponse workTypeResponse = response.body();
                techList = response.body().getTechList();
                Log.i("**work respnse", " " + response.body());
                if (techList.size() == 0) {
                } else {
                    int i = 0;
                    String smonth = techList.get(i).getMonth() + " " + techList.get(i).getYear();
                    String sname = techList.get(i).getName();
                    String sloc = techList.get(i).getLocation();
                    String imageUri = K.Url.IMAGE_URL + techList.get(i).getImage();
                    month.setText(smonth);
                    name.setText(sname);
                    loc.setText(sloc);
                    if (imageUri.equals("") || imageUri.equals(null)) {
                        tech_img.setImageResource(R.drawable.user);
                        ShowProgressBar(false);
                    } else {
                        ImageUtils.glideImage(tech_img, imageUri, R.drawable.user);
                        ShowProgressBar(false);
                    }
                }
                ShowProgressBar(false);
            }

            @Override
            public void onFailure(Call<TechnicianMonthResponse> call, Throwable t) {
                try {
                } catch (Exception e) {
                }
            }
        });
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
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

    public void getFaultyVts(final int click) {

        ApiHolder log_att = ServiceConnectionNewURL.getClient(versionname).create(ApiHolder.class);
        Call<UpdateDataResponse> call = log_att.faulty_vts(usrname);
        Log.i("****call", String.valueOf(call));
        call.enqueue(new Callback<UpdateDataResponse>() {
            @Override
            public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                UpdateDataResponse updateDataResponse = response.body();
                Log.i("**respnse", " " + response.body());
                try {
                    if (updateDataResponse != null) {
                        if (updateDataResponse.getType() == 1) {
                            try {
                                faulty_num = updateDataResponse.getFaulty_vts();
                                try {
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (Integer.parseInt(faulty_num) >= Integer.parseInt(updateDataResponse.getRange())) {
                                    showDialog("You have " + faulty_num + " faulty devices, Please send to office immediately.", 0);
                                    menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_warning_black_24dp));
                                } else if (click == 1 && Integer.parseInt(faulty_num) < Integer.parseInt(updateDataResponse.getRange())) {
                                    showDialog("You have " + faulty_num + " faulty devices.", 0);
                                    menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_warning_grey_24dp));
                                } else {
                                    //menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_warning_grey_24dp));
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        assert updateDataResponse != null;
                        Log.v("Response", updateDataResponse.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
            }
        });
    }

    void showDialog(String msg, int labl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.drawable.ic_warning_black_24dp);
        builder.setTitle("Faulty Devices");
        builder.setMessage(msg);
        if (labl == 0) {
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    //DO TASK
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void ShowProgressBar(boolean show) {
        try {
            if (show) {
                progressBars.setVisibility(View.VISIBLE);
            } else {
                progressBars.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    dashBoardFragment = new DashBoardFragment();
                    dashBoardFragment.setArguments(bundle);
                    return dashBoardFragment;
                case 1:
                    otherDashBoardFragment = new OtherDashBoardFragment();
                    otherDashBoardFragment.setArguments(bundle);
                    return otherDashBoardFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "My Dashboard";
                case 1:
                    return "Other's Dashboard";
            }
            return null;
        }
    }

    class ViewPagerAdapterAtd extends FragmentPagerAdapter {

        public ViewPagerAdapterAtd(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    activityLogFragment = new ActivityLogFragment();
                    activityLogFragment.setArguments(bundle);
                    return activityLogFragment;
                case 1:
                    viewActivityLogsFragment = new ViewActivityLogsFragment();
                    viewActivityLogsFragment.setArguments(bundle);
                    return viewActivityLogsFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Mark";
                case 1:
                    return "View Attendance";
            }
            return null;
        }
    }

    class ViewPagerAdapterStock extends FragmentPagerAdapter {

        public ViewPagerAdapterStock(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    stockFragment = new StockFragment();
                    stockFragment.setArguments(bundle);
                    return stockFragment;
                case 1:
                    viewStockFragment = new ViewStockFragment();
                    viewStockFragment.setArguments(bundle);
                    return viewStockFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Upload";
                case 1:
                    return "View";
            }
            return null;
        }
    }

    class ViewPagerReturnMaterial extends FragmentPagerAdapter {
        public ViewPagerReturnMaterial(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    materialReturnFragment = new in.eoninfotech.eontechnician.MaterialReturnFragment();
                    materialReturnFragment.setArguments(bundle);
                    return materialReturnFragment;

                case 1:
                    materialReturnView = new in.eoninfotech.eontechnician.MaterialReturnViews();
                    materialReturnView.setArguments(bundle);
                    return materialReturnView;
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Return To Eon";
                case 1:
                    return "View";
            }
            return null;
        }
    }

    public class ViewPagerSendToTechnician extends FragmentPagerAdapter {
        public ViewPagerSendToTechnician(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    materialtoTechFragment = new in.eoninfotech.eontechnician.MaterialtoTechFragment();
                    materialtoTechFragment.setArguments(bundle);
                    return materialtoTechFragment;

                case 1:
                    materialReturnView = new in.eoninfotech.eontechnician.MaterialReturnViews();
                    materialReturnView.setArguments(bundle);
                    return materialReturnView;
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Send To Technician";
                case 1:
                    return "View";
            }
            return null;
        }
    }

    class ViewPagerAdapterCallSheet extends FragmentPagerAdapter {

        public ViewPagerAdapterCallSheet(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    callSheetFragment = new CallSheetFragment();
                    callSheetFragment.setArguments(bundle);
                    return callSheetFragment;
                case 1:
                    viewCallSheetFragment = new ViewCallSheetFragment();
                    viewCallSheetFragment.setArguments(bundle);
                    return viewCallSheetFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Upload";
                case 1:
                    return "View";
            }
            return null;
        }
    }

    class ViewPagerAdapterActivity extends FragmentPagerAdapter {

        public ViewPagerAdapterActivity(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    //installmentFragment = new NewInstallmentFragment();
                    installmentFragment = new NewInstallmentFragmentUpdated();
                    installmentFragment.setArguments(bundle);
                    return installmentFragment;

                case 1:
                    activityDetailFragment = new ActivityDetailFragment();
                    activityDetailFragment.setArguments(bundle);
                    return activityDetailFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Update";
                case 1:
                    return "View";
            }
            return null;
        }
    }

    class ViewPagerAdapterBills extends FragmentPagerAdapter {

        public ViewPagerAdapterBills(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {


            switch (position) {
                case 0 -> {
                    billIntimationFragment = new BillIntimationFragment();
                    billIntimationFragment.setArguments(bundle);
                    return billIntimationFragment;
                }
                case 1 -> {
                    billViewFragment = new BillViewFragment();
                    billViewFragment.setArguments(bundle);
                    return billViewFragment;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Upload";
                case 1:
                    return "View";
            }
            return null;
        }
    }

     class ViewPagerAddRemoveUM extends FragmentPagerAdapter {

         public ViewPagerAddRemoveUM(FragmentManager fm) {
             super(fm);
         }

         @Override
         public Fragment getItem(int position) {

             switch (position) {
                 case 0 -> {
                     addUMFragment = new AddUMFragment();
                     addUMFragment.setArguments(bundle);
                     return addUMFragment;
                 }
                 case 1 -> {
                     removeUMFragment = new RemoveUMFragment();
                     removeUMFragment.setArguments(bundle);
                     return removeUMFragment;
                 }
             }
             return null;
         }

         @Override
         public int getCount() {
             return int_items;
         }

         @Override
         public CharSequence getPageTitle(int position) {

             switch (position) {
                 case 0:
                     return "Add UM";
                 case 1:
                     return "Remove UM";
             }
             return null;
         }
    }

    class ViewPagerAdapterAddMaterial extends FragmentPagerAdapter {

        public ViewPagerAdapterAddMaterial(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0 -> {
                    additionalMaterialFragment = new AdditionalMaterialFragment();
                    additionalMaterialFragment.setArguments(bundle);
                    return additionalMaterialFragment;
                }
                case 1 -> {
                    additionalMaterialViewFragment = new AdditionalMaterialViewFragment();
                    additionalMaterialViewFragment.setArguments(bundle);
                    return additionalMaterialViewFragment;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Upload";
                case 1:
                    return "View";
            }
            return null;
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //loadContent();
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Existing App Update Resume logic
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                try {
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            AppUpdateType.IMMEDIATE,
                                            MainActivity.this,
                                            UPDATE_REQUEST_CODE);
                                } catch (IntentSender.SendIntentException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });

        // ✅ Run heavy tasks AFTER UI loads
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            initializeFirebase();
            monitorConnectivity();
            checkLocation();
            getFaultyVts(0);
        }, 1000); // 1 second delay after screen visible
    }
}

