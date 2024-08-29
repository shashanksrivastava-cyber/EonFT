package in.eoninfotech.eontechnician.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountDetailRepository {

    String version;
    ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
    private final MutableLiveData<MainResponse> countDetails = new MutableLiveData<>();

    private static CountDetailRepository countDetailRepository;

    public CountDetailRepository(Application application) {

    }
    public static CountDetailRepository getInstance(){
        if (countDetailRepository == null){
            countDetailRepository = new CountDetailRepository(new Application());
        }
        return countDetailRepository;
    }

    public MutableLiveData<MainResponse> getCountDetails(String username,String status,String customer) {
        Call<MainResponse> listOfMovieOut = client_att.get_live_device_count_details(username,status,customer);
        listOfMovieOut.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                countDetails.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                countDetails.postValue(null);
            }
        });
        return countDetails;
    }
}
