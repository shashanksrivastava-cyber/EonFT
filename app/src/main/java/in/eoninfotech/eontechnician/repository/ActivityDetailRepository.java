package in.eoninfotech.eontechnician.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.responses.ActivityResponse;
import in.eoninfotech.eontechnician.responses.ClientLocationResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDetailRepository {

    String version;
    ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);

    private final MutableLiveData<ActivityResponse> activityDetail = new MutableLiveData<>();

    private static ActivityDetailRepository activityDetailRepository;

    public ActivityDetailRepository(Application application) {

    }

    public static ActivityDetailRepository getInstance(){
        if (activityDetailRepository == null){
            activityDetailRepository = new ActivityDetailRepository(new Application());
        }
        return activityDetailRepository;
    }

    public MutableLiveData<ActivityResponse> getActivtyDetail(String date, String tech_id) {
        Call<ActivityResponse> listOfMovieOut = client_att.view_activities(date,tech_id);
        listOfMovieOut.enqueue(new Callback<ActivityResponse>() {
            @Override
            public void onResponse(@NonNull Call<ActivityResponse> call, @NonNull Response<ActivityResponse> response) {
                activityDetail.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<ActivityResponse> call, @NonNull Throwable t) {
                activityDetail.postValue(null);
            }
        });
        return activityDetail;
    }


}
