package in.eoninfotech.eontechnician.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.responses.ClientLocationResponse;
import in.eoninfotech.eontechnician.responses.ClientResponse;
import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientLocationRepository {

    String version;
    ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);

    private final MutableLiveData<ClientLocationResponse> clientLocation = new MutableLiveData<>();

    private static ClientLocationRepository clientLocationRepository;

    public ClientLocationRepository(Application application) {

    }

    public static ClientLocationRepository getInstance(){
        if (clientLocationRepository == null){
            clientLocationRepository = new ClientLocationRepository(new Application());
        }
        return clientLocationRepository;
    }

    public MutableLiveData<ClientLocationResponse> getClientLocation(String customer,String server,String dbname) {
        Call<ClientLocationResponse> listOfMovieOut = client_att.reqeuestClientLocation(customer,server,dbname);
        listOfMovieOut.enqueue(new Callback<ClientLocationResponse>() {
            @Override
            public void onResponse(@NonNull Call<ClientLocationResponse> call, @NonNull Response<ClientLocationResponse> response) {
                clientLocation.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<ClientLocationResponse> call, @NonNull Throwable t) {
                clientLocation.postValue(null);
            }
        });
        return clientLocation;
    }

}
