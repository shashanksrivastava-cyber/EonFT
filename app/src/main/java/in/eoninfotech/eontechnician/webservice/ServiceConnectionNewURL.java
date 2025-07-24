package in.eoninfotech.eontechnician.webservice;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by root on 12/9/18.
 */

public class ServiceConnectionNewURL {


    //public static final String BASE_URL = "http://mis.eon.co.in/eonmis/android/techApp/";
     public static final String BASE_URL = "https://mis.eurotrack.in/eonmis/android/techApp/";

     //public static final String BASE_URL_NEW = "https://mis.eurotrack.in/eonmis/android/techApp/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String version) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        AuthInterceptor authInterceptor = new AuthInterceptor(getApplicationContext());
        OkHttpClient okclient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okclient)
                    .build();
        }
        return retrofit;
    }
    public static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //AuthInterceptor authInterceptor = new AuthInterceptor(getApplicationContext());
        OkHttpClient okclient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okclient)
                    .build();
        }
        return retrofit;
    }

}

