package in.eoninfotech.eontechnician.service;

import android.content.Context;
import android.content.SharedPreferences;

import in.eoninfotech.eontechnician.AppPreferences;

public class SessionManager {

    public static void clearSession(Context context) {
        SharedPreferences sp = context.getSharedPreferences("login_user", Context.MODE_PRIVATE);
        sp.edit().clear().apply();

        try {
            AppPreferences appPrefs = new AppPreferences(context.getApplicationContext());
            appPrefs.setLoggedIn(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
