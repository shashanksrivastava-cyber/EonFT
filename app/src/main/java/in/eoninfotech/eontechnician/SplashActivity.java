package in.eoninfotech.eontechnician;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.splashscreen.SplashScreen;
import in.eoninfotech.eontechnician.activity.LoginActivityNew;

/**
 * Created by root on 17/10/18.
 */

public class SplashActivity extends AppCompatActivity  {

    AppPreferences appPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // System splash
        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appPrefs = new AppPreferences(this);

        // Delay only for UI visibility
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (appPrefs.isLoggedIn()) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivityNew.class));
            }

            finish();

        }, 500); // 500ms is enough
    }
}
