//package in.eoninfotech.eontechnician.fragments;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.viewpager2.widget.ViewPager2;
//
//import in.eoninfotech.eontechnician.MainActivity;
//import in.eoninfotech.eontechnician.R;
//import in.eoninfotech.eontechnician.adapters.ViewPagerAdapter;
//
//public class ViewPagerFragment extends Fragment {
//
//    private ViewPager2 viewPager;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_view_pager, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view,
//                              @Nullable Bundle savedInstanceState) {
//        viewPager = view.findViewById(R.id.view_pager);
//        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
//        viewPager.setAdapter(adapter);
//    }
//}
