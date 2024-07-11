package in.eoninfotech.eontechnician.storage;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Harsh on 04-12-2017.
 */

public final class LocationPrefs {

    private static final String LAST_KNOWN_LAT = "last_known_lat";
    private static final String LAST_KNOWN_LNG = "last_known_lng";
    private static final String LAST_LOC = "last_loc";

    private SharedPreferences prefs;

    public LocationPrefs(final Context context) {
        prefs = context.getSharedPreferences(LocationPrefs.class.getCanonicalName(), Context.MODE_PRIVATE);
    }
    public SharedPreferences getPrefs() {
        return prefs;
    }

    public double getLastKnownLat() {
        return Double.parseDouble(prefs.getString(LAST_KNOWN_LAT, ""));
    }
    public Double setLastKnownLat(double latitude) {
        prefs.edit().putString(LAST_KNOWN_LAT, String.valueOf(latitude)).commit();
        return null;
    }
    public Double setLastKnownLng(double longitude) {
        prefs.edit().putString(LAST_KNOWN_LNG, String.valueOf(longitude)).commit();
        return null;
    }
    public double getlastKownLng() {
        return Double.parseDouble(prefs.getString(LAST_KNOWN_LNG, ""));
    }

    public String setLastLoc(String  address) {
        prefs.edit().putString(LAST_LOC, String.valueOf(address)).commit();
        return null;
    }
    public String getlastLoc() {
        return new String(prefs.getString(LAST_LOC, ""));
    }


}
