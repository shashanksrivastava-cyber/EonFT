package in.eoninfotech.eontechnician.webservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.IOException;

import in.eoninfotech.eontechnician.activity.LoginActivityNew;
import okhttp3.Interceptor;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        if (response.code() == 401) {
            // Clear SharedPreferences
            SharedPreferences prefs = context.getSharedPreferences("your_pref_name", Context.MODE_PRIVATE);
            prefs.edit().clear().apply();

            // Redirect to LoginActivityNew
            Intent intent = new Intent(context, LoginActivityNew.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }

        return response;
    }
}
