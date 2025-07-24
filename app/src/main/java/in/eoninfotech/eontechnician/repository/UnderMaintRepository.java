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

public class UnderMaintRepository {

    String version;

    ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);

    private final MutableLiveData<MainResponse> umStatus = new MutableLiveData<>();
    private final MutableLiveData<MainResponse> vehicleStatus = new MutableLiveData<>();

    private static UnderMaintRepository underMaintRepository;

    public UnderMaintRepository(Application application) {

    }

    public static UnderMaintRepository getInstance(){
        if (underMaintRepository == null){
            underMaintRepository = new UnderMaintRepository(new Application());
        }
        return underMaintRepository;
    }

    public MutableLiveData<MainResponse>getUMDetails(String mainClientId, String clientId, String locId,String status){
        Call<MainResponse> listOfMovieOut = client_att.get_um_vehicle(mainClientId,clientId,locId,status);
        listOfMovieOut.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                umStatus.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                umStatus.postValue(null);
            }
        });
        return umStatus;
    }

    public MutableLiveData<MainResponse>getAddVehicleUM(String mainClientId, String clientId, String locId,String status,String reg_no,String activity_type, String activity_time, String activity_date,String device_serial,String vts_type,String contact_person, String contact_no,String tech_remarks,String technician_id){
        Call<MainResponse> listOfMovieOut = client_att.add_vehicle_um(mainClientId,clientId,locId,status,reg_no,activity_type,activity_time,activity_date,device_serial,vts_type,contact_person,contact_no,tech_remarks,technician_id);
        listOfMovieOut.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                vehicleStatus.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                vehicleStatus.postValue(null);
            }
        });
        return vehicleStatus;
    }
}
