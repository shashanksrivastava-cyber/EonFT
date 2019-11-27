package in.eoninfotech.eontechnician;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import in.eoninfotech.eontechnician.activity.LoginActivity;
import in.eoninfotech.eontechnician.fragments.FragmentAddNextDayPlan;
import in.eoninfotech.eontechnician.fragments.ShowNextDayPlanFragment;

public class TabNextDayPlanActivity extends AppCompatActivity {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2;
    String disttid;
    String uusername, version;
    Bundle bundle;
    FragmentAddNextDayPlan fragmentAddNextDayPlan;
    ShowNextDayPlanFragment showNextDayPlanFragment;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_tab_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        uusername = sharedprefs.getString("dis_user", "");
        disttid = sharedprefs.getString("s_distt", "");
        version = sharedprefs.getString("version", "");
        actionBar.setTitle("Tomorrow Plan");
        Log.i("****tab dis n usr***", disttid + " " + uusername);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent inteer = new Intent(TabNextDayPlanActivity.this, MainActivity.class);
            startActivity(inteer);
            finish();
        } else if (id == R.id.menu_logout) {
            Intent inteer = new Intent(TabNextDayPlanActivity.this, LoginActivity.class);
            inteer.putExtra("username", uusername);
            editor.putString("pass", "");
            editor.commit();
            startActivity(inteer);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            bundle = new Bundle();
            bundle.putString("disttid", disttid);
            bundle.putString("usernme", uusername);

            switch (position) {
                case 0:
                    fragmentAddNextDayPlan = new FragmentAddNextDayPlan();
                    fragmentAddNextDayPlan.setArguments(bundle);
                    return fragmentAddNextDayPlan;
                case 1:
                    showNextDayPlanFragment = new ShowNextDayPlanFragment();
                    showNextDayPlanFragment.setArguments(bundle);
                    return showNextDayPlanFragment;
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
                    return "Add Plan";
                case 1:
                    return "Plan Detail";
            }
            return null;
        }
    }
}
