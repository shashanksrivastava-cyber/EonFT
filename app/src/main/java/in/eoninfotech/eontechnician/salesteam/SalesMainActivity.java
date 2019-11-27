package in.eoninfotech.eontechnician.salesteam;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import in.eoninfotech.eontechnician.AppPreferences;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.activity.LoginActivityNew;
import in.eoninfotech.eontechnician.adminuser.ChangePasswordFragment;


public class SalesMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fm;
    FragmentTransaction ft;
    Toolbar toolbar;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String usrname, alertt, uusername, versionname, disgnid = "0";
    NavigationView navigationView;
    Bundle bundle;
    SalesDashboardFragment dashboardFragment;
    ChangePasswordFragment changePasswordFragment;
    SalesViewEntryFragment salesViewEntryFragment;
    int PERMISSION_ALL = 1;
    AppPreferences appPrefs;
    String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    int REQUEST_CODE_PERMISSION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Dashboard");
        appPrefs = new AppPreferences(getApplicationContext());
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        uusername = sharedprefs.getString("s_uuser", "");
        versionname = sharedprefs.getString("version", "");
        Log.i("******shared******", uusername + versionname);
        alertt = sharedprefs.getString("alert", "");
        usrname = sharedprefs.getString("s_uuser", "");
        disgnid = sharedprefs.getString("s_distt", "");
        fm = getSupportFragmentManager();
        bundle = new Bundle();
        bundle.putString("disttid", disgnid);
        bundle.putString("usernme", uusername);
        bundle.putString("version", versionname);

       /* try{
         String view_s = getIntent().getStringExtra("view");
        }catch (Exception e) {*/
            dashboardFragment = new SalesDashboardFragment();
            dashboardFragment.setArguments(bundle);
            ft = fm.beginTransaction().add(R.id.framelay, dashboardFragment);
            ft.commit();
       // }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView headr_usrnam = (TextView) view.findViewById(R.id.header_username);
        headr_usrnam.setText(usrname.toUpperCase());

        try {
            //checkWritingPermission();
            if (!hasPermissions(SalesMainActivity.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(SalesMainActivity.this, PERMISSIONS, PERMISSION_ALL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.menu_logout) {
//            Intent inteer = new Intent(SalesMainActivity.this, LoginActivityNew.class);
//            inteer.putExtra("username", "us");
//            editor.putString("pass", "");
//            editor.commit();
//            startActivity(inteer);
//            finish();
//        }
//
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.sales_nav_dashboard) {
            dashboardFragment = new SalesDashboardFragment();
            dashboardFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, dashboardFragment);
            setTitle("Dashboard");
            ft.commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.sales_nav_requirement) {
            Intent intent = new Intent(SalesMainActivity.this, SalesInstallationActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.sales_nav_bill_in) {
            Intent intent = new Intent(SalesMainActivity.this, BillingDetailActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.sales_nav_view_entry) {
            salesViewEntryFragment = new SalesViewEntryFragment();
            salesViewEntryFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, salesViewEntryFragment);
            setTitle("Entries");
            ft.commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.sales_nav_view_bill) {
            Intent intent = new Intent(SalesMainActivity.this, ViewBillActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.sales_nav_passwrd) {
            changePasswordFragment = new ChangePasswordFragment();
            changePasswordFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, changePasswordFragment);
            setTitle("Change Password");
            ft.commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
 else if (id == R.id.menu_logouts) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Confirm Logout")
                    .setMessage("Are you sure you want to logout ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent inteer = new Intent(SalesMainActivity.this, LoginActivityNew.class);
                            inteer.putExtra("username", "us");
                            editor.putString("pass", "");
                            appPrefs.setLoggedIn(false);
                            editor.commit();
                            startActivity(inteer);
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        return true;
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
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
            } else {
                // permission wasn't granted
            }
        }
    }
}
