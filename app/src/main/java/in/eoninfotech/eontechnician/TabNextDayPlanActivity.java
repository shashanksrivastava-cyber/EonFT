package in.eoninfotech.eontechnician;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import in.eoninfotech.eontechnician.activity.LoginActivity;

public class TabNextDayPlanActivity extends AppCompatActivity {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2;
    String uusername, version,disttid;
    Bundle bundle;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_tab_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        uusername = sharedprefs.getString("dis_user", "");
        disttid = sharedprefs.getString("s_distt", "");
        version = sharedprefs.getString("version", "");
        actionBar.setTitle("Tomorrow Plan");
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
                case 1:
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
