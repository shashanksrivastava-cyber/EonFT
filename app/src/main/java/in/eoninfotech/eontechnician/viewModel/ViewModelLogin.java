package in.eoninfotech.eontechnician.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.repository.LoginDetailRepository;
import in.eoninfotech.eontechnician.responses.ActivityResponse;
import in.eoninfotech.eontechnician.responses.LoginResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//public class ViewModelLogin extends AndroidViewModel {
//
//    private final LoginDetailRepository repository;
//    public ViewModelLogin(@NonNull Application application, LoginDetailRepository repository) {
//        super(application);
//        this.repository = repository;
//    }
//
//    private MutableLiveData<LoginResponse> loginResponse = new MutableLiveData<>();
//
//    public ViewModelLogin(@NonNull Application application) {
//        super(application);
//        this.repository = new LoginDetailRepository(application);
//    }
//
//    public MutableLiveData<LoginResponse> getLoginResponse(String username, String password,String imei_no,String version_name,String fcm_token) {
//        loginResponse = getLoginDetails(username,password,imei_no,version_name,fcm_token);
//        return loginResponse;
//    }
//
//    private MutableLiveData<LoginResponse> getLoginDetails(String username, String password,String imei_no,String version_name,String fcm_token) {
//        return repository.getActivtyDetail(username,password,imei_no,version_name,fcm_token);
//    }
//}
public class ViewModelLogin extends AndroidViewModel {

    private final LoginDetailRepository repository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ViewModelLogin(@NonNull Application application) {
        super(application);
        this.repository = new LoginDetailRepository(application);
    }

    // ✅ Expose login method returning a Single
    public void login(String username, String password, String imei_no, String version_name, String fcm_token,
                      LoginCallback callback) {

        // success
        // error
        Disposable disposable = repository.getActivityDetail(username, password, imei_no, version_name, fcm_token)
                .subscribeOn(Schedulers.io())                 // run on background
                .observeOn(AndroidSchedulers.mainThread())    // observe on UI thread
                .subscribe(
                        callback::onSuccess,
                        callback::onError
                );

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear(); // cleanup to avoid memory leaks
    }

    // ✅ callback interface to send result to UI
    public interface LoginCallback {
        void onSuccess(LoginResponse response);
        void onError(Throwable throwable);
    }
}