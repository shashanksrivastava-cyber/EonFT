package in.eoninfotech.eontechnician.activity;

import android.content.SharedPreferences;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;

import in.eoninfotech.eontechnician.Storage.LocationPrefs;


public class SendDatatoServer extends AppCompatActivity {

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

//        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
//        Call<LocationsResponse> call = log_att.locationResponse(username,address,imei);
//        call.enqueue(new Callback<LocationsResponse>() {
//            @Override
//            public void onResponse(Call<LocationsResponse> call, Response<LocationsResponse> response) {
//                if (response.body().getType() == 1) {
//                    Toast.makeText(SendDatatoServer.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(SendDatatoServer.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<LocationsResponse> call, Throwable t) {
//            }
//        });
    }
}
