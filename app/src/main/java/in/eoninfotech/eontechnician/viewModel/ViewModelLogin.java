package in.eoninfotech.eontechnician.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.repository.LoginDetailRepository;
import in.eoninfotech.eontechnician.responses.ActivityResponse;
import in.eoninfotech.eontechnician.responses.LoginResponse;

public class ViewModelLogin extends AndroidViewModel {

    private final LoginDetailRepository repository;
    public ViewModelLogin(@NonNull Application application, LoginDetailRepository repository) {
        super(application);
        this.repository = repository;
    }

    private MutableLiveData<LoginResponse> loginResponse = new MutableLiveData<>();

    public ViewModelLogin(@NonNull Application application) {
        super(application);
        this.repository = new LoginDetailRepository(application);
    }

    public MutableLiveData<LoginResponse> getLoginResponse(String username, String password,String imei_no,String version_name,String fcm_token) {
        loginResponse = getLoginDetails(username,password,imei_no,version_name,fcm_token);
        return loginResponse;
    }

    private MutableLiveData<LoginResponse> getLoginDetails(String username, String password,String imei_no,String version_name,String fcm_token) {
        return repository.getActivtyDetail(username,password,imei_no,version_name,fcm_token);
    }
}
