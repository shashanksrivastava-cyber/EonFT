package in.eoninfotech.eontechnician.repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainClientRepository {

    private final MutableLiveData<MainResponse> mainClient = new MutableLiveData<>();
    private final ApiHolder clientApi;
    private boolean isDataLoaded = false;

    public MainClientRepository(@NonNull Application application) {
        SharedPreferences prefs = application.getSharedPreferences("login_user_pass", Context.MODE_PRIVATE);
        String version = prefs.getString("version", ""); // default empty if not set
        clientApi = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
    }

    public LiveData<MainResponse> getMainClient() {
        if (!isDataLoaded) {
            fetchMainClientFromApi();
        }
        return mainClient;
    }

    private void fetchMainClientFromApi() {
        Call<MainResponse> call = clientApi.reqeuestMainClientList();
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                if (response.isSuccessful()) {
                    mainClient.setValue(response.body());
                    isDataLoaded = true;
                } else {
                    mainClient.setValue(null);
                    isDataLoaded = false;
                }
            }
            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                mainClient.postValue(null);
                isDataLoaded = false;
            }
        });
    }
}
