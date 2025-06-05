package in.eoninfotech.eontechnician.webservice;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/***************************************************************************/
// Copyright EON Infotech Ltd., published work, created 2017.          //
// This computer program includes Confidential, Proprietary information  //
// and is a trade secret of EON Infotech Ltd. All use, disclosure and/or //
// reproduction is prohibited unless authorised in writing by an         //
// authorised officer of EON Infotech Ltd. All rights reserved.          //

/**************************************************************************/
/**
 * Created by Harman on 9/10/2017.
 */
public class ServiceConnection {
    //public static final String BASE_URL = "http://mis.eon.co.in/eonmis/android/techApp/";
    public static final String BASE_URL = "http://mis.eurotrack.in/eonmis/android/techApp/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient(String version) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okclient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.MINUTES)
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


    public static Retrofit getClients(String version){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.MINUTES)
                .readTimeout(10,TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }

        return  retrofit;

    }
}
