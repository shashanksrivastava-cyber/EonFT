package in.eoninfotech.eontechnician;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import in.eoninfotech.eontechnician.helper.GPSService;
import in.eoninfotech.eontechnician.helper.Location_prop;

/**
 * Created by root on 22/5/19.
 */

public class GetLocation {

    public static Location_prop getCurrentLocation(final Service act) {
        final GPSService getLoc = new GPSService(act);
        final Location_prop p = new Location_prop();
        if (getLoc.canGetLocation()) {
            p.latitude = getLoc.getLatitude();
            p.longitude = getLoc.getLongitude();
            p.time = getLoc.getTime();
            // getLoc.get
            p.status=1;
            /* Toast.makeText(act, "Location Fetched",
                    Toast.LENGTH_LONG).show();*/
            //getLoc.stopLocationUpdates();
        } else {
            p.status =0;
            Toast.makeText(act, "Please turn on GPS and Network Settings",
                    Toast.LENGTH_LONG).show();
        }
        return p;
    }
    public static String getAddressfromLatLong(Activity act, String lati, String longi) {
        String full_address = "";
        try {
            Geocoder geo = new Geocoder(act, Locale.ENGLISH);
            double latitude = Double.parseDouble(lati);
            double longitude = Double.parseDouble(longi);

            if (latitude != 0.0 && longitude != 0.0) {
                List<Address> li = geo.getFromLocation(latitude, longitude, 1);
                if (li != null) {
                    Address ad = li.get(0);
                    for (int i = 0; i < ad.getMaxAddressLineIndex(); i++) {
                        full_address += ad.getAddressLine(i) + ", ";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (full_address.length() > 0) {
            return full_address.substring(0, full_address.length() - 2);
        } else {
            return "No Address Found";
        }
    }

    public static String getStateName(Context act, double latitude, double longitude) {
        String address = "";
        try {
            Geocoder geo = new Geocoder(act, Locale.ENGLISH);
            if (latitude != 0.0 && longitude != 0.0) {
                List<Address> li = geo.getFromLocation(latitude, longitude, 1);
                if (li != null) {
                    address = li.get(0).getAdminArea();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (address.length() > 0) {
            return address;
        } else {
            return "";
        }
    }

    public static double getDistance(double start_latitude, double start_longitude, double end_latitude, double end_longitude) {

        Location loc_start = new Location("");
        loc_start.setLatitude(start_latitude);
        loc_start.setLongitude(start_longitude);

        Location loc_end = new Location("");
        loc_end.setLatitude(end_latitude);
        loc_end.setLongitude(end_longitude);

        return loc_start.distanceTo(loc_end);
    }

    public static String getAddress(Activity act, double lat, double lng) {
        String str = "No Address Found";
        Geocoder geocoder = new Geocoder(act, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);

            Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();
            str = add;
            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            str = "No Address Found";
            // Toast.makeText(act, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return str;
    }
}
