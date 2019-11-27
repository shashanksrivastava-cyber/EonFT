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
 * Created by root on 6/3/19.
 */

public class DashBoardMainFragment extends Fragment {

    View v;
    public static TabLayout tabLayout;
    public static int int_items = 2;
    private ViewPager viewPager;
    String tab = "1";
    Bundle bundle;
    String usrname,versionname, disgnid = "0";
    DashBoardFragment dashBoardFragment;
    OtherDashBoardFragment otherDashBoardFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.activity_fragment, container, false);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);

        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */
//        tabLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                tabLayout.setupWithViewPager(viewPager);
//            }
//        });

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
                    return "My DashBoard";
                case 1:
                    return "Other's DashBoard";
            }
            return null;
        }
    }
}
