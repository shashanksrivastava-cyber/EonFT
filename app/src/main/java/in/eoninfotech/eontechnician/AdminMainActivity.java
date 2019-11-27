package in.eoninfotech.eontechnician;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import in.eoninfotech.eontechnician.activity.LoginActivity;
import in.eoninfotech.eontechnician.adminuser.ChangePasswordFragment;
import in.eoninfotech.eontechnician.adminuser.DashboardFragment;
import in.eoninfotech.eontechnician.adminuser.PendencyFragment;
import in.eoninfotech.eontechnician.adminuser.RequirementFragment;
import in.eoninfotech.eontechnician.fragments.ViewActivityLogsFragment;
import in.eoninfotech.eontechnician.helper.ClientList;
import in.eoninfotech.eontechnician.helper.EONUtil;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.fragments.AdminCallSheetListFragment;
import in.eoninfotech.eontechnician.fragments.AdminCriticalSitesFragment;
import in.eoninfotech.eontechnician.fragments.AdminIncentiveFragment;
import in.eoninfotech.eontechnician.fragments.AdminShowNextDayPlan;
import in.eoninfotech.eontechnician.fragments.AdminStockFragment;

/**
 * Created by root on 12/10/17.
 */
public class AdminMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fm;
    FragmentTransaction ft;
    Toolbar toolbar;
    ArrayList<ClientList> ClientLists = new ArrayList<ClientList>();
    AppPreferences appPrefs;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String usrname, alertt, uusername, versionname, disgnid = "0";
    NavigationView navigationView;
    Bundle bundle;
    AdminCriticalSitesFragment adminCriticalSitesFragment;
    AdminCallSheetListFragment adminCallSheetListFragment;
    AdminIncentiveFragment adminIncentiveFragment;
    AdminStockFragment adminStockFragment;
    AdminShowNextDayPlan adminShowNextDayPlan;
    DashboardFragment dashboardFragment;
    ChangePasswordFragment changePasswordFragment;
    PendencyFragment pendencyFragment;
    RequirementFragment requirementFragment;
    ViewActivityLogsFragment viewActivityLogsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Mark Activity");
        appPrefs = new AppPreferences(getApplicationContext());
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        uusername = sharedprefs.getString("s_uuser", "");
        versionname = sharedprefs.getString("version", "");
        Log.i("******shared******", uusername + versionname);
        alertt = sharedprefs.getString("alert", "");
        usrname = sharedprefs.getString("dis_user", "");
        disgnid = sharedprefs.getString("s_distt", "");
        fm = getSupportFragmentManager();
        bundle = new Bundle();
        bundle.putString("disttid", disgnid);
        bundle.putString("usernme", uusername);
        bundle.putString("version", versionname);

        dashboardFragment = new DashboardFragment();
        dashboardFragment.setArguments(bundle);
        ft = fm.beginTransaction().add(R.id.framelay, dashboardFragment);
        ft.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView headr_usrnam = (TextView) view.findViewById(R.id.header_username);
        headr_usrnam.setText(usrname.toUpperCase());
        if (disgnid.equals("0")) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_new_repair).setVisible(false);
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
        if (id == R.id.menu_logout) {
            Intent inteer = new Intent(AdminMainActivity.this, LoginActivity.class);
            inteer.putExtra("username", "us");
            editor.putString("pass", "");
            editor.commit();
            startActivity(inteer);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_incentive) {
            adminIncentiveFragment = new AdminIncentiveFragment();
            adminIncentiveFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, adminIncentiveFragment);
            setTitle("Incentive");
        } else if (id == R.id.nav_critical_sites) {
            try {
                adminCriticalSitesFragment = new AdminCriticalSitesFragment();
                adminCriticalSitesFragment.setArguments(bundle);
                ft = fm.beginTransaction().replace(R.id.framelay, adminCriticalSitesFragment);
                setTitle("Critical Sites");
            } catch (IllegalStateException ise) {
                ise.printStackTrace();
            }
        } else if (id == R.id.nav_stock) {
            adminStockFragment = new AdminStockFragment();
            adminStockFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, adminStockFragment);
            setTitle("Stock");
        } else if (id == R.id.nav_mark_site) {
            dashboardFragment = new DashboardFragment();
            dashboardFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, dashboardFragment);
            setTitle("Mark Activity");
            ft.commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
