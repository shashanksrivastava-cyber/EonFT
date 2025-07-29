//package in.eoninfotech.eontechnician.adapters;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.viewpager2.adapter.FragmentStateAdapter;
//
//import in.eoninfotech.eontechnician.fragments.DashBoardFragment;
//import in.eoninfotech.eontechnician.fragments.OtherDashBoardFragment;
//
//public class ViewPagerAdapter extends FragmentStateAdapter {
//
//    public ViewPagerAdapter(@NonNull Fragment fragment) {
//        super(fragment);
//    }
//
//    @NonNull
//    @Override
//    public Fragment createFragment(int position) {
//        switch (position) {
//            case 0:
//                return new DashBoardFragment();
//            case 1:
//                return new OtherDashBoardFragment();
//            default:
//                throw new IllegalArgumentException("Invalid position: " + position);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return 2;
//    }
//}
