package in.eoninfotech.eontechnician;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

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
import de.hdodenhof.circleimageview.CircleImageView;
import in.eoninfotech.eontechnician.Responses.TechnicianMonthDetail;
import in.eoninfotech.eontechnician.Responses.TechnicianMonthResponse;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.Service.AlarmService;
import in.eoninfotech.eontechnician.Service.ForegroundService;
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
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.MessageResponse;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import in.eoninfotech.eontechnician.webservice.TrackingDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fm;
    FragmentTransaction ft;
    String username, version, dis_username, image;
    Toolbar toolbar;
    ArrayList<ClientList> ClientLists = new ArrayList<ClientList>();
    AppPreferences appPrefs;
    FloatingActionButton fab, panic_fab;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    FragmentCurrentMonth fragmentCurrentMonth;
    String usrname, alertt, uusername, versionname, disgnid = "0", activityName = "Activities";
    NavigationView navigationView;
    Bundle bundle;
    FaqsFragment faqsfragment;
    ActivityDetailFragment activityDetailFragment;
    ActivityLogFragment activityLogFragment;
    NewInstallmentFragment installmentFragment;
    LiveStatusFragment liveStatusFragment;
    StockFragment stockFragment;
    TextView textCartItemCount;
    PaymentCollectionReportFragment paymentCollectionReportFragment;
    MaterialReturnFragment materialReturnFragment;
    MaterialtoTechFragment materialtoTechFragment;
    MaterialReturnViews materialReturnView;
    BillIntimationFragment billIntimationFragment;
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
    ViewPagerAdapterDashboard viewPagerAdapterDashboard;
    ViewPagerAdapterActivity viewPagerAdapterActivity;
    ViewPagerAdapterBills viewPagerAdapterBills;
    ViewPagerReturnMaterial viewPagerReturnMaterial;
    ViewPagerSendToTechnician viewPagerSendtoTechnician;
    ArrayList<TrackingDetail> trackingDetails = new ArrayList<>();
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    int REQUEST_CODE_PERMISSION = 1001;
    DownloadManager manager;
    public static final String downloadMp3Url = "http://mail.cybernetra.net:8080/android/eonApp/2.7.1/song.mp3";
    MediaPlayer mp = new MediaPlayer();
    private Menu menu;
    String faulty_num, imei;
    Dialog myDialog;
    RelativeLayout progressBar;
    TextView txtclose, month, name, loc, viewMore;
    CircleImageView tech_img;
    ProgressBar progressBars;
    ArrayList<TechnicianMonthDetail> techList = new ArrayList<>();
    public static TabLayout tabLayout;
    public static int int_items = 2;
    private ViewPager viewPager, viewpagerattendance, viewpageractivity, viewpagerstock, viewpagercallsheet,viewPagerBill,viewPagerMaterialReturn,viewPagertechnician;
    String tab = "1";
    private String currentVersion;
    LinearLayout linearLayout;
    int id = 0;
    String s_date = "0", status = "0";
    String msg_type = "";
    ArrayList<MessageResponse> messageResponses = new ArrayList<>();
    FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle("Dashboard");
        linearLayout = findViewById(R.id.framelay);
        appPrefs = new AppPreferences(getApplicationContext());
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        uusername = sharedprefs.getString("s_uuser", "");
        versionname = sharedprefs.getString("version", "");
        image = sharedprefs.getString("image", "");
        alertt = sharedprefs.getString("alert", "");
        usrname = sharedprefs.getString("s_uuser", "");
        disgnid = sharedprefs.getString("s_distt", "");
        username = sharedprefs.getString("s_uuser", "");
        dis_username = sharedprefs.getString("dis_user", "");
        imei = sharedprefs.getString("imei1", "");
        fm = getSupportFragmentManager();
        bundle = new Bundle();
        bundle.putString("disttid", disgnid);
        bundle.putString("usernme", usrname);
        bundle.putString("version", versionname);
        bundle.putString("image", image);
        //handler.post(timedTask);
        try {
            TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(MainActivity.this);
            imei = telephonyInfo.getImsiSIM1();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            new android.app.AlertDialog.Builder(this)
                    .setMessage("Your Location is OFF.")
                    .setPositiveButton("Enable Location", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent dialogIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(dialogIntent);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
        fab = findViewById(R.id.fab);
        panic_fab = findViewById(R.id.panic_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(intent);
            }
        });
        //serviceStart();
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        new GetVersionCode().execute();
        myDialog = new Dialog(this);
        dashBoardFragment = new DashBoardFragment();
        dashBoardFragment.setArguments(bundle);
        ft = fm.beginTransaction().add(R.id.framelay, dashBoardFragment);
        ft.commit();
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
        //serviceStop();
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
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
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

