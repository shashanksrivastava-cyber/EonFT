package in.eoninfotech.eontechnician.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.responses.DashBoardResponse;
import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDashboardRepository {

    String version;
    ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);

    private final MutableLiveData<DashBoardResponse> addCounts = new MutableLiveData<>();

    private static AddDashboardRepository addDashboardRepository;

    public AddDashboardRepository(Application application) {

    }

    public static AddDashboardRepository getInstance(){
        if (addDashboardRepository == null){
            addDashboardRepository = new AddDashboardRepository(new Application());
        }
        return addDashboardRepository;
    }

    public MutableLiveData<DashBoardResponse> getAddCounts(String zone) {
        Call<DashBoardResponse> listOfMovieOut = client_att.dashBoardResponse(zone);
        listOfMovieOut.enqueue(new Callback<DashBoardResponse>() {
            @Override
            public void onResponse(@NonNull Call<DashBoardResponse> call, @NonNull Response<DashBoardResponse> response) {
                addCounts.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<DashBoardResponse> call, @NonNull Throwable t) {
                addCounts.postValue(null);
            }
        });
        return addCounts;
    }

}
