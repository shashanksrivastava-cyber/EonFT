package in.eoninfotech.eontechnician;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 27/5/19.
 */

public class GpsLocationReceiver extends BroadcastReceiver {

    boolean isGpsEnabled;
    boolean isNetworkEnabled;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
//           {
            Log.i(TAG, "Location Providers changed");

            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            //Start your Activity if location was enabled:
            if (isGpsEnabled || isNetworkEnabled) {
//                Intent i = new Intent(context, MainActivity.class);
//                context.startActivity(i);
            }
        }
    }
    }