//    private void serviceStop() {
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 19);
//        calendar.set(Calendar.MINUTE, 59);
//        calendar.set(Calendar.SECOND, 59);
//        Intent intent1 = new Intent(MainActivity.this, StopService.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent1, PendingIntent.FLAG_MUTABLE);
//        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//    }
//
//    private void serviceStart() {
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 8);
//        calendar.set(Calendar.MINUTE, 00);
//        calendar.set(Calendar.SECOND, 00);
//        Intent intent1 = new Intent(MainActivity.this, AlarmService.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent1, PendingIntent.FLAG_MUTABLE);
//        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//    }

    private void loadContent() {

        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<MessageResponse> call = log_att.messageResponse(uusername, s_date, status, msg_type);
        call.enqueue(new Callback<MessageResponse>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.body().getType() == 1) {
                    MessageResponse activityResponse = response.body();
                    if (activityResponse.getMsg_count().equals("0")) {
                        panic_fab.setVisibility(View.GONE);
                        frame.setVisibility(View.GONE);
                    } else {
                        panic_fab.setVisibility(View.VISIBLE);
                        frame.setVisibility(View.VISIBLE);
                        textCartItemCount.setText((CharSequence) activityResponse.getMsg_count());
                    }
                } else {
                    panic_fab.setVisibility(View.GONE);
                    frame.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
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
        setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    private void setupBadge() {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                loadContent();
            }
        });
        thread.start();
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            if (disgnid.equals("0")) {
            } else {
                FragmentIncentiveTab fragmentIncentiveTab;
                fragmentIncentiveTab = new FragmentIncentiveTab();
                fragmentIncentiveTab.setArguments(bundle);
                ft = fm.beginTransaction().replace(R.id.framelay, fragmentIncentiveTab);
            }
            setTitle("Incentive");
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            ft.commit();
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_mark_site) {
            activityLogFragment = new ActivityLogFragment();
            activityLogFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, activityLogFragment);
            setTitle("Attendance Activity");
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.removeAllTabs();
            viewpagerattendance.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            viewpageractivity.setVisibility(View.GONE);
            viewpagercallsheet.setVisibility(View.GONE);
            viewpagerstock.setVisibility(View.GONE);
            viewPagerBill.setVisibility(View.GONE);
            viewPagerMaterialReturn.setVisibility(View.GONE);
            viewPagerAdapterAtd = new ViewPagerAdapterAtd(getSupportFragmentManager());
            viewpagerattendance.setAdapter(viewPagerAdapterAtd);
            tabLayout.setupWithViewPager(viewpagerattendance);
            ft.commit();
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            hideKeyboard();
        } else if (id == R.id.nav_tech_dashboard) {
            dashBoardFragment = new DashBoardFragment();
            dashBoardFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, dashBoardFragment);
            setTitle("Dashboard");
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            viewpagerattendance.setVisibility(View.GONE);
            viewpageractivity.setVisibility(View.GONE);
            viewpagercallsheet.setVisibility(View.GONE);
            viewPagerBill.setVisibility(View.GONE);
            viewPagerMaterialReturn.setVisibility(View.GONE);
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
            ft.commit();
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            hideKeyboard();
        } else if (id == R.id.nav_incentive) {
            fragmentCurrentMonth = new FragmentCurrentMonth();
            fragmentCurrentMonth.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, fragmentCurrentMonth);
            setTitle("Incentive Activity");
            tabLayout.setVisibility(View.GONE);
            tabLayout.removeAllTabs();
            viewpagerattendance.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            viewpageractivity.setVisibility(View.GONE);
            viewpagercallsheet.setVisibility(View.GONE);
            viewpagerstock.setVisibility(View.GONE);
            viewPagerBill.setVisibility(View.GONE);
            viewPagerMaterialReturn.setVisibility(View.GONE);
            viewPagertechnician.setVisibility(View.GONE);
            ft.commit();
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            hideKeyboard();
        } else if (id == R.id.nav_stock) {
            stockFragment = new StockFragment();
            stockFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, stockFragment);
            setTitle("Stock Activity");
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.removeAllTabs();
            viewpagerstock.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            viewpagerattendance.setVisibility(View.GONE);
            viewpageractivity.setVisibility(View.GONE);
            viewpagercallsheet.setVisibility(View.GONE);
            viewPagerBill.setVisibility(View.GONE);
            viewPagerMaterialReturn.setVisibility(View.GONE);
            viewPagerAdapterStock = new ViewPagerAdapterStock(getSupportFragmentManager());
            viewpagerstock.setAdapter(viewPagerAdapterStock);
            tabLayout.setupWithViewPager(viewpagerstock);
            ft.commit();
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            hideKeyboard();
        } else if (id == R.id.nav_live_status) {
            liveStatusFragment = new LiveStatusFragment();
            liveStatusFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, liveStatusFragment);
            setTitle("Live Status");
            tabLayout.setVisibility(View.GONE);
            viewpagerattendance.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            viewpageractivity.setVisibility(View.GONE);
            viewpagercallsheet.setVisibility(View.GONE);
            viewpagerstock.setVisibility(View.GONE);
            viewPagerBill.setVisibility(View.GONE);
            viewPagerMaterialReturn.setVisibility(View.GONE);
            viewPagertechnician.setVisibility(View.GONE);
            ft.commit();
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            hideKeyboard();
        } else if (id == R.id.nav_new_repair) {
            installmentFragment = new NewInstallmentFragment();
            installmentFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, installmentFragment);
            setTitle("Activities");
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.removeAllTabs();
            viewpagerattendance.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            viewpageractivity.setVisibility(View.VISIBLE);
            viewpagercallsheet.setVisibility(View.GONE);
            viewpagerstock.setVisibility(View.GONE);
            viewPagerBill.setVisibility(View.GONE);
            viewPagerMaterialReturn.setVisibility(View.GONE);
            viewPagerAdapterActivity = new ViewPagerAdapterActivity(getSupportFragmentManager());
            viewpageractivity.setAdapter(viewPagerAdapterActivity);
            tabLayout.setupWithViewPager(viewpageractivity);
            ft.commit();
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            hideKeyboard();
        } else if (id == R.id.nav_call_sheet) {
            callSheetFragment = new CallSheetFragment();
            callSheetFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, callSheetFragment);
            setTitle("Call Sheet");
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.removeAllTabs();
            viewpagerattendance.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            viewpageractivity.setVisibility(View.GONE);
            viewpagercallsheet.setVisibility(View.VISIBLE);
            viewpagerstock.setVisibility(View.GONE);
            viewPagerBill.setVisibility(View.GONE);
            viewPagerMaterialReturn.setVisibility(View.GONE);
            viewPagerAdapterCallSheet = new ViewPagerAdapterCallSheet(getSupportFragmentManager());
            viewpagercallsheet.setAdapter(viewPagerAdapterCallSheet);
            tabLayout.setupWithViewPager(viewpagercallsheet);
            ft.commit();
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            hideKeyboard();
        } else if (id == R.id.nav_pay_col_report) {
            paymentCollectionReportFragment = new PaymentCollectionReportFragment();
            paymentCollectionReportFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, paymentCollectionReportFragment);
            setTitle("Payment Collection Report");
            tabLayout.setVisibility(View.GONE);
            tabLayout.removeAllTabs();
            viewpagerattendance.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            viewpageractivity.setVisibility(View.GONE);
            viewpagercallsheet.setVisibility(View.GONE);
            viewpagerstock.setVisibility(View.GONE);
            viewPagerBill.setVisibility(View.GONE);
            viewPagerMaterialReturn.setVisibility(View.GONE);
            viewPagerAdapterCallSheet = new ViewPagerAdapterCallSheet(getSupportFragmentManager());
            viewpagercallsheet.setAdapter(viewPagerAdapterCallSheet);
            tabLayout.setupWithViewPager(viewpagercallsheet);
            ft.commit();
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            hideKeyboard();
        } else if (id == R.id.nav_bill) {
            billIntimationFragment = new BillIntimationFragment();
            billIntimationFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, billIntimationFragment);
            setTitle("Bill Intimation");
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.removeAllTabs();
            viewpagerattendance.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            viewpageractivity.setVisibility(View.GONE);
            viewpagercallsheet.setVisibility(View.GONE);
            viewpagerstock.setVisibility(View.GONE);
            viewPagerBill.setVisibility(View.VISIBLE);
            viewPagerMaterialReturn.setVisibility(View.GONE);
            viewPagertechnician.setVisibility(View.GONE);
            viewPagerAdapterBills = new ViewPagerAdapterBills(getSupportFragmentManager());
            viewPagerBill.setAdapter(viewPagerAdapterBills);
            tabLayout.setupWithViewPager(viewPagerBill);
            ft.commit();
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            hideKeyboard();
        }
        else if (id == R.id.nav_material_return) {
            materialReturnFragment = new MaterialReturnFragment();
            materialReturnFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, materialReturnFragment);
            setTitle("Return Material");
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
        } else if (id == R.id.nav_material_send_to_tech) {
            materialReturnFragment = new MaterialReturnFragment();
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
            viewPagertechnician.setVisibility(View.VISIBLE);
            viewPagerSendtoTechnician = new ViewPagerSendToTechnician(getSupportFragmentManager());
            viewPagertechnician.setAdapter(viewPagerSendtoTechnician);
            tabLayout.setupWithViewPager(viewPagertechnician);
            ft.commit();
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            hideKeyboard();
        }
        
        else if (id == R.id.nav_material) {
           Intent intent = new Intent(MainActivity.this, ReceiveDeviceActivity.class);
           startActivity(intent);
        }
