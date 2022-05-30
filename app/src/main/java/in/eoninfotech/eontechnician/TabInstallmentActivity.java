package in.eoninfotech.eontechnician;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import in.eoninfotech.eontechnician.activity.LoginActivity;
import in.eoninfotech.eontechnician.fragments.NewInstallmentFragment;
import in.eoninfotech.eontechnician.fragments.OtherFragment;
import in.eoninfotech.eontechnician.fragments.RetroOldInstallmentFragment;

public class TabInstallmentActivity extends AppCompatActivity {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 1;
    String disttid;
    String uusername, version;
    Bundle bundle;
    NewInstallmentFragment newInstallFragment;
    RetroOldInstallmentFragment oldInstallmentFragment;
    OtherFragment secondOtherFragment;
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
        String data = getIntent().getExtras().getString("new","defaultKey");
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        uusername = sharedprefs.getString("s_uuser", "");
        disttid = sharedprefs.getString("s_distt", "");
        version = sharedprefs.getString("version","");
        actionBar.setTitle(data);
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
    public void onBackPressed() {

        Intent inteer = new Intent(TabInstallmentActivity.this, MainActivity.class);
        startActivity(inteer);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent inteer = new Intent(TabInstallmentActivity.this, MainActivity.class);
            startActivity(inteer);
            finish();

        } else if (id == R.id.menu_logout) {
            Intent inteer = new Intent(TabInstallmentActivity.this, LoginActivity.class);
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
            bundle.putString("version", version);
            switch (position) {
                case 0:
                    newInstallFragment = new NewInstallmentFragment();
                    newInstallFragment.setArguments(bundle);
                    return newInstallFragment;

                case 1:
                    oldInstallmentFragment = new RetroOldInstallmentFragment();
                    oldInstallmentFragment.setArguments(bundle);
                    return oldInstallmentFragment;
                case 2:
                    secondOtherFragment = new OtherFragment();
                    secondOtherFragment.setArguments(bundle);
                    return secondOtherFragment;

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
                    return "New Install";
                case 1:
                    return "Replace";
                case 2:
                    return "Other";
            }
            return null;
        }
    }
}
