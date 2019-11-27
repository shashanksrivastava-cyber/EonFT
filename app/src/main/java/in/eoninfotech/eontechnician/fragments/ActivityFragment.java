package in.eoninfotech.eontechnician.fragments;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import in.eoninfotech.eontechnician.R;

/**
 * Created by root on 4/3/19.
 */

public class ActivityFragment extends Fragment {

    View v;
    public static TabLayout tabLayout;
    public static int int_items = 2;
    private ViewPager viewPager;
    String tab = "1";
    Bundle bundle;
    String usrname,versionname, disgnid = "0";
    NewInstallmentFragment newInstallmentFragment;
    ActivityDetailFragment activityDetailFragment;
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
                    newInstallmentFragment = new NewInstallmentFragment();
                    newInstallmentFragment.setArguments(bundle);
                    return newInstallmentFragment;
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
}
