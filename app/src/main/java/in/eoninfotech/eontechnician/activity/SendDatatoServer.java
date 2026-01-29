package in.eoninfotech.eontechnician.activity;

import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import in.eoninfotech.eontechnician.storage.LocationPrefs;


public class SendDatatoServer extends AppCompatActivity  {

    LocationPrefs locationPrefs;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String address;
    String username,fullUserName, version,imei,imei1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        fullUserName = sharedprefs.getString("dis_user","");
        imei = sharedprefs.getString("imei1","");
        imei1 = sharedprefs.getString("imei2","");

        sendData();

        Double lat = (locationPrefs.getLastKnownLat());
        Double lng = (locationPrefs.getlastKownLng());
        Geocoder geocoder;
        geocoder = new Geocoder(SendDatatoServer.this, Locale.getDefault());

        try {
            String addresses = String.valueOf(geocoder.getFromLocation(lat, lng, 1));

            String[] separated = addresses.split("=");

            String abc = separated[1];

            String[] separate = abc.split(":");

            String def = separate[1];

            String[] separat = def.split("]");

            address = separat[0];

            Log.e("", "address" + address);

            // Toast.makeText(this, ""+address, Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendData() {

    }
}
