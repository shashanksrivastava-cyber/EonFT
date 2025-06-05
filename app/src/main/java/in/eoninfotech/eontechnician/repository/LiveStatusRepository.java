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

public class LiveStatusRepository {

    String version;
    ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);

    private final MutableLiveData<MainResponse> liveStatus = new MutableLiveData<>();

    private static LiveStatusRepository liveStatusRepository;

    public LiveStatusRepository(Application application) {

    }

    public static LiveStatusRepository getInstance(){
        if (liveStatusRepository == null){
            liveStatusRepository = new LiveStatusRepository(new Application());
        }
        return liveStatusRepository;
    }

    public MutableLiveData<MainResponse> getLiveStatus(String server,String dbname,String dist_id,String depo_id,String status,String type) {
        Call<MainResponse> listOfMovieOut = client_att.get_live_status(server,dbname,dist_id,depo_id,status,type);
        listOfMovieOut.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                liveStatus.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                liveStatus.postValue(null);
            }
        });
        return liveStatus;
    }


    public MutableLiveData<MainResponse> getLiveStatusReport(String server,String dbname,String dist_id,String depo_id,String status,String type,String veh_serial_no) {
        Call<MainResponse> listOfMovieOut = client_att.get_live_status_report(server,dbname,dist_id,depo_id,status,type,veh_serial_no);
        listOfMovieOut.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                liveStatus.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                liveStatus.postValue(null);
            }
        });
        return liveStatus;
    }

    public MutableLiveData<MainResponse> getLiveDrumReport(String veh_no,String server,String dbname) {
        Call<MainResponse> listOfMovieOut = client_att.get_live_drum_report(veh_no,server,dbname);
        listOfMovieOut.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                liveStatus.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                liveStatus.postValue(null);
            }
        });
        return liveStatus;
    }
}
