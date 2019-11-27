package in.eoninfotech.eontechnician.fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.eoninfotech.eontechnician.AppPreferences;
import in.eoninfotech.eontechnician.MainActivity;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.TechnicianMonthDetail;
import in.eoninfotech.eontechnician.helper.ClientList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by root on 13/11/18.
 */

public class HomeFragment extends Fragment {

    View v;
    ArrayList<ClientList> ClientLists = new ArrayList<ClientList>();
    AppPreferences appPrefs;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String usrname, alertt, uusername, versionname, disgnid = "0", activityName = "Activities";
    Bundle bundle;
    DashBoardFragment dashBoardFragment;
    OtherDashBoardFragment otherDashBoardFragment;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    int REQUEST_CODE_PERMISSION = 1001;
    DownloadManager manager;
    public static final String downloadMp3Url = "http://mail.cybernetra.net:8080/android/eonApp/2.7.1/song.mp3";
    MediaPlayer mp = new MediaPlayer();
    private Menu menu;
    Dialog myDialog;
    TextView txtclose, month, name, loc, viewMore;
    ArrayList<TechnicianMonthDetail> techList = new ArrayList<>();
    public static TabLayout tabLayout;
    public static int int_items = 2;
    private ViewPager viewPager;
    String tab = "1";
    ViewPagerAdapter viewPagerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.home_fragment, container, false);

        appPrefs = new AppPreferences(getActivity());
        sharedprefs = getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        uusername = sharedprefs.getString("s_uuser", "");
        versionname = sharedprefs.getString("version", "");
        Log.i("******shared******", uusername + versionname);
        alertt = sharedprefs.getString("alert", "");
        usrname = sharedprefs.getString("s_uuser", "");
        disgnid = sharedprefs.getString("s_distt", "");
        bundle = new Bundle();
        bundle.putString("disttid", disgnid);
        bundle.putString("usernme", usrname);
        bundle.putString("version", versionname);
        Log.i("******shareds******", usrname + versionname);

        myDialog = new Dialog(getActivity());
        Intent intent = getActivity().getIntent();
        tab = intent.getStringExtra("tab");
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
//        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
        return v;
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
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
                    return "My DashBoard";
                case 1:
                    return "Other DashBoard";
            }
            return null;
        }
    }
}
