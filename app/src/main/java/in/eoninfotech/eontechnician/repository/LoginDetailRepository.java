package in.eoninfotech.eontechnician.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.responses.ActivityResponse;
import in.eoninfotech.eontechnician.responses.LoginResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginDetailRepository {

    String version;
    ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);

    private final MutableLiveData<LoginResponse> loginDetails = new MutableLiveData<>();

    private static LoginDetailRepository loginDetailRepository;

    public LoginDetailRepository(Application application) {

    }

    public static LoginDetailRepository getInstance(){
        if (loginDetailRepository == null){
            loginDetailRepository = new LoginDetailRepository(new Application());
        }
        return loginDetailRepository;
    }

    public MutableLiveData<LoginResponse> getActivtyDetail(String username, String password,String imei_no,String version_name,String fcm_token) {
        Call<LoginResponse> listOfMovieOut = client_att.loginResponse(username,password,imei_no,version_name,fcm_token);
        listOfMovieOut.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                loginDetails.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                loginDetails.postValue(null);
            }
        });
        return loginDetails;
    }

}
