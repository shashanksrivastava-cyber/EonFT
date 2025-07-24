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

public class SerialNoRepository {

    String version;
    ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);

    private final MutableLiveData<MainResponse> subClient = new MutableLiveData<>();

    private static SerialNoRepository serialNoRepository;

    public SerialNoRepository(Application application) {

    }

    public static SerialNoRepository getInstance(){
        if (serialNoRepository == null){
            serialNoRepository = new SerialNoRepository(new Application());
        }
        return serialNoRepository;
    }

    public MutableLiveData<MainResponse> getSerialNo(String tech_id,String customer) {
        Call<MainResponse> listOfMovieOut = client_att.get_cust_tech_device(tech_id,customer);
        listOfMovieOut.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                subClient.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                subClient.postValue(null);
            }
        });
        return subClient;
    }
}