//        else if (id == R.id.nav_view_activity) {
//            viewActivityLogsFragment = new ViewActivityLogsFragment();
//            viewActivityLogsFragment.setArguments(bundle);
//            ft = fm.beginTransaction().replace(R.id.framelay, viewActivityLogsFragment);
//            setTitle("Select Date");
//            ft.commit();
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
      //  }
        else if (id == R.id.nav_call_sheet) {
            adminCallSheetListFragment = new AdminCallSheetListFragment();
            adminCallSheetListFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, adminCallSheetListFragment);
            setTitle("Call Sheet");
        }
        else if (id == R.id.nav_update_client_list) {
            new UpdateClientList().execute("abc");
        }
        else if (id == R.id.nav_next_plan) {
            adminShowNextDayPlan = new AdminShowNextDayPlan();
            adminShowNextDayPlan.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, adminShowNextDayPlan);
            setTitle("Next Day Plan");
        }
        else if (id == R.id.nav_dashboard) {
            dashboardFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, dashboardFragment);
            setTitle("Dashboard");
        }
        else if (id == R.id.nav_pendency) {
            pendencyFragment = new PendencyFragment();
            pendencyFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, dashboardFragment);
            setTitle("Pendency Detail");
        }
        else if (id == R.id.nav_requirement) {
             requirementFragment = new RequirementFragment();
            requirementFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, requirementFragment);
            setTitle("Requirement Detail");
        }
        else if (id == R.id.nav_passwrd) {
            changePasswordFragment = new ChangePasswordFragment();
            changePasswordFragment.setArguments(bundle);
            ft = fm.beginTransaction().replace(R.id.framelay, changePasswordFragment);
            setTitle("Change Password");
        }
        try {
            ft.commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    class UpdateClientList extends AsyncTask<String, Void, Void> {
        ProgressDialog pDialog;
        URL url;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = new ProgressDialog(AdminMainActivity.this);
            pDialog.setMessage("Updating list...");
            pDialog.setCancelable(false);
            pDialog.show();
            deleteRecord();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                String data = K.Url.urlkey;
                ClientLists.clear();
                byte[] encodedd = Base64.encode(data.getBytes("UTF-8"), Base64.DEFAULT);
                String str1 = new String(encodedd, "UTF-8");
                Log.i("***urlen****", K.Url.getclients + str1);
                url = new URL(K.Url.getclients + str1);
                DocumentBuilderFactory builder = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = builder.newDocumentBuilder();
                Document doc = documentBuilder.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                NodeList nl = doc.getElementsByTagName("master");
                Element ea = (Element) nl.item(0);
                nl = ea.getElementsByTagName("client");
                for (int i = 0; i < nl.getLength(); i++) {
                    ea = (Element) nl.item(i);
                    ClientList s = new ClientList();
                    s.setClientid(K.getNode("clid", ea));
                    s.setClientname(K.getNode("clname", ea).trim());
                    s.setDrs_status(K.getNode("drs_status", ea));
                    ClientLists.add(s);
                }
                Log.i("***stop size***", String.valueOf(ClientLists.size()));
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
            if (ClientLists.size() > 0) {
                addRecord();
                Toast.makeText(AdminMainActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AdminMainActivity.this, "try again please", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addRecord() {
        int i = 0;
        Log.i("***stop size***", String.valueOf(ClientLists.size()));
        for (; i < ClientLists.size(); i++) {
            EONUtil.insertStationData(this, i + 1, ClientLists.get(i).getClientid() + ":" + ClientLists.get(i).getDrs_status(), ClientLists.get(i).getClientname());
        }
        if (i == ClientLists.size()) {
            appPrefs.saveProviderInfo("true");
        }
    }

    public void deleteRecord() {
        EONUtil.deleteStationData(this);
        appPrefs.saveProviderInfo("false");
    }

}