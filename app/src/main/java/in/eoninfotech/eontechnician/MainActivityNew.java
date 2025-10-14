package in.eoninfotech.eontechnician;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.Task;
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

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.eoninfotech.eontechnician.activity.Devicedashboards;
import in.eoninfotech.eontechnician.activity.LoginActivityNew;
import in.eoninfotech.eontechnician.activity.MessageActivity;
import in.eoninfotech.eontechnician.activity.ReceiveDeviceActivity;
import in.eoninfotech.eontechnician.fragments.ActivityDetailFragment;
import in.eoninfotech.eontechnician.fragments.ActivityLogFragment;
import in.eoninfotech.eontechnician.fragments.AddUMFragment;
import in.eoninfotech.eontechnician.fragments.AdditionalMaterialFragment;
import in.eoninfotech.eontechnician.fragments.AdditionalMaterialViewFragment;
import in.eoninfotech.eontechnician.fragments.BillIntimationFragment;
import in.eoninfotech.eontechnician.fragments.BillViewFragment;
import in.eoninfotech.eontechnician.fragments.CallSheetFragment;
import in.eoninfotech.eontechnician.fragments.DashBoardFragment;
import in.eoninfotech.eontechnician.fragments.FragmentIncentiveTab;
import in.eoninfotech.eontechnician.fragments.LiveStatusFragment;
import in.eoninfotech.eontechnician.fragments.LiveStatusFragmentEon;
import in.eoninfotech.eontechnician.fragments.NewInstallmentFragment;
import in.eoninfotech.eontechnician.fragments.OtherDashBoardFragment;
import in.eoninfotech.eontechnician.fragments.PaymentCollectionReportFragment;
import in.eoninfotech.eontechnician.fragments.RemoveUMFragment;
import in.eoninfotech.eontechnician.fragments.StockFragment;
import in.eoninfotech.eontechnician.fragments.ViewActivityLogsFragment;
import in.eoninfotech.eontechnician.fragments.ViewCallSheetFragment;
import in.eoninfotech.eontechnician.fragments.ViewStockFragment;
import in.eoninfotech.eontechnician.helper.ClientList;
import in.eoninfotech.eontechnician.helper.EONUtil;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.responses.TechnicianMonthDetail;
import in.eoninfotech.eontechnician.responses.TechnicianMonthResponse;
import in.eoninfotech.eontechnician.service.ForegroundService;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.MessageResponse;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import in.eoninfotech.eontechnician.webservice.TrackingDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityNew extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int PERMISSION_ALL = 1;
    private static final int REQUEST_CODE_PERMISSION = 1001;
    private static final int TAB_ITEMS_COUNT = 2;
    private static final String[] PERMISSIONS = {
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private final int UPDATE_REQUEST_CODE = 99;

    // UI Components
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TabLayout tabLayout;
    private AppUpdateManager appUpdateManager;
    private ViewPager viewPager, viewPagerAttendance, viewPagerActivity, viewPagerStock,viewPagertechnician,
            viewPagerCallSheet, viewPagerBill, viewPagerMaterialReturn,viewPagerAddMaterial;
    private FloatingActionButton fab, panicFab;
    private FrameLayout frame;
    private TextView textCartItemCount;
    private FrameLayout linearLayout;
    private ProgressBar progressBars;

    // Data
    private String username, version, displayUsername, image, imei, designationId = "0",intent_req = "TT";
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;
    private AppPreferences appPrefs;
    private Bundle bundle;
    private Menu menu;

    // Fragments
    private DashBoardFragment dashBoardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupToolbar();
        initializeData();
        setupNavigationDrawer();
        checkPermissionsAndLocation();
        setupViewPagers();
        loadInitialFragment();


        // Check permissions before starting
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    101);
        } else {
        }
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        linearLayout = findViewById(R.id.framelay);
        fab = findViewById(R.id.fab);
        panicFab = findViewById(R.id.panic_fab);
        frame = findViewById(R.id.frame);

        // ViewPagers
        viewPager = findViewById(R.id.viewpager);
        viewPagerAttendance = findViewById(R.id.viewpagerattendance);
        viewPagerActivity = findViewById(R.id.viewpageractivity);
        viewPagerStock = findViewById(R.id.viewpagerstock);
        viewPagerCallSheet = findViewById(R.id.viewpagercallsheet);
        viewPagerBill = findViewById(R.id.viewPagerBill);
        viewPagerMaterialReturn = findViewById(R.id.viewPagerMaterialReturn);
        viewPagerAddMaterial = findViewById(R.id.viewPagerAddMaterial);
        viewPagertechnician = findViewById(R.id.viewPagerSendtoTechnician);

        tabLayout = findViewById(R.id.tabs);

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        setTitle("Dashboard");
    }

    private void initializeData() {
        appPrefs = new AppPreferences(getApplicationContext());
        sharedPrefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedPrefs.edit();

        username = sharedPrefs.getString("s_uuser", "");
        version = sharedPrefs.getString("version", "");
        image = sharedPrefs.getString("image", "");
        displayUsername = sharedPrefs.getString("dis_user", "");
        designationId = sharedPrefs.getString("s_distt", "");
        imei = sharedPrefs.getString("imei1", "");

        bundle = new Bundle();
        bundle.putString("disttid", designationId);
        bundle.putString("usernme", username);
        bundle.putString("version", version);
        bundle.putString("image", image);

        setupFloatingActionButtons();
    }

    private void addDashboard() {
        showFragmentWithViewPager(new DashBoardFragment(), "Dashboard",
                new ViewPagerAdapter(getSupportFragmentManager()), viewPager);
    }

    private void sendToFT() {
        showFragmentWithViewPager(new in.eoninfotech.eontechnician.MaterialtoTechFragment(), "Send to Technician",
                new ViewPagerSendToTechnician(getSupportFragmentManager()), viewPagertechnician);
    }

    private void sendToEon() {
        showFragmentWithViewPager(new in.eoninfotech.eontechnician.MaterialReturnFragment(), "Return To Eon",
                new ViewPagerReturnMaterial(getSupportFragmentManager()), viewPagerMaterialReturn);
    }

    private void setupFloatingActionButtons() {
        fab.setOnClickListener(view -> {
            startActivity(new Intent(MainActivityNew.this, MessageActivity.class));
        });

        // Check for app updates
        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateManager.registerListener(installStateUpdatedListener);

        inAppUpdate();
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
                            MainActivityNew.this,
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

    private void setupNavigationDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Set navigation header
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView headerUsername = headerView.findViewById(R.id.header_username);
        headerUsername.setText(displayUsername.toUpperCase());

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void checkPermissionsAndLocation() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        checkLocationEnabled();
    }

    private void checkLocationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!gpsEnabled && !networkEnabled) {
            showLocationEnableDialog();
        }
    }

    private void showLocationEnableDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Your Location is OFF.")
                .setPositiveButton("Enable Location", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                })
                .setCancelable(false)
                .show();
    }

    private void setupViewPagers() {
        // Main Dashboard ViewPager
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        // Other ViewPagers will be initialized when needed
    }

    private void loadInitialFragment() {
        Intent intent1 = getIntent();
        try {
            intent_req = intent1.getStringExtra("intent");
        } catch (Exception e) {
            intent_req = "";
            throw new RuntimeException(e);
        }

        try {
            if (intent_req.equalsIgnoreCase("toEon")) {
                sendToEon();
            } else if (intent_req.equalsIgnoreCase("toFT")) {
                sendToFT();
            } else {
                dashBoardFragment = new DashBoardFragment();
                dashBoardFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.framelay, dashBoardFragment)
                        .commit();
            }
        } catch (Exception e) {
            dashBoardFragment = new DashBoardFragment();
            dashBoardFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.framelay, dashBoardFragment)
                    .commit();
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.faulty_status, menu);

        MenuItem menuItem = menu.findItem(R.id.msg_alert);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.textViewCounter);
        panicFab = actionView.findViewById(R.id.panic_fab);
        frame = actionView.findViewById(R.id.frame);

        setupBadge();

        actionView.setOnClickListener(v -> {
            startActivity(new Intent(MainActivityNew.this, MessageActivity.class));
        });

        return true;
    }

    private void setupBadge() {
        new Thread(this::loadContent).start();
    }

    private void loadContent() {
        ApiHolder api = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<MessageResponse> call = api.messageResponse(username, "0", "0", "");

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.body() != null && response.body().getType() == 1) {
                    String msgCount = response.body().getMsg_count();
                    runOnUiThread(() -> {
                        if (msgCount.equals("0")) {
                            panicFab.setVisibility(View.GONE);
                            frame.setVisibility(View.GONE);
                        } else {
                            panicFab.setVisibility(View.VISIBLE);
                            frame.setVisibility(View.VISIBLE);
                            textCartItemCount.setText(msgCount);
                        }
                    });
                } else {
                    runOnUiThread(() -> {
                        panicFab.setVisibility(View.GONE);
                        frame.setVisibility(View.GONE);
                    });
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                runOnUiThread(() -> {
                    panicFab.setVisibility(View.GONE);
                    frame.setVisibility(View.GONE);
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        hideKeyboard();

        switch (id) {
            case R.id.nav_dashboard:
                if (!designationId.equals("0")) {
                    showFragment(new FragmentIncentiveTab(), "Incentive");
                }
                break;

            case R.id.nav_mark_site:
                showFragmentWithViewPager(new ActivityLogFragment(), "Attendance Activity",
                        new ViewPagerAdapterAtd(getSupportFragmentManager()), viewPagerAttendance);
                break;

            case R.id.nav_tech_dashboard:
                showFragmentWithViewPager(new DashBoardFragment(), "Dashboard",
                        new ViewPagerAdapter(getSupportFragmentManager()), viewPager);
                break;

            case R.id.nav_incentive:
                showFragment(new in.eoninfotech.eontechnician.FragmentCurrentMonth(), "Incentive Activity");
                break;

            case R.id.nav_stock:
                showFragmentWithViewPager(new StockFragment(), "Stock Activity",
                        new ViewPagerAdapterStock(getSupportFragmentManager()), viewPagerStock);
                break;

            case R.id.nav_live_status:
                showFragment(new LiveStatusFragment(), "Live Status");
                break;

            case R.id.nav_live_status_eon:
                showFragment(new LiveStatusFragmentEon(), "Live Status Eon");
                break;

            case R.id.nav_new_repair:
                showFragmentWithViewPager(new NewInstallmentFragment(), "Activities",
                        new ViewPagerAdapterActivity(getSupportFragmentManager()), viewPagerActivity);
                break;

            case R.id.nav_call_sheet:
                showFragmentWithViewPager(new CallSheetFragment(), "Call Sheet",
                        new ViewPagerAdapterCallSheet(getSupportFragmentManager()), viewPagerCallSheet);
                break;

            case R.id.nav_pay_col_report:
                showFragment(new PaymentCollectionReportFragment(), "Payment Collection Report");
                break;

            case R.id.nav_bill:
                showFragmentWithViewPager(new BillIntimationFragment(), "Bill Intimation",
                        new ViewPagerAdapterBills(getSupportFragmentManager()), viewPagerBill);
                break;

            case R.id.nav_material_return:
//                showFragmentWithViewPager(new in.eoninfotech.eontechnician.MaterialReturnFragment(), "Return To Eon",
//                        new ViewPagerReturnMaterial(getSupportFragmentManager()), viewPagerMaterialReturn);
                break;

            case R.id.nav_additional_material:
                showFragmentWithViewPager(new AdditionalMaterialFragment(), "Service Stock Demand",
                        new ViewPagerAddMaterial(getSupportFragmentManager()), viewPagerAddMaterial);
                break;

            case R.id.nav_material_send_to_tech:
//                showFragmentWithViewPager(new in.eoninfotech.eontechnician.MaterialtoTechFragment(), "Send to Technician",
//                        new ViewPagerSendToTechnician(getSupportFragmentManager()), viewPagertechnician);
                break;

            case R.id.nav_material:
                startActivity(new Intent(this, ReceiveDeviceActivity.class));
                break;
            case R.id.material_dashboard:
                showFragment(new DeviceDashboardFragment(), "Live Inventory Dashboard");
                break;

            case R.id.menu_logout:
                showLogoutConfirmation();
                break;

            case R.id.nav_technician_of_the_month:
                showTechnicianOfTheMonthDialog();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(Fragment fragment, String title) {

//        if (fragment == null) return;
//
//        fragment.setArguments(bundle); // If you have data to pass
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.framelay, fragment);
//        ft.commitAllowingStateLoss(); // safer than commit()
//
//        setTitle(title);
//        hideAllViewPagers(); // hide your other ViewPagers if needed
//        tabLayout.setVisibility(View.GONE);

//        if (fragment == null) return;
//
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.framelay, fragment);
//        ft.commitAllowingStateLoss(); // safer than commit()
//
//        // Ensure container is visible
//        FrameLayout container = findViewById(R.id.framelay);
//        container.setVisibility(View.VISIBLE);
//
//        // Set title
//        setTitle(title);

        // Hide all ViewPagers (optional)

        if (fragment == null) return;

        // Pass bundle if needed
        fragment.setArguments(bundle);

        // Hide all ViewPagers first
        hideAllViewPagers();

        // Hide TabLayout
        if (tabLayout != null) tabLayout.setVisibility(View.GONE);

        // Show fragment container
        FrameLayout container = findViewById(R.id.framelay);
        container.setVisibility(View.VISIBLE);

        // Replace fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framelay, fragment)
                .commitAllowingStateLoss();

        setTitle(title);

        tabLayout.setVisibility(View.GONE);

    }

    private void showFragmentWithViewPager(Fragment fragment, String title,
                                           FragmentPagerAdapter adapter, ViewPager viewPagerToShow) {

        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentById(R.id.framelay);

        // ✅ Prevent reloading if the same fragment is already visible
        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
            // Still make sure the correct ViewPager is visible
            hideAllViewPagers();
            viewPagerToShow.setVisibility(View.VISIBLE);
            viewPagerToShow.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPagerToShow);
            tabLayout.setVisibility(View.VISIBLE);
            return;
        }

        // Set arguments if needed
        fragment.setArguments(bundle);

        // Replace fragment
        fm.beginTransaction()
                .replace(R.id.framelay, fragment)
                .commit();

        // Update UI
        setTitle(title);
        hideAllViewPagers();
        viewPagerToShow.setVisibility(View.VISIBLE);
        viewPagerToShow.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPagerToShow);
        tabLayout.setVisibility(View.VISIBLE);

    }

    private void hideAllViewPagers() {
//        viewPager.setVisibility(View.GONE);
//        viewPagerAttendance.setVisibility(View.GONE);
//        viewPagerActivity.setVisibility(View.GONE);
//        viewPagerCallSheet.setVisibility(View.GONE);
//        viewPagerStock.setVisibility(View.GONE);
//        viewPagerBill.setVisibility(View.GONE);
//        viewPagerMaterialReturn.setVisibility(View.GONE);
//        viewPagerAddMaterial.setVisibility(View.GONE);
//        viewPagertechnician.setVisibility(View.GONE);

        if (viewPager != null) viewPager.setVisibility(View.GONE);
        if (viewPagerAttendance != null) viewPagerAttendance.setVisibility(View.GONE);
        if (viewPagerActivity != null) viewPagerActivity.setVisibility(View.GONE);
        if (viewPagerCallSheet != null) viewPagerCallSheet.setVisibility(View.GONE);
        if (viewPagerStock != null) viewPagerStock.setVisibility(View.GONE);
        if (viewPagerBill != null) viewPagerBill.setVisibility(View.GONE);
        if (viewPagerMaterialReturn != null) viewPagerMaterialReturn.setVisibility(View.GONE);
        if (viewPagerAddMaterial != null) viewPagerAddMaterial.setVisibility(View.GONE);
        if (viewPagertechnician != null) viewPagertechnician.setVisibility(View.GONE);

    }

    private void showLogoutConfirmation() {
//        boolean isServiceRunning = EONUtil.isServiceRunning(this, ForegroundService.class);
//        String running = String.valueOf(isServiceRunning);

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> performLogout())
                .setNegativeButton("No", null)
                .show();
    }

    private void performLogout() {
        editor.putString("s_uuser", "");
        editor.putString("pass", "");
        editor.putString("logout", "logout");
        //editor.putString("isRunning", isRunning);
        appPrefs.setLoggedIn(false);
        editor.apply();

        startActivity(new Intent(this, LoginActivityNew.class)
                .putExtra("username", "us"));
        finish();
    }

    private void showTechnicianOfTheMonthDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.technician_monthpopup);

        TextView txtClose = dialog.findViewById(R.id.txtclose);
        TextView month = dialog.findViewById(R.id.technician_month);
        TextView name = dialog.findViewById(R.id.tech_name);
        TextView loc = dialog.findViewById(R.id.tech_loc);
        CircleImageView techImg = dialog.findViewById(R.id.ivProfile);
        TextView viewMore = dialog.findViewById(R.id.viewMore);

        txtClose.setOnClickListener(v -> dialog.dismiss());
        viewMore.setOnClickListener(view -> {
            startActivity(new Intent(this, in.eoninfotech.eontechnician.CardViewActivity.class));
            dialog.dismiss();
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

        loadTechnicianOfTheMonthData(month, name, loc, techImg);
    }

    private void loadTechnicianOfTheMonthData(TextView monthView, TextView nameView,
                                              TextView locView, CircleImageView imageView) {
        ShowProgressBar(true);

        ApiHolder api = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<TechnicianMonthResponse> call = api.requestTechnicianoftheMonth();

        call.enqueue(new Callback<TechnicianMonthResponse>() {
            @Override
            public void onResponse(Call<TechnicianMonthResponse> call,
                                   Response<TechnicianMonthResponse> response) {
                if (response.body() != null && !response.body().getTechList().isEmpty()) {
                    TechnicianMonthDetail detail = response.body().getTechList().get(0);

                    monthView.setText(detail.getMonth() + " " + detail.getYear());
                    nameView.setText(detail.getName());
                    locView.setText(detail.getLocation());

                    String imageUri = K.Url.IMAGE_URL + detail.getImage();
                    if (imageUri.isEmpty()) {
                        imageView.setImageResource(R.drawable.user);
                    } else {
                        ImageUtils.glideImage(imageView, imageUri, R.drawable.user);
                    }
                }
                ShowProgressBar(false);
            }

            @Override
            public void onFailure(Call<TechnicianMonthResponse> call, Throwable t) {
                ShowProgressBar(false);
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void ShowProgressBar(boolean show) {
        progressBars.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    // ViewPager Adapters
    static class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            return switch (position) {
                case 0 -> new DashBoardFragment();
                case 1 -> new OtherDashBoardFragment();
                default -> null;
            };
        }

        @Override
        public int getCount() {
            return TAB_ITEMS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return switch (position) {
                case 0 -> "My Dashboard";
                case 1 -> "Other's Dashboard";
                default -> null;
            };
        }
    }

    static class ViewPagerReturnMaterial extends FragmentPagerAdapter {
        public ViewPagerReturnMaterial(FragmentManager fm) {
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {

            return switch (position) {
                case 0 -> new in.eoninfotech.eontechnician.MaterialReturnFragment();
                case 1 -> new in.eoninfotech.eontechnician.MaterialReturnViews();
                default -> null;
            };
        }

        @Override
        public int getCount() {
            return TAB_ITEMS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return switch (position) {
                case 0 -> "Return to Eon";
                case 1 -> "View";
                default -> null;
            };
        }
    }

    static class ViewPagerAddMaterial extends FragmentPagerAdapter {
        public ViewPagerAddMaterial(FragmentManager fm) {
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {

            return switch (position) {
                case 0 -> new AdditionalMaterialFragment();
                case 1 -> new AdditionalMaterialViewFragment();
                default -> null;
            };
        }

        @Override
        public int getCount() {
            return TAB_ITEMS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return switch (position) {
                case 0 -> "Upload";
                case 1 -> "View";
                default -> null;
            };
        }
    }

    static class ViewPagerAdapterCallSheet extends FragmentPagerAdapter {
        public ViewPagerAdapterCallSheet(FragmentManager fm) {
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return switch (position) {
                case 0 -> new CallSheetFragment();
                case 1 -> new ViewCallSheetFragment();
                default -> null;
            };
        }

        @Override
        public int getCount() {
            return TAB_ITEMS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return switch (position) {
                case 0 -> "Upload";
                case 1 -> "View";
                default -> null;
            };
        }
    }

    static class ViewPagerAdapterBills extends FragmentPagerAdapter {
        public ViewPagerAdapterBills(FragmentManager fm) {
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        @Override
        public Fragment getItem(int position) {

            return switch (position) {
                case 0 -> new BillIntimationFragment();
                case 1 -> new BillViewFragment();
                default -> null;
            };
        }

        @Override
        public int getCount() {
            return TAB_ITEMS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return switch (position) {
                case 0 -> "Upload";
                case 1 -> "View";
                default -> null;
            };
        }
    }

    static class ViewPagerAdapterStock extends FragmentPagerAdapter {
        public ViewPagerAdapterStock(FragmentManager fm) {
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        @Override
        public Fragment getItem(int position) {

            return switch (position) {
                case 0 -> new StockFragment();
                case 1 -> new ViewStockFragment();
                default -> null;
            };
        }

        @Override
        public int getCount() {
            return TAB_ITEMS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return switch (position) {
                case 0 -> "Upload";
                case 1 -> "View";
                default -> null;
            };
        }
    }

    static class ViewPagerAdapterActivity extends FragmentPagerAdapter {
        public ViewPagerAdapterActivity(FragmentManager fm) {
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        @Override
        public Fragment getItem(int position) {

            return switch (position) {
                case 0 -> new NewInstallmentFragment();
                case 1 -> new ActivityDetailFragment();
                default -> null;
            };
        }

        @Override
        public int getCount() {
            return TAB_ITEMS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return switch (position) {
                case 0 -> "Upload";
                case 1 -> "View";
                default -> null;
            };
        }
    }

    static class ViewPagerSendToTechnician extends FragmentPagerAdapter {
        public ViewPagerSendToTechnician(FragmentManager fm) {
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        @Override
        public Fragment getItem(int position) {

            return switch (position) {
                case 0 -> new in.eoninfotech.eontechnician.MaterialtoTechFragment();
                case 1 -> new in.eoninfotech.eontechnician.MaterialReturnViews();
                default -> null;
            };
        }

        @Override
        public int getCount() {
            return TAB_ITEMS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return switch (position) {
                case 0 -> "Send to Technician";
                case 1 -> "View";
                default -> null;
            };
        }
    }

    static class ViewPagerAdapterAtd extends FragmentPagerAdapter {
        public ViewPagerAdapterAtd(FragmentManager fm) {
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        @Override
        public Fragment getItem(int position) {
            return switch (position) {
                case 0 -> new ActivityLogFragment();
                case 1 -> new ViewActivityLogsFragment();
                default -> null;
            };

        }

        @Override
        public int getCount() {
            return TAB_ITEMS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return switch (position) {
                case 0 -> "Mark";
                case 1 -> "View Attendance";
                default -> null;
            };
        }
    }
// Other ViewPager adapters follow similar pattern...
// Implement other adapters (ViewPagerAdapterAtd, ViewPagerAdapterStock, etc.) similarly

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadContent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
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

}
