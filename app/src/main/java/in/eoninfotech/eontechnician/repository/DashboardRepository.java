package in.eoninfotech.eontechnician.repository;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRepository {

    String version;
    ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
    private final MutableLiveData<MainResponse> dashboardCounts = new MutableLiveData<>();

    private static DashboardRepository dashboardRepository;

    public DashboardRepository(Application application) {

    }

    public static DashboardRepository getInstance(){
        if (dashboardRepository == null){
            dashboardRepository = new DashboardRepository(new Application());
        }
        return dashboardRepository;
    }

    public MutableLiveData<MainResponse> getDashboardCount(String username) {
        Call<MainResponse> listOfMovieOut = client_att.get_live_device_count(username);
        listOfMovieOut.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                dashboardCounts.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                dashboardCounts.postValue(null);
            }
        });
        return dashboardCounts;
    }
}