//        else if (id == R.id.faqs) {
//            faqsfragment = new FaqsFragment();
//            faqsfragment.setArguments(bundle);
//            ft = fm.beginTransaction().replace(R.id.framelay, faqsfragment);
//            setTitle("FAQs");
//            tabLayout.setVisibility(View.GONE);
//            tabLayout.removeAllTabs();
//            viewpagerattendance.setVisibility(View.GONE);
//            viewPager.setVisibility(View.GONE);
//            viewpageractivity.setVisibility(View.GONE);
//            viewpagercallsheet.setVisibility(View.GONE);
//            viewpagerstock.setVisibility(View.GONE);
//            viewPagerAdapterCallSheet = new ViewPagerAdapterCallSheet(getSupportFragmentManager());
//            viewpagercallsheet.setAdapter(viewPagerAdapterCallSheet);
//            tabLayout.setupWithViewPager(viewpagercallsheet);
//            ft.commit();
//            DrawerLayout drawer = findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
//            hideKeyboard();
//        }
        else if (id == R.id.menu_logout) {
            final boolean isRunning = EONUtil.isServiceRunning(this.getBaseContext(), ForegroundService.class);
            String running = String.valueOf(isRunning);
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Confirm Logout")
                    .setMessage("Are you sure you want to logout ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (isRunning) {
                                Intent inteer = new Intent(MainActivity.this, LoginActivityNew.class);
                                inteer.putExtra("username", "us");
                                editor.putString("s_uuser", "");
                                editor.putString("pass", "");
                                editor.putString("logout", "logout");
                                editor.putString("isRunning", running);
                                appPrefs.setLoggedIn(false);
                                editor.commit();
                                startActivity(inteer);
//                                Intent stopIntent = new Intent(MainActivity.this, ForegroundService.class);
//                                stopIntent.putExtra("param_name", "end");
//                                getBaseContext().stopService(stopIntent);
                                finish();
                            } else {
                                Intent inteer = new Intent(MainActivity.this, LoginActivityNew.class);
                                inteer.putExtra("username", "us");
                                editor.putString("s_uuser", "");
                                editor.putString("pass", "");
                                editor.putString("logout", "logout");
                                editor.putString("isRunning", "");
                                appPrefs.setLoggedIn(false);
                                editor.commit();
                                startActivity(inteer);
//                                Intent stopIntent = new Intent(MainActivity.this, ForegroundService.class);
//                                stopIntent.putExtra("param_name", "end");
//                                getBaseContext().stopService(stopIntent);
                                finish();
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else if (id == R.id.nav_technician_of_the_month) {
            myDialog.setContentView(R.layout.technician_monthpopup);
            txtclose = myDialog.findViewById(R.id.txtclose);
            month = myDialog.findViewById(R.id.technician_month);
            name = myDialog.findViewById(R.id.tech_name);
            loc = myDialog.findViewById(R.id.tech_loc);
            tech_img = myDialog.findViewById(R.id.ivProfile);
            progressBars = findViewById(R.id.progressBars);
            viewMore = myDialog.findViewById(R.id.viewMore);
            txtclose.setOnClickListener(v -> myDialog.dismiss());

            viewMore.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), CardViewActivity.class);
                startActivity(intent);
                myDialog.hide();
            });
            myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            getDetail();
            hideKeyboard();
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Override this method in the activity that hosts the Fragment and call super
        // in order to receive the result inside onActivityResult from the fragment.
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
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
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {

            bundle.putString("disttid", disgnid);
            bundle.putString("usernme", usrname);
            bundle.putString("version", versionname);

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
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {

            bundle.putString("disttid", disgnid);
            bundle.putString("usernme", usrname);
            bundle.putString("version", versionname);

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
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {

            bundle.putString("disttid", disgnid);
            bundle.putString("usernme", usrname);
            bundle.putString("version", versionname);

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
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        @Override
        public Fragment getItem(int position) {

            bundle.putString("disttid", disgnid);
            bundle.putString("usernme", usrname);
            bundle.putString("version", versionname);

            switch (position) {
                case 0:
                    materialReturnFragment = new MaterialReturnFragment();
                    materialReturnFragment.setArguments(bundle);
                    return materialReturnFragment;

                case 1:
                    materialReturnView = new MaterialReturnViews();
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

    class ViewPagerSendToTechnician extends FragmentPagerAdapter {
        public ViewPagerSendToTechnician(FragmentManager fm) {
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        @Override
        public Fragment getItem(int position) {

            bundle.putString("disttid", disgnid);
            bundle.putString("usernme", usrname);
            bundle.putString("version", versionname);

            switch (position) {
                case 0:
                    materialtoTechFragment = new MaterialtoTechFragment();
                    materialtoTechFragment.setArguments(bundle);
                    return materialtoTechFragment;

                case 1:
                    materialReturnView = new MaterialReturnViews();
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
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {

            bundle.putString("disttid", disgnid);
            bundle.putString("usernme", usrname);
            bundle.putString("version", versionname);

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
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {

            bundle.putString("disttid", disgnid);
            bundle.putString("usernme", usrname);
            bundle.putString("version", versionname);
            switch (position) {
                case 0:
                    installmentFragment = new NewInstallmentFragment();
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

    class ViewPagerAdapterDashboard extends FragmentPagerAdapter {

        public ViewPagerAdapterDashboard(FragmentManager fm) {
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {

            bundle.putString("disttid", disgnid);
            bundle.putString("usernme", usrname);
            bundle.putString("version", versionname);

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

    class ViewPagerAdapterBills extends FragmentPagerAdapter {

        public ViewPagerAdapterBills(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            bundle.putString("disttid", disgnid);
            bundle.putString("usernme", usrname);
            bundle.putString("version", versionname);

            switch (position) {
                case 0:
                    billIntimationFragment = new BillIntimationFragment();
                    billIntimationFragment.setArguments(bundle);
                    return billIntimationFragment;
                case 1:
                    billViewFragment = new BillViewFragment();
                    billViewFragment.setArguments(bundle);
                    return billViewFragment;
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

    class GetVersionCode extends AsyncTask<Void, String, String> {

        Dialog dialog;

        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;
            try {
                org.jsoup.nodes.Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (org.jsoup.nodes.Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (org.jsoup.nodes.Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;
        }

        @Override
        protected void onPostExecute(String onlineVersion) {

            super.onPostExecute(onlineVersion);

            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                    Snackbar snackbar = Snackbar.make(linearLayout, "New Version Available, Kindly Update App from Play store", Snackbar.LENGTH_LONG)
                            .setAction("UPDATE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                                            ("https://play.google.com/store/apps/details?id=in.eoninfotech.eontechnician")));
                                }
                            });
                    snackbar.setActionTextColor(Color.WHITE);
                    snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                }
            }
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        loadContent();
    }

    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
    }
}
