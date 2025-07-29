//package in.eoninfotech.eontechnician;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toolbar;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.view.GravityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;
//
//import com.google.android.material.dialog.MaterialAlertDialogBuilder;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.navigation.NavigationView;
//import com.google.android.play.core.appupdate.AppUpdateManager;
//
//import in.eoninfotech.eontechnician.activity.LoginActivityNew;
//import in.eoninfotech.eontechnician.activity.MessageActivity;
//
//public abstract class MainActivityNew extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
//
//    private DrawerLayout drawerLayout;
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        drawerLayout = findViewById(R.id.drawer_layout);
//        NavigationView navView = findViewById(R.id.nav_view);
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//
//        NavigationUI.setupWithNavController(navView, navController);
//
//        navView.setNavigationItemSelectedListener(item -> {
//            if (item.getItemId() == R.id.dashboard) {
//                navController.navigate(R.id.viewPagerFragment);
//            }
//            drawerLayout.closeDrawer(GravityCompat.START);
//            return true;
//        });
//    }
//
//}
