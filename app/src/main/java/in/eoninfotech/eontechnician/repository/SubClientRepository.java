package in.eoninfotech.eontechnician.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.responses.ClientResponse;
import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubClientRepository {

    String version;
    ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);

    private final MutableLiveData<ClientResponse> subClient = new MutableLiveData<>();

    private static SubClientRepository subClientRepository;

    public SubClientRepository(Application application) {

    }

    public static SubClientRepository getInstance(){
        if (subClientRepository == null){
            subClientRepository = new SubClientRepository(new Application());
        }
        return subClientRepository;
    }

    public MutableLiveData<ClientResponse> getSubClient(String c_id) {
        Call<ClientResponse> listOfMovieOut = client_att.reqeuestClientList(c_id);
        listOfMovieOut.enqueue(new Callback<ClientResponse>() {
            @Override
            public void onResponse(@NonNull Call<ClientResponse> call, @NonNull Response<ClientResponse> response) {
                subClient.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<ClientResponse> call, @NonNull Throwable t) {
                subClient.postValue(null);
            }
        });
        return subClient;
    }
}
