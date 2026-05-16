package in.eoninfotech.eontechnician.helper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
/**
 * Created by Harman on 17/11/2017.
 */
/***************************************************************************/
// Copyright EON Infotech Ltd., unpublished work, created 2017.          //
// This computer program includes Confidential, Proprietary information  //
// and is a trade secret of EON Infotech Ltd. All use, disclosure and/or //
// reproduction is prohibited unless authorised in writing by an         //
// authorised officer of EON Infotech Ltd. All rights reserved.          //

/**************************************************************************/
public class GPSService implements LocationListener {

    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private boolean canGetLocation;
    double latitude = 0.0;
    double longitude = 0.0;
    long time = 0;
    private Context ctx;

    private static final long min_Distance = 0;
    private static final long min_Time = 60 * 1000 * 10;

    protected LocationManager locManager;
    Location location;
    private String TAG = "**provider";

    public GPSService() {
        getLocation();
    }

    public GPSService(Context ctx) {
        this.ctx = ctx;
        getLocation();
    }

    private Location getLocation() {
        try {

            locManager = (LocationManager) ctx
                    .getSystemService(Context.LOCATION_SERVICE);
            /*try {
                Log.d(TAG ,"Removing Test providers");
                locManager.removeTestProvider(LocationManager.GPS_PROVIDER);
            } catch (IllegalArgumentException error) {
                Log.d(TAG,"Got exception in removing test  provider");
            }
*/
            isGPSEnabled = locManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                canGetLocation = true;

                if (isGPSEnabled) {

                    if (location == null) {

                        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        }
                        locManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, min_Time,
                                min_Distance, this);
                        if (locManager != null) {

                            location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                time = location.getTime();
                                Log.e("Gps  ", latitude + " " + longitude);
                                return location;
                            }
                        }
                    }

                }
                if (isNetworkEnabled) {
                    locManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, min_Time,
                            min_Distance, this);

                    if (locManager != null) {
                        location = locManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            time = location.getTime();
                            Log.e("Network  ", latitude + " " + longitude);
                            return location;
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e + "");
        }
        return location;

    }


    public double getLatitude() {
        if (location == null) {
            getLocation();
        }
        return latitude;
    }

    public long getTime(){
        if(location == null){
            getLocation();
        }
        return time;
    }


    public double getLongitude() {
        if (location == null) {
            getLocation();
        }
        return longitude;
    }

    public boolean canGetLocation() {
        return canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        time= location.getTime();
        Log.e("Result  ", latitude + " " + longitude+ " "+ time);


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }


}
