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

public class MainClientRepository {

    String version;
    ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);

    private final MutableLiveData<MainResponse> mainClient = new MutableLiveData<>();

    private static MainClientRepository mainClientRepository;

    public MainClientRepository(Application application) {

    }

    public static MainClientRepository getInstance(){
        if (mainClientRepository == null){
            mainClientRepository = new MainClientRepository(new Application());
        }
        return mainClientRepository;
    }

    public MutableLiveData<MainResponse> getMainClient() {
        Call<MainResponse> listOfMovieOut = client_att.reqeuestMainClientList();
        listOfMovieOut.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                mainClient.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                mainClient.postValue(null);
            }
        });
        return mainClient;
    }
}
