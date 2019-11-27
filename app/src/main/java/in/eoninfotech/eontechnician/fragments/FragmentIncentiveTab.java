package in.eoninfotech.eontechnician.fragments;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.fragments.FragmentAddNextDayPlan;
import in.eoninfotech.eontechnician.fragments.FragmentCurrentMonth;
import in.eoninfotech.eontechnician.fragments.FragmentLastMonth;
import in.eoninfotech.eontechnician.fragments.ShowNextDayPlanFragment;

public class FragmentIncentiveTab extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2;
    String disttid;
    String uusername, version;
    Bundle bundle;
    FragmentCurrentMonth fragmentCurrentMonth;
    FragmentLastMonth fragmentLastMonth;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x =  inflater.inflate(R.layout.activity_install_tab_layout,null);
        tabLayout = x.findViewById(R.id.tabs);
        viewPager = x.findViewById(R.id.viewpager);

        disttid = getArguments().getString("disttid");
        uusername = getArguments().getString("usernme");
        version = getArguments().getString("version");
        Log.i("****tab dis n usr***", disttid + " " + uusername);

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;
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
            bundle.putString("version",version);
            switch (position) {
                case 0:
                    fragmentCurrentMonth = new FragmentCurrentMonth();
                    fragmentCurrentMonth.setArguments(bundle);
                    return fragmentCurrentMonth;
                case 1:
                    fragmentLastMonth = new FragmentLastMonth();
                    fragmentLastMonth.setArguments(bundle);
                    return fragmentLastMonth;
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
                    return "Current Month";
                case 1:
                    return "Last Month";
            }
            return null;
        }
    }
}
