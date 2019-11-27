package in.eoninfotech.eontechnician.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.eoninfotech.eontechnician.R;

/**
 * Created by root on 4/3/19.
 */

public class ViewMarkAttandenceFragment extends Fragment {

    View v;
    String username, dist_id, version;
    public static TabLayout tabLayout;
    public static int int_items = 2;
    private ViewPager viewPager;
    String tab = "1";
    Bundle bundle;
    String usrname,versionname, disgnid = "0";
    ActivityLogFragment activityLogFragment;
    ViewActivityLogsFragment viewActivityLogsFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.activity_fragment, container, false);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);

        username = getArguments().getString("usernme");
        dist_id = getArguments().getString("disttid");
        version = getArguments().getString("version");

        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        return v;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            bundle = new Bundle();
            bundle.putString("disttid", disgnid);
            bundle.putString("usernme", username);
            bundle.putString("version", version);

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
                    return "Upload";
                case 1:
                    return "View";
            }
            return null;
        }
    }
}
